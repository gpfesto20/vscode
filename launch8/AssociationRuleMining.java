import java.io.*;
import java.util.*;

public class AssociationRuleMining {

    public static void main(String[] args) {
        String csvFile = "sample.csv";  
        List<Set<String>> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] transaction = line.split(",");
                Set<String> items = new HashSet<>();
                for (int i = 1; i < transaction.length; i++) {
                    if (transaction[i].equalsIgnoreCase("True")) {
                        items.add(getItemName(i));
                    }
                }
                transactions.add(items);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Minimum Support Threshold (as fraction): ");
        double minSupportThreshold = scanner.nextDouble();

        System.out.print("Enter Minimum Confidence Threshold (as fraction): ");
        double minConfidenceThreshold = scanner.nextDouble();
        scanner.close();

        int totalTransactions = transactions.size();
        Map<Set<String>, Integer> frequentItemsets = new HashMap<>();
        Map<Set<String>, Integer> currentItemsetCount = getItemsetCounts(transactions, 1);

        int k = 1;
        while (!currentItemsetCount.isEmpty()) {
            System.out.printf("\n%d-itemsets with support >= %.2f:\n", k, minSupportThreshold);
            System.out.printf("| %-20s | %-10s | %-10s |\n", "Itemset", "Count", "Support (%)");
            System.out.println("-----------------------------------------------");
            filterAndPrintFrequentItemsets(currentItemsetCount, totalTransactions, minSupportThreshold, frequentItemsets);
            currentItemsetCount = generateNextItemsets(frequentItemsets.keySet(), transactions, k + 1);
            k++;
        }

        System.out.printf("\nAssociation Rules with Confidence >= %.2f:\n", minConfidenceThreshold);
        System.out.printf("| %-20s | %-20s | %-10s |\n", "Rule", "Confidence", "Confidence (%)");
        System.out.println("-----------------------------------------------------");
        findAssociationRules(frequentItemsets, totalTransactions, minConfidenceThreshold);
    }

    public static String getItemName(int index) {
        switch (index) {
            case 1: return "Milk";
            case 2: return "Bread";
            case 3: return "Butter";
            case 4: return "Eggs";
            case 5: return "Apples";
            default: return "";
        }
    }

    public static Map<Set<String>, Integer> getItemsetCounts(List<Set<String>> transactions, int setSize) {
        Map<Set<String>, Integer> itemsetCount = new HashMap<>();
        for (Set<String> transaction : transactions) {
            if (transaction.size() >= setSize) {
                Set<Set<String>> combinations = getCombinations(transaction, setSize);
                for (Set<String> combination : combinations) {
                    itemsetCount.put(combination, itemsetCount.getOrDefault(combination, 0) + 1);
                }
            }
        }
        return itemsetCount;
    }

    public static void filterAndPrintFrequentItemsets(Map<Set<String>, Integer> itemsetCount, int totalTransactions, double minSupportThreshold, Map<Set<String>, Integer> frequentItemsets) {
        Iterator<Map.Entry<Set<String>, Integer>> iterator = itemsetCount.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Set<String>, Integer> entry = iterator.next();
            double support = (double) entry.getValue() / totalTransactions;
            if (support >= minSupportThreshold) {
                frequentItemsets.put(entry.getKey(), entry.getValue());
                System.out.printf("| %-20s | %-10d | %-10.2f |\n", entry.getKey(), entry.getValue(), support * 100);
            } else {
                iterator.remove();
            }
        }
    }

    public static Set<Set<String>> getCombinations(Set<String> transaction, int setSize) {
        Set<Set<String>> combinations = new HashSet<>();
        List<String> transactionList = new ArrayList<>(transaction);
        generateCombinations(combinations, new HashSet<>(), transactionList, 0, setSize);
        return combinations;
    }

    public static void generateCombinations(Set<Set<String>> combinations, Set<String> currentSet, List<String> items, int index, int setSize) {
        if (currentSet.size() == setSize) {
            combinations.add(new HashSet<>(currentSet));
            return;
        }
        for (int i = index; i < items.size(); i++) {
            currentSet.add(items.get(i));
            generateCombinations(combinations, currentSet, items, i + 1, setSize);
            currentSet.remove(items.get(i));
        }
    }

    public static Map<Set<String>, Integer> generateNextItemsets(Set<Set<String>> previousItemsets, List<Set<String>> transactions, int setSize) {
        Map<Set<String>, Integer> nextItemsetCount = new HashMap<>();
        for (Set<String> transaction : transactions) {
            if (transaction.size() >= setSize) {
                Set<Set<String>> combinations = getCombinations(transaction, setSize);
                for (Set<String> combination : combinations) {
                    if (allSubsetsAreFrequent(combination, previousItemsets)) {
                        nextItemsetCount.put(combination, nextItemsetCount.getOrDefault(combination, 0) + 1);
                    }
                }
            }
        }
        return nextItemsetCount;
    }

    public static boolean allSubsetsAreFrequent(Set<String> combination, Set<Set<String>> previousItemsets) {
        for (String item : combination) {
            Set<String> subset = new HashSet<>(combination);
            subset.remove(item);
            if (!previousItemsets.contains(subset)) {
                return false;
            }
        }
        return true;
    }

    public static void findAssociationRules(Map<Set<String>, Integer> frequentItemsets, int totalTransactions, double minConfidenceThreshold) {
        for (Map.Entry<Set<String>, Integer> entry : frequentItemsets.entrySet()) {
            Set<String> itemset = entry.getKey();
            int itemsetCount = entry.getValue();

            if (itemset.size() > 1) {
                Set<Set<String>> subsets = getCombinations(itemset, itemset.size() - 1);

                for (Set<String> subset : subsets) {
                    Set<String> remaining = new HashSet<>(itemset);
                    remaining.removeAll(subset);

                    int subsetCount = frequentItemsets.getOrDefault(subset, 0);
                    if (subsetCount > 0) {
                        double confidence = (double) itemsetCount / subsetCount;

                        if (confidence >= minConfidenceThreshold) {
                            System.out.printf("| %-20s | %-20.3f | %-10.2f |\n", subset + " => " + remaining, confidence, confidence * 100);
                        }
                    }
                }
            }
        }
    }
}



Association rules are a fundamental concept in data mining used to discover interesting relationships or patterns among a set of items in large datasets, particularly in transactional data. These rules are often used in market basket analysis, where the goal is to find associations between products that customers frequently purchase together.

Support measures how frequently an itemset appears in the dataset and is calculated using the formula:

{Support}(A) = {Frequency of itemset A}/{Total number of transactions}

Confidence measures how often items in the right-hand side of a rule appear when the left-hand side is present, and is calculated using:

{Confidence}(A -> B) = A U B/ A


Using a minimum support threshold, we first identify all frequent itemsets, which are item combinations whose support exceeds the threshold. Then, we generate association rules from these frequent itemsets and calculate their confidence. Only the rules with confidence above a specified minimum threshold are considered strong association rules.

InAssociation rules provide valuable insights into the relationships between items in a dataset. By mining these patterns, businesses can make data-driven decisions, such as identifying which products to bundle together or which items are frequently bought together, thereby enhancing marketing strategies, improving inventory management, and increasing sales.

csv-collectioncol-bitvector-asr
weka