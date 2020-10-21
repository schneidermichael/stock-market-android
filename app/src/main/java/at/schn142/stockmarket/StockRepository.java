package at.schn142.stockmarket;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

class StockRepository {

    private StockDao mStockDao;
    private LiveData<List<Stock>> mAllStocks;

    StockRepository(Application application) {
        StockRoomDatabase db = StockRoomDatabase.getDatabase(application);
        mStockDao = db.stockDao();
        mAllStocks = mStockDao.getAlphabetizedStocks();
    }

    LiveData<List<Stock>> getAllStocks() {
        return mAllStocks;
    }


    void insert(Stock stock) {
        StockRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStockDao.insert(stock);
        });
    }


}
