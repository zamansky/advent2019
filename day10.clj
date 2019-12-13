(ns day10
  (:require [clojure.string :as string]
            [utils :as u]))


(defn add-index-to-row [rowno row]
  (map-indexed (fn [i v]
                 [i rowno v]
                 )
               row
               ))

(defn keep-meteor-row [row]
  (filter #(= (get % 2) \#) row)
  )
(defn drop-char-from-row [row]
  (map (fn [ [x y c] ] [x y]) row))


;; convert a graph to a list of x,y locations
(defn parse-data [file]
  (as->
      (slurp file) f
    (string/split f #"\n")
    (map-indexed (fn [i v]
                   (add-index-to-row i v))f)
    (map keep-meteor-row f)
    (map drop-char-from-row f)
    (flatten f)
    (partition 2 2 f)
    (into [] f)
    ))

(def meteors (parse-data "day10.dat"))


(defn gcd [a b]
  (if (zero? b)
    a
    (recur b (mod a b))))


(defn calc-slope [ [x0 y0] [x1 y1] ]
  (let [den (- x0 x1)
        num (- y0 y1)
        g (u/abs (gcd num den))
        num (/ num g)
        den (/ den g)
        slope [num den]
        ]
    (if (= g 0) (println x0 y0 x1 y1 den num "GCD: " g))
    slope
    ))



(defn lines [meteor meteors]
  (let [m (filter #(not (= meteor %)) meteors)]
    [(count (into #{} (map #(calc-slope meteor %) m))) meteor]
    ))


(def base (get  (first  (reverse (sort #(compare (first %1) (first %2)) (map #(lines % meteors) meteors))))1))
;; 20 20 is the one

(def targets (filter #(not (= base %)) meteors))

(defn dist [ [x0 y0] [x1 y1] ]
  (Math/sqrt
   (+ (* (- x1 x0) (- x1 x0))
      (* (- y1 y0) (- y1 y0))
      ))
  )


(defn angle [ [x0 y0] [x1 y1]]
  (let [x (- x1 x0)
        y (- y1 y0)
        t (Math/atan2 x y)
        t (if (< t 0) (+ Math/PI (u/abs  t)) t)
        ]
    t
    )
  )

(def updated-targets (map
                      (fn [t] [(angle base t) (dist base t)    t])
                      targets))


(def z     (sort-by first updated-targets))
;; sort by slopes and dists

(def z2 (map #(first %) z))

(def a (first  (take 1 (drop 199 (reduce (fn [l i]
                                           (if  (not-any? #(< (u/abs (- i %)) 0.00001) l)  (conj l i) l ))
                                         [] z2 )))))


;; (filter (fn [x] (< (u/abs (- (first x)  a)) 0.00001)) z)

;; (filter #(= (last %) '(3 17)) z)
;; 
