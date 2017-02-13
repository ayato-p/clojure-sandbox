(ns demo.entity.user
  (:require [demo.db :as db]
            [stch.sql :as sql]))

(defn find
  ([db query]
   (-> (sql/select :*)
       (sql/from :users)
       (->> (db/fetch db))))
  ([db] (find db {})))
