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
                  :text "New Post"}]))

(defn drafts-link [access-token]
  (when (not (nil? access-token))
    [navbar-link {:href (url-for :drafts)
                  :text "Drafts"}]))

(defn navbar []
  (let [{:keys [navbar-collapsed access-token navbar-bg-white]} @app-state
        navbar-class (classes {"navbar-collapse" true
                               "collapse" navbar-collapsed})
        navbar-default-class (classes {"navbar" true
                                       "navbar-default" true
                                       "navbar-fixed-top" true
                                       "bg-white" navbar-bg-white})]
    [:nav {:class navbar-default-class}
      [:div {:class "container"}
        [:div {:class "navbar-header"}
          [:button {:class "navbar-toggle collapsed" :on-click actions/toggle-navbar}
            [:span {:class "icon-bar"}]
            [:span {:class "icon-bar"}]
            [:span {:class "icon-bar"}]]
          [:div {:class "navbar-brand text-center"}
            [:a {:href (url-for :posts)}
              "Adventure Walker"]]]
        [:div {:class navbar-class}
          [:ul {:class "nav navbar-nav navbar-right"}
            [navbar-link {:href (url-for :posts)
                          :text "Home"}]
            [new-post-link access-token]
            [drafts-link access-token]
            [navbar-link {:href "https://instagram.com/adventure_walker"
                          :text "Instagram"}]
            [navbar-link {:href "https://twitter.com/adventurewalkr"
                          :text "Twitter"}]
            [navbar-link {:href "https://www.pinterest.com/adventurewalkr"
                          :text "Pinterest"}]]]]]))
