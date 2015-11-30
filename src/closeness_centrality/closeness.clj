(ns closeness-centrality.closeness)

;;
;; Find closeness of nodes in a graph
;;

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
  (let [f (farness graph node)]
    (if (= f 0)
      (throw (Exception.
              (str "Node " node " cannot reach any node")) )
      (/ 1 f))))

(defn nodes-closeness [graph]
  "Closeness value of each node in graph"
  (reduce-kv (fn [ranking key _]
               (assoc ranking key (closeness graph key)))
             (sorted-map)
             graph))
