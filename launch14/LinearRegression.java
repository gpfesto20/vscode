import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LinearRegression {

    public static void main(String[] args) {
        String csvFile = "study_exam_scores.csv"; 
        String line;
        String cvsSplitBy = ",";
        
        int n = 0;
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                double x = Double.parseDouble(data[0]);
                double y = Double.parseDouble(data[1]);

                n++;
                sumX += x;
                sumY += y;
                sumXY += (x * y);
                sumX2 += (x * x);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double m = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double c = (sumY - m * sumX) / n;

        System.out.printf("Slope (m): %.4f%n", m);
        System.out.printf("Intercept (c): %.4f%n", c);
        System.out.printf("Linear Regression Equation: y = %.4fx + %.4f%n", m, c);
    }
}


Linear Regression is a statistical method used to model the relationship between a dependent variable 
y and one or more independent variables x. It assumes a linear relationship between the variables, which means that changes in the independent variable(s) result in proportional changes in the dependent variable. The goal of linear regression is to find the best-fitting straight line, known as the regression line, that minimizes the sum of squared differences (errors) between the actual data points and the predicted values.

In the context of simple linear regression (like the code provided), the equation for the line is:


y=mx+c
Where:


y is the dependent variable (e.g., exam score),
x is the independent variable (e.g., study hours),
m is the slope (representing the rate of change of y with respect to x),
c is the intercept (the value of y when x=0).

m=nsumxy -sumxsumy/n (sum(xsq) -(sumx)sq)

c=sumy*sum(xsq) -sumx sumxy/n (sum(xsq) -(sumx)sq)



Prediction and Analysis: Linear regression is a powerful tool for predicting and understanding the relationship between variables, especially when the relationship is approximately linear.
Simple yet Effective: It is simple to implement and can provide valuable insights with minimal computation, making it widely used in data mining and predictive analytics.


LinearRegressionlearner