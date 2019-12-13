(ns day11
  (:require [clojure.string :as string]
            [utils :as u]
            [intcode :as ic]
            ))

(def source (ic/load-source "day09.dat"))

(defn -main[]
  (ic/run-code {:program source :current 0})
  )


(ic/run-code {:program source :current 0 :base 0})


