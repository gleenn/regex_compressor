(use 'criterium.core)
(require '[clojure.java.io :as io])
(require 'frak)
(import com.gleenn.regex_compressor.RegexCompressor)

;(def ud-words (-> (io/file "/tmp/less-words.csv") io/reader line-seq))
(def words (-> (io/file "/usr/share/dict/words") io/reader line-seq))
(defn naive-pattern
      "Create a naive regular expression pattern for matching every string
       in strs."
      [strs]
      (->> strs
           (clojure.string/join "|")
           (format "(?:%s)")
           re-pattern))

;; Shuffle 10000 words and build a naive and frak pattern from them.
(def ws (shuffle (take 10000 words)))

(prn ws)


(prn "n-pat")
(with-progress-reporting (bench (def n-pat (naive-pattern ws)) :verbose))

(prn "f-pat")
(with-progress-reporting (bench (def f-pat (frak/pattern ws)) :verbose))

(prn "g-pat")
(with-progress-reporting (bench (def g-pat (re-pattern (com.gleenn.regex_compressor.RegexCompressor/compress ws))) :verbose))

;; Verify the naive pattern matches everything it was constructed from.
(every? #(re-matches n-pat %) ws)
(every? #(re-matches f-pat %) ws)
(every? #(re-matches g-pat %) ws)
;; => true

;; Shuffle the words again since the naive pattern is built in the
;; same order as it's inputs.
;(def ws' (shuffle ws))


;(bench (doseq [w ws'] (re-matches n-pat w)))
;;;             Execution time mean : 1.499489 sec
;;;    Execution time std-deviation : 181.365166 ms
;;;   Execution time lower quantile : 1.337817 sec ( 2.5%)
;;;   Execution time upper quantile : 1.828733 sec (97.5%)
;
;;; frak pattern
;
;(bench (doseq [w ws'] (re-matches f-pat w)))
;;;             Execution time mean : 155.515855 ms
;;;    Execution time std-deviation : 5.663346 ms
;;;   Execution time lower quantile : 148.168855 ms ( 2.5%)
;;;   Execution time upper quantile : 164.164294 ms (97.5%)
;
;(bench (doseq [w ws'] (re-matches g-pat w)))
;
