(ns bog.core
  (:require [reagent.core :as reagent :refer [atom]]
            [bog.app-state :refer [app-state]]
            [bog.routes :refer [app-routes]]
            [bog.views.posts-view :refer [posts-view]]
            [bog.views.login-view :refer [login-view]]
            [bog.views.new-post :refer [new-post]]
            [bog.views.drafts-view :refer [drafts-view]]
            [bog.components.navbar :refer [navbar]]
            [bog.local-storage :as storage]))

(enable-console-print!)

(app-routes)

(defmulti views identity)
(defmethod views :posts [] posts-view)
(defmethod views :login [] login-view)
(defmethod views :new-post [] new-post)
(defmethod views :drafts [] drafts-view)
(defmethod views :default [] (:h1 "Not found"))

(defn app []
  (let [{:keys [view]} @app-state
        c (views view)
        access-token (storage/get-item "access-token")]
    (swap! app-state assoc :access-token access-token)
    [:div {:class "max-height"}
      [navbar]
      [c]]))

(reagent/render [app] (js/document.getElementById "app"))
