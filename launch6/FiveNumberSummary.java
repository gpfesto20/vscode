import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FiveNumberSummary {

    public static List<String> splitData(String line, char delim) {
        return Arrays.asList(line.split(String.valueOf(delim)));
    }

    public static double findMedian(List<Double> sortedData) {
        int size = sortedData.size();
        if (size % 2 == 0) {
            return (sortedData.get(size / 2 - 1) + sortedData.get(size / 2)) / 2.0;
        } else {
            return sortedData.get(size / 2);
        }
    }

    public static List<Double> calcQuartiles(List<Double> sortedData) {
        List<Double> quarts = new ArrayList<>();
        int size = sortedData.size();

        double med = findMedian(sortedData);

        List<Double> lower = sortedData.subList(0, size / 2);
        double q1 = findMedian(lower);

        List<Double> upper = (size % 2 == 0) ? sortedData.subList(size / 2, size) : sortedData.subList(size / 2 + 1, size);
        double q3 = findMedian(upper);

        quarts.add(q1);  
        quarts.add(med);  
        quarts.add(q3);  
        return quarts;
    }

    public static void main(String[] args) {
        List<Double> data = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("Dataset.csv"))) {
            String line = reader.readLine();  
            while ((line = reader.readLine()) != null) {
                List<String> fields = splitData(line, ',');
                if (fields.isEmpty()) {
                    System.out.println("Empty line encountered, skipping.");
                    continue;
                }
                data.add(Double.parseDouble(fields.get(0)));
            }

            Collections.sort(data);

            double minVal = data.get(0);
            double maxVal = data.get(data.size() - 1);
            List<Double> quarts = calcQuartiles(data);

            // Calculate IQR
            double q1 = quarts.get(0);
            double q3 = quarts.get(2);
            double iqr = q3 - q1;

            // Calculate lower and upper bounds for outliers
            double lowerBound = q1 - 1.5 * iqr;
            double upperBound = q3 + 1.5 * iqr;

            // Find outliers
            List<Double> outliers = new ArrayList<>();
            for (Double value : data) {
                if (value < lowerBound || value > upperBound) {
                    outliers.add(value);
                }
            }

            // Output the 5-number summary, IQR, and outliers
            System.out.println("5-Number Summary:");
            System.out.println("Min: " + minVal);
            System.out.println("Q1: " + q1);
            System.out.println("Median: " + quarts.get(1));
            System.out.println("Q3: " + q3);
            System.out.println("Max: " + maxVal);
            System.out.println("IQR: " + iqr);
            System.out.println("Lower Bound for Outliers: " + lowerBound);
            System.out.println("Upper Bound for Outliers: " + upperBound);

            // Print outliers
            System.out.println("Outliers: " + outliers);

        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}


The 5-number summary is a set of descriptive statistics that provides a quick overview of a dataset. It includes the following five values:

Minimum (Min): The smallest value in the dataset.
First Quartile (Q1): The median of the lower half of the dataset (25th percentile).
Median: The middle value of the dataset (50th percentile).
Third Quartile (Q3): The median of the upper half of the dataset (75th percentile).
Maximum (Max): The largest value in the dataset.
To calculate the 5-number summary, you need to:

Sort the data.
Find the Median: This splits the data into two halves.
Find Q1 and Q3: These are the medians of the lower and upper halves, respectively.
Find the Min and Max: The smallest and largest values.
Formula for Outliers (based on IQR):
IQR (Interquartile Range) = Q3 - Q1
Lower Bound for Outliers = Q1 - 1.5 * IQR
Upper Bound for Outliers = Q3 + 1.5 * IQR
Any data points outside these bounds are considered outliers.
This allows you to summarize the data's spread and identify potential outliers.


csvread->boxplot