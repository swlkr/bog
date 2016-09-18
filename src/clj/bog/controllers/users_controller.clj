(ns bog.controllers.users-controller
  (:require [bog.logic.users :as users]
            [bog.logic.tokens :as tokens]
            [environ.core :refer [env]]
            [bog.utils :as utils]
            [bog.db :as db]))

(defn id->str [m]
  (assoc m :id (.toString (:id m))))

(defn create! [request]
  (let [{:keys [secret]} env]
    (as-> (:body request) r
          (users/pre-create r)
          (db/insert-user<! r)
          (select-keys r [:id :email])
          (id->str r)
          (tokens/generate r secret)
          (hash-map :access-token r)
          (utils/ring-response r))))
