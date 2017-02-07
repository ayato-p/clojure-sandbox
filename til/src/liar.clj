(ns liar)

(defn with-liar* [exp]
  (map (fn [sym]
         (case sym
           true 'false
           false 'true
           sym))
       exp))

(require '[clojure.test :refer [is]])

(defmacro with-liar [& body]
  (let [body# (map with-liar* body)]
    `(do ~@body#)))

(with-liar (is true))


;; FAIL in () (form-init1497587791262311540.clj:17)
;; expected: false
;;   actual: false
;; false
