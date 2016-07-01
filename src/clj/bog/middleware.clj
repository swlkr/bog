(ns bog.middleware
  (:require [bog.logic.tokens :as tokens]
            [environ.core :refer [env]]))

(defn wrap-exceptions [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (let [{:keys [status]} (ex-data e)]
          {:status (or status 500)
           :body {:message (.getMessage e)}})))))

(defn wrap-jwt-auth [handler]
  (fn [request]
    (let [{:keys [headers]} request
          secret (env :secret)
          user (tokens/decode! (get headers "authorization") secret)
          req (assoc request :user user)]
      (if (not (nil? user))
        (handler req)
        (throw
          (ex-info "You don't have permission to do that"
            {:status 401}))))))
