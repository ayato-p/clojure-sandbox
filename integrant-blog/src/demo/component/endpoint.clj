(ns demo.component.endpoint
  (:require [bidi.ring :as bidi-ring]
            [clojure.string :as str]
            [demo.middleware :as mid]
            [demo.route :as route]
            [integrant.core :as ig]))

(defn dispatcher [handler-kw]
  {:pre [(keyword? handler-kw) (namespace handler-kw)]}
  (->> ((juxt namespace name) handler-kw)
       (str/join "/")
       symbol
       resolve))

(def handler
  (-> (bidi-ring/make-handler route/app-routes dispatcher)
      mid/middlwares))

(defmethod ig/init-key ::endpoint [_ {:keys [db]}]
  (fn [req]
    (handler (assoc req :$db db))))

(defmethod ig/halt-key! ::endpoint [_ _])
