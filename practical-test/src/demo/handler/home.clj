(ns demo.handler.home
  (:require [integrant.core :as ig]
            [ring.util.response :as res]))

(defmethod ig/init-key :app.handler.home/index
  [_ _]
  (fn home-index [req]
    (res/response "Hello")))
