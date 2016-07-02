(ns bog.controllers.comments-controller
  (:require [bog.logic.comments :as comments]))

(defn create-comment! [request]
  (-> (:body request)
      comments/create!
      comments/create-response))
