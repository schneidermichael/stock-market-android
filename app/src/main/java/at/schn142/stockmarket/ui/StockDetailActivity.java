package at.schn142.stockmarket.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

    private MaterialButtonToggleGroup toggleGroup;
    private MaterialButton buttonFiveDay;
    private MaterialButton buttonOneMonth;
    private MaterialButton buttonThreeMonth;
    private MaterialButton buttonSixMonth;
    private MaterialButton buttonOneYear;
    private MaterialButton buttonTwoYear;
    private MaterialButton buttonFiveYear;
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

        setTitle(companyName);

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);

        toggleGroup = findViewById(R.id.toggle_button_group);
        buttonFiveDay = findViewById(R.id.button_five_day);
        buttonOneMonth = findViewById(R.id.button_one_month);
        buttonThreeMonth = findViewById(R.id.button_three_month);
        buttonSixMonth = findViewById(R.id.button_six_month);
        buttonOneYear = findViewById(R.id.button_one_year);
        buttonTwoYear = findViewById(R.id.button_two_year);
        buttonFiveYear = findViewById(R.id.button_five_year);

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

        mViewModel.getOLHCDataEntry(symbol, range);

        detailStock = mViewModel.searchStock(symbol);

        valueOpen.setText(Double.toString(detailStock.getOpen()));
        valueHigh.setText(Double.toString(detailStock.getHigh()));
        valueLow.setText(Double.toString(detailStock.getLow()));
        valueVol.setText(Double.toString(detailStock.getVolume()));
        valuePE.setText(Double.toString(detailStock.getPeRatio()));
        valueMkt.setText(Double.toString(detailStock.getMarketCap()));
        valueWH.setText(Double.toString(detailStock.getWeekHigh()));
        valueWL.setText(Double.toString(detailStock.getWeekLow()));
        valueAvg.setText(Double.toString(detailStock.getAvgTotalVolume()));


        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {

            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                if(checkedId == buttonFiveDay.getId() && isChecked == true){

                    range = StockRange.fiveDay;

                }else if (checkedId == buttonOneMonth.getId() && isChecked == true){

                    range = StockRange.oneMonth;

                }else if (checkedId == buttonThreeMonth.getId() && isChecked == true){

                    range = StockRange.threeMonth;

                }else if (checkedId == buttonSixMonth.getId() && isChecked == true){

                    range = StockRange.sixMonth;

                }else if (checkedId == buttonOneYear.getId() && isChecked == true){

                    range = StockRange.oneYear;

                }else if (checkedId == buttonTwoYear.getId() && isChecked == true){

                    range = StockRange.twoYear;

                }else if (checkedId == buttonFiveYear.getId() && isChecked == true){

                    range = StockRange.fiveYear;
                }
                mViewModel.getOLHCDataEntry(symbol, range);
            }
        });

        anyChartView = findViewById(R.id.any_chart_view_stock);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_stock));

        table = Table.instantiate("x");

        mViewModel.getData().observe(this, new Observer<List<DataEntry>>() {
            @Override
            public void onChanged(List<DataEntry> dataEntries) {
                //Funktioniert leider nicht rückwärts
                APIlib.getInstance().setActiveAnyChartView(anyChartView);
                table.addData(dataEntries);

            }
        });



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


