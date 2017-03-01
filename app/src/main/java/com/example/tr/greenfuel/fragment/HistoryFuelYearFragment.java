package com.example.tr.greenfuel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tr.greenfuel.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangpeng on 2017/2/28.
 */

public class HistoryFuelYearFragment extends Fragment {

    private BarChart barChart;

    private String[] xAxisName = new String[]{"2010年", "2011年", "2012年", "2013年", "2014年", "2015年", "2016年", "2017年"};

    public HistoryFuelYearFragment() {
    }

    private void setXAxis() {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  //设置x坐标的位置
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisName[(int) value];
            }
        });
        barChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)(value)+"ml";
            }
        });
        barChart.getAxisLeft().setAxisMinimum(0f); // start at zero
        barChart.getAxisRight().setEnabled(false);
    }

    private void initData() {
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < xAxisName.length; i++) {
            barEntries.add(new BarEntry(i, (float) Math.random() * 100));
        }
        BarDataSet dataSet = new BarDataSet(barEntries, "油耗");
        dataSet.setColors(new int[]{R.color.springgreen, R.color.yellow, R.color.red, R.color.deepskyblue}, getActivity());
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f); //由x坐标值已经决定了两个bar之间的间隙为1，所以这里设置bar的宽度为0.9，则bar之间的宽度就为0.1了

        barChart.animateY(2000);    //设置动画效果
        barChart.setData(data);
        barChart.setFitBars(true);  //使所有的bar都能完整显示出来
        barChart.invalidate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history_fuel_year, null);

        barChart = (BarChart) rootView.findViewById(R.id.bar_chart);
        setXAxis();
        initData();
        return rootView;
    }

}
