(ns day07
  (:require [clojure.string :as string]
            [utils :as u]))



;;(def raw_data (slurp "day05.dat"))
(def raw_data (slurp "day07.dat"))
(def data  (->> (string/split raw_data #",")
                (map  u/parse-int)
                (into [])))

(def output (atom []))
(def input (atom [5]))


(defn get-param [program current modes param-num]
  (if (= (get modes param-num) 0)
    (get program (get program (+ current param-num 1)))
    (get program (+ current param-num 1))
    ))

(defn parse-op [op]
  (let [opcode (rem op 100)
        modes  (->> (quot op 100)
                    str
                    (str "000")
                    reverse
                    (map #(u/parse-int (str %)))
                    ;;(take args)
                    (into [])
                    )]
    {:opcode opcode :modes modes }
    ))

(defn do-op1-2 [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
  ;;(println "op12: " fullop (count program) current)
  (let [a (get-param program current modes 0)
        b (get-param program current modes 1) 
        c (get program (+ current 3))
        result  (if (= opcode 1)
                  (+ a b)
                  (* a b)
                  )]
    {:program (assoc program  c result) :current (+ current 4)}
    ))


(defn get-next-input []
  (let [retval (first @input)]
    (swap! input ( comp vec   rest))
    retval
    ))


(defn do-op3 [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
  ;;(println "op3: " fullop (count program) current)
  (let [a (get-param program current modes 0)
        b (get program (+ current 1))
        ]
    {:program (assoc program b (get-next-input)) :current (+ current 2)}))




(defn do-op5 [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
  (let [a (get-param program current modes 0)
        b (get-param program current modes 1)
        ]
    ;;(println "OP5 " fullop a b)
    (if (= a 0)
      {:program program :current (+ current 3)}
      {:program program :current b})
    ))

(defn do-op6 [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
  (let [a (get-param program current modes 0)
        b (get-param program current modes 1)

        ]
    ;;(println "OP6 " fullop a b)
    (if (= a 0)
      {:program program :current b}
      {:program program :current (+ current 3)}
      )) )


(defn do-op7 [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
(let [a (get-param program current modes 0)
      b (get-param program current modes 1)
      c (get program (+ current 3))
      ]
  (if (< a b)
    {:program (assoc program c 1) :current (+ current 4)}
    {:program (assoc program c 0) :current (+ current 4)}
    )) )

(defn do-op8 [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
(let [a (get-param program current modes 0)
      b (get-param program current modes 1)
      c (get program (+ current 3))
      ]
  (if (= a b)
    {:program (assoc program c 1) :current (+ current 4)}
    {:program (assoc program c 0) :current (+ current 4)}
    )) )


(defn do-op [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
  ;;(println "DOOP: " fullop)
  (let [newpayload (cond
                     (= opcode 99) {:program program :current current}
                     (= opcode 1) (do-op1-2 fullop payload)
                     (= opcode 2) (do-op1-2 fullop payload)
                     (= opcode 3) (do-op3 fullop payload)
                     (= opcode 4) (do
                                    (let [value (get-param program current modes 0)]
                                      (swap! output conj value)
                                      {:program program :current (+ current 2)} )
                                    )
                     (= opcode 5) (do-op5 fullop payload)
                     (= opcode 6) (do-op6 fullop payload)
                     (= opcode 7) (do-op7 fullop payload)
                     (= opcode 8) (do-op8 fullop payload)
                     )]
    newpayload
    ))

(defn process [payload]
  (let [program (:program payload)
        current (:current payload)
        op (get program current)
        fullop (parse-op op)
        payload {:program program :current current}
        ]
    (cond
      (= op 99) (last @output)
      :else (recur (do-op fullop {:program program :current current}) )
      )
    )

  )

(defn permutations [colls]
  (if (= 1 (count colls))
    (list colls)
    (for [head colls
          tail (permutations (disj (set colls) head))]
      (cons head tail))))

(def orders (permutations [0 1 2 3 4]))

(defn run-one [order]
  (loop [setting (first order) order (rest order)]
    ;;(println order " : " )
    (reset! input [setting (last @output)])
    (if (empty? order)
      (process {:program data :current 0})
      (do (process {:program data :current 0})
          (recur (first order) (rest order)))))
  )
(defn runsim []
  (let [orders (permutations [0 1 2 3 4])]
    (for [order orders]
      (do  (reset! output [0])
           (run-one order)
           ))
    ))
