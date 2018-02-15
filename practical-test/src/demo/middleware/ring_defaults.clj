(ns demo.middleware.ring-defaults
  (:require [ring.middleware.defaults :as defaults]
            [integrant.core :as ig]))

(defmethod ig/init-key :app.middleware/ring-defaults
  [_ opts]
  (fn wrap-defaults [handler]
    (defaults/wrap-defaults handler opts)))
