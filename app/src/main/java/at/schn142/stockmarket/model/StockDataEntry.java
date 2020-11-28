package at.schn142.stockmarket.model;

import com.anychart.chart.common.dataentry.HighLowDataEntry;

/**
 * This class represents StockDataEntry
 *
 * @author michaelschneider
 * @version 1.0
 */
public class StockDataEntry extends HighLowDataEntry {

    /**
     * Creates a StockDataEntry
     * @param x refers to the Date formatted as YYYY-MM-DD
     * @param open refers to the official open price
     * @param high refers to the market-wide highest price
     * @param low refers to the market-wide lowest price
     * @param close refers to the official close price
     */
    public StockDataEntry(String x, Double open, Double high, Double low, Double close) {
        super(x, high, low);
        setValue("open", open);
        setValue("close", close);
    }
}
