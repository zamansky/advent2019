(ns day01
  (:require [clojure.string :as string]
            [utils :as u]))


(def test-masses [12 14 1969 100756])

;; load data
(def masses (->>
             (slurp "day01.dat")
             string/split-lines
             (map u/parse-int)
             ))


(defn calc-mass-part1 [mass]
  (- (quot mass 3) 2))

(defn part1 [masses]
  "calculate fuel for each mass then sum them all"
  (reduce + (map calc-mass-part1 masses)))

(defn calc-mass-part2 [mass cost]
  "Same as part 1 but loop to calculate fuel for the fuel"
  (let [fuel (calc-mass-part1 mass)]
    (if (<= fuel 0)
      cost
      (recur fuel (+ cost fuel)))))

(defn part2 [masses]
  (reduce + (map #(calc-mass-part2 % 0) masses )))

(defn main []
  (println (str "Part1: " (part1 masses)))
  (println (str "Part2: " (part2 masses)))
  )

(main)
