(ns bog.components.link
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [quiescent.dom :as d]
            [quiescent.core :as q]
            [bog.app :refer [dispatch!]]))

(q/defcomponent Link [{:keys [href className label style onClick]} & children]
  (d/a {:className className
        :style style
        :href "#"
        :onClick (fn [e]
                  (.preventDefault e)
                  (if (nil? href)
                    (onClick e)
                    (dispatch! :on-url-change href)))}
    children))
