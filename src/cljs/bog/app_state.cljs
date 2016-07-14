(ns bog.app-state
   (:require [reagent.core :as r]))

(defonce app-state (r/atom {:error nil
                            :user nil
                            :access-token nil
                            :posts []
                            :drafts []
                            :navbar-collapsed true
                            :login {:email ""
                                    :password ""}
                            :new-post {:title ""
                                       :content ""
                                       :type "post"}
                            :view :posts}))
