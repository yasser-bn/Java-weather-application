import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherAPIService {
    private static final String API_KEY = "2930539f7a7d16cdfe0b280b8ddbf1ee"; // Replace with your OpenWeatherMap API key
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5";

    public static JSONObject getWeather(String location) throws Exception {
        String urlString = BASE_URL + "/weather?q=" + location + "&appid=" + API_KEY + "&units=metric";
        return getResponse(urlString);
    }

    public static JSONObject getForecast(String location) throws Exception {
        String urlString = BASE_URL + "/forecast?q=" + location + "&appid=" + API_KEY + "&units=metric";
        return getResponse(urlString);
    }

    private static JSONObject getResponse(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();
        return new JSONObject(content.toString());
    }
}
