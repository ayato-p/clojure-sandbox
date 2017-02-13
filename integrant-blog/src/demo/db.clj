(ns demo.db
  (:require [clojure.java.jdbc :as jdbc]
            [inflections.core :as inf]
            [stch.sql :as sql]
            [stch.sql.format :as sf]
            [clojure.string :as str]))

(defn execute
  ([db sql]
   (execute db sql {}))
  ([db sql opts]
   (letfn [(execute* [con]
             (let [sql (cond-> sql (map? sql) sf/format)
                   sql (if (vector? sql) sql [sql])]
               (with-open [p (jdbc/prepare-statement (jdbc/db-find-connection con) (first sql) {:return-keys true})]
                 (jdbc/execute! con (into [p] (rest sql)) opts)
                 (doall (jdbc/result-set-seq (.getGeneratedKeys p) {:identifiers inf/dasherize})))))]
     (if-let [con (jdbc/db-find-connection db)]
       (execute* con)
       (jdbc/with-db-connection [con db]
         (execute* con))))))

(defn fetch
  ([db sql]
   (fetch db sql {}))
  ([db sql opts]
   (let [sql (cond-> sql (map? sql) sf/format)
         opts (merge {:identifiers (comp str/lower-case inf/dasherize)} opts)]
     (jdbc/query db sql opts))))
