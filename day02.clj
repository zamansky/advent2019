(ns day02
  (:require [clojure.string :as string]
            [utils :as u]))



;;(def raw_data (slurp "day02-sample.dat"))
(def raw_data (slurp "day02.dat"))
(def data  (->> (string/split raw_data #",")
                (map  u/parse-int)
                (into [])))

(defn do-op [program current]
  (let [op (get program current)
        a (get program  (get program (+ current 1)))
        b (get program  (get program (+ current 2)))
        c (get program (+ current 3))
        result (if (= op 1)
                 (+ a b)
                 (* a b))]
    (assoc program c result )
    ))

(defn process-part1 [program current]  
  (let [op (get program current)]
    (cond
      (= op 99) program
      :else (recur (do-op program current) (+ current 4))
      )
    ))

(defn process-part2 [program]
  (for [noun (range 100)
        verb (range 100)]
    (let [program (-> program
                      (assoc 1 noun)
                      (assoc 2 verb))]
      [(first (process-part1 program 0)) (+ (* 100 noun) verb)])
    ))

(defn part1[data]
  (let [program (-> data
                    (assoc 1 12)
                    (assoc 2 2))]
    (first  (process-part1 program 0))
    ))


(defn part2[data]
  (let [sols ( process-part2 data)
        ans (filter #(= (first %) 19690720) sols)
        ]
    ans
    )
  )
