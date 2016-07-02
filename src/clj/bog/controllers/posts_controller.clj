(ns bog.controllers.posts-controller
  (:require [bog.logic.posts :as post]
            [environ.core :refer [env]]
            [bog.utils :refer [throw+]]
            [bog.logic.posts :as posts]))

(defn create-post! [request]
  (let [{:keys [body user]} request
        input (assoc body :user user)]
    (->> input
         posts/create!
         posts/create-response)))
