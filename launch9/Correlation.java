import java.io.*;
import java.util.*;
public class Correlation {
    public static void main(String[] args) throws Exception {
        String inputFile = "student_data.csv";
        List<String[]> data = readCSV(inputFile);
        int totalRows = data.size() - 1;
        List<String[]> results = new ArrayList<>();
        results.add(new String[]{"Item 1 (Activity)", "Item 2 (Activity)", "Correlation Coefficient", "Type of Correlation"});
        for (int i = 1; i <= totalRows; i++) {
            for (int j = i + 1; j <= totalRows; j++) {
                double correlation = findCorrelation(data, i, j);
                String verdict = getVerdict(correlation);
                String item1 = data.get(i)[0];
                String item2 = data.get(j)[0];                
                System.out.printf("Correlation between %s & %s = %.3f (%s)\n", item1, item2, correlation, verdict);
                results.add(new String[]{item1, item2, String.format("%.3f", correlation), verdict});
            }
        }
        writeCSV("Correlation_output.csv", results);
    }

    public static List<String[]> readCSV(String fileName) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }
        }
        return data;
    }
    public static void writeCSV(String fileName, List<String[]> data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }        }    }
    public static double findCorrelation(List<String[]> data, int tid1, int tid2) {
        int tid1Count = 0, tid2Count = 0, commonCount = 0;

        for (int j = 1; j < data.get(0).length; j++) {
            int value1 = Integer.parseInt(data.get(tid1)[j]);
            int value2 = Integer.parseInt(data.get(tid2)[j]);

            if (value1 == 1) tid1Count++;
            if (value2 == 1) tid2Count++;
            if (value1 == 1 && value2 == 1) commonCount++;
        }
        if (tid1Count == 0 || tid2Count == 0) return 0;
        return (double) commonCount / (tid1Count * tid2Count);
    }
    public static String getVerdict(double correlation) {
        if (correlation == 0) return "No relationship";
        if (correlation > 0) return "Positive correlation";
        return "Negative correlation";
    }
}


correlation refers to the measure of the relationship or association between two items or entities. A high positive correlation (close to +1) indicates that as one variable increases, the other tends to increase as well. A high negative correlation (close to -1) suggests that one variable decreases as the other increases. No correlation (close to 0) implies no relationship between the variables.

The formula used here is:

C(A,B)=P(A∩B)/ P(A)⋅P(B)

​
 
where:

C(A,B) is the correlation coefficient between items 
P(A∩B) is the probability of both items occurring together,
P(A) and P(B) are the probabilities of items 
A and B occurring individually.


 correlation helps identify relationships between different variables or items, allowing us to understand patterns and dependencies. A positive correlation indicates that two items tend to increase together, while a negative correlation means one decreases as the other increases. No correlation implies no discernible relationship between the items.


 linearcorrelation
 correlation(sttribu)