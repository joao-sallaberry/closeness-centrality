(ns closeness-centrality.score
  (:require [closeness-centrality.closeness :as cls]))

;;
;; Nodes scores based on closeness and fraud
;;

(def fraudulent "List of fraudulent nodes" (atom #{}))

(defn f-factor [k]
  "Find coefficient F(k) = (1 - (1/2)^k)"
  (let [exp #(reduce * (repeat %2 %1))]
    (- 1 (exp 1/2 k))))

(defn apply-factors-to-scores [scores factors]
  "Multiply every value in scores by its correspondent in factors"
  (reduce-kv
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
                        (cls/distance-to-all-nodes graph fraud))))
          (cls/nodes-closeness graph)
          @fraudulent))
