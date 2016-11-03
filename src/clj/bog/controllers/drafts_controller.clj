(ns bog.controllers.drafts-controller
  (:require [bog.logic.drafts :as drafts]
            [bog.db :as db]
            [bog.utils :as utils]))

(defn create! [body user]
  (-> (drafts/create (merge body {:user_id (:id user)}))
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
      (drafts/update)
      (db/update-draft<!)
      (utils/ring-response)))

(defn delete! [id]
  (-> (db/delete-draft<! {:id id})
      (utils/ring-response)))
