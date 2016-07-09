(ns bog.actions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [bog.app-state :refer [app-state]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [bog.local-storage :as storage]))

(enable-console-print!)

(defn login [e]
  (let [email (-> @app-state :login :email)
        pw (-> @app-state :login :password)]
    (.preventDefault e)
    (swap! app-state assoc :error "")
    (go
        (let [url "/api/tokens"
              response (<! (http/post url {:json-params {:email email :password pw}}))]
          (if (= 200 (:status response))
            (do
              (swap! app-state assoc :access-token (-> response :body :access-token))
              (swap! app-state assoc :view :new-post)
              (swap! app-state update-in [:login] {:email "" :password ""})
              (storage/set-item! "access-token" (-> response :body :access-token)))
            (swap! app-state assoc :error (-> response :body :message)))))))
