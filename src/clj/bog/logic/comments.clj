(ns bog.logic.comments
  (:require [bog.utils :as utils]
            [bog.errors :as errors]))

(defn create [m]
  (let [ks [:id :post_id :name :content]]
    (->> (select-keys m ks)
         (utils/ensure! "A map is required" map?)
         (utils/ensure! (errors/missing-keys ks) (partial utils/keys? ks))
         (utils/update-vals utils/escape-html))))
