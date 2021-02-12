package at.schn142.stockmarket;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anychart.chart.common.dataentry.DataEntry;

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

import at.schn142.stockmarket.dao.StockDao;
import at.schn142.stockmarket.model.OLHCDataEntry;
import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.model.StockRange;

/**
 * This class represents Repository
 *
 * @author michaelschneider
 * @version 1.0
 */
class Repository {

    public static final String TAG = Repository.class.getName();

    private StockDao mStockDao;

    private MutableLiveData<List<Stock>> mSearchStocks;

    private LiveData<List<Stock>> mAllStocks;

    private MutableLiveData<List<DataEntry>> mData;

    private Stock mStock;

    private Thread thread;

    /**
     * Creates a Repository object
     * @param application context from app
     */
    Repository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mStockDao = db.stockDao();
        mAllStocks = mStockDao.getAlphabetizedStocks();
        mSearchStocks = new MutableLiveData<>();
        mData = new MutableLiveData<>();
    }

    /**
     * Insert/Add a stock to the Database
     * @param stock to add
     */
    void insert(Stock stock) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mStockDao.insert(stock);
        });
    }

    /**
     * Delete/Remove a stock from the Database
     * @param stock to delete
     */
    public void delete(Stock stock) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mStockDao.delete(stock);
            }
        };
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(runnable);
        thread.start();

    }

    /**
     * Delete all stocks in Database
     */
    public void deleteAll() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mStockDao.deleteAll();
            }
        };
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(runnable);
        thread.start();

    }

    /**
     * Update all stocks in Database
     */
    public void updateAll() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                List<Stock> stocks = getAllStocks().getValue();
                for (Stock s : stocks){

                    try {
                        URL stockUrl = new URL("https://sandbox.iexapis.com/stable/stock/" + s.getSymbol() + "/quote?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");

                        String response = "";
                        try {
                            response = getResponseFromHttpUrl(stockUrl);
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                        Log.i(TAG, response);

                        JSONObject object = new JSONObject(response);

                        Stock stock = new Stock(object.getString("symbol"),
                                object.getString("companyName"),
                                object.getString("latestPrice"),
                                object.getString("changePercent")
                        );

                        update(stock);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Search for stocks with API(alphavantage)
     * @param searchQuery String from search input
     */
    public void search(String searchQuery) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    URL searchUrl = new URL("https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + searchQuery + "&apikey=5O7CMDACW9L0M2OQ");
                    List<Stock> postList = new ArrayList<>();
                    String response = "";

                    try {
                        response = getResponseFromHttpUrl(searchUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JSONObject entryPointSearchJSONObject = new JSONObject(response);
                    JSONArray jsonArray = entryPointSearchJSONObject.optJSONArray("bestMatches");

                    if (entryPointSearchJSONObject != null) {
                        JSONObject searchItemJSONObject = new JSONObject();

                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                searchItemJSONObject = (JSONObject) jsonArray.get(i);

                                postList.add(new Stock(searchItemJSONObject.getString("1. symbol"), searchItemJSONObject.getString("2. name")));

                            }
                        }

                        if (postList.isEmpty() || postList.size() < 1) {
                            postList.clear();
                            if (!searchQuery.isEmpty()) {

                            postList.add((new Stock("", "No results for \"" + searchQuery + "\"")));
                            }
                        }
                        mSearchStocks.postValue(postList);
                    }

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
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(runnable);
        thread.start();
    }


    /**
     * Get all stocks from the search
     * @return MutableLiveData<List<Stock>>
     */
    MutableLiveData<List<Stock>> getSearchStocks() {
        return mSearchStocks;
    }

    /**
     * Get all stocks from the database
     * @return LiveData<List<Stock>>
     */
    LiveData<List<Stock>> getAllStocks() {
        return mAllStocks;
    }

    /**
     * Get all data for the OLHCChartData
     * @return MutableLiveData<List<DataEntry>>
     */
    MutableLiveData<List<DataEntry>> getData() {
        return mData;
    }


    /**
     * Generate a list for the OLHCChartData
     * @param symbol for this stock
     * @param range for this stick
     */
    public void getOLHCDataEntry(String symbol, StockRange range) {

        Runnable runnable = new Runnable() {
            public void run() {

                try {
                    URL stockUrl = new URL("https://sandbox.iexapis.com/stable/stock/" + symbol + "/chart/" + range.getText() + "?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");

                    String response = "";
                    try {
                        response = getResponseFromHttpUrl(stockUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    List<DataEntry> runnableData = new ArrayList<>();

                    JSONArray array = new JSONArray(response);
                    JSONObject object;

                    for (int i = 0; i < array.length(); i++) {
                        object = (JSONObject) array.get(i);
                        String stringDate = object.getString("date");
                        //     Long date = dateToLong(stringDate);
                        Double open = object.getDouble("open");
                        Double close = object.getDouble("close");
                        Double high = object.getDouble("high");
                        Double low = object.getDouble("low");

                        runnableData.add(new OLHCDataEntry(stringDate, open, close, high, low));
                    }

                    mData.postValue(runnableData);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(runnable);
        thread.start();

    }

    /**
     * Get stock for a specific symbol
     * @param symbol for this stock
     * @return a stock
     */
    public Stock searchStock(String symbol) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    URL stockUrl = new URL("https://sandbox.iexapis.com/stable/stock/" + symbol + "/quote?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");

                    String response = "";
                    try {
                        response = getResponseFromHttpUrl(stockUrl);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    Log.i(TAG, response);

                    JSONObject object = new JSONObject(response);

                    Stock stock = new Stock(object.getString("symbol"),
                            object.optString("companyName"),
                            object.optDouble("open"),
                            object.optDouble("close"),
                            object.optDouble("high"),
                            object.optDouble("low"),
                            object.optString("latestPrice"),
                            object.optString("changePercent"),
                            object.optDouble("volume"),
                            object.optDouble("avgTotalVolume"),
                            object.optDouble("marketCap"),
                            object.optDouble("peRatio"),
                            object.optDouble("week52High"),
                            object.optDouble("week52Low")
                            );

                    mStock = stock;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(runnable);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mStock;
    }


    /**
     * Read Response from Url
     * @param url for request
     * @return a String or null
     * @throws IOException
     */
    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Update stock in Database
     * @param stock to update
     */
    void update(Stock stock) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mStockDao.update(stock);
        });
    }

    /**
     * Get all stock details per API
     * @param symbol of stock
     */
    public void getStockDetailData(String symbol) {

    }
}
