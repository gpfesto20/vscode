public class KMeansClustering {

    static class Point {
        double[] coordinates;

        Point(double... coords) {
            this.coordinates = coords;
        }

        @Override
        public String toString() {
            if (coordinates.length == 1) {
                return String.format("%.2f", coordinates[0]);
            }
            return String.format("(%.2f, %.2f)", coordinates[0], coordinates[1]);
        }
    }

    public static void main(String[] args) {
        String csvFile = "Spotify_Most_Streamed_Songs.csv";
        List<Point> dataPoints;

        // dataPoints = readColumnData(csvFile, 2);
        dataPoints = readColumnData(csvFile, 2, 3);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of clusters (K): ");
        int k = scanner.nextInt();

        List<Point> centroids = initializeRandomCentroids(dataPoints, k);

        System.out.println("Randomly selected initial centroids (clusters): " + centroids + "\n");

        List<Integer> clusterAssignments = new ArrayList<>();
        boolean centroidsChanged = true;
        int iterations = 0;

        while (centroidsChanged && iterations < 100) {
            iterations++;
            centroidsChanged = false;
            clusterAssignments.clear();

            System.out.println("Iteration " + iterations);

            System.out.printf("%-25s", "DataPoint");
            for (int i = 0; i < k; i++) {
                System.out.printf("%-15s", "C" + (i + 1));
            }
            System.out.printf("%-15s%-15s%n", "Min Distance", "Close to");

            for (Point point : dataPoints) {
                int nearestCentroidIndex = findNearestCentroid(point, centroids);
                clusterAssignments.add(nearestCentroidIndex);

                System.out.printf("%-25s", point);
                double minDistance = Double.MAX_VALUE;
                for (int i = 0; i < k; i++) {
                    double dist = distance(point, centroids.get(i));
                    System.out.printf("%-15.2f", dist);
                    if (dist < minDistance) {
                        minDistance = dist;
                    }
                }
                String closeTo = "C" + (nearestCentroidIndex + 1);
                System.out.printf("%-15.2f%-15s%n", minDistance, closeTo);
            }

            for (int i = 0; i < k; i++) {
                List<Point> clusterPoints = getClusterPoints(dataPoints, clusterAssignments, i);
                if (!clusterPoints.isEmpty()) {
                    Point newCentroid = calculateMean(clusterPoints);
                    if (distance(centroids.get(i), newCentroid) > 1e-4) {
                        centroids.set(i, newCentroid);
                        centroidsChanged = true;
                    }
                }
            }
            System.out.println("Updated Centroids: " + centroids + "\n");
        }

        System.out.println("Final Cluster Assignments:");
        for (int i = 0; i < dataPoints.size(); i++) {
            System.out.println("Point: " + dataPoints.get(i) + " -> Cluster: C" + (clusterAssignments.get(i) + 1));
        }
    }

    public static List<Point> readColumnData(String csvFile, int... columnIndexes) {
        List<Point> dataPoints = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                double[] coords = new double[columnIndexes.length];
                for (int i = 0; i < columnIndexes.length; i++) {
                    coords[i] = Double.parseDouble(fields[columnIndexes[i]]);
                }
                dataPoints.add(new Point(coords));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataPoints;
    }

    public static List<Point> initializeRandomCentroids(List<Point> dataPoints, int k) {
        List<Point> centroids = new ArrayList<>();
        Random rand = new Random();
        List<Point> copyDataPoints = new ArrayList<>(dataPoints);
        Collections.shuffle(copyDataPoints, rand);
        for (int i = 0; i < k; i++) {
            centroids.add(copyDataPoints.get(i));
        }
        return centroids;
    }

    public static int findNearestCentroid(Point point, List<Point> centroids) {
        int nearestCentroidIndex = 0;
        double minDistance = distance(point, centroids.get(0));

        for (int i = 1; i < centroids.size(); i++) {
            double dist = distance(point, centroids.get(i));
            if (dist < minDistance) {
                minDistance = dist;
                nearestCentroidIndex = i;
            }
        }
        return nearestCentroidIndex;
    }

    public static List<Point> getClusterPoints(List<Point> data, List<Integer> assignments, int clusterIndex) {
        List<Point> clusterPoints = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (assignments.get(i) == clusterIndex) {
                clusterPoints.add(data.get(i));
            }
        }
        return clusterPoints;
    }

    public static Point calculateMean(List<Point> points) {
        int dimensions = points.get(0).coordinates.length;
        double[] sum = new double[dimensions];
        for (Point point : points) {
            for (int i = 0; i < dimensions; i++) {
                sum[i] += point.coordinates[i];
            }
        }
        double[] mean = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            mean[i] = sum[i] / points.size();
        }
        return new Point(mean);
    }

    public static double distance(Point p1, Point p2) {
        double sum = 0;
        for (int i = 0; i < p1.coordinates.length; i++) {
            sum += Math.pow(p1.coordinates[i] - p2.coordinates[i], 2);
        }
        return Math.sqrt(sum);
    }
}



partition-based clustering in unsupervised learning, focusing on K-Means clustering. This approach divides data into distinct clusters by minimizing the distance between data points and their cluster centers (in K-Means) or representative points (in K-Medoids).

K-Means Clustering Summary:
K-Means is an iterative algorithm that groups data into k clusters by assigning each data point to the nearest centroid.
Steps include:
Select a value for k.
Choose k initial points as centroids.
Compute distances and assign data points to the nearest centroid.
Recalculate centroids based on current assignments.
Repeat until convergence (no further changes in cluster assignments)


colfilter-kmeans
clusrering
