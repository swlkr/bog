(ns bog.controllers.drafts-controller
  (:require [bog.logic.drafts :as drafts]
            [bog.db :as db]
            [bog.utils :as utils]))

(defn create! [body]
  (-> (drafts/pre-create body)
      (db/insert-draft<!)
      (utils/ring-response)))

(defn list! [user_id]
  (-> (db/get-drafts {:user_id user_id})
      (utils/ring-response)))

(defn get! [id]
  (-> (db/get-drafts-by-id {:id id})
      (first)
      (utils/ring-response)))

(defn update! [id body]
  (-> (merge {:id id} body)
      (drafts/pre-update)
      (db/update-draft<!)
      (utils/ring-response)))

(defn delete! [id]
  (-> {:id id}
      (db/delete-draft<!)
      (utils/ring-response)))
