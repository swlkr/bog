(ns bog.logic.posts-test
  (:require [clojure.test :refer :all]
            [bog.logic.posts :refer :all]))

(def valid-request {:title "a"
                    :content "b"
                    :user {:id 1}
                    :type "post"})
(def expected {:title "a"
               :content "b"
               :user_id 1
               :type "post"})

(deftest create-test-with-valid-req
  (is (= expected (create valid-request))))

(def invalid-type-req {:title "a"
                       :content "b"
                       :user {:id 1}
                       :type "a"})

(deftest create-test-with-invalid-type
  (is
    (thrown-with-msg?
      java.lang.Exception
      #"Value does not match schema:*"
      (create invalid-type-req))))

(deftest create-test-with-nil
  (is
    (thrown-with-msg?
      java.lang.Exception
      #"Value does not match schema:*"
      (create nil))))

(def html-req {:title "<script>alert('title')</script>"
               :content "<script>alert('content')</script>"
               :user {:id 1}
               :type "quote"})

(def html-expected {:title "%3Cscript%3Ealert%28%27title%27%29%3C%2Fscript%3E"
                    :content "%3Cscript%3Ealert%28%27content%27%29%3C%2Fscript%3E"
                    :user_id 1
                    :type "quote"})

(deftest create-test-with-html-req
  (is (= html-expected (create html-req))))
