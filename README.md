# cheapest-airfares

This project uses a graph representation to implement **Dijkstra's algorithm** for computing lowest-cost paths between airports in the United States and **Prim's algorithm** for finding the minimum spanning tree.


### Running the Program
The driver program you will run is `cheapest-airfares/src/airportGraph.java`. It takes one of the following arguments.

* `-p`: finding cheapest paths

* `-m`: finding the minimum spanning tree

With `-p`, you will be asked two prompts: 'departure airport?' and 'arrival airport?'. The program will return the lowest-cost path from the departure airport to the arrival airport.

If there is no path, it will let you know there exists no such path. If the start and end airports are equal, it will return a path containing one airport and a cost of 0. Otherwise, the path will contain at least two airport names -- the start and end airports and any other airports along the lowest-airfare path. The airports are in the order they appear on the path.

With `-m`, the program will print one minimum spanning tree, which is a path that connects all the airports together without any cycles and with the minimum possible total airfares.


### Data
The data for domestic airline airfare from the second quarter of 2017 were obtained from [Department of Transportation](https://data.transportation.gov/Aviation/Consumer-Airfare-Report-Table-1a-All-U-S-Airport-P/tfrh-tu9e).

The specific report used in this project can be downloaded [here](https://data.transportation.gov/Aviation/Consumer-Airfare-Report-Table-1a-All-U-S-Airport-P/tfrh-tu9e).
