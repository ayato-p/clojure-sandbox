(ns demo.system
  (:require [bidi.bidi :as bidi]
            [bidi.ring :as bidi.ring]
            [demo.boundary.mailer :as boundary.mailer]
            [demo.boundary.sql :as boundary.sql]
            [integrant.core :as ig]
            ragtime.jdbc
            [ring.adapter.jetty :as jetty]))

(require '[demo.handler.home]
         '[demo.handler.user]
         '[demo.middleware.html]
         '[demo.middleware.ring-defaults])

(defmethod ig/init-key :app/server
  [_ opts]
  (jetty/run-jetty (:handler opts) (dissoc opts :handler)))

(defmethod ig/halt-key! :app/server
  [_ server]
  (.stop server))

(defmethod ig/prep-key :app/router
  [_ {:keys [routes] :as opts}]
  (->> (bidi/route-seq routes)
       (map :handler)
       (reduce (fn [m h] (assoc m h (ig/ref h))) {})
       (assoc opts :handler-fn)))

(defmethod ig/init-key :app/router
  [_ m]
  m)

(defmethod ig/prep-key :app/endpoint
  [_ {:keys [middlewares] :as opts}]
  (update opts :middlewares #(map ig/ref %)))

(defmethod ig/init-key :app/endpoint
  [_ {:keys [router middlewares]}]
  (let [{:keys [routes handler-fn]} router]
    (reduce #(%2 %1)
            (bidi.ring/make-handler routes handler-fn)
            middlewares)))

(defmethod ig/init-key ::mailer
  [_ {:keys [smtp from] :as settings}]
  (boundary.mailer/map->MailerBoundary settings))

(defmethod ig/init-key ::database
  [_ db-spec]
  (boundary.sql/map->SQLBoundary db-spec))

(defmethod ig/init-key :app/migrator
  [_ {:keys [db migrations-dir]}]
  {:datastore (ragtime.jdbc/sql-database db)
   :migrations (ragtime.jdbc/load-resources migrations-dir)})
