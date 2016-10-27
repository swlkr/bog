(ns bog.views.preview-draft-view
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [quiescent.core :as q]
            [quiescent.dom :as d]
            [cljs.core.async :refer [<!]]
            [bog.app :as app]
            [bog.api :as api]
            [bog.components.hero :refer [Hero]]
            [bog.actions.drafts :as drafts]))

(q/defcomponent PreviewDraftView
  :on-mount #(app/dispatch! :drafts/get nil)
  [state]
  (let [{:keys [title content days_ago]} (:draft state)]
    (d/div {}
      (Hero {:is-medium true
             :background-image "https://placehold.it/1024x245"})
      (d/div {:className "container" :style {:marginTop "20px"}}
        (d/div {:className "columns"}
          (d/div {:className "column is-half is-offset-one-quarter"}
            (d/div {:className "content"}
              (d/h1 {:className "title"} title)
              (d/p {} (str days_ago " days ago"))
              (d/p {} content))))))))
