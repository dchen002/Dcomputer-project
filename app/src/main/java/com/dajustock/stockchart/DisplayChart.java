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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        lineChart = findViewById(R.id.lc);
        initLineChart();

    }


    private void initLineChart(){
        lineChart.animateXY(2000, 2000);
        Description description = new Description();
        description.setText(""); //自定义描述
        lineChart.setDescription(description);
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.WHITE);

        if ( !setData() ) {
            return;
        }

        setXAxis();
        setYAxis();

    }


    private void setYAxis(){
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setDrawLabels(true);

        yAxisLeft.setAxisMaxValue(Collections.max(yValueList)+5);
        yAxisLeft.setAxisMinValue(Collections.min(yValueList)-5);
        yAxisLeft.setGranularity(3f);
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value  + "";
            }
        });

        lineChart.getAxisRight().setEnabled(false);
    }


    private void setXAxis(){

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        //
        final String[] weekStrs = new String[]{"L1", "L2", "L3", "L4", "L5", "L6","L7"};
        xAxis.setLabelCount(weekStrs.length); //
        xAxis.setTextColor(Color.GREEN); //
        xAxis.setTextSize(15f); //
        xAxis.setGranularity(1f); //
        //
        xAxis.setAxisMinimum(-0.1f); //
        //
        xAxis.setTextColor(Color.WHITE);
        //
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return weekStrs[(int) value];
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //
    }


    private boolean setData() {
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

        List<Entry> yVals1 = new ArrayList<>();
        float[] ys1 = new float[7];
        for(int i=0;i<7;i++){
            ys1[i]=historicalClose.get(historicalClose.size()-7+i);
            yValueList.add(ys1[i]);
        }

        //
        if ( historicalOpen.size()<7) {
            return false;
        }
        List<Entry> yVals2 = new ArrayList<>();
        float[] ys2 = new float[7];
        for(int i=0;i<7;i++){
            ys2[i]=historicalOpen.get(historicalOpen.size()-7+i);
            yValueList.add(ys2[i]);
        }
        //
        //List<Entry> yVals3 = new ArrayList<>();
        //float[] ys3= new float[]{28f, 45f, 32f, 48f, 40f, 55f,45f};
        for (int i = 0; i < ys1.length; i++) {
            yVals1.add(new Entry(i, ys1[i]));
            yVals2.add(new Entry(i, ys2[i]));
            //yVals3.add(new Entry(i, ys3[i]));
        }
        //
        LineDataSet lineDataSet1 = new LineDataSet(yVals1, "Close Price");
        LineDataSet lineDataSet2 = new LineDataSet(yVals2, "Open Price");
       // LineDataSet lineDataSet3 = new LineDataSet(yVals3, "Low");
        lineDataSet2.setCircleColor(Color.RED); //
        //lineDataSet3.setCircleColor(Color.GREEN);//
        lineDataSet1.setValueFormatter(new DefaultValueFormatter(2));
        lineDataSet2.setValueFormatter(new DefaultValueFormatter(2));
        lineDataSet1.setCircleRadius(5); //
        lineDataSet2.setCircleRadius(5); //
        //lineDataSet3.setCircleRadius(5); //
        lineDataSet1.setDrawCircleHole(false); //
        lineDataSet2.setDrawCircleHole(false);
        //lineDataSet3.setDrawCircleHole(false);
        lineDataSet2.setColor(Color.RED); //
        //lineDataSet3.setColor(Color.GREEN); //
        //
        lineDataSet1.setValueTextSize(12f);
        lineDataSet2.setValueTextSize(12f);
        //lineDataSet3.setValueTextSize(12f);
        //
        LineData lineData = new LineData(lineDataSet1,lineDataSet2);
        //
        lineData.setValueTextColor(Color.WHITE);
        //
        lineChart.setData(lineData);
        return true;
    }



}
