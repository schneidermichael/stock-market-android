package at.schn142.stockmarket;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Stock.class}, version = 1, exportSchema = false)
public abstract class StockRoomDatabase extends RoomDatabase {

    public abstract StockDao stockDao();

    private static volatile StockRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static StockRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StockRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StockRoomDatabase.class, "stock_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

