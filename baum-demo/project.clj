(defproject demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["cstap-repo" "https://cstap-repo.s3.amazonaws.com/"]
                 ["zou-repo" "https://s3.amazonaws.com/zou-repo"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [rkworks/baum "0.4.0"]
                 [cstap/fu "0.1.1-SNAPSHOT"]])
