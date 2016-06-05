(ns demo.component.middleware.company-info
  (:require [demo.component.middleware.proto :as p]))

(defn- find-company [db]
  (prn db)
  {:name "Company A"
   :description "This is IT company."})

(defrecord CompanyInfo [db]
  p/IMiddleware
  (wrap [this handler]
    (fn [req]
      (let [company (find-company db)]
        (handler (assoc req :company-info company))))))
