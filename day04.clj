(ns day04
  (:require [clojure.string :as string]
            [utils :as u]))

(count  (re-seq #"(\d)\1" "121234"))


(defn is-increasing? [n]
  (every? (fn [ [a b] ] (<= (int a) (int b))) (partition 2 1 n) )
  )


(defn part2-isvalidpassword[p]
  (let [s (str p)
        cleaned (string/replace s #"([0-9])\1\1+" "")
        has_doubles (re-seq #"(\d)\1" cleaned)
        ]
    has_doubles))


(defn part2 [low high]
  (let [passwords (map str (range low (inc high)))
        onlyinc (filter is-increasing? passwords)
        pws (filter part2-isvalidpassword onlyinc)
        ]
    (count pws)))


(defn part1 [low high]
  (let [passwords (map str  (range low (inc high)))
        has_doubles (filter #(re-seq #"(\d)\1" %) passwords)
        p (map #(partition 2 1 %) has_doubles)
        ans (map #(every? (fn [ [a b]] (<= (int a) (int b))) %) p)
        ans (filter true? ans)
        ]
    (count  ans)
    ))


(defn part1-threading [low high]
  (->> (range low (inc high))
       (map str)
       (filter #(re-seq #"(\d)\1" %))
       (map #(partition 2 1 %))
       (map #(every? (fn [ [a b]] (<= (int a) (int b))) %) )
       (filter true? )
       
       count
       )
  )


(defn part2-threading [low high ]
  (->> (range low (inc high))
       (map str)
       (filter is-increasing?)
       (filter part2-isvalidpassword)
       count))

;; (time  (part1 372304 847060))
;; (time  (part2 372304 847060))
;; (time  (part1-threading  372304 847060))
;; (time  (part2-threading 372304 847060))
