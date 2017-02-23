package com.example.tr.greenfuel.nearFunction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/23.
 */

public class WeiZhangActivity extends AppCompatActivity {

    private Spinner province;    //省别名
    private EditText carNumber;  //车牌
    private EditText carId; //车辆识别号
    private EditText engineId;  //发动机号

    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_zhang);
        initViews();
    }

    private void initViews(){
        province = (Spinner) findViewById(R.id.province);
        carNumber = (EditText)findViewById(R.id.carNumber);
        carId = (EditText)findViewById(R.id.carId);
        engineId = (EditText)findViewById(R.id.engineId);
    }

    private void initData(){

    }

    //查询违章
    public void searchNow(View v){

    }

    public void back(View v){
        finish();
    }
}
