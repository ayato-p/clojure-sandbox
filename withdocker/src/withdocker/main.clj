(ns withdocker.main
  (:gen-class)
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as r]
            [immutant.web :as web]
            [ring.middleware.defaults :as defaults]))

(defroutes app-routes
  (GET "/" [] "Hello Docker World")
  (r/not-found "Not Found"))

(def app
  (defaults/wrap-defaults app-routes defaults/site-defaults))

(defn -main []
  (web/run #'app :host "0.0.0.0" :port 8080))
