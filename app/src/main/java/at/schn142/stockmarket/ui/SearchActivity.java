package at.schn142.stockmarket.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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
import at.schn142.stockmarket.ViewModel;
import at.schn142.stockmarket.adapter.SearchAdapter;

import static android.widget.LinearLayout.VERTICAL;

/**
 * This class represents SearchActivity
 * Show all searchable stocks
 *
 * @author michaelschneider
 * @version 1.0
 */
public class SearchActivity extends AppCompatActivity {

    public static final String TAG = SearchActivity.class.getName();
    public static final String EXTRA_REPLY = "at.schn142.stockmarket.activity.REPLY";

    private ViewModel mViewModel;
    private RecyclerView recyclerView;
    private DividerItemDecoration dividerItemDecoration;
    private SearchAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        adapter = new SearchAdapter(this);

        recyclerView.setLayoutManager(layoutManager);

        dividerItemDecoration = new DividerItemDecoration(this, VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);

        mViewModel.getSearchStock().observe(this, new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {

                adapter.setStocks(stocks);
            }
        });

        adapter.setOnItemClickListener(new SearchAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) throws InterruptedException {
                Stock stock = adapter.getStockAtPosition(position);
                if (!stock.getSymbol().equalsIgnoreCase("")) {

                    Stock newStock = mViewModel.searchStock(stock.getSymbol());
                    if (newStock != null) {
                        Log.d(TAG, stock.getCompanyName());
                        Intent replyIntent = new Intent();
                        if (stock.getCompanyName().isEmpty()) {
                            setResult(RESULT_CANCELED, replyIntent);
                        } else {

                            mViewModel.insert(newStock);
                            replyIntent.putExtra(EXTRA_REPLY, stock.getCompanyName());
                            setResult(RESULT_OK, replyIntent);
                        }
                        finish();
                    }
                }
                Toast.makeText(SearchActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconified(false);
        
        searchView.setQueryHint(getString(R.string.search_field));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                handler.removeCallbacksAndMessages(null);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.search(query);
                    }
                }, 750);
                return true;
            }
        });
        return true;
    }

}
