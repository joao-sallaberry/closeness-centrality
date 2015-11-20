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

(defn farness [g s]
  (reduce-kv #(+ %1 %3) 0 (distance-to-all-nodes g s)))

(defn closeness [g s]
  (/ 1 (farness g s)))

(defn nodes-closeness [g]
  (reduce-kv (fn [ranking key _]
               (assoc ranking key (closeness g key)))
             (sorted-map)
             g))

(defn closeness-sorted-nodes [g]
  (let [unsorted-nodes (nodes-closeness g)]
    (into (sorted-map-by (fn [key1 key2]
                           (compare [(unsorted-nodes key2) key2]
                                    [(unsorted-nodes key1) key1])))
          unsorted-nodes)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  )
