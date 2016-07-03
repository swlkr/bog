(ns bog.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET POST wrap-routes defroutes]]
            [compojure.route :refer [resources not-found]]
            [compojure.coercions :refer [as-int]]
            [bog.middleware :refer [wrap-jwt-auth]]
            [bog.controllers.users-controller :as users-controller]
            [bog.controllers.tokens-controller :as tokens-controller]
            [bog.controllers.status-controller :as status-controller]
            [bog.controllers.posts-controller :as posts-controller]
            [bog.controllers.comments-controller :as comments-controller]))

; protected api routes
(defroutes protected-api-routes
  (GET "/api/protected-status" request (status-controller/get-status request))
  (POST "/api/posts" request (posts-controller/create-post! request)))

; api routes
(defroutes api-routes
  (GET "/api/status" request (status-controller/get-status request))
  (POST "/api/users" request (users-controller/create-user! request))
  (GET "/api/users/:id/posts" [id :<< as-int] (posts-controller/get-list! id))
  (POST "/api/tokens" request (tokens-controller/create-token! request))
  (POST "/api/comments" request (comments-controller/create-comment! request))
  (GET "/api/posts/:id/comments" [id :<< as-int] (comments-controller/get-comments! id))
  (wrap-routes protected-api-routes wrap-jwt-auth))

; client routes
(def client-response
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (io/input-stream (io/resource "public/index.html"))})

(defroutes routes
  api-routes
  (GET "/" _ client-response)
  (resources "/")
  (not-found (slurp (io/resource "public/404.html"))))
