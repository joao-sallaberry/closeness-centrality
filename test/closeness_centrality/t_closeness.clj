(ns closeness-centrality.t_closeness
  (:use closeness-centrality.closeness)
  (:use midje.sweet))


(fact 
    (distance-to-all-nodes {:1 #{:2}} :1) => {:1 0 :2 1}
    (distance-to-all-nodes {:1 #{:2}} :2) => {:2 0}
    (distance-to-all-nodes {:1 #{:2 :3} :2 #{:3 :4}} :1) =>
    {:1 0 :2 1 :3 1 :4 2})

(fact
    (farness {} :1)                              => 0
    (farness {:1 #{:2}} :1)                      => 1
    (farness {:1 #{:2 :3 :4}} :1)                => 3
    (farness {:2 #{:3} :3 #{:2 :4} :4 #{:1}} :3) => 4)

(fact
    (closeness {} :1)                      => (throws Exception)
    (closeness {:1 #{:2}} :1)                      => 1
    (closeness {:1 #{:2 :3 :4}} :1)                => 1/3
    (closeness {:2 #{:3} :3 #{:2 :4} :4 #{:1}} :3) => 1/4)

(fact
    (nodes-closeness {}) => {}
    (nodes-closeness {:1 #{2}}) => {:1 1}
    (nodes-closeness {:1 #{:2 :3} :2 #{:1} :4 #{:5}}) =>
    {:1 1/2 :2 1/3 :4 1}
    (nodes-closeness {:1 #{:2 :3} :2 #{:1} :3 #{:1}}) =>
    {:1 1/2 :2 1/3 :3 1/3})
