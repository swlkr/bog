(ns bog.logic.posts
  (:require [ring.util.codec :as c]
            [bog.db :as db]
            [bog.schemas.post-schema :as post-schema]
            [schema.core :as s]))

(defn build-sql-params [params]
  (let [{:keys [user title content type sort-order]} params]
    {:user_id (:id user)
     :title title
     :content content
     :type (name type)}))

(defn encode-html [params]
  (let [{:keys [title content]} params
        s-title (c/url-encode title)
        s-content (c/url-encode content)]
    (merge params {:title s-title :content s-content})))

(defn create [m]
    (->> m
         post-schema/parse-request
         (s/validate post-schema/PostRequest)
         encode-html
         build-sql-params))

(defn create! [params]
  (-> params
      create
      db/insert-post<!))

(defn create-response [post]
  {:status 200
   :body {:post post}})

(defn get-list []
  (db/get-posts))

(defn get-list-response [db-rows]
  (->> db-rows
       (map #(select-keys % [:id :title :content :created_at]))
       (assoc {} :posts)
       (assoc {} :status 200 :body)))
