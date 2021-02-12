package at.schn142.stockmarket.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.ViewModel;
import at.schn142.stockmarket.model.Stock;
import at.schn142.stockmarket.utilities.AppUtils;

/**
 * This class represents CompareActivity
 * Show the difference between two Stocks in a Chart(Type:Line)
 *
 * @author michaelschneider
 * @version 1.0
 */
public class CompareActivity extends AppCompatActivity {

    public static final String SYMBOLONE = "at.schn142.stockmarket.ui.compare.SYMBOLONE";
    public static final String SYMBOLTWO = "at.schn142.stockmarket.ui.compare.SYMBOLTWO";

    private ViewModel mViewModel;

    private MaterialTextView valueOpen1;
    private MaterialTextView valueHigh1;
    private MaterialTextView valueLow1;
    private MaterialTextView valueVol1;
    private MaterialTextView valuePE1;
    private MaterialTextView valueMkt1;
    private MaterialTextView valueWH1;
    private MaterialTextView valueWL1;
    private MaterialTextView valueAvg1;

    private MaterialTextView valueOpen2;
    private MaterialTextView valueHigh2;
    private MaterialTextView valueLow2;
    private MaterialTextView valueVol2;
    private MaterialTextView valuePE2;
    private MaterialTextView valueMkt2;
    private MaterialTextView valueWH2;
    private MaterialTextView valueWL2;
    private MaterialTextView valueAvg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        Bundle extras = getIntent().getExtras();
        String symbolOne = extras.getString(SYMBOLONE);
        String symbolTwo = extras.getString(SYMBOLTWO);

        setTitle(symbolOne+" vs. "+symbolTwo);

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);

        valueOpen1 = findViewById(R.id.value_symbol1_open);
        valueHigh1  = findViewById(R.id.value_symbol1_high);
        valueLow1  = findViewById(R.id.value_symbol1_low);
        valueVol1  = findViewById(R.id.value_symbol1_vol);
        valuePE1  = findViewById(R.id.value_symbol1_PE);
        valueMkt1  = findViewById(R.id.value_symbol1_Mkt);
        valueWH1  = findViewById(R.id.value_symbol1_WH);
        valueWL1  = findViewById(R.id.value_symbol1_WL);
        valueAvg1  = findViewById(R.id.value_symbol1_Avg);

        valueOpen2  = findViewById(R.id.value_symbol2_open);
        valueHigh2  = findViewById(R.id.value_symbol2_high);
        valueLow2  = findViewById(R.id.value_symbol2_low);
        valueVol2  = findViewById(R.id.value_symbol2_vol);
        valuePE2  = findViewById(R.id.value_symbol2_PE);
        valueMkt2  = findViewById(R.id.value_symbol2_Mkt);
        valueWH2  = findViewById(R.id.value_symbol2_WH);
        valueWL2  = findViewById(R.id.value_symbol2_WL);
        valueAvg2 = findViewById(R.id.value_symbol2_Avg);


        Stock symbolOneStock = mViewModel.searchStock(symbolOne);

        Stock symbolTwoStock = mViewModel.searchStock(symbolTwo);

        valueOpen1.setText(AppUtils.checkNull(symbolOneStock.getOpen()));
        valueHigh1.setText(AppUtils.checkNull(symbolOneStock.getHigh()));
        valueLow1.setText(AppUtils.checkNull(symbolOneStock.getLow()));
        valueVol1.setText(AppUtils.checkNull(symbolOneStock.getVolume()));
        valuePE1.setText(AppUtils.checkNull(symbolOneStock.getPeRatio()));
        valueMkt1.setText(AppUtils.checkNull(symbolOneStock.getMarketCap()));
        valueWH1.setText(AppUtils.checkNull(symbolOneStock.getWeekHigh()));
        valueWL1.setText(AppUtils.checkNull(symbolOneStock.getWeekLow()));
        valueAvg1.setText(AppUtils.checkNull(symbolOneStock.getAvgTotalVolume()));

        valueOpen2.setText(AppUtils.checkNull(symbolTwoStock.getOpen()));
        valueHigh2.setText(AppUtils.checkNull(symbolTwoStock.getHigh()));
        valueLow2.setText(AppUtils.checkNull(symbolTwoStock.getLow()));
        valueVol2.setText(AppUtils.checkNull(symbolTwoStock.getVolume()));
        valuePE2.setText(AppUtils.checkNull(symbolTwoStock.getPeRatio()));
        valueMkt2.setText(AppUtils.checkNull(symbolTwoStock.getMarketCap()));
        valueWH2.setText(AppUtils.checkNull(symbolTwoStock.getWeekHigh()));
        valueWL2.setText(AppUtils.checkNull(symbolTwoStock.getWeekLow()));
        valueAvg2.setText(AppUtils.checkNull(symbolTwoStock.getAvgTotalVolume()));



    }
}