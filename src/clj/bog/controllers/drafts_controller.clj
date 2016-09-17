(ns bog.controllers.drafts-controller
  (:require [bog.logic.drafts :as drafts]
            [bog.db :as db]
            [bog.utils :as utils]))

(defn create! [request]
  (->> (-> request :body)
       (drafts/pre-create)
       (db/insert-draft<!)
       (utils/ring-response)))
