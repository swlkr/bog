(ns bog.logic.comments-test
  (:require [clojure.test :refer :all]
            [bog.logic.comments :refer :all]))

(deftest create-comment
  (testing "invalid comment"
    (let [m {:name "first last" :content "<test/>" :draft_id "" :id ""}]
      (is (= {:name "first last" :content "&lt;test/&gt;" :draft_id "" :id ""}
             (create m)))))
  (testing "valid comment"
    (let [m {:name "name" :content "content" :draft_id "" :id ""}]
      (is (= m (create m))))))
