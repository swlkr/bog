(ns bog.views.draft-list-view
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [quiescent.core :as q]
            [quiescent.dom :as d]
            [cljs.core.async :refer [>!]]
            [bog.components.hero :refer [Hero]]
            [bog.components.link :refer [Link]]
            [bog.app :refer [dispatch! add-action app]]
            [bog.api :as api]
            [bog.utils :as utils]))

(defn on-delete-draft-click [state draft]
  (dispatch! :loading true)
  (go
    (let [req {:url (str "/api/drafts/" (:id draft)) :method :delete}
          res (<! (api/send state req))
          {:keys [status body]} res]
      (dispatch! :loading false)
      (if (= status 200)
        (dispatch! :on-delete-draft-success body)
        (dispatch! :on-delete-draft-err body)))))

(defn on-delete-draft-success [state body]
  (let [new-drafts (utils/remove-from-coll-by-id (:drafts state) body)]
    (assoc state :drafts new-drafts)))

(defn on-delete-draft-err [state body]
  (assoc state :message (:message body)))

(add-action :on-delete-draft-click on-delete-draft-click)
(add-action :on-delete-draft-success on-delete-draft-success)
(add-action :on-delete-draft-err on-delete-draft-err)

(q/defcomponent DraftRow
  :keyfn :id
  [{:keys [id title] :as draft}]
  (d/tr {}
    (d/td {}
      (Link {:href (str "/drafts/" id)} title))
    (d/td {:style {:width "90%"}})
    (d/td {}
      (Link {:href (str "/drafts/" id)} "Edit"))
    (d/td {}
      (Link {:href (str "/drafts/" id)} "Preview"))
    (d/td {}
      (Link {:onClick (fn [e]
                       (let [c (js/confirm "Are you sure?")]
                        (when (= c true)
                          #(dispatch! :on-delete-draft-click draft))))}
          "Delete"))))

(defn get-drafts [state _]
  (go
    (let [req {:url "/api/drafts" :method :get}
          {:keys [status body]} (<! (api/send state req))]
      (if (= status 200)
        (dispatch! :get-drafts-res body)
        (dispatch! :get-drafts-res-err body))))
  (assoc state :info "Getting drafts..."))

(defn get-drafts-res-err [state body]
  (assoc state :message (:message body) :info ""))

(defn get-drafts-res [state drafts]
  (assoc state :drafts drafts :info ""))

(add-action :get-drafts get-drafts)
(add-action :get-drafts-res get-drafts-res)
(add-action :get-drafts-res-err get-drafts-res-err)

(q/defcomponent DraftListView
  :on-mount #(dispatch! :get-drafts nil)
  [state]
  (let [{:keys [drafts]} state]
    (d/div {}
      (Hero {:title "Drafts"
             :subtitle "Here's a list of drafts"})
      (d/div {:className "container m-t-3"}
        (d/div {:className "columns"}
          (d/div {:className "column"}
            (Link {:href "/drafts/new"} "New Draft")
            (d/table {:className "table m-t-3"}
              (d/tbody {}
                (map DraftRow drafts)))))))))
