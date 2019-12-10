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

(def test (first meteors))
(def others (rest meteors))

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

(reverse (sort #(compare (first %1) (first %2)) (map #(lines % meteors) meteors)))
(def z ( map #(lines % meteors) meteors))
q
