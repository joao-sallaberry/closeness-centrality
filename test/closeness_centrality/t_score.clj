(ns closeness-centrality.t_score
  (:use closeness-centrality.score)
  (:use midje.sweet))


(fact
    (f-factor 0) => 0
    (f-factor 1) => 1/2
    (f-factor 2) => 3/4)

(fact
    (apply-factors-to-scores {:1 1} {:1 1}) =>
    {:1 1}
    (apply-factors-to-scores {:1 1 :2 3} {:1 2 :2 1}) =>
    {:1 2 :2 3}
    (apply-factors-to-scores {:1 1} {:2 1}) => (throws Exception))

(fact
    (final-score {} #{}) => {}
    (final-score {:1 #{:2} :2 #{:1}} #{}) => {:1 1 :2 1}
    (final-score {:1 #{:2} :2 #{:1}} #{:1}) => {:1 0 :2 1/2}
    (final-score {:1 #{:2} :2 #{:1}} #{:1 :2}) => {:1 0 :2 0})
