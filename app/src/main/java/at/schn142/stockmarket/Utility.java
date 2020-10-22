package at.schn142.stockmarket;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class Utility {
    public static final String TAG = "Utility";

    public static URL buildUrl() {

        URL url = null;
        try {
            url = new URL("https://sandbox.iexapis.com/stable/ref-data/iex/symbols?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");
            //url = new URL("https://sandbox.iexapis.com/stable/stock/"+searchQuery+"/quote?token=Tsk_88f94b01307d4faba605e2ebbb5a71ae");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(TAG,url.toString());
        return url;
    }
}
