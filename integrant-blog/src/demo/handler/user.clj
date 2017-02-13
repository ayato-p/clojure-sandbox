(ns demo.handler.user
  (:require [demo.entity.user :as eu]
            [demo.view.user :as view]
            [ring.util.response :as res]))

(defn index [{:keys [$db] :as req}]
  (let [users (eu/find $db)]
    (-> (view/index users)
        res/response)))

(defn new [req]
  (res/response))

(defn create [req])
