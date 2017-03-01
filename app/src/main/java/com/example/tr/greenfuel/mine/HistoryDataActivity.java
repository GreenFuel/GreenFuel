package com.example.tr.greenfuel.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/20.
 */

public class HistoryDataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_data);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                System.out.println("异常：" + ex.getMessage());
                ex.printStackTrace(System.out);
                System.out.println(thread.getName());
            }
        });
    }

    //转历史排放数据统计
    public void goHistoryEmission(View v) {
        startActivity(new Intent(HistoryDataActivity.this, HistoryEmissionActivity.class));
    }

    //转历史油耗数据统计
    public void goHistoryFuelConsumption(View v) {
        startActivity(new Intent(HistoryDataActivity.this, HistoryFuelConsumptionActivity.class));
    }

    public void back(View v) {
        finish();
    }
}
