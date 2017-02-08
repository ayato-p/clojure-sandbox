(defproject demo "0.1.0-SNAPSHOT"
  :description "building blog app with integrant"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 ;; lifecycle
                 [integrant "0.2.0"]

                 ;; server
                 [org.immutant/web "2.1.6"]
                 [ring/ring-core "1.5.1"]

                 ;; ring middleware
                 [ring/ring-defaults "0.2.1"]

                 ;; routing
                 [bidi "2.0.16"]

                 ;; database
                 [org.clojure/java.jdbc "0.7.0-alpha1"]
                 [org.postgresql/postgresql "9.4.1212"]
                 [ragtime "0.6.3"]

                 ;; sql
                 [stch-library/sql "0.1.1"]

                 ;; config
                 [rkworks/baum "0.4.0"]

                 ;; webjars
                 [org.webjars/bootstrap "3.3.7-1"]]

  :profiles
  {:dev {:dependencies []
         :repl-options {:init-ns demo.repl}}
   :uberjar {:aot :all
             :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
