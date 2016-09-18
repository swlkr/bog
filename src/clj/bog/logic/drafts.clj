(ns bog.logic.drafts
  (:require [bog.db :as db]
            [bog.utils :as utils]
            [bog.errors :as errors]))

(defn pre-create [req]
  (let [ks [:id :user_id :title :content :type :sort_order]
        draft-types #{"post" "quote" "video" "slideshow"}]
    (->> (select-keys req ks)
         (utils/ensure! "A map is required" map?)
         (utils/ensure! (errors/missing-keys ks) (partial utils/keys? ks))
         (utils/ensure! "Draft can only be quote, post, video or slideshow"
                        (comp (partial contains? draft-types) :type)))))

(defn pre-update [req]
  (let [ks [:id]
        opt-ks [:title :content :type :sort_order]]
    (->> (select-keys req (concat ks opt-ks))
         (utils/ensure! "A map is required" map?)
         (utils/ensure! (errors/missing-keys ks) (partial utils/keys? ks))
         (merge {:title nil :content nil :type nil :sort_order nil}))))
