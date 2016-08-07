(ns bog.views.post-view
  (:require [reagent.core :as r]
            [bog.routes :as routes]
            [markdown.core :refer [md->html]]
            [bog.sync :refer [build-args sync!]]
            [bog.app-state :refer [app-state]]
            [bog.components.header :refer [header]]))

(defn post-view []
  (let [{:keys [id]} (:route-params @app-state)
        server (build-args :get (str "/api/posts/" id))
        state [:post]]
    (sync! server state)
    (fn []
      (let [{:keys [title content]} (:post @app-state)]
        [:div {:class "post-view"}
          [header :title title]
          [:div {:class "container-fluid"}
            [:div {:class "row"}
              [:div {:class "col-xs-12 col-sm-8 col-sm-offset-2"}
                [:div {:class "post-content-container"}
                  [:div {:class "post-content"
                         :dangerouslySetInnerHTML {:__html (md->html content)}}]]]]]]))))
