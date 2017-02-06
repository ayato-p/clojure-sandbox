(ns demo.system
  (:require [integrant.core :as ig]))

(defonce +system+ nil)

(defn- halt-system [sys]
  (when sys (ig/halt! sys)))

(defn boot
  ([] (boot {}))
  ([config]
   (alter-var-root
    #'+system+
    (fn [sys] (halt-system sys)
      (ig/load-namespaces config)
      (ig/init config)))))

(defn shutdown!
  []
  (halt-system +system+))
