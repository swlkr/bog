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

(defn wrap-jwt [handler]
  (fn [request]
    (let [authorization (get-in request [:headers "authorization"])
          secret (env :secret)
          user (tokens/decode! authorization secret)
          req (assoc request :user user)]
      (handler req))))

(defn wrap-auth [handler]
  (fn [request]
    (let [{:keys [user]} request]
      (if (not (nil? user))
        (handler request)
        (throw
          (ex-info "You don't have permission to do that"
            {:status 401}))))))
