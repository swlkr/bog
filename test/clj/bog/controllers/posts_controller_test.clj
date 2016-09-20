(ns bog.controllers.posts-controller-test
  (:require [clojure.test :refer :all]
            [bog.server :refer [http-handler]]
            [clojure.data.json :as json]
            [bog.test-utils :refer [build-request]]
            [bog.db :as db]
            [environ.core :refer [env]]
            [bog.logic.tokens :as tokens]))

(deftest create-post-test
  (with-redefs [db/insert-post<! (fn [params] params)
                env {:secret "shhhhh"}
                tokens/decode! (fn [token secret] {:id 1})]

    (testing "with invalid post type"
      (let [request (build-request :url "/api/posts"
                                   :method :post
                                   :body {:id ""
                                          :user_id ""
                                          :title "a"
                                          :content "b"
                                          :type "a"
                                          :sort_order 0})

            expected {:status 500
                      :body (json/write-str {:message "Post can only be quote, post, video, slideshow"})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))

    (testing "with nil title"
      (let [request (build-request :url "/api/posts"
                                   :method :post
                                   :body {:title nil
                                          :content "b"
                                          :type "post"
                                          :sort_order 0
                                          :user_id ""
                                          :id ""})
            expected {:status 500
                      :body (json/write-str {:message "Missing parameters. Expected: id, user_id, title, content, type, sort_order"})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))

    (testing "with valid post"
      (let [request (build-request :url "/api/posts"
                                   :method :post
                                   :body {:id "uuid"
                                          :user_id "uuid"
                                          :title "title"
                                          :content "content"
                                          :type "post"
                                          :sort_order 0})
            expected {:status 200
                      :body (json/write-str {:id "uuid"
                                             :user_id "uuid"
                                             :title "title"
                                             :content "content"
                                             :type "post"
                                             :sort_order 0})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))))
