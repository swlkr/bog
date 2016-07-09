(ns bog.logic.users
  (:require [bog.utils :as utils :refer [throw+ ensure!]]
            [clojurewerkz.scrypt.core :as sc]
            [bog.db :as db]
            [bog.schemas :refer [SignUpRequest LoginRequest]]
            [schema.core :as s]))

;;; Signing up

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

(defn create! [request secret]
  (->> (:body request)
       (s/validate SignUpRequest)
       ensure-valid-email
       ensure-valid-password
       encrypt-password
       format-sql-params
       db/insert-user<!))

;;; Logging in

(defn is-matching-password? [db-params http-params]
  (let [{:keys [password]} db-params]
    (sc/verify (:password http-params) password)))

(defn ensure-matching-password [db-params http-params]
  (if (is-matching-password? db-params http-params)
    db-params
    (throw+ "Incorrect password")))

(defn login! [request secret]
  (as-> (:body request) r
        (s/validate LoginRequest r)
        (db/get-users-by-email r)
        (first r)
        (ensure! (comp not nil?) r "There was no user with that email would you like to sign up?")
        (select-keys r [:id :email :password])
        (ensure-matching-password r (:body request))
        (select-keys r [:id])))
