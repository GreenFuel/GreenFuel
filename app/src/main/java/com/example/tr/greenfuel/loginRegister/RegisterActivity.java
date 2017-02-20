package com.example.tr.greenfuel.loginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tr.greenfuel.R;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by tangpeng on 2017/2/19.
 */

public class RegisterActivity extends AppCompatActivity {
    private TextView getCheckCode;  //获取验证码
    private EditText phone, password, passwordAgain, checkCode;

    private static final String APP_KEY = "1b6e5e14dc960";
    private static final String APP_SECRET = "bd2f9b1def8a8fe821e49f330b7f8c11";

    private boolean isChecked = false;  //验证码是否正确
    private int secondCount;    //计秒器
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1)
            {
                if(secondCount == 0){
                    getCheckCode.setTextColor(getResources().getColor(R.color.colorIconBlue));
                    getCheckCode.setText("获取验证码");
                }else{
                    getCheckCode.setText(secondCount+"s后重发");
                    getCheckCode.setTextColor(getResources().getColor(R.color.colorGray2));
                }
            }else if(msg.what == 2){
                checkCode.setHint("已验证通过");
                checkCode.setFocusable(false);
                getCheckCode.setClickable(false);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        //初始化短信验证码SDK
        SMSSDK.initSDK(this, APP_KEY, APP_SECRET);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {//回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,即验证正确
                        System.out.println("验证码验证成功");
                        isChecked = true;
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获得验证码成功
                        System.out.println("EVENT_GET_VERIFICATION_CODE:"+1);
                        boolean smart = (boolean)data;
                        System.out.println("EVENT_GET_VERIFICATION_CODE:"+2);
                        System.out.println("smart:"+smart);
                        if(smart){
                            isChecked = true;
                            handler.sendEmptyMessage(2);
                        }
                        System.out.println(3);
                        System.out.println("获得验证码成功");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//获取支持的国家列表成功，data.toString获取
                        System.out.println("获得国家列表成功");
                    } else {
                        ((Throwable) data).printStackTrace();
                        System.out.println("监听事件异常");
                    }
                }
            }
        };
        //注册短信监听器
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void initViews() {
        getCheckCode = (TextView) findViewById(R.id.getCheckCode);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.set_password);
        passwordAgain = (EditText) findViewById(R.id.confirm_password);
        checkCode = (EditText) findViewById(R.id.checkCode);

        getCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneStr = phone.getText().toString().trim();
                String psdStr = password.getText().toString().trim();
                String psdAgainStr = passwordAgain.getText().toString().trim();
                if (phoneStr != null && phoneStr.length() == 11) {
                    if (psdAgainStr != null && psdAgainStr.equals(psdStr)) {//向服务器请求的服务
                        SMSSDK.getVerificationCode("86", phoneStr);
                        secondCount = 65;
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                while(secondCount > 0){
                                    secondCount--;
                                    handler.sendEmptyMessage(1);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }.start();
                    } else {
                        Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "手机号不全", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    public void back(View v) {
        finish();
    }

    //下一步，先验证输入的短信验证验证码是否正确
    public void goNextStep(View v) {
        String code = checkCode.getText().toString().trim();
        if ((code != null && code.length() == 4)|| !checkCode.isFocusable()) {
            SMSSDK.submitVerificationCode("86", phone.getText().toString().trim(), code);
            if(isChecked) {//是否验证成功
                startActivity(new Intent(RegisterActivity.this, FillPersonInfoActivity.class));
            }else{
                Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "请输入完整的验证码", Toast.LENGTH_SHORT).show();
        }
    }
}
