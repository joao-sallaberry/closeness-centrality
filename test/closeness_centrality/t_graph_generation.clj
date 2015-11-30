(ns closeness-centrality.t_graph-generation
  (:use closeness-centrality.graph-generation)
  (:use midje.sweet))


(def test-file "resources/test/edges.txt")
(spit test-file "1 2
1 3
2 4
3 4")

(fact 
    (read-lines test-file) => [[:1 :2]
                               [:1 :3]
                               [:2 :4]
                               [:3 :4]])

(fact
    (add-directed-edge {} [:1 :2])        => {:1 #{:2}}
    (add-directed-edge {:2 #{:1}} [:1 :2]) => {:1 #{:2} :2 #{:1}}
    (add-directed-edge {:1 #{:2}} [:1 :3]) => {:1 #{:2 :3}})

(fact
    (add-edge {} [:1 :2])         => {:1 #{:2} :2 #{:1}}
    (add-edge {:1 #{:3}} [:1 :2]) => {:1 #{:2 :3} :2 #{:1}}
    (add-edge {:1 #{:3}} [:2 :4]) => {:1 #{:3} :2 #{:4} :4 #{:2}}
    )

(fact
    (add-edges-to-graph {} [[:1 :2] [:1 :10] [:100 :1]]) =>
    {:1 #{:2 :10 :100} :2 #{:1} :10 #{:1} :100 #{:1}}
    (add-edges-to-graph {:1 #{:2}} [[:1 :3] [:4 :5]]) =>
    {:1 #{:2 :3} :3 #{:1} :4 #{:5} :5 #{:4}}
    (add-edges-to-graph {} (read-lines test-file)) =>
    {:1 #{:2 :3} :2 #{:1 :4} :3 #{:1 :4} :4 #{:2 :3}})
