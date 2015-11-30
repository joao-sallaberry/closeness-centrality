# Closeness Centrality

Web App for ranking nodes in a social network based on [closeness centrality] (https://en.wikipedia.org/wiki/Centrality#Closeness_centrality) and fraud.

## Challenge Description:

Suppose we are looking to do social network analysis for prospective customers. We want to extract from their social network a metric called "closeness centrality".

Centrality metrics try to approximate a measure of influence of an individual within a social network. The distance between any two vertices is their shortest path. The *farness* of a given vertex *v* is the sum of all distances from each vertex to *v*. Finally, the *closeness* of a vertex *v* is the inverse of the *farness*.

The first part of the challenge is to rank the vertices in a given undirected graph by their *closeness*. The graph is provided in the attached file; each line of the file consists of two vertex names separated by a single space, representing an edge between those two nodes.

The second part of the challenge is to create a RESTful web server with endpoints to register edges and to render a ranking of vertexes sorted by centrality. We can think of the centrality value for a node as an initial "score" for that customer.

The third and final part is to add another endpoint to flag a customer node as "fraudulent". It should take a vertex id, and update the internal customer score as such:
- The fraudulent customer score should be zero.
- Customers directly related to the "fraudulent" customer should have their score halved.
- More generally, scores of customers indirectly referred by the "fraudulent" customer should be multiplied by a coefficient F:

    F(k) = (1 - (1/2)^k)

where k is the length of the shortest path from the "fraudulent" customer to the customer in question.

## Implementation

*In the implementation, vertices are referred to as nodes.*

Since the graph is unweighted and undirected, all-pairs shortest path was found using breadth-first search (bfs) from every node, resulting in time complexity of O(NE + N²), being N the number of nodes and E the number of edges.

BFS was also used to calculate coefficient F for nodes flagged as fraudulent.

The RESTful server was made with the help of [Composure API] (https://github.com/metosin/compojure-api).

## Installation

Download from https://github.com/joao-sallaberry/closeness-centrality.

## Usage

1. Install Leiningen (http://leiningen.org/).

2. Clone the repo at https://github.com/joao-sallaberry/closeness-centrality.

3. cd to the folder and execute:
   
    lein ring server

4. Go to localhost:3000/index.html to check the available endpoints.

## Endpoints

- **GET /api/rank** - Return list of JSON 

    [ { "node": "2", "score": 0.04136029411764706 },
      { "node": "8", "score": 0.04029605263157895 },
      { "node": "7", "score": 0.03860294117647059 },
      { "node": "3", "score": 0.03860294117647059 },
      ... ]

- **PUT /api/edge/{n1}/{n2}** - Add an edge connecting nodes n1 and n2

    { "message": "edge 3 <-> 1 successfully added" }

- **PUT /api/flag/{node}** - Flag node as fraudulent

    { "message": "node 1 flagged as fraudulent" }

## Tests

   *lein midje* will run all tests.

## License

Copyright © 2015

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
