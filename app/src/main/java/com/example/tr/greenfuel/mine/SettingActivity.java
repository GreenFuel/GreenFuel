package com.example.tr.greenfuel.mine;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.tr.greenfuel.MainActivity;
import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/24.
 */

public class SettingActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(SettingActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (MainActivity.hasLogin) {
            findViewById(R.id.exit_login).setVisibility(View.VISIBLE);
        }
    }

    //应用升级
    public void updateApp(View v) {
        showProgressDialog();
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(1000);
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    //退出登录
    public void exitLogin(View v) {
        new AlertDialog.Builder(this).setMessage("退出登录？").setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.hasLogin = false;
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在检测版本");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void back(View v) {
        finish();
    }
}
