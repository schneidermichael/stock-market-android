package at.schn142.stockmarket.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.Stock;

/**
 * This class represents CompareAdapter for the class CompareFragment
 *
 * @author michaelschneider
 * @version 1.0
 */
public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.ViewHolder> {


    private final LayoutInflater mInflater;
    private List<Stock> mStocks;

    /**
     * View holder object for each individual element in the list
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);
            imageView = itemView.findViewById(R.id.image_view);

        }

    }

    /**
     * Create a CompareAdapter object
     * @param context of current state of the Activity
     */
    public CompareAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    /**
     * Creates and initializes the ViewHolder and its associated View
     * @param parent into which the new View will be added after it is bound to an adapter position
     * @param viewType of the new View
     * @return a ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.card_view_compare, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Replace/Update the contents of a View
     * @param holder whose contents should be updated
     * @param position of the holder
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(mStocks != null){
            Stock current = mStocks.get(position);

            holder.imageView.setVisibility(current.isChecked() ? View.VISIBLE : View.GONE);
            holder.textView.setText(current.getCompanyName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getSelected().size() < 2) {
                        current.setChecked(!current.isChecked());
                        holder.imageView.setVisibility(current.isChecked() ? View.VISIBLE : View.GONE);
                    } else if (current.isChecked() && getSelected().size() == 2) {
                        current.setChecked(!current.isChecked());
                        holder.imageView.setVisibility(current.isChecked() ? View.VISIBLE : View.GONE);
                    }

                }
            });

        }else{
            holder.textView.setText(R.string.no_word);
            holder.imageView.setVisibility(View.GONE);
        }

    }

    /**
     * Set the dataset of the Adapter.
     * @param stocks containing the data to populate views to be used
     * by RecyclerView.
     */
    public void setStocks(List<Stock> stocks) {
        this.mStocks = stocks;
        notifyDataSetChanged();
    }

    /**
     * Get selected stocks from Recylerview
     * @return a List<Stock>
     */
    public List<Stock> getSelected() {
        List<Stock> selected = new ArrayList<>();
        for (int i = 0; i < mStocks.size(); i++) {
            if (mStocks.get(i).isChecked()) {
                selected.add(mStocks.get(i));
            }
        }
        return selected;
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