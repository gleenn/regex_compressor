(ns graphviz
  (:require [clojure.java.io :as io])
  (:import com.gleenn.regex_compressor.Trie))

(defn words-trie []
  (let [words (-> (io/file "/usr/share/dict/words") io/reader line-seq)
        trie (Trie.)]
    (doseq [word (take 1000 words)]
      (.addWord trie word))
    trie))

(def id #(System/identityHashCode %))

(defn prn-recursively [trie]
  (println (format "  %d [label=\"%s\" shape=circle];"
                   (id trie)
                   (or (.getCharacter trie) "root")))
  (when (.isTerminal trie)
    (println (format "  %d. [shape=point]" (id trie)))
    (println (format "  %d->%d. [shape=point]" (id trie) (id trie))))
  (doseq [child (map #(.getValue %) (.entrySet (.getChildren trie)))]
    (println (format "  %d->%d;" (id trie) (id child)))
    (prn-recursively child)))

(defn prn-trie [trie]
  (println "digraph \"\"")
  (println "{")
  (prn-recursively trie)
  (println "}"))
