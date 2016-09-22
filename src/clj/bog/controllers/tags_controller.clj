(ns bog.controllers.tags-controller
  (:require [bog.logic.tags :as tags]
            [bog.db :as db]
            [bog.utils :as utils]))

(defn create! [draft_id body]
  (-> (merge {:draft_id draft_id} body)
      (tags/create)
      (db/insert-tag<!)
      (utils/ring-response)))

(defn list! [draft_id]
  (-> (db/get-tags {:draft_id draft_id})
      (utils/ring-response)))

(defn get! [id]
  (-> (db/get-tags-by-id {:id id})
      (first)
      (utils/ring-response)))

(defn update! [id body]
  (-> (merge {:id id} body)
      (tags/update)
      (db/update-tag<!)
      (utils/ring-response)))

(defn delete! [id]
  (-> (db/delete-tag<! {:id id})
      (utils/ring-response)))
