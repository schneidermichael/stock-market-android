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

import at.schn142.stockmarket.model.LineDataEntry;
import at.schn142.stockmarket.model.StockDataEntry;
import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.model.StockRange;

class StockRepository {

    public static final String TAG = "StockRepository";

    private StockDao mStockDao;

    private MutableLiveData<List<Stock>> mSearchStocks;

    private LiveData<List<Stock>> mAllStocks;

    private MutableLiveData<List<DataEntry>> mData;

    private Stock mStock;

    private Thread searchThread;

    private Thread stockThread;

    private Thread stockDataEntryThread;

    private Thread deleteThread;

    private Thread deleteAllThread;

    private Thread lineDataEntryThread;

    StockRepository(Application application) {
        StockRoomDatabase db = StockRoomDatabase.getDatabase(application);
        mStockDao = db.stockDao();
        mAllStocks = mStockDao.getAlphabetizedStocks();
        mSearchStocks = new MutableLiveData<>();
        mData = new MutableLiveData<>();
    }

    MutableLiveData<List<Stock>> getSearchStocks() {
        return mSearchStocks;
    }

    LiveData<List<Stock>> getAllStocks() {
        return mAllStocks;
    }

    MutableLiveData<List<DataEntry>> getData() {
        return mData;
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

    public void deleteAll() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mStockDao.deleteAll();
            }
        };
        if (deleteAllThread != null) {
            deleteAllThread.interrupt();
        }
        deleteAllThread = new Thread(runnable);
        deleteAllThread.start();

    }

    public void delete(Stock stock) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mStockDao.delete(stock);
            }
        };
        if (deleteThread != null) {
            deleteThread.interrupt();
        }
        deleteThread = new Thread(runnable);
        deleteThread.start();

    }

    public void getDataEntryForOHLC(String symbol, StockRange range) {

        Runnable fetchDataRunnable = new Runnable() {
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

                        runnableData.add(new StockDataEntry(stringDate, open, close, high, low));
                    }

                    mData.postValue(runnableData);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if (stockDataEntryThread != null) {
            stockDataEntryThread.interrupt();
        }
        stockDataEntryThread = new Thread(fetchDataRunnable);
        stockDataEntryThread.start();

    }

    public List<DataEntry> getLineChartData(String symbolOne, String symbolTwo) {

        List<DataEntry> data = new ArrayList<>();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                List<DataEntry> runData = new ArrayList<>();

                try {
                    URL symbolOneUrl = new URL("https://sandbox.iexapis.com/stable/stock/" + symbolOne + "/chart/" + StockRange.fiveYear.getText() + "?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");
                    URL symbolTwoUrl = new URL("https://sandbox.iexapis.com/stable/stock/" + symbolTwo + "/chart/" + StockRange.fiveYear.getText() + "?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");

                    String responseSymbolOne = "";
                    String responseSymbolTwo = "";
                    try {
                        responseSymbolOne = getResponseFromHttpUrl(symbolOneUrl);
                        responseSymbolTwo = getResponseFromHttpUrl(symbolTwoUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JSONArray arrayOne = new JSONArray(responseSymbolOne);
                    JSONArray arrayTwo = new JSONArray(responseSymbolTwo);

                    JSONObject objectOne;
                    JSONObject objectTwo;

                    if (arrayOne.length() == arrayTwo.length()) {

                        for (int i = 0; i < arrayOne.length(); i++) {

                            //Array1
                            objectOne = (JSONObject) arrayOne.get(i);
                            String stringDate = objectOne.getString("date");
                            Double closeOne = objectOne.getDouble("close");

                            //Array2
                            objectTwo = (JSONObject) arrayTwo.get(i);
                            Double closeTwo = objectTwo.getDouble("close");

                            data.add(new LineDataEntry(stringDate, closeOne, closeTwo));
                        }

                    } else
                        Log.i("TAG", "Can't compare");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if (lineDataEntryThread != null) {
            lineDataEntryThread.interrupt();
        }
        lineDataEntryThread = new Thread(runnable);
        lineDataEntryThread.start();

        try {
            lineDataEntryThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return data;

    }

    //TODO Einheitliche API verwenden, wegen unterschiedlichen SYMBOLEN
    //TODO Doppelte Aktien markieren oder nicht anzeigen

    public Stock searchStock(String symbol) throws InterruptedException {

        Runnable fetchStockRunnable = new Runnable() {
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
                        Log.i("TREFFER: ", String.valueOf(postList.size()));

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
        if (searchThread != null) {
            searchThread.interrupt();
        }
        searchThread = new Thread(fetchJsonRunnable);
        searchThread.start();
    }

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

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private long stringToLong(String stringDate){
//
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
//        long millisecondsSinceEpoch = LocalDate.parse(stringDate, dateFormatter)
//                .atStartOfDay(ZoneOffset.UTC)
//                .toInstant()
//                .toEpochMilli();
//        return millisecondsSinceEpoch;
//    }


}
