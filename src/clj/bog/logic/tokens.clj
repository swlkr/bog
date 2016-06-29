(ns bog.logic.tokens
  (:require [clj-jwt.core :refer [verify jwt to-str sign str->jwt]]
            [clj-time.core :refer [now plus days]]
            [environ.core :refer [env]]))

(def claim
  {:iss "self"
   :exp (plus (now) (days 7))
   :iat (now)})

(defn generate [data secret]
   (-> data jwt (sign :HS256 secret) to-str))

(defn generate! [payload]
 (let [secret (env :secret)]
   (-> (merge payload claim)
       (generate secret))))

(defn verify-token [token secret]
  (-> token str->jwt (verify secret)))

(defn decode [token secret]
  (if (not (nil? (verify-token token secret)))
    (-> token str->jwt :claims)
    nil))

(defn decode! [token]
  (when (not (nil? token))
    (decode token (env :secret))))

(defn response [token]
  {:status 200
   :body {:token token}})
