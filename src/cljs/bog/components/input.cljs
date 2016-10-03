(ns bog.components.input
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [quiescent.core :refer [defcomponent]])
  (:require [cljs.core.async :refer [>!]]
            [quiescent.dom :as d]))

(defn input [])

(defcomponent Input [{:keys [label type placeholder className value onChange channel key]}]
  (d/div {:className "m-t-2"}
    (d/label {:className "label"} label)
    (d/p {:className "control"}
      (d/input {:type type
                :placeholder placeholder
                :className (str "input " className)
                :value value
                :onChange (or onChange (fn [e]
                                         (let [val (-> e .-target .-value)]
                                           (go (>! channel {:key key :val val})))))}))))
