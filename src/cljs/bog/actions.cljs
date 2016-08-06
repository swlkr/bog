(ns bog.actions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [bog.app-state :refer [app-state]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [bog.local-storage :as storage]
            [bog.routes :as routes]))

(defn login [e]
  (let [email (-> @app-state :login :email)
        pw (-> @app-state :login :password)]
    (.preventDefault e)
    (swap! app-state assoc :error "")
    (go
        (let [url "/api/tokens"
              {:keys [status body]} (<! (http/post url {:json-params {:email email :password pw}}))]
          (if (= 200 status)
            (do
              (swap! app-state assoc :access-token (:access-token body))
              (swap! app-state assoc :view :new-post)
              (swap! app-state update-in [:login] {:email "" :password ""})
              (storage/set-item! "access-token" (:access-token body)))
            (swap! app-state assoc :error (:message body)))))))

(defn toggle-navbar []
  (let [{:keys [navbar-collapsed]} @app-state]
    (swap! app-state assoc :navbar-collapsed (not navbar-collapsed))))

(defn create-draft! []
  (let [{:keys [access-token new-post]} @app-state
        {:keys [title content type]} new-post]
    (swap! app-state assoc :error "")
    (go
        (let [url "/api/posts"
              {:keys [status body]} (<! (http/post url {:json-params {:title title
                                                                      :content content
                                                                      :type type
                                                                      :draft true}
                                                        :headers {"Authorization" access-token}}))]
          (if (= 200 status)
            (do
              (swap! app-state update-in [:new-post] {:title "" :content "" :type "post"})
              (routes/push! "/drafts"))
            (swap! app-state assoc :error (:message body)))))))

(defn update-draft! []
  (let [{:keys [access-token edit-draft]} @app-state
        {:keys [title content type id]} edit-draft]
    (swap! app-state assoc :error "")
    (go
        (let [url (str "/api/posts/" id)
              {:keys [status body]} (<! (http/put url {:json-params {:title title
                                                                     :content content
                                                                     :type type
                                                                     :draft true}
                                                        :headers {"Authorization" access-token}}))]
          (if (= 200 status)
            (do
              (swap! app-state update-in [:edit-draft] {:title "" :content "" :type "post"})
              (swap! app-state assoc :view :drafts))
            (swap! app-state assoc :error (:message body)))))))
