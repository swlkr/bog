(ns bog.controllers.status-controller)

(defn get-status [request]
  {:status 200
   :body {:message "alive"}})
