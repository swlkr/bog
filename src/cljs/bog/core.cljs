(ns bog.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljs.core.async :refer [>! <!]]
            [bidi.bidi :as bidi]
            [bog.render :as render]
            [bog.app :refer [app dispatch! add-action]])
  (:import goog.history.Html5History))

(defn init-history
  "Set up Google Closure history management"
  [app]
  (doto (Html5History.)
        (events/listen
          EventType/NAVIGATE
          (fn [evt]
            (let [token (.-token evt)]
              (dispatch! :on-url-change token))))
        (.setUseFragment false)
        (.setPathPrefix "")
        (.setEnabled true)))

(defn init-updates
 "For every entry in a map of channel identifiers to handlers,
 initiate a channel listener which will update the application state
 using the appropriate function whenever a value is recieved, as
 well as triggering a render."
 [app]
 (let [{:keys [functions channels state]} app]
   (doseq [[ch update-fn] functions]
     (go
        (while true
          (let [val (<! (get channels ch))
                _ (.log js/console (str "on channel [" ch "], recieved value [" val "]"))
                new-state (swap! state update-fn val)]
            (render/request-render app)))))))

(defn main
  "Application entry point"
  []
  (init-history @app)
  (init-updates @app)
  (render/request-render @app))

(main)
