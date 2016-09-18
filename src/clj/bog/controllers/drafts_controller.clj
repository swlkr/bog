(ns bog.controllers.drafts-controller
  (:require [bog.logic.drafts :as drafts]
            [bog.db :as db]
            [bog.utils :as utils]))

(defn create! [request]
  (->> (-> request :body)
       (drafts/pre-create)
       (db/insert-draft<!)
       (utils/ring-response)))

(defn list! [request]
  (-> (-> request :body)
      (select-keys [:user_id])
      (db/get-drafts)
      (utils/ring-response)))

(defn get! [id]
  (-> {:id id}
      (db/get-drafts-by-id)
      (first)
      (utils/ring-response)))

(defn update! [id request]
  (->> (-> request :body)
       (merge {:id id})
       (drafts/pre-update)
       (db/update-draft<!)
       (utils/ring-response)))

(defn delete! [id]
  (-> {:id id}
      (db/delete-draft<!)
      (utils/ring-response)))
