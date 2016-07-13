(ns bog.components.list-group
  (:require [bog.utils :refer [classes]]))

(defn list-group [{:keys [items path state]}]
  (let [type (-> @state :new-post :type)]
    [:list-group {:class "list-group"}
      (for [item items]
        ^{:key (:value item)} [:a {:class (classes {"list-group-item" true
                                                    "active" (= type (:value item))})
                                   :href "#"
                                   :on-click (fn [e]
                                              (do
                                                (.preventDefault e)
                                                (swap! state assoc-in path (:value item))))}
                                (:text item)])]))
