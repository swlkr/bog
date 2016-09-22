(ns bog.logic.tags
  (:require [bog.db :as db]
            [bog.utils :as utils]
            [bog.errors :as errors])
  (:refer-clojure :exclude [update]))

(defn validate [m]
  (let [ks [:id :draft_id :name]]
    (->> (select-keys m ks)
         (utils/ensure! "A map is required" map?)
         (utils/ensure! (errors/missing-keys ks) (partial utils/keys? ks)))))

(def create validate)
(def update validate)
