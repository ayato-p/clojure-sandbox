(ns demo.core
  (:gen-class)
  (:require [ataraxy.core :as route]
            [cheshire.core :as c]
            [clojure.core.async :as async]
            [clojure.core.async.impl.channels]
            [clojure.java.io :as io]
            [clj-http.client :as http]
            [clj-lazy-json.core :as json]
            [ring.adapter.jetty :as server]
            [ring.middleware.params])
  (:import [clojure.core.async.impl.channels ManyToManyChannel]))

(defonce server
  (atom nil))

(defn start-server [handler]
  (when-not @server
    (reset! server (server/run-jetty handler {:join? false :async? true :port 3000}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(extend-type ManyToManyChannel
  ring.core.protocols/StreamableResponseBody
  (write-body-to-stream [channel response output-stream]
    (async/go (with-open [writer (io/writer output-stream)]
                (loop []
                  (when-let [msg (async/<! channel)]
                    (doto writer (.write msg) (.flush))
                    (recur)))))))

(defn company-name-generator []
  (->> #(rand-nth (range (int \A) (inc (int \Z))))
       repeatedly
       (take 10)
       (map char)
       (apply str)))

(defn company-generator []
  {:id (random-uuid)
   :name (company-name-generator)})

(defn companies-handler [{[_ limit] :ataraxy/result} respond raise]
  (let [ch (async/chan)]
    (respond {:status 200 :headers {"content/type" "application/json"} :body ch})
    (async/go
      (async/>! ch "[")
      (loop [[a & [b :as rest]] (take limit (repeatedly company-generator))]
        (prn a)
        (when a
          (async/>! ch (c/encode a))
          (when b (async/>! ch ","))
          (async/<! (async/timeout 2000))
          (recur rest)))
      (async/>! ch "]")
      (async/close! ch))))

(defn driver []
  (let [c (async/chan)
        response (http/get "http://localhost:3000/companies?limit=10"
                           {:as :reader})]
    (future
      (with-open [rdr (:body response)]
        (json/process-json
         (json/parse rdr)
         {}
         [:$ :*] #(async/go (async/>! c %2))))
      (async/close! c))
    c))

(def app
  (route/handler
   {:routes '{["/companies" #{limit}] [::companies ^int limit]}
    :handlers {::companies companies-handler}}))

(comment
  (start-server (ring.middleware.params/wrap-params app))
  (stop-server)
  ;;
  (let [c (driver)]
    (async/go-loop []
      (when-let [x (async/<! c)]
        (prn x)
        (recur))))
  ;;
  )

(defn -main
  [& args])

