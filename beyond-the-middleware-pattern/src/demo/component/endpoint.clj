(ns demo.component.endpoint
  (:require [com.stuartsierra.component :as c]
            [demo.component.middleware.proto :as mp]
            [ring.util.response :as res]))

(defprotocol IEndpoint
  (ring-handler [this]))

(defn default-handler [req]
  (-> (pr-str (:params req))
      res/response
      (res/content-type "text/plain; charset=utf-8")))

(defrecord Endpoint [middleware]
  c/Lifecycle
  (start [this]
    (println "Start endpoint")
    this)
  (stop [this]
    (println "Stop endpoint")
    this)

  IEndpoint
  (ring-handler [this]
    (mp/wrap middleware default-handler)))
