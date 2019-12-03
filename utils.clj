(ns utils)

(defn parse-int [s]
  "string->int"
  (Integer. (re-find  #"\d+" s )))

(defn abs [n] (max n (- n)))
