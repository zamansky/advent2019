(ns day11
  (:require [clojure.string :as string]
            [utils :as u]
            [intcode :as ic]
            [clojure.core.async :as a  :refer [>! <! >!! <!! go chan buffer close! thread
                                               alts! alts!! timeout]]
            ))

(def source (ic/load-source "day11.dat"))

(ic/run-code {:program source :current 0})

@ic/input


@ic/output
