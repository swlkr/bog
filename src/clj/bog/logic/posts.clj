(ns bog.logic.posts
  (:require [bog.utils :as utils]
            [bog.errors :as errors]))

(defn pre-create [m]
  (let [ks [:id :user_id :title :content :type :sort_order]
        post-types #{"post" "quote" "video" "slideshow"}]
    (->> (select-keys m ks)
         (utils/ensure! "A map is required" map?)
         (utils/ensure! (errors/missing-keys ks) (partial utils/keys? ks))
         (utils/ensure! "Post can only be quote, post, video, slideshow"
                        (comp (partial contains? post-types) :type))
         (utils/ensure! (errors/missing-keys ks) (partial every? (comp not utils/blank?))))))

(defn pre-update [m]
  (let [ks [:id]
        opt-ks [:title :content :type :sort_order]
        post-types #{"post" "quote" "video" "slideshow" nil}]
    (->> (select-keys m (concat ks opt-ks))
         (utils/ensure! "A map is required" map?)
         (utils/ensure! (errors/missing-keys ks) (partial utils/keys? ks))
         (merge {:title nil :content nil :type nil :sort_order nil})
         (utils/ensure! "Post can only be quote, post, video, slideshow"
                        (comp (partial contains? post-types) :type)))))
