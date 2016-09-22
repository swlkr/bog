(ns bog.controllers.posts-controller
  (:require [bog.utils :as utils]
            [bog.db :as db]))

(defn list! []
  (-> (db/get-posts)
      (utils/ring-response)))

(defn get! [id]
  (-> (db/get-posts-by-id {:id id})
      (first)
      (utils/ring-response)))
