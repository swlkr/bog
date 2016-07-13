(ns bog.components.navbar
  (:require [bog.routes :refer [url-for]]
            [bog.app-state :refer [app-state]]
            [bog.actions :as actions]
            [bog.utils :refer [classes]]
            [reagent.core :as r]))

(defn navbar-link [{:keys [href text icon]}]
  [:li
    [:a {:href href
         :on-click (fn [] (actions/toggle-navbar))}
      [:i {:class (str "m-r-1 fa fa-1x fa-" icon)}]
      text]])

(defn new-post-link [access-token]
  (when (not (nil? access-token))
    [navbar-link {:href (url-for :new-post)
                  :icon "asterisk"
                  :text "New Post"}]))

(defn navbar []
  (let [{:keys [navbar-collapsed access-token]} @app-state
        navbar-class (classes {"navbar-collapse" true
                                  "collapse" (not navbar-collapsed)})]
    [:nav {:class "navbar navbar-default navbar-fixed-top"}
      [:div {:class "container-fluid"}
        [:div {:class "navbar-header"}
          [:button {:class "navbar-toggle collapsed" :on-click actions/toggle-navbar}
            [:span {:class "icon-bar"}]
            [:span {:class "icon-bar"}]
            [:span {:class "icon-bar"}]]
          [:div {:class "navbar-brand text-center" :style {:marginLeft "42px"}} "Adventure Walker"]]
        [:div {:class navbar-class}
          [:ul {:class "nav navbar-nav navbar-right"}
            [navbar-link {:href (url-for :posts)
                          :icon "home"
                          :text "Home"}]
            [new-post-link access-token]
            [navbar-link {:href "https://instagram.com/adventure_walker"
                          :icon "instagram"
                          :text "Instagram"}]
            [navbar-link {:href "https://twitter.com/adventurewalkr"
                          :icon "twitter"
                          :text "Twitter"}]
            [navbar-link {:href "https://www.pinterest.com/adventurewalkr"
                          :icon "pinterest"
                          :text "Pinterest"}]]]]]))
