package at.schn142.stockmarket.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapviewlite.MapOverlay;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import at.schn142.stockmarket.PlatformPositioningProvider;
import at.schn142.stockmarket.R;
import at.schn142.stockmarket.StockExchange;

public class MapActivity extends AppCompatActivity {

    public static final String TAG = "MapActivity";

    private MapViewLite mapView;
    private PlatformPositioningProvider platformPositioningProvider;
    private boolean mapLoaded = false;

    private List<StockExchange> stockExchangeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get a MapViewLite instance from the layout.
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        stockExchangeList = new LinkedList<StockExchange>();
        stockExchangeList.add(new StockExchange("New York Stock Exchange","NYSE",40.706005,-74.008827));

        loadMapScene();

        new Thread(new loadCurrentLocationRunnable(new PlatformPositioningProvider(this))).start();

        new Thread(new loadStockExchangeRunnable()).start();

    }

    private void generateOverlay(StockExchange s){
        TextView textView = new TextView(this);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setText(s.getName());
        Log.d("OVERLAY", "Textview finished");

        Button btn = new Button(this);
        btn.setText("Show Details");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, s.getSymbol());
            }
        });
        Log.d("OVERLAY", "BTN finished");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.color.purple_700);
        linearLayout.setPadding(10, 10, 10, 10);
        linearLayout.addView(textView);
        linearLayout.addView(btn);
        Log.d("OVERLAY", "Layout finished");

        GeoCoordinates geoCoordinates = new GeoCoordinates(s.getLatitude(), s.getLongitude());
        Log.d("OVERLAY", "geoCoordinates finished");
        MapOverlay<LinearLayout> mapOverlay = new MapOverlay<>(linearLayout, geoCoordinates);
        Log.d("OVERLAY", "mapOverlay finished");

        mapView.addMapOverlay(mapOverlay);
    }


    private void loadMapScene() {
        // Load a scene from the SDK to render the map with a map style.
        mapView.getMapScene().loadScene(MapStyle.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapScene.ErrorCode errorCode) {
                if (errorCode == null) {
                    mapView.getCamera().setTarget(new GeoCoordinates(48.210033, 16.363449));
                    mapView.getCamera().setZoomLevel(14);
                    mapLoaded = true;

                } else {
                    Log.d(TAG, "onLoadScene failed: " + errorCode.toString());
                }
            }
        });
    }

    class loadStockExchangeRunnable implements Runnable {

        public loadStockExchangeRunnable() {
        }

        @Override
        public void run() {
            try {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (StockExchange s : stockExchangeList){
                            generateOverlay(s);
                        }
                    }
                });

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class loadCurrentLocationRunnable implements Runnable{

        PlatformPositioningProvider platformPositioningProvider;

        loadCurrentLocationRunnable(PlatformPositioningProvider platformPositioningProvider){
            this.platformPositioningProvider = platformPositioningProvider;
        }

        @Override
        public void run() {
            while(!mapLoaded);
            Handler mainHandler = new Handler(getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    platformPositioningProvider.startLocating(new PlatformPositioningProvider.PlatformLocationListener() {
                        @Override
                        public void onLocationUpdated(android.location.Location location) {
                            Log.d(TAG, "LON: " + String.valueOf(location.getLongitude()));
                            Log.d(TAG, "LAT: " + String.valueOf(location.getLatitude()));
                            //mapView.getCamera().setTarget(new GeoCoordinates(location.getLatitude(), location.getLongitude()));
                            //mapView.getCamera().setZoomLevel(14);
                        }
                    });
                }
            });

        }
    }



}