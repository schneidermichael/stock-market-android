package at.schn142.stockmarket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private List<Stock> mStock;
    private RecyclerView recyclerView;
    private StockListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText mSearchQueryEditText;
    private Button mSearchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mSearchQueryEditText = findViewById(R.id.et_search_bar);
        mSearchButton = findViewById(R.id.button_search);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIexCloud();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new StockListAdapter(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        // Get a new or existing ViewModel from the ViewModelProvider.
        mStockViewModel = new ViewModelProvider(this).get(StockViewModel.class);

        mStockViewModel.getAllStock().observe(this, new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {
                mStock = stocks;
                mAdapter.setStocks(stocks);
            }
        });

    }

    private void searchIexCloud(){

        String searchQuery = mSearchQueryEditText.getText().toString();
        Context context = StockListActivity.this;

        if(searchQuery.equals("")){
            Toast.makeText(context, "Please enter a search term.", Toast.LENGTH_SHORT).show();
        }else{
            URL url = Utility.buildUrl(searchQuery);
//            new QueryGithubAPITask().execute(url);
            mStockViewModel.searchIexCloud(url);

        }

    }


}