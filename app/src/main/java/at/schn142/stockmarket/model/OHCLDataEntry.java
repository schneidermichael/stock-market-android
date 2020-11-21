package at.schn142.stockmarket.model;

import com.anychart.chart.common.dataentry.HighLowDataEntry;

public class OHCLDataEntry extends HighLowDataEntry {

    public OHCLDataEntry(String x, Number high, Number low) {
        super(x, high, low);
    }

    public OHCLDataEntry(Number x, Number high, Number low) {
        super(x, high, low);
    }

    public OHCLDataEntry(Long x, Double open, Double high, Double low, Double close) {
        super(x, high, low);
        setValue("open", open);
        setValue("close", close);
    }
}
