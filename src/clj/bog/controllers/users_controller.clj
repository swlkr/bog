(ns bog.controllers.users-controller
  (:require [bog.logic.users :as users]
            [bog.logic.tokens :as tokens]
            [environ.core :refer [env]]
            [bog.utils :as utils]
            [bog.db :as db]))

(defn create! [request]
  (let [{:keys [secret]} env]
    (as-> (:body request) r
          (users/pre-create r)
          (db/insert-user<! r)
          (select-keys r [:id :email])
          (tokens/generate r secret)
          (hash-map :access-token r)
          (utils/ring-response r))))
