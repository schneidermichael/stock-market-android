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

    private LiveData<List<Stock>> mAllStocks;

    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<StockCard>> stockCardList;

    private Thread mThread;

    public StockViewModel(Application application) {
        super(application);
        mRepository = new StockRepository(application);
        mAllStocks = mRepository.getAllStocks();
    }

    LiveData<List<Stock>> getAllStock() {
        return mAllStocks;
    }

    void insert(Stock stock) {
        mRepository.insert(stock);
    }

    //we will call this method to get the data
    public LiveData<List<StockCard>> getStockCard() {
        //if the list is null
        if (stockCardList == null) {
            stockCardList = new MutableLiveData<List<StockCard>>();
            //we will load it asynchronously from server in this method
            loadStockCard();
        }
        //finally we will return the list
        return stockCardList;
    }

    public void loadStockCard() {
        Runnable fetchJsonRunnable = new Runnable() {
            @Override
            public void run() {
                try{
                    String symbol;
                    URL url;
                    String response;
                    JSONObject json;
                    List<StockCard> postList = new ArrayList<>();

                    StockCard a = new StockCard("AAPL","Apple","122.12","0.01383");
                    StockCard b = new StockCard("BA","The Boeing Company","100.2","-0.13456");
                    StockCard c = new StockCard("BE","Berkshire Hathaway Inc.","89.5","20.1344");
                    StockCard d = new StockCard("NI","NIKE, Inc.","20.0","5.9421");
                    List<StockCard> testList = new ArrayList<>();
                    testList.add(a);
                    testList.add(b);
                    testList.add(c);
                    testList.add(d);

                    StockViewModel stockViewModel = null;


                    for(int i=0; i < testList.size();i++){
                        symbol = testList.get(i).getSymbol();
                        url = new URL("https://sandbox.iexapis.com/stable/stock/"+symbol+"/quote?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");
                        response = getResponseFromHttpUrl(url);
                        json = new JSONObject(response);

                        StockCard stock = new StockCard(json.getString("symbol"),
                                                    json.getString("companyName"),
                                                    json.getString("latestPrice"),
                                                    json.getString("changePercent"));
                        postList.add(stock);
                    }
                    stockCardList.postValue(postList);

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
