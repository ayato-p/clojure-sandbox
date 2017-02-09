(ns demo.handler.main
  (:require [ring.util.response :as res]))

(defn home [req]
  (res/response "Hello!!"))
