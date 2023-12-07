import java.util.*;
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

        countryToStateIdMap.put("United States", "USA"); // Replace with actual state ID
        countryToStateIdMap.put("United States of America", "USA");
        countryToStateIdMap.put("US", "USA");
        countryToStateIdMap.put("Bosnia", "BOS");
        countryToStateIdMap.put("Italy", "ITA");
        countryToStateIdMap.put("Czech", "CZR");
        countryToStateIdMap.put("Germany", "GFR");
        countryToStateIdMap.put("North Korea", "PRK");
        countryToStateIdMap.put("South Korea", "ROK");
        countryToStateIdMap.put("Korea", "ROK");

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
                String neighbor = parts[3].trim();         //2nd Country's stateID
                int distance = Integer.parseInt(parts[4]); // distance is in the 5th column

                // If the country is not in the countryDataMap, add it
                distancesMap.putIfAbsent(country, new HashMap<>());
                distancesMap.putIfAbsent(neighbor, new HashMap<>());

                // Add the distance to the neighbor
                distancesMap.get(country).put(neighbor, distance);
                distancesMap.get(neighbor).put(country, distance);
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
//        This function provides the shortest path distance between
//         the capitals of the two countries passed as arguments.
//        If either of the countries does not exist or if the countries do not share a land border,
//         this function must return a value of -1

        // Check if both countries exist
        if (!bordersMap.containsKey(country1) || !bordersMap.containsKey(country2)) {
            return -1; // One or both countries do not exist
        }

        // Check if the countries share a land border
        Map<String, Integer> country1Borders = bordersMap.get(country1);
        if (!country1Borders.containsKey(country2)) {
            return -1; // Countries do not share a land border
        }

        // Retrieve the distance from distancesMap
        int distance = distancesMap.get(country1).get(country2);

        return distance;
    }


    public List<String> findPath (String country1, String country2) {
//        This function determines and returns the shortest path between the two countries passed as arguments
//         (starting in the capital of country1, ending in the capital of country1,
//          and going through the capitals of each country along the way).

        System.out.println("Country1: " + country1);
        System.out.println("Country2: " + country2);

        if (!bordersMap.containsKey(country1) || !bordersMap.containsKey(country2)) {   //if country not found
            return Collections.emptyList();
        }

        Map<String, Integer> distanceMap = new HashMap<>();
        Map<String, String> previousMap = new HashMap<>();
        PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distanceMap::get));

        // Initialization
        for (String country : bordersMap.keySet()) {
            distanceMap.put(country, Integer.MAX_VALUE);
            //debugging
            System.out.println(priorityQueue);
            previousMap.put(country, null);
        }
        distanceMap.put(country1, 0);
        priorityQueue.add(country1);

        // Dijkstra's algorithm main loop
        while (!priorityQueue.isEmpty()) {
            String currentCountry = priorityQueue.poll();

            // Debugging output
            System.out.println("Processing: " + currentCountry);
            // Check if the currentCountry is in distancesMap
            if (!distancesMap.containsKey(currentCountry)) {
                continue;
            }

            for (String neighbor : distancesMap.get(currentCountry).keySet()) {
                // Check if the neighbor is in distancesMap

                // Debugging
                System.out.println("Neighbor: " + neighbor);
                if (!distancesMap.containsKey(neighbor)) {
                    continue;
                }

                int currentToNeighborDistance = getDistance(currentCountry, neighbor);

                if (currentToNeighborDistance != -1) {
                    int newDistance = distanceMap.get(currentCountry) + currentToNeighborDistance;

                    //Debugging
                    System.out.println("New Distance: " + newDistance);

                    if (newDistance < distanceMap.get(neighbor)) {
                        System.out.println(neighbor);
                        distanceMap.put(neighbor, newDistance);
                        previousMap.put(neighbor, currentCountry);
                        priorityQueue.add(neighbor);
                    }
                }
            }
        }

        // Reconstruct the path
        List<String> path = new ArrayList<>();
        String current = country2;
        while (current != null) {
            path.add(current);
            current = previousMap.get(current);
        }
        Collections.reverse(path);

        // Construct the result list with distance information
        List<String> result = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            String start = path.get(i);
            String end = path.get(i + 1);
            result.add(start + " --> " + end + " (" + distancesMap.get(start).get(end) + " km.)");
        }

        return result.size() > 1 ? result : Collections.emptyList(); // Return empty list if no valid path found
    }


    public void acceptUserInput() throws IOException {
//       This function allows a user to interact with your implementation
//        on the console. Through this function, your implementation is required to receive and validate the names
//        of two countries from a user. The country names must be validated
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the name of the first country (type EXIT to quit): ");
            String input1 = scanner.nextLine().trim();

            if (input1.equalsIgnoreCase("EXIT")) {
                // User wants to exit
                break;
            }

            // Validate input1 against bordersMap
            if (!countryToStateIdMap.containsKey(input1)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }

            System.out.print("Enter the name of the second country (type EXIT to quit): ");
            String input2 = scanner.nextLine().trim();

            if (input2.equalsIgnoreCase("EXIT")) {
                break;
            }

            // Validate input2 against bordersMap
            if (!countryToStateIdMap.containsKey(input2)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }
            List<String> path = findPath(input1, input2);
            if (path.isEmpty()) {
                System.out.println("No valid path found.");
            } else {
                System.out.println("Quickest Path:");
                for (String step : path) {
                    System.out.println(step);
                }
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

