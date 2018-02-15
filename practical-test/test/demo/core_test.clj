(ns demo.core-test
  (:require [clojure.test :as t]
            [fudje.sweet :as fj]))

(t/deftest flare-test
  (t/is (= [{:name "alice", :age 120}
            {:name "bob", :age 120}
            {:name "carol", :age 120}
            {:name "erika", :age 120}
            {:name "frank", :age 120}]
           [{:name "alice", :age 120}
            {:name "bob", :age 120}
            {:name "caol", :age 120}
            {:name "erika", :age 120}
            {:name "frank", :age 120}])))

(t/deftest fudje-test
  (t/is (compatible
         {:name "kaguya luna" :age 143}
         (fj/contains {:name "kaguya lun"}))))
