(ns demo.system
  (:require [bidi.bidi :as bidi]
            [bidi.ring :as bidi.ring]
            [demo.boundary.sql :as boundary.sql]
            demo.handler.home
            demo.handler.user
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]))

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

(defmethod ig/init-key :app/endpoint
  [_ {:keys [router]}]
  (let [{:keys [routes handler-fn]} router]
    (bidi.ring/make-handler routes handler-fn)))

(defmethod ig/init-key :app/database
  [_ db-spec]
  (boundary.sql/map->SQLBoundary db-spec))
