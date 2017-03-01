package com.example.tr.greenfuel.drivingAdvice;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.example.tr.greenfuel.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Charts extends AppCompatActivity {
    /**
    private PieChart pieChart;
    private LineChart lineChart;
    private BarChart barChart;
    private String[] polution={"PM2.5","NOx","HC"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        init();
        showcharts();
    }
    public void init(){
        pieChart = (PieChart) findViewById(R.id.pieChart);
        lineChart = (LineChart) findViewById(R.id.lineChart);
        barChart = (BarChart) findViewById(R.id.barChart);
    }
    private void showcharts() {//展现所有的图表
        PieData mPieData = getPieData(4, 100);    //饼状图分析
        showChart_pie(pieChart, mPieData);

        float[] value = valuegetdate(24);//折线分析
        showChart_line(lineChart,24,"PM2.5排放值",value);

        showChart_Bar(barChart,"CO",31,value);


    }
    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        for (int i = 0; i < 3; i++) {
            xValues.add(polution[i]);  //饼块上显示PM2.5、CO、NO、SO3
        }

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%

        float quarterly1 = 22;
        float quarterly2 = 33;
        float quarterly3 = 46;
        //   float quarterly4 = 38;

        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        yValues.add(new Entry(quarterly3, 2));
        //yValues.add(new Entry(quarterly4, 3));

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "排放物"/*显示在比例图上);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        pieDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        // colors.add(Color.rgb(57, 135, 200));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        // data.setValueTextSize(9f);
        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }

    private void showChart_pie(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(60f); // 半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        pieChart.setDescription("排放物比例饼状图");
        pieChart.setDrawCenterText(true); // 饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.setRotationEnabled(true); // 可以手动旋转

        pieChart.setUsePercentValues(true); // 显示成百分比
        pieChart.setCenterText("良好"); // 饼状图中间的文字
        pieChart.setCenterTextColor(Color.rgb(205, 0, 205));
        // 设置数据
        pieChart.setData(pieData);
        Legend mLegend = pieChart.getLegend(); // 设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER); // 最右边显示
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        pieChart.animateXY(100, 100); // 设置动画
    }

    private void showChart_line(LineChart mChart,int count,String name,float v[]){

        mChart.setDescription("时间/h");

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            yVals1.add(new Entry(v[i],i));
        }
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        LineDataSet set1 = new LineDataSet(yVals1, name);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.rgb(240, 240, 30));
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setFillAlpha(40);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setDrawCircleHole(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);
        mChart.setDrawGridBackground(false);

        mChart.setGridBackgroundColor(Color.WHITE);
        mChart.setData(data);
        mChart.getAxisRight().setEnabled(false);//设置右边没有坐标轴
        Legend mLegend = mChart.getLegend(); //
        mLegend.setFormSize(9f);//
        mLegend.setTextColor(Color.BLACK);//
        mChart.animateX(2000); // 设置显示动画
    }

    private void showChart_Bar(BarChart chart,String name,int count,float v[]) {
        // TODO Auto-generated method stub

        chart.setDescription(name);

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 1; i <=count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random()*i) +20;                                                    // 0.1) / 10);
            yVals1.add(new BarEntry((int)val, i));
        }
        BarDataSet set1 = new BarDataSet(yVals1, name);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        // set1.setColor(ColorTemplate.getHoloBlue());
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setDrawValues(false);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        BarData data = new BarData(xVals, dataSets);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);
        chart.invalidate();
        Legend mLegend = chart.getLegend();

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);
        mLegend.setFormSize(6f);
        mLegend.setTextColor(Color.BLACK);
//	      mLegend.setTypeface(mTf);

        chart.animateX(2500);

    }
    private float[] valuegetdate(int i) {
        // TODO Auto-generated method stub
        float[] value = new float[i];
        for(int k=0;k<i;k++){
            value[k]=(int)(1+Math.random()*(10-1+1));
        }
        return value;
    }
    */
}
