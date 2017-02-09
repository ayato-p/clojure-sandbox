(ns demo.response
  (:require [ring.util.response :as res]))

(defn html [res]
  (res/content-type res "text/html; charset=utf-8"))
