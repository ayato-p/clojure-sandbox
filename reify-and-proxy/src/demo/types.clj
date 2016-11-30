(ns demo.types)

(gen-class
 :name demo.types.MyHello
 :main false
 :init init
 :state state
 :methods [[hello [] String]])

(defn -init [] [[] nil])

(defn -hello [this] "Hello")

(compile 'demo.types)
