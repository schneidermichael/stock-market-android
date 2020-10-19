package at.schn142.stockmarket;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.graphics.Color.rgb;

public class StockListActivityAdapter extends RecyclerView.Adapter<StockListActivityAdapter.MyViewHolder> {
    public static final String TAG = "MyAdapter";
    private static DecimalFormat formatFLoat = new DecimalFormat("0.00");

    private JSONArray jsonArray;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView textCardViewSymbol;
        public TextView textCardViewCompanyName;
        public TextView textCardViewLatestPrice;
        public TextView textCardViewChangePercent;

        public MyViewHolder(CardView c){
            super(c);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    Intent intent = new Intent (v.getContext(), StockListDetailActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
            textCardViewSymbol = (TextView) c.findViewById(R.id.cardTextViewSymbol);
            textCardViewCompanyName = (TextView) c.findViewById(R.id.cardTextViewCompanyName);
            textCardViewLatestPrice = (TextView) c.findViewById(R.id.cardTextViewLatestPrice);
            textCardViewChangePercent = (TextView) c.findViewById(R.id.cardTextViewChangePercent);
            cardView = (CardView) c.findViewById(R.id.cardView);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StockListActivityAdapter(JSONArray myDataset) {
        jsonArray = myDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public StockListActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        JSONObject i = null;
        String changePercent= "";
        Float roundChangePercent;
        try {
            i = jsonArray.getJSONObject(position);
            holder.textCardViewSymbol.setText(i.getString("symbol"));
            holder.textCardViewCompanyName.setText(i.getString("companyName"));
            holder.textCardViewLatestPrice.setText(i.getString("latestPrice"));
            changePercent = i.getString("changePercent");
            changePercent = String.valueOf(formatFLoat.format(Float.parseFloat(changePercent)));

            if(changePercent.charAt(0)== '-'){
                holder.textCardViewChangePercent.setBackgroundColor(rgb(243,17,0));
                holder.textCardViewChangePercent.setText(changePercent);
            }else{
                holder.textCardViewChangePercent.setBackgroundColor(rgb(76,175,80));
                holder.textCardViewChangePercent.setText("+"+changePercent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return jsonArray.length();
    }
}
