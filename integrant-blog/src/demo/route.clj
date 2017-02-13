(ns demo.route
  (:require [demo.handler.main :as main]
            [demo.handler.user :as user]))

(def app-routes
  ["/" {"" ::main/home
        "users" {"" ::user/index}}])
