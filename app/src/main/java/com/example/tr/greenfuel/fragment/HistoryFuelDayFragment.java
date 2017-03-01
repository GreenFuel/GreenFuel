package com.example.tr.greenfuel.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.tr.greenfuel.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangpeng on 2017/2/28.
 */

public class HistoryFuelDayFragment extends Fragment implements Spinner.OnItemSelectedListener {

    private Spinner spinnerMonth;
    private Spinner spinnerYear;
    private LineChart lineChart;
    private List<String> basicDays;
    private List<String> days;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        initBasicDays();

        View rootView = inflater.inflate(R.layout.fragment_history_fuel_day, null);
        spinnerMonth = (Spinner) rootView.findViewById(R.id.spinner_month);
        spinnerYear = (Spinner) rootView.findViewById(R.id.spinner_year);
        spinnerMonth.setOnItemSelectedListener(this);
        spinnerYear.setOnItemSelectedListener(this);

        lineChart = (LineChart) rootView.findViewById(R.id.line_chart);

        changeDays();   //计算选择的年月对应的天数
        initData();     //初始化chart
        return rootView;
    }

    private void initBasicDays() {
        if (basicDays == null)
            basicDays = new ArrayList<>();
        for (int i = 1; i <= 28; i++) {
            basicDays.add(i + "");
        }
    }

    private void changeDays() {
        String year = (String) spinnerYear.getSelectedItem();
        int yearInt = Integer.parseInt(year.substring(0, year.length() - 1));
        String month = (String) spinnerMonth.getSelectedItem();
        boolean isRunNian = false;

        if ((yearInt % 4 == 0 && yearInt % 100 != 0) || yearInt % 400 == 0) {//是闰年
            isRunNian = true;
        }

        days = new ArrayList<>(basicDays);
        if (month.equals("二月")) {
            if (isRunNian) {
                days.add("29");
            }
        } else {
            days.add("29");
            days.add("30");
            switch (month) {
                case "一月":
                case "三月":
                case "五月":
                case "七月":
                case "八月":
                case "十月":
                case "十二月":
                    days.add("31");
                    break;
            }
        }
    }

    private void initData() {

        List<Entry> entryList = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            entryList.add(new Entry(i, (float) Math.random() * 100));
        }
        //Collections.sort(entryList, new EntryXComparator());  x的坐标值必须是升序

        LineDataSet lineDataSet1 = new LineDataSet(entryList, "油耗");    //对折线的文字说明
        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet1.setColor(Color.GREEN);    //线段颜色
        lineDataSet1.setValueTextColor(Color.RED);   //顶点的颜色
        lineDataSet1.setCircleColor(Color.CYAN);
        lineDataSet1.setCircleColorHole(Color.WHITE);

        setLineChart();

        LineData lineData = new LineData(lineDataSet1);
        lineChart.animateY(3000);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    //设置chart的坐标轴
    private void setLineChart() {
        XAxis xAxis = lineChart.getXAxis(); //x坐标轴
        xAxis.setGranularity(1f);    //设置最小的间隔单位
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value >= days.size())
                    value = days.size() - 1;
                return days.get((int) value);
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  //设置x坐标的位置

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "ml";
            }
        });
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisRight().setEnabled(false);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        changeDays();
        initData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
