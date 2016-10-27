(ns bog.render
  (:require [quiescent.core :as q]
            [quiescent.dom :as d]
            [bog.routes :as routes]
            [bog.views.login-view :refer [LoginView]]
            [bog.views.home-view :refer [HomeView]]
            [bog.views.draft-list-view :refer [DraftListView]]
            [bog.views.new-draft-view :refer [NewDraftView]]
            [bog.views.preview-draft-view :refer [PreviewDraftView]]
            [bog.views.edit-draft-view :refer [EditDraftView]]))

(defn view->component [view]
  (condp = view
    :login LoginView
    :draft-list DraftListView
    :new-draft NewDraftView
    :preview-draft PreviewDraftView
    :edit-draft EditDraftView
    :home HomeView
    HomeView))
    ; not found

(q/defcomponent App
  "The root of the application"
  [state]
  (let [{:keys [view]} state
        component (view->component view)]
    (component state)))

;; Here we use an atom to tell us if we already have a render queued
;; up; if so, requesting another render is a no-op
(let [render-pending? (atom false)]
  (defn request-render
    "Render the given application state tree."
    [app]
    (when (compare-and-set! render-pending? false true)
      (.requestAnimationFrame js/window
                              (fn []
                                (q/render (App @(:state app))
                                          (.getElementById js/document "app")))
                              (reset! render-pending? false)))))
