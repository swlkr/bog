(ns bog.components.notification
  (:require [quiescent.core :as q]
            [quiescent.dom :as d]
            [bog.utils :as utils]))

(q/defcomponent Notification [{:keys [message is-danger is-info]}]
  (when (not (empty? message))
    (let [class (utils/classes {"notification" true
                                "is-danger" is-danger})]
      (d/div {:className class}
        message))))
