(ns bog.components.menu
  (:require-macros [quiescent.core :refer [defcomponent]])
  (:require [quiescent.dom :refer [aside p ul]]))

(defcomponent Menu [{:keys [label]} & children]
  (aside {:className "menu"}
    (p {:className "menu-label"} label)
    (ul {:className "menu-list"}
      children)))
