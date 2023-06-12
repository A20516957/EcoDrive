package com.sid.androsid.ecodrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieChart1 extends AppCompatActivity {
    int gra[] = {50,15,5,30};
    String names[]= { "Siddharth" , "Sachin" , "Bhavya" , "Naitik" };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barchart);

        setupPieChart();
        setupBarChart();
    }

    private void setupBarChart() {

        BarChart barChart;

        barChart = (BarChart) findViewById(R.id.barchart);

        barChart.setDrawBarShadow(true);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(40);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,40f));
        barEntries.add(new BarEntry(2,36f));
        barEntries.add(new BarEntry(3,5f));
        barEntries.add(new BarEntry(4,20f));
        barEntries.add(new BarEntry(5,30f));
        barEntries.add(new BarEntry(6,40f));
        barEntries.add(new BarEntry(7,22f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Data Set 1");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);

        String[] mode = new String[] {"Mon", "Tues", "Wed", "Thur","Fri","Sat","Sun","Mon", "days"};
        XAxis xAxis = barChart.getXAxis();
        xAxis. setValueFormatter(new MyXAxisValueFormatter(mode));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        //xAxis.setAxisMinimum(2);
        //xAxis.setAxisMaximum(4);

    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[]  mvalues;

        public MyXAxisValueFormatter(String[] values){
            this.mvalues = values;
        }


        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mvalues[(int)value];
        }
    }

    private void setupPieChart() {
        //polulating a list of PieEntries;
        List<PieEntry> pieEntries = new ArrayList<>();

        for(int i=0; i < gra.length; i++) {
            pieEntries.add(new PieEntry(gra[i],names[i]));
        }

        PieDataSet dataSet =  new PieDataSet( pieEntries, "Score");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data =  new PieData(dataSet);

        PieChart chart = (PieChart) findViewById(R.id.chart1);
        chart.setData(data);
        chart.invalidate();
    }
}
