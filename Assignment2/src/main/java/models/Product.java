package models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import java.lang.Long;

public class Product implements Comparable {
    private final long barcode;
    private String title;
    private double price;

    public Product(long barcode) {
        this.barcode = barcode;
    }

    public Product(long barcode, String title, double price) {
        this(barcode);
        this.title = title;
        this.price = price;
    }

    /**
     * parses product information from a textLine with format: barcode, title, price
     *
     * @param textLine
     * @return a new Product instance with the provided information
     * or null if the textLine is corrupt or incomplete
     */
    public static Product fromLine(String textLine) {

        List<String> splittedText = Arrays.asList(textLine.split(", ")); // split textline at ', ' into array

        Product newProduct = new Product(Long.parseLong(splittedText.get(0)), splittedText.get(1), Double.parseDouble(splittedText.get(2))); // create Product instance from textline

        // TODO convert the information in line to a new Product instance

        return newProduct;
    }

    public long getBarcode() {
        return barcode;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Product)) return false;
        return this.getBarcode() == ((Product) other).getBarcode();
    }

    @Override
    public int compareTo(Object other) {

        if (this.getBarcode() > ((Product) other).getBarcode()) {

            // if current object is greater,then return 1
            return 1;
        } else if (this.getBarcode() < ((Product) other).getBarcode()) {

            // if current object is greater,then return -1
            return -1;
        } else {

            // if current object is equal to o,then return 0
            return 0;
        }
    }

    @Override
    public String toString() {
        return barcode + "/" + title + '/' + price;
    }

    // TODO add public and private methods as per your requirements
}
