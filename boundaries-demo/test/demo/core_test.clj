(ns demo.core-test
  (:require [clojure.test :as t]
            [demo.core :as sut]
            [integrant.core :as ig]
            [shrubbery.core :as shrubbery]))

(def simple-user-database-mock
  (let [database (reduce (fn [m {:keys [username] :as user}]
                           (assoc m username user))
                         {}
                         [{:username "ayato-p"}
                          {:username "athos"}])]
    (reify sut/UserDatabase
      (get-user [_ username]
        (get database username)))))

(t/deftest simple-mocking-test
  (t/is (= (sut/get-user simple-user-database-mock "athos")
           {:username "athos"}))
  (t/is (= (sut/get-user simple-user-database-mock "ayato-p")
           {:username "ayato-p"})))

(t/deftest real-database-test
  (let [system (ig/init sut/system-config [:myapp.handler/get-user])
        get-user-fn (:myapp.handler/get-user system)]
    (t/is (= (get-user-fn "athos")
             {:username "athos"}))
    (t/is (= (get-user-fn "ayato-p")
             {:username "ayato-p"}))))

(t/deftest shrubbery-stub-test
  (let [user-database-stub (shrubbery/stub
                            sut/UserDatabase
                            {:get-user {:username "ayato-p"}})]
    (t/is (= (sut/get-user user-database-stub "ayato-p")
             {:username "ayato-p"}))))

(t/deftest shrubbery-mock-test
  (let [user-database-mock (shrubbery/mock
                            sut/UserDatabase
                            {:get-user {:username "ayato-p"}})]
    (t/is (not (shrubbery/received? user-database-mock sut/get-user)))
    (t/is (= (sut/get-user user-database-mock "ayato-p")
             {:username "ayato-p"}))
    (t/is (= (shrubbery/call-count user-database-mock sut/get-user) 1))
    (t/is (shrubbery/received? user-database-mock sut/get-user))
    (t/is (shrubbery/received? user-database-mock sut/get-user ["ayato-p"]))
    (t/is (not (shrubbery/received? user-database-mock sut/get-user ["athos"])))))
