package com.example.tr.greenfuel.loginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/19.
 */

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void back(View v){
        finish();
    }

    //
    public void goNextStep(View v){
        startActivity(new Intent(RegisterActivity.this,FillPersonInfoActivity.class));
    }
}
