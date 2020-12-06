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

import java.util.ArrayList;
import java.util.List;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.ViewModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        Bundle extras = getIntent().getExtras();
        String symbolOne = extras.getString(SYMBOLONE);
        String symbolTwo = extras.getString(SYMBOLTWO);

        setTitle(symbolOne+" vs. "+symbolTwo);

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view_compare);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_compare));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title(getString(R.string.cartesian_title));

        cartesian.yAxis(0).title(getString(R.string.cartesian_axis));
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();

        seriesData = mViewModel.getLineChartData(symbolOne,symbolOne);

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name(symbolOne);
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name(symbolTwo);
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }
}