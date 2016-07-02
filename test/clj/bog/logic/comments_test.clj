(ns bog.logic.comments-test
  (:require [clojure.test :refer :all]
            [bog.logic.comments :refer :all]
            [ring.util.codec :as c]))

(deftest create-valid
  (let [input {:name "name" :content "<test/>" :post-id 1}
        expected {:name "name" :content (c/url-encode (:content input)) :post_id 1}]
    (is (= expected (create input)))))
