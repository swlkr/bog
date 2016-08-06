(ns bog.sync
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [bog.app-state :refer [app-state]]))

(defn build-server-args [method url & {:keys [headers body]}]
  {:url url
   :method method
   :opts {:headers (or headers {})
          :json-params (or body {})}})

(defn build-state-args [op path]
  {:op op
   :path path})

(defn server-fn [method]
  (condp = method
    :get http/get
    :post http/post
    :put http/put
    :delete http/delete))

(defn get-auth-header! [auth]
  (if (= auth true)
    {:headers {"Authorization" (:access-token @app-state)}}
    {:headers {}}))

(defn sync! [server-args state-args & {:keys [auth]}]
  (go
    (let [auth-header (get-auth-header! auth)
          {:keys [url method opts]} server-args
          m-opts (merge auth-header opts)
          {:keys [op path]} state-args
          {:keys [status body]} (<! ((server-fn method) url m-opts))]
      (if (and (>= status 200)
               (< status 300))
        (swap! app-state op path body)
        (swap! app-state assoc :error (:message body))))))
