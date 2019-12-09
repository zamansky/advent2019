(ns day09
  (:require [clojure.string :as string]
            [utils :as u]))



(def raw_data (slurp "day09.dat"))
(def data  (->> (string/split raw_data #",")
                (map  u/parse-int)
                (into [])))

(def input9 (into  data (take 5000 (repeat 0)) ))


(def output (atom []))
(def input (atom [2]))
(def base (atom 0))

(defn get-param [program current modes param-num]
(let [param (get modes param-num)]
  (cond
    (= param 0) (get program (get program (+ current param-num 1)))
    (= param 1) (get program (+ current param-num 1))
    (= param 2) (get program  (+ @base (get program (+ current param-num 1))))
    )
  ))


(defn get-write-param [program current modes param-num]
  (let [param (get modes param-num)]
    (cond
      (= param 0) (get program (+ current param-num 1))
      (= param 1)  (get program (+ current param-num 1))
      (= param 2) (+ (get program  (+ current param-num 1)) @base)
      )
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
        c (get-write-param program current modes 2)
        
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
  (let [b (get-write-param program current modes 0)
        ]
    {:program (assoc program  b  (get-next-input)) :current (+ current 2)}))

(defn do-op4 [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
  (let [value (get-param program current modes 0)]
    (swap! output conj value)
    {:program program :current (+ current 2)} )
  )




(defn do-op5 [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
(let [a (get-param program current modes 0)
      b (get-param program current modes 1)
      ]
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
      c (get-write-param program current modes 2)
      ]
  (if (< a b)
    {:program (assoc program c 1) :current (+ current 4)}
    {:program (assoc program c 0) :current (+ current 4)}
    )) )

(defn do-op8 [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
  (let [a (get-param program current modes 0)
        b (get-param program current modes 1)
        c (get-write-param program current modes 2) 
        ]
    (if (= a b)
      {:program (assoc program c 1) :current (+ current 4)}
      {:program (assoc program c 0) :current (+ current 4)}
      )) )

(defn do-op9 [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
(let [param (get-param program current modes 0)
      ]
  (swap! base + param)
  {:program program :current (+ current 2)}
  ))

(defn do-op [{:keys [opcode modes] :as fullop} {:keys [program current] :as payload}]
  ;;(println "DOOP: " fullop "input: " @input "Output: " @output current "BASE: " @base)
  (let [newpayload (cond
                     (= opcode 99) {:program program :current current}
                     (= opcode 1) (do-op1-2 fullop payload)
                     (= opcode 2) (do-op1-2 fullop payload)
                     (= opcode 3) (do-op3 fullop payload)
                     (= opcode 4) (do-op4 fullop payload)                     
                     (= opcode 5) (do-op5 fullop payload)
                     (= opcode 6) (do-op6 fullop payload)
                     (= opcode 7) (do-op7 fullop payload)
                     (= opcode 8) (do-op8 fullop payload)
                     (= opcode 9) (do-op9 fullop payload)
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
      :else (recur (do-op fullop {:program program :current current}) ))))


(process {:program input9 :current 0})
(println @output)
