(ns bog.logic.posts
  (:require [bog.db :as db]
            [bog.schemas :refer [PostRequest
                                 parse-post-request
                                 UpdatePostRequest
                                 parse-update-post-request]]
            [schema.core :as s]))

(defn build-sql-params [params]
  (let [{:keys [user title content type draft]} params]
    {:user_id (:id user)
     :title title
     :content content
     :type (name (or type ""))
     :draft draft}))

(defn format-date [date format-str]
  (.format (java.text.SimpleDateFormat. format-str) date))

(defn format-created-at [m]
  (let [{:keys [created_at]} m]
    (assoc m :created_at (format-date created_at "MMMM dd yyyy"))))

(defn create! [params]
  (->> params
       (parse-post-request)
       (s/validate PostRequest)
       (build-sql-params)
       (db/insert-post<!)))

(defn build-update-sql-params [params]
  (let [{:keys [id]} params]
    (-> params
        (build-sql-params)
        (merge {:id id}))))

(defn update! [params]
  (->> params
       (parse-update-post-request)
       (s/validate UpdatePostRequest)
       (build-update-sql-params)
       (db/update-post<!)
       (format-created-at)))

(defn get-list []
  (->> (db/get-posts)
       (map #(select-keys % [:id :title :content :created_at :draft]))
       (map #(format-created-at %))))

(defn get-drafts []
  (->> (db/get-drafts)
       (map #(select-keys % [:id :title :content :created_at :draft]))
       (map #(format-created-at %))))
