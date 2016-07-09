(ns bog.views.login-view
   (:require [reagent.core :as r]
             [bog.routes :as routes]
             [bog.app-state :refer [app-state]]
             [bog.components.input :refer [input]]
             [bog.actions :as actions]))

(defn login-view []
  (let [error (:error @app-state)]
    [:div {:class "container-fluid max-height"}
      [:div {:class "logo-container text-center"}
        [:i {:class "logo fa fa-5x fa-leaf"}]]
      [:form {:class "m-t-3"}
        [:div {:class (str "alert alert-danger" (if (nil? error) "hidden" ""))}
          [:strong "Oh snap! "]
          error]
        [:div {:class "form-group"}
          [input :type "text"
                 :placeholder "example@your-email.com"
                 :class "form-control input-lg"
                 :path [:login :email]
                 :state app-state]]
        [:div {:class "form-group"}
          [input :type "password"
                 :placeholder "Your password"
                 :class "form-control input-lg"
                 :path [:login :password]
                 :state app-state]]
        [:button {:class "btn btn-primary btn-block btn-lg"
                  :on-click actions/login} "Login"]]
      [:div {:class "text-center m-t-2"}
        [:a {:href (routes/url-for :forgot-password)}
          "Forgot your password?"]]]))
