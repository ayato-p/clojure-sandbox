(ns demo.core
  (:require [clojure.core.async :as a :refer [go go-loop]]))

(defn partition-step
  ([n]
   (partition-step n 1))
  ([n step]
   (fn [rf]
     (let [a (java.util.ArrayList. n)]
       (fn
         ([] (rf))
         ([result]
          (let [result (if (.isEmpty a)
                         result
                         (let [v (vec (.toArray a))]
                           (.clear a)
                           (unreduced (rf result v))))]
            (rf result)))
         ([result input]
          (.add a input)
          (if (= (.size a) n)
            (let [v (vec (.toArray a))]
              (dotimes [_ step]
                (.remove a 0))
              (rf result v))
            result)))))))

(defonce combo-ch (a/chan 1 (comp
                             (map #(* % %))
                             (filter even?)
                             (partition-step 9 2))))

(let [status (atom true)]
  (go
    (while @status
      (let [l (a/<! combo-ch)]
        (println l)
        (when-not l
          (reset! status false))))))

(dotimes [i 100]
  (a/put! combo-ch i))

(comment
  (a/close! combo-ch))
