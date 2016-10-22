(ns bog.views.new-draft-view
  (:require [quiescent.core :as q]
            [quiescent.dom :as d]
            [markdown.core :refer [md->html]]
            [bog.components.textarea :refer [Textarea]]
            [bog.components.input :refer [Input]]
            [bog.components.hero :refer [Hero]]
            [bog.components.notification :refer [Notification]]
            [bog.app :refer [dispatch! add-action]]))

(defn on-new-draft-title-change [state val]
  (assoc state :new-draft/title val))

(defn on-new-draft-content-change [state val]
  (assoc state :new-draft/content val))

(add-action :on-new-draft-content-change on-new-draft-content-change)
(add-action :on-new-draft-title-change on-new-draft-title-change)

(q/defcomponent NewDraftView
  [state]
  (let [{:keys [:new-draft/title :new-draft/content info]} state
        html-content (md->html content)]
    (d/div {}
      (Hero {:title "New Draft"
             :subtitle "Create a new draft here!"})
      (d/div {:className "container m-t-3"}
        (d/div {:className "columns"}
          (d/div {:className "column"}
            (Notification {:message info})
            (Input {:label "Title"
                    :placholder "New draft title goes here"
                    :value (or title "")
                    :onChange (fn [e]
                                (let [val (-> e .-target .-value)]
                                  (dispatch! :on-new-draft-title-change val)))})

            (Textarea {:className "textarea"
                       :placeholder "New draft content goes here"
                       :style {:height "200px"}
                       :value (or content "")
                       :label "Content"
                       :onChange (fn [e]
                                   (let [val (-> e .-target .-value)]
                                    (dispatch! :on-new-draft-content-change val)))})

            (d/p {:className "control"}
              (d/button {:className "button is-primary"
                         :style {:marginRight "10px"}
                         :onClick #(dispatch! :on-save-draft-click nil)}

                "Save"))
            (d/div {:className "box"}
              (d/div {:className "content"}
                (d/h1 {} title)
                (d/div {:dangerouslySetInnerHTML {:__html html-content}})))))))))
