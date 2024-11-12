import java.io.*;
import java.util.*;

public class HierarchicalClustering {
    public static void main(String[] args) {
        String file = "Mine_Data.csv";
        List<Double> voltages = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose Linkage Method:");
        System.out.println("1. Single Linkage");
        System.out.println("2. Complete Linkage");
        System.out.println("3. Average Linkage");
        int choice = sc.nextInt();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                voltages.add(Double.parseDouble(fields[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<List<Double>> clusters = new ArrayList<>();
        for (double v : voltages) {
            List<Double> c = new ArrayList<>();
            c.add(v);
            clusters.add(c);
        }

        while (clusters.size() > 1) {
            double minDist = Double.MAX_VALUE;
            int idx1 = -1, idx2 = -1;

            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    double dist = 0;

                    switch (choice) {
                        case 1:
                            dist = singleLinkage(clusters.get(i), clusters.get(j));
                            break;
                        case 2:
                            dist = completeLinkage(clusters.get(i), clusters.get(j));
                            break;
                        case 3:
                            dist = averageLinkage(clusters.get(i), clusters.get(j));
                            break;
                        default:
                            System.out.println("Invalid choice. Exiting.");
                            return;
                    }

                    if (dist < minDist) {
                        minDist = dist;
                        idx1 = i;
                        idx2 = j;
                    }
                }
            }

            System.out.printf("Merging clusters with minimum distance: %.4f%n", minDist);
            clusters.get(idx1).addAll(clusters.get(idx2));
            clusters.remove(idx2);

            System.out.println("Merged clusters: " + clusters);
        }

        System.out.println("Final Cluster: " + clusters);
    }

    private static double singleLinkage(List<Double> c1, List<Double> c2) {
        double minDist = Double.MAX_VALUE;
        for (double v1 : c1) {
            for (double v2 : c2) {
                double dist = Math.abs(v1 - v2);
                if (dist < minDist) {
                    minDist = dist;
                }
            }
        }
        return minDist;
    }

    private static double completeLinkage(List<Double> c1, List<Double> c2) {
        double maxDist = Double.MIN_VALUE;
        for (double v1 : c1) {
            for (double v2 : c2) {
                double dist = Math.abs(v1 - v2);
                if (dist > maxDist) {
                    maxDist = dist;
                }
            }
        }
        return maxDist;
    }

    private static double averageLinkage(List<Double> c1, List<Double> c2) {
        double totalDist = 0.0;
        int count = 0;
        for (double v1 : c1) {
            for (double v2 : c2) {
                totalDist += Math.abs(v1 - v2);
                count++;
            }
        }
        return totalDist / count;
    }
}


Hierarchical clustering is an unsupervised learning method that organizes data into a nested hierarchy of clusters. It has two main types:

Agglomerative (Bottom-up) Clustering: Starts with each data point as its own cluster, then merges clusters based on similarity until all belong to one cluster.
Divisive (Top-down) Clustering: Begins with a single cluster containing all data points and splits into smaller clusters based on certain criteria until each data point is its own cluster.
Linkage Methods in hierarchical clustering:

Single Linkage: Minimum distance between any two points in different clusters.
Complete Linkage: Maximum distance between any two points in different clusters.
Average Linkage: Average distance between all points in different clusters.
Conclusion
Hierarchical clustering creates a hierarchy of clusters, allowing for flexible data analysis. It is especially useful when the number of clusters is unknown.


colfil-HierarchicalClustering