(ns demo.core-test
  (:require [clojure.test :as t]
            [demo.test-helper :as h]
            [clojure.java.jdbc :as jdbc]))

(defn gen-users [db]
  (jdbc/with-db-transaction [db db]
    (dotimes [idx 1000]
      (jdbc/execute! db (format "insert into users (id, username, password) values (%s, '%s', '%s')"
                                idx (h/rand-alphabet 10) (h/rand-alphabet 1000))))))

(defn count-users [db]
  (-> (jdbc/query db "select count(id) from users")
      first
      :count))

(t/deftest parallel-test-1
  (h/with-test-database db h/db-spec
    (gen-users db)

    (t/is (= (count-users db) 1000))))

(t/deftest parallel-test-2
  (h/with-test-database db h/db-spec
    (gen-users db)

    (t/is (= (count-users db) 1000))))

(t/deftest parallel-test-3
  (h/with-test-database db h/db-spec
    (gen-users db)

    (t/is (= (count-users db) 1000))))

(t/deftest parallel-test-4
  (h/with-test-database db h/db-spec
    (gen-users db)

    (t/is (= (count-users db) 1000))))

(t/deftest parallel-test-5
  (h/with-test-database db h/db-spec
    (gen-users db)

    (t/is (= (count-users db) 1000))))

(t/deftest parallel-test-6
  (h/with-test-database db h/db-spec
    (gen-users db)

    (t/is (= (count-users db) 1000))))

(t/deftest parallel-test-7
  (h/with-test-database db h/db-spec
    (gen-users db)

    (t/is (= (count-users db) 1000))))

(t/deftest parallel-test-8
  (h/with-test-database db h/db-spec
    (gen-users db)

    (t/is (= (count-users db) 1000))))

(t/deftest parallel-test-9
  (h/with-test-database db h/db-spec
    (gen-users db)

    (t/is (= (count-users db) 1000))))

(t/deftest parallel-test-10
  (h/with-test-database db h/db-spec
    (gen-users db)

    (t/is (= (count-users db) 1000))))
