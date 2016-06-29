(ns bog.logic.users-test
  (:require [clojure.test :refer :all]
            [bog.logic.users :refer :all]
            [environ.core :refer [env]]))

(def secret "shhhhhhhh")

(deftest create-user-with-blank-email-test
  (let [request {:body {:email nil}}]
    (is
      (thrown-with-msg?
        java.lang.Exception
        #"email was blank"
        (create request secret)))))

(deftest create-user-with-blank-password-test
  (let [request {:body {:password nil}}]
    (is
      (thrown-with-msg?
        java.lang.Exception
        #"password was blank"
        (create request secret)))))

(deftest create-user-with-mismatch-password-test
  (let [request {:body {:email "swlkr.rbl@gmail.com" :password "password" :password-confirm "pw"}}]
    (is
      (thrown-with-msg?
        java.lang.Exception
        #"Password and confirm password don't match"
        (create request secret)))))

(deftest create-user-with-invalid-password-test
  (let [request {:body {:email "swlkr.rbl@gmail.com" :password "password" :password-confirm "password"}}]
    (is
      (thrown-with-msg?
        java.lang.Exception
        #"Password needs to be at least 13 characters long"
        (create request secret)))))

(deftest create-user-with-valid-request-test
  (let [request {:body {:email "swlkr.rbl@gmail.com" :password "correct horse battery staple" :password-confirm "correct horse battery staple"}}
        expected [:email :password]]
    (is (= expected (keys (create request secret))))))

(def params {:email "a" :password "b"})
(deftest login-with-request-test
  (is (= params (login params))))

(deftest login-with-nil-params
  (is
    (thrown-with-msg?
      clojure.lang.ExceptionInfo
      #":email was blank"
      (login {:email nil}))))

(def http-params {:email "email" :password "pw"})
(def enc-params (encrypt-password http-params))
(def db-results [{:id 1 :email "email" :password (:encrypted-password enc-params)}])
(deftest login-with-db-params
  (let [expected {:id 1}]
    (is (= expected (login db-results http-params)))))
