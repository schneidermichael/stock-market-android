package at.schn142.stockmarket.model;

import com.anychart.chart.common.dataentry.ValueDataEntry;

/**
 * This class represents LineDataEntry
 *
 * @author michaelschneider
 * @version 1.0
 */
public class LineDataEntry extends ValueDataEntry {

    /**
     * Creates a LineDataEnry
     * @param x refers to the Date formatted as YYYY-MM-DD
     * @param value refers to the official price of the first stock
     * @param value2 refers to the official price of the second stock
     */
    public LineDataEntry(String x, Number value, Number value2) {
        super(x, value);
        setValue("value2", value2);
    }
}
