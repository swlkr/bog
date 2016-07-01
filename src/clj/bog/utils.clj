(ns bog.utils)

(defn throw+ [msg]
  (throw
    (ex-info msg {})))

(defn nil-val? [[k v]]
  (nil? v))

(defn filter-nil-vals [data]
  (->> data
       (filter nil-val?)
       (into {})))

(defn ensure-no-nil-vals [data]
  (let [nil-data (filter-nil-vals data)
        a (count (keys nil-data))]
    (if (= a 0)
      data
      (throw+ (-> nil-data
                  keys
                  first
                  (str " was blank"))))))

(defn dissoc-nil-values [data]
  (let [keys (keys (filter-nil-vals data))]
    (apply dissoc data keys)))

(defn ensure! [f arg err]
  (if (apply f [arg])
    arg
    (throw+ err)))

(defn key-is-nil? [k]
  (fn [m]
    (nil? (k m))))

(defn has-keys? [m keys]
  (every? true? (map #(contains? m %) keys)))

(defn ensure-has-keys [m ks]
  (if (has-keys? m ks)
    m
    (throw+
      (str
        "Missing parameters. Expected: "
        (clojure.string/join ", " (map #(str (name %)) ks))))))
