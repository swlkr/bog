(ns bog.views.login-view
   (:require [reagent.core :as r]
             [bog.routes :as routes]
             [bog.app-state :refer [app-state]]
             [bog.components.input :refer [input]]
             [bog.actions :as actions]
             [bog.utils :refer [classes]]))

(defn notification [{:keys [message is-danger is-info]}]
  (when message
    (let [class (classes {"notification" true
                          "is-danger" is-danger})]
      [:div {:class class}
        message])))

(defn login-view []
  (let [error (:error @app-state)]
    [:div
      [:section {:class "hero is-light is-fullheight"}
        [:div {:class "hero-body"}
          [:div {:class "container"}
            [:div {:class "columns is-mobile"}
              [:div {:class "column is-half is-offset-one-quarter"}
                [:div {:class "logo-container has-text-centered"}
                  [:i {:class "fa fa-5x fa-paper-plane"}]
                  [:h1 {:class "title m-t-3"} "Adventure Walker"]]
                [:form {:class "m-t-3"}
                  [notification {:message error
                                 :is-danger true}]
                  [input :type "text"
                         :placeholder "your@email.com"
                         :class "input"
                         :path [:login :email]
                         :state app-state]
                  [input :type "password"
                         :placeholder "password"
                         :class "input"
                         :path [:login :password]
                         :state app-state]]
                [:div {:class "level is-mobile m-t-1"}
                  [:div {:class "level-left"}
                    [:div {:class "level-item"}
                      [:a {:class "button is-primary"
                           :on-click actions/login} "Login"]]]
                  [:div
                    [:div {:class "level-item"}
                      [:a {:href (routes/url-for :forgot-password)}
                        "Forgot your password?"]]]]]]]]]]))
