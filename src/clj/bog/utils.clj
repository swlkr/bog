(ns bog.utils)

(defn nil-val? [[k v]]
  (nil? v))

(defn filter-nil-vals [data]
  (->> data
       (filter nil-val?)
       (into {})))

(defn dissoc-nil-values [data]
  (let [keys (keys (filter-nil-vals data))]
    (apply dissoc data keys)))
