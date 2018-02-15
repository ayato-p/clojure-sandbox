(defproject demo "0.1.0-SNAPSHOT"
  :pedantic? :warn
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :exclusions [org.clojure/clojure]
  :dependencies [[bidi "2.1.3" :exclusions [ring/ring-core]]
                 [com.draines/postal "2.0.2"]
                 [hiccup "2.0.0-alpha1"]
                 [honeysql "0.9.1"]
                 [integrant "0.7.0-alpha1"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.postgresql/postgresql "42.2.1"]
                 [ragtime "0.7.2"]
                 [ring "1.6.3"]
                 [ring/ring-defaults "0.3.1"]
                 [rkworks/baum "0.4.0"]]
  :profiles
  {:dev {:source-paths ["env/dev/src" "src"]
         :dependencies [[com.gearswithingears/shrubbery "0.4.1"]
                        [integrant/repl "0.3.0"]
                        [fudje "0.9.7"]]}})
