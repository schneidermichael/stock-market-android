package at.schn142.stockmarket.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.schn142.stockmarket.model.Stock;

/**
 * DAO for Stock
 *
 * @author michaelschneider
 * @version 1.0
 */
@Dao
public interface StockDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Stock stock);

    @Update
    void update(Stock stock);

    @Delete
    void delete(Stock stock);

    @Query("DELETE FROM stock_table")
    void deleteAll();

    @Query("SELECT * from stock_table ORDER BY symbol ASC")
    LiveData<List<Stock>> getAlphabetizedStocks();

}
