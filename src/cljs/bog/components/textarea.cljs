(ns bog.components.textarea)

(defn textarea [& {:keys [placeholder class path state rows]}]
  [:textarea {:placeholder placeholder
              :class class
              :rows rows
              :value (get-in @state path)
              :on-change #(swap! state assoc-in path (-> % .-target .-value))}])
