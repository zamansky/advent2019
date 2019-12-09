(ns utils)

(defn parse-int [s]
  "string->int"
  (biginteger (re-find  #"\-?\d+" s )))

(defn abs [n] (max n (- n)))

(defn char->int [c]
  (Long/parseLong (String/valueOf c)))

