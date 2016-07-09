(ns bog.components.textarea
  (:require [bog.app-state :refer [app-state]]))

(defn textarea [& {:keys [placeholder class path state rows]}]
  [:textarea {:placeholder placeholder
               :class class
               :rows rows
               :value (get-in @state path)
               :on-change #(swap! state assoc-in path (-> % .-target .-value))}])
