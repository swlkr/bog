(ns bog.logic.users-test
  (:require [clojure.test :refer :all]
            [bog.logic.users :refer :all]
            [environ.core :refer [env]]
            [bog.db :as db]))

(def secret "shhhhhhhh")

(deftest create-user-with-mismatch-password-test
  (with-redefs [db/insert-user<! (fn [params] {})]
    (let [request {:body {:email "swlkr.rbl@gmail.com" :password "password" :password-confirm "pw"}}]
      (is
        (thrown-with-msg?
          java.lang.Exception
          #"Password and confirm password don't match"
          (create! request secret))))))

(deftest create-user-with-invalid-password-test
  (with-redefs [db/insert-user<! (fn [params] {})]
    (let [request {:body {:email "swlkr.rbl@gmail.com" :password "password" :password-confirm "password"}}]
      (is
        (thrown-with-msg?
          java.lang.Exception
          #"Password needs to be at least 13 characters long"
          (create! request secret))))))

(deftest create-user-with-valid-request-test
  (let [request {:body {:email "swlkr.rbl@gmail.com" :password "correct horse battery staple" :password-confirm "correct horse battery staple"}}
        expected [:email :password]]
    (with-redefs [db/insert-user<! (fn [params] {:email "email" :password "pw"})]
      (is (= expected (keys (create! request secret)))))))

(deftest login-with-db-params
    (let [expected {:id 1}
          body {:email "email" :password "pw"}
          request {:body body}
          enc-params (encrypt-password body)
          db-results [{:id 1 :email "email" :password (:encrypted-password enc-params)}]]
      (with-redefs [db/get-users-by-email (fn [params] db-results)]
        (is (= expected (login! request secret))))))
