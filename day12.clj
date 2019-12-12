(ns day12
  (:require [clojure.string :as string]
            [utils :as u]
            ))

( defn calc-deltav [v]
 (cond
   (> v 0) -1
   (< v 0) 1
   :else 0))


(defn calc-deltas-two-moons [m1 m2]
  (let [ [ m1l m1v ]  [ (:loc m1) (:v m1)]
        [ m2l m2v ]  [ (:loc m2) (:v m2)]
        diff1 (mapv - m1l m2l)
        diff2 (mapv calc-deltav diff1)
        newv (mapv + diff2)
        ]
    newv
    )
  )

(defn calc-new-velocity [moon moons]
  (let [m (filterv #(not (= (:loc moon) (:loc %))) moons)
        ]
    (reduce (fn  [moon nextmoon]
              (let [v (:v moon)
                    nextdeltas (calc-deltas-two-moons moon nextmoon)
                    newv (mapv + v nextdeltas)
                    ]
                (assoc moon :v newv )
                )) moon m)))

(defn calc-new-velocities [moons]
  (map #(calc-new-velocity % moons) moons))

(defn move-moons [moons]
  (let [newvs (calc-new-velocities moons)
        newmoons (mapv (fn [m]
                         (assoc m :loc (mapv + (:loc m) (:v m)))
                         ) newvs)
        ]newmoons))


(defn sumabs [v]
  (->> v
       (map u/abs)
       (reduce +)))

(defn calc-energy [moon]
  (let [pe (sumabs (:loc moon))
        ke (sumabs (:v moon) )
        e (* pe ke)
        ]
    (assoc moon :pe pe :ke ke :e e)

    )
  )

(def test-input1 [{:loc [-1 0 2] :v [0 0 0]}
                  {:loc [2 -10 -7] :v [0 0 0]}
                  {:loc [4 -8 8] :v [0 0 0]}
                  {:loc [3 5 -1] :v [0 0 0]}])

(def test-input2 [{:loc [-8 -10 0] :v [0 0 0]}
                  {:loc [5 5 10] :v [0 0 0]}
                  {:loc [2 -7 3] :v [0 0 0]}
                  {:loc [9 -8 -3] :v [0 0 0]}])
(def input [{:loc [1 -4 3] :v [0 0 0]}
            {:loc [-14 9 -4] :v [0 0 0]}
            {:loc [-4 -6 7] :v [0 0 0]}
            {:loc [6 -9 -11] :v [0 0 0]}])


(defn single-move [moons]
  (let [m (move-moons moons)
        moons-with-energy (mapv calc-energy m)
        ]
    moons-with-energy
    ))

(defn part1 [moons steps]
  (let [final-state (loop [moons moons count 0]
                      (cond
                        (= count steps) moons
                        :else (recur  (single-move moons) (inc count))))
        ans (->> final-state
                 (mapv #(:e %))
                 (reduce +))
        ]
    ans))

(defn get-location-axis [moons axis]
  (mapv #(get (:loc %) axis)moons )
  )
(defn get-v-axis [moons axis]
  (mapv #(get (:v %) axis)moons )
  )

(defn find-cycle [init-moons axis]
  (let [test-coords (get-location-axis init-moons axis)
        final-state (loop [moons (single-move init-moons)  count 1]
                      (cond
                        (and (= (get-location-axis moons axis) test-coords)
                             (= (get-v-axis moons axis) '(0 0 0 0)))       count
                        :else (recur  (single-move moons) (inc count))))
        ]
    final-state))


(def cycle-lengths (map #(find-cycle input %) [0 1 2]))

(def part2ans  (reduce u/lcm cycle-lengths))
