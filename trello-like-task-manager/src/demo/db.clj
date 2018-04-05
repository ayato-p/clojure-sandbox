(ns demo.db
  (:require [camel-snake-kebab.core :as csk]
            [clojure.java.jdbc :as jdbc]
            [potemkin.namespaces :as pk.ns])
  (:import com.zaxxer.hikari.HikariDataSource))

(pk.ns/import-vars [jdbc with-db-transaction])

(defprotocol Coercer
  (->db-spec [this]))

(extend-protocol Coercer
  Object
  (->db-spec [obj] obj)

  com.zaxxer.hikari.HikariDataSource
  (->db-spec [datasource]
    {:datasource datasource}))

(defn execute [db query]
  (-> (->db-spec db)
      (jdbc/execute! query {:return-keys true :identifiers csk/->kebab-case})))

(defn fetch [db query]
  (-> (->db-spec db)
      (jdbc/query query {:identifiers csk/->kebab-case})))
