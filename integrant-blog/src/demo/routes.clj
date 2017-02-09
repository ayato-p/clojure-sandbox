(ns demo.routes
  (:require [demo.handler.main :as main]))

(def app-routes
  ["/" {"" ::main/home}])
