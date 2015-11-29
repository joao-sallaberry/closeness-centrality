(ns web-api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [web-api.methods :as web]))

;;
;; Schemas
;;
(s/defschema Message {:message String})

;;
;; Endpoints
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

    (GET* "/rank" []
      :query-params []
      :summary      "Return closeness ranking of edges"
      (ok (web/rank-nodes)))

    (PUT* "/edge/:n1/:n2" []
      :return       Message
      :path-params  [n1 :- s/Int, n2 :- s/Int]
      :summary      "Add an edge to the graph connecting nodes n1 and n2"
      (ok (web/add-edge n1 n2)))

    (PUT* "/flag/:node" []
      :return       Message
      :path-params  [node :- s/Int]
      :summary      "Flag node as fraudulent"
      (ok (web/flag-fraudulent node)))
))
