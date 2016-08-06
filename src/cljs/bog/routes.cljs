(ns bog.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [bog.app-state :refer [app-state]]
            [bog.local-storage :as storage]
            [clojure.string :as str]))

(def routes ["/" {"" :posts
                  "login" :login
                  "posts" {"" :new-post}
                  "drafts" {"" :drafts
                            ["/" :id "/edit"] :edit-draft}}])

(def url-for (partial bidi/path-for routes))

(defn has-token? []
  (let [token (storage/get-item "access-token")]
    (and (not (nil? token))
         (not (str/blank? token)))))

(def auth-routes [:new-post])

(defn set-page! [match]
  (let [{:keys [handler route-params]} match]
    (if (and
          (not (has-token?))
          (= handler :new-post))
      (aset js/location "href" "/login")
      (swap! app-state assoc :view handler :route-params route-params))))

(def history
  (pushy/pushy set-page! (partial bidi/match-route routes)))

(defn push! [path]
  (pushy/set-token! history path))

(defn app-routes []
  (pushy/start! history))
