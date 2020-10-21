package at.schn142.stockmarket;

public class StockCard {

    private String symbol;
    private String companyName;
    private String latestPrice;
    private String changePercent;

    public StockCard(String symbol, String companyName, String latestPrice, String changePercent) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.latestPrice = latestPrice;
        this.changePercent = changePercent;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLatestPrice() {
        return latestPrice;
    }

    public String getChangePercent() {
        return changePercent;
    }


}
