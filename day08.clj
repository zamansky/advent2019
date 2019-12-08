(ns day08
  (:require [clojure.string :as string]
            [utils :as u]))


(defn count-num [val nums]
  (->> nums
       (filter #(= % val))
       count
       )
  )


(def width 25)
(def height 6)
(def size (* width height))


(def raw-data (->> (slurp "day08.dat")
                   (map u/char->int)
                   (partition size)
                   ))

(defn part1 [data]
  (let [image (->> data 
                   (apply min-key #(count-num 0 %)))
        ones (->> image
                  (count-num 1 ))
        twos (->> image
                  (count-num 2 ))
        ]
    (* ones twos)
    ))


(defn transpose [m]
  (apply mapv vector m))

(defn find-pixel [r]
  (first (drop-while #(= 2 %) r )))

(defn part2 [data]
  (let [image (transpose data)
        colors (map find-pixel image)
        ]
    (doseq [ line (partition width (map #(if (= 0 %) " " "X") colors))]
      (println (apply str  line)))
    ))

(part1 raw-data)
(part2 raw-data)
