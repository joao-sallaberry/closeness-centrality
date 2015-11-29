(ns closeness-centrality.graph-generation
  (:gen-class))

(require '[clojure.string :as str])

;;
;; Generate graph from file
;;

;(def edges-file "resources/edges.txt")
(def edges-file "resources/simple-test.txt")

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
  (atom (add-edges-to-graph (read-lines edges-file)
                            (sorted-map))))
