(ns day05
  (:require [clojure.string :as string]
            [utils :as u]))



;;(def raw_data (slurp "day05.dat"))
(def raw_data (slurp "day05.dat"))
(def data  (->> (string/split raw_data #",")
                (map  u/parse-int)
                (into [])))

(def output (atom []))

(def opcode-sizes {1 4 2 4 3 2 4 2 99 0})


;;  (if (< (count x) numargs)
;;   (into  (vec  (take (- (- opcodesize 2) (count x) ) (repeat 0))) x ) x ))


(defn parse-op [op]
  (let [opcode (rem op 100)
        opcodesize (get opcode-sizes opcode)
        args (- opcodesize 2)
        modes  (->> (quot op 100)
                    str
                    (str "000")
                    reverse
                    (map #(u/parse-int (str %)))
                    ;;(take args)
                    (into [])
                    )]
    {:opcode opcode :modes modes :opsize opcodesize}
    ))

(defn do-1-2 [opcode modes program current]
  (let [a (if (= (get modes 0) 0)
            (get program (get program (+ current 1)))
            (get program (+ current 1)))
        b (if (= (get modes 1) 0)
            (get program (get program (+ current 2)))
            (get program (+ current 2)))
        c (get program (+ current 3))
        result  (if (= opcode 1)
                  (+ a b)
                  (* a b)
                  )
        ]
    (assoc program  c result)
    )
  )

(defn do-op [{:keys [opcode modes] :as fullop} program current]
  (println fullop)
  (cond
    (= opcode 99) program
    (= opcode 1) (do-1-2 opcode modes program current)
    (= opcode 2) (do-1-2 opcode modes program current)
    (= opcode 3) (assoc program (get program  (inc current)) 1) ;; save the input at the oplocation
    (= opcode 4) (do
                   (let [value (if (= (get modes 0) 0)
                                 (get program (get program (inc current)))
                                 (get program (inc current))
                                 )
                         ]
                     (swap! output conj value)
                     program)
                   )

    )
  )

(defn process-part1 [program current]  
  (let [op (get program current)
        fullop (parse-op op)
        ]
    (cond
      (= op 99) program
      :else (recur (do-op fullop program current) (+ current (:opsize fullop) ))
      )
    ))


