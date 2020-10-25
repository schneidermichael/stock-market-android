package at.schn142.stockmarket;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class StockRepository {

    public static final String TAG = "StockRepository";

    private StockDao mStockDao;

    private MutableLiveData<List<Stock>> mSearchStocks;

    private LiveData<List<Stock>> mAllStocks;

    private Thread mThread;

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

    public void deleteAll()  {
        new deleteAllStocksAsyncTask(mStockDao).execute();
    }

    public void deleteStock(Stock stock)  {
        new deleteStockAsyncTask(mStockDao).execute(stock);
    }

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

    private static class deleteStockAsyncTask extends AsyncTask<Stock, Void, Void> {
        private StockDao mAsyncTaskDao;

        deleteStockAsyncTask(StockDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Stock... params) {
            mAsyncTaskDao.deleteStock(params[0]);
            return null;
        }
    }

    public void searchIexCloud(String searchQuery) {
        Runnable fetchJsonRunnable = new Runnable() {
            @Override
            public void run() {
                try{

                    URL symbolUrl = new URL("https://sandbox.iexapis.com/stable/ref-data/iex/symbols?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");
                    List<Stock> postList = new ArrayList<>();
                    String response = "";
                    try {
                        response = getResponseFromHttpUrl(symbolUrl);
                    }catch (FileNotFoundException i){
                        i.getCause();
                    }

                    JSONArray jsonStocks = new JSONArray(response);
                    JSONObject o = new JSONObject();
                    for(int i = 0; i < jsonStocks.length(); i++)
                    {
                        o = (JSONObject) jsonStocks.get(i);
                        if(o.getString("symbol").contains(searchQuery)){
                            URL stockUrl = new URL("https://sandbox.iexapis.com/stable/stock/"+o.getString("symbol")+"/quote?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");

                            String r = "";
                            try {
                                r = getResponseFromHttpUrl(stockUrl);
                            }catch (FileNotFoundException file){
                                file.getCause();
                            }
                            JSONObject oNew = new JSONObject(r);

                            Stock stock = new Stock(oNew.getString("symbol"),
                                    oNew.getString("companyName"),
                                    oNew.getString("latestPrice"),
                                    oNew.getString("changePercent")
                            );

                            postList.add(stock);
                        }
                    }
                    Log.i(TAG, String.valueOf(postList.size()));
                    mSearchStocks.postValue(postList);

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
