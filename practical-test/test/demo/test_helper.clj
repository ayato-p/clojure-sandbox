(ns demo.test-helper
  (:require [clojure.spec.alpha :as s]
            [demo.config :as conf]
            [integrant.core :as ig]))

(defn read-test-config []
  (conf/read-config))

(s/def ::component-keys
  (s/and vector?
         (s/+ keyword?)))

(s/def ::test-conf
  (s/map-of keyword? any?))

(s/def ::with-test-system-args
  (s/cat :component-keys (s/? (s/spec ::component-keys))
         :test-conf (s/? ::test-conf)
         :body (s/* any?)))

(s/conform ::with-test-system-args '([:x :y] {:xxx 1}))

(defmacro with-test-system [system-sym & args]
  (let [{:keys [component-keys test-conf body]} (s/conform ::with-test-system-args args)]
    `(let [~system-sym ~(if component-keys
                          `(ig/init (ig/prep (merge (read-test-config) ~test-conf)) ~component-keys)
                          `(ig/init (ig/prep (merge (read-test-config) ~test-conf))))]
       ~@body)))

(defmacro with-test-database [db-sym & body]
  `(with-test-system ~'&system [:app/database]
     (let [~db-sym (:app/database ~'&system)]
       ~@body)))
