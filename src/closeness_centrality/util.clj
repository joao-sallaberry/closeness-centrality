(ns closeness-centrality.util)

;;
;; Helper funcions
;;

(defn sort-map-by-value [in-map]
  "Sort a map by value"
  (into (sorted-map-by (fn [key1 key2]
                         (compare [(in-map key2) key2]
                                  [(in-map key1) key1])))
        in-map))
