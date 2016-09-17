(ns bog.errors
  (:require [clojure.string :as string]))

(defn missing-keys [ks]
  (str
    "Missing parameters. Expected: "
    (clojure.string/join ", " (map #(str (name %)) ks))))
