(ns bog.logic.comments
  (:require [ring.util.codec :as c]
            [bog.db :as db]
            [bog.schemas.comment-schema :refer [CommentRequest parse-request]]
            [schema.core :as s]))

(defn build-sql-params [params]
  (let [{:keys [content name post-id]} params]
    {:post_id post-id
     :content content
     :name name}))

(defn encode-html [params]
  (let [{:keys [name content]} params
        s-name (c/url-encode name)
        s-content (c/url-encode content)]
    (merge params {:name s-name :content s-content})))

(defn create [m]
    (->> m
         parse-request
         (s/validate CommentRequest)
         encode-html
         build-sql-params))

(defn create! [params]
  (-> params
      create
      db/insert-comment<!))

(defn create-response [comment]
  {:status 200
   :body {:comment comment}})
