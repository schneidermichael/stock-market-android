package at.schn142.stockmarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.Stock;

/**
 * This class represents SearchAdapter for the class SearchActivity
 *
 * @author michaelschneider
 * @version 1.0
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Stock> mStocks;

    public interface ClickListener {
        void onItemClick(int position, View v) throws InterruptedException;
    }

    /**
     * View holder object for each individual element in the list
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView cardView;
        public TextView textViewSymbol;
        public TextView textViewCompanyName;

        public ViewHolder(View itemView) {

            super(itemView);

            itemView.setOnClickListener(this);

            cardView = itemView.findViewById(R.id.search_card_view);
            textViewSymbol = (TextView) itemView.findViewById(R.id.search_card_text_view_symbol);
            textViewCompanyName = (TextView) itemView.findViewById(R.id.search_card_text_view_company_name);

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
        SearchAdapter.clickListener = clickListener;
    }

    /**
     * Create a SearchAdapter object
     * @param context of current state of the Activity
     */
    public SearchAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Creates and initializes the ViewHolder and its associated View
     * @param parent into which the new View will be added after it is bound to an adapter position
     * @param viewType of the new View
     * @return a ViewHolder
     */
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View itemView = mInflater.inflate(R.layout.card_view_search, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Replace/Update the contents of a View
     * @param holder whose contents should be updated
     * @param position of the holder
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (mStocks != null) {
            Stock current = mStocks.get(position);

            holder.textViewSymbol.setText(current.getSymbol());
            holder.textViewCompanyName.setText(current.getCompanyName());

        } else {
            holder.textViewSymbol.setText("No Word");
            holder.textViewCompanyName.setText("No Word");
        }
    }

    /**
     * Set the dataset of the Adapter.
     * @param stocks containing the data to populate views to be used
     * by RecyclerView.
     */
    public void setStocks(List<Stock> stocks) {
        mStocks = stocks;
        notifyDataSetChanged();
    }

    /**
     * Get the stock from the Recylerview
     * @param position of the stock in List<Stock>
     * @return a stock
     */
    public Stock getStockAtPosition(int position) {
        return mStocks.get(position);
    }


    /**
     * Get the size of the dataset
     * @return zero or a positive integer
     */
    @Override
    public int getItemCount() {
        if (mStocks != null)
            return mStocks.size();
        else return 0;

    }

}
