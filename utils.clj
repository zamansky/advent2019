(ns utils)

(defn parse-int [s]
  "string->int"
  (Integer. (re-find  #"\d+" s )))
