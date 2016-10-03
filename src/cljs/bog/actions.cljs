(ns bog.actions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [bog.app-state :refer [app-state]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [bog.local-storage :as storage]
            [bog.routes :as routes]))

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

(defn input-ch [state {:keys [key val]}]
  (assoc state key val))

(defn login-ch [state val]
  (let [{:keys [email password login-res-ch]} val]
    (go
      (let [res (<! (http/post "/api/tokens" {:json-params {:email email :password password}}))]
        (>! login-res-ch res)))
    (assoc state :info "Logging in...")))

(defn login-res-ch [state response]
  (let [{:keys [status body]} response]
    (if (= status 200)
      (do
        (. js/window.history (pushState "" "" "posts/new"))
        (assoc state :access-token (:access-token body)
                     :email ""
                     :password ""
                     :message ""
                     :info ""
                     :view :new-post))
      (assoc state :message (:message body)
                   :info ""))))

(defonce auth-routes #{:new-post})

(defn get-view [routes token]
  (->> routes
       (keys)
       (filter #(= token (peek %)))
       (first)
       (first)))

(defn token->view [routes token]
  (let [view (get-view routes token)]
    (if (nil? view)
      (->> routes
           (keys)
           (filter #(= (count %) 1))
           (first)
           (first))
      view)))

(defn route-ch
  "Given an application state, set the current view based on the URL token"
  [state val]
  (let [{:keys [token routes]} val
        view (token->view routes token)]
    (if (and
          (nil? (:access-token state))
          (contains? auth-routes view))
      (do
        (. js/window.history (pushState "" "" "/login"))
        (assoc state :view :login))
      (assoc state :view view))))
