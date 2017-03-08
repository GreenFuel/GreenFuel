package com.example.tr.greenfuel.loginRegister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tr.greenfuel.MainActivity;
import com.example.tr.greenfuel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tangpeng on 2017/2/18.
 */

public class LoginActivity extends AppCompatActivity {

    public static final String BASIC_URL = "http://192.168.1.125:8080/lcx/servlet";
    private static final String AIM_URL = "/DriverLogin";

    private Button login;
    private CheckBox agreeProtocol;
    private EditText phoneView, passView;

    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;

    private InputMethodManager inputMethodManager;

    private boolean DEBUG =true;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissProgressDialog();
            if (msg.what == 1) {
                finish();
            }
            if (msg.what == 2) {
                Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);
        initViews();
    }

    public void initViews() {
        phoneView = (EditText) findViewById(R.id.edit_phone);
        passView = (EditText) findViewById(R.id.edit_password);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
        agreeProtocol = (CheckBox) findViewById(R.id.agreeProtocol);
        agreeProtocol.setChecked(true);
        agreeProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                login.setClickable(isChecked);
            }
        });
    }

    private void checkInfo() {
        showProgressDialog();
        final String phone = phoneView.getText().toString().trim();
        final String pass = passView.getText().toString().trim();
        Pattern pattern = Pattern.compile("((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))[0-9]{8}");
        Matcher matcher = pattern.matcher(phone);
        if(DEBUG){
            if(phone.equals("18483652982") && pass.equals("123456")){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
            String ph = sp.getString("phone","");
            String ps = sp.getString("password","");
            if(phone.equals(ph) && pass.equals(ps) && phone !="" && phone != null){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
        if (!matcher.matches()) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
        } else if (pass.equals("")) {
            Toast.makeText(this, "请填写密码", Toast.LENGTH_SHORT).show();
        } else {
            String jsonStr = "{\"driPhone\":" + "\"" + phone + "\"," +
                    "\"driPassword\":" + "\"" + pass + "\"" +
                    "}";
            JSONObject object = null;
            try {
                object = new JSONObject(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("json转换异常");
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASIC_URL + AIM_URL, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int msg = jsonObject.getInt("msg");
                        if (msg == 1) {//登录成功
                            MainActivity.hasLogin = true;
                            handler.sendEmptyMessage(1);
                            int driId = jsonObject.getInt("driId");
                            System.out.println("登录成功");
                        } else {
                            handler.sendEmptyMessage(0);
                            Toast.makeText(LoginActivity.this, jsonObject.getString("notice"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("服务器没反应" + volleyError.getMessage());
                    handler.sendEmptyMessage(2);
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(login.getWindowToken(), 0);
    }

    //转到注册
    public void register(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class).putExtra("fromActivity", 0));
    }

    public void forgetPassword(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class).putExtra("fromActivity", 1));
    }

    public void back(View v) {
        finish();
    }

    //查看服务条款
    public void checkServiceItems(View v) {

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("登录中...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
        }
        //progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
