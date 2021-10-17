package models;


import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Logger;

public class Purchase {
    private final Product product;
    private int count;

    public Purchase(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    /**
     * parses purchase summary information from a textLine with format: barcode, amount
     *
     * @param textLine
     * @param products a list of products ordered and searchable by barcode
     *                 (i.e. the comparator of the ordered list shall consider only the barcode when comparing products)
     * @return a new Purchase instance with the provided information
     * or null if the textLine is corrupt or incomplete
     */
    public static Purchase fromLine(String textLine, List<Product> products) {
        try {
            List<String> splittedText = Arrays.asList(textLine.split(", ")); // split textline at ', ' into array

            // Get product index for requested purchase line
            int productIndex = products.indexOf(new Product(Long.parseLong(splittedText.get(0))));

            if (productIndex != -1) {
                Purchase newPurchase = new Purchase(products.get(productIndex), Integer.parseInt(splittedText.get(1)));

                // TODO convert the information in the textLine to a new Purchase instance
                //  use the products.indexOf to find the product that is associated with the barcode of the purchase

                return newPurchase;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * add a delta amount to the count of the purchase summary instance
     *
     * @param delta
     */
    public void addCount(int delta) {
        this.count += delta;
    }

    public long getBarcode() {
        return this.product.getBarcode();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public String toString() {
        String price = String.format("%.2f", (product.getPrice() * this.getCount())).replace(",", ".");
        return String.format("%s/%s/%d/%s", product.getBarcode(), product.getTitle(), this.getCount(), price);
    }


    // TODO add public and private methods as per your requirements
}
