(ns bog.controllers.users-controller
  (:require [bog.logic.users :as users]
            [bog.logic.tokens :as tokens]
            [environ.core :refer [env]]
            [bog.utils :refer [throw+]]))

(defn create-user! [request]
  (let [{:keys [secret database-url]} env]
    (if (or
          (nil? secret)
          (nil? database-url))
      (throw+ "Environment variables secret or database-url are missing") ; do this on startup?
      (-> request
          (users/create! secret)
          (tokens/generate! secret)
          (tokens/response)))))
