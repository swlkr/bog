(ns bog.components.input
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [quiescent.core :refer [defcomponent]])
  (:require [cljs.core.async :refer [>!]]
            [quiescent.dom :as d]
            [quiescent.dom.uncontrolled :as du]))

(defn input [])

(defcomponent Input [{:keys [label type placeholder className value onChange]}]
  (d/p {:className "control"}
    (d/label {:className "label"} label)
    (du/input {:type type
               :placeholder placeholder
               :className (str "input " className)
               :value value
               :onChange onChange})))
