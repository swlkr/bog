(ns bog.app-state
   (:require [reagent.core :as r]))

(defonce app-state (r/atom {:error nil
                            :user nil
                            :token nil
                            :view :posts}))
