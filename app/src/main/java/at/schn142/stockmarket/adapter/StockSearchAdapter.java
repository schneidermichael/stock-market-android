package at.schn142.stockmarket.adapter;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.Stock;

import static android.graphics.Color.rgb;

public class StockSearchAdapter extends RecyclerView.Adapter<StockSearchAdapter.StockSearchHolder> {
    public static final String TAG = "StockListAdapter";
    private static DecimalFormat myFormatter = new DecimalFormat("0.000");
    private static ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Stock> mStocks;

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


    public static class StockSearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView textCardViewSymbol;
        public TextView textCardViewCompanyName;
        public TextView textCardViewLatestPrice;
        public TextView textCardViewChangePercent;

        public StockSearchHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            cardView = (CardView) itemView.findViewById(R.id.searchCardView);
            textCardViewSymbol = (TextView) itemView.findViewById(R.id.cardTextViewSymbol);
            textCardViewCompanyName = (TextView) itemView.findViewById(R.id.cardTextViewCompanyName);
            textCardViewLatestPrice = (TextView) itemView.findViewById(R.id.cardTextViewLatestPrice);
            textCardViewChangePercent = (TextView) itemView.findViewById(R.id.cardTextViewChangePercent);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
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
    public StockSearchAdapter.StockSearchHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View itemView = mInflater.inflate(R.layout.home_card_view, parent, false);
        return new StockSearchAdapter.StockSearchHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(StockSearchAdapter.StockSearchHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (mStocks != null) {
            Stock current = mStocks.get(position);

            holder.textCardViewSymbol.setText(current.getSymbol());
            holder.textCardViewCompanyName.setText(current.getCompanyName());
            holder.textCardViewLatestPrice.setText(current.getLatestPrice());

            String changePercent = "0.00";
            try {
                changePercent = String.valueOf(myFormatter.format(Double.parseDouble(current.getChangePercent())));
            } catch (NumberFormatException n) {
                n.printStackTrace();
            }

            if (current.getChangePercent().charAt(0) == '-') {
                holder.textCardViewChangePercent.setBackgroundColor(rgb(243, 17, 0));
                holder.textCardViewChangePercent.setText(changePercent);
            } else {
                holder.textCardViewChangePercent.setBackgroundColor(rgb(76, 175, 80));
                holder.textCardViewChangePercent.setText("+" + changePercent);
            }


        } else {
            // Covers the case of data not being ready yet.
            holder.textCardViewSymbol.setText("No Word");
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
