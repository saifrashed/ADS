package models;


import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

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

        List<String> splittedText = Arrays.asList(textLine.split(", ")); // split textline at ', ' into array


        // place products in OrderedArrayList to allow custom operations.
        OrderedArrayList<Product> productArray = new OrderedArrayList<>();
        productArray.addAll(products);
        productArray.sort(Product::compareTo);

        int productIndex = productArray.indexOf(new Product(Long.parseLong(splittedText.get(0))));

        Purchase newPurchase = new Purchase(productArray.get(productIndex), Integer.parseInt(splittedText.get(1)));

        // TODO convert the information in the textLine to a new Purchase instance
        //  use the products.indexOf to find the product that is associated with the barcode of the purchase

        return newPurchase;
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
        return product.getBarcode() + "/" + product.getTitle() + '/' + this.getCount() + "/" + price;
    }


    // TODO add public and private methods as per your requirements
}
