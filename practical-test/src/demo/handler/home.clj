(ns demo.handler.home
  (:require [hiccup2.core :as h]
            [integrant.core :as ig]
            [ring.util.response :as res]))

(defmethod ig/init-key :app.handler.home/index
  [_ _]
  (fn home-index [req]
    (->> [:h1 "Demo Home"]
         h/html
         str
         res/response)))
