(ns demo.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as res]
            [ring.middleware.defaults :as defaults]))

(defonce server (atom nil))

(defn- find-by-user-id [id]
  {:name "ayato-p" :age 25})

(defn wrap-user [handler]
  (fn [req]
    (let [user (find-by-user-id (get-in req [:session :user-id]))]
      (cond-> req
        user (assoc :user user)
        :else handler))))

(defn wrap-html [handler]
  (fn [req]
    (-> (handler req)
        (res/content-type "text/html; charset=UTF8"))))

(defn- fib [n]
  (if (< n 2)
    n
    (+ (fib (- n 2))
       (fib (- n 1)))))

(defn wrap-fib [handler]
  (let [memoized-fib (memoize fib)]
    (fn [req]
      (let [n (Long/parseLong (get-in req [:params :n] "0") 10)]
        (handler (assoc req :fib-num (memoized-fib n)))))))

(defn auth-error-handler [req])

(defn authenticated? [req]
  true)

(defn wrap-auth [handler]
  (fn [req]
    (if (authenticated? req)
      (handler req)
      (auth-error-handler req))))

(defn handler [req]
  (-> (pr-str req)
      res/response))

(def app
  (-> handler
      wrap-user
      wrap-html
      wrap-fib
      (defaults/wrap-defaults defaults/site-defaults)))

(defn start-server []
  (when-not @server
    (reset! server (jetty/run-jetty #'app {:port 3000 :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart-server []
  (stop-server)
  (start-server))

;; (restart-server)
