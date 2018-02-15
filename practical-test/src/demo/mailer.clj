(ns demo.mailer
  (:refer-clojure :exclude [send])
  (:require [postal.core :as postal])
  (:import demo.boundary.mailer.MailerBoundary))

(defprotocol Mailer
  (send-message [this mail]))

(extend-protocol Mailer
  MailerBoundary
  (send-message [this mail]
    (->> (assoc mail :from (:from this))
         (postal/send-message (:smtp this)))))
