(ns bog.views.new-draft-view
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [quiescent.core :as q]
            [quiescent.dom :as d]
            [markdown.core :refer [md->html]]
            [bog.components.textarea :refer [Textarea]]
            [bog.components.input :refer [Input]]
            [bog.components.hero :refer [Hero]]
            [bog.components.notification :refer [Notification]]
            [bog.app :refer [dispatch! add-action]]
            [bog.api :as api]
            [bog.actions.drafts :as drafts]))

(q/defcomponent NewDraftView
  [state]
  (let [{:keys [info new-draft submitting]} state
        {:keys [title content]} new-draft
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
                                  (dispatch! :drafts/change [[:new-draft :title] val])))})

            (Textarea {:className "textarea"
                       :placeholder "New draft content goes here"
                       :style {:height "200px"}
                       :value (or content "")
                       :label "Content"
                       :onChange (fn [e]
                                   (let [val (-> e .-target .-value)]
                                    (dispatch! :drafts/change [[:new-draft :content] val])))})

            (d/p {:className "control"}
              (d/button {:className "button is-primary"
                         :style {:marginRight "10px"}
                         :onClick #(dispatch! :drafts/create nil)}
                "Save")

              (d/button {:className (str "button is-default " (when submitting "is-loading"))
                         :onClick #(dispatch! :urls/change "/drafts")}
                "Cancel"))

            (d/div {:className "box"}
              (d/div {:className "content"}
                (d/h1 {} title)
                (d/div {:dangerouslySetInnerHTML {:__html html-content}})))))))))
