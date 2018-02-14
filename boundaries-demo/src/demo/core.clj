(ns demo.core
  (:require [clojure.java.jdbc :as jdbc]
            [integrant.core :as ig]))

(comment
  (defn get-user [db username]
    (first (jdbc/query db ["select * from users where username = ?" username])))

  (let [db-spec {:dbtype "postgresql"
                 :dbname "mydatabase"
                 :user "postgres"}]
    (get-user db-spec "ayato-p"))
  ;; => {:username "ayato-p"}
  )


(defrecord SQLBoundary [db-spec])

(defprotocol UserDatabase
  (get-user [this username]))

(extend-protocol UserDatabase
  SQLBoundary
  (get-user [{db-spec :db-spec :as db} username]
    (first (jdbc/query db-spec ["select * from users where username = ?" username]))))


(comment
  (let [db (map->SQLBoundary {:db-spec {:dbtype "postgresql"
                                        :dbname "mydatabase"
                                        :user "postgres"}})]
    (get-user db "ayato-p"))
  ;; => {:username "ayato-p"}
  )


(def system-config
  {:myapp.sql/boundary {:db-spec {:dbtype "postgresql"
                                  :dbname "mydatabase"
                                  :user "postgres"}}
   :myapp.handler/get-user {:db (ig/ref :myapp.sql/boundary)}})

(defmethod ig/init-key :myapp.sql/boundary
  [_ opts]
  (map->SQLBoundary opts))

(defmethod ig/init-key :myapp.handler/get-user
  [_ {:keys [db]}]
  (fn [req]
    (let [username (get-in req [:params :username])]
      {:status 200
       :body (:username (get-user db username))})))

(defn start-system []
  (ig/init system-config))

(comment
  (let [system (start-system)
        f (:myapp.handler/get-user system)]
    (f "ayato-p"))
  ;; => {:username "ayato-p"}
  )
