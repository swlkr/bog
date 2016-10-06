(ns bog.views.new-post-view
  (:require-macros [quiescent.core :refer [defcomponent]]
                   [cljs.core.async.macros :refer [go]])
  (:require [quiescent.dom :refer [div section h1 h2 aside ul li p a textarea]]
            [markdown.core :refer [md->html]]
            [cljs.core.async :refer [>!]]))

(defcomponent NewPostView [state channels]
  (let [{:keys [input-ch]} channels
        {:keys [new-draft-content]} state
        content (md->html new-draft-content)]
    (div {}
      (section {:className "hero"}
        (div {:className "hero-body"}
          (div {:className "container"}
            (h1 {:className "title"}
              "New Post")
            (h2 {:className "subtitle"}
              "This is where you can create new posts!"))))
      (div {:className "container is-fluid m-t-3"}
        (div {:className "columns"}
          (div {:className "column is-2"}
            (aside {:className "menu"}
              (p {:className "menu-label"} "Drafts")
              (ul {:className "menu-list"}
                (li {}
                  (a {:href "#"} "Draft 1"))
                (li {}
                  (a {:href "#"} "Draft 2")))))
          (div {:className "column"}
            (p {:className "control"}
              (textarea {:className "textarea"
                         :placeholder "New drafts start here"
                         :style {:height "300px"}
                         :value (or new-draft-content "")
                         :onChange (fn [e]
                                     (let [val (-> e .-target .-value)]
                                       (go (>! input-ch {:key :new-draft-content :val val}))))}))
            (div {:className "content"
                  :dangerouslySetInnerHTML {:__html content}})))))))
