package at.schn142.stockmarket.model;

import androidx.annotation.NonNull;
import androidx.annotation.TransitionRes;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import kotlin.jvm.Transient;


//SUPPORTS ONLY FOLLOWING DATA TYPES: TEXT, INTEGER, BLOB, REAL and UNDEFINED.

@Entity(tableName = "stock_table")
public class Stock implements Comparable<Stock>{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "symbol")
    private String symbol;

    @ColumnInfo(name = "companyName")
    private String companyName;

    @ColumnInfo(name = "latestPrice")
    private String latestPrice;

    @ColumnInfo(name = "changePercent")
    private String changePercent;

    @ColumnInfo(name = "isChecked")
    private boolean isChecked = false;

    public Stock(String symbol, String companyName, String latestPrice, String changePercent) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.latestPrice = latestPrice;
        this.changePercent = changePercent;
    }

    @Ignore
    public Stock(String symbol, String companyName) {
        this.symbol = symbol;
        this.companyName = companyName;
    }

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

    @Override
    public int compareTo(Stock stock) {
        return this.symbol.compareTo(stock.symbol);
    }
}
