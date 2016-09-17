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
