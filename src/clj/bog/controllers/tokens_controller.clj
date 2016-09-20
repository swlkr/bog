(ns bog.controllers.tokens-controller
  (:require [bog.logic.users :as users]
            [bog.logic.tokens :as tokens]
            [environ.core :refer [env]]
            [bog.utils :as utils]))

(defn create! [body]
  (let [{:keys [secret]} env]
    (as-> body b
          (users/verify! b)
          (tokens/generate b secret)
          (hash-map :access-token b)
          (utils/ring-response b))))
