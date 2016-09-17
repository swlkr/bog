(ns bog.logic.drafts
  (:require [bog.db :as db]
            [bog.utils :as utils]
            [bog.errors :as errors]))

(def insert-keys [:id :user_id :title :content :type :sort_order])
(def missing-insert-keys (errors/missing-keys insert-keys))
(defn insert-request? [m]
  (utils/has-keys? m insert-keys))

(defn make-insert-params [m]
  (let [{:keys [id user_id title content sort_order type]} m]
    {:user_id user_id
     :id id
     :title title
     :content content
     :type (name type)
     :sort_order sort_order}))

(defn pre-create [req]
  (->> req
       (utils/ensure! "A map is required" map?)
       (utils/ensure! missing-insert-keys insert-request?)
       (make-insert-params)))

(defn make-list-params [m]
  (let [{:keys [user_id]} m]
    {:user_id user_id}))

(defn make-get-params [id]
  {:id id})

(def update-keys [:id :title :content :type :sort_order])
(def missing-update-keys (errors/missing-keys update-keys))
(defn update-request? [m]
  (utils/has-keys? m update-keys))

(defn make-update-params [m]
  (let [{:keys [id title content sort_order type]} m]
    {:id id
     :title title
     :content content
     :type (name type)
     :sort_order sort_order}))

(defn pre-update [req]
  (->> req
       (utils/ensure! "A map is required" map?)
       (utils/ensure! missing-update-keys update-request?)
       (make-update-params)))
