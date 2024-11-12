import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MinMaxNormalization {

    // Function to normalize pixel values in the image
    public static BufferedImage normalizeImage(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Variables to store min and max pixel values
        int minPixel = 255;
        int maxPixel = 0;

        // Find the min and max pixel values in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = originalImage.getRGB(x, y) & 0xFF;
                if (pixel < minPixel) {
                    minPixel = pixel;
                }
                if (pixel > maxPixel) {
                    maxPixel = pixel;
                }
            }
        }

        System.out.println("Minimum pixel value in the image: " + minPixel);
        System.out.println("Maximum pixel value in the image: " + maxPixel);

        // Create a new BufferedImage to store normalized image
        BufferedImage normalizedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Normalize pixel values to range [0, 255]
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int originalPixel = originalImage.getRGB(x, y) & 0xFF;
                int normalizedPixel = (int) (((originalPixel - minPixel) * 255.0) / (maxPixel - minPixel));
                normalizedImage.setRGB(x, y, (normalizedPixel << 16) | (normalizedPixel << 8) | normalizedPixel);

                // Print each normalization step
                // System.out.println("Original Pixel: " + originalPixel + " -> Normalized
                // Pixel: " + normalizedPixel);
            }
        }

        return normalizedImage;
    }

    public static void main(String[] args) {
        try {
            // Load the image (grayscale)
            File inputFile = new File("input_image.png");
            BufferedImage originalImage = ImageIO.read(inputFile);

            System.out.println("Original Image Loaded.");
            System.out.println("Image Dimensions: " + originalImage.getWidth() + " x " + originalImage.getHeight());

            // Perform Min-Max Normalization
            BufferedImage normalizedImage = normalizeImage(originalImage);

            // Save the normalized image
            File outputFile = new File("normalized_image.png");
            ImageIO.write(normalizedImage, "png", outputFile);
            System.out.println("Normalized Image saved as 'normalized_image.png'.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


**Theory:**

**Min-max normalization** is a technique used to scale input values to a specific range, commonly applied in image processing to adjust pixel intensity. In images, this enhances contrast or prepares data for machine learning models. For instance, in medical imaging, it makes details clearer. In cases such as MRI and CT scans, ensuring that pixel values are uniformly scaled improves model accuracy and visual quality.

**Algorithm:**
1. **Input:** Image and target normalization range [0,255]
2. **Find min/max Values:** Identify the minimum (min) and maximum (max) pixel values in the image.
3. **Normalize:** For each pixel, apply the normalization formula to scale it to the desired range.
4. **Output:** A new image with normalized pixel values.

**Pseudocode:**
1. Find min and max.
2. For each pixel, compute:

