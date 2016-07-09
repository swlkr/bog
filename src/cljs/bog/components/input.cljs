(ns bog.components.input
  (:require [bog.app-state :refer [app-state]]))

(defn input [& {:keys [type placeholder class path state]}]
  [:input {:type type
           :placeholder placeholder
           :class class
           :value (get-in @state path)
           :on-change #(swap! state assoc-in path (-> % .-target .-value))}])
