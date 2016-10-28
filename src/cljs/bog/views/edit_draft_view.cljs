(ns bog.views.edit-draft-view
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

(q/defcomponent EditDraftView
  :on-mount #(dispatch! :drafts/get nil)
  [state]
  (let [{:keys [submitting draft]} state
        {:keys [title content]} draft
        html-content (md->html content)]
    (d/div {}
      (Hero {:title "Edit Draft"
             :subtitle "Edit an existing draft here!"})
      (d/div {:className "container m-t-3"}
        (d/div {:className "columns"}
          (d/div {:className "column"}
            (Input {:label "Title"
                    :value (or title "")
                    :onChange (fn [e]
                                (let [val (-> e .-target .-value)]
                                  (dispatch! :drafts/change [[:draft :title] val])))})

            (Textarea {:className "textarea"
                       :style {:height "200px"}
                       :value (or content "")
                       :label "Content"
                       :onChange (fn [e]
                                   (let [val (-> e .-target .-value)]
                                    (dispatch! :drafts/change [[:draft :content val]])))})

            (d/p {:className "control"}
              (d/button {:className (str "button is-primary " (when submitting "is-loading"))
                         :style {:marginRight "10px"}
                         :onClick #(dispatch! :drafts/update nil)}
                "Save")

              (d/button {:className (str "button is-default " (when submitting "is-loading"))
                         :onClick #(dispatch! :urls/change "/drafts")}
                "Cancel"))



            (d/div {:className "box"}
              (d/div {:className "content"}
                (d/h1 {} title)
                (d/div {:dangerouslySetInnerHTML {:__html html-content}})))))))))
