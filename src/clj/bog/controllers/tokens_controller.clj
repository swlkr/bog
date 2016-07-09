(ns bog.controllers.tokens-controller
  (:require [bog.logic.users :as users]
            [bog.logic.tokens :as tokens]
            [environ.core :refer [env]]
            [bog.utils :refer [throw+ ring-response]]))

(defn create-token! [request]
  (let [{:keys [secret database-url]} env]
    (as-> request r
          (users/login! r secret)
          (tokens/generate r secret)
          (assoc {} :access-token r)
          (ring-response r))))
