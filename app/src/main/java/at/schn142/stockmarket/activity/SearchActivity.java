package at.schn142.stockmarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.StockViewModel;
import at.schn142.stockmarket.adapter.StockSearchAdapter;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "StockSearchActivity";
    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private StockViewModel mStockViewModel;
    private RecyclerView recyclerView;
    private StockSearchAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);

        mAdapter = new StockSearchAdapter(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        // Get a new or existing ViewModel from the ViewModelProvider.
        mStockViewModel = new ViewModelProvider(this).get(StockViewModel.class);

        mStockViewModel.getSearchStock().observe(this, new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {
                mAdapter.setStocks(stocks);
            }
        });

        mAdapter.setOnItemClickListener(new StockSearchAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Stock stock = mAdapter.getStockAtPosition(position);
                Intent replyIntent = new Intent();
                if (stock.getCompanyName().isEmpty()) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    mStockViewModel.insert(stock);
                    replyIntent.putExtra(EXTRA_REPLY, stock.getCompanyName());
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }

        });
    }

    private void searchIexCloud(String searchQuery){

        Context context = SearchActivity.this;

        if(searchQuery.equals("")){
            Toast.makeText(context, "Please enter a search term.", Toast.LENGTH_SHORT).show();
        }else{
            mStockViewModel.searchIexCloud(searchQuery);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Type a stocks symbol");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchIexCloud(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }


}
