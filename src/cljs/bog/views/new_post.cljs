(ns bog.views.new-post
   (:require [reagent.core :as r]
             [bog.routes :as routes]
             [bog.app-state :refer [app-state]]
             [bog.components.input :refer [input]]
             [bog.components.textarea :refer [textarea]]
             [markdown.core :refer [md->html]]))

(defn new-post []
  (let [title (-> @app-state :new-post :title)
        content (-> @app-state :new-post :content md->html)]
    [:div {:class "container-fluid m-t-3"}
      [:div {:class "row"}
        [:div {:class "col-xs-12 col-sm-6 col-md-4"}
          [:h1 "New Post"]
          [:form
            [:div {:class "form-group"}
              [input :type "text"
                     :placeholder "Title goes here"
                     :class "form-control"
                     :path [:new-post :title]
                     :state app-state]]
            [:div {:class "form-group"}
              [textarea :placeholder "Body goes here"
                         :class "form-control"
                         :rows 10
                         :path [:new-post :content]
                         :state app-state]]
            [:button {:type "button" :class "btn btn-default m-r-1"} "Save Draft"]
            [:button {:type "button" :class "btn btn-primary m-r-1"} "Publish"]
            [:button {:type "button" :class "btn btn-link"} "Add Image"]]]
        [:div {:class "col-xs-12 col-sm-6 col-md-8"}
          [:h1 "Preview"]
          [:h3 title]
          [:div {:dangerouslySetInnerHTML {:__html content}}]]]]))