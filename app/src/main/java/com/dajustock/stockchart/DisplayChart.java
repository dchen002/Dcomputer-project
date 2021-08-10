package com.dajustock.stockchart;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.os.StrictMode;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class DisplayChart extends AppCompatActivity {

    List<BigDecimal> historicalQuotes = new ArrayList<>();
    private com.github.mikephil.charting.charts.LineChart lineChart;
    List<Float> yValueList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_chart);
        //connect to Internet
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Hide system default title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //Instantiated line graph component
        lineChart = findViewById(R.id.lc);
        initLineChart();

    }

    
    private void initLineChart(){
        //Adding animation effects
        lineChart.animateXY(2000, 2000);
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.WHITE);

        if ( !setData() ) {
            return;
        }

        setXAxis();
        setYAxis();

    }

    //Set Y-axis data
    private void setYAxis(){
        //set Y-axis on left side
        YAxis yAxisLeft = lineChart.getAxisLeft();
        //Plotting the Y-axis
        yAxisLeft.setDrawAxisLine(true);
        //set label
        yAxisLeft.setDrawLabels(true);
        //Set the maximum value of the Y-axis
        yAxisLeft.setAxisMaxValue(Collections.max(yValueList)+5);
        //Set the minimum value of the Y-axis
        yAxisLeft.setAxisMinValue(Collections.min(yValueList)-5);
        //Set spacing size
        yAxisLeft.setGranularity(3f);
        //set colour
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value  + "";
            }
        });
        //Without right-hand Y-axis enabled
        lineChart.getAxisRight().setEnabled(false);
    }

    //Set X-axis data
    private void setXAxis(){
        //Setting the display chart style
        XAxis xAxis = lineChart.getXAxis();
        //Without plotting the X-axis
        xAxis.setDrawAxisLine(false);
        //Without drawing grid lines
        xAxis.setDrawGridLines(false);
        //set information on xAxis need to display which is last 7 days' stock price.
        final String[] weekStrs = new String[]{"D-1", "D-2", "D-3", "D-4", "D-5", "D-6","D-7"};
        //Set the number of labels
        xAxis.setLabelCount(weekStrs.length);
        xAxis.setTextColor(Color.GREEN);
        xAxis.setTextSize(15f);
        //Set spacing size
        xAxis.setGranularity(1f);
        //Set the minimum value of the X-axis
        xAxis.setAxisMinimum(-0.1f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return weekStrs[(int) value];
            }
        });
        //Show X-axis at the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    //Populating the chart with historical data
    private boolean setData() {
        //Retrieve the stock code entered by the user
        Intent intent = getIntent();
        String message = intent.getStringExtra(ChartSearch.EXTRA_MESSAGE);
        List<Float> historicalClose= new ArrayList<>();
        List<Float> historicalOpen= new ArrayList<>();

        try {
            Stock stock = YahooFinance.get(message, true);
            if (null == stock) {
                System.out.println("No data available for symbol " + message);
                return false;
        }
        //Adding historical data to historicalQuotes
        List<HistoricalQuote> historicalQuotes = stock.getHistory(Interval.DAILY);

        for (HistoricalQuote quote : historicalQuotes) {
            historicalClose.add(quote.getClose().floatValue());
            historicalOpen.add(quote.getOpen().floatValue());
        }
        }catch (IOException e ) {
            System.out.println("Wrong stock code.");
        }


        if ( historicalClose.size() < 7) {
            return false;
        }
        //Add historical closing prices within seven days to the yVals1 list
        List<Entry> yVals1 = new ArrayList<>();
        float[] ys1 = new float[7];
        for(int i=0;i<7;i++){
            ys1[i]=historicalClose.get(historicalClose.size()-7+i);
            yValueList.add(ys1[i]);
        }

        //return false if the historical data size is less than 7
        if ( historicalOpen.size()<7) {
            return false;
        }
        //Add historical opening prices within seven days to the yVals2 list
        List<Entry> yVals2 = new ArrayList<>();
        float[] ys2 = new float[7];
        for(int i=0;i<7;i++){
            ys2[i]=historicalOpen.get(historicalOpen.size()-7+i);
            yValueList.add(ys2[i]);
        }

        for (int i = 0; i < ys1.length; i++) {
            yVals1.add(new Entry(i, ys1[i]));
            yVals2.add(new Entry(i, ys2[i]));
            //yVals3.add(new Entry(i, ys3[i]));
        }
        //Create folded data sets from the data of each set of Entry objects separately
        LineDataSet lineDataSet1 = new LineDataSet(yVals1, "Close Price");
        LineDataSet lineDataSet2 = new LineDataSet(yVals2, "Open Price");
       // LineDataSet lineDataSet3 = new LineDataSet(yVals3, "Low");
        //Set the colour of the dotted circle
        lineDataSet2.setCircleColor(Color.RED);
        lineDataSet1.setValueFormatter(new DefaultValueFormatter(2));
        lineDataSet2.setValueFormatter(new DefaultValueFormatter(2));
        lineDataSet1.setCircleRadius(5);
        lineDataSet2.setCircleRadius(5);
        //Set solid dots
        lineDataSet1.setDrawCircleHole(false);
        lineDataSet2.setDrawCircleHole(false);
        lineDataSet2.setColor(Color.RED);
        lineDataSet1.setValueTextSize(12f);
        lineDataSet2.setValueTextSize(12f);
        //Add each set of folded data to the folded data
        LineData lineData = new LineData(lineDataSet1,lineDataSet2);
        lineData.setValueTextColor(Color.WHITE);
        //Set the line data to the chart
        lineChart.setData(lineData);
        return true;
    }



}
