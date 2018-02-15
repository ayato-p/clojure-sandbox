(ns demo.handler.user
  (:require [bidi.bidi :as bidi]
            [demo.mailer :as mailer]
            [demo.sql.user :as sql.user]
            [hiccup2.core :as h]
            [integrant.core :as ig]
            [ring.util.anti-forgery :as anti-forgery]
            [ring.util.response :as res]))

(defmethod ig/init-key :app.handler.user/index
  [_ {:keys [db]}]
  (fn user-index [req]
    (let [routes (get-in req [:cargo :router :routes])
          users (sql.user/find-all db)]
      (->> [:table
            [:thead
             [:tr [:th "なまえ"] [:th "メールアドレス"] [:th]]]
            [:tbody
             (for [u users]
               [:tr
                [:td (:fullname u)]
                [:td (:email u)]
                [:td
                 [:form
                  {:action (bidi/path-for routes :app.handler.user/send-mail :user/id (:id u))
                   :method "post"}
                  (h/raw (anti-forgery/anti-forgery-field))
                  [:button {:type :submit} "メールを送信する"]]]])]]
           h/html
           str
           res/response))))

(defmethod ig/init-key :app.handler.user/send-mail
  [_ {:keys [db mailer]}]
  (fn send-mail [{:keys [route-params] :as req}]
    (let [routes (get-in req [:cargo :router :routes])
          user (->> (:user/id route-params)
                    Long/parseLong
                    (sql.user/find-by-id db))]
      (mailer/send-message mailer {:to (:email user)
                                   :subject "テストメール"
                                   :body "これはテスト"})
      (res/redirect (bidi/path-for routes :app.handler.user/index)))))
