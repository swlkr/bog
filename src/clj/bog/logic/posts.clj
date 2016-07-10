(ns bog.logic.posts
  (:require [ring.util.codec :as c]
            [bog.db :as db]
            [bog.schemas :refer [PostRequest parse-post-request]]
            [schema.core :as s]))

(defn build-sql-params [params]
  (let [{:keys [user title content type draft]} params]
    {:user_id (:id user)
     :title title
     :content content
     :type (name type)
     :draft draft}))

(defn encode-html [params]
  (let [{:keys [title content]} params
        s-title (c/url-encode title)
        s-content (c/url-encode content)]
    (merge params {:title s-title :content s-content})))

(defn create! [params]
  (->> params
       (parse-post-request)
       (s/validate PostRequest)
       (encode-html)
       (build-sql-params)
       (db/insert-post<!)))

(defn format-date [date format-str]
  (.format (java.text.SimpleDateFormat. format-str) date))

(defn format-created-at [m]
  (let [{:keys [created_at]} m]
    (assoc m :created_at (format-date created_at "MMMM dd yyyy"))))

(defn get-list []
  (->> (db/get-posts)
       (map #(select-keys % [:id :title :content :created_at :draft]))
       (map #(format-created-at %))))

(defn get-drafts []
  (->> (db/get-drafts)
       (map #(select-keys % [:id :title :content :created_at :draft]))
       (map #(format-created-at %))))
