(ns bog.controllers.posts-controller
  (:require [bog.logic.posts :as post]
            [environ.core :refer [env]]
            [bog.utils :refer [throw+ ring-response]]
            [bog.logic.posts :as posts]))

(defn create-post! [request]
  (let [{:keys [body user]} request
        input (assoc body :user user)]
    (->> input
         posts/create!
         ring-response)))

(defn get-posts! [request]
  (-> (posts/get-list)
      (ring-response)))

(defn get-post! [id]
  (-> (posts/get-post id)
      (ring-response)))

(defn get-drafts! [request]
  (-> (posts/get-drafts)
      (ring-response)))

(defn get-draft! [id]
  (-> (posts/get-draft id)
      (ring-response)))

(defn update-post! [request id]
  (let [{:keys [body user]} request
        params (assoc body :user user :id id)]
    (-> params
        (posts/update!)
        (ring-response))))
