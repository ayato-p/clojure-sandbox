(ns user
  (:require [demo.config :as conf]
            demo.system
            [integrant.core :as ig]
            [integrant.repl :refer [clear go halt init prep reset reset-all]]
            [integrant.repl.state :as state]
            [ragtime.repl :refer [migrate rollback]]))

(alter-var-root #'integrant.repl/init-system
                (fn [_]
                  (fn init-system [config]
                    (#'integrant.repl/build-system
                     #(ig/init (ig/prep config))
                     #(ex-info "Config failed to init; also failed to halt failed system"
                               {:init-exception %1}
                               %2)))))

(integrant.repl/set-prep! conf/read-config)
