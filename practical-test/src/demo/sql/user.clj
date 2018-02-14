(ns demo.sql.user
  (:require [clojure.java.jdbc :as jdbc])
  (:import demo.boundary.sql.SQLBoundary))

(defprotocol UserDatabase
  (find-all [this]))

(extend-protocol UserDatabase
  SQLBoundary
  (find-all [this]
    (jdbc/query this "select * from users")))
