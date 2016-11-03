(ns bog.logic.images
  (:require [bog.db :as db]
            [bog.utils :as utils]
            [bog.errors :as errors]
            [clojure.string :as string])
  (:refer-clojure :exclude [update]))

(defn create [m]
  (let [ks [:id :draft_id :filename :url]]
    (->> (select-keys m ks)
         (utils/ensure! "A map is required" map?)
         (utils/ensure! (errors/missing-keys ks) (partial utils/keys? ks)))))

(defn update [m]
  (let [ks [:id :draft_id]
        opt-ks [:filename :url]]
    (->> (select-keys m (concat ks opt-ks))
         (utils/ensure! "A map is required" map?)
         (utils/ensure! (errors/missing-keys ks) (partial utils/keys? ks))
         (merge {:filename nil :url nil}))))
