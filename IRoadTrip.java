import java.io.InputStreamReader;
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

        readBordersFile(bordersFile);       //reads borders.txt
        readDistancesFile(distancesFile);   //reads capdist.csv
        readStateNamesFile(stateNamesFile); //reads state_name.tsv
    }

    public void readBordersFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        //Do not need to save the distances in file as they are length of borders
        while ((line = reader.readLine()) != null) {
            // Process each line from the borders file
            String[] parts = line.split("=");
            if (parts.length == 2) {
                String country = parts[0].trim();
                String[] neighbors = parts[1].split(";");

                // If the country is not in the countryDataMap, add it
                bordersMap.putIfAbsent(country, new HashMap<>());

                for (String neighbor : neighbors) {
                    String cleanedNeighbor = neighbor.trim();
                    // Mark the neighbor in the map to indicate a border
                    bordersMap.get(country).put(cleanedNeighbor, 0);
                }
            }
        }
        reader.close();
    }

    public void readDistancesFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        // Skip the first line (first line is headers)
        reader.readLine();

        //distances between each country's capital, will only need the distances from bordering countries
        while ((line = reader.readLine()) != null) {
            // Process each line from the distances CSV file
            String[] parts = line.split(",");

            if (parts.length >= 2) {
                String country = parts[1].trim();          //1st Country's stateID
                String neighbor = parts[3].trim();         //2nd COuntry's stateID
                int distance = Integer.parseInt(parts[4]); // distance is in the 5th column

                // If the country is not in the countryDataMap, add it
                distancesMap.putIfAbsent(country, new HashMap<>());

                // Add the distance to the neighbor
                distancesMap.get(country).put(neighbor, distance);
            }
        }
        reader.close();
    }

    private void readStateNamesFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        //only used to connect country name with state ID
        while ((line = reader.readLine()) != null) {
            // Process each line from state_name.tsv
            String[] parts = line.split("\t");
            if (parts.length == 5 && "2020-12-31".equals(parts[4])) {
                String stateId = parts[1];
                String stateNumber = parts[0];
                String countryName = parts[2];

                countryToStateIdMap.putIfAbsent(countryName, stateId);
            }
        }
        reader.close();
    }



    public int getDistance (String country1, String country2) {
//        This function provides the
//        shortest path distance between the capitals of the two countries passed as arguments.
//        If either of the
//        countries does not exist or if the countries do not share a land border, this function must return a value of
//        -1.
        // Replace with your code


        return -1;
    }


    public List<String> findPath (String country1, String country2) {
//        This function
//        determines and returns the shortest path between the two countries passed as arguments (starting in the
//        capital of country1, ending in the capital of country1, and going through the capitals of each country along
//         the way).



        return null;
    }


    public void acceptUserInput() throws IOException {
//       This function allows a user to interact with your implementation
//        on the console. Through this function, your implementation is required to receive and validate the names
//        of two countries from a user. The country names must be validated
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("Enter the name of the first country (type EXIT to quit): ");
            String firstCountry = reader.readLine().trim();

            if (firstCountry.equalsIgnoreCase("EXIT")) {
                break;
            }

            if (!isValidCountry(firstCountry)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }

            System.out.print("Enter the name of the second country (type EXIT to quit): ");
            String secondCountry = reader.readLine().trim();

            if (secondCountry.equalsIgnoreCase("EXIT")) {
                break;
            }
            if (!isValidCountry(secondCountry)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }

            String stateId1 = countryToStateIdMap.get(firstCountry);
            String stateId2 = countryToStateIdMap.get(secondCountry);

            if (stateId1 != null && stateId2 != null) {
                List<String> path = findPath(stateId1, stateId2);

                if (!path.isEmpty()) {
                    findPath(stateId1, stateId2);
                } else {
                    System.out.println("No path found between " + firstCountry + " and " + secondCountry);
                }
            } else {
                System.out.println("Invalid country names. Please enter valid country names.");
            }

        }
    }

    private boolean isValidCountry(String country) {
        // Check if the country exists in either bordersMap or distancesMap
        return bordersMap.containsKey(country);
    }

    public static void main(String[] args) throws IOException {
        IRoadTrip a3 = new IRoadTrip(args);

        a3.acceptUserInput();
    }

}

