(ns user
  (:require [demo.config :as conf]
            [integrant.repl :refer [clear go halt init prep reset reset-all]]))

(integrant.repl/set-prep! conf/read-config)
