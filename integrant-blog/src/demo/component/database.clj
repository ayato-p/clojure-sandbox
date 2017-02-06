(ns demo.component.database
  (:require [integrant.core :as ig]))

(defmethod ig/init-key ::database [_ opts]
  opts)

(defmethod ig/halt-key! ::database [_ _]
  nil)
