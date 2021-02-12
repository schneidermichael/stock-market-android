package at.schn142.stockmarket.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Stock;
import com.anychart.core.stock.Plot;
import com.anychart.data.Table;
import com.anychart.data.TableMapping;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.StockRange;
import at.schn142.stockmarket.ViewModel;
import at.schn142.stockmarket.utilities.AppUtils;

/**
 * This class represents StockDetailActivity
 * Show the OLHC Chart from the stock
 *
 * @author michaelschneider
 * @version 1.0
 */
public class StockDetailActivity extends AppCompatActivity {

    public static final String SYMBOL = "at.schn142.stockmarket.ui.home.SYMBOL";
    public static final String COMPANYNAME = "at.schn142.stockmarket.ui.home.COMPANYNAME";

    private ViewModel mViewModel;

    private StockRange range;
    private Table table;
    private AnyChartView anyChartView;
    private TableMapping mapping;
    private Stock stock;
    private Plot plot;

    private MaterialTextView valueOpen;
    private MaterialTextView valueHigh;
    private MaterialTextView valueLow;
    private MaterialTextView valueVol;
    private MaterialTextView valuePE;
    private MaterialTextView valueMkt;
    private MaterialTextView valueWH;
    private MaterialTextView valueWL;
    private MaterialTextView valueAvg;

    private at.schn142.stockmarket.model.Stock detailStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        Bundle extras = getIntent().getExtras();
        String symbol = extras.getString(SYMBOL);
        String companyName = extras.getString(COMPANYNAME);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        String switchPref = sharedPref.getString(getString(R.string.pref_chart), getString(R.string.fiveY));

        setTitle(companyName);

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);

        valueOpen = findViewById(R.id.value_Open);
        valueHigh = findViewById(R.id.value_High);
        valueLow = findViewById(R.id.value_Low);
        valueVol = findViewById(R.id.value_Vol);
        valuePE = findViewById(R.id.value_PE);
        valueMkt = findViewById(R.id.value_Mkt);
        valueWH = findViewById(R.id.value_WH);
        valueWL = findViewById(R.id.value_WL);
        valueAvg = findViewById(R.id.value_Avg);


        range = StockRange.fiveDay;

        if (switchPref.equals(getString(R.string.fiveY))) {
            range = StockRange.fiveYear;
        } else if (switchPref.equals(getString(R.string.twoY))) {
            range = StockRange.twoYear;
        } else if (switchPref.equals(getString(R.string.oneY))) {
            range = StockRange.oneYear;
        } else if (switchPref.equals(getString(R.string.sixM))) {
            range = StockRange.sixMonth;
        } else if (switchPref.equals(getString(R.string.threeM))) {
            range = StockRange.threeMonth;
        } else if (switchPref.equals(getString(R.string.oneM))) {
            range = StockRange.oneMonth;
        } else if (switchPref.equals(getString(R.string.fiveD))) {
            range = StockRange.fiveDay;
        }

        detailStock = mViewModel.searchStock(symbol);

        valueOpen.setText(AppUtils.checkNull(detailStock.getOpen()));
        valueHigh.setText(AppUtils.checkNull(detailStock.getHigh()));
        valueLow.setText(AppUtils.checkNull(detailStock.getLow()));
        valueVol.setText(AppUtils.checkNull(detailStock.getVolume()));
        valuePE.setText(AppUtils.checkNull(detailStock.getPeRatio()));
        valueMkt.setText(AppUtils.checkNull(detailStock.getMarketCap()));
        valueWH.setText(AppUtils.checkNull(detailStock.getWeekHigh()));
        valueWL.setText(AppUtils.checkNull(detailStock.getWeekLow()));
        valueAvg.setText(AppUtils.checkNull(detailStock.getAvgTotalVolume()));

        anyChartView = findViewById(R.id.any_chart_view_stock);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_stock));

        table = Table.instantiate("x");

        mViewModel.getData().observe(this, new Observer<List<DataEntry>>() {
            @Override
            public void onChanged(List<DataEntry> dataEntries) {
                table.addData(dataEntries);

            }
        });

        mViewModel.getOLHCDataEntry(symbol, range);

        mapping = table.mapAs("{open: 'open', high: 'high', low: 'low', close: 'close'}");

        stock = AnyChart.stock();

        plot = stock.plot(0);

        plot.ohlc(mapping)
                .name(symbol)
                .legendItem("{\n" +
                        "        iconType: 'rising-falling'\n" +
                        "      }");

        stock.scroller().ohlc(mapping);

        anyChartView.setChart(stock);
    }

}


