package com.example.tr.greenfuel.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/20.
 */

public class HistoryFuelConsumptionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_fuel_consumption);
    }

    public void back(View v){
        finish();
    }
}
