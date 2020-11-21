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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.activity.StockActivity;
import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.StockViewModel;
import at.schn142.stockmarket.activity.SearchActivity;
import at.schn142.stockmarket.adapter.StockListAdapter;

import static android.widget.LinearLayout.VERTICAL;

public class HomeFragment extends Fragment {

    public static final int NEW_STOCK_ACTIVITY_REQUEST_CODE = 1;
    public static final String SYMBOL = "at.schn142.stockmarket.ui.home.SYMBOL";
    public static final String COMPANYNAME = "at.schn142.stockmarket.ui.home.COMPANYNAME";


    private StockViewModel mStockViewModel;
    private RecyclerView stockRecyclerView;
    private DividerItemDecoration stockItemDecor;
    private StockListAdapter stockListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(getActivity());

        String switchPref = sharedPref.getString("pref_sort","asc");
        
        stockRecyclerView =  (RecyclerView) root.findViewById(R.id.stock_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());

        stockListAdapter = new StockListAdapter(getActivity());

        stockRecyclerView.setLayoutManager(layoutManager);

        stockItemDecor = new DividerItemDecoration(getActivity(), VERTICAL);
        stockRecyclerView.addItemDecoration(stockItemDecor);

        stockRecyclerView.setAdapter(stockListAdapter);

        mStockViewModel = new ViewModelProvider(this).get(StockViewModel.class);

        mStockViewModel.getAllStocks().observe(getViewLifecycleOwner(), new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {

                if (switchPref.equals("asc")){
                    stockListAdapter.sortStocksAsc(stocks);
                }else
                    stockListAdapter.sortStocksDesc(stocks);
            }
        });

        stockListAdapter.setOnItemClickListener(new StockListAdapter.ClickListener(){

            @Override
            public void onItemClick(int position, View v) {

                Intent intent = new Intent (v.getContext(), StockActivity.class);
                Stock stock = stockListAdapter.getStockAtPosition(position);
                intent.putExtra(SYMBOL,stock.getSymbol());
                intent.putExtra(COMPANYNAME,stock.getCompanyName());
                v.getContext().startActivity(intent);
            }
        });


        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                //Ansonsten ist der Return Code falsch -> Fragment
                getActivity().startActivityForResult(intent, NEW_STOCK_ACTIVITY_REQUEST_CODE);
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
                        Stock stock = stockListAdapter.getStockAtPosition(position);
                        Toast.makeText(getActivity(), "Deleting " +
                                stock.getCompanyName(), Toast.LENGTH_LONG).show();


                        mStockViewModel.delete(stock);
                    }
                });

        helper.attachToRecyclerView(stockRecyclerView);

        return root;
    }
}