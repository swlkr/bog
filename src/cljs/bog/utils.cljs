(ns bog.utils
  (:require [clojure.string :as string]))

(defn classes [params]
  (->> params
      (filterv #(true? (second %)))
      (keys)
      (string/join " ")))

(defn find-index-by-key [key items id]
  (-> (map key items)
      (.indexOf id)))

(def find-index-by-id (partial find-index-by-key :id))
