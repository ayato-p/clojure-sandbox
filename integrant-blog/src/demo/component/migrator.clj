(ns demo.component.migrator
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [integrant.core :as ig]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl])
  (:import java.time.format.DateTimeFormatter
           java.time.LocalDateTime
           java.util.UUID))

(defmethod ig/init-key ::migrator [_ {:keys [db path]}]
  {:path (some-> (io/resource path)
                 (.getPath))
   :datastore (jdbc/sql-database db)
   :migrations (jdbc/load-resources path)})

(defmethod ig/halt-key! ::migrator [_ _])

(def ^:private simple-format
  (DateTimeFormatter/ofPattern "yyyyMMddHHmmss"))

(defn gen-migration-file
  ([migrator]
   (gen-migration-file migrator (str (UUID/randomUUID))))
  ([migrator filename]
   (let [datetime-part (.format (LocalDateTime/now) simple-format)
         prefix (str/join "-" [datetime-part filename])
         path (:path migrator)
         files (map #(io/as-file (str path "/" prefix "." % ".sql")) ["up" "down"])]
     (if (reduce (fn [b f]
                   (and b (.createNewFile f)))
                 true
                 files)
       (println "Success!!")
       (do
         (println "Failure!!")
         (doseq [f files
                 :when (.exists f)]
           (.delete f)))))))

(defn migrate [migrator]
  (repl/migrate (dissoc migrator :path)))

(defn rollback [migrator]
  (repl/rollback (dissoc migrator :path)))
