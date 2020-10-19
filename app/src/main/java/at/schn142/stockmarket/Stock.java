package at.schn142.stockmarket;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stock_table")
public class Stock {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "symbol")
    private String symbol;

    public Stock(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol(){
        return this.symbol;
    }
}
