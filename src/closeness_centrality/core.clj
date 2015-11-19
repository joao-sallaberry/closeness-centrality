(ns closeness-centrality.core
  (:gen-class))

(use 'clojure.java.io)
(require '[clojure.string :as str])

(defn read-lines []
  "Read lines from file"
  (map (fn [line]
         (str/split line #" "))
       (str/split (slurp "resources/edges.txt") #"\r\n")))

(defn mirror-edges [lines]
  (into
   (map (fn [[n1 n2]]
          (vector (keyword n1) (Integer. n2)))
        lines)
   (map (fn [[n1 n2]]
          (vector (keyword n2) (Integer. n1)))
        lines)))

(defn add-edges-to-graph [edges graph]
  (reduce (fn [g [n1 n2]]
            (if (contains? g n1)
              (assoc g n1 (conj (n1 g) n2))
              (assoc g n1 [n2])))
          graph
   edges))

(def graph (add-edges-to-graph (mirror-edges (read-lines))
                               (sorted-map)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  )
