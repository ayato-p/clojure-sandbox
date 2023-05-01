(ns app.core
  (:require [clj-http.client :as http]
            [ring.adapter.jetty :as jetty]))


(defn start-server []
  (let [f (fn [req]
            (Thread/sleep 5000)
            {:status 200})]
    (jetty/run-jetty f {:port 3000 :join? false})))

(comment
  (start-server)
  (http/get "http://localhost:3000"
            {:async true}
            (partial println :got) (partial println :err))

  (let [result (reify clojure.lang.IDeref
                 (deref [_]
                   (let [x (atom {:status :started})]
                     (http/get "http://localhost:3000"
                               {:async   true 
                                :throw-exceptions false
                                ;; :socket-timeout 1000
                                :connection-timeout 10000}
                               (fn [response]
                                 (swap! x assoc :status :completed :response response))
                               (fn [err]
                                 (swap! x assoc :status :failed :error err)))
                     (loop [{:keys [status response error]} @x]
                       (case status
                         :started
                         (do
                           (Thread/sleep 500)
                           (println "sleeping...")
                           (recur @x))
                         :completed
                         response
                         :failed
                         error)))))]
    @result)
  ;;--
  )