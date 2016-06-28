(ns bog.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources not-found]]))

(def client-response
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (io/input-stream (io/resource "public/index.html"))})

(defroutes routes
  (GET "/" _ client-response)
  (resources "/")
  (not-found (slurp (io/resource "public/404.html"))))
