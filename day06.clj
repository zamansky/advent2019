(ns day06
  (:require [clojure.string :as string]
            [utils :as u]))




(def dfs-map (->>  (string/split (slurp "day06.dat") #"\n")
                   (map #(string/split % #"\)"))
                   (map (fn [ [a b] ] [(keyword a) (keyword b)]))
                   (reduce (fn [ m [k v]]
                             (assoc m k (vec  (conj (k m) v)))) {} )
                   ))

(defn part1-bfs [dfs-map node level]
  (if (not (node dfs-map))
    level
    (+ level
       (reduce +
               (for [n (node dfs-map)]
                 (+ 0  (part1-bfs dfs-map n (inc level))))))))


