(defproject demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring "2.0.0-alpha1"]
                 [ataraxy "0.4.3"]
                 [clj-http "3.12.3"]
                 [cheshire "5.11.0"]
                 [audiogum/clj-lazy-json "0.0.3"]
                 [org.clojure/core.async "1.6.673"]]
  :main ^:skip-aot demo.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
