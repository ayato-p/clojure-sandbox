(ns demo.middleware
  (:require [demo.response :as res]
            [ring.middleware.defaults :as defaults]))

(defn wrap-webpage [handler]
  (fn [req]
    (res/html (handler req))))

(defn middlwares [handler]
  (-> handler
      (defaults/wrap-defaults defaults/site-defaults)
      wrap-webpage))
