import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;

public class WeatherApp {
    private JFrame frame;
    private JTextField locationField;
    private JTextArea weatherInfoArea;
    private JLabel iconLabel;
    private JLabel forecastLabel;
    private JComboBox<String> unitComboBox;
    private DefaultListModel<String> searchHistoryModel;
    private JList<String> searchHistoryList;
    private List<String> searchHistory;
    private String previousDay = "";

    public WeatherApp() {
        frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Custom panel with gradient background
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(0x0E4D92); // Dark Blue
                Color color2 = new Color(0xADD8E6); // Light Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title label
        JLabel titleLabel = new JLabel("Welcome to Weather Information App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        mainPanel.add(titleLabel, gbc);

        // Location input field
        locationField = new JTextField();
        locationField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(locationField, gbc);

        // Button to get weather information
        JButton getWeatherButton = new JButton("Search");
        getWeatherButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        getWeatherButton.setBackground(new Color(0xFFD700)); // Yellow color
        getWeatherButton.setForeground(Color.BLACK);
        getWeatherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String location = locationField.getText();
                getWeather(location);
            }
        });
        gbc.gridx = 2;
        gbc.weightx = 0;
        mainPanel.add(getWeatherButton, gbc);

        // Label to display weather icon
        iconLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(iconLabel, gbc);

        // Text area to display weather information
        weatherInfoArea = new JTextArea();
        weatherInfoArea.setEditable(false);
        weatherInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(weatherInfoArea);
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        // Combo box to select temperature unit
        unitComboBox = new JComboBox<>(new String[]{"Celsius", "Fahrenheit"});
        unitComboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        unitComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getWeather(locationField.getText());
            }
        });
        gbc.gridy = 4;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(unitComboBox, gbc);

        // List to display search history
        searchHistoryModel = new DefaultListModel<>();
        searchHistoryList = new JList<>(searchHistoryModel);
        searchHistoryList.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchHistoryList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = searchHistoryList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String location = searchHistoryModel.getElementAt(index);
                        locationField.setText(location);
                        getWeather(location);
                    }
                }
            }
        });
        searchHistory = new ArrayList<>();
        JScrollPane historyScrollPane = new JScrollPane(searchHistoryList);
        historyScrollPane.setBorder(BorderFactory.createTitledBorder("Search History"));
        gbc.gridy = 5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(historyScrollPane, gbc);

        // Label to display weather forecast
        forecastLabel = new JLabel();
        forecastLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JScrollPane forecastScrollPane = new JScrollPane(forecastLabel);
        forecastScrollPane.setBorder(BorderFactory.createTitledBorder("8-day Forecast"));
        gbc.gridy = 6;
        gbc.weighty = 0.5;
        mainPanel.add(forecastScrollPane, gbc);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    // Method to fetch weather data for a given location
    private void getWeather(String location) {
        try {
            JSONObject weatherData = WeatherAPIService.getWeather(location);
            displayWeatherInfo(weatherData);
            if (!searchHistory.contains(location)) {
                searchHistory.add(location);
                searchHistoryModel.addElement(location);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to get weather data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to display weather information on UI
    private void displayWeatherInfo(JSONObject weatherData) {
        try {
            // Displaying weather icon
            String iconCode = weatherData.getJSONArray("weather").getJSONObject(0).getString("icon");
            String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";
            ImageIcon icon = new ImageIcon(new URL(iconUrl));
            iconLabel.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Constructing weather information string
        String currentWeatherInfo = "Location: " + weatherData.getString("name") + "\n" +
                "Temperature: " + convertTemperature(weatherData.getJSONObject("main").getDouble("temp")) + "°" + getUnit() + "\n" +
                "Humidity: " + weatherData.getJSONObject("main").getInt("humidity") + "%\n" +
                "Wind Speed: " + weatherData.getJSONObject("wind").getDouble("speed") + " m/s\n" +
                "Conditions: " + weatherData.getJSONArray("weather").getJSONObject(0).getString("description");

        // Displaying weather information in text area
        weatherInfoArea.setText(currentWeatherInfo);

        try {
            // Fetching and displaying weather forecast
            JSONObject forecastData = WeatherAPIService.getForecast(locationField.getText());
            displayWeatherForecast(forecastData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to display weather forecast on UI
    private void displayWeatherForecast(JSONObject forecastData) {
        try {
            JSONArray forecastArray = forecastData.getJSONArray("list");
            List<JSONObject> sortedForecast = new ArrayList<>();
            for (int i = 0; i < forecastArray.length(); i++) {
                sortedForecast.add(forecastArray.getJSONObject(i));
            }
            Collections.sort(sortedForecast, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String timeA = a.getString("dt_txt");
                    String timeB = b.getString("dt_txt");
                    return timeA.compareTo(timeB);
                }
            });
            StringBuilder forecastInfo = new StringBuilder("<html>");
            for (JSONObject forecast : sortedForecast) {
                String dateTime = forecast.getString("dt_txt");
                double temp = forecast.getJSONObject("main").getDouble("temp");
                String description = forecast.getJSONArray("weather").getJSONObject(0).getString("description");
                String day = getDayOfWeek(dateTime);
                if (!day.equals(previousDay)) {
                    forecastInfo.append("<b>").append(day).append("</b>: ").append(formatForecast(dateTime, temp, description)).append("<br>");
                    previousDay = day;
                }
            }
            forecastInfo.append("</html>");
            forecastLabel.setText(forecastInfo.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to format weather forecast
    private String formatForecast(String dateTime, double temp, String description) {
        // Extracting day and temperature
        String[] dateTimeParts = dateTime.split(" ");
        String[] dateParts = dateTimeParts[0].split("-");
        String day = getDayOfWeek(dateParts[2], dateParts[1], dateParts[0]);
        String temperature = String.format("%.0f", temp);

        // Formatting the forecast
        return String.format("<b>%s</b>: %s°C %s", day, temperature, description);
    }

    // Method to get day of the week from date
    private String getDayOfWeek(String day, String month, String year) {
        // Parsing the date to get the day of the week
        int dayOfMonth = Integer.parseInt(day);
        int monthOfYear = Integer.parseInt(month);
        int yearNum = Integer.parseInt(year);

        // Creating a calendar instance
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearNum, monthOfYear - 1, dayOfMonth); // month is zero-based

        // Getting the day of the week
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Mapping the day of the week to a string representation
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sun";
            case Calendar.MONDAY:
                return "Mon";
            case Calendar.TUESDAY:
                return "Tue";
            case Calendar.WEDNESDAY:
                return "Wed";
            case Calendar.THURSDAY:
                return "Thu";
            case Calendar.FRIDAY:
                return "Fri";
            case Calendar.SATURDAY:
                return "Sat";
            default:
                return "";
        }
    }

    // Method to get day of the week from date
    private String getDayOfWeek(String dateTime) {
        // Parsing the date to get the day of the week
        String[] dateTimeParts = dateTime.split(" ");
        String[] dateParts = dateTimeParts[0].split("-");
        String day = dateParts[2];
        return day;
    }

    // Method to convert temperature based on selected unit
    private double convertTemperature(double tempInCelsius) {
        if (unitComboBox.getSelectedItem().equals("Fahrenheit")) {
            return tempInCelsius * 9 / 5 + 32;
        }
        return tempInCelsius;
    }

    // Method to get temperature unit
    private String getUnit() {
        return unitComboBox.getSelectedItem().equals("Fahrenheit") ? "F" : "C";
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WeatherApp();
            }
        });
    }
}
