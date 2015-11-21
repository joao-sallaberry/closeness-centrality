(ns closeness-centrality.core
  (:gen-class))

(use 'clojure.java.io)
(require '[clojure.string :as str])

(def edges-file "resources/simple-test.txt")

(defn read-lines [file]
  "Read lines from file"
  (map (fn [line]
         (map keyword (str/split line #" ")))
       (str/split (slurp file) #"\r?\n")))

(defn add-directed-edge [graph edge]
  (let [[n1 n2] edge]
    (if (contains? graph n1)
              (assoc graph n1 (conj (n1 graph) n2))
              (assoc graph n1 [n2]))))

(defn add-edge [graph edge]
  (let [[n1 n2] edge]
    (add-directed-edge 
     (add-directed-edge graph [n1 n2]) [n2 n1])))

(defn add-edges-from-lines [edges graph]
  (reduce add-edge graph edges))

(def graph (add-edges-from-lines (read-lines edges-file)
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
