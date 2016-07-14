(ns bog.views.drafts-view
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [bog.routes :as routes]
            [bog.app-state :refer [app-state]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [markdown.core :refer [md->html]]
            [bog.components.header :refer [header]]))

(defn get-drafts! []
  (go
    (let [url "/api/drafts"
          {:keys [status body]} (<! (http/get url))]
      (swap! app-state assoc :drafts body))))

(defn edit-draft-clicked [e d]
  (do
    (.preventDefault e)
    (swap! app-state assoc :edit-draft d :view :edit-draft)))

(defn draft [d]
  (let [{:keys [title content created_at]} d]
      [:a {:href "#" :class "list-group-item" :on-click (fn [e] (edit-draft-clicked e d))}
        [:h4 {:class "list-group-item-heading"} title]
        [:p {:class "list-group-item-text text-muted"} created_at]]))


(defn draft-list [drafts]
  [:div {:class "row"}
    [:div {:class "col-xs-12 col-sm-6 col-sm-offset-3 col-lg-4 col-lg-offset-4"}
      [:div {:class "list-group"}
        (for [d drafts]
          ^{:key (:id d)} [draft d])]]])

(defn drafts-view []
  (get-drafts!)
  (fn []
    (let [drafts (:drafts @app-state)]
      [:div {:class "drafts-view"}
        [header]
        [:div {:class "container-fluid"}
          [:div {:class "row"}
            [:div {:class "col-xs-12"}
              [draft-list drafts]]]]])))
