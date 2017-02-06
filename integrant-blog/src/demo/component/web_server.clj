(ns demo.component.web-server
  (:require [immutant.web :as web]
            [integrant.core :as ig]
            [ring.util.response :as res]))

(defn- default-handler [req]
  (res/response "Hello, world"))

(defmethod ig/init-key ::server [_ {:keys [handler] :as opts}]
  (web/run (or handler default-handler) opts))

(defmethod ig/halt-key! ::server [_ server]
  (web/stop))
