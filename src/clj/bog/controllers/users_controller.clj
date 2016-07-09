(ns bog.controllers.users-controller
  (:require [bog.logic.users :as users]
            [bog.logic.tokens :as tokens]
            [environ.core :refer [env]]
            [bog.utils :refer [throw+ ring-response]]))

(defn create-user! [request]
  (let [{:keys [secret database-url]} env]
    (as-> request r
          (users/create! r secret)
          (select-keys r [:id])
          (tokens/generate r secret)
          (assoc {} :access-token r)
          (ring-response r))))
