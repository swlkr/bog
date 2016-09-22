(ns bog.controllers.comments-controller
  (:require [bog.logic.comments :as comments]
            [bog.db :as db]
            [bog.utils :as utils]))

(defn create! [post_id body]
  (-> (merge {:post_id post_id} body)
      (comments/create)
      (db/insert-comment<!)
      (utils/ring-response)))

(defn list! [post_id]
  (-> (db/get-comments-by-post-id {:post_id post_id})
      (utils/ring-response)))

(defn delete! [id]
  (-> (db/delete-comment<! {:id id})
      (utils/ring-response)))
