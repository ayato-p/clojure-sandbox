(ns demo.middleware.html
  (:require [ring.util.response :as res]
            [integrant.core :as ig]))

(defmethod ig/init-key :app.middleware/ensure-html
  [_ _]
  (fn wrap-ensure-html [handler]
    (fn ensure-html [req]
      (let [res (handler req)]
        (cond-> res
          (nil? (get-in res [:headers "Content-Type"]))
          (res/content-type "text/html; charset=UTF-8"))))))
