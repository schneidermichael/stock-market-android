package at.schn142.stockmarket;

public class StockExchange {

    private String name;
    private String symbol;
    private double latitude;
    private double longitude;

    public StockExchange(String name, String symbol, double latitude, double longitude) {
        this.name = name;
        this.symbol = symbol;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
