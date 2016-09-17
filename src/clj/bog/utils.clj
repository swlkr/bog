(ns bog.utils)

(defn throw+
  ([msg] (throw (ex-info msg {})))
  ([msg data] (throw (ex-info msg data))))

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

(defn ensure! [err f arg]
  (if (apply f [arg])
    arg
    (throw+ err)))

(defn ring-response [body]
  {:status 200
   :body body})

(defn key-is-nil? [k]
  (fn [m]
    (nil? (k m))))

(defn has-keys? [m keys]
  (every? (partial contains? m) keys))

(defn seq-contains? [coll target]
  (some #(= target %) coll))
