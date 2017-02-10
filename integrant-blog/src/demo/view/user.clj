(ns demo.view.user
  (:require [hiccup2.core :as h]))

(defn- user-list [users]
  [:table
   `[:tbody
     ~@(for [{:keys [username]} users]
         [:tr
          [:td username]])]])

(defn index [users]
  (-> `([:h1 "ユーザーの一覧"]
        ~(user-list users))
      h/html) )
