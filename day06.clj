(ns day06
  (:require [clojure.string :as string]
            [utils :as u]))




(def d (->>  (string/split (slurp "day06.dat") #"\n")
             (map #(string/split % #"\)"))
             (map (fn [ [a b] ] [(keyword a) (keyword b)]))
             (reduce (fn [ m [k v]]
                       (assoc m k (vec  (conj (k m) v)))) {} )
             ))

(defn part1-bfs [d node level]
  (if (not (node d))
    level
    (+ level
       (reduce +
               (for [n (node d)]
                 (+ 0  (part1-bfs d n (inc level))))))))


