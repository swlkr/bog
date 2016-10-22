(ns bog.app
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [>! chan]]))

(defn get-initial-state []
  {:view :home
   :message ""
   :access-token ""
   :info ""
   :drafts []
   :posts []
   :new-draft/content ""
   :new-draft/title ""
   :user/email ""
   :user/password ""
   :route-params {}})

(defn get-access-token []
  (let [access-token (.getItem js/localStorage "bog.core.access-token")]
    {:access-token access-token}))

(defn init-app []
  (atom {:state (atom (merge (get-initial-state) (get-access-token)))
         :channels {}
         :functions {}}))

(def app (init-app))

(defn add-action-fn [app [name f]]
  (-> app
      (update-in [:functions] assoc name f)
      (update-in [:channels] assoc name (chan))))

(defn add-action [name f]
  (swap! app add-action-fn [name f]))

(defn dispatch! [name arg]
  (let [ch (get-in @app [:channels name])]
    (go (>! ch (or arg "")))))
