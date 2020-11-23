package at.schn142.stockmarket;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.schn142.stockmarket.model.Stock;

@Database(entities = {Stock.class}, version = 5, exportSchema = false)
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
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
//            databaseWriteExecutor.execute(() -> {
//                // Populate the database in the background.
//                // If you want to start with more words, just add them.
//                StockDao dao = INSTANCE.stockDao();
//                dao.deleteAll();
//
//                Stock stock = new Stock("AAPL","Apple","122.12","0.01383");
//                dao.insert(stock);
//                stock = new Stock("BA","The Boeing Company","100.2","-0.13456");
//                dao.insert(stock);
//                stock = new Stock("BE","Berkshire Hathaway Inc.","89.5","20.1344");
//                dao.insert(stock);
//                stock = new Stock("NI","NIKE, Inc.","20.0","5.9421");
//                dao.insert(stock);
//
//            });
        }
    };
}

