package at.schn142.stockmarket.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Stock;
import com.anychart.core.stock.Plot;
import com.anychart.data.Table;
import com.anychart.data.TableMapping;
import com.anychart.enums.StockSeriesType;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.model.StockRange;
import at.schn142.stockmarket.StockViewModel;

public class StockActivity extends AppCompatActivity {

    public static final String SYMBOL = "at.schn142.stockmarket.ui.home.SYMBOL";
    public static final String COMPANYNAME = "at.schn142.stockmarket.ui.home.COMPANYNAME";

    public static final String TAG = "StockActivity";


    private StockViewModel mStockViewModel;

    private MaterialButtonToggleGroup toggleGroup;
    private MaterialButton buttonFiveDay;
    private MaterialButton buttonOneMonth;
    private MaterialButton buttonThreeMonth;
    private MaterialButton buttonSixMonth;
    private MaterialButton buttonOneYear;
    private MaterialButton buttonTwoYear;
    private MaterialButton buttonFiveYear;
    private StockRange range;
    private List<DataEntry> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Bundle extras = getIntent().getExtras();
        String symbol = extras.getString(SYMBOL);
        String companyName = extras.getString(COMPANYNAME);

        setTitle(companyName);

        mStockViewModel = new ViewModelProvider(this).get(StockViewModel.class);

        toggleGroup = findViewById(R.id.toggle_button_group);
        buttonFiveDay = findViewById(R.id.button_five_day);
        buttonOneMonth = findViewById(R.id.button_one_month);
        buttonThreeMonth = findViewById(R.id.button_three_month);
        buttonSixMonth = findViewById(R.id.button_six_month);
        buttonOneYear = findViewById(R.id.button_one_year);
        buttonTwoYear = findViewById(R.id.button_two_year);
        buttonFiveYear = findViewById(R.id.button_five_year);

        range = StockRange.threeMonth;

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
                mStockViewModel.getDataEntryForOHLC(symbol, range);
            }
        });

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Table table = Table.instantiate("x");

        //TODO erst nachdem dem betätigen des ersten Toggle-Button erscheint der erste Chart
        mStockViewModel.getData().observe(this, new Observer<List<DataEntry>>() {
            @Override
            public void onChanged(List<DataEntry> dataEntries) {
                data = dataEntries;
                table.addData(data);
            }
        });

        TableMapping mapping = table.mapAs("{open: 'open', high: 'high', low: 'low', close: 'close'}");

        Stock stock = AnyChart.stock();

        Plot plot = stock.plot(0);


        plot.ema(table.mapAs("{value: 'close'}"), 20d, StockSeriesType.LINE);

        plot.ohlc(mapping)
                .name(symbol)
                .legendItem("{\n" +
                        "        iconType: 'rising-falling'\n" +
                        "      }");

        stock.scroller().ohlc(mapping);



        anyChartView.setChart(stock);

    }

}


