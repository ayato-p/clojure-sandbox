(defproject demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [integrant "0.7.0-alpha1"]
                 [ragtime "0.7.2"]
                 [rkworks/baum "0.4.0"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [org.postgresql/postgresql "42.2.2"]
                 [hikari-cp "2.2.0"]
                 [camel-snake-kebab "0.4.0"]
                 [potemkin "0.4.5"]]

  :profiles
  {:dev {:source-paths ["env/dev/src" "src"]
         :dependencies [[integrant/repl "0.3.0"]
                        [fudje "0.9.7"]
                        [zcaudate/hara "2.8.2"]]}})
