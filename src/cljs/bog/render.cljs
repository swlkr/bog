(ns bog.render
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [>!]]
            [quiescent.core :as q]
            [quiescent.dom :as d]))

(defn view->component [routes view]
  (->> routes
       (keys)
       (filter #(= view (first %)))
       (first)
       (get routes)))

(q/defcomponent App
  "The root of the application"
  [state channels routes]
  (let [{:keys [view]} state
        current-view (view->component routes view)]
    (current-view state channels)))

;; Here we use an atom to tell us if we already have a render queued
;; up; if so, requesting another render is a no-op
(let [render-pending? (atom false)]
  (defn request-render
    "Render the given application state tree."
    [app]
    (when (compare-and-set! render-pending? false true)
      (.requestAnimationFrame js/window
                              (fn []
                                (q/render (App @(:state app) (:channels app) (:routes app))
                                          (.getElementById js/document "app")))
                              (reset! render-pending? false)))))
