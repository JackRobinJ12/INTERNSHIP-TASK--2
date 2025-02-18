import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class WeatherApp {

    // Replace with your WeatherAPI.com key
    private static final String API_KEY = "a393efec1d7446b0ac1133257251602";
    private static final String API_URL = "https://api.weatherapi.com/v1/current.json?key=%s&q=%s";

    public static void main(String[] args) {
        // Ask the user for the city name
        System.out.print("Enter the city name: ");
        String city = new java.util.Scanner(System.in).nextLine();

        try {
            // Fetch weather data
            String weatherData = fetchWeatherData(city);

            // Parse and display the weather data
            displayWeatherData(weatherData);
        } catch (IOException e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
        }
    }

    // Method to fetch weather data from WeatherAPI.com
    private static String fetchWeatherData(String city) throws IOException {
        // Encode the city name to handle spaces (e.g., "New York" -> "New%20York")
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

        // Construct the final API URL correctly
        String urlString = String.format(API_URL, API_KEY, encodedCity);
        System.out.println("Fetching data from: " + urlString); // Debugging output

        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Check if response code is not 200 (OK)
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Server returned HTTP response code: " + responseCode);
        }

        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    // Method to parse and display weather data
    private static void displayWeatherData(String weatherData) {
        JSONObject json = new JSONObject(weatherData);

        // Extract relevant data from WeatherAPI response
        String cityName = json.getJSONObject("location").getString("name");
        String country = json.getJSONObject("location").getString("country");
        double temperature = json.getJSONObject("current").getDouble("temp_c");
        int humidity = json.getJSONObject("current").getInt("humidity");
        String weatherDescription = json.getJSONObject("current").getJSONObject("condition").getString("text");

        // Display the data in a structured format
        System.out.println("\nWeather Data for " + cityName + ", " + country + ":");
        System.out.println("---------------------------------");
        System.out.println("Temperature: " + temperature + " °C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Weather: " + weatherDescription);
        System.out.println("---------------------------------");
    }
}
