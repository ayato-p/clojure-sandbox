(ns demo.middleware
  (:require [demo.response :as res]
            [ring.middleware.defaults :as defaults]
            [clojure.string :as str]))

(defn- remove-trailing-slash [s]
  (if (and (string? s) (not= s "/") (str/ends-with? s "/"))
    (apply str (butlast s))
    s))

(defn wrap-remove-trailing-slash [handler]
  (fn [req]
    (-> (cond-> req
          (:uri req) (update :uri remove-trailing-slash)
          (:path-info req) (update :path-info remove-trailing-slash))
        handler)))

(defn wrap-webpage [handler]
  (fn [req]
    (res/html (handler req))))

(defn middlwares [handler]
  (-> handler
      (defaults/wrap-defaults defaults/site-defaults)
      wrap-webpage
      wrap-remove-trailing-slash))
