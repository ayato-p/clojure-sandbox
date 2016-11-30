(defproject demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.4.0"]
                 [org.immutant/web "2.1.4"]]

  :profiles
  {:uberjar {:aot :all
             :uberjar-name "demo.jar"}}
  :main demo.core)
