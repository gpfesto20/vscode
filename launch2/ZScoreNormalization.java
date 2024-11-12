import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ZScoreNormalization {

    public static List<String> split(String line, char delimiter) {
        List<String> result = new ArrayList<>();
        String[] tokens = line.split(String.valueOf(delimiter));
        result.addAll(Arrays.asList(tokens));
        return result;
    }

    public static List<Double> zScoreNormalization(List<Double> data) {
        double sum = 0, mean, stddev = 0;

        for (double val : data) {
            sum += val;
        }
        mean = sum / data.size();

        for (double val : data) {
            stddev += Math.pow(val - mean, 2);
        }
        stddev = Math.sqrt(stddev / data.size());

        List<Double> zScoreData = new ArrayList<>();
        for (double val : data) {
            double zScoreVal = (val - mean) / stddev;
            zScoreData.add(zScoreVal);
        }

        return zScoreData;
    }

    public static void main(String[] args) {
        List<Double> voltageData = new ArrayList<>();

        try (BufferedReader in = Files.newBufferedReader(Paths.get("Mine_Dataset.csv"));
             BufferedWriter out = Files.newBufferedWriter(Paths.get("ZscoreNormalization.csv"))) {

            String line = in.readLine();
            out.write(line);
            out.newLine();

            while ((line = in.readLine()) != null) {
                List<String> fields = split(line, ',');
                double voltage = Double.parseDouble(fields.get(0));
                voltageData.add(voltage);
            }

            List<Double> zScoreNormalized = zScoreNormalization(voltageData);

            in.close();
            BufferedReader inAgain = Files.newBufferedReader(Paths.get("Mine_Dataset.csv"));
            inAgain.readLine();

            int index = 0;
            while ((line = inAgain.readLine()) != null) {
                List<String> fields = split(line, ',');

                double normalizedVoltage = zScoreNormalized.get(index++);
                out.write(normalizedVoltage + "," + fields.get(1) + "," + fields.get(2) + "," + fields.get(3));
                out.newLine();
            }

            System.out.println("Z-Score Normalization applied successfully");

        } catch (Exception e) {
            System.out.println("Error!");
        }
    }
}


Z-score normalization (also called standardization) is a technique used to rescale data by centering it around the mean and adjusting it based on the standard deviation. This method transforms data into a distribution with a mean of 0 and a standard deviation of 1, which is useful for comparing data points that come from different distributions or scales. Z-score normalization is widely used in data mining and machine learning, especially when the algorithm assumes normally distributed data.

The formula for Z-score normalization is:
Z= X−μ / σ


X is the original value,
μ is the mean of the data,
σ is the standard deviation of the data


csvread-filter-normalizer-csvwrite

​
