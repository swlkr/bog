(ns bog.controllers.posts-controller
  (:require [bog.logic.posts :as posts]
            [bog.utils :as utils]
            [bog.db :as db]))

(defn create! [body]
  (-> (posts/create body)
      (db/insert-post<!)
      (utils/ring-response)))

(defn list! []
  (-> (db/get-posts)
      (utils/ring-response)))

(defn get! [id]
  (-> (db/get-posts-by-id {:id id})
      (first)
      (utils/ring-response)))

(defn update! [id body]
  (-> (merge {:id id} body)
      (posts/update)
      (db/update-post<!)
      (utils/ring-response)))

(defn delete! [id]
  (-> (db/delete-post<! {:id id})
      (utils/ring-response)))
