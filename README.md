# Closeness Centrality

Web App for ranking nodes in a social network based on closeness centrality and fraud.

## Installation

Download from https://github.com/joao-sallaberry/closeness-centrality.

## Usage

1. Install Leiningen (http://leiningen.org/).

2. Clone the repo at https://github.com/joao-sallaberry/closeness-centrality.

3. cd to the folder and execute:
   
    lein ring server

4. Go to localhost:3000/index.html to check the available endpoints.

## Endpoints

- GET /api/rank - Return list of JSON 

    [ { "node": "2", "score": 0.04136029411764706 },
      { "node": "8", "score": 0.04029605263157895 },
      { "node": "7", "score": 0.03860294117647059 },
      { "node": "3", "score": 0.03860294117647059 },
      ... ]

- PUT /api/edge/{n1}/{n2} - Add an edge connecting nodes n1 and n2

    { "message": "edge 3 <-> 1 successfully added" }

- PUT /api/flag/{node} - Flag node as fraudulent

    { "message": "node 1 flagged as fraudulent" }

## License

Copyright © 2015

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
