(ns demo.system1
  (:require bidi.ring
            [integrant.core :as ig]))

(defonce system (atom nil))

(defmethod ig/init-key :app/database
  [_ _]
  (atom {}))

(defmethod ig/halt-key! :app/database
  [_ db]
  (reset! db nil))

(defmethod ig/init-key :app.handler/x
  [_ {:keys [db]}]
  (fn [req]
    (swap! db update-in [:count :x] (fnil inc 0))
    {:status 200}))

(defmethod ig/init-key :app.handler/y
  [_ {:keys [db]}]
  (fn [req]
    (swap! db update-in [:count :y] (fnil inc 0))
    {:status 200}))

(defmethod ig/init-key :app.handler/z
  [_ {:keys [db]}]
  (fn [req]
    (swap! db update-in [:count :z] (fnil inc 0))
    {:status 200}))

(defmethod ig/init-key :app/router
  [_ {:keys [routes handlers]}]
  (bidi.ring/make-handler routes handlers))

(def system-config
  {:app/database {}
   :app.handler/x {:db (ig/ref :app/database)}
   :app.handler/y {:db (ig/ref :app/database)}
   :app.handler/z {:db (ig/ref :app/database)}
   :app/router
   {:handlers
    {:app.handler/x (ig/ref :app.handler/x)
     :app.handler/y (ig/ref :app.handler/y)
     :app.handler/z (ig/ref :app.handler/z)}
    :routes
    ["/" {"this-is-x" :app.handler/x
          "this" {"/is" {"/y" :app.handler/y
                         "/z" {:post :app.handler/z}}}}]}})

(defn reset-system []
  (when @system
    (ig/halt! @system)
    (reset! system nil))
  (reset! system (ig/init system-config)))

(comment
  (reset-system)

  ((:app/router @system) {:uri "/this-is-x"})
  ;; => {:status 200}
  @(:app/database @system)
  ;; => {:count {:x 1}}
  ((:app/router @system) {:uri "/this/is/y"})
  ;; => {:status 200}
  @(:app/database @system)
  ;; => {:count {:x 1, :y 1}}
  ((:app/router @system) {:uri "/this/is/z" :request-method :post})
  ;; => {:status 200}
  @(:app/database @system)
  ;; => {:count {:x 1, :y 1, :z 1}}
  ((:app/router @system) {:uri "/this/is/z"})
  ;; => nil
  @(:app/database @system)
  ;; => {:count {:x 1, :y 1, :z 1}}
  )
