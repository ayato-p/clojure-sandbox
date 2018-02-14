(ns demo.system
  (:require [bidi.bidi :as bidi]
            [bidi.ring :as bidi.ring]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]))

(defmethod ig/init-key :app/server
  [_ opts]
  (jetty/run-jetty (:handler opts) (dissoc opts :handler)))

(defmethod ig/halt-key! :app/server
  [_ server]
  (.close server))

(defmethod ig/prep-key :app/endpoint
  [_ {:keys [routes] :as opts}]
  (->> (bidi/route-seq routes)
       (map :handler)
       (reduce (fn [m h] (assoc m h (ig/ref h))) {})
       (assoc opts :handler-fn)))

(defmethod ig/init-key :app/endpoint
  [_ {:keys [routes handler-fn]}]
  (bidi.ring/make-handler routes handler-fn))

(defmethod ig/init-key :app/database
  [_ opts])


(defmethod ig/init-key :app/handler
  [k _]
  (fn [_] k))

(derive :app.handler.home/index :app/handler)
(derive :app.handler.user/index :app/handler)

(map :handler (bidi/route-seq ["/" {"" :home
                                    "u" :user}]))


(comment
  (let [m {:app/endpoint {:routes ["/" {"" :app.handler.home/index
                                        "u" :app.handler.user/index}]}
           :app.handler.home/index {}
           :app.handler.user/index {}}]
    ((:app/endpoint (ig/init (ig/prep m))) {:uri "/" :request-method :get})))
