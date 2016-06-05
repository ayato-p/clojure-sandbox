(ns demo.component.middleware-aggregator
  (:require [com.stuartsierra.dependency :as dep]
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
  p/IMiddleware
  (wrap [this handler]
    (let [ms (reduce (fn [ms [k v]]
                       (if (satisfies? p/IMiddleware v)
                         (assoc ms k v)
                         ms))
                     (or middlewares {})
                     this)]
      (-> (build-deps-graph dependency-map)
          dep/topo-comparator
          (sort (keys ms))
          (->> (reduce (fn [f k]
                         (println "Applying middleware:" k)
                         (p/wrap (get ms k) f))
                       handler))))))
