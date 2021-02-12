package at.schn142.stockmarket.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

import at.schn142.stockmarket.model.Stock;

public class AppUtils {

    public static String checkStringAvailable(String stock) {
        if (stock.isEmpty() || stock == null) {
            return "N/A";
        } else
            return stock;
    }

    public static String roundLatestPrice(Stock stock) {
        if (stock.getLatestPrice() == null || stock.getLatestPrice().isEmpty()) {
            return "N/A";
        } else{
            BigDecimal bd = new BigDecimal(stock.getLatestPrice());
            bd = bd.setScale(2);
            return bd.toString();
        }

    }

    public static String roundChangePercent(Stock stock) {

        if (stock.getChangePercent() == null || stock.getChangePercent().isEmpty()) {
            return "N/A";
        } else{
            BigDecimal bd = new BigDecimal(stock.getChangePercent());
            bd = bd.setScale(3, RoundingMode.DOWN);
            return bd.toString();
        }


    }

    public static String checkNull(Double stock) {
        if (stock == null || stock.isNaN() || stock.isInfinite()) {
            return "N/A";
        }
        BigDecimal bd = new BigDecimal(stock);
        return bd.toString();
    }


}
