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
            [bog.controllers.drafts-controller :as drafts-controller]
            [bog.controllers.images-controller :as images-controller]))

(defmacro defresource [name & nested]
  `(context ~(str "/" name) []
    (POST "/" ~'{body :body} (~(symbol (str name "-controller/create!")) ~'body))
    (GET "/" ~'{:keys [user]} (~(symbol (str name "-controller/list!")) ~'(:id user)))
    (context "/:id" [~'id]
      (GET "/" [] (~(symbol (str name "-controller/get!")) ~'id))
      (PUT "/" ~'{body :body} (~(symbol (str name "-controller/update!")) ~'id ~'body))
      (DELETE "/" [] (~(symbol (str name "-controller/delete!")) ~'id))
      ~@nested)))

; protected api routes
(defroutes protected-api-routes
  (context "/api" []
    (GET "/protected-status" request (status-controller/get request))
    (defresource "drafts"
      (defresource "images"))
    (context "/posts" []
      (POST "/" {body :body} (posts-controller/create! body))
      (context "/:id" [id]
        (PUT "/" {body :body} (posts-controller/update! id body))
        (DELETE "/" [] (posts-controller/delete! id))))))

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
