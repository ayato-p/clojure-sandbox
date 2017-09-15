(defproject demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.postgresql/postgresql "42.1.4"]
                 [org.clojure/java.jdbc "0.7.1"]]

  :profiles
  {:plugins/eftest
   {:plugins [[lein-eftest "0.3.1"]]}}

  :aliases
  {"eftest" ["with-profile" "+plugins/eftest" "eftest"]})
