package at.schn142.stockmarket.ui.map;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapviewlite.MapOverlay;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;

import java.util.LinkedList;
import java.util.List;

import at.schn142.stockmarket.PlatformPositioningProvider;
import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.StockExchange;

import static android.os.Looper.getMainLooper;

public class MapFragment extends Fragment {

    public static final String TAG = "MapFragment";


    private MapViewLite mapView;
    private PlatformPositioningProvider platformPositioningProvider;
    private boolean mapLoaded = false;

    private List<StockExchange> stockExchangeList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);

        // Get a MapViewLite instance from the layout.
        mapView = root.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        stockExchangeList = new LinkedList<StockExchange>();
        stockExchangeList.add(new StockExchange("New York Stock Exchange","NYSE",40.706005,-74.008827));

        loadMapScene();

        new Thread(new loadCurrentLocationRunnable(new PlatformPositioningProvider(root.getContext()))).start();

        new Thread(new loadStockExchangeRunnable()).start();

        return root;
    }

    private void generateOverlay(StockExchange s){
        TextView textView = new TextView(getContext());
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setText(s.getName());
        Log.d("OVERLAY", "Textview finished");

        Button btn = new Button(getContext());
        btn.setText("Show Details");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, s.getSymbol());
            }
        });
        Log.d("OVERLAY", "BTN finished");

        LinearLayout linearLayout = new LinearLayout(getContext());
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

                Handler handler = new Handler(getMainLooper());
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

    public class loadCurrentLocationRunnable implements Runnable{

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