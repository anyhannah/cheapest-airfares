import java.util.*;

public class AirportGraph {

    public static void main(String[] args) {
  		CSVtoGraph.main();

    	if(args.length != 1) {
            System.err.println("USAGE: [-m | -p]");
            System.exit(1);
        }
        String f1 = "vertices.txt"; // vertex file
        String f2 = "edges.txt"; // edge file
        String opt = args[0]; // MST or shortest path
        if((!"-m".equals(opt)) & (!"-p".equals(opt))) {
            System.err.println("INPUT: requires -m or -p");
            System.exit(1);
        }

        GraphReader graphReader = new GraphReader();
        Collection<Vertex> vertices = graphReader.readVertex(f1);
        Collection<Edge> edges = graphReader.readEdges(f2);
        MyGraph graph = new MyGraph(vertices, edges);
        
        Set<Vertex> uniqueV = new HashSet<Vertex>(vertices);
        Set<Edge> uniqueE = new HashSet<Edge>(edges);

        System.out.println("Airports: "+ uniqueV);
        System.out.println("<Departure, Arrival, Cost>: "+ uniqueE);
        
        if ("-m".equals(opt)) { // MST option
            Set<Edge> minSpanTreeEdges = graph.getMinimumSpanningTreeEdges();
//            MyGraph minSpanTreeGraph = new MyGraph(vertices, minSpanTreeEdges);
            System.out.println("A minimum spanning tree consists of " + minSpanTreeEdges);
        } else { // Shortest path option
        	Scanner console = new Scanner(System.in);
            while(true) {
                System.out.print("Start airport? ");
                Vertex a = new Vertex(console.nextLine());
                if(!vertices.contains(a)) {
                    System.out.println("no such airport for departure");
                    System.exit(0);
                }

                System.out.print("Destination airport? ");
                Vertex b = new Vertex(console.nextLine());
                if(!vertices.contains(b)) {
                    System.out.println("no such airport for arrival");
                    System.exit(1);
                }

                // call shortestPath and print out the result
                Path result = graph.shortestPath(a, b);
                if (result == null) {
                    System.out.println("No path from " + a + " to " + b + " exists.");
                } else {
                    String printPath = "The cheapest flight from " + (result.vertices).get(0);
                    for (int i=1; i<(result.vertices).size(); i++) {
                        printPath += " to " + (result.vertices).get(i);
                    }
                    System.out.println(printPath + " costs $" + result.cost + ".");
                }
            }
        }
    }
}
