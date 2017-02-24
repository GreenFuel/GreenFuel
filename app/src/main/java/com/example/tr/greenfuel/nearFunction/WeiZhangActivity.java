package com.example.tr.greenfuel.nearFunction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/23.
 */

public class WeiZhangActivity extends AppCompatActivity {

    private Spinner province;    //省别名
    private EditText carNumber;  //车牌
    private EditText carId; //车辆识别号
    private EditText engineId;  //发动机号


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_zhang);
        initViews();
    }

    private void initViews() {
        province = (Spinner) findViewById(R.id.province);
        carNumber = (EditText) findViewById(R.id.carNumber);
        carId = (EditText) findViewById(R.id.carId);
        engineId = (EditText) findViewById(R.id.engineId);
    }

    //查询违章
    public void searchNow(View v){
        String provinceStr = (String) province.getSelectedItem();
        String carNumberStr = carNumber.getText().toString().trim();
        String carIdStr = carId.getText().toString().trim();
        String engineIdStr = engineId.getText().toString().trim();

        if(provinceStr != null && carNumberStr != null && carNumberStr.length() == 6 )
        {
            if( carIdStr != null && carIdStr.length() == 17 ){
                if(engineIdStr != null && !engineIdStr.equals("")){
                    Intent intent = new Intent(WeiZhangActivity.this,WeiZhangSearchResultActivity.class);
                    intent.putExtra("province",provinceStr);
                    intent.putExtra("carNumber",carNumberStr);
                    intent.putExtra("carId",carIdStr);
                    intent.putExtra("engineId",engineIdStr);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "请填写发动机号", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "请填全车辆识别号", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "请填全车牌号", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View v){
        finish();
    }
}
