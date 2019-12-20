(ns day11
  (:require [clojure.string :as string]
            [utils :as u]
            [intcode :as ic]
            ))

(def source (ic/load-source "day11.dat"))

(defn -main[]
  (ic/run-code {:program source :current 0})
  )


(def res (ic/run-code {:program source :current 0 :base 0 :input 0 :output []}))
(:status res)
(:output res)

(def dirs [[0 -1] [1 0] [0 1] [-1 0]])

(defn move-robot [robot dir]
  (mapv + robot (get dirs dir))
  )

(defn part1 [payload]
  (loop [board {}
         payload payload
         robot [0 0]
         dir 0 ]
    (let [ {:keys [status input output] :as payload} (ic/run-code  payload)
          newdir (cond
                   (= (get output 1) 1) (mod (dec dir) 4)
                   (= (get output 1) 0) (mod (inc dir) 4)
                   :else dir
                   )
          newrobot (move-robot robot newdir)
          nextcolor (if  (get board newrobot) (get board newrobot ) 0 )
          color (if  (get board robot) (get board robot ) 0 )
          newboard (assoc board robot color)
          ]
      ;;(println output robot dir)
      ;;(println color board newboard newrobot newdir)
      (cond
        (= status :exit) board
        :else (recur newboard
                     (if (= (:status payload) :needs-input)
                       (assoc payload :output [] :input nextcolor :status nil)
                       (assoc payload :output []))
                     
                     newrobot
                     newdir
                     )
        ))))


(count  (part1 {:program source :current 0 :base 0 :input 0 :output []}))
