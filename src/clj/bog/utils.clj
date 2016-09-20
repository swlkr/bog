(ns bog.utils
  (:require [clojure.string :as string]))

(defn throw+
  ([msg] (throw (ex-info msg {})))
  ([msg data] (throw (ex-info msg data))))

(defn nil-val? [[k v]]
  (nil? v))

(defn filter-nil-vals [data]
  (->> data
       (filter nil-val?)
       (into {})))

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

(defn keys? [ks m]
  (every? (partial contains? m) ks))

(defn format-date [date format-str]
  (.format (java.text.SimpleDateFormat. format-str) date))

(defn update-vals [f m]
  (persistent!
    (reduce-kv (fn [m k v] (assoc! m k (f v)))
               (transient (empty m)) m)))

(defn escape-html [val]
  (if (string? val)
    (-> val
        (string/replace #"&" "&amp;")
        (string/replace #"<" "&lt;")
        (string/replace #">" "&gt;")
        (string/replace #"\"" "&quot;"))
    val))
