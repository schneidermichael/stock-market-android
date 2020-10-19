package at.schn142.stockmarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class StockListActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    protected JSONArray jsonArray;

    String result;
    String outputResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        loadWebResult();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    private void initDataset(JSONArray retrieveStockDataInJSON) {

        jsonArray = retrieveStockDataInJSON;
        Log.d(TAG, "Dateset sucessfully created");
    }

    private void loadWebResult() {
        ExampleRunnable runnable = new ExampleRunnable("https://sandbox.iexapis.com/stable/stock/market/list/mostactive?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");
        new Thread(runnable).start();
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

    class ExampleRunnable implements Runnable{

        URL url;

        ExampleRunnable(String urlString){
            try {
                url = new URL(urlString);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {

                result = getResponseFromHttpUrl(url);
                outputResult = "";

                JSONArray retrieveStockDataInJSON = new JSONArray(result);

                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Initialize dataset, this data would usually come from a local content provider or
                        // remote server.
                        initDataset(retrieveStockDataInJSON);

                        // specify an adapter
                        mAdapter = new StockListActivityAdapter(jsonArray);
                        recyclerView.setAdapter(mAdapter);
                    }
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }
    }
}