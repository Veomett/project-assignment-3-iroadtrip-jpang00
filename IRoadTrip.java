import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IRoadTrip {
    private Map<String, Map<String, Integer>> bordersMap;
    private Map<String, Map<String, Integer>> distancesMap;
    private Map<String, String> countryToStateIdMap;

    public IRoadTrip (String [] args) throws IOException {

        String bordersFile = args[0];
        String distancesFile = args[1];
        String stateNamesFile = args[2];

        bordersMap = new HashMap<>();
        distancesMap = new HashMap<>();
        countryToStateIdMap = new HashMap<>();

        //reads borders.txt
        readBordersFile(bordersFile);
        //reads capdist.csv
        readDistancesFile(distancesFile);
        //reads state_name.tsv
        readStateNamesFile(stateNamesFile);
    }



    private void readStateNamesFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            // Process each line from state_name.tsv
            String[] parts = line.split("\t");
            if (parts.length == 5 && "2020-12-31".equals(parts[4])) {
                String stateId = parts[1];
                String stateNumber = parts[0];
                String countryName = parts[2];
                countryToStateIdMap.put(stateId, countryName);
            }
        }
        reader.close();
    }



    public int getDistance (String country1, String country2) {
        // Replace with your code
        return -1;
    }


    public List<String> findPath (String country1, String country2) {
        // Replace with your code


        return null;
    }


    public void acceptUserInput() {
        // Replace with your code

        System.out.println("IRoadTrip - skeleton");
    }




    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);

        a3.acceptUserInput();
    }

}

