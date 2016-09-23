(ns bog.components.input)

(defn input [& {:keys [type placeholder class path state]}]
  [:p {:class "control"}
    [:input {:type type
             :placeholder placeholder
             :class class
             :value (get-in @state path)
             :on-change #(swap! state assoc-in path (-> % .-target .-value))}]])
