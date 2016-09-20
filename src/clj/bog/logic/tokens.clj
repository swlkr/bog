(ns bog.logic.tokens
  (:require [clj-jwt.core :refer [verify jwt to-str sign str->jwt]]
            [clj-time.core :refer [now plus days]]
            [environ.core :refer [env]]))

(defn id->str [m]
  (assoc m :id (.toString (:id m))))

(defn generate [payload secret]
  (let [claim {:iss "self"
               :exp (plus (now) (days 1))
               :iat (now)}]
    (-> (id->str payload)
        (merge claim)
        (jwt)
        (sign :HS256 secret)
        (to-str))))

(defn verify-token [token secret]
  (-> token str->jwt (verify secret)))

(defn decode [token secret]
  (if (not (nil? (verify-token token secret)))
    (-> token str->jwt :claims)
    nil))

(defn decode! [token secret]
  (when (and
          (every? (comp not nil?) [token secret])
          (not (nil? (verify-token token secret))))
    (-> token str->jwt :claims)))
