(defproject demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [integrant "0.7.0-alpha1"]
                 [rkworks/baum "0.4.0"]
                 [ring "1.6.3"]
                 [bidi "2.1.3"]]
  :profiles
  {:dev {:source-paths ["env/dev/src" "src"]
         :dependencies [[integrant/repl "0.3.0"]
                        [com.gearswithingears/shrubbery "0.4.1"]]}})
