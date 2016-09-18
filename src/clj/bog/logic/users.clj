(ns bog.logic.users
  (:require [bog.utils :as utils :refer [throw+ ensure!]]
            [clojurewerkz.scrypt.core :as sc]
            [bog.db :as db]
            [bog.utils :as utils]
            [bog.errors :as errors]))

; Signing up

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

(defn encrypt-password [params]
  (let [{:keys [password]} params
        p (sc/encrypt password 16384 8 1)]
    (assoc params :password p)))

(defn valid-email? [email]
  (and
    (string? email)
    (not (nil? (clojure.string/index-of email "@")))))

(defn pre-create [body]
  (let [ks [:id :email :password :password-confirm]]
    (->> (select-keys body ks)
         (utils/ensure! "A map is required" map?)
         (utils/ensure! (errors/missing-keys ks) (partial utils/keys? ks))
         (utils/ensure! "That email address is invalid" (comp valid-email? :email))
         (utils/ensure! "Password and confirm password don't match" matches-confirm-password?)
         (utils/ensure! "Password needs to be at least 13 characters long" is-password-long-enough?)
         (encrypt-password))))

;;; Logging in

(defn is-matching-password? [db-params http-params]
  (let [{:keys [password]} db-params]
    (sc/verify (:password http-params) password)))

(defn verify! [body]
  (let [ks [:email :password]]
    (as-> body b
          (utils/ensure! "A map is required" map? b)
          (select-keys b ks)
          (utils/ensure! (errors/missing-keys ks) (partial utils/keys? ks) b)
          (db/get-users-by-email b)
          (first b)
          (utils/ensure! "There was no user with that email. Would you like to sign up?" (comp not nil?) b)
          (select-keys b [:id :email :password])
          (utils/ensure! "Incorrect password" (partial is-matching-password? b) body)
          (select-keys b [:id]))))
