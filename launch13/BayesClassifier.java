import java.io.*;
import java.util.*;

public class BayesClassifier {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the weather conditions for a new case:");

        System.out.print("Outlook (Sunny, Overcast, Rain): ");
        String outlook = scanner.nextLine();

        System.out.print("Temperature (Hot, Mild, Cool): ");
        String temperature = scanner.nextLine();

        System.out.print("Humidity (High, Normal): ");
        String humidity = scanner.nextLine();

        System.out.print("Wind (Weak, Strong): ");
        String wind = scanner.nextLine();

        String[] newCase = {outlook, temperature, humidity, wind};

        String csvFile = "football_play_data.csv";
        String csvSplitBy = ",";
        List<String[]> dataset = new ArrayList<>();
        int total = 0;
        int yesCount = 0;
        int noCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                dataset.add(values);
                total++;

                if (values[4].equalsIgnoreCase("Yes")) {
                    yesCount++;
                } else {
                    noCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        double pYes = (double) yesCount / total;
        double pNo = (double) noCount / total;

        System.out.printf("Calculated Prior Probabilities:%n");
        System.out.printf("P(Yes) = %.4f%n", pYes);
        System.out.printf("P(No) = %.4f%n", pNo);

        System.out.println("\nLikelihoods for Each Attribute:");
        System.out.printf("| %-11s | %-16s | %-15s |%n", "Attribute", "Likelihood (Yes)", "Likelihood (No)");
        System.out.println("---------------------------------------------");

        for (int i = 0; i < newCase.length; i++) {
            double likelihoodYes = calculateAttributeLikelihood(newCase[i], dataset, i, "Yes");
            double likelihoodNo = calculateAttributeLikelihood(newCase[i], dataset, i, "No");

            System.out.printf("| %-11s | %-16.6f | %-15.6f |%n", newCase[i], likelihoodYes, likelihoodNo);
        }

        double yesPosterior = pYes * calculateLikelihood(newCase, dataset, "Yes");
        double noPosterior = pNo * calculateLikelihood(newCase, dataset, "No");

        System.out.printf("%nCalculated Posterior Probabilities:%n");
        System.out.printf("P(Yes|New Case) = %.6f%n", yesPosterior);
        System.out.printf("P(No|New Case) = %.6f%n", noPosterior);

        if (yesPosterior > noPosterior) {
            System.out.println("\nClassified as: Yes (Football will be played)");
        } else {
            System.out.println("\nClassified as: No (Football will not be played)");
        }
    }

    public static double calculateAttributeLikelihood(String attribute, List<String[]> dataset, int attributeIndex, String classLabel) {
        int totalInClass = 0;
        int attributeMatchCount = 0;

        for (String[] data : dataset) {
            if (data[4].equalsIgnoreCase(classLabel)) {
                totalInClass++;
                if (data[attributeIndex].equalsIgnoreCase(attribute)) {
                    attributeMatchCount++;
                }
            }
        }

        return (double) attributeMatchCount / totalInClass;
    }

    public static double calculateLikelihood(String[] newCase, List<String[]> dataset, String classLabel) {
        double likelihood = 1.0;

        for (int i = 0; i < newCase.length; i++) {
            likelihood *= calculateAttributeLikelihood(newCase[i], dataset, i, classLabel);
        }

        return likelihood;
    }
}


**Theory:**

**Bayes’ theorem:**  
Bayes’ theorem calculates the probability of a hypothesis given observed data. It is widely used in classification problems and can be expressed as:
P(H|E) = P(E|H) * P(H)  / P(E)

- **P(H|E):** Posterior probability  
- **P(H):** Prior probability  
- **P(E|H):** Likelihood (probability of observing E given H)  
- **P(E):** Marginal likelihood (probability of E)

**Naive Bayes Classifier:**  
The Naive Bayes classifier assumes that the attributes are conditionally independent given the class. The classification is done by calculating the posterior probability for each class and choosing the class with the highest probability.

**Formula for Classification:**
P(C|x) = P(x|C)* P(C) / P(x)

**Conclusion (Data Mining Perspective):**

In data mining, the Naive Bayes classifier is a simple yet powerful algorithm for predictive modeling. It performs well with large datasets and is effective for applications like spam detection and sentiment analysis. Its assumption of conditional independence makes it computationally efficient and applicable even with limited data.