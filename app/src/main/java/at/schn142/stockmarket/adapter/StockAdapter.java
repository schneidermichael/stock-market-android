package at.schn142.stockmarket.adapter;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.Collections;
import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.Stock;

import static android.graphics.Color.rgb;

/**
 * This class represents StockAdapter for the class StockFragment
 *
 * @author michaelschneider
 * @version 1.0
 */
public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    private static DecimalFormat myFormatter = new DecimalFormat("0.00");
    private static StockAdapter.ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Stock> mStocks;

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    /**
     * View holder object for each individual element in the list
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private CardView cardView;
        private TextView textViewSymbol;
        private TextView textViewCompanyName;
        private TextView textViewLatestPrice;
        private TextView textViewChangePercent;

        public ViewHolder(View itemView){

            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            cardView = itemView.findViewById(R.id.stock_card_view);
            textViewSymbol = (TextView) itemView.findViewById(R.id.stock_card_text_view_symbol);
            textViewCompanyName = (TextView) itemView.findViewById(R.id.stock_card_text_view_company_name);
            textViewLatestPrice = (TextView) itemView.findViewById(R.id.stock_card_text_view_latest_price);
            textViewChangePercent = (TextView) itemView.findViewById(R.id.stock_card_text_view_change_percent);

        }


        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onLongClick(view, getAdapterPosition());
            return true;
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        StockAdapter.clickListener = clickListener;
    }



    /**
     * Create a StockAdapter object
     * @param context of current state of the Activity
     */
    public StockAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    /**
     * Creates and initializes the ViewHolder and its associated View
     * @param parent into which the new View will be added after it is bound to an adapter position
     * @param viewType of the new View
     * @return a ViewHolder
     */
    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.card_view_stock, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Replace/Update the contents of a View
     * @param holder whose contents should be updated
     * @param position of the holder
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED,8)
                .build();

        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);

        if (mStocks != null) {
            Stock current = mStocks.get(position);

            holder.textViewSymbol.setText(current.getSymbol());
            holder.textViewCompanyName.setText(current.getCompanyName());
            holder.textViewLatestPrice.setText(current.getLatestPrice());

            String changePercent = "0.00";
            try {

                changePercent = String.valueOf(myFormatter.format(Double.parseDouble(current.getChangePercent())));
            }catch (NumberFormatException n){
                n.printStackTrace();
            }

            if(current.getChangePercent().charAt(0)== '-'){
                shapeDrawable.setFillColor(ContextCompat.getColorStateList(mInflater.getContext(),R.color.red));
                holder.textViewChangePercent.setText(changePercent);
            }else{
                shapeDrawable.setFillColor(ContextCompat.getColorStateList(mInflater.getContext(),R.color.green));
                holder.textViewChangePercent.setText("+"+changePercent);
            }

            ViewCompat.setBackground(holder.textViewChangePercent,shapeDrawable);

        } else {

            holder.textViewSymbol.setText("No Word");
            holder.textViewCompanyName.setText("No Word");
            holder.textViewLatestPrice.setText("No Word");
            holder.textViewSymbol.setText("No Word");
        }
    }

    /**
     * Arrange the dataset of the Adapter in an ascending order
     * @param stocks containing the data to populate views to be used
     * by RecyclerView.
     */
    public void sortStocksAsc(List<Stock> stocks) {
        mStocks = stocks;
        notifyDataSetChanged();
    }

    /**
     * Arrange the dataset of the Adapter in an descending order
     * @param stocks containing the data to populate views to be used
     * by RecyclerView.
     */
    public void sortStocksDesc(List<Stock> stocks){
        Collections.sort(stocks,Collections.reverseOrder());
        mStocks = stocks;
        notifyDataSetChanged();
    }

    /**
     * Get the stock from the Recylerview
     * @param position of the stock in List<Stock>
     * @return a stock
     */
    public Stock getStockAtPosition (int position) {
        return mStocks.get(position);
    }

    /**
     * Get the size of the dataset
     * @return zero or a positive integer
     */
    @Override
    public int getItemCount(){
        if (mStocks != null)
            return mStocks.size();
        else return 0;
    }

}
