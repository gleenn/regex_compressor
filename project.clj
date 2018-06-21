(defproject regex-compressor "0.1.0-SNAPSHOT"
  :description "Compresses regexes!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:dev        {:dependencies [[frak "0.1.6"]
                                         [criterium "0.4.4"]
                                         [junit/junit "4.12"]
                                         [org.hamcrest/hamcrest-all "1.3"]
                                         [org.testng/testng "RELEASE"]]}
             }
  :plugins [[lein-virgil "0.1.6"]
            [lein-junit "1.1.8"]]
  :java-source-paths ["src/main/java" "src/test/java"]
  :junit ["src/test/java"]
  :jvm-opts ["-Xmx2g"])
