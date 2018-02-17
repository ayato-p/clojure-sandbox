(ns demo.core
  (:require [integrant.core :as ig]
            [weavejester.dependency :as dep]))


;;; refset
;;; -----------------------
(defmethod ig/init-key :default
  [_ m]
  m)

(def sys-conf
  {::a 1
   ::b 2
   ::c 3
   [::d ::c] 4
   ::x (ig/refset ::c)})

(derive ::a ::c)
(derive ::b ::c)

(ig/expand sys-conf)
;; => {:demo.core/a 1,
;;     :demo.core/b 2,
;;     :demo.core/c 3,
;;     [:demo.core/d :demo.core/c] 4,
;;     :demo.core/x #{1 4 3 2}}

(def sys-conf2
  {[::g1 ::a] 1
   [::g1 ::b] 2
   [::g1 ::c] 3
   ::x (ig/refset ::g1)})

(ig/expand sys-conf2)
;; => {[:demo.core/g1 :demo.core/a] 1,
;;     [:demo.core/g1 :demo.core/b] 2,
;;     [:demo.core/g1 :demo.core/c] 3,
;;     :demo.core/x #{1 3 2}}

;;; prep
;;; -----------------------

(defmethod ig/prep-key ::x
  [_ _]
  {:a (ig/ref ::a)
   :b (ig/ref ::b)})

(defmethod ig/prep-key ::y
  [_ conf-vec]
  (reduce (fn [m kw]
            (assoc m (keyword (name kw)) (ig/ref kw)))
          {}
          conf-vec))

(def sys-conf
  {::a 1
   ::b 2
   ::x {}
   ::y [::a ::b]})

(ig/prep sys-conf)
;; => #:demo.core{:a 1,
;;                :b 2,
;;                :x {:a {:key :demo.core/a}, :b {:key :demo.core/b}},
;;                :y {:a {:key :demo.core/a}, :b {:key :demo.core/b}}}

(ig/expand (ig/prep sys-conf))
;; => #:demo.core{:a 1, :b 2, :x {:a 1, :b 2}, :y {:a 1, :b 2}}

;;; key-comparator
;;; -----------------------

(def graph
  (-> (dep/graph)
      (dep/depend ::level1a ::level0)
      (dep/depend ::level1b ::level0)
      (dep/depend ::level1c ::level0)
      (dep/depend ::level1d ::level0)

      (dep/depend ::level2 ::level1a)
      (dep/depend ::level2 ::level1b)
      (dep/depend ::level2 ::level1c)
      (dep/depend ::level2 ::level1d)

      (dep/depend ::level3a ::level2)
      (dep/depend ::level3b ::level2)
      (dep/depend ::level3c ::level2)
      (dep/depend ::level3d ::level2)))

(dep/topo-sort graph)
;; => (:demo.core/level0
;;     :demo.core/level1a
;;     :demo.core/level1b
;;     :demo.core/level1c
;;     :demo.core/level1d
;;     :demo.core/level2
;;     :demo.core/level3b
;;     :demo.core/level3a
;;     :demo.core/level3c
;;     :demo.core/level3d)

(dep/topo-sort compare graph)
;; => (:demo.core/level0
;;     :demo.core/level1a
;;     :demo.core/level1b
;;     :demo.core/level1c
;;     :demo.core/level1d
;;     :demo.core/level2
;;     :demo.core/level3a
;;     :demo.core/level3b
;;     :demo.core/level3c
;;     :demo.core/level3d)

;;; fold
;;; -----------------------
(def sys-conf
  {::a 1
   ::b 2
   ::x {:a (ig/ref ::a)
        :b (ig/ref ::b)}})

(ig/init sys-conf)
;; => #:demo.core{:a 1, :b 2, :x {:a 1, :b 2}}

(ig/fold (ig/init sys-conf) (fn [v k _] (conj v k)) [])
;; => [:demo.core/a :demo.core/b :demo.core/x]
