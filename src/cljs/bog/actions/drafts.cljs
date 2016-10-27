(ns bog.actions.drafts
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [bog.app :as app]
            [bog.api :as api])
  (:refer-clojure :exclude [get update list]))

(defn get [state _]
  (go
    (let [{:keys [route-params]} state
          id (:id route-params)
          req {:url (str "/api/drafts/" id)
               :method :get}
          {:keys [status body]} (<! (api/send state req))]
      (if (= status 200)
        (app/dispatch! :drafts/get-res body)
        (app/dispatch! :on-error body))))
  (assoc state :loading true))

(defn get-res [state draft]
  (assoc state :draft draft :loading false))

(defn change [state [path v]]
  (assoc-in state path v))

(defn update [state _]
  (go
    (let [{:keys [id title content]} (:draft state)
          req {:method :put
               :url (str "/api/drafts/" id)
               :body {:title title
                      :content content
                      :type type
                      :id id}}
          {:keys [status body]} (<! (api/send state req))]
      (if (= status 200)
        (app/dispatch! :drafts/update-res body)
        (app/dispatch! :on-error body))))
  (assoc state :submitting true))

(defn update-res [state draft]
  (app/dispatch! :on-url-change "/drafts")
  (assoc state :submitting false :draft draft))

(defn create [state _]
  (go
    (let [{:keys [title content]} (:new-draft state)
          req {:method :post
               :url "/api/drafts"
               :body {:title title
                      :content content
                      :id (str (random-uuid))
                      :sort_order 0
                      :type :post
                      :published false}}
          {:keys [status body]} (<! (api/send state req))]
      (if (= status 200)
        (app/dispatch! :drafts/create-res body)
        (app/dispatch! :on-error body))))
  (assoc state :submitting true))

(defn create-res [state draft]
  (app/dispatch! :on-url-change "/drafts")
  (assoc state :submitting false :draft draft :new-draft {}))

(defn delete! [state draft]
  (go
    (let [req {:url (str "/api/drafts/" (:id draft)) :method :delete}
          res (<! (api/send state req))
          {:keys [status body]} res]
      (if (= status 200)
        (app/dispatch! :drafts/delete-res body)
        (app/dispatch! :on-error body))))
  (assoc state :loading true))

(defn delete-res [state {:keys [id]}]
  (let [drafts (filterv #(not= id (:id %)) (:drafts state))]
    (assoc state :drafts drafts)))

(defn list [state _]
  (go
    (let [req {:url "/api/drafts" :method :get}
          {:keys [status body]} (<! (api/send state req))]
      (if (= status 200)
        (app/dispatch! :drafts/list-res body)
        (app/dispatch! :on-error body))))
  (assoc state :loading true))

(defn list-res [state drafts]
  (assoc state :drafts drafts :info ""))

(app/add-action :drafts/list list)
(app/add-action :drafts/list-res list-res)

(app/add-action :drafts/delete delete!)
(app/add-action :drafts/delete-res delete-res)

(app/add-action :drafts/change change)

(app/add-action :drafts/create create)
(app/add-action :drafts/create-res create-res)

(app/add-action :drafts/get get)
(app/add-action :drafts/get-res get-res)

(app/add-action :drafts/update update)
(app/add-action :drafts/update-res update-res)
