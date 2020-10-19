package at.schn142.stockmarket;

import android.app.Application;

import java.util.List;

class StockRepository {

    private StockDao mStockDao;
    private List<Stock> mAllStocks;

    StockRepository(Application application) {
        StockRoomDatabase db = StockRoomDatabase.getDatabase(application);
        mStockDao = db.stockDao();
        mAllStocks = mStockDao.getAlphabetizedStocks();
    }

    List<Stock> getAllStocks() {
        return mAllStocks;
    }

    void insert(Stock stock) {
        StockRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStockDao.insert(stock);
        });
    }
}
