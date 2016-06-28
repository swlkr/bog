(ns bog.server
  (:require [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [environ.core :refer [env]]
            [org.httpkit.server :refer [run-server]]
            [bog.routes :refer [routes]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]])
  (:gen-class))

(def http-handler
  (-> routes
      (wrap-defaults api-defaults)
      (wrap-json-body {:keywords? true})
      wrap-json-response
      wrap-with-logger
      wrap-gzip))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (run-server http-handler {:port port :join? false})))
