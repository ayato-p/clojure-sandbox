(ns demo.sql.user
  (:require [clojure.java.jdbc :as jdbc])
  (:import demo.boundary.sql.SQLBoundary))

(defprotocol UserFinder
  (find-all [this])
  (find-by-id [this id]))

(extend-protocol UserFinder
  SQLBoundary
  (find-all [this]
    (jdbc/query this "select * from users"))
  (find-by-id [this id]
    (first (jdbc/query this ["select * from users where id = ?" id]))))
