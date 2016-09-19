(ns bog.logic.users-test
  (:require [clojure.test :refer :all]
            [bog.logic.users :refer :all]
            [environ.core :refer [env]]
            [bog.db :as db]))

(def secret "shhhhhhhh")

(deftest create-user-with-mismatch-password-test
  (with-redefs [db/insert-user<! (fn [params] {})]
    (let [body {:id "" :email "swlkr.rbl@gmail.com" :password "password" :password-confirm "pw"}]
      (is
        (thrown-with-msg?
          java.lang.Exception
          #"Password and confirm password don't match"
          (pre-create body))))))

(deftest create-user-with-invalid-password-test
  (with-redefs [db/insert-user<! (fn [params] {})]
    (let [body {:id "" :email "swlkr.rbl@gmail.com" :password "password" :password-confirm "password"}]
      (is
        (thrown-with-msg?
          java.lang.Exception
          #"Password needs to be at least 13 characters long"
          (pre-create body))))))

(deftest create-user-with-valid-request-test
  (let [body {:id "" :email "swlkr.rbl@gmail.com" :password "correct horse battery staple" :password-confirm "correct horse battery staple"}
        expected [:id :email :password :password-confirm]]
    (with-redefs [db/insert-user<! (fn [params] {:email "email" :password "pw"})]
      (is (= expected (keys (pre-create body)))))))

(deftest login-with-db-params
    (let [expected {:id "FE100AF2-2116-4781-9E31-3CFAFA6EFC09"}
          body {:id "" :email "email" :password "pw"}
          enc-params (encrypt-password body)
          db-results [{:id "FE100AF2-2116-4781-9E31-3CFAFA6EFC09" :email "email" :password (:password enc-params)}]]
      (with-redefs [db/get-users-by-email (fn [params] db-results)]
        (is (= expected (verify! body))))))
