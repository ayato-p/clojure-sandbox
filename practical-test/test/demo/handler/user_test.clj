(ns demo.handler.user-test
  (:require [clojure.set :as set]
            [clojure.test :as t]
            [demo.mailer :as mailer]
            [demo.sql.user :as sql.user]
            [demo.test-helper :as h]
            [fudje.sweet :as fj]
            [integrant.core :as ig]
            [shrubbery.core :as shrubbery]))

(defmethod ig/init-key ::mailer
  [_ _]
  (shrubbery/mock
   mailer/Mailer
   {:send-message {:code 0, :error :SUCCESS, :message "messages sent"}}))

(defmethod ig/init-key ::database
  [_ _]
  (shrubbery/mock
   sql.user/UserFinder
   {:find-by-id {:id 1, :fullname "bob" :email "bob@acme.com"}}))

(t/deftest send-mail-test
  (h/with-test-system sys [:app.handler.user/send-mail]
    {[:app/database :demo.system/database] [:app/database ::database]
     [:app/mailer :demo.system/mailer] [:app/mailer ::mailer]}

    (let [handler (:app.handler.user/send-mail sys)
          database (get sys [:app/database ::database])
          mailer (get sys [:app/mailer ::mailer])]

      (t/is (compatible
             (handler {:route-params {:user/id "1"}})
             (fj/contains {:status 302})))
      (t/is (shrubbery/received? mailer mailer/send-message
                                 [{:to "bob@acme.com" :subject "テストメール" :body "これはテスト"}]))
      (t/is (= (shrubbery/call-count mailer mailer/send-message) 1)))))
