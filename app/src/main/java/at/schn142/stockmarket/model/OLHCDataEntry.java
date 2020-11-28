package at.schn142.stockmarket.model;

import com.anychart.chart.common.dataentry.HighLowDataEntry;

/**
 * This class represents OLHCDataEntry
 *
 * @author michaelschneider
 * @version 1.0
 */
public class OLHCDataEntry extends HighLowDataEntry {

    /**
     * Creates a OLHCDataEntry
     * @param x refers to the Date formatted as YYYY-MM-DD
     * @param open refers to the official open price
     * @param high refers to the market-wide highest price
     * @param low refers to the market-wide lowest price
     * @param close refers to the official close price
     */
    public OLHCDataEntry(String x, Double open, Double high, Double low, Double close) {
        super(x, high, low);
        setValue("open", open);
        setValue("close", close);
    }
}
