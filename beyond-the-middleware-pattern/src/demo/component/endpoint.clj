(ns demo.component.endpoint
  (:require [demo.component.middleware.proto :as mp]
            [ring.util.response :as res]))

(defprotocol IEndpoint
  (ring-handler [this]))

(defn default-handler [req]
  (-> (pr-str (select-keys req [:params :company-info]))
      res/response
      (res/content-type "text/plain; charset=utf-8")))

(defrecord Endpoint [middleware]
  IEndpoint
  (ring-handler [this]
    (mp/wrap middleware default-handler)))
