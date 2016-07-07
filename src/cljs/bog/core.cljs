(ns bog.core
  (:require [reagent.core :as reagent :refer [atom]]
            [bog.app-state :refer [app-state]]
            [bog.routes :refer [app-routes]]
            [bog.views.posts-view :refer [posts-view]]
            [bog.views.login-view :refer [login-view]]
            [bog.components.navbar :refer [navbar]]
            [bog.components.header :refer [header]]))

(enable-console-print!)

(app-routes)

(defmulti views identity)
(defmethod views :posts [] posts-view)
(defmethod views :login [] login-view)
(defmethod views :default [] (:h1 "Not found"))

(defn app []
  (let [{:keys [view]} @app-state
        c (views view)]
    [:div
      [navbar]
      [header]
      [c]]))

(reagent/render [app] (js/document.getElementById "app"))
