package at.schn142.stockmarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.StockViewModel;
import at.schn142.stockmarket.adapter.StockSearchAdapter;

import static android.widget.LinearLayout.VERTICAL;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "StockSearchActivity";
    public static final String EXTRA_REPLY = "at.schn142.stockmarket.activity.REPLY";

    private StockViewModel mStockViewModel;
    private RecyclerView searchRecyclerView;
    private DividerItemDecoration searchItemDecor;
    private StockSearchAdapter searchAdapter;
    private RecyclerView.LayoutManager searchlayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        searchlayoutManager = new LinearLayoutManager(this);

        searchAdapter = new StockSearchAdapter(this);

        searchRecyclerView.setLayoutManager(searchlayoutManager);

        searchItemDecor = new DividerItemDecoration(this, VERTICAL);
        searchRecyclerView.addItemDecoration(searchItemDecor);

        searchRecyclerView.setAdapter(searchAdapter);

        mStockViewModel = new ViewModelProvider(this).get(StockViewModel.class);

        mStockViewModel.getSearchStock().observe(this, new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {

                searchAdapter.setStocks(stocks);
            }
        });

        searchAdapter.setOnItemClickListener(new StockSearchAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Stock stock = searchAdapter.getStockAtPosition(position);
                Log.d(TAG,stock.getCompanyName());
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
