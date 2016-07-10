(ns bog.controllers.users-controller-test
  (:require [clojure.test :refer :all]
            [bog.server :refer [http-handler]]
            [ring.mock.request :as mock]
            [clojure.data.json :as json]
            [environ.core :refer [env]]
            [bog.db :as db]
            [bog.test-utils :refer [build-request]]))

(def env-vars {:secret "shhhhh" :database-url "postgres://blah-blah-blah"})

(deftest invalid-create-user-test
  (with-redefs [db/insert-user<! (fn [params] {})
                env env-vars]

    (testing "mismatched confirm pw and password"
      (let [request (build-request
                      :url "/api/users"
                      :method :post
                      :body {:email "test@example.com" :password "password" :password-confirm "pw"})
            expected {:status 500
                      :body (json/write-str {:message "Password and confirm password don't match"})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))

    (testing "short password"
      (let [request (build-request
                      :url "/api/users"
                      :method :post
                      :body {:email "test@example.com" :password "pw" :password-confirm "pw"})
            expected {:status 500
                      :body (json/write-str {:message "Password needs to be at least 13 characters long"})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))))

(deftest valid-create-user-test
  (with-redefs [db/insert-user<! (fn [params] {:id 1 :email "" :password "" :created_at ""})
                env env-vars]

    (testing "valid request"
      (let [request (build-request
                      :url "/api/users"
                      :method :post
                      :body {:email "test@example.com" :password "correct horse battery staple" :password-confirm "correct horse battery staple"})
            response (http-handler request)]
        (is
          (= 200 (:status response)))))))
