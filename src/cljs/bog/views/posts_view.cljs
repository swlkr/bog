(ns bog.views.posts-view
  (:require [reagent.core :as r]
            [bog.routes :as routes]
            [markdown.core :refer [md->html]]
            [bog.sync :refer [build-args sync!]]
            [bog.app-state :refer [app-state]]
            [bog.components.header :refer [header]]))

(defn post [p]
  (let [{:keys [id title content created_at]} p]
    [:div {:class "col-xs-12 col-sm-6 col-md-4 col-lg-3"}
      [:div {:class "card"}
        [:div {:class "card-block"}
          [:h4 {:class "card-title"} title]]
        [:img {:class "img-responsive" :src "https://placekitten.com/400/400"}]
        [:div {:class "card-block"}
          [:p {:class "card-text" :dangerouslySetInnerHTML {:__html (subs (md->html content) 0 140)}}]]
        [:div {:class "card-block"}
          [:a {:class "card-link" :href (routes/url-for :post :id id)}
            "Continue Reading"
            [:i {:class "fa fa-long-arrow-right m-l-1"}]]]
        [:div {:class "card-footer text-muted"} created_at]]]))

(defn post-list [posts]
  [:div {:class "row"}
    (for [p posts]
      ^{:key (:id p)} [post p])])

(defn posts-view []
  (let [server (build-args :get "/api/posts")
        state [:posts]]
    (sync! server state)
    (fn []
      (let [posts (:posts @app-state)]
        [:div {:class "posts-view"}
          [header :src "https://scontent-lax3-1.cdninstagram.com/t51.2885-15/e35/13531913_935576759884344_1748044273_n.jpg?ig_cache_key=MTI4NzE4MjMyMTI3OTIxMjA2Mw%3D%3D.2"
                  :title "Find Adventure"]
          [:div {:class "container-fluid"}
            [:div {:class "row"}
              [:div {:class "col-xs-12"}
                [post-list posts]]]]]))))
