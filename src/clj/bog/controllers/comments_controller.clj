(ns bog.controllers.comments-controller
  (:require [bog.logic.comments :as comments]))

(defn create-comment! [request]
  (-> (:body request)
      comments/create!
      comments/create-response))

(defn get-comments! [id]
  (-> id
      comments/get-by-post-id!
      comments/get-by-post-id-response))
