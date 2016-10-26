(ns bog.views.home-view
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [quiescent.core :refer [defcomponent]])
  (:require [cljs.core.async :refer [>!]]
            [quiescent.dom :refer [div section h1 h2]]
            [bog.components.link :refer [Link]]
            [bog.components.hero :refer [Hero]]))

(defcomponent HomeView [state channels]
  (div {}
    (Hero {:title "Adventure Walker"})
    (Link {:href "/drafts"} "Drafts")))
