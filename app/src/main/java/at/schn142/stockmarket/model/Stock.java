package at.schn142.stockmarket.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public Stock(String symbol, String companyName, String latestPrice, String changePercent) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.latestPrice = latestPrice;
        this.changePercent = changePercent;
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

    @Override
    public int compareTo(Stock stock) {
        return this.symbol.compareTo(stock.symbol);
    }
}
