(ns bog.components.input
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [>!]]
            [quiescent.core :as q]
            [quiescent.dom :as d]
            [quiescent.dom.uncontrolled :as du]))

(q/defcomponent Input [{:keys [label type placeholder className value onChange]}]
  (d/p {:className "control"}
    (d/label {:className "label"} label)
    (du/input {:type type
               :placeholder placeholder
               :className (str "input " className)
               :value value
               :onChange onChange})))
