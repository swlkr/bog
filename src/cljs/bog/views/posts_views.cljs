(ns bog.views.posts-view
   (:require [reagent.core :as r]
             [bog.routes :as routes]))

(defn posts-view []
 [:div.container-fluid
   [:div.row
     [:div.col-xs-12
       [:h1 "Posts page"]
       [:a {:href (routes/url-for :login)} "login"]]]])
