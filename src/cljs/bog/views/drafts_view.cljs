(ns bog.views.drafts-view
  (:require [reagent.core :as r]
            [bog.routes :as routes]
            [bog.sync :refer [build-args sync!]]
            [bog.app-state :refer [app-state]]
            [markdown.core :refer [md->html]]
            [bog.components.header :refer [header]]))

(defn draft [d]
  (let [{:keys [id title content created_at]} d]
      [:a {:href (routes/url-for :edit-draft :id id) :class "list-group-item"}
        [:h4 {:class "list-group-item-heading"} title]
        [:p {:class "list-group-item-text text-muted"} created_at]]))

(defn draft-list [drafts]
  [:div {:class "row"}
    [:div {:class "col-xs-12 col-sm-6 col-sm-offset-3 col-lg-4 col-lg-offset-4"}
      [:div {:class "list-group"}
        (for [d drafts]
          ^{:key (:id d)} [draft d])]]])

(defn drafts-view []
  (let [server (build-args :get "/api/drafts")
        state [:drafts]]
    (sync! server state :auth true)
    (fn []
      (let [drafts (:drafts @app-state)]
        [:div {:class "drafts-view"}
          [header]
          [:div {:class "container-fluid"}
            [:div {:class "row"}
              [:div {:class "col-xs-12"}
                [draft-list drafts]]]]]))))
