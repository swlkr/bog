(ns bog.utils
  (:require [clojure.string :as string]))

(defn classes [params]
  (->> params
      (filterv #(true? (second %)))
      (keys)
      (string/join " ")))
