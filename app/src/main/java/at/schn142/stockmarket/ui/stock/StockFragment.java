package at.schn142.stockmarket.ui.stock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.ui.StockDetailActivity;
import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.ViewModel;
import at.schn142.stockmarket.ui.SearchActivity;
import at.schn142.stockmarket.adapter.StockAdapter;

import static android.widget.LinearLayout.VERTICAL;

/**
 * This class represents StockFragment
 * Show your favorite stocks
 *
 * @author michaelschneider
 * @version 1.0
 */
public class StockFragment extends Fragment {

    public static final int NEW_STOCK_ACTIVITY_REQUEST_CODE = 1;
    public static final String SYMBOL = "at.schn142.stockmarket.ui.home.SYMBOL";
    public static final String COMPANYNAME = "at.schn142.stockmarket.ui.home.COMPANYNAME";

    private ViewModel mViewModel;
    private RecyclerView recyclerView;
    private DividerItemDecoration dividerItemDecoration;
    private StockAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_stock, container, false);

        setHasOptionsMenu(true);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(getActivity());

        String switchPref = sharedPref.getString(getString(R.string.pref_sort),getString(R.string.asc));

        recyclerView =  (RecyclerView) root.findViewById(R.id.recycler_view_stock);
        layoutManager = new LinearLayoutManager(getActivity());

        adapter = new StockAdapter(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        dividerItemDecoration = new DividerItemDecoration(getActivity(), VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);

        mViewModel.getAllStocks().observe(getViewLifecycleOwner(), new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {

                if (switchPref.equals(getString(R.string.asc))){
                    adapter.sortStocksAsc(stocks);
                }else if(switchPref.equals(getString(R.string.desc))){
                    adapter.sortStocksDesc(stocks);
                }else if(switchPref.equals(getString(R.string.high))){
                    adapter.sortStocksLatestPriceHigh(stocks);
                }else if(switchPref.equals(getString(R.string.low))){
                    adapter.sortStocksLatestPriceLow(stocks);
                }
            }
        });

        adapter.setOnItemClickListener(new StockAdapter.ClickListener(){

            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent (view.getContext(), StockDetailActivity.class);
                Stock stock = adapter.getStockAtPosition(position);
                intent.putExtra(SYMBOL,stock.getSymbol());
                intent.putExtra(COMPANYNAME,stock.getCompanyName());
                view.getContext().startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Stock stock = adapter.getStockAtPosition(position);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://finance.yahoo.com/quote/"+stock.getSymbol());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }

        });



        FloatingActionButton fab = root.findViewById(R.id.fab);

        if (isOnline()){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                //Ansonsten ist der Return Code falsch -> Fragment
                getActivity().startActivityForResult(intent, NEW_STOCK_ACTIVITY_REQUEST_CODE);
            }
        });

        }
        else {
            setHasOptionsMenu(false);
            Toast.makeText(getActivity(), R.string.no_internet_stock, Toast.LENGTH_LONG).show();
        }

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Stock stock = adapter.getStockAtPosition(position);

                        Toast.makeText(getActivity(), R.string.deleting +
                                stock.getCompanyName(), Toast.LENGTH_LONG).show();


                        mViewModel.delete(stock);
                    }
                });

        helper.attachToRecyclerView(recyclerView);

        return root;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {

            mViewModel.updateAll();
            Toast.makeText(getActivity(), R.string.update_all, Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.stock_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    //Code from Manage network usage
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}