(ns bog.controllers.comments-controller
  (:require [bog.logic.comments :as comments]
            [bog.db :as db]
            [bog.utils :as utils]))

(defn create! [draft_id body]
  (-> (merge {:draft_id draft_id} body)
      (comments/create)
      (db/insert-comment<!)
      (utils/ring-response)))

(defn list! [draft_id]
  (-> (db/get-comments-by-draft-id {:draft_id draft_id})
      (utils/ring-response)))

(defn delete! [id]
  (-> (db/delete-comment<! {:id id})
      (utils/ring-response)))
