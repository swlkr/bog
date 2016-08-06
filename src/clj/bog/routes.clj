(ns bog.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET POST PUT wrap-routes defroutes]]
            [compojure.route :refer [resources not-found]]
            [compojure.coercions :refer [as-int]]
            [bog.middleware :refer [wrap-jwt-auth]]
            [bog.controllers.users-controller :refer [create-user!]]
            [bog.controllers.tokens-controller :refer [create-token!]]
            [bog.controllers.status-controller :refer [get-status]]
            [bog.controllers.posts-controller :refer [get-posts!
                                                      create-post!
                                                      get-drafts!
                                                      update-post!
                                                      get-draft!]]
            [bog.controllers.comments-controller :refer [create-comment! get-comments!]]))

; protected api routes
(defroutes protected-api-routes
  (GET "/api/protected-status" request (get-status request))
  (POST "/api/posts" request (create-post! request))
  (GET "/api/drafts" request (get-drafts! request))
  (GET "/api/drafts/:id" [id :<< as-int :as request] (get-draft! id))
  (PUT "/api/posts/:id" [id :<< as-int :as request] (update-post! request id)))

; api routes
(defroutes api-routes
  (GET "/api/status" request (get-status request))
  (POST "/api/users" request (create-user! request))
  (GET "/api/posts" request (get-posts! request))
  (POST "/api/tokens" request (create-token! request))
  (POST "/api/comments" request (create-comment! request))
  (GET "/api/posts/:id/comments" [id :<< as-int] (get-comments! id))
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
  (GET "/drafts" _ (client-response))
  (GET "/drafts/:id/edit" _ (client-response))
  (resources "/")
  (not-found (slurp (io/resource "public/404.html"))))
