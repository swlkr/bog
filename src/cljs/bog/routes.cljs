(ns bog.routes
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [clojure.string :as str]
            [cljs.core.async :refer [>!]])
  (:refer-clojure :exclude [uuid]))

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
