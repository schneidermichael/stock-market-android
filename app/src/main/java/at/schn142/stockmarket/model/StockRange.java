package at.schn142.stockmarket.model;

/**
 * This enum represents the StockRange
 *
 * @author michaelschneider
 * @version 1.0
 */
public enum StockRange {
    fiveYear("5y"),
    twoYear("2y"),
    oneYear("1y"),
    sixMonth("6m"),
    threeMonth("3m"),
    oneMonth("1m"),
    fiveDay("5d");

    private final String text;

    /**
     * Creates a new Stockrange enum
     * @param s The value of the Stockrange
     */
    StockRange(String s) {
        this.text = s;
    }


    public String getText() {
        return text;
    }
}
