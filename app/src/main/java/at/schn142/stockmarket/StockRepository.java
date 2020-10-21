package at.schn142.stockmarket;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class StockRepository {

 //   private StockDao mStockDao;
    private MutableLiveData<List<Stock>> mAllStocks = new MutableLiveData<>();;

    private Thread mThread;

//    StockRepository(Application application) {
//        StockRoomDatabase db = StockRoomDatabase.getDatabase(application);
//        mStockDao = db.stockDao();
//      //  mAllStocks = mStockDao.getAlphabetizedStocks();
//    }

    MutableLiveData<List<Stock>> getAllStocks() {
        return mAllStocks;
    }

//    void insert(Stock stock) {
//        StockRoomDatabase.databaseWriteExecutor.execute(() -> {
//            mStockDao.insert(stock);
//        });
//    }

    public void searchIexCloud(URL url) {
        Runnable fetchJsonRunnable = new Runnable() {
            @Override
            public void run() {
                try{
                    List<Stock> postList = new ArrayList<>();
                    String response = "";
                    try {
                        response = getResponseFromHttpUrl(url);
                    }catch (FileNotFoundException i){
                        postList.add(new Stock("NOT FOUND","","","0.0"));
                        mAllStocks.postValue(postList);
                        return;
                    }

                        JSONObject json = new JSONObject(response);

                        Stock stock = new Stock(json.getString("symbol"),
                                json.getString("companyName"),
                                json.getString("latestPrice"),
                                json.getString("changePercent"));

                    postList.add(stock);

                    mAllStocks.postValue(postList);

                }catch (IOException | JSONException e){
                    e.printStackTrace();
                }

            }
        };

        // Stop the thread if its initialized
        // If the thread is not working interrupt will do nothing
        // If its working it stops the previous work and starts the
        // new runnable
        if (mThread != null) {
            mThread.interrupt();
        }
        mThread = new Thread(fetchJsonRunnable);
        mThread.start();
    }

    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setRequestMethod("GET");
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }


}
