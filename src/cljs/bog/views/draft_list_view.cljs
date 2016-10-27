(ns bog.views.draft-list-view
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [quiescent.core :as q]
            [quiescent.dom :as d]
            [cljs.core.async :refer [>!]]
            [bog.components.hero :refer [Hero]]
            [bog.components.link :refer [Link]]
            [bog.components.card :refer [Card]]
            [bog.app :as app]
            [bog.api :as api]
            [bog.utils :as utils]
            [bog.actions.drafts :as drafts]))

(q/defcomponent DraftCard
  :keyfn :id
  [{:keys [id title content days_ago] :as draft}]
  (d/div {:key id :className "column is-one-third"}
    (Card {:id id :title title :img "http://placehold.it/300x225"}
      (d/div {}
        (d/div {:style {:marginBottom "20px"}}
          (d/p {:className "text-overflow"}
            content)
          (d/div {}
            (str days_ago " days ago")))
        (d/div {}
          (Link {:href (str "/drafts/" id)} "Preview")
          (Link {:style {:marginLeft "5px"}
                 :href (str "/drafts/" id "/edit")} "Edit")
          (Link {:style {:marginLeft "5px"}
                 :onClick (fn [e]
                           (let [c (js/confirm "Are you sure?")]
                            (when c
                              (app/dispatch! :drafts/delete draft))))}
            "Delete"))))))

(q/defcomponent DraftListView
  :on-mount #(app/dispatch! :drafts/list nil)
  [state]
  (let [{:keys [drafts]} state]
    (d/div {}
      (Hero {:title "Drafts"
             :subtitle "Here's a list of drafts"})
      (d/div {:className "container m-t-3"}
        (d/div {:className "columns"}
          (d/div {:className "column"}
            (Link {:href "/drafts/new"} "New Draft")
            (d/div {:className "columns m-t-3 is-multiline"}
              (map DraftCard drafts))))))))
