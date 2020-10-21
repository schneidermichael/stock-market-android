package at.schn142.stockmarket;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StockDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Stock stock);

    @Query("DELETE FROM stock_table")
    void deleteAll();

 //   @Query("SELECT * from stock_table ORDER BY symbol ASC")
 //   LiveData<List<Stock>> getAlphabetizedStocks();

}
