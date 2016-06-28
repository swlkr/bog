(ns bog.middleware)

(defn wrap-exceptions [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        {:status 500 :body {:message (.getMessage e)}}))))
