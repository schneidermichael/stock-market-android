package at.schn142.stockmarket.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.StockViewModel;
import at.schn142.stockmarket.activity.SearchActivity;
import at.schn142.stockmarket.adapter.StockListAdapter;
import at.schn142.stockmarket.ui.settings.SettingsFragment;

public class HomeFragment extends Fragment {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;


    private StockViewModel mStockViewModel;
    private RecyclerView stockRecyclerView;
    private StockListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(getContext());

        String switchPref = sharedPref.getString("pref_sort","asc");
        
        stockRecyclerView = root.findViewById(R.id.stock_recycler_view);
        mAdapter = new StockListAdapter(root.getContext());

        layoutManager = new LinearLayoutManager(root.getContext());
        stockRecyclerView.setLayoutManager(layoutManager);
        stockRecyclerView.setAdapter(mAdapter);

        // Get a new or existing ViewModel from the ViewModelProvider.
        mStockViewModel = new ViewModelProvider(this).get(StockViewModel.class);

        mStockViewModel.getAllStocks().observe(getViewLifecycleOwner(), new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {

                if (switchPref.equals("asc")){
                    mAdapter.sortStocksAsc(stocks);
                }else
                    mAdapter.sortStocksDesc(stocks);
            }
        });


        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), SearchActivity.class);
                //Ansonsten ist der Return Code falsch -> Fragment
                getActivity().startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });


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
                        Stock stock = mAdapter.getStockAtPosition(position);
                        Toast.makeText(root.getContext(), "Deleting " +
                                stock.getCompanyName(), Toast.LENGTH_LONG).show();


                        mStockViewModel.deleteStock(stock);
                    }
                });

        helper.attachToRecyclerView(stockRecyclerView);


        return root;
    }
}