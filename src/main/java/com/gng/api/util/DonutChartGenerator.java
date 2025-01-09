package com.gng.api.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.style.PieStyler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonutChartGenerator {

    public static void main(String[] args) {
        String jsonPath = System.getProperty("user.dir") + "\\target\\allure-report\\widgets\\status-chart.json";

        try {
            // Read the JSON data
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> data = gson.fromJson(new FileReader(jsonPath), listType);

            // Validate if data is null or empty
            if (data == null || data.isEmpty()) {
                System.err.println("No data found in the JSON file.");
                return;
            }

            // Count occurrences of each status and calculate total duration
            Map<String, Integer> statusCounts = new HashMap<>();
            long totalDurationMillis = 0;
            for (Map<String, Object> item : data) {
                String status = (String) item.get("status");
                statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
                Map<String, Number> time = (Map<String, Number>) item.get("time");
                totalDurationMillis += time.get("duration").longValue();
            }

            // Calculate totals and percentages
            int totalCases = data.size();
            int passedCases = statusCounts.getOrDefault("passed", 0);
            int failedCases = statusCounts.getOrDefault("failed", 0);
            int otherCases = totalCases - passedCases - failedCases;
            double passedPercentage = (passedCases / (double) totalCases) * 100;
            double failedPercentage = (failedCases / (double) totalCases) * 100;
            double otherPercentage = (otherCases / (double) totalCases) * 100;

            // Create a donut chart with a custom color scheme
            PieChart chart = new PieChartBuilder().width(1100).height(500).title("Test Results").build();
            chart.getStyler().setLegendVisible(true);
            chart.getStyler().setPlotContentSize(0.75);
            chart.getStyler().setCircular(true);
            chart.getStyler().setAnnotationType(PieStyler.AnnotationType.LabelAndPercentage);
            chart.getStyler().setAnnotationDistance(1.15);
            chart.getStyler().setDrawAllAnnotations(true);
            chart.getStyler().setPlotBackgroundColor(Color.WHITE);
            chart.getStyler().setChartBackgroundColor(Color.WHITE);
            chart.getStyler().setDonutThickness(0.2); // Ensure the thickness is less than 1 to create a donut chart
            chart.getStyler().setDecimalPattern("#.00");
            chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 20));
            chart.getStyler().setLegendFont(new Font("Arial", Font.PLAIN, 16));

            // Set custom colors based on status
            Map<String, Color> statusColorMap = new HashMap<>();
            statusColorMap.put("passed", new Color(0, 153, 76)); // Green for passed
            statusColorMap.put("failed", new Color(204, 0, 0)); // Red for failed
            statusColorMap.put("other", new Color(255, 165, 0));  // Orange for other statuses

            // Add series to the chart with corresponding colors
            for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
                String status = entry.getKey();
                int count = entry.getValue();
                Color color = statusColorMap.getOrDefault(status, statusColorMap.get("other"));
                chart.addSeries(status + " (" + count + ")", count).setFillColor(color);
            }

            // Create a buffered image to draw on
            BufferedImage bufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedImage.createGraphics();

            // Render the chart onto the buffered image
            chart.paint(g2d, chart.getWidth(), chart.getHeight());

            // Add annotations for summary details
            int y = 50; // Starting y position for annotations
            int step = 20; // Vertical spacing between annotations
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("Date: " + LocalDate.now(), 10, y);
            y += step;
            g2d.drawString("Total Cases: " + totalCases, 10, y);
            y += step;
            g2d.drawString("Passed Cases: " + passedCases + " (" + String.format("%.2f", passedPercentage) + "%)", 10, y);
            y += step;
            g2d.drawString("Failed Cases: " + failedCases + " (" + String.format("%.2f", failedPercentage) + "%)", 10, y);
            y += step;
            g2d.drawString("Other Cases: " + otherCases + " (" + String.format("%.2f", otherPercentage) + "%)", 10, y);
            y += step;
            g2d.drawString("Total Time Taken: " + formatDuration(totalDurationMillis), 10, y);

            // Dispose graphics resources
            g2d.dispose();

            // Construct the path to save the image in the target folder
            String outputImagePath = Paths.get(System.getProperty("user.dir"), "target/allure-report", "donutChartImage.png").toString();

            // Save the buffered image as a PNG file using ImageIO
            ImageIO.write(bufferedImage, "PNG", Paths.get(outputImagePath).toFile());
            System.out.println("Donut chart generated successfully and saved to " + outputImagePath);

        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static String formatDuration(long millis) {
        long seconds = millis / 1000;
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
}
