package at.schn142.stockmarket.model;

import com.anychart.chart.common.dataentry.ValueDataEntry;

public class LineDataEntry extends ValueDataEntry {

    public LineDataEntry(String x, Number value) {
        super(x, value);
    }

    public LineDataEntry(Number x, Number value) {
        super(x, value);
    }

    public LineDataEntry(String x, Number value, Number value2) {
        super(x, value);
        setValue("value2", value2);
    }
}
