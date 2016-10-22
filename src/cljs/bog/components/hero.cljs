(ns bog.components.hero
  (:require-macros [quiescent.core :refer [defcomponent]])
  (:require [quiescent.dom :refer [section div h1 h2]]))

(defcomponent Hero [{:keys [title subtitle]}]
  (section {:className "hero"}
    (div {:className "hero-body"}
      (div {:className "container"}
        (h1 {:className "title"} title)
        (h2 {:className "subtitle"} subtitle)))))
