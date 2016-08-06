(ns bog.views.edit-draft-view
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [bog.routes :as routes]
            [bog.app-state :refer [app-state]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [bog.components.post-form :refer [post-form]]
            [markdown.core :refer [md->html]]
            [bog.actions :as actions]))

(defn get-draft! [id]
  (go
    (let [url (str "/api/drafts/" id)
          {:keys [access-token]} @app-state
          {:keys [status body]} (<! (http/get url {:headers {"Authorization" access-token}}))]
      (swap! app-state assoc :edit-draft body))))

(defn edit-draft-view []
  (let [{:keys [route-params]} @app-state
        {:keys [id]} route-params]
    (get-draft! id)
    (fn []
      [post-form {:paths {:title [:edit-draft :title]
                          :content [:edit-draft :content]
                          :type [:edit-draft :type]}
                  :page-title "Edit Draft"
                  :app-state app-state
                  :on-save-draft-click actions/update-draft!}])))
