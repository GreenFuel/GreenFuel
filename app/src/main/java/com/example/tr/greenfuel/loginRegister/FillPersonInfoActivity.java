package com.example.tr.greenfuel.loginRegister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/19.
 */

public class FillPersonInfoActivity extends AppCompatActivity {

    private CheckBox agreeProtocol;
    private Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_person_info);
        initViews();
    }

    private void initViews(){
        agreeProtocol = (CheckBox)findViewById(R.id.agreeProtocol);
        register = (Button)findViewById(R.id.register);
        register.setClickable(agreeProtocol.isChecked());

        agreeProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                register.setClickable(isChecked);
            }
        });
    }

    //选择品牌
    public  void choseCarBrand(View v){

    }

    //选择车型
    public  void choseCarType(View v){

    }
    //选择排放标准
    public  void choseEmissionStandard(View v){

    }

    //查看服务条款
    public void checkServiceItems(View v){

    }

    public void back(View v){
      finish();
    }
}
