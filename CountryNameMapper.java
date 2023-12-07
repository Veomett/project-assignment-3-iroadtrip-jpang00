import java.util.HashMap;
import java.util.Map;

public class CountryNameMapper {
    private Map<String, String> countryNameMap;

    public CountryNameMapper() {
        // Initialize the map with variations and their corresponding country names
        countryNameMap = new HashMap<>();
        countryNameMap.put("usa", "United States");
        countryNameMap.put("united states", "United States");
        countryNameMap.put("us", "United States");
        countryNameMap.put("North Korea", "Korea, North");
        countryNameMap.put("South korea", "Korea, South");
        countryNameMap.put("Ivory Coast", "Cote d'Ivoire");
        countryNameMap.put("Czech", "Czech Republic");
        countryNameMap.put("Bosnia", "Bosnia and Herzegovina");
        countryNameMap.put("DRC", "Congo, Democratic Republic of the");
        countryNameMap.put("Democratic Republic of the Congo", "Congo, Democratic Republic of the");
        countryNameMap.put("Congo", "Congo, Republic of the");
        countryNameMap.put("Republic of the Congo", "Congo, Republic of the");

        // Add more entries as needed
    }

    public String getCountryName(String userInput) {
        // Convert user input to lowercase for case-insensitive comparison
        String lowerCaseInput = userInput.toLowerCase();

        // Check if the exact user input is in the map
        if (countryNameMap.containsKey(lowerCaseInput)) {
            return countryNameMap.get(lowerCaseInput);
        }

        // If the exact match is not found return null
        return null;
    }

}
