(ns demo.core
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [integrant.core :as ig]
            [rum.core :as rum]))

(enable-console-print!)

(defonce system (atom nil))

(rum/defc display-window-size < rum/reactive
  [db]
  (let [{:keys [height width]} (rum/react db)]
    [:div
     [:h1 (str "window height: " height)]
     [:h1 (str "window width: " width)]]))

(defmethod ig/init-key :demo/dom
  [_ id]
  (let [dom (dom/createDom "div" {:id id})]
    (dom/appendChild (.-body js/document)
                     dom)
    dom))

(defmethod ig/halt-key! :demo/dom
  [_ elm]
  (dom/removeNode elm))

(defn current-window-size []
  (let [width (.-innerWidth js/window)
        height (.-innerHeight js/window)]
    {:width width :height height}))

(defmethod ig/init-key :demo/event
  [_ db]
  (letfn [(handler [_]
            (reset! db (current-window-size)))]
    (events/listen js/window "resize" handler)
    handler))

(defmethod ig/halt-key! :demo/event
  [_ handler]
  (events/listen js/window "resize" handler))

(defmethod ig/init-key :demo/db
  [_ _]
  (atom (current-window-size)))

(defmethod ig/halt-key! :demo/db
  [_ db]
  (reset! db nil))

(defmethod ig/init-key :demo/app
  [_ {:keys [db elm]}]
  (rum/mount (display-window-size db) elm))

(def system-conf
  {:demo/dom "myapp"
   :demo/event (ig/ref :demo/db)
   :demo/db nil
   :demo/app {:db (ig/ref :demo/db)
              :elm (ig/ref :demo/dom)}})

(defn start []
  (reset! system (ig/init system-conf)))

(defn stop []
  (ig/halt! @system)
  (reset! system nil))

(defn on-js-reload []
  (stop)
  (start))

(events/listen js/window "load" (fn [_] (start)))
