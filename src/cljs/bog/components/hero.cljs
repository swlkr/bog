(ns bog.components.hero
  (:require-macros [quiescent.core :refer [defcomponent]])
  (:require [quiescent.dom :refer [section div h1 h2]]
            [bog.utils :as utils]))

(defcomponent Hero [{:keys [title subtitle is-medium is-primary is-bold background-image]}]
  (let [className (utils/classes {"hero" true
                                  "is-medium" is-medium
                                  "is-primary" is-primary
                                  "is-bold" is-bold})]
    (section {:className className :style {:backgroundImage (str "url(" background-image ")")}}
      (div {:className "hero-body"}
        (div {:className "container"}
          (h1 {:className "title"} title)
          (h2 {:className "subtitle"} subtitle))))))
