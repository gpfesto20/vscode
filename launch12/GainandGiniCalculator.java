import java.io.*;
import java.util.*;

public class GainandGiniCalculator {
    public static void main(String[] args) {
        String csvFile = "play_dataset.csv";
        String csvSplitBy = ",";
        List<String[]> dataset = new ArrayList<>();
        int total = 0;
        int yesCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                dataset.add(values);
                total++;

                if (values[4].equalsIgnoreCase("Play")) {
                    yesCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        double parentEntropy = calculateEntropy(yesCount, total);
        double parentGini = calculateGini(yesCount, total);

        System.out.printf("Parent Information Gain (Entropy): %.4f%n", parentEntropy);
        System.out.printf("Parent Gini Index: %.4f%n", parentGini);
        System.out.println("\n");

        List<AttributeResults> resultsList = new ArrayList<>();

        for (int attributeIndex = 0; attributeIndex < 4; attributeIndex++) {
            total = 0;
            yesCount = 0;
            Map<String, int[]> childCount = new HashMap<>();

            for (String[] values : dataset) {
                total++;

                if (values[4].equalsIgnoreCase("Play")) {
                    yesCount++;
                }

                if (attributeIndex == 2) {
                    int humidity = Integer.parseInt(values[2]);
                    values[2] = (humidity > 80) ? "High" : "Normal";
                } else if (attributeIndex == 1) {
                    int temperature = Integer.parseInt(values[1]);
                    values[1] = (temperature > 80) ? "High" : (temperature >= 70) ? "Mild" : "Low";
                }

                String childName = values[attributeIndex];
                childCount.putIfAbsent(childName, new int[2]);

                if (values[4].equalsIgnoreCase("Play")) {
                    childCount.get(childName)[0]++;
                } else {
                    childCount.get(childName)[1]++;
                }
            }

            double childEntropy = 0;
            double childGini = 0;

            System.out.printf("| %-15s | %-5s | %-10s | %-6s | %-10s | %-10s |%n", "Attribute Value", "Play", "Don't Play", "Total", "Entropy", "Gini Index");
            System.out.println("--------------------------------------------------------------------------");

            int grandTotal = 0;
            for (Map.Entry<String, int[]> entry : childCount.entrySet()) {
                String key = entry.getKey();
                int[] counts = entry.getValue();
                int childTotal = counts[0] + counts[1];
                grandTotal += childTotal;

                double entropy = calculateEntropy(counts[0], childTotal);
                double gini = calculateGini(counts[0], childTotal);
                double proportion = (double) childTotal / total;

                childEntropy += proportion * entropy;
                childGini += proportion * gini;

                System.out.printf("| %-15s | %-5d | %-10d | %-6d | %-10.4f | %-10.4f |%n", key, counts[0], counts[1], childTotal, entropy, gini);
            }

            System.out.println("--------------------------------------------------------------------------");
            System.out.printf("| %-15s | %-5s | %-10s | %-6d | %-10s | %-10s |%n", "Total", "", "", grandTotal, "", "");
            System.out.printf("%nChild Entropy: %.4f%n", childEntropy);
            System.out.printf("Information Gain: %.4f%n", parentEntropy - childEntropy);
            System.out.printf("Child Gini: %.4f%n", childGini);
            System.out.printf("Gini Gain: %.4f%n", parentGini - childGini);
            System.out.println("\n\n");

            resultsList.add(new AttributeResults(attributeIndex, parentEntropy, childEntropy, parentEntropy - childEntropy, parentGini, childGini));
        }

        AttributeResults bestAttribute = resultsList.stream()
                .max(Comparator.comparing(AttributeResults::getGiniGain))
                .orElse(null);

        if (bestAttribute != null) {
            System.out.printf("Best splitting criterion is Attribute %d with Gini Gain: %.4f%n",
                    bestAttribute.getAttributeIndex(), bestAttribute.getGiniGain());
        }
    }

    public static double calculateEntropy(int yesCount, int total) {
        if (total == 0) return 0;
        double pYes = (double) yesCount / total;
        double pNo = 1 - pYes;
        return -pYes * log2(pYes) - pNo * log2(pNo);
    }

    public static double calculateGini(int yesCount, int total) {
        if (total == 0) return 0;
        double pYes = (double) yesCount / total;
        double pNo = 1 - pYes;
        return 1 - (pYes * pYes + pNo * pNo);
    }

    public static double log2(double value) {
        return (value == 0) ? 0 : Math.log(value) / Math.log(2);
    }

    static class AttributeResults {
        private final int attributeIndex;
        private final double parentEntropy;
        private final double childEntropy;
        private final double infoGain;
        private final double parentGini;
        private final double childGini;

        public AttributeResults(int attributeIndex, double parentEntropy, double childEntropy, double infoGain, double parentGini, double childGini) {
            this.attributeIndex = attributeIndex;
            this.parentEntropy = parentEntropy;
            this.childEntropy = childEntropy;
            this.infoGain = infoGain;
            this.parentGini = parentGini;
            this.childGini = childGini;
        }

        public int getAttributeIndex() {
            return attributeIndex;
        }

        public double getGiniGain() {
            return parentGini - childGini;
        }
    }
}



Attribute Classification
In decision tree algorithms, attributes (or features) of the dataset are analyzed to determine the best attribute to split the data, creating the most homogenous subsets.
The algorithm evaluates each attribute and determines its potential to split the dataset into groups with minimized impurity (i.e., each subset should ideally contain records with similar outcomes, like all records indicating "Play" or "Don't Play").






Information Gain in data mining measures the reduction in entropy (uncertainty) when a dataset is split based on a particular feature. It helps identify the most informative feature for splitting the data.

Formula for entropy:
Entropy(S)=-(pyesl og2(pyes)+pno log2(no))

child entropy formula for each subgroup:
Entropy(S)=-(pyesl og2(pyes)+pno log2(no))

Formula for Information Gain:
infogain=entro(parent)-sum(schild/s * entro(child))



Gini Index
The Gini Index is an alternative metric to entropy for measuring impurity. It is widely used in the CART (Classification and Regression Trees) algorithm.
The Gini Index of a dataset measures the probability that a randomly selected record will be misclassified if it were labeled according to the overall distribution of classes in the subset:
 giniindex=1- (pyes2 + pno2)


A lower Gini Index indicates less impurity, making the split more desirable for achieving homogeneous groups in the tree.

Gini Gain
Gini Gain is similar to Information Gain but based on the Gini Index. It measures the reduction in the Gini Index after a split compared to the Gini Index before the split.
The Gini Gain for an attribute is calculated as:

Gini Gain=Parent Gini−∑(proportion of subset)×Gini Index of subset


    decisontreelearn

    classify(j48)
