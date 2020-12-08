package at.schn142.stockmarket.model;

import androidx.annotation.NonNull;
import androidx.annotation.TransitionRes;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import kotlin.jvm.Transient;


/**
 * This class represents Stock
 *
 * @author michaelschneider
 * @version 1.0
 */
@Entity(tableName = "stock_table")
public class Stock implements Comparable<Stock>{

    /**
     * The symbol for this stock
     */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "symbol")
    private String symbol;

    /**
     * The company name for this stock
     */
    @ColumnInfo(name = "companyName")
    private String companyName;


    /**
     * The open price for this stock
     */
    @ColumnInfo(name = "open")
    private Double open;

    /**
     * The close price for this stock
     */
    @ColumnInfo(name = "close")
    private Double close;

    /**
     * The market-wide highest price for the stock
     */
    @ColumnInfo(name = "high")
    private Double high;

    /**
     * The market-wide lowest price for the stock
     */
    @ColumnInfo(name = "low")
    private Double low;

    /**
     * The latest price for this stock
     */
    @ColumnInfo(name = "latestPrice")
    private String latestPrice;

    /**
     * The percent change for this stock
     */
    @ColumnInfo(name = "changePercent")
    private String changePercent;

    /**
     * The volume for this stock
     */
    @ColumnInfo(name = "volume")
    private Double volume;

    /**
     * The 30 day average volume for this stock
     */
    @ColumnInfo(name = "avgTotalVolume")
    private Double avgTotalVolume;

    /**
     * The Market capitalization for this stock
     */
    @ColumnInfo(name = "marketCap")
    private Double marketCap;

    /**
     * The price-to-earnings ratio for this stock
     */
    @ColumnInfo(name = "peRatio")
    private Double peRatio;

    /**
     * The adjusted 52 week high for this stock
     */
    @ColumnInfo(name = "week52High")
    private Double weekHigh;

    /**
     * The adjusted 52 week high for this stock
     */
    @ColumnInfo(name = "week52Low")
    private Double weekLow;

    /**
     * Boolean value for this stock
     */
    @ColumnInfo(name = "isChecked")
    private boolean isChecked = false;

    /**
     * Creates a Stock object with four values
     * @param symbol The symbol for this stock
     * @param companyName The company name for this stock
     * @param latestPrice The latest price for this stock
     * @param changePercent The percent change for this stock
     */
    public Stock(String symbol, String companyName, String latestPrice, String changePercent) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.latestPrice = latestPrice;
        this.changePercent = changePercent;
    }

    /**
     * Creates a Stock object with twelve values
     * @param symbol The symbol for this stock
     * @param companyName The company name for this stock
     * @param open The open price for this stock
     * @param close The close price for this stock
     * @param high The market-wide highest price for the stock
     * @param low The market-wide lowest price for the stock
     * @param latestPrice The latest price for this stock
     * @param changePercent The percent change for this stock
     * @param volume The volume for this stock
     * @param avgTotalVolume The 30 day average volume for this stock
     * @param marketCap The Market capitalization for this stock
     * @param peRatio The price-to-earnings ratio for this stock
     * @param weekHigh The adjusted 52 week high for this stock
     * @param weekLow The adjusted 52 week high for this stock
     */
    @Ignore
    public Stock(@NonNull String symbol, String companyName, Double open, Double close, Double high, Double low, String latestPrice, String changePercent, Double volume, Double avgTotalVolume, Double marketCap, Double peRatio, Double weekHigh, Double weekLow) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.latestPrice = latestPrice;
        this.changePercent = changePercent;
        this.volume = volume;
        this.avgTotalVolume = avgTotalVolume;
        this.marketCap = marketCap;
        this.peRatio = peRatio;
        this.weekHigh = weekHigh;
        this.weekLow = weekLow;
    }

    /**
     * Creates a Stock object with two values
     * @param symbol The symbol for this stock
     * @param companyName The company name for this stock
     */
    @Ignore
    public Stock(String symbol, String companyName) {
        this.symbol = symbol;
        this.companyName = companyName;
    }

    /**
     * Creates a empty Stock object
     */
    @Ignore
    public Stock(){

    }

    @NonNull
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(@NonNull String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public String getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(String latestPrice) {
        this.latestPrice = latestPrice;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getAvgTotalVolume() {
        return avgTotalVolume;
    }

    public void setAvgTotalVolume(Double avgTotalVolume) {
        this.avgTotalVolume = avgTotalVolume;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public Double getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(Double peRatio) {
        this.peRatio = peRatio;
    }

    public Double getWeekHigh() {
        return weekHigh;
    }

    public void setWeekHigh(Double weekHigh) {
        this.weekHigh = weekHigh;
    }

    public Double getWeekLow() {
        return weekLow;
    }

    public void setWeekLow(Double weekLow) {
        this.weekLow = weekLow;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /**
     * Compares this stock with the specified stock for order.
     * @param stock the stock to be compared
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the stock
     */
    @Override
    public int compareTo(Stock stock) {
        return this.symbol.compareTo(stock.symbol);
    }
}
