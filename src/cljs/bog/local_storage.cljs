(ns bog.local-storage)

(defn set-item! [key value]
  (.setItem (.-localStorage js/window) key value))

(defn get-item [key]
  (.getItem (.-localStorage js/window) key))
