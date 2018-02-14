(ns demo.handler.user
  (:require [demo.sql.user :as sql.user]
            [integrant.core :as ig]
            [ring.util.response :as res]
            [clojure.string :as str]))

(defmethod ig/init-key :app.handler.user/index
  [_ {:keys [db]}]
  (fn user-index [req]
    (->> (for [u (sql.user/find-all db)]
           (str (:fullname u) "\t" (:email u)))
         (str/join "\n")
         res/response)))
