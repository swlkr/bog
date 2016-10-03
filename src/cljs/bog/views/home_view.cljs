(ns bog.views.home-view
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [quiescent.core :refer [defcomponent]])
  (:require [cljs.core.async :refer [>!]]
            [quiescent.dom :refer [div section h1 h2]]))

(defcomponent HomeView [state channels]
  (section {:className "hero"}
    (div {:className "hero-body"}
      (div {:className "container"}
        (h1 {:className "title"}
          "Home page")
        (h2 {:className "subtitle"}
          "This is where the posts will go")))))
