(ns bog.logic.tokens-test
  (:require [clojure.test :refer :all]
            [bog.logic.tokens :refer :all]))

(def secret "shhhhhhh")
(def data {:id 1 :email "email"})
(def token "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZW1haWwiOiJlbWFpbCIsImlzcyI6InNlbGYiLCJleHAiOjE0Njc0Mzc0MDcsImlhdCI6MTQ2NjgzMjYwN30.o7t4U5_wQAbBPMt5wyvBpOJZVx7PRrohkirSzli6DsU")
(def token-length (count token))

(deftest verify-token-test
  (is (= true (verify-token token secret))))

(def decoded-payload {:email "email", :exp 1467437407, :iat 1466832607, :id 1, :iss "self"})
(deftest decode-test
  (is (= decoded-payload (decode token secret))))
