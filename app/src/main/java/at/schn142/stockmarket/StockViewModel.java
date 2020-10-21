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

    private StockRepository mRepository = new StockRepository();

    private LiveData<List<Stock>> mAllStocks = mRepository.getAllStocks();

    public StockViewModel(Application application) {
        super(application);
    //    mRepository = new StockRepository(application);
    //    mAllStocks = mRepository.getAllStocks();
    }

    LiveData<List<Stock>> getAllStock() {
        return mAllStocks;
    }

 //   void insert(Stock stock) {
 //       mRepository.insert(stock);
 //   }

    public void searchIexCloud(URL url) {
        mRepository.searchIexCloud(url);
    }

}
