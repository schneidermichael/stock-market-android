package at.schn142.stockmarket.ui.compare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.ViewModel;
import at.schn142.stockmarket.ui.CompareActivity;
import at.schn142.stockmarket.adapter.CompareAdapter;
import at.schn142.stockmarket.model.Stock;

/**
 *
 *
 * @author michaelschneider
 * @version 1.0
 */
public class CompareFragment extends Fragment {

    public static final String SYMBOLONE = "at.schn142.stockmarket.ui.compare.SYMBOLONE";
    public static final String SYMBOLTWO = "at.schn142.stockmarket.ui.compare.SYMBOLTWO";
    
    public static final String TAG = "MapFragment";

    private ViewModel mStockViewModel;
    private RecyclerView recyclerView;
    private CompareAdapter adapter;
    private MaterialButton btnSelected;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_compare, container, false);

        btnSelected = (MaterialButton) root.findViewById(R.id.buttonCompare);
        recyclerView = (RecyclerView) root.findViewById(R.id.compare_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        adapter = new CompareAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        mStockViewModel = new ViewModelProvider(this).get(ViewModel.class);

        mStockViewModel.getAllStocks().observe(getViewLifecycleOwner(), new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {

                adapter.setStocks(stocks);
            }
        });

        btnSelected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (adapter.getSelected().size() == 2) {
                    Intent intent = new Intent (view.getContext(), CompareActivity.class);
                    Stock one = adapter.getSelected().get(0);
                    Stock two = adapter.getSelected().get(1);

                    intent.putExtra(SYMBOLONE,one.getSymbol());
                    intent.putExtra(SYMBOLTWO,two.getSymbol());

                    view.getContext().startActivity(intent);
                }
                else if(adapter.getSelected().size() == 0){
                    Toast.makeText(getActivity(), "No selection", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Only one selection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

}