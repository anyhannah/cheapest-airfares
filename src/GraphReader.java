import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GraphReader {

    public Collection<Vertex> readVertex(String f1)  {
        List<Vertex> v = new ArrayList<>(); // storage for vertices
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(f1)))) {
            String line;
            while (((line = reader.readLine()) != null) && !line.isEmpty()) {
                v.add(new Vertex(line));
            }
        } catch (IOException e) {
            System.err.print("Vertex IOException");
            e.printStackTrace();
            System.exit(1);
        }
        return v;
    }

    public Collection<Edge> readEdges(String f2)  {
        List<Edge> edges = new ArrayList<>(); // storage for edges
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(f2)))) {
            String line;
            while (true){
                String a = reader.readLine(); // source line
                String b = reader.readLine(); //destination line
                String w = reader.readLine();
                if ((a != null) && (b != null) && (w != null)) {
                    Vertex from = new Vertex(a); // source
                    Vertex to = new Vertex(b); //destination
                    int weight = (int) Double.parseDouble(w);
                    edges.add(new Edge(from, to, weight));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.print("Edge IOException");
            e.printStackTrace();
            System.exit(1);
        }
        return edges;
    }
}