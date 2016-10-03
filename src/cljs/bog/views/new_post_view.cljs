(ns bog.views.new-post-view
  (:require-macros [quiescent.core :refer [defcomponent]])
  (:require [quiescent.dom :refer [div section h1 h2]]
            [markdown.core :refer [md->html]]))

(defcomponent NewPostView [state channels]
  (section {:className "hero"}
    (div {:className "hero-body"}
      (div {:className "container"}
        (h1 {:className "title"}
          "New Post")
        (h2 {:className "subtitle"}
          "This is where you can create new posts!")))))
