(ns demo.integration-test
  (:require [clojure.string :as str]
            [demo.config :as conf]
            [demo.db :as db]
            [demo.query :as q]
            [demo.system :as system]
            [integrant.core :as ig]
            [ragtime.core :as rg]))

(defn drop-all-tables [db]
  (doseq [q (map :pg-drop
                 (db/fetch db
                           (str/join
                            " "
                            ["SELECT"
                             " 'drop table if exists ' || tablename || ' cascade;' as pg_drop"
                             "FROM"
                             " pg_tables"
                             "WHERE"
                             " schemaname = 'public';"])))]
    (db/execute db q)))

(defmacro with-test-system [sym & body]
  `(let [~sym (system/start "local-config.edn")
         ~'&db (~sym :app/hikari-cp)
         ragtime# (~sym  :app/ragtime)]
     (try
       (rg/migrate-all (:datastore ragtime#) {} (:migrations ragtime#))
       ~@body
       (finally
         (drop-all-tables ~'&db)
         (ig/halt! ~sym)))))

(t/deftest simple-test-cases
  (with-test-system system
    (let [board-foo (q/add-board &db "board-foo")
          board-bar (q/add-board &db "board-bar")
          board-baz (q/add-board &db "board-baz")

          task-foo-1 (q/add-task &db "task-foo-1")
          task-bar-1 (q/add-task &db "task-bar-1")
          task-baz-1 (q/add-task &db "task-baz-1")]

      (t/is (compatible
             {nil (fj/contains [{:task-title "task-foo-1"}
                                {:task-title "task-bar-1"}
                                {:task-title "task-baz-1"}]
                               :in-any-order)}
             (group-by :board-name (q/find-tasks &db))))

      (q/put-task-on-board &db (:task-uuid task-foo-1) (:board-uuid board-foo))
      (q/put-task-on-board &db (:task-uuid task-bar-1) (:board-uuid board-bar))
      (q/put-task-on-board &db (:task-uuid task-baz-1) (:board-uuid board-baz))

      (t/is (compatible
             (group-by :board-name (q/find-tasks &db))
             {"board-foo" [(fj/contains {:task-title "task-foo-1" :task-order 0})]
              "board-bar" [(fj/contains {:task-title "task-bar-1" :task-order 0})]
              "board-baz" [(fj/contains {:task-title "task-baz-1" :task-order 0})]}))

      (let [task-foo-2 (q/add-task &db "task-foo-2")
            task-foo-3 (q/add-task &db "task-foo-3")
            task-foo-4 (q/add-task &db "task-foo-4")
            task-foo-5 (q/add-task &db "task-foo-5")]
        (q/put-task-on-board &db (:task-uuid task-foo-2) (:board-uuid board-foo))
        (q/put-task-on-board &db (:task-uuid task-foo-3) (:board-uuid board-foo))
        (q/put-task-on-board &db (:task-uuid task-foo-4) (:board-uuid board-foo))
        (q/put-task-on-board &db (:task-uuid task-foo-5) (:board-uuid board-foo))

        (t/is (compatible
               {"board-foo" [(fj/contains {:task-title "task-foo-1" :task-order 0})
                             (fj/contains {:task-title "task-foo-2" :task-order 1})
                             (fj/contains {:task-title "task-foo-3" :task-order 2})
                             (fj/contains {:task-title "task-foo-4" :task-order 3})
                             (fj/contains {:task-title "task-foo-5" :task-order 4})]
                "board-bar" [(fj/contains {:task-title "task-bar-1" :task-order 0})]
                "board-baz" [(fj/contains {:task-title "task-baz-1" :task-order 0})]}
               (group-by :board-name (q/find-tasks &db))))

        (q/reput-task-on-board &db (:task-uuid task-foo-2) (:board-uuid board-bar))
        (q/reput-task-on-board &db (:task-uuid task-foo-3) (:board-uuid board-bar))

        (t/is (compatible
               {"board-foo" [(fj/contains {:task-title "task-foo-1" :task-order 0})
                             (fj/contains {:task-title "task-foo-4" :task-order 1})
                             (fj/contains {:task-title "task-foo-5" :task-order 2})]
                "board-bar" [(fj/contains {:task-title "task-bar-1" :task-order 0})
                             (fj/contains {:task-title "task-foo-2" :task-order 1})
                             (fj/contains {:task-title "task-foo-3" :task-order 2})]
                "board-baz" [(fj/contains {:task-title "task-baz-1" :task-order 0})]}
               (group-by :board-name (q/find-tasks &db))))

        (q/reput-task-on-board &db (:task-uuid task-foo-5) (:board-uuid board-foo) (:task-uuid task-foo-1))

        (t/is (compatible
               {"board-foo" [(fj/contains {:task-title "task-foo-1" :task-order 0})
                             (fj/contains {:task-title "task-foo-5" :task-order 1})
                             (fj/contains {:task-title "task-foo-4" :task-order 2})]
                "board-bar" [(fj/contains {:task-title "task-bar-1" :task-order 0})
                             (fj/contains {:task-title "task-foo-2" :task-order 1})
                             (fj/contains {:task-title "task-foo-3" :task-order 2})]
                "board-baz" [(fj/contains {:task-title "task-baz-1" :task-order 0})]}
               (group-by :board-name (q/find-tasks &db))))))))
