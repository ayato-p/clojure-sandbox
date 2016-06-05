(ns demo.system
  (:require [com.stuartsierra.component :as c]
            [demo.component.endpoint :as e]
            [demo.component.middleware-aggregator :as m]
            [demo.component.server :as s]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.nested-params :refer [wrap-nested-params]]
            [ring.middleware.params :refer [wrap-params]]))

(defn demo-system [conf]
  (-> (c/system-map :middleware/middlewares {:params wrap-params
                                             :nested-params wrap-nested-params
                                             :keyword-params wrap-keyword-params}
                    :middleware/dependency-map {:nested-params {:params :before}
                                                :keyword-params {:nested-params :before}}
                    :middleware (m/map->MiddlewareAggregator {})
                    :endpoint (e/map->Endpoint {})
                    :server (s/map->WebServer {:host "localhost" :port 3000}))
      (c/system-using
       {:middleware {:middlewares    :middleware/middlewares
                     :dependency-map :middleware/dependency-map}
        :endpoint   [:middleware]
        :server     [:endpoint]})))
