package at.schn142.stockmarket;

import android.app.Application;
import android.util.Log;
import android.widget.ListView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StockViewModel extends AndroidViewModel {

    public static final String TAG = "StockViewModel";

    private StockRepository mRepository;

    private LiveData<List<Stock>> mSearchStocks;

    private LiveData<List<Stock>> mAllStocks;

    public StockViewModel(Application application) {
        super(application);
        mRepository = new StockRepository(application);
        mAllStocks = mRepository.getAllStocks();
        mSearchStocks = mRepository.getSearchStocks();
    }

    LiveData<List<Stock>> getSearchStock() {
        return mSearchStocks;
    }

    LiveData<List<Stock>> getAllStocks() { return mAllStocks;}

    void insert(Stock stock) {
        mRepository.insert(stock);
    }

    public void searchIexCloud(String searchQuery) {
        mRepository.searchIexCloud(searchQuery);
    }

}
