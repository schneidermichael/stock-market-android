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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.Collections;
import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.Stock;

import static android.graphics.Color.rgb;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHolder> {

    public static final String TAG = "StockListAdapter";

    private static DecimalFormat myFormatter = new DecimalFormat("0.00");
    private static StockListAdapter.ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Stock> mStocks;


    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CardView stockListCardView;
        public TextView stockListTextCardViewSymbol;
        public TextView stockListCardViewCompanyName;
        public TextView stockListCardViewLatestPrice;
        public TextView stockListCardViewChangePercent;

        public ViewHolder(View itemView){

            super(itemView);

            itemView.setOnClickListener(this);

            stockListCardView = itemView.findViewById(R.id.listStockCardView);
            stockListTextCardViewSymbol = (TextView) itemView.findViewById(R.id.listStockCardTextViewSymbol);
            stockListCardViewCompanyName = (TextView) itemView.findViewById(R.id.listStockCardTextViewCompanyName);
            stockListCardViewLatestPrice = (TextView) itemView.findViewById(R.id.listStockCardTextViewLatestPrice);
            stockListCardViewChangePercent = (TextView) itemView.findViewById(R.id.listStockCardTextViewChangePercent);

        }


        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        StockListAdapter.clickListener = clickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StockListAdapter(Context context) {

        mInflater = LayoutInflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StockListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View itemView = mInflater.inflate(R.layout.home_card_view, parent, false);
        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED,8)
                .build();

        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);

        if (mStocks != null) {
            Stock current = mStocks.get(position);

            holder.stockListTextCardViewSymbol.setText(current.getSymbol());
            holder.stockListCardViewCompanyName.setText(current.getCompanyName());
            holder.stockListCardViewLatestPrice.setText(current.getLatestPrice());

            String changePercent = "0.00";
            try {
                changePercent = String.valueOf(myFormatter.format(Double.parseDouble(current.getChangePercent())));
            }catch (NumberFormatException n){
                n.printStackTrace();
            }

            if(current.getChangePercent().charAt(0)== '-'){
                shapeDrawable.setFillColor(ContextCompat.getColorStateList(mInflater.getContext(),R.color.red));
                holder.stockListCardViewChangePercent.setText(changePercent);
            }else{
                shapeDrawable.setFillColor(ContextCompat.getColorStateList(mInflater.getContext(),R.color.green));
                holder.stockListCardViewChangePercent.setText("+"+changePercent);
            }

            ViewCompat.setBackground(holder.stockListCardViewChangePercent,shapeDrawable);

        } else {
            // Covers the case of data not being ready yet.
            holder.stockListTextCardViewSymbol.setText("No Word");
            holder.stockListCardViewCompanyName.setText("No Word");
            holder.stockListCardViewLatestPrice.setText("No Word");
            holder.stockListTextCardViewSymbol.setText("No Word");
        }
    }

    public void sortStocksAsc(List<Stock> stocks) {
        mStocks = stocks;
        notifyDataSetChanged();
    }

    public void sortStocksDesc(List<Stock> stocks){
        Collections.sort(stocks,Collections.reverseOrder());
        mStocks = stocks;
        notifyDataSetChanged();
    }

    public Stock getStockAtPosition (int position) {
        return mStocks.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount(){
        if (mStocks != null)
            return mStocks.size();
        else return 0;
    }

}
