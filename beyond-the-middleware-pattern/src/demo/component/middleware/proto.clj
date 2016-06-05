(ns demo.component.middleware.proto)

(defprotocol IMiddleware
  (wrap [this handler]))

(extend-protocol IMiddleware
  clojure.lang.IFn
  (wrap [this handler]
    (this handler)))
