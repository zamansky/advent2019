(ns day06
  (:require [clojure.string :as string]
            [utils :as u]))



;; build an edge list for a depth first search
(def dfs-map (->>  (string/split (slurp "day06.dat") #"\n")
                   (map #(string/split % #"\)"))
                   (map (fn [ [a b] ] [(keyword a) (keyword b)]))
                   (reduce (fn [ m [k v]]
                             (assoc m k (vec  (conj (k m) v)))) {} )
                   ))

;; dfs but keep track of the level.
(defn part1-bfs [dfs-map node level]
  (if (not (node dfs-map))
    level
    (+ level
       (reduce +
               (for [n (node dfs-map)]
                 (+ 0  (part1-bfs dfs-map n (inc level))))))))


;; Find the path from the root to a node
(defn find-target [dfs-map node target path]
  (cond (= node target) path
        (not (node dfs-map)) nil
        :else (let [tmp (for [n (node dfs-map)]
                          (find-target dfs-map n target (conj path n)))
                    paths  (flatten tmp) ;;(reduce conj tmp)
                    ans (take-while #(not ( nil? %) ) (drop-while nil? paths))
                    ]
                ans
                )))

(defn part2 [dfs-map]
  (let [COM-to-SAN (find-target dfs-map :COM :SAN []) ;; find the paths from the real root to each node
        COM-to-YOU (find-target dfs-map :COM :YOU [])
        ;; find the common root 
        [root-san root-you] (first (drop-while (fn [ [a b] ] (= a b)) (map vector COM-to-SAN COM-to-YOU)))
        ;; drop the common path to the common root
        com-path (drop-while (fn [x] (not (= x root-san))) COM-to-SAN)
        you-path (drop-while (fn [x] (not (= x root-you))) COM-to-YOU)
        ;; the two lengths -2 (for the nodes themselves are the answer)
        ans (- (+ (count com-path) (count you-path)) 2)
        ]
    ans))

