(ns demo.config
  (:require [baum.core :as b]
            [clojure.java.io :as io]
            [integrant.core :as ig]))

(defn read-config
  ([] (read-config "config.edn"))
  ([filename]
   (b/read-file (io/resource filename)
                {:readers {'ig/ref ig/ref
                           'ig/refset ig/refset}})))
