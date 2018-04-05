(ns demo.system
  (:require [clojure.set :as set]
            [demo.config :as conf]
            [hikari-cp.core :as hikari-cp]
            [integrant.core :as ig]
            [ragtime.jdbc :as rag.jdbc]))

(defmethod ig/init-key :app/database
  [_ db-spec]
  db-spec)

(def ^:private db-spec->hikari-cp
  {:dbtype :adapter
   :dbname :database-name
   :host   :server-name
   :port   :port-number
   :user   :username})

(defmethod ig/init-key :app/hikari-cp
  [_ {:keys [db-spec pool-options]}]
  (->> (set/rename-keys db-spec db-spec->hikari-cp)
       (conj pool-options)
       hikari-cp/make-datasource))

(defmethod ig/init-key :app/ragtime
  [_ {:keys [db-spec migration-dir]}]
  {:datastore (rag.jdbc/sql-database db-spec)
   :migrations (rag.jdbc/load-resources migration-dir)})

(defn prep []
  (ig/prep (conf/read-config "config.edn")))

(defn start []
  (ig/init (prep)))
