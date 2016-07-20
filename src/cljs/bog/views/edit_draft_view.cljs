(ns bog.views.edit-draft-view
   (:require [reagent.core :as r]
             [bog.routes :as routes]
             [bog.app-state :refer [app-state]]
             [bog.components.post-form :refer [post-form]]
             [markdown.core :refer [md->html]]
             [bog.actions :as actions]))

(defn edit-draft-view []
  [post-form {:paths {:title [:edit-draft :title]
                      :content [:edit-draft :content]
                      :type [:edit-draft :type]}
              :page-title "Edit Draft"
              :app-state app-state
              :on-save-draft-click actions/update-draft!}])
