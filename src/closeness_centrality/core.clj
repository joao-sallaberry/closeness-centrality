(ns closeness-centrality.core
  (:gen-class))

(use 'clojure.java.io)
(require '[clojure.string :as str])

(defn read-lines []
  "Read lines from file"
  (map (fn [line]
         (str/split line #" "))
       (str/split (slurp "resources/simple-test.txt") #"\r?\n")))

(defn mirror-edges [lines]
  (into
   (map (fn [[n1 n2]]
          (vector (keyword n1) (keyword n2)))
        lines)
   (map (fn [[n1 n2]]
          (vector (keyword n2) (keyword n1)))
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

(defn distance-to-all-nodes [g s]
  "Runs BFS to find the distance from s to all other nodes"
  (loop [distances {s 0}
         explored #{s}
         queue (conj (clojure.lang.PersistentQueue/EMPTY) s)]
    (if (empty? queue)
      distances
      (let [v (peek queue)
            neighbors (g v)
            new-neighbors (remove explored neighbors)
            node&dist #(hash-map % (inc (distances v)))]
        (recur
         (apply merge distances (map node&dist new-neighbors))
         (into explored neighbors)
         (into (pop queue) new-neighbors))))))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  )
