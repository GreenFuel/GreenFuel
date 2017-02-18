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
 * Created by tangpeng on 2017/2/18.
 */

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private CheckBox agreeProtocol;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    public void initViews(){
        login = (Button)findViewById(R.id.login);
        agreeProtocol = (CheckBox)findViewById(R.id.agreeProtocol);

        agreeProtocol.setChecked(true);
        agreeProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    login.setClickable(isChecked);
            }
        });
    }

    //转到注册
    public void register(View v){

    }

    public void forgetPassword(View v){

    }
    public void back(View v){
        finish();
    }

    //查看服务条款
    public void checkServiceItems(View v){

    }
}
