(ns demo.middleware.cargo
  (:require [integrant.core :as ig]))

(defmethod ig/init-key :app.middleware/cargo
  [_ deps]
  (fn wrap-cargo [handler]
    (fn cargo [req]
      (handler (assoc req :cargo deps)))))
