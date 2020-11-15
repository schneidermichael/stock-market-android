package at.schn142.stockmarket.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.Stock;

public class StockSearchAdapter extends RecyclerView.Adapter<StockSearchAdapter.ViewHolder> {

    public static final String TAG = "StockSearchAdapter";

    private static ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Stock> mStocks;

    public interface ClickListener {
        void onItemClick(int position, View v) throws InterruptedException;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView searchCardView;
        public TextView searchTextCardViewSymbol;
        public TextView searchTextCardViewCompanyName;

        public ViewHolder(View itemView) {

            super(itemView);

            itemView.setOnClickListener(this);

            searchCardView = itemView.findViewById(R.id.searchCardView);
            searchTextCardViewSymbol = (TextView) itemView.findViewById(R.id.searchCardTextViewSymbol);
            searchTextCardViewCompanyName = (TextView) itemView.findViewById(R.id.searchCardTextViewCompanyName);

        }


        @Override
        public void onClick(View view) {
            try {
                clickListener.onItemClick(getAdapterPosition(), view);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        StockSearchAdapter.clickListener = clickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StockSearchAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StockSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View itemView = mInflater.inflate(R.layout.search_card_view, parent, false);
        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (mStocks != null) {
            Stock current = mStocks.get(position);

            holder.searchTextCardViewSymbol.setText(current.getSymbol());
            holder.searchTextCardViewCompanyName.setText(current.getCompanyName());

        } else {
            // Covers the case of data not being ready yet.
            holder.searchTextCardViewSymbol.setText("No Word");
            holder.searchTextCardViewCompanyName.setText("No Word");
        }
    }

    public void setStocks(List<Stock> stocks) {
        mStocks = stocks;
        notifyDataSetChanged();
    }

    public Stock getStockAtPosition(int position) {
        return mStocks.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mStocks != null)
            return mStocks.size();
        else return 0;

    }

}
