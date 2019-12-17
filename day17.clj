(ns day17
  (:require [clojure.string :as string]
            [utils :as u]
            [intcode :as ic]
            ))

(def source (ic/load-source "day17.dat"))
(def payload {:program source :current 0 :output [] :input 0})

(def state (ic/run-code payload))
