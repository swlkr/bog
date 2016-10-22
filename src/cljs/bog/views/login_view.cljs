(ns bog.views.login-view
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [quiescent.dom :refer [div section i h1 form a]]
            [quiescent.core :as q]
            [cljs.core.async :refer [<!]]
            [bog.utils :refer [classes]]
            [bog.components.notification :refer [Notification]]
            [bog.components.input :refer [Input]]
            [bog.app :refer [add-action dispatch!]]
            [bog.api :as api]))

(defn on-email-change [state email]
  (assoc state :user/email email))

(defn on-password-change [state password]
  (assoc state :user/password password))

(defn on-login-click [state _]
  (go
    (let [{:keys [:user/email :user/password]} state
          req {:url "/api/tokens"
               :method :post
               :body {:email email
                      :password password}}
          res (<! (api/send state req))
          {:keys [status body]} res]
      (if (= status 200)
        (dispatch! :on-login-success body)
        (dispatch! :on-login-err body))))
  (assoc state :info "Logging in..."))

(defn on-login-err [state val]
  (assoc state :message (:message val)
               :info ""))

(defn on-login-success [state val]
  (. js/window.history (pushState "" "" "/drafts"))
  (.setItem js/localStorage "bog.core.access-token" (:access-token val))
  (assoc state :access-token (:access-token val)
               :email ""
               :password ""
               :message ""
               :info ""
               :view :draft-list))

(add-action :on-email-change on-email-change)
(add-action :on-password-change on-password-change)
(add-action :on-login-click on-login-click)
(add-action :on-login-err on-login-err)
(add-action :on-login-success on-login-success)

(q/defcomponent LoginView [state]
  (let [{:keys [message :user/email :user/password info]} state]
    (section {:className "hero is-light is-fullheight"}
      (div {:className "hero-body"}
        (div {:className "container"}
          (div {:className "columns is-mobile"}
            (div {:className "column
                                is-12-mobile
                                is-4-desktop
                                is-offset-4-desktop
                                is-4-tablet
                                is-offset-4-tablet"}

              (div {:className "logo-container has-text-centered"}
                (i {:className "fa fa-2x fa-paper-plane"})
                (h1 {:className "title m-t-3"} "Adventure Walker"))
              (form {:className "m-t-3"}
                (Notification {:message message :is-danger true})
                (Notification {:message info})

                (Input {:type "text"
                        :placeholder "Email"
                        :value (or email "")
                        :onChange (fn [e]
                                    (let [val (-> e .-target .-value)]
                                      (dispatch! :on-email-change val)))})

                (Input {:type "password"
                        :placeholder "Password"
                        :value (or password "")
                        :onChange (fn [e]
                                    (let [val (-> e .-target .-value)]
                                      (dispatch! :on-password-change val)))}))

              (div {:className "level is-mobile m-t-1"}
                (div {:className "level-left"}
                  (div {:className "level-item"}
                    (a {:className "button is-primary"
                        :onClick #(dispatch! :on-login-click nil)}
                      "Login")))))))))))
