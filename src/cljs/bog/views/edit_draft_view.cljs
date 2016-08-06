(ns bog.views.edit-draft-view
  (:require [reagent.core :as r]
            [bog.routes :as routes]
            [bog.app-state :refer [app-state]]
            [bog.sync :refer [build-args sync!]]
            [bog.utils :refer [find-index-by-id]]
            [bog.components.post-form :refer [post-form]]
            [markdown.core :refer [md->html]]
            [bog.actions :as actions]))

(defn on-publish-click []
  (let [{:keys [edit-draft drafts]} @app-state
        body (assoc edit-draft :draft false)
        server (build-args :put (str "/api/posts/" (:id edit-draft)) :body body)
        index (find-index-by-id drafts (:id edit-draft))
        state [drafts index]]
    (sync! server state :auth true)
    (swap! app-state assoc :view :posts)))

(defn edit-draft-view []
  (let [{:keys [id]} (:route-params @app-state)
        server (build-args :get (str "/api/drafts/" id))
        state [:edit-draft]]
    (sync! server state :auth true)
    (fn []
      [post-form {:paths {:title [:edit-draft :title]
                          :content [:edit-draft :content]
                          :type [:edit-draft :type]}
                  :page-title "Edit Draft"
                  :app-state app-state
                  :on-save-draft-click actions/update-draft!
                  :on-publish-click on-publish-click}])))
