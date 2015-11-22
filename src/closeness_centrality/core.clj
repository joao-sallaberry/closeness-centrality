(ns closeness-centrality.core
  (:gen-class))

(use 'clojure.java.io)
(require '[clojure.string :as str])

(def edges-file "resources/simple-test.txt")
;(def edges-file "resources/edges.txt")

(defn read-lines [file]
  "Read lines of edges from file"
  (map (fn [line]
         (map keyword (str/split line #" ")))
       (str/split (slurp file) #"\r?\n")))

(defn add-directed-edge [graph edge]
  "Add directed edge to graph"
  (let [[n1 n2] edge]
    (if (contains? graph n1)
              (assoc graph n1 (conj (n1 graph) n2))
              (assoc graph n1 [n2]))))

(defn add-edge [graph edge]
  "Add edge to graph in both directions"
  (let [[n1 n2] edge]
    (add-directed-edge 
     (add-directed-edge graph [n1 n2]) [n2 n1])))

(defn add-edges-to-graph [edges graph]
  "Add a list of edges to graph"
  (reduce add-edge graph edges))

(def graph "Graph generated from edges-file"
  (add-edges-to-graph (read-lines edges-file)
                                 (sorted-map)))

(defn web-add-edge [n1 n2]
  "Endpoint for adding edges to the graph"
  (def graph (add-edge graph [(keyword (str n1))
                              (keyword (str n2))]))
  {:message (str "edge " n1 "<->" n2 " successfully added")})

(defn distance-to-all-nodes [graph s]
  "BFS to find the distance from s to all other nodes"
  (loop [distances {s 0}
         explored #{s}
         queue (conj (clojure.lang.PersistentQueue/EMPTY) s)]
    (if (empty? queue)
      distances
      (let [n (peek queue)
            neighbors (graph n)
            new-neighbors (remove explored neighbors)
            node&dist #(hash-map % (inc (distances n)))]
        (recur
         (apply merge distances (map node&dist new-neighbors))
         (into explored neighbors)
         (into (pop queue) new-neighbors))))))

(defn farness [graph node]
  "Farness value of node"
  (reduce-kv #(+ %1 %3) 0 (distance-to-all-nodes graph node)))

(defn closeness [graph node]
  "Closeness value of node"
  (/ 1 (farness graph node)))

(defn nodes-closeness [graph]
  "Closeness value of each node in graph"
  (reduce-kv (fn [ranking key _]
               (assoc ranking key (closeness graph key)))
             (sorted-map)
             graph))

(def fraudulent "List of fraudulent nodes" #{:5})

(defn web-flag-fraudulent [graph node]
  "Endpoint for flagging nodes as fraudulent"
  (let [n (keyword (str node))]
    (if (contains? graph n)
      (do
        (def fraudulent (conj fraudulent n))
        {:message (str "node " node " flagged as fraudulent")})
      {:message (str "node " node " does not exist")})))

(defn f-factor [k]
  "Find coefficient F(k) = (1 - (1/2)^k)"
  (let [exp #(reduce * (repeat %2 %1))]
    (- 1 (exp 1/2 k))))

(defn apply-factors-to-scores [scores factors]
  "Multiply every value in scores by its correspondent in factors"
  (reduce-kv ; map??
   (fn [scores n score]
     (assoc scores n (* (scores n) score)))
   scores
   factors))

(defn final-score [graph]
  "Final score of every node considering fraud"
  (reduce (fn [scores fraud]
            (apply-factors-to-scores
             scores
             (reduce-kv #(assoc %1 %2 (f-factor %3))
                        {}
                        (distance-to-all-nodes graph fraud))))
          (nodes-closeness graph)
          fraudulent))

(defn sort-map-by-value [in-map]
  "Sort a map by value"
  (into (sorted-map-by (fn [key1 key2]
                         (compare [(in-map key2) key2]
                                  [(in-map key1) key1])))
        in-map))

(defn web-rank-nodes [graph]
  "Endpoint listing nodes orded by score"
  (map #(hash-map :node (first %) :score (second %))
       (sort-map-by-value (final-score graph))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (sort-map-by-value (final-score graph))
  )
