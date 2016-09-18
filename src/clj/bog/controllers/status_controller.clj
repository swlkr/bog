(ns bog.controllers.status-controller
  (:refer-clojure :exclude [get]))

(defn get [request]
  {:status 200
   :body {:message "alive"}})
