(ns demo.core
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as t]
            [clojure.string :as str]))

::some-too-long-string
(s/def ::name (s/and string? (complement str/blank?)))
(s/def ::age (s/and integer? (s/or :zero zero? :pos pos?)))
(s/def ::some-too-long-string string?)

(def m {::foo 1
        ::bar 2})

(::foo m)



(s/def ::person
  (s/keys :req-un [::name ::age]))

(defn do-something [person])

(s/fdef do-something
        :args (s/cat :person ::person))

(t/instrument)

(for [i (range 10)]
  (range i))
;; => (()
;;     (0)
;;     (0 1)
;;     (0 1 2)
;;     (0 1 2 3)
;;     (0 1 2 3 4)
;;     (0 1 2 3 4 5)
;;     (0 1 2 3 4 5 6)
;;     (0 1 2 3 4 5 6 7)
;;     (0 1 2 3 4 5 6 7 8))

;; => ([0 0 0 0]
;;     [1 1 1 1]
;;     [2 2 2 2]
;;     [3 3 3 3]
;;     [4 4 4 4]
;;     [5 5 5 5]
;;     [6 6 6 6]
;;     [7 7 7 7]
;;     [8 8 8 8]
;;     [9 9 9 9])
