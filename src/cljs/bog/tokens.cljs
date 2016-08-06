(ns bog.tokens
  (:require [bog.local-storage :as storage]
            [bog.app-state :refer [app-state]]))

(defn get-token []
  (storage/get-item "access-token"))
