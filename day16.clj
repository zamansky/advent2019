(ns day16
  (:require [clojure.string :as string]
            [utils :as u]
            [intcode :as ic]
            [hashp.core :as h]
            ))

(def base-pattern [0 1 0 -1])


(def test-num (->>
               (into []"12345678")
               (mapv  u/char->int)
               ))

(def test-num2 (->>
                (into [] "80871224585914546619083218645595")
                (mapv  u/char->int)
                ))

(def test-num3 (->>
                (into [] "19617804207202209144916044189917")
                (mapv  u/char->int)
                ))

(def test-num4 (->>
                (into [] "69317163492948606335995924319873")
                (mapv  u/char->int)
                ))

(def input (->> (into [] (slurp "day16.dat"))
                (mapv  u/char->int)
                ))
(def biginput (repeat 650 input))
(defn make-pattern [base-pattern digit]
  (let [p (->> base-pattern
               (mapv #(repeat digit %))
               flatten
               vec  )
        ]p))



(defn pattern-vector [input pattern digit]
  (let [pattern (make-pattern pattern digit)
        start (vec  (rest pattern))
        addon (vec  (take (count input) (repeat pattern)))
        res (into start addon)
        res (vec  (flatten res))
        ]
    res
    ))

(defn make-patterns [base-pattern input]
  (mapv (fn [i]
          (let [base (pattern-vector input base-pattern i)
                ]
            base
            ))
        (range 1 (inc (count input)))))


;;(reduce +  (mapv * test-num (pattern-vector base-pattern))
(defn calc-digit [input patterns digit]
  (let [
        d (mod (u/abs (reduce + (mapv * input (get patterns (dec digit))))) 10)

        ]d
       )
  )


(defn do-phase [input patterns]
  (map-indexed (fn [index item]
                 (calc-digit input patterns (inc  index)))
               input))



;;(def patterns (make-patterns base-pattern input))


(defn do-phase-times [input patterns times]
  (let [
        next (map-indexed (fn [index item] 
                            (calc-digit input patterns (inc index))) 
                          input)
        ]
    (cond
      (= times 0) next 
      :else (recur next patterns (dec times))
      )))

;;    (3 9 4 5 1 1 9 8)

;;52611030
;;52541026
