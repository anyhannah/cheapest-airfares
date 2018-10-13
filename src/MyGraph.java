// A MyGraph class is used to get a set of vertices and edges,
// and find the adjacent vertices to a vertex and the cost from one
// edge to another. It also computes the shortest path (with minimum cost)
// from one vertex to another vertex in the graph as well as
// the minimum spanning tree of the graph

import java.util.*;

/**
 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 */
public class MyGraph implements Graph {

    private List<Vertex> vertices;
    private Set<Edge> edges;
    private Map<Vertex, Integer> vertexInt; // vertex and its index in array matrix
    private int[][] adjMat; // matrix to contain cost between vertices

    /**
     * Pre: Throws an IllegalArgumentException if vertices are not found in the graph,
     if weight is negative, or if the same directed edges have different weight.
     * Post:Creates a MyGraph object with the given collection of vertices
     * 		and the given collection of edges.
     * @param v a collection of the vertices in this graph
     * @param e a collection of the edges in this graph
     */

    public MyGraph(Collection<Vertex> v, Collection<Edge> e) {
        Collection<Vertex> copiedVCol = new ArrayList<Vertex>(); // new storage for copied vertices
        Collection<Edge> copiedEgCol = new ArrayList<Edge>(); // new storage for copied edges

        // Moves a copy of vertices to a vertex storage
        for (Vertex target:v) {
            Vertex newV = new Vertex(target.toString());
            copiedVCol.add(newV);
        }

        // Moves a copy of edges to an edge storage
        for (Edge target:e) {
            Edge newEg = new Edge(target.getSource(), target.getDestination(), target.getWeight());
            copiedEgCol.add(newEg);
        }

        vertexInt = new HashMap<Vertex, Integer>();
        edges = new HashSet<Edge>(copiedEgCol);
        Set<Vertex> tempVertices = new HashSet<Vertex>(copiedVCol); // no duplicated vertices
        vertices = new ArrayList<Vertex>(tempVertices);

        for (Vertex target:vertices) {
            int idx = vertices.indexOf(target);
            vertexInt.put(target, idx);
        }

        for (Edge targetEdge: e) {
            if (!vertices.contains(targetEdge.getDestination())
                    || !vertices.contains(targetEdge.getSource())) {
                throw new IllegalArgumentException();
            }
            if (targetEdge.getWeight() < 0) {
                throw new IllegalArgumentException();
            }
        }

        // Fills the matrix with all -1
        adjMat = new int[vertices.size()][vertices.size()];
        for (int i=0; i<vertices.size(); i++) {
            for (int j=0; j<vertices.size(); j++) {
                adjMat[i][j] = -1;
            }
        }

        // Completes adjacency matrix with cost from vertex to vertex
        for (Edge targetE: edges) {
            int row = vertexInt.get(targetE.getSource());
            int column = vertexInt.get(targetE.getDestination());
            if (adjMat[row][column] != -1) {
                throw new IllegalArgumentException();
            }
            adjMat[row][column] = targetE.getWeight();
        }
    }


    /**
     * Return the collection of vertices of this graph
     * @return the vertices as a collection (which is anything iterable)
     */
    public Collection<Vertex> vertices() {
        Collection<Vertex> newVertices = new ArrayList<Vertex>(); // new storage for copied vertices

        // Moves a copy of vertices
        for (Vertex target:vertices) {
            Vertex newV = new Vertex(target.toString());
            newVertices.add(newV);
        }
        return newVertices;
    }

    /**
     * Return the collection of edges of this graph
     * @return the edges as a collection (which is anything iterable)
     */
    public Collection<Edge> edges() {
        Collection<Edge> newEdges = new ArrayList<Edge>(); // new storage for copied edges

        // Moves a copy of edges
        for (Edge target:edges) {
            Edge newEg = new Edge(target.getSource(), target.getDestination(), target.getWeight());
            newEdges.add(newEg);
        }
        return newEdges;
    }

    /**
     * Return a collection of vertices adjacent to a given vertex v.
     *   i.e., the set of all vertices w where edges v -> w exist in the graph.
     * Return an empty collection if there are no adjacent vertices.
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    public Collection<Vertex> adjacentVertices(Vertex v) {
        if (!vertexInt.containsKey(v) || v.equals(null)) {
            throw new IllegalArgumentException();
        }
        List<Vertex> adjacent = new LinkedList<>();

        int idx = vertexInt.get(v);
        for (int i=0; i<vertices.size(); i++) {
            if (adjMat[idx][i] != -1) {
                adjacent.add(vertices.get(i)); //adjacent vertex
            }
        }
        return adjacent;
    }

    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
     * Assumes that we do not have negative cost edges in the graph.
     * @param a one vertex
     * @param b another vertex
     * @return cost of edge if there is a directed edge from a to b in the graph,
     * return -1 otherwise.
     * @throws IllegalArgumentException if a or b do not exist.
     */
    public int edgeCost(Vertex a, Vertex b) {
        if (!vertexInt.containsKey(a) || !vertexInt.containsKey(b)
                || a.equals(null) || b.equals(null)) {
            throw new IllegalArgumentException();
        }

        int sourceInt = vertexInt.get(a);
        int destinInt = vertexInt.get(b);
        return adjMat[sourceInt][destinInt];
    }

    /**
     * Returns the shortest path from a to b in the graph, or null if there is
     * no such path.  Assumes all edge weights are nonnegative.
     * Uses Dijkstra's algorithm.
     * @param a the starting vertex
     * @param b the destination vertex
     * @return a Path where the vertices indicate the path from a to b in order
     *   and contains a (first) and b (last) and the cost is the cost of
     *   the path. Returns null if b is not reachable from a.
     * @throws IllegalArgumentException if a or b does not exist.
     */
    public Path shortestPath(Vertex a, Vertex b) {
        Set<Vertex> unchecked = new HashSet<Vertex>();
        Map<Vertex, Integer> cost = new HashMap<Vertex, Integer>();
        Map<Vertex, Vertex> predecessor = new HashMap<Vertex, Vertex>();
        if (!vertices.contains(a) || !vertices.contains(b)) {
            throw new IllegalArgumentException();
        }
        List<Vertex> path = new LinkedList<>();

        //When the start and end vertex are equal
        if (a.equals(b)) {
            path.add(a);
            return new Path(path, 0);
        }

        Vertex from = a;
        for (Vertex v:vertices) {
            unchecked.add(v);
            cost.put(v, Integer.MAX_VALUE);
            predecessor.put(v, v);
        }

        // update costs of the source's adjacent vertices
        for (Vertex adjV :adjacentVertices(from)) {
            cost.put(adjV, edgeCost(a,adjV));
            predecessor.put(adjV, from);
        }
        unchecked.remove(from);
        while (unchecked.size() > 1) {
            // when every cost is infinity and no minimum cost is found,
            // pick one vertex randomly
            if (minVertex(cost, unchecked, from).equals(from)) {
                from = unchecked.iterator().next();
            } else { // replace starting vertex with the one having minimum cost
                from = minVertex(cost, unchecked, from);
            }

            // update cost with lowest-cost
            for (Vertex v :adjacentVertices(from)) {
                if (unchecked.contains(v)) {
                    if (cost.get(v) > cost.get(from)+edgeCost(from,v)) {
                        cost.put(v, cost.get(from)+edgeCost(from,v));
                        predecessor.put(v, from);
                    }
                }
            }
            unchecked.remove(from);
        }

        // starting from the destination, keep track of
        // the vertices' predecessor and reverse the order
        Vertex vtx = b;
        while (!vtx.equals(a)) {
            path.add(vtx);
            if (vtx.equals(predecessor.get(vtx))) {
                return null; // no path is found
            } else {
                vtx = predecessor.get(vtx);
            }
        }
        path.add(vtx);
        Collections.reverse(path);
        int minCost = cost.get(b);

        return new Path(path, minCost);
    }

    // Given the vertices' cost, set of unknown vertices, and previous vertex
    // that had minimum cost, it returns a vertex which are not
    // known yet and has the minimum cost
    private Vertex minVertex(Map<Vertex, Integer> cost, Set<Vertex> unchecked, Vertex minKey) {
        int min = Integer.MAX_VALUE;
        for (Vertex target: unchecked) {
            if (cost.get(target) < min) {
                min = cost.get(target);
                minKey = target;
            }
        }
        return minKey;
    }

    /**
     * Method uses Prim's algorithm to find min spanning tree
     * @return Set of edges of the minimum spanning tree of the current graph
     */
    public Set<Edge> getMinimumSpanningTreeEdges() {
        class UnorientedEdge implements Comparable<UnorientedEdge> {
            private List<Vertex> vertices;
            private int weight;

            public UnorientedEdge(List<Vertex> vertices, int weight) {
                this.vertices = vertices;
                this.weight = weight;
            }

            public boolean isConnectedTo(Vertex vertex) {
                return vertices.contains(vertex);
            }

            public Vertex getFirstVertex() {
                return vertices.get(0);
            }

            public Vertex getSecondVertex() {
                return vertices.get(1);
            }

            public int getWeight() {
                return weight;
            }

            @Override
            public int compareTo(UnorientedEdge other) {
                return weight - other.weight;
            }
        }

        Set<Edge> result = new HashSet<>();
        List<UnorientedEdge> unorientedEdges = new ArrayList<UnorientedEdge>();
        for (Edge e : edges) {
            List<Vertex> vertices = new ArrayList<>();
            vertices.add(e.getSource());
            vertices.add(e.getDestination());
            unorientedEdges.add(new UnorientedEdge(vertices, e.getWeight()));
        }

        List<Vertex> notConnected = new ArrayList<>(vertices);
        List<Vertex> connected = new ArrayList<>();
        if (!notConnected.isEmpty()) {
            Vertex start = notConnected.remove(0);
            connected.add(start);
        }

        while (!notConnected.isEmpty()) {
            PriorityQueue<UnorientedEdge> connectedEdges = new PriorityQueue<>();
            //finding edges that connected to vertices of connected list
            for (UnorientedEdge edge : unorientedEdges) {
                for (Vertex connectedVertex : connected) {
                    if (edge.isConnectedTo(connectedVertex)) {
                        connectedEdges.offer(edge);
                    }
                }
            }

            //adding "cheapest" edge (which doesn't create a circle) to the result list
            do {
                List<Vertex> notConnectedPrev = notConnected;
                //getting "cheapest" edge
                UnorientedEdge edge = connectedEdges.poll();
                Vertex first = edge.getFirstVertex();
                Vertex second = edge.getSecondVertex();

                //checking for circles
                if (!(connected.contains(first) && connected.contains(second))) {
                    result.add(new Edge(first, second, edge.getWeight()));

                    //removing vertex from notConnected and adding to connected
                    if (connected.contains(first)) {
                        connected.add(second);
                        notConnected.remove(second);
                    } else {
                        connected.add(first);
                        notConnected.remove(first);
                    }
                    break;
                }
				if (notConnected.equals(notConnectedPrev)) {
					notConnected = new ArrayList<>();
				}
            } while (!connectedEdges.isEmpty());

        }
        
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Edge e : edges) {
            sb.append(e.getSource() + "\n");
            sb.append(e.getDestination() + "\n");
            sb.append(e.getWeight() + "\n");
        }
        return sb.toString();
    }
}
