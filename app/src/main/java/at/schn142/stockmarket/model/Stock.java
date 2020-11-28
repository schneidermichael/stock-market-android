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

    public String getSymbol(){
        return this.symbol;
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
