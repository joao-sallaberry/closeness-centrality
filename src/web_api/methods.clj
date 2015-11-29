(ns web-api.methods
  (:require [closeness-centrality.graph-generation :as gph]
            [closeness-centrality.score :as score]
            [closeness-centrality.util :as util]))

;;
;; Services to handle the social network
;;

(defn add-edge [n1 n2]
  "Endpoint for adding edges to the graph"
  (swap! gph/graph gph/add-edge [(keyword (str n1))
                                 (keyword (str n2))])
  {:message (str "edge " n1 " <-> " n2 " successfully added")})

(defn flag-fraudulent [node]
  "Endpoint for flagging nodes as fraudulent"
  (let [n (keyword (str node))]
    (if (contains? @gph/graph n)
      (do
        (swap! score/fraudulent conj n)
        {:message (str "node " node " flagged as fraudulent")})
      {:message (str "node " node " does not exist")})))

(defn rank-nodes []
  "Endpoint listing nodes orded by score"
  (map #(hash-map :node (first %) :score (second %))
       (util/sort-map-by-value (score/final-score @gph/graph))))
