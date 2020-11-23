package at.schn142.stockmarket.model;

import com.anychart.chart.common.dataentry.HighLowDataEntry;

public class StockDataEntry extends HighLowDataEntry {

    public StockDataEntry(String x, Number high, Number low) {
        super(x, high, low);
    }

    public StockDataEntry(Number x, Number high, Number low) {
        super(x, high, low);
    }

    public StockDataEntry(Long x, Double open, Double high, Double low, Double close) {
        super(x, high, low);
        setValue("open", open);
        setValue("close", close);
    }

    public StockDataEntry(String x, Double open, Double high, Double low, Double close) {
        super(x, high, low);
        setValue("open", open);
        setValue("close", close);
    }
}
