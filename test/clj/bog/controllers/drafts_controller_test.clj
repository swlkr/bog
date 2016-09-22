(ns bog.controllers.drafts-controller-test
  (:require [clojure.test :refer :all]
            [bog.server :refer [http-handler]]
            [clojure.data.json :as json]
            [bog.test-utils :refer [build-request]]
            [bog.db :as db]
            [environ.core :refer [env]]
            [bog.logic.tokens :as tokens]))

(deftest create-draft-test
  (with-redefs [db/insert-draft<! (fn [params] params)
                env {:secret "shhhhh"}
                tokens/decode! (fn [token secret] {:id 1})]

    (testing "with invalid draft data"
      (let [request (build-request :url "/api/drafts"
                                   :method :post
                                   :body {:title "a"
                                          :content "b"
                                          :type "a"
                                          :sort_order 0
                                          :id "931CA995-204C-4A21-A750-F7883899F631"
                                          :published false})
            expected {:status 500
                      :body (json/write-str {:message "Draft can only be quote, post, video or slideshow"})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))

    (testing "with nil title"
      (let [request (build-request :url "/api/drafts"
                                   :method :post
                                   :body {:title nil
                                          :content "b"
                                          :type "post"
                                          :sort_order 0
                                          :published false})
            expected {:status 500
                      :body (json/write-str {:message "Missing parameters. Expected: id, user_id, title, content, type, sort_order, published"})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))

    (testing "with valid draft"
      (let [request (build-request :url "/api/drafts"
                                   :method :post
                                   :body {:title "title"
                                          :content "content"
                                          :type "post"
                                          :sort_order 0
                                          :id "931CA995-204C-4A21-A750-F7883899F631"
                                          :published false})

            expected {:status 200
                      :body (json/write-str {:id "931CA995-204C-4A21-A750-F7883899F631"
                                             :user_id 1
                                             :title "title"
                                             :content "content"
                                             :type "post"
                                             :sort_order 0
                                             :published false})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))))
