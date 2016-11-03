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
            [bog.actions.drafts :as drafts]
            [clojure.string :as string]))

(defn get-file-type [filename]
  (-> filename
      (string/split #"[.]")
      (last)))

(defn gen-filename [id filename]
  (let [file-type (get-file-type filename)]
    (str id "." file-type)))

(defn upload-file! [state file name]
  (go
    (let [id (str (random-uuid))
          filename (gen-filename id name)
          req {:url "/api/files"
               :method :upload
               :body [["file" [file filename]]]}
          {:keys [body status]} (<! (api/send state req))]
        (if (= status 200)
          (dispatch! :drafts/upload-image-res (merge body {:id id}))
          (dispatch! :on-error body)))))

(defn upload-files [state _]
  (js/setTimeout (fn []
                   (if-let [el (.getElementById js/document "file-input")]
                     (if-let [files (-> el .-files)]
                       (let [file (aget files 0)
                             filename (.-name file)]
                         (upload-file! state file filename)))))
                 1000)
  (assoc state :loading true))

(defn upload-image-res [state photo]
  (let [{:keys [id url]} photo]
    (assoc state :photo {:id id :url url})))

(add-action :drafts/upload-image upload-files)
(add-action :drafts/upload-image-res upload-image-res)

(q/defcomponent NewDraftView [state]
  (let [{:keys [info new-draft submitting photo]} state
        {:keys [title content]} new-draft
        html-content (md->html content)]
    (d/div {}
      (Hero {:title "New Draft"
             :subtitle "Create a new draft here!"})
      (d/div {:className "container m-t-3"}
        (d/div {:className "columns"}
          (d/div {:className "column"}
            (Notification {:message info})

            (d/input {:type "file"
                      :label "Cover photo"
                      :id "file-input"
                      :onChange (fn [e] (dispatch! :drafts/upload-image e))})

            (d/img {:src (:url photo)})

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
