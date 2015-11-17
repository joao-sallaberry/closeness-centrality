(ns closeness-centrality.core
  (:gen-class))

(use 'clojure.java.io)
(require '[clojure.string :as str])

(defn add-edge-from-line-helper [graph n1 n2]
  (let [node1 (keyword (str \v n1))
        node2 (read-string n2)
        node-list (get graph node1)]
    (if node-list
      (assoc graph node1 (conj node-list node2))
      (assoc graph node1 #{node2}))))

(defn add-edge-from-line [graph line]
  (let [[node1 node2] (str/split line #" ")]
    (add-edge-from-line-helper 
     (add-edge-from-line-helper graph node1 node2) node2 node1)))

(defn read-graph []
  "Read graph edges from file"
  (with-open [rdr (reader "resources/edges.txt")]
    (loop [line (line-seq rdr)
           graph {}]
      ;(println line)
      (if (not (string? line))
        graph
        (recur (line-seq rdr) (add-edge-from-line graph line))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (read-graph)
  ;(bfs tree)
  )
