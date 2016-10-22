(ns bog.components.textarea
  (:require-macros [quiescent.core :refer [defcomponent]])
  (:require [quiescent.dom :as d]
            [quiescent.dom.uncontrolled :as du]))

(defn textarea [& {:keys [placeholder class path state rows]}]
  [:textarea {:placeholder placeholder
              :class class
              :rows rows
              :value (get-in @state path)
              :on-change #(swap! state assoc-in path (-> % .-target .-value))}])


(defcomponent Textarea [{:keys [placeholder className style value onChange label]}]
  (d/p {:className "control"}
    (d/label {:className "label"} label)
    (du/textarea {:placeholder placeholder
                  :className (str "textarea " className)
                  :style style
                  :value value
                  :onChange onChange})))
