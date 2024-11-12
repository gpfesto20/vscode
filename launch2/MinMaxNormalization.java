import java.io.*;
import java.util.*;
import java.nio.file.*;

public class MinMaxNormalization {
    public static List<String> split(String line, char delimiter) {
        List<String> result = new ArrayList<>();
        String[] tokens = line.split(String.valueOf(delimiter));
        result.addAll(Arrays.asList(tokens));
        return result;
    }

    public static void main(String[] args) {
        double mini, maxi, newMini, newMaxi;
        List<Double> voltageData = new ArrayList<>();

        try (BufferedReader in = Files.newBufferedReader(Paths.get("Dataset.csv"));
             BufferedWriter out = Files.newBufferedWriter(Paths.get("MinMaxNormalization.csv"))) {

            String line = in.readLine();
            out.write(line);
            out.newLine();

            while ((line = in.readLine()) != null) {
                List<String> fields = split(line, ',');
                double voltage = Double.parseDouble(fields.get(0));
                voltageData.add(voltage);
            }

            mini = Collections.min(voltageData);
            maxi = Collections.max(voltageData);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter new min: ");
            newMini = scanner.nextDouble();
            System.out.print("Enter new max: ");
            newMaxi = scanner.nextDouble();

            in.close();

            int index = 0;
            BufferedReader inAgain = Files.newBufferedReader(Paths.get("Mine_Dataset.csv"));
            inAgain.readLine(); 

            while ((line = inAgain.readLine()) != null) {
                List<String> fields = split(line, ',');

                double voltage = voltageData.get(index++);
                double normalizedVoltage = ((voltage - mini) / (maxi - mini)) * (newMaxi - newMini) + newMini;

                out.write(normalizedVoltage + "," + fields.get(1) + "," + fields.get(2) + "," + fields.get(3));
                out.newLine();
            }

            System.out.println("Min-Max Normalization applied successfully!");

        } catch (Exception e) {
            System.out.println("Error opening input file");
        }
    }
}





Min-max normalization is a technique used to scale data to a specific range, usually between 0 and 1. This method adjusts the values in a dataset so that they fit within this range while preserving the relationships between them. It's useful in data mining and machine learning to prevent certain features from dominating others due to differences in scale.

The formula for min-max normalization is:
X′ =(X-minx/maxx- minx)*(new_max−new_min)+new_min