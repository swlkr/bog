(ns bog.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET POST PUT DELETE wrap-routes defroutes context]]
            [compojure.route :refer [resources not-found]]
            [compojure.coercions :refer [as-int]]
            [bog.middleware :refer [wrap-jwt-auth]]
            [bog.controllers.users-controller :as users-controller]
            [bog.controllers.tokens-controller :as tokens-controller]
            [bog.controllers.status-controller :as status-controller]
            [bog.controllers.posts-controller :as posts-controller]
            [bog.controllers.comments-controller :as comments-controller]
            [bog.controllers.drafts-controller :as drafts-controller]))

; protected api routes
(defroutes protected-api-routes
  (context "/api" []
    (GET "/protected-status" request (status-controller/get request))
    (context "/drafts" []
      (POST "/" {body :body} (drafts-controller/create! body))
      (GET "/" {:keys [user]} (drafts-controller/list! (:id user)))
      (context "/:id" [id]
        (GET "/" [] (drafts-controller/get! id))
        (PUT "/" {body :body} (drafts-controller/update! id body))
        (DELETE "/" [] (drafts-controller/delete! id))))
    (context "/posts" []
      (POST "/" {body :body} (posts-controller/create! body))
      (context "/:id" [id]
        (PUT "/" {body :body} (posts-controller/update! id body))
        (DELETE "/" [] (posts-controller/delete! id))))))

; api routes
(defroutes api-routes
  (context "/api" []
    (POST "/users" request (users-controller/create! request))
    (POST "/tokens" request (tokens-controller/create! request))
    (GET "/status" request (status-controller/get request))
    (context "/posts" []
      (GET "/" [] (posts-controller/list!))
      (context "/:id" [id]
        (GET "/" [] (posts-controller/get! id))
        (GET "/comments" [] (comments-controller/list! id))
        (POST "/comments" {body :body} (comments-controller/create! id body)))))
  (wrap-routes protected-api-routes wrap-jwt-auth))

; client routes
(defn client-response []
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (io/input-stream (io/resource "public/index.html"))})

(defroutes routes
  api-routes
  (GET "/" _ (client-response))
  (GET "/login" _ (client-response))
  (GET "/posts/new" _ (client-response))
  (GET "/posts/:id" _ (client-response))
  (GET "/drafts" _ (client-response))
  (GET "/drafts/:id/edit" _ (client-response))
  (resources "/")
  (not-found (slurp (io/resource "public/404.html"))))
