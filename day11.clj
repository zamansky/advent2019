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
