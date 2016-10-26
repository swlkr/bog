(ns bog.routes
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [clojure.string :as str]
            [cljs.core.async :refer [>!]]
            [bog.app :refer [dispatch! add-action]])
  (:refer-clojure :exclude [uuid]))

(def app-routes ["/" {"" :home
                      "login" :login
                      "drafts" {"" :draft-list
                                "/new" :new-draft
                                ["/" :id "/edit"] :edit-draft
                                ["/" :id] :preview-draft}}])

(def auth-routes #{:new-draft :draft-list :preview-draft :edit-draft})

(defn on-url-change [state token]
  (let [{:keys [handler route-params]} (bidi/match-route app-routes token)]
    (if (and
          (nil? (:access-token state))
          (contains? auth-routes handler))
      (do
        (. js/window.history (pushState "" "" "/login"))
        (assoc state :view :login))
      (assoc state :view handler :route-params route-params))))

(add-action :on-url-change on-url-change)

(def routes [])

(def url-for (partial bidi/path-for routes))

(defn set-page! [channels match]
  (let [{:keys [handler route-params]} match]
    (go
      (>! (:route-ch channels) {:handler handler :route-params route-params}))))

(defn push! [path])
  ;(pushy/set-token! history path))

(defn init-routing! [app]
  (let [{:keys [channels routes]} app
        history (pushy/pushy (partial set-page! channels) (partial bidi/match-route routes))]
    (pushy/start! history)))
