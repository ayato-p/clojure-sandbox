(defproject demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [integrant "0.2.0"]
                 [org.immutant/web "2.1.6"]
                 [ring/ring-core "1.5.1"]
                 [bidi "2.0.16"]
                 [ring/ring-defaults "0.2.1"]

                 ;; database
                 [org.clojure/java.jdbc "0.7.0-alpha1"]
                 [org.postgresql/postgresql "9.4.1212"]
                 [ragtime "0.6.3"]

                 ;; config
                 [rkworks/baum "0.4.0"]]

  :profiles
  {:dev {:dependencies []
         :repl-options {:init-ns demo.repl}}
   :uberjar {:aot :all
             :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
