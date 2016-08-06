(ns bog.sync
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [bog.tokens :refer [get-token]]
            [bog.app-state :refer [app-state]]))

(defn build-args [method url & {:keys [headers body]}]
  {:url url
   :method method
   :opts {:headers (or headers {})
          :json-params (or body {})}})

(defn server-fn [method]
  (condp = method
    :get http/get
    :post http/post
    :put http/put
    :delete http/delete))

(defn get-auth-header! [auth]
  (if (= auth true)
    {"Authorization" (get-token)}
    {}))

(defn sync! [req path & {:keys [auth]}]
  (go
    (let [auth-header (get-auth-header! auth)
          {:keys [url method opts]} req
          auth-opts (assoc-in opts [:headers] auth-header)
          {:keys [status body]} (<! ((server-fn method) url auth-opts))]
      (if (and (>= status 200)
               (< status 300))
        (swap! app-state assoc-in path body)
        (swap! app-state assoc :error (:message body))))))
