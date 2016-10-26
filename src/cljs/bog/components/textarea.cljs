(ns bog.components.textarea
  (:require [quiescent.dom :as d]
            [quiescent.dom.uncontrolled :as du]
            [quiescent.core :as q]))

(q/defcomponent Textarea [{:keys [placeholder className style value onChange label]}]
  (d/p {:className "control"}
    (d/label {:className "label"} label)
    (du/textarea {:placeholder placeholder
                  :className (str "textarea " className)
                  :style style
                  :value value
                  :onChange onChange})))
