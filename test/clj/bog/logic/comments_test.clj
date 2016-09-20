(ns bog.logic.comments-test
  (:require [clojure.test :refer :all]
            [bog.logic.comments :refer :all]
            [ring.util.codec :as c]))

(deftest create-valid
  (let [input {:name "name" :content "<test/>" :post_id 1}
        expected {:name "name" :content (c/url-encode (:content input)) :post_id 1}]
    (is (= expected (create input)))))

(deftest get-by-post-id-response-test
  (let [db-rows [{:id 1 :name "a" :content "b"}]
        expected {:body {:comments db-rows} :status 200}]
    (is (= expected (get-by-post-id-response db-rows)))))
