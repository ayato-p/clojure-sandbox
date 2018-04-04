(ns user
  (:require [clojure.repl :refer :all]
            [demo.system :as system]
            [hara.namespace.eval :as hara.ns.eval]
            [integrant.repl :as ig.repl :refer [go reset reset-all]]
            [integrant.repl.state :as ig.repl.state]
            [potemkin.namespaces :as pk.ns]
            [ragtime.repl :as rg.repl]))

(ig.repl/set-prep! system/prep)

(defn migrate [system]
  (rg.repl/migrate (system :app/ragtime)))

(defn rollback [system]
  (rg.repl/rollback (system :app/ragtime)))


;;; Hack -----------------------------------------------------------------------

(def $ #'ig.repl.state/system)

(defmacro inject-to-core [syms]
  (let [from-ns (ns-name *ns*)]
    `(hara.ns.eval/with-ns 'clojure.core
       (pk.ns/import-vars [~from-ns ~@syms]))))

(inject-to-core [$])
