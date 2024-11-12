import java.io.*;
import java.util.*;

public class Weights {
    public static void main(String[] args) {
        String inputFilePath = "region_product_quantity.csv"; 
        String outputFilePath = "output_region_product_quantity.csv"; 

        Map<String, Integer> regionSmartphoneMap = new LinkedHashMap<>();
        Map<String, Integer> regionLaptopMap = new LinkedHashMap<>();
        int totalSmartphones = 0;
        int totalLaptops = 0;
        Map<String, Integer> regionTotalMap = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] fields = line.split(",");
                String region = fields[0].trim();
                String product = fields[1].trim();
                int quantity = Integer.parseInt(fields[2].trim());

                if (product.equalsIgnoreCase("Smartphone")) {
                    regionSmartphoneMap.put(region, regionSmartphoneMap.getOrDefault(region, 0) + quantity);
                    totalSmartphones += quantity;
                } else if (product.equalsIgnoreCase("Laptop")) {
                    regionLaptopMap.put(region, regionLaptopMap.getOrDefault(region, 0) + quantity);
                    totalLaptops += quantity;
                }

                regionTotalMap.put(region, regionTotalMap.getOrDefault(region, 0) + quantity);
            }

            regionTotalMap.put("Total", totalSmartphones + totalLaptops);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
            bw.write("Region,Smartphone (Count),Smartphone (TWeight %),Smartphone (DWeight %),"
                    + "Laptop (Count),Laptop (TWeight %),Laptop (DWeight %),Total (Count)");
            bw.newLine();

            for (String region : regionTotalMap.keySet()) {
                if (!region.equals("Total")) {
                    int smartphoneCount = regionSmartphoneMap.getOrDefault(region, 0);
                    int laptopCount = regionLaptopMap.getOrDefault(region, 0);
                    int totalCount = regionTotalMap.get(region);

                    double smartphoneTWeight = totalSmartphones > 0 ? ((double) smartphoneCount / totalSmartphones) * 100 : 0;
                    double laptopTWeight = totalLaptops > 0 ? ((double) laptopCount / totalLaptops) * 100 : 0;
                    double smartphoneDWeight = totalCount > 0 ? ((double) smartphoneCount / totalCount) * 100 : 0;
                    double laptopDWeight = totalCount > 0 ? ((double) laptopCount / totalCount) * 100 : 0;

                    bw.write(String.format("%s,%d,%.2f%%,%.2f%%,%d,%.2f%%,%.2f%%,%d%n",
                            region, smartphoneCount, smartphoneTWeight, smartphoneDWeight,
                            laptopCount, laptopTWeight, laptopDWeight, totalCount));
                }
            }

            bw.write(String.format("Total,%d,100%%,100%%,%d,100%%,100%%,%d%n",
                    totalSmartphones, totalLaptops, regionTotalMap.get("Total")));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("Output written to: %s%n", outputFilePath);
    }
}





In data mining, TWeight (Total Weight) and DWeight (Distribution Weight) are measures that provide insights into the proportion of each product category (e.g., Smartphones, Laptops) relative to the total quantity or the total quantity within each region.

TWeight (Total Weight): This is the percentage of the product's quantity relative to the total quantity of that product across all regions. It shows how a region's sales of a product contribute to the overall sales of that product.

Formula:
TWeight= Product Count in Region/Total Product Count (across all regions) ×100

DWeight (Distribution Weight): This is the percentage of the product's quantity relative to the total quantity in that specific region. It shows how the sales of a product are distributed within a region.

Formula:
Formula:
TWeight= Product Count in Region/Total Count in region) ×100