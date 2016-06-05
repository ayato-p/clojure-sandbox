(ns demo.component.server
  (:require [com.stuartsierra.component :as c]
            [demo.component.endpoint :as ep]
            [ring.adapter.jetty :as jetty]))

(defrecord WebServer [endpoint host port]
  c/Lifecycle
  (start [this]
    (println "Start server")
    (if-not (contains? this :server)
      (let [ep (ep/ring-handler endpoint)]
        (assoc this :server (jetty/run-jetty ep {:join? false :host host :port port})))
      this))
  (stop [this]
    (println "Stop server")
    (if-let [ep (:server this)]
      (do (.stop ep)
          (dissoc this :server))
      this)))
