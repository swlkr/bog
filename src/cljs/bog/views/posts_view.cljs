(ns bog.views.posts-view
  (:require [reagent.core :as r]
            [bog.routes :as routes]
            [markdown.core :refer [md->html]]
            [bog.sync :refer [build-server-args build-state-args sync!]]
            [bog.app-state :refer [app-state]]
            [bog.components.header :refer [header]]))

(defn post [p]
  (let [{:keys [title content created_at]} p]
    [:div {:class "col-xs-12 col-sm-6 col-md-4 col-lg-3"}
      [:div {:class "card"}
        [:div {:class "card-block"}
          [:h4 {:class "card-title"} title]]
        [:img {:class "img-responsive" :src "https://placekitten.com/400/400"}]
        [:div {:class "card-block"}
          [:p {:class "card-text" :dangerouslySetInnerHTML {:__html (md->html content)}}]]
        [:div {:class "card-block"}
          [:a {:class "card-link" :href "#"}
            "Continue Reading"
            [:i {:class "fa fa-long-arrow-right m-l-1"}]]]
        [:div {:class "card-footer text-muted"} created_at]]]))

(defn post-list [posts]
  [:div {:class "row"}
    (for [p posts]
      ^{:key (:id p)} [post p])])

(defn posts-view []
  (let [s-args (build-server-args :get "/api/posts")
        st-args (build-state-args assoc :posts)]
    (sync! s-args st-args)
    (fn []
      (let [posts (:posts @app-state)]
        [:div {:class "posts-view"}
          [header]
          [:div {:class "container-fluid"}
            [:div {:class "row"}
              [:div {:class "col-xs-12"}
                [post-list posts]]]]]))))
