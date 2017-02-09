(ns demo.endpoint
  (:require [bidi.ring :as bidi-ring]
            [demo.middleware :as mid]
            [demo.routes :as routes]
            [clojure.string :as str]))

(defn dispatcher [handler-kw]
  {:pre [(keyword? handler-kw) (namespace handler-kw)]}
  (->> ((juxt namespace name) handler-kw)
       (str/join "/")
       symbol
       resolve))

(def handler
  (-> (bidi-ring/make-handler routes/app-routes dispatcher)
      mid/middlwares))
