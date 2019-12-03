(ns day03
  (:require [clojure.string :as string]
            [utils :as u]))

(def dirs {:U [0 1] :D [0 -1] :L [-1 0] :R [1 0]})

(def  testlines ["R8,U5,L5,D3"
                 "U7,R6,D4,L4"])

(def data (string/split (slurp "day03.dat") #"\n"))

(defn parse-line
  "Convert a line into a list of kv pairs of dir and distance"
  [l]
  (let [l (string/split l #"," )
        l (map  #(re-matches #"(U|D|L|R)([0-9]+)" %) l)
        l (map #(drop 1 %) l)
        l (map (fn [ [k d]] [ (keyword k) (u/parse-int d)] )l)
        ]
    l))


(defn add-to-map
  "Add all the points for a specific instruction to the map"
  [m  point [dir dist] id ]
  ;; calculate all the new points for this run
  (let [newpoints  (for [d (range  1  (+ dist 1)  ) ]
                     (let [tp (mapv #(* d %) (dir dirs))]
                       (mapv + point tp))
                     )
        ]
    ;; Add them to the map 
    (reduce (fn [m val]
              (if (and  (get m val) (not (= (get m val) [0 0])))
                (update m val conj id )
                ( assoc m val #{id} :point val))

              ) m newpoints)))


(defn build-map
  "Add a complete line of instructions to the map"
  [map line id]
  (reduce (fn [map val]
            (add-to-map map (:point map) val id)
            ) (assoc map :point [0 0]) line))


(defn solve [input]
  (let [l1 (build-map {} (parse-line (input 0)) 0)
        l2 (build-map l1 (parse-line (input 1)) 1)
        ans (first (sort  (map (fn [e] (+ (u/abs ((e 0) 0)) (u/abs ((e 0) 1))))
                               (filter (fn [e] (= (count (e 1)) 2)) (dissoc l2 :point)))))
        ]
    ans
    ))

(solve data)

