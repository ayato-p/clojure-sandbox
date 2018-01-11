(ns demo.system2
  (:require bidi.ring
            [integrant.core :as ig]))

(defonce system (atom nil))

(defmethod ig/init-key :app/database
  [_ _]
  (atom {}))

(defmethod ig/halt-key! :app/database
  [_ db]
  (reset! db nil))

(defmulti resolve-handler identity)

(defn x-handler [req]
  (let [db (:db req)]
    (swap! db update-in [:count :x] (fnil inc 0)))
  {:status 200})

(defmethod resolve-handler :app.handler/x [_] x-handler)

(defn y-handler [req]
  (let [db (:db req)]
    (swap! db update-in [:count :y] (fnil inc 0)))
  {:status 200})

(defmethod resolve-handler :app.handler/y [_] y-handler)

(defn z-handler [req]
  (let [db (:db req)]
    (swap! db update-in [:count :z] (fnil inc 0)))
  {:status 200})

(defmethod resolve-handler :app.handler/z [_] z-handler)

(def routes
  ["/" {"this-is-x" :app.handler/x
        "this" {"/is" {"/y" :app.handler/y
                       "/z" {:post :app.handler/z}}}}])

(defmethod ig/init-key :app/router
  [_ {:keys [routes resolver]}]
  (bidi.ring/make-handler routes resolver))

(defmethod ig/init-key :app/endpoint
  [_ {:keys [database router]}]
  (letfn [(wrap-db [handler]
            (fn [req]
              (handler (assoc req :db database))))]
    (wrap-db router)))

(def system-config
  {:app/database {}
   :app/router {:routes routes
                :resolver resolve-handler}
   :app/endpoint {:router (ig/ref :app/router)
                  :database (ig/ref :app/database)}})

(defn reset-system []
  (when @system
    (ig/halt! @system)
    (reset! system nil))
  (reset! system (ig/init system-config)))

(comment
  (reset-system)

  ((:app/endpoint @system) {:uri "/this-is-x"})
  ;; => {:status 200}
  @(:app/database @system)
  ;; => {:count {:x 1}}
  ((:app/endpoint @system) {:uri "/this/is/y"})
  ;; => {:status 200}
  @(:app/database @system)
  ;; => {:count {:x 1, :y 1}}
  ((:app/endpoint @system) {:uri "/this/is/z" :request-method :post})
  ;; => {:status 200}
  @(:app/database @system)
  ;; => {:count {:x 1, :y 1, :z 1}}
  ((:app/endpoint @system) {:uri "/this/is/z"})
  ;; => nil
  @(:app/database @system)
  ;; => {:count {:x 1, :y 1, :z 1}}
  )
