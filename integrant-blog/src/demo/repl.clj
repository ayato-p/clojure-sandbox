(ns demo.repl
  (:require [baum.core :as b]
            [demo.component.migrator :as mig]
            [demo.integrant.baum :as ib]
            [demo.system :as sys]))

(defn go []
  (let [conf (b/read-file "resources/config.edn"
                          {:readers {'igref ib/igref}})]
    (sys/boot conf)))

(defn stop []
  (sys/shutdown!))

(defn gen-migration-file [filename]
  (mig/gen-migration-file (::mig/migrator sys/+system+) filename))

(defn migrate []
  (mig/migrate (::mig/migrator sys/+system+)))

(defn rollback []
  (mig/rollback (::mig/migrator sys/+system+)))
