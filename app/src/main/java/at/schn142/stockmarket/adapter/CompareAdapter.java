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

public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<Stock> mStocks;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);

        }

    }

    public CompareAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = mInflater.inflate(R.layout.compare_card_view, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if(mStocks != null){
            Stock current = mStocks.get(position);

            viewHolder.imageView.setVisibility(current.isChecked() ? View.VISIBLE : View.GONE);
            viewHolder.textView.setText(current.getCompanyName());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getSelected().size() < 2) {
                        current.setChecked(!current.isChecked());
                        viewHolder.imageView.setVisibility(current.isChecked() ? View.VISIBLE : View.GONE);
                    } else if (current.isChecked() && getSelected().size() == 2) {
                        current.setChecked(!current.isChecked());
                        viewHolder.imageView.setVisibility(current.isChecked() ? View.VISIBLE : View.GONE);
                    }

                }
            });

        }else{
            viewHolder.textView.setText("No Word");
            viewHolder.imageView.setVisibility(View.GONE);
        }

    }

    public void setStocks(List<Stock> stocks) {
        this.mStocks = stocks;
        notifyDataSetChanged();
    }

    public List<Stock> getSelected() {
        List<Stock> selected = new ArrayList<>();
        for (int i = 0; i < mStocks.size(); i++) {
            if (mStocks.get(i).isChecked()) {
                selected.add(mStocks.get(i));
            }
        }
        return selected;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mStocks != null)
            return mStocks.size();
        else return 0;
    }
}