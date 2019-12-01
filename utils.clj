(ns utils)

(defn parse-int [s]
  (Integer. (re-find  #"\d+" s )))
