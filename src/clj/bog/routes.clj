(ns bog.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [resources not-found]]
            [bog.logic.users :as users]
            [bog.logic.tokens :as tokens]))

; api routes
(defroutes api-routes
  (GET "/api/status" request
    {:status 200
     :body {:message "alive"}})

  (POST "/api/users" request
    (-> (users/create! request)
        (tokens/generate!)
        (tokens/response)))

  (POST "/api/tokens" request
    (-> (users/login! request)
        (tokens/generate!)
        (tokens/response))))

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
