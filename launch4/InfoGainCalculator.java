import java.io.*;
import java.util.*;

public class InfoGainCalculator {

    public static void main(String[] args) {
        String csvFile = "football_play_data.csv";
        String line;
        String csvSplitBy = ",";
        int total = 0;
        int yesCount = 0;
        Map<String, int[]> childCount;
        List<String> columnHeaders = new ArrayList<>();
        List<Double> infoGains = new ArrayList<>();
        List<String> columnsWithInfoGain = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String headerLine = br.readLine();
            if (headerLine != null) {
                columnHeaders = Arrays.asList(headerLine.split(csvSplitBy));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();  
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                total++;
                if (values[4].equals("Yes")) {
                    yesCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double parentEntropy = calculateEntropy(yesCount, total);

        for (int i = 0; i < columnHeaders.size() - 1; i++) {
            childCount = new HashMap<>();
            int columnTotal = 0;
            int columnYesCount = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                br.readLine();  
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(csvSplitBy);
                    String childName = values[i];

                    columnTotal++;
                    if (values[4].equals("Yes")) {
                        columnYesCount++;
                    }

                    childCount.putIfAbsent(childName, new int[2]);
                    if (values[4].equals("Yes")) {
                        childCount.get(childName)[0]++;
                    } else {
                        childCount.get(childName)[1]++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            double childEntropy = 0;
            System.out.println("\n=== Column: " + columnHeaders.get(i) + " ===");
            System.out.println("Parent Entropy: " + String.format("%.4f", parentEntropy));
            System.out.println("\n| Child Node | Yes Count | No Count | Total | Entropy |");
            System.out.println("-----------------------------------------------------");

            for (Map.Entry<String, int[]> entry : childCount.entrySet()) {
                int[] counts = entry.getValue();
                int childTotal = counts[0] + counts[1];
                double childNodeEntropy = calculateEntropy(counts[0], childTotal);
                childEntropy += ((double) childTotal / columnTotal) * childNodeEntropy;

                System.out.printf("| %-10s | %-9d | %-8d | %-5d | %-8.4f%n",
                        entry.getKey(), counts[0], counts[1], childTotal, childNodeEntropy);
            }

            System.out.println("-----------------------------------------------------");
            System.out.printf("Total Child Entropy: %.4f%n", childEntropy);

            double infoGain = parentEntropy - childEntropy;
            infoGains.add(infoGain);
            columnsWithInfoGain.add(columnHeaders.get(i));
            System.out.printf("Information Gain: %.4f%n", infoGain);
        }
        int maxIndex = infoGains.indexOf(Collections.max(infoGains));
        String bestColumn = columnsWithInfoGain.get(maxIndex);
        double bestInfoGain = infoGains.get(maxIndex);

        System.out.println("\n=== Column with Highest Information Gain ===");
        System.out.println("Column: " + bestColumn);
        System.out.printf("Information Gain: %.4f%n", bestInfoGain);
    }

    public static double calculateEntropy(int yesCount, int total) {
        if (total == 0) return 0;
        double pYes = (double) yesCount / total;
        double pNo = 1 - pYes;
        return -pYes * log2(pYes) - pNo * log2(pNo);
    }

    public static double log2(double value) {
        if (value == 0) return 0;
        return Math.log(value) / Math.log(2);
    }
}




Information Gain in data mining measures the reduction in entropy (uncertainty) when a dataset is split based on a particular feature. It helps identify the most informative feature for splitting the data.

Steps to Calculate Information Gain:

Calculate Parent Entropy: Compute the entropy for the entire dataset before the split, based on the distribution of the target variable (e.g., "Yes"/"No").

Formula for entropy:
Entropy(S)=-(pyesl og2(pyes)+pno log2(no))


Calculate Child Entropy: For each feature, compute the entropy of the resulting subgroups (child nodes) after the split. Each child group will have its own "Yes" and "No" counts.

Child entropy formula for each subgroup:
Entropy(S)=-(pyesl og2(pyes)+pno log2(no))

Calculate Information Gain: The information gain is the difference between the parent entropy and the weighted sum of the child entropies.

Formula for Information Gain:
infogain=entro(parent)-sum(schild/s * entro(child))



csvread-entropyscorere

inofgain(selectattribute)
