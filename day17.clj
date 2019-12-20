(ns day17
  (:require [clojure.string :as string]
            [utils :as u]
            [intcode :as ic]
            ))

(def source (ic/load-source "day17.dat"))
(def payload {:program source :current 0 :output [] :input 0 :base 0})

(def state (ic/run-code payload))
(def raw-board (:output state))


(defn make-board [board]
  (loop [row 0 col 0 current (first board) remaining (rest  board) map {}]
    (cond
      (nil? current) map
      (and  (not= current 46)
            (not= current 10)) (recur row (inc col) (first remaining) (rest remaining)  (assoc map [row col] {:p current :h nil :v nil :int nil}))
      (= current 10)    (recur (inc row) 0 (first remaining) (rest remaining)  map)
      (= current 46) (recur row (inc col) (first remaining) (rest remaining) map)
      (empty? remaining) map
      :else map
      )))
(def maze (make-board raw-board))

(defn find-intersections [raw-maze]
  (reduce (fn [maze current]
            (let [ pos (first  current)
                  [ row col ]  pos
                  up (get maze [ (dec row) col])
                  down (get maze [ (inc row) col])
                  left (get maze [ row (dec  col)])
                  right (get maze [ row (inc  col)])
                  h (not (nil? (and left right)))
                  v (not (nil?  (and up down)))
                  intersects (and h v)
                  ]
              (assoc maze pos {:current pos :h h :v v :int intersects})
              )
            )
          raw-maze raw-maze
          ))

(def intersects (find-intersections maze))

(def part1-ans (->> intersects
                    (filter #(= (:int (second %)) true))
                    (map #(:current (second %)))
                    (map #(apply * %))
                    (reduce +)
                    ))


;; (def p (map char raw-board))

;; (def pp (apply str p))
;; (println pp)

;;R,12,L,10,L,10,L,6,L,12,R,12,L,4
;;R,12,L10,L10,L6,L,12,R,12
;;L,4,L,12,R,12,L,6,L,6,L,12
;;R,12,L4,L,12
;;R,12,L,6,R,12,L,10
;;L,10,L,12,R,12,L,6
;;L,12,R,12,L,6


(def inputs[65 44 66 44 65 44 66 44 67 44 66 44 67 44 65 44 67 44 67 10
            82 44 49 50  44 76 44 49 48 44 76 44 49 48 10
            76 44 54 44 76 44 49 50 44 82 44 49 50  44 76 44 52 10
            76 44 49 50  44 82 44 49  50  44 76 44 54 10
            110 10
            ]
  )

(def i2 [65 44 67 44 65 44 67 44 66 44 67 44 66 44 65 44 66 44 66 10
         82 44 49 50  44 76 44 49 48 44 76 44 49 48  10
         76 44 49 50  44 82 44 49  50  44 76 44 54 10
         76 44 54 44 76 44 49 50 44 82 44 49 50  44 76 44 52 10
         118 10
         ]
  )

(def payload {:program (assoc source 0 2) :current 0 :base 0  :output []})

(defn part2 [payload inputs]
  (loop [payload payload next-input (first inputs) inputs (rest inputs)]
    ;;(prn (:status payload) next-input)
    (let [npayload (ic/run-code payload)
          output (:output npayload)]
      ;;(prn (:status npayload) next-input)

      (cond
        (=  (:status npayload) :needs-input) (recur (assoc npayload :input next-input :output output) (first inputs) (rest inputs))
        (=  (:status npayload) :exit) (:output npayload)
        :else (recur (assoc npayload :output []) next-input inputs)
        ))))


