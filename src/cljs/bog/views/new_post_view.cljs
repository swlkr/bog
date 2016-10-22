(ns bog.views.new-post-view
  (:require-macros [quiescent.core :refer [defcomponent]]
                   [cljs.core.async.macros :refer [go]])
  (:require [quiescent.dom :refer [div section h1 h2 aside ul li p a button textarea]]
            [markdown.core :refer [md->html]]
            [cljs.core.async :refer [>!]]
            [bog.app :as app]
            [bog.components.hero :refer [Hero]]
            [bog.components.menu :refer [Menu]]
            [bog.components.textarea :refer [Textarea]]
            [bog.components.input :refer [Input]]))

(defcomponent DraftListElement
  :keyfn :id
  [{:keys [id title]}]
  (li {}
    (a {:href (str "/drafts/" id)} title)))

(defcomponent NewPostView
  :on-mount (fn [] (go (>! (:req-ch app/channels) {:url "/api/drafts" :method :get :path [:drafts]})))
  [state channels]
  (let [{:keys [input-ch req-ch create-draft-ch]} channels
        {:keys [:new-draft/content :new-draft/title drafts]} state
        html-content (md->html content)]
    (div {}
      (Hero {:title "New Draft"
             :subtitle "This is where you can create new drafts!"})

      (div {:className "container is-fluid m-t-3"}
        (div {:className "columns"}

          (div {:className "column is-2"}
            (Menu {:label "Drafts"}
              (map DraftListElement drafts)))

          (div {:className "column"}
            (Input {:label "Title"
                    :placholder "New draft title goes here"
                    :value (or title "")
                    :onChange (fn [e]
                                (let [val (-> e .-target .-value)]
                                  (go (>! input-ch {:new-draft/title val}))))})

            (Textarea {:className "textarea"
                       :placeholder "New draft content goes here"
                       :style {:height "200px"}
                       :value (or content "")
                       :label "Content"
                       :onChange (fn [e]
                                   (let [val (-> e .-target .-value)]
                                     (go (>! input-ch {:new-draft/content val}))))})

            (p {:className "control"}
              (button {:className "button is-primary"
                       :style {:marginRight "10px"}
                       :onClick #(go (>! create-draft-ch {:content content
                                                          :title title}))}
                "Save")
              (button {:className "button"
                       :onClick #(go (>! req-ch {:method :post
                                                 :url "/api/drafts"
                                                 :body {:content content
                                                        :title title}}))}
                "Publish"))

            (div {:className "box"}
              (div {:className "content"}
                (h1 {} title)
                (div {:dangerouslySetInnerHTML {:__html html-content}})))))))))
