(ns bog.views.preview-draft-view
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [quiescent.core :as q]
            [quiescent.dom :as d]
            [cljs.core.async :refer [<!]]
            [bog.app :as app]
            [bog.api :as api]
            [bog.components.hero :refer [Hero]]))

(defn get-draft [state _]
  (.log js/console "get-draft")
  (go
    (let [{:keys [route-params]} state
          id (:id route-params)
          req {:url (str "/api/drafts/" id) :method :get}
          {:keys [status body]} (<! (api/send state req))]
      (if (= status 200)
        (app/dispatch! :get-draft-res body)
        (app/dispatch! :on-error body))
      (.log js/console (str id))))
  (assoc state :loading true))

(defn get-draft-res [state draft]
  (assoc state :draft draft :loading false))

(app/add-action :get-draft get-draft)
(app/add-action :get-draft-res get-draft-res)

(q/defcomponent PreviewDraftView
  :on-mount #(app/dispatch! :get-draft nil)
  [state]
  (let [{:keys [title content days_ago]} (:draft state)]
    (d/div {}
      (Hero {:is-medium true
             :background-image "https://placehold.it/1024x245"})
      (d/div {:className "container" :style {:marginTop "20px"}}
        (d/div {:className "columns"}
          (d/div {:className "column is-half is-offset-one-quarter"}
            (d/div {:className "content"}
              (d/h1 {:className "title"} title)
              (d/p {} (str days_ago " days ago"))
              (d/p {} content))))))))
