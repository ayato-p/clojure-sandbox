(ns demo.integrant.baum
  (:require [baum.core :as b]
            [integrant.core :as ig]))

(b/defreader igref [v opts]
  (ig/ref v))
