(ns demo.core
  (:require [baum.core :as b]
            [clojure.java.io :as io]))

(b/read-file (io/resource "config/conf-1.edn"))
(b/read-file (io/resource "zou/config/default/ring-defaults.edn"))

(io/resource "zou/config/default/ring-defaults.edn")
