(ns demo.repl
  (:require [clojure.java.io :as io]
            [clojure.java.javadoc :refer [javadoc]]
            [clojure.pprint :refer [pprint]]
            [clojure.reflect :refer [reflect]]
            [clojure.repl :refer [apropos dir doc find-doc pst source]]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.test :as test]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [com.stuartsierra.component :as c]
            [demo.system :as s]))

(def system nil)

(defn init []
  (alter-var-root #'system (constantly (s/demo-system {}))))

(defn start []
  (alter-var-root #'system c/start-system))

(defn stop []
  (alter-var-root #'system c/stop-system))

(defn go []
  (init)
  (start)
  :ready)

(defn reset []
  (stop)
  (refresh :after `go))
