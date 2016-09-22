(ns bog.controllers.images-controller
  (:require [bog.logic.images :as images]
            [bog.db :as db]
            [bog.utils :as utils]))

(defn create! [draft_id body]
  (-> (merge {:draft_id draft_id} body)
      (images/create)
      (db/insert-image<!)
      (utils/ring-response)))

(defn list! [draft_id]
  (-> (db/get-images {:draft_id draft_id})
      (utils/ring-response)))

(defn get! [id]
  (-> (db/get-images-by-id {:id id})
      (first)
      (utils/ring-response)))

(defn update! [id body]
  (-> (merge {:id id} body)
      (images/update)
      (db/update-image<!)
      (utils/ring-response)))

(defn delete! [id]
  (-> (db/delete-image<! {:id id})
      (utils/ring-response)))
