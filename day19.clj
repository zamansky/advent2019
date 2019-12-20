(ns day19
  (:require [clojure.string :as string]
            [utils :as u]
            [intcode :as ic]
            ))

(def source (ic/load-source "day19.dat"))
(def payload {:program source :current 0 :output [] :input 0 :base 0})
(defn part1 [] 
  (count (filter #(= 1%)
                 (for [x (range 50)
                       y (range 50)

                       ]
                   (let [step1 (ic/run-code (assoc payload :input x))
                         step2 (ic/run-code (assoc step1 :input y))
                         out (first (:output step2))
                         ]
                     out
                     )
                   )))
  )

(defn tractored [x y payload]
  (let [step1 (ic/run-code (assoc payload :input x))
        step2 (ic/run-code (assoc step1 :input y))
        out (first (:output step2))
        ]
    (=  out 1)
    )
  )


(defn find-first-x [y payload]
  (loop [ x y ]
    (cond
      (tractored x y payload) x
      :else (recur (inc x))
      )

    ) )

(defn find-last-x [x y payload]
  (loop [ x x ]
    (cond
      (not  (tractored x y payload)) x
      :else (recur (inc x))
      )

    ) )


(defn find-first-x-2 [y payload]
  (loop [ x y]
    (prn x)
    (cond
      (and  (tractored x y payload)
            (not tractored  (dec x) y payload)) x
      (not (tractored x y payload)) (recur (+ x (quot  (-  (* 10000 10000) x) 2)))
      :else (recur (quot x 2))
      )

    ) )

(defn part2 [payload]
  (loop [y 950]
    (let [x (find-first-x y payload)
          x2 (find-last-x x y payload)
          ]
      ;;(prn y x x2 (- x2 x))
      (cond
        (tractored (- x2 99) (- y 99) payload) [(- x2 99) (- y 99)]
        :else (recur (inc y))))))

(defn p2 [payload]
  (let [candidates  (for [y (range 2000) x (range 2000)]
                      (let [check (and (tractored x y payload)
                                       (tractored (+ x 99) y payload)
                                       (tractored x (+ y 99) payload)
                                       (tractored (+ x 99) (+ y 99) payload)
                                       )]
                        [check x y])
                      )]
    (first  (filter #(first %) candidates))
    ))
(defn get-points [] 

  (filter #(= 1 (first %))
          (for [x (range 1000)
                y (range 1000)

                ]
            (let [step1 (ic/run-code (assoc payload :input x))
                  step2 (ic/run-code (assoc step1 :input y))
                  out (first (:output step2))
                  ]
              [x y]
              )
            )))

(defn f []
  (loop [y 300]
    (let [f (find-first-x y payload)
          l (find-last-x f y payload)
          range  (- l f)
          ]
      
      (cond
        (>= range 100) [f y]

        :else (recur (inc y))))))
;; 466 447
;; 568 545



(defn find-line [y]
  (let [x1 (find-first-x y payload)
        x2 (find-last-x x1 y payload)]
    [x2 x1]
    ))
(prn (p2 payload))

;; the below is too low
;; 1144 1000
;; (+ (* 1045 10000) 905)
