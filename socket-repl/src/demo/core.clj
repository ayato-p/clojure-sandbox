(ns demo.core
  (:require [immutant.web :as web]
            [ring.util.response :as res])
  (:gen-class))

(defonce server (atom nil))

(defn handler [req]
  (-> "Hello"
      res/response
      (res/content-type "text/plain")))

(defn start-server []
  (when-not @server
    (reset! server (web/run #'handler {:port 3000}))))

(defn stop-server []
  (when @server
    (web/stop @server)
    (reset! server nil)))

(defn -main []
  (start-server))
