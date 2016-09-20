(ns bog.controllers.users-controller
  (:require [bog.logic.users :as users]
            [bog.logic.tokens :as tokens]
            [environ.core :refer [env]]
            [bog.utils :as utils]
            [bog.db :as db]))

(defn create! [body]
  (let [{:keys [secret]} env]
    (as-> body b
          (users/pre-create b)
          (db/insert-user<! b)
          (select-keys b [:id :email])
          (tokens/generate b secret)
          (hash-map :access-token b)
          (utils/ring-response b))))
