(ns intcode
  (:require [clojure.string :as string]
            [utils :as u]
            ))


(defn load-source [filename]
  (let [raw-data (slurp filename)
        data (->> (string/split raw-data #",")
                  (map  u/parse-int)
                  (into []))
        source (into data (take 100000 (repeat 0)))
        ]
    source
    ))




(defn get-param [program current modes param-num base]
  (let [param (get modes param-num)]
    (cond
      (= param 0) (get program (get program (+ current param-num 1)))
      (= param 1) (get program (+ current param-num 1))
      (= param 2) (get program  (+ base (get program (+ current param-num 1))))
      )
    ))


(defn get-write-param [program current modes param-num base]
  (let [param (get modes param-num)]
    (cond
      (= param 0) (get program (+ current param-num 1))
      (= param 1)  (get program (+ current param-num 1))
      (= param 2) (+ (get program  (+ current param-num 1)) base)
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

(defn do-op1-2 [{:keys [opcode modes] :as fullop} {:keys [program current  base input] :as payload}]
  ;;(println "op12: " fullop (count program) current)
  (let [a (get-param program current modes 0 base)
        b (get-param program current modes 1 base) 
        c (get-write-param program current modes 2 base) 
        
        result  (if (= opcode 1)
                  (+ a b)
                  (* a b)
                  )]
    {:program (assoc program  c result) :current (+ current 4) :base base :input input}
    ))




(defn do-op3 [{:keys [opcode modes] :as fullop} {:keys [program current base input] :as payload}]
  ;;(println "op3: " fullop (count program) current)
  (let [b (get-write-param program current modes 0 base)
        ]
    (if input
      {:program (assoc program  b  input) :current (+ current 2) :base base :input input}
      {:program program :current current :base base :status :needs-input}

      )))

(defn do-op4 [{:keys [opcode modes] :as fullop} {:keys [program current base input] :as payload}]
  (let [value (get-param program current modes 0 base) ]
    {:program program :current (+ current 2) :base base :input input :output value :status :output} )
  )




(defn do-op5 [{:keys [opcode modes] :as fullop} {:keys [program current base input] :as payload}]
  (let [a (get-param program current modes 0 base)
        b (get-param program current modes 1 base)
        ]
    (if (= a 0)
      {:program program :current (+ current 3) :base base :input input}
      {:program program :current b :base base :input input} )
    ))

(defn do-op6 [{:keys [opcode modes] :as fullop} {:keys [program current base input] :as payload}]
  (let [a (get-param program current modes 0 base)
        b (get-param program current modes 1 base)

        ]
    ;;(println "OP6 " fullop a b)
    (if (= a 0)
      {:program program :current b :base base :input input}
      {:program program :current (+ current 3) :base base :input input}
      )) )


(defn do-op7 [{:keys [opcode modes] :as fullop} {:keys [program current base input] :as payload}]
  (let [a (get-param program current modes 0 base )
        b (get-param program current modes 1 base )
        c (get-write-param program current modes 2 base)
        ]
    (if (< a b)
      {:program (assoc program c 1) :current (+ current 4) :base base :input input}
      {:program (assoc program c 0) :current (+ current 4) :base base :input input}
      )) )

(defn do-op8 [{:keys [opcode modes] :as fullop} {:keys [program current base input] :as payload}]
  (let [a (get-param program current modes 0 base)
        b (get-param program current modes 1 base)
        c (get-write-param program current modes 2 base) 
        ]
    (if (= a b)
      {:program (assoc program c 1) :current (+ current 4) :base base :input input}
      {:program (assoc program c 0) :current (+ current 4) :base base :input input}
      )) )

(defn do-op9 [{:keys [opcode modes] :as fullop} {:keys [program current base input] :as payload}]
  (let [param (get-param program current modes 0 base)
        ]
    {:program program :current (+ current 2) :base (+ base param) :input input}
    ))

(defn do-op [{:keys [opcode modes] :as fullop} {:keys [program current base input] :as payload}]
  ;;(println "DOOP: " fullop "input: " @input "Output: " @output current "BASE: " base)
  (let [newpayload (cond
                     (= opcode 99) {:program program :current current :status :exit}
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

(defn run-code [payload]
  (let [program (:program payload)
        current (:current payload)
        op (get program current)
        fullop (parse-op op)
        payload payload
        nextpayload (do-op fullop payload)
        status (:status nextpayload)
        ]
    (cond
      (= status :exit) nextpayload
      (= status :needs-input) nextpayload
      (= status :output) nextpayload
      :else (recur nextpayload ))))

