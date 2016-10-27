(ns bog.components.card
  (:require [quiescent.core :as q]
            [quiescent.dom :as d]
            [bog.utils :as utils]))

(q/defcomponent Card
  :keyfn :id
  [{:keys [img title fullwidth]} & children]
  (let [class (utils/classes {"is-fullwidth" fullwidth
                              "card" true})]
    (d/div {:className class}
      (when (not (nil? img))
        (d/div {:className "card-image"}
          (d/figure {:className "image is-4by3"}
            (d/img {:src img}))))
      (d/div {:className "card-content"}
        (d/h1 {:className "title"} title)
        (d/div {:className "content"}
          children)))))
