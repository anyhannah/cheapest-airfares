import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CSVtoGraph {

    public static void main() {

        String csvFile = "2017_Q2_airfare.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        HashSet<String> vertices = new HashSet<String>();
        
        try {
        	FileWriter v_writer = new FileWriter("vertices.txt", true);
        	FileWriter eg_writer = new FileWriter("edges.txt", true);
        	
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // ignore header
            	if (line.contains("Table 1a")) {
	            	// use comma as separator
	                String[] country = line.split(cvsSplitBy);
	                String departure = country[11];
	                String arrival = country[12];
	                
	                vertices.add(departure);
	                vertices.add(arrival);
	                
	                eg_writer.write(departure);
	                eg_writer.write("\n");
	                eg_writer.write(arrival);
	                eg_writer.write("\n");
	                eg_writer.write(country[15]); // flight fare
	                eg_writer.write("\n");
                }
            }
            
            Iterator<String> iterator = vertices.iterator();
            while(iterator.hasNext()) {
            	v_writer.write(iterator.next());
            	v_writer.write("\n");
            }
            v_writer.close();
            eg_writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

}