(ns bog.views.draft-list-view
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [quiescent.core :as q]
            [quiescent.dom :as d]
            [cljs.core.async :refer [>!]]
            [bog.components.hero :refer [Hero]]
            [bog.components.link :refer [Link]]
            [bog.components.card :refer [Card]]
            [bog.app :refer [dispatch! add-action app]]
            [bog.api :as api]
            [bog.utils :as utils]))

(defn on-delete-draft-click [state draft]
  (dispatch! :loading true)
  (go
    (let [req {:url (str "/api/drafts/" (:id draft)) :method :delete}
          res (<! (api/send state req))
          {:keys [status body]} res]
      (dispatch! :loading false)
      (if (= status 200)
        (dispatch! :on-delete-draft-success body)
        (dispatch! :on-error body)))))

(defn on-delete-draft-res [state {:keys [id]}]
  (update-in state [:drafts id] dissoc))

(add-action :on-delete-draft-click on-delete-draft-click)
(add-action :on-delete-draft-res on-delete-draft-res)

(q/defcomponent DraftCard
  :keyfn :id
  [{:keys [id title content days_ago] :as draft}]
  (d/div {:className "column is-one-quarter"}
    (Card {:title title :img "http://placehold.it/300x225"}
      (d/div {}
        (d/div {:style {:marginBottom "20px"}}
          (d/p {}
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
                              (dispatch! :on-delete-draft-click draft))))}
            "Delete"))))))

(defn get-drafts [state _]
  (go
    (let [req {:url "/api/drafts" :method :get}
          {:keys [status body]} (<! (api/send state req))]
      (if (= status 200)
        (dispatch! :get-drafts-res body)
        (dispatch! :get-drafts-res-err body))))
  (assoc state :info "Getting drafts..."))

(defn get-drafts-res-err [state body]
  (assoc state :message (:message body) :info ""))

(defn get-drafts-res [state drafts]
  (assoc state :drafts drafts :info ""))

(add-action :get-drafts get-drafts)
(add-action :get-drafts-res get-drafts-res)
(add-action :get-drafts-res-err get-drafts-res-err)

(q/defcomponent DraftListView
  :on-mount #(dispatch! :get-drafts nil)
  [state]
  (let [{:keys [drafts]} state]
    (d/div {}
      (Hero {:title "Drafts"
             :subtitle "Here's a list of drafts"})
      (d/div {:className "container m-t-3"}
        (d/div {:className "columns"}
          (d/div {:className "column"}
            (Link {:href "/drafts/new"} "New Draft")
            (d/div {:className "columns m-t-3"}
              (map DraftCard drafts))))))))
