(ns bog.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET POST PUT DELETE wrap-routes defroutes context]]
            [compojure.route :refer [resources not-found]]
            [compojure.coercions :refer [as-int]]
            [bog.middleware :as middleware]
            [bog.controllers.users-controller :as users-controller]
            [bog.controllers.tokens-controller :as tokens-controller]
            [bog.controllers.status-controller :as status-controller]
            [bog.controllers.posts-controller :as posts-controller]
            [bog.controllers.comments-controller :as comments-controller]
            [bog.controllers.drafts-controller :as drafts-controller]
            [bog.controllers.images-controller :as images-controller]
            [bog.controllers.tags-controller :as tags-controller]
            [bog.controllers.files-controller :as files-controller]))

; protected api routes
(defroutes protected-api-routes
  (context "/api" []
    (GET "/protected-status" request (status-controller/get request))
    (DELETE "/comments/:id" [id] (comments-controller/delete! id))
    (POST "/files" {params :params} (files-controller/create! params))

    (context "/drafts" []
      (POST "/" {:keys [body user]} (drafts-controller/create! body user))
      (GET "/" {user :user} (drafts-controller/list! (:id user)))
      (context "/:id" [id]
        (GET "/" [] (drafts-controller/get! id))
        (PUT "/" {body :body} (drafts-controller/update! id body))
        (DELETE "/" [] (drafts-controller/delete! id))

        (context "/images" []
          (POST "/" {body :body} (images-controller/create! id body))
          (GET "/" [] (images-controller/list! id))
          (context "/:image-id" [image-id]
            (GET "/" [] (images-controller/get! image-id))
            (PUT "/" {body :body} (images-controller/update! image-id body))
            (DELETE "/" [] (images-controller/delete! image-id))))

        (context "/tags" []
          (POST "/" {body :body} (tags-controller/create! id body))
          (GET "/" [] (tags-controller/list! id))
          (context "/:tag-id" [tag-id]
            (GET "/" [] (tags-controller/get! tag-id))
            (PUT "/" {body :body} (tags-controller/update! tag-id body))
            (DELETE "/" [] (tags-controller/delete! tag-id))))))))

; api routes
(defroutes api-routes
  (context "/api" []
    (POST "/users" {body :body} (users-controller/create! body))
    (POST "/tokens" {body :body} (tokens-controller/create! body))
    (GET "/status" request (status-controller/get request))

    (context "/posts" []
      (GET "/" [] (posts-controller/list!))
      (context "/:id" [id]
        (GET "/" [] (posts-controller/get! id))

        (context "/comments" []
          (GET "/" [] (comments-controller/list! id))
          (POST "/" {body :body} (comments-controller/create! id body))))))

  (-> protected-api-routes
      (wrap-routes middleware/wrap-jwt)
      (wrap-routes middleware/wrap-auth)))

; client routes
(defn client-response []
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (io/input-stream (io/resource "public/index.html"))})

(defroutes routes
  (wrap-routes api-routes middleware/wrap-jwt)
  (GET "/" _ (client-response))
  (GET "/login" _ (client-response))
  (GET "/posts/new" _ (client-response))
  (GET "/posts/:id" _ (client-response))
  (GET "/drafts" _ (client-response))
  (GET "/drafts/new" _ (client-response))
  (GET "/drafts/:id" _ (client-response))
  (GET "/drafts/:id/edit" _ (client-response))
  (resources "/")
  (not-found (slurp (io/resource "public/404.html"))))
