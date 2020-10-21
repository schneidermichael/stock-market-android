package at.schn142.stockmarket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class StockListActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private StockViewModel mStockViewModel;

    private RecyclerView recyclerView;
    private StockListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new StockListAdapter(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Get a new or existing ViewModel from the ViewModelProvider.
        mStockViewModel = new ViewModelProvider(this).get(StockViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
//        mStockViewModel.getAllStock().observe(this, new Observer<List<Stock>>() {
//            @Override
//            public void onChanged(@Nullable final List<Stock> stocks) {
//                // Update the cached copy of the stock in the adapter.
//                mAdapter.setStocks(stocks);
//            }
//        });

        mStockViewModel.getStockCard().observe(this, new Observer<List<StockCard>>() {
            @Override
            public void onChanged(List<StockCard> stockCards) {
                mAdapter.setStockCard(stockCards);
                recyclerView.setAdapter(mAdapter);
            }
        });

    }


}