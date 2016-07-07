(ns bog.views.posts-view
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [bog.routes :as routes]
            [bog.app-state :refer [app-state]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [markdown.core :refer [md->html]]))

(defn get-posts! []
  (go
    (let [url "/api/posts"
          response (<! (http/get url))]
      (swap! app-state assoc :posts (-> response :body :posts)))))

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
  (get-posts!)
  (fn []
    (let [{:keys [posts]} @app-state]
      [:div {:class "container-fluid"}
        [:div {:class "row"}
          [:div {:class "col-xs-12"}
            [post-list posts]]]])))
