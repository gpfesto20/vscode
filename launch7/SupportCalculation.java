import java.io.*;
import java.util.*;

public class SupportCalculation {

    public static void main(String[] args) {
        String csvFile = "sample.csv";  
        String line;
        String csvSplitBy = ",";
        double minSupportThreshold = 0.3;

        List<Set<String>> transactions = new ArrayList<>();
        List<String> itemsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String header = br.readLine(); 
            String[] items = header.split(csvSplitBy);
            
            for (int i = 1; i < items.length; i++) {
                itemsList.add(items[i]);
            }

            while ((line = br.readLine()) != null) {
                String[] transaction = line.split(csvSplitBy);
                Set<String> itemsInTransaction = new HashSet<>();

                for (int i = 1; i < transaction.length; i++) {
                    if (transaction[i].equalsIgnoreCase("True")) {
                        itemsInTransaction.add(itemsList.get(i - 1));
                    }
                }

                transactions.add(itemsInTransaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<Set<String>, Integer> frequentItemsets = new HashMap<>();
        Map<Set<String>, Integer> currentItemsetCount = getItemsetCounts(transactions, 1);
        int k = 1;

        while (!currentItemsetCount.isEmpty()) {
            System.out.printf("\n%d-itemsets with support >= %.2f:\n", k, minSupportThreshold);
            System.out.printf("| %-20s | %-10s | %-10s |\n", "Itemset", "Count", "Support (%)");
            System.out.println("----------------------------------------------");
            printFrequentItemsets(currentItemsetCount, transactions.size(), minSupportThreshold);
            frequentItemsets.putAll(currentItemsetCount);
            currentItemsetCount = generateNextItemsets(frequentItemsets.keySet(), transactions, k + 1);
            k++;
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

    public static void printFrequentItemsets(Map<Set<String>, Integer> itemsetCount, int totalTransactions, double minSupportThreshold) {
        for (Map.Entry<Set<String>, Integer> entry : itemsetCount.entrySet()) {
            double support = (double) entry.getValue() / totalTransactions;
            if (support >= minSupportThreshold) {
                System.out.printf("| %-20s | %-10d | %-10.2f |\n", entry.getKey(), entry.getValue(), support * 100);
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
                    nextItemsetCount.put(combination, nextItemsetCount.getOrDefault(combination, 0) + 1);
                }
            }
        }
        return nextItemsetCount;
    }
}


frequent itemsets are sets of items that appear together in a dataset with a frequency (support) greater than or equal to a user-defined threshold. The support of an itemset is calculated as the ratio of the number of transactions containing that itemset to the total number of transactions. The formula for support is:
Support(X)= Count of transactions containing X/Total number of transactions

​
 
For example, given a dataset of transactions with items like Milk, Bread, Butter, Eggs, Apples, and a minimum support threshold of 30% (0.30), the goal is to find all itemsets (combinations of items) whose support exceeds this threshold.

Steps:
Extract the transactions: From the CSV dataset, represent each transaction as a set of items, indicating whether the item is present (True) or absent (False).
Count itemsets: For each size of itemsets (1-itemsets, 2-itemsets, etc.), count how many transactions contain the itemset.
Calculate support: For each itemset, calculate the support percentage using the formula mentioned above.
Filter frequent itemsets: Only retain itemsets with support greater than or equal to the minimum threshold.

Frequent itemsets are key in identifying associations between items in transactions. By finding these itemsets, businesses can gain insights into which items are often bought together, aiding in decisions like product bundling or targeted marketing.



csv-collectioncol-bitvect-associationrule

weka






