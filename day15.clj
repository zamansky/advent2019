(ns day15
  (:require [clojure.string :as string]
            [utils :as u]
            [intcode :as ic]
            ))

(def source (ic/load-source "day15.dat"))
(def payload {:program source :current 0 :output [] :input nil})




(defn get-next-positions [[x y] payload visited]
  (let [step (:step payload)
        moves  []
        north (ic/run-code (assoc payload :input 1 :output nil))
        moves (if (not (= (first (:output north)) 0))
                (conj moves (assoc north :pos [x (dec y)] :step (inc step) )) moves)
        
        south (ic/run-code (assoc payload :input 2 :output nil ))
        moves (if (not (= (first (:output south)) 0))
                (conj moves (assoc south :pos [x (inc y)] :step (inc step)  )) moves)

        west (ic/run-code (assoc payload :input 3 :output nil ))
        moves (if (not (= (first (:output west)) 0))
                (conj moves (assoc west :pos [(inc x) y] :step (inc step))) moves)

        east (ic/run-code (assoc payload :input 4 :output nil ))
        moves (if (not (= (first (:output east)) 0))
                (conj moves (assoc east :pos [(dec x) y] :step (inc step))) moves)

        moves (filter (fn [x] (not (contains? visited (:pos x)))) moves)
        ]
    
    moves
    ))


(defn solve1 [q visited]
  (let [f (first q)
        remaining (rest q)
        pos (:pos f)
        visited (conj visited pos)
        next-cells  (get-next-positions pos f visited)
        next-iter (into remaining next-cells)
        ]
    ;;(println pos (:output f) (:step f) (count next-iter) )
    ;;(println (empty? next-iter))
    (cond
      (= (first (:output f)) 2) [(:pos f)(:step f)]
      :else (recur next-iter  visited)
      )

    )
  )

(defn part1 [payload]
  (solve1 [(assoc payload :pos [0 0] :step 0)] #{})
  )




(defn make-map [q visited themap]
  (let [f (first q)
        remaining (rest q)
        pos (:pos f)
        themap (if (not (contains? themap pos))
                 (assoc themap pos (:step f))
                 themap
                 )
        visited (conj visited pos)
        

        next-cells  (get-next-positions pos f visited)
        next-iter (into remaining next-cells)
        ]
    ;;(println pos (:step f))
    ;;(println pos (:output f) (:step f) (count next-iter) )
    (cond
      (empty? next-iter) themap
      :else (recur next-iter  visited themap)
      )

    )
  )

(defn bfs-nexts [f visited themap step]
  (let [deltas [ [0 -1] [0 1] [1 0] [-1 0]]
        ns (mapv (fn [x]  (mapv + x  (:pos f)))
                 deltas)
        ns (into [] (filter #(and
                              (contains? themap %)
                              (not (contains? visited %))) ns))
        ns (mapv (fn [x] { :pos x :step (inc step)} )  ns)
        ]
    
    ns ))


(defn bfs [q visited themap]
  (let [f (first q)
        pos (:pos f)
        step (:step f)
        themap (if (contains? visited pos)
                 themap
                 ( assoc themap pos step)
                 )
        remaining (rest q)
        visited (conj visited pos)
        next-cells  (bfs-nexts f visited themap step)
        next-iter (into remaining next-cells)
        ]
    
    (cond
      (empty? next-iter) themap
      :else (recur next-iter  visited themap)
      )))

(defn part2 [payload]
  (let [themap (make-map [(assoc payload :pos [0 0] :step 0)] #{} {}) ;; like part 1
        themap (into {}  (map (fn [x] [(first x) 0])) themap) ;; make  a dict with pos as key and steps as value
        themap (bfs [{:pos [16 14] :step 0}] #{} themap) ;; do bfs which will add steps to the map
        ans (apply max (map #(second %) themap)) ;; find the max step
        ]

    ans
    ))


