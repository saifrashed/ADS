import models.Purchase;
import models.PurchaseTracker;

import java.util.Comparator;

public class SupermarketStatisticsMain {

    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Supermarket Statistics processor\n");

        PurchaseTracker purchaseTracker = new PurchaseTracker();

        purchaseTracker.importProductsFromVault("/products.txt");

        purchaseTracker.importPurchasesFromVault("/purchases");

        // TODO provide the comparators that can order the purchases by specified criteria
        purchaseTracker.showTops(5, "worst sales volume",
                (o1, o2) -> {
                    if (o1.getCount() > o2.getCount()) {
                        return 1;
                    } else if (o1.getCount() < o2.getCount()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
        );

        purchaseTracker.showTops(5, "best sales revenue", (o1, o2) -> {
                    if ((o1.getProduct().getPrice() * o1.getCount()) < (o2.getProduct().getPrice() * o2.getCount())) {
                        return 1;
                    } else if ((o1.getProduct().getPrice() * o1.getCount()) > (o2.getProduct().getPrice() * o2.getCount())) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
        );

        purchaseTracker.showTotals();
    }


}
