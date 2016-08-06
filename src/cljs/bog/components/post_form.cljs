(ns bog.components.post-form
   (:require [reagent.core :as r]
             [bog.routes :as routes]
             [bog.app-state :refer [app-state]]
             [bog.components.input :refer [input]]
             [bog.components.textarea :refer [textarea]]
             [bog.components.list-group :refer [list-group]]
             [markdown.core :refer [md->html]]))

(defn post-form [props]
  (let [{:keys [paths page-title app-state on-save-draft-click on-publish-click]} props
        title (get-in @app-state (:title paths))
        content (get-in @app-state (:content paths))
        enc-content (md->html content)
        type (get-in @app-state (:type paths))]
    [:div {:class "container-fluid m-t-3"}
      [:div {:class "row"}
        [:div {:class "col-xs-12 col-sm-6 col-md-4"}
          [:h1 page-title]
          [:form
            [:div {:class "form-group"}
              [:label "Title"]
              [input :type "text"
                     :placeholder "Title goes here"
                     :class "form-control"
                     :path (:title paths)
                     :state app-state]]
            [:div {:class "form-group"}
              [:label "Content"]
              [textarea :placeholder "Body goes here"
                        :class "form-control"
                        :rows 10
                        :path (:content paths)
                        :state app-state]]
            [:div {:class "form-group"}
              [:label "Type"]
              [list-group {:items [{:text "Post" :value "post"}
                                   {:text "Quote" :value "quote"}
                                   {:text "Slideshow" :value "slideshow"}]
                           :path (:type paths)
                           :state app-state}]]
            [:button {:type "button"
                      :class "btn btn-default m-r-1"
                      :on-click on-save-draft-click}
              "Save Draft"]
            [:button {:type "button"
                      :class "btn btn-primary m-r-1"
                      :on-click on-publish-click}
              "Publish"]
            [:button {:type "button" :class "btn btn-link"} "Add Image"]]]
        [:div {:class "col-xs-12 col-sm-6 col-md-8"}
          [:h1 "Preview"]
          [:h3 title]
          [:div {:dangerouslySetInnerHTML {:__html enc-content}}]]]]))
