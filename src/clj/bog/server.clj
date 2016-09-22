(ns bog.server
  (:require [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [environ.core :refer [env]]
            [org.httpkit.server :refer [run-server]]
            [bog.routes :refer [routes]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [bog.middleware :as middleware])
  (:gen-class))

(def http-handler
  (-> routes
      middleware/wrap-exceptions
      wrap-json-response
      (wrap-defaults api-defaults)
      (wrap-json-body {:keywords? true})
      wrap-with-logger
      wrap-gzip))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 10555))
        vars (select-keys env [:database-url :secret])]
    (run-server http-handler {:port port :join? false})))
