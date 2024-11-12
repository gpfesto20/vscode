import java.io.*;
import java.util.*;

public class DataBinning {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String csvFile = "House_Prices_Dataset.csv";
        int columnIndex = 3;
        System.out.print("Enter the number of bins you want to create: ");
        int numBins = scanner.nextInt();

        System.out.println("\nChoose the type of binning you want to perform:");
        System.out.println("1. Equal Frequency Binning");
        System.out.println("2. Equal Width Binning");
        System.out.print("Your choice (1 or 2): ");
        int binningChoice = scanner.nextInt();
        System.out.println();

        List<Double> dataValues = readDataFromCsv(csvFile, columnIndex);
        if (dataValues.isEmpty()) {
            System.out.println("No data available for binning.");
            return;
        }

        Collections.sort(dataValues);
        List<List<Double>> bins;

        if (binningChoice == 1) {
            bins = equalFrequencyBinning(dataValues, numBins);
        } else if (binningChoice == 2) {
            bins = equalWidthBinning(dataValues, numBins);
        } else {
            System.out.println("Invalid choice. Exiting.");
            return;
        }

        System.out.println("Binning results:");
        for (int binCount = 0; binCount < bins.size(); binCount++) {
            List<Double> bin = bins.get(binCount);
            System.out.println("Bin " + (binCount + 1) + " contents: " + bin);
            System.out.printf("Smoothing by Bin Mean: %.2f%n", smoothByMean(bin));
            System.out.printf("Smoothing by Bin Median: %.2f%n", smoothByMedian(bin));
            System.out.printf("Smoothing by Boundaries: %s%n", smoothByBoundaries(bin));
            System.out.println("--------------------------------------------");
        }
        scanner.close();
    }

    private static List<Double> readDataFromCsv(String csvFile, int columnIndex) {
        List<Double> dataValues = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > columnIndex) {
                    try {
                        dataValues.add(Double.parseDouble(values[columnIndex]));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return dataValues;
    }

    private static List<List<Double>> equalFrequencyBinning(List<Double> dataValues, int numBins) {
        List<List<Double>> bins = new ArrayList<>();
        int binSize = dataValues.size() / numBins;
        for (int i = 0; i < dataValues.size(); i += binSize) {
            int end = Math.min(i + binSize, dataValues.size());
            bins.add(new ArrayList<>(dataValues.subList(i, end)));
        }
        return bins;
    }

    private static List<List<Double>> equalWidthBinning(List<Double> dataValues, int numBins) {
        List<List<Double>> bins = new ArrayList<>();
        double min = Collections.min(dataValues);
        double max = Collections.max(dataValues);
        double binWidth = (max - min) / numBins;

        for (int i = 0; i < numBins; i++) {
            double binLowerBound = min + i * binWidth;
            double binUpperBound = min + (i + 1) * binWidth;
            List<Double> bin = new ArrayList<>();
            for (Double value : dataValues) {
                if (value >= binLowerBound && value < binUpperBound) {
                    bin.add(value);
                }
            }
            bins.add(bin);
        }
        bins.get(bins.size() - 1).add(max);
        return bins;
    }

    private static double smoothByMean(List<Double> bin) {
        return bin.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private static double smoothByMedian(List<Double> bin) {
        Collections.sort(bin);
        int size = bin.size();
        return size % 2 == 0
                ? (bin.get(size / 2 - 1) + bin.get(size / 2)) / 2.0
                : bin.get(size / 2);
    }

    private static List<Double> smoothByBoundaries(List<Double> bin) {
        double min = Collections.min(bin);
        double max = Collections.max(bin);
        List<Double> boundarySmoothBin = new ArrayList<>();
        for (Double value : bin) {
            boundarySmoothBin.add(Math.abs(value - min) < Math.abs(value - max) ? min : max);
        }
        return boundarySmoothBin;
    }
}


Binning is a data preprocessing technique used to smooth or group a continuous set of data values into a smaller number of "bins" or intervals. This approach helps reduce noise, manage large datasets, and improve the performance of machine learning algorithms by reducing the dimensionality of continuous features.

Types of Binning
The code above uses two main binning techniques:

Equal Frequency Binning:

Divides data into bins that each contain approximately the same number of data points. This method ensures each bin has a balanced amount of data but may result in bins with varying ranges (intervals) in the data values.
Formula: Divide sorted data points into 𝑛 bins, each containing approximately 
total number of data points/𝑛

For example, if you have 100 values and 4 bins, each bin will contain about 25 values.

Equal Width Binning:
Divides the range of data values into bins of equal width, resulting in bins with the same interval size but potentially differing numbers of data points.
Formula: 
Bin Width=Max Value−Min Value/Number of Bins

For example, with a data range of 0 to 100 and 4 bins, each bin would have a width of 25.
Smoothing Techniques
The code provides three methods to smooth the values within each bin:

Smoothing by Mean: Replaces all values in a bin with the mean of that bin.
Smoothing by Median: Replaces all values in a bin with the median value of that bin.
Smoothing by Boundaries: Replaces each value in the bin with the closest boundary value (either the bin’s minimum or maximum).



csv-autobin

discretize(preprocess)