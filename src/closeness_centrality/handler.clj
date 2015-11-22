(ns closeness-centrality.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [closeness-centrality.core :as cc]))

;;
;; Schemas
;;
(s/defschema Message {:message String})
(s/defschema Pizza Message)
;;
;; Methods
;;
(defapi app
  (swagger-ui)
  (swagger-docs 
    {:info {:title "API for handling social network"}})

  (GET* "/" []
    :no-doc true
    (ok "hello world"))

  (context* "/api" []
    :tags ["closeness centrality"]

    (GET* "/rank" [] ; TODO: change to include fraud
      :query-params []
      :summary      "Return closeness ranking of edges"
      (ok (cc/closeness-sorted-nodes cc/graph)))

    (GET* "/edge" []
      :return       Message
      :query-params [n1 :- Long, n2 :- Long]
      :summary      "Add an edge to the graph connecting nodes n1 and n2"
      (ok (cc/rest-add-edge n1 n2)))

    (PUT* "/flag/:node" []
      :return Message
      :path-params [node :- s/Int]
      :summary "Flags node as fraudulent"
      (ok (cc/rest-flag-fraudulent cc/graph
                                   (keyword (str node)))))
))
