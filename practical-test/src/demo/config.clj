(ns demo.config
  (:require [baum.core :as b]
            [clojure.java.io :as io]
            [integrant.core :as ig]))

(defn read-config []
  (b/read-file (io/resource "config.edn")
               {:readers {'ig/ref ig/ref
                          'ig/refset ig/refset}}))
