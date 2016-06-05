(ns demo.component.middleware-aggregator
  (:require [com.stuartsierra.component :as c]
            [com.stuartsierra.dependency :as dep]
            [demo.component.middleware.proto :as p]))

(defn- build-deps-graph [deps-map]
  (reduce (fn [graph [id anc type]]
            (case type
              :before (dep/depend graph anc id)
              :after (dep/depend graph id anc)))
          (dep/graph)
          (for [[id v] deps-map
                [anc type] v]
            [id anc type])))

(defrecord MiddlewareAggregator [middlewares dependency-map]
  c/Lifecycle
  (start [this]
    (println "Start middleware")
    this)
  (stop [this]
    (println "Stop middleware")
    this)

  p/IMiddleware
  (wrap [this handler]
    (-> (build-deps-graph dependency-map)
        dep/topo-comparator
        (sort (keys middlewares))
        (->> (reduce (fn [f k]
                       (println "Applying middleware:" k)
                       (p/wrap (get middlewares k) f))
                     handler)))))
