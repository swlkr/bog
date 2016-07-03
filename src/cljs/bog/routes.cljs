(ns bog.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [bog.app-state :refer [app-state]]))

(def routes ["/" {"" :posts
                  "login" :login}])

(def url-for (partial bidi/path-for routes))

(defn set-page! [match]
  (let [route (:handler match)]
    (swap! app-state assoc :view route)))

(def history
  (pushy/pushy set-page! (partial bidi/match-route routes)))

(defn app-routes []
  (pushy/start! history))
