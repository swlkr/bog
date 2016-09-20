(ns bog.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET POST PUT DELETE wrap-routes defroutes context]]
            [compojure.route :refer [resources not-found]]
            [compojure.coercions :refer [as-int]]
            [bog.middleware :refer [wrap-jwt-auth]]
            [bog.controllers.users-controller :as users-controller]
            [bog.controllers.tokens-controller :as tokens-controller]
            [bog.controllers.status-controller :as status-controller]
            [bog.controllers.posts-controller :refer [get-posts!
                                                      get-post!
                                                      create-post!
                                                      update-post!]]
            [bog.controllers.comments-controller :refer [create-comment! get-comments!]]
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
      (POST "/" request (create-post! request))
      (PUT "/:id" [id :<< as-int :as request] (update-post! request id)))))

; api routes
(defroutes api-routes
  (context "/api" []
    (POST "/users" request (users-controller/create! request))
    (POST "/tokens" request (tokens-controller/create! request))
    (GET "/status" request (status-controller/get request)))
  (GET "/api/posts" req (get-posts! req))
  (GET "/api/posts/:id" [id :<< as-int :as req] (get-post! id))
  (POST "/api/comments" req (create-comment! req))
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
  (GET "/posts/:id" _ (client-response))
  (GET "/drafts" _ (client-response))
  (GET "/drafts/:id/edit" _ (client-response))
  (resources "/")
  (not-found (slurp (io/resource "public/404.html"))))
