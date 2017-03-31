(defproject regex-compressor "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:dev {:dependencies [[frak "0.1.6"]
                                  [criterium "0.4.4"]]}}
  :plugins [[lein-virgil "0.1.6"]]
  :java-source-paths ["src/main/java"]
  :jvm-opts ["-Xmx2g"])
