(ns demo.test-helper
  (:require [clojure.spec.alpha :as s]
            [demo.config :as conf]
            [integrant.core :as ig]
            [clojure.set :as set]))

(defn read-test-config []
  (conf/read-config))

(s/def ::component-keys
  (s/and vector?
         (s/+ keyword?)))

(s/def ::rename-keys map?)

(s/def ::with-test-system-args
  (s/cat :component-keys (s/? (s/spec ::component-keys))
         :rename-keys (s/? ::rename-keys)
         :body (s/* any?)))

(defmacro with-test-system [system-sym & args]
  (let [{:keys [component-keys rename-keys body]} (s/conform ::with-test-system-args args)]
    `(let [conf# (cond-> (read-test-config)
                   (map? ~rename-keys) (set/rename-keys ~rename-keys))
           ~system-sym (if ~component-keys
                         (ig/init (ig/prep conf#) ~component-keys)
                         (ig/init (ig/prep conf#)))]
       ~@body)))

(defmacro with-test-database [db-sym & body]
  `(with-test-system ~'&system [:app/database]
     (let [~db-sym (:app/database ~'&system)]
       ~@body)))
