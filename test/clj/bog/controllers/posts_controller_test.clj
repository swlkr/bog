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
                                   :body {:title "a"
                                          :content "b"
                                          :type "a"
                                          :draft false})
            expected {:status 500
                      :body (json/write-str {:message "Value cannot be coerced to match schema: {:type (not (#{:video :quote :post :slideshow} :a))}"})
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
                                          :draft false})
            expected {:status 500
                      :body (json/write-str {:message "Value cannot be coerced to match schema: {:title (not (instance? java.lang.String nil))}"})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))

    (testing "with valid post"
      (let [request (build-request :url "/api/posts"
                                   :method :post
                                   :body {:title "title"
                                          :content "content"
                                          :type "post"
                                          :draft false})
            expected {:status 200
                      :body (json/write-str {:user_id 1
                                             :title "title"
                                             :content "content"
                                             :type "post"
                                             :draft false})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))

    (testing "with valid post with html"
      (let [request (build-request :url "/api/posts"
                                   :method :post
                                   :body {:title "<script>alert('title')</script>"
                                          :content "<script>alert('title')</script>"
                                          :type "post"
                                          :draft false})
            expected {:status 200
                      :body (json/write-str {:user_id 1
                                             :title "%3Cscript%3Ealert%28%27title%27%29%3C%2Fscript%3E"
                                             :content "%3Cscript%3Ealert%28%27title%27%29%3C%2Fscript%3E"
                                             :type "post"
                                             :draft false})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is
          (= expected
            (http-handler request)))))))


(deftest create-draft-test
  (with-redefs [db/insert-post<! (fn [params] params)
                env {:secret "shhhhh"}
                tokens/decode! (fn [token secret] {:id 1})]
    (testing "valid draft"
      (let [draft {:title "title"
                   :content "content"
                   :type "post"
                   :draft true}
            request (build-request :url "/api/posts"
                                   :method :post
                                   :body draft)
            expected {:status 200
                      :body (json/write-str {:user_id 1 :title "title" :content "content" :type "post" :draft true})
                      :headers {"Content-Type" "application/json; charset=utf-8"}}]
        (is (= expected
               (http-handler request)))))))
