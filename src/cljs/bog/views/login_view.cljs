(ns bog.views.login-view
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [quiescent.core :refer [defcomponent]])
  (:require [quiescent.dom :refer [div section i h1 form a]]
            [cljs.core.async :refer [>!]]
            [bog.routes :as routes]
            [bog.actions :as actions]
            [bog.utils :refer [classes]]
            [bog.components.notification :refer [Notification]]
            [bog.components.input :refer [Input]]))

(defcomponent LoginView [state channels]
  (let [{:keys [input-ch login-ch login-res-ch]} channels
        {:keys [message email password info]} state]
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
                        :value email
                        :key :email
                        :channel input-ch})
                (Input {:type "password"
                        :placeholder "Password"
                        :value password
                        :key :password
                        :channel input-ch}))
              (div {:className "level is-mobile m-t-1"}
                (div {:className "level-left"}
                  (div {:className "level-item"}
                    (a {:className "button is-primary"
                        :onClick #(go (>! login-ch {:email email :password password :login-res-ch login-res-ch}))}
                      "Login")))))))))))



(comment let [error (:error @app-state)]
    [:div
      [:section {:class "hero is-light is-fullheight"}
        [:div {:class "hero-body"}
          [:div {:class "container"}
            [:div {:class "columns is-mobile"}
              [:div {:class "column
                            is-10-mobile
                            is-offset-1-mobile
                            is-4-deskto
                            is-offset-4-desktop
                            is-4-tablet
                            is-offset-4-tablet"}
                [:div {:class "logo-container has-text-centered"}
                  [:i {:class "fa fa-2x fa-paper-plane"}]
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
                        "Forgot your password?"]]]]]]]]]])
