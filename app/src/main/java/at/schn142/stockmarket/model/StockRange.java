package at.schn142.stockmarket.model;

public enum StockRange {
    fiveYear("5y"),
    twoYear("2y"),
    oneYear("1y"),
    sixMonth("6m"),
    threeMonth("3m"),
    oneMonth("1m"),
    fiveDay("5d");

    private final String text;

    StockRange(String s) {
        this.text = s;
    }

    public String getText() {
        return text;
    }
}
