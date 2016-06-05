(ns demo.component.middleware.proto)

(defprotocol IMiddleware
  (wrap [this handler]))

(extend-protocol IMiddleware
  clojure.lang.Fn
  (wrap [this handler]
    (this handler)))
