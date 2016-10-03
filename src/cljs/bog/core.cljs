(ns bog.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljs.core.async :refer [>! <! chan]]
            [bog.actions :as actions]
            [bog.render :as render]
            [cljs.reader :as r]
            [cljs-http.client :as http]
            [bog.views.login-view :refer [LoginView]]
            [bog.views.new-post-view :refer [NewPostView]]
            [bog.views.home-view :refer [HomeView]]
            [quiescent.core :as q]
            [quiescent.dom :as d])
  (:import goog.history.Html5History))

(enable-console-print!)

(defn init-history
  "Set up Google Closure history management"
  [app]
  (doto (Html5History.)
        (events/listen
          EventType/NAVIGATE
          (fn [evt]
            (let [ch (-> app :channels :route-ch)
                  token (.-token evt)]
              (go (>! ch {:token token :routes (:routes app)})))))
        (.setUseFragment false)
        (.setPathPrefix "")
        (.setEnabled true)))

(defn load
  "Load the application state from HTML5 localstorage, nil if it doesn't exist"
  []
  (when-let [s (.getItem js/localStorage "bog.core")]
    (r/read-string s)))


(def routes {[:login "/login"] LoginView
             [:new-post "/posts/new"] NewPostView
             [:home "/"] HomeView
             [:not-found] (q/defcomponent NotFoundView [] (d/div {} "Not found"))})

(defn get-initial-state
  "Returns a new, empty application state."
  []
  {:view :home
   :message ""
   :info ""
   :email ""
   :password ""})

(defn load-app
  "Return a map containing the initial application"
  []
  {:state (atom (or (load) (get-initial-state)))
   :channels {:route-ch (chan)
              :input-ch (chan)
              :login-ch (chan)
              :login-res-ch (chan)}
   :consumers {:route-ch actions/route-ch
               :input-ch actions/input-ch
               :login-ch actions/login-ch
               :login-res-ch actions/login-res-ch}
   :routes routes})

(defn init-updates
 "For every entry in a map of channel identifiers to consumers,
 initiate a channel listener which will update the application state
 using the appropriate function whenever a value is recieved, as
 well as triggering a render."
 [app]
 (let [{:keys [consumers channels state]} app]
   (doseq [[ch update-fn] consumers]
     (go
        (while true
          (let [val (<! (get channels ch))
                _ (.log js/console (str "on channel [" ch "], recieved value [" val "]"))
                new-state (swap! state update-fn val)]
            (println (str @state))
            (render/request-render app)))))))

(defn store
  "Save the given application state in HTML5 localstorage"
  [state]
  (.setItem js/localStorage "bog.core" (str state)))

(defn init-persistence
  "Given an application, watch it for changes and persist via localstorage"
  [app]
  (add-watch (:state app) :persistence
             (fn [_ _ _ new-state] (store new-state))))

(defn main
  "Application entry point"
  []
  (let [app (load-app)]
    (init-history app)
    (init-persistence app)
    (init-updates app)
    (render/request-render app)))

(main)
