(ns bog.logic.users
  (:require [bog.utils :as utils :refer [throw+ ensure!]]
            [clojurewerkz.scrypt.core :as sc]
            [bog.db :as db]))

(defn matches-confirm-password? [{:keys [password password-confirm]}]
  (and
    (string? password)
    (string? password-confirm)
    (= password password-confirm)))

(defn is-password-long-enough? [{:keys [password]}]
  (let [len (count password)]
    (and
      (> len 12)
      (< len 101))))

(defn ensure-valid-password [params]
  (as-> params r
    (ensure! matches-confirm-password? r "Password and confirm password don't match")
    (ensure! is-password-long-enough? r "Password needs to be at least 13 characters long")))

(defn is-valid-email? [{:keys [email]}]
  (let [pattern #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"]
    (and
      (string? email)
      (not (nil? (re-matches pattern email))))))

(defn ensure-valid-email [data]
  (let [{:keys [email]} data]
    (ensure! is-valid-email? data "That email address is invalid")))

(defn encrypt-password [params]
  (let [{:keys [password]} params
        p (sc/encrypt password 16384 8 1)]
    (assoc params :encrypted-password p)))

(defn format-sql-params [params]
  (let [{:keys [email encrypted-password]} params]
    {:email email :password encrypted-password}))

(defn create [request secret]
  (-> (:body request)
      (utils/ensure-no-nil-vals) ; make sure nothing is empty
      (ensure-valid-email) ; make sure the email is valid
      (ensure-valid-password) ; make sure the password is valid
      (encrypt-password)
      (format-sql-params)))

(defn format-token-params [{:keys [id]}]
  {:id id})

(defn create! [request secret]
  (-> (create request secret)
      (db/insert-user<!)
      (format-token-params)))

(defn is-matching-password? [db-params http-params]
  (let [{:keys [password]} db-params]
    (sc/verify (:password http-params) password)))

(defn ensure-matching-password [db-params http-params]
  (if (is-matching-password? db-params http-params)
    db-params
    (throw+ "Incorrect password")))

(defn login-sql-params [{:keys [email password]}]
  {:email email :password password})

(defn more-than-one? [arr]
  (and
    (not (nil? arr))
    (> (count arr) 0)))

(defn login
  ([http-params]
   (-> http-params
       (utils/ensure-no-nil-vals)
       (utils/ensure-has-keys [:email :password])
       (login-sql-params)))

  ([db-results http-params]
   (as-> db-results r
       (ensure! more-than-one? r "That email doesn't exist. Would you like to sign up instead?")
       (first r)
       (ensure-matching-password r http-params)
       (format-token-params r))))

(defn login! [request secret]
  (-> (:body request)
      (login)
      (db/get-users-by-email)
      (login (:body request))))
