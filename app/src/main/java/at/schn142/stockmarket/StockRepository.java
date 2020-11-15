package at.schn142.stockmarket;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.model.StockExchange;

import static android.os.Looper.getMainLooper;

class StockRepository {

    public static final String TAG = "StockRepository";

    private StockDao mStockDao;

    private MutableLiveData<List<Stock>> mSearchStocks;

    private LiveData<List<Stock>> mAllStocks;

    private Thread searchThread;

    private Thread stockThread;

    private Stock mStock;

    StockRepository(Application application) {
        StockRoomDatabase db = StockRoomDatabase.getDatabase(application);
        mStockDao = db.stockDao();
        mAllStocks = mStockDao.getAlphabetizedStocks();
        mSearchStocks = new MutableLiveData<>();
    }

    MutableLiveData<List<Stock>> getSearchStocks() {
        return mSearchStocks;
    }

    LiveData<List<Stock>> getAllStocks() {
        return mAllStocks;
    }

    void insert(Stock stock) {
        StockRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStockDao.insert(stock);
        });
    }

    void update(Stock stock) {
        StockRoomDatabase.databaseWriteExecutor.execute(() -> {
           mStockDao.update(stock);
        });
    }

    public void deleteAll()  {
        new deleteAllStocksAsyncTask(mStockDao).execute();
    }

    public void delete(Stock stock)  {
        new deleteAsyncTask(mStockDao).execute(stock);
    }

    //TODO replace with thread
    private static class deleteAllStocksAsyncTask extends AsyncTask<Void, Void, Void> {
        private StockDao mAsyncTaskDao;

        deleteAllStocksAsyncTask(StockDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


    //TODO replace with thread
    private static class deleteAsyncTask extends AsyncTask<Stock, Void, Void> {
        private StockDao mAsyncTaskDao;

        deleteAsyncTask(StockDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Stock... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public Stock searchStock(String symbol) throws InterruptedException {

        Runnable fetchStockRunnable = new Runnable() {
            @Override
            public void run() {

                try {
                    URL stockUrl = new URL("https://sandbox.iexapis.com/stable/stock/"+symbol+"/quote?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");

                    String response = "";
                    try {
                        response = getResponseFromHttpUrl(stockUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JSONObject object = new JSONObject(response);

                    Stock stock = new Stock(object.getString("symbol"),
                            object.getString("companyName"),
                            object.getString("latestPrice"),
                            object.getString("changePercent")
                    );

                    mStock = stock;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        if (stockThread != null) {
            stockThread.interrupt();
        }
        stockThread = new Thread(fetchStockRunnable);
        stockThread.start();

        stockThread.join();

        return mStock;
    }

    public void searchIexCloud(String searchQuery) {
        Runnable fetchJsonRunnable = new Runnable() {
            @Override
            public void run() {

                try {
                    URL searchUrl = new URL("https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords="+searchQuery+"&apikey=5O7CMDACW9L0M2OQ");
                    List<Stock> postList = new ArrayList<>();
                    String response = "";

                    try {
                        response = getResponseFromHttpUrl(searchUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JSONObject entryPointSearchJSONObject = new JSONObject(response);

                    JSONArray jsonArray = entryPointSearchJSONObject.getJSONArray("bestMatches");

                    JSONObject searchItemJSONObject = new JSONObject();

                    for(int i = 0; i < jsonArray.length(); i++){
                        searchItemJSONObject = (JSONObject) jsonArray.get(i);
                        postList.add(new Stock(searchItemJSONObject.getString("1. symbol"),searchItemJSONObject.getString("2. name")));

                    }

                    //TODO Exception Handling
                    Log.i(TAG, String.valueOf(postList.size()));
                    if (postList.isEmpty()) {
                        postList.clear();
                    }
                        mSearchStocks.postValue(postList);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // Stop the thread if its initialized
        // If the thread is not working interrupt will do nothing
        // If its working it stops the previous work and starts the
        // new runnable
        if (searchThread != null) {
            searchThread.interrupt();
        }
        searchThread = new Thread(fetchJsonRunnable);
        searchThread.start();
    }

    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setRequestMethod("GET");
            //TODO Error in Log
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
