(ns bog.components.navbar)

(defn navbar []
  [:nav {:class "navbar navbar-default navbar-fixed-top"}
    [:div {:class "container-fluid"}
      [:div {:class "navbar-header"}
        [:button {:class "navbar-toggle collapsed"}
          [:span {:class "icon-bar"}]
          [:span {:class "icon-bar"}]
          [:span {:class "icon-bar"}]]
        [:div {:class "navbar-brand text-center"} "Adventure Walker"
          (comment
            [:ul {:class "pull-left list-inline"}
              [:li
                [:a {:href "https://instagram.com/adventure_walker"}
                  [:i {:class "fa fa-1x fa-instagram"}]]]
              [:li
                [:a {:href "https://twitter.com/adventurewalkr"}
                  [:i {:class "fa fa-1x fa-twitter"}]]]
              [:li
                [:a {:href "https://www.pinterest.com/adventurewalkr"}
                  [:i {:class "fa fa-1x fa-pinterest"}]]]])]]]])
