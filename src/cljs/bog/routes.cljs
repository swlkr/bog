(ns bog.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [bog.app-state :refer [app-state]]
            [bog.local-storage :as storage]
            [clojure.string :as str]))

(def routes ["/" {"" :posts
                  "login" :login
                  "new-post" :new-post}])

(def url-for (partial bidi/path-for routes))

(defn has-token? []
  (let [token (storage/get-item "access-token")]
    (and (not (nil? token))
         (not (str/blank? token)))))

(def auth-routes [:new-post])

(defn set-page! [match]
  (let [route (:handler match)]
    (if (and
          (not (has-token?))
          (= route :new-post))
      (aset js/location "href" "/login")
      (swap! app-state assoc :view route))))

(def history
  (pushy/pushy set-page! (partial bidi/match-route routes)))

(defn app-routes []
  (pushy/start! history))
