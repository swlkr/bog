(ns bog.views.login-view
   (:require [reagent.core :as r]
             [bog.routes :as routes]))

(defn login-view []
 [:div.container-fluid
   [:div.row
     [:div.col-xs-12
       [:h1 "Login page"]
       [:a {:href (routes/url-for :posts)} "posts"]]]])
