(ns bog.controllers.tokens-controller
  (:require [bog.logic.users :as users]
            [bog.logic.tokens :as tokens]
            [environ.core :refer [env]]
            [bog.utils :as utils]))

(defn create! [request]
  (let [{:keys [secret]} env]
    (as-> (:body request) body
          (users/verify! body)
          (tokens/generate body secret)
          (hash-map :access-token body)
          (utils/ring-response body))))
