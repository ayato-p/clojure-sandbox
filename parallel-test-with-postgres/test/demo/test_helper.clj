(ns demo.test-helper
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]
            [clojure.test :as t]))

(def db-spec
  {:connection-uri "jdbc:postgresql://localhost:5432/alice_db?user=alice"})

(defn inject-schema [db-spec schema]
  (update db-spec :connection-uri #(str % "&currentSchema=" schema)))

(defn rand-alphabet [n]
  (let [a-z (map char (range (int \a) (inc (int \z))))]
    (str/join (repeatedly n #(rand-nth a-z)))))

(defn migrate [db-spec]
  (->> ["create table users ("
        "  id int primary key"
        "  ,username text not null"
        "  ,password text not null"
        ")"]
       (str/join " ")
       (jdbc/execute! db-spec)))

(defmacro with-test-database [db-sym db-spec & body]
  (let [schema (rand-alphabet 10)]
    `(let [~db-sym (inject-schema ~db-spec ~schema)]
       (try
         (jdbc/execute! ~db-sym (str "create schema " ~schema))

         (migrate ~db-sym)

         ~@body

         (finally
           (jdbc/execute! ~db-sym (str "drop schema " ~schema " cascade")))))))
