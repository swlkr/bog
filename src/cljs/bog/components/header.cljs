(ns bog.components.header
  (:require [bog.app-state :refer [app-state]]))

(defn header [& {:keys [src title sub-title]}]
  [:div {:class "header jumbotron text-center"
         :style {:background-image (str "-webkit-image-set( url(" src ") 1x, url(" src ") 2x)")}};}}]}
    [:div {:class "overlay"}
      [:p {:class "text"} title]]])

(defn on-scroll []
  (let [y (-> js/window .-scrollY)]
    (if (> y 50)
      (swap! app-state assoc :navbar-bg-white true)
      (swap! app-state assoc :navbar-bg-white false))))

(.addEventListener js/window "scroll" on-scroll false)
