package com.example.tr.greenfuel.loginRegister;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.loginRegister.aboutCar.CarBrand;
import com.example.tr.greenfuel.loginRegister.aboutCar.CarBrandActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tangpeng on 2017/2/19.
 */

public class FillPersonInfoActivity extends AppCompatActivity {

    private static final String BASIC_URL = "http://192.168.1.128:8080/lcx/servlet";
    private static final String AIM_URL = "/DriverRegister";

    private CheckBox agreeProtocol;
    private Button register;

    private TextView textEmissionType, textCarBrand, textCarType;
    private EditText editUserName;

    private CarBrand parentCar; //品牌
    private CarBrand childCar;  //车型

    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {//注册成功
                Toast.makeText(FillPersonInfoActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {//注册失败
                Toast.makeText(FillPersonInfoActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_person_info);
        initViews();

        requestQueue = Volley.newRequestQueue(this);
    }

    private void initViews() {
        agreeProtocol = (CheckBox) findViewById(R.id.agreeProtocol);
        register = (Button) findViewById(R.id.register);
        register.setClickable(agreeProtocol.isChecked());
        textEmissionType = (TextView) findViewById(R.id.textEmissionType);
        textCarBrand = (TextView) findViewById(R.id.textCarBrand);
        textCarType = (TextView) findViewById(R.id.textCarType);
        editUserName = (EditText) findViewById(R.id.editUserName);

        agreeProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                register.setClickable(isChecked);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameStr = editUserName.getText().toString().trim();
                if (userNameStr != null && !userNameStr.equals("")) {
                    if (childCar != null) {
                        String txtEmission = textEmissionType.getText().toString().trim();
                        if (txtEmission != null && !txtEmission.equals("")) {
                            showProgressDialog();
                            commitPersonInfo(userNameStr, txtEmission);
                        } else {
                            Toast.makeText(FillPersonInfoActivity.this, "请选择排放标准", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(FillPersonInfoActivity.this, "请选择车辆品牌及型号", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FillPersonInfoActivity.this, "请填写昵称", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void commitPersonInfo(String userNameStr, String txtEmission) {//提交资料完成注册
        Intent intent = getIntent();
        String driPhone = intent.getStringExtra("phone");
        String pass = intent.getStringExtra("password");
        String carBrand = parentCar.getBrandName();
        String carType = childCar.getBrandName();

        String jsonStr = "{\"driPhone\":" + "\"" + driPhone + "\"," +
                "\"driName\":" + "\"" + userNameStr + "\"," +
                "\"driPassword\":" + "\"" + pass + "\"," +
                "\"carBrand\":" + "\"" + carBrand + "\"," +
                "\"carType\":" + "\"" + carType + "\"," +
                "\"carEmissionStd\":" + "\"" + txtEmission + "\"" +
                "}";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON转换失败1");
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASIC_URL + AIM_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    System.out.println(jsonObject.toString());
                    int driId = jsonObject.getInt("driId");
                    int msg = jsonObject.getInt("msg");
                    if (msg == 0) {
                        handler.sendEmptyMessage(2);
                        System.out.println("msg值不对");
                    } else if (msg == 1) {
                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("JSON转换失败2");
                }
                dismissProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("onErrorResponse");
//                if(volleyError != null)
//                    System.out.println(volleyError.getMessage());
                handler.sendEmptyMessage(2);
                dismissProgressDialog();
            }
        });
        if (requestQueue != null)
            requestQueue.add(jsonObjectRequest);
    }

    //选择品牌
    public void choseCarBrand(View v) {
        startActivityForResult(new Intent(FillPersonInfoActivity.this, CarBrandActivity.class).putExtra("fromActivity", 1), 1);
    }

    //选择车型
    public void choseCarType(View v) {
        if (parentCar != null && !textCarBrand.getText().toString().trim().equals("")) {
            //System.out.println("传过去的值：" + ",parentid：" + parentCar.getId() + ",carBrand：" + parentCar.getBrandName());
            startActivityForResult(new Intent(FillPersonInfoActivity.this, CarBrandActivity.class).putExtra("fromActivity", 2)
                    .putExtra("parentid", parentCar.getId()).putExtra("carBrand", parentCar.getBrandName()), 2);
        } else {
            Toast.makeText(this, "请先选择车辆品牌", Toast.LENGTH_SHORT).show();
        }
    }

    //选择排放标准
    public void choseEmissionStandard(View v) {
        new AlertDialog.Builder(this).setItems(new String[]{"国一", "国二", "国三", "国四", "国五"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        textEmissionType.setText("国一");
                        break;
                    case 1:
                        textEmissionType.setText("国二");
                        break;
                    case 2:
                        textEmissionType.setText("国三");
                        break;
                    case 3:
                        textEmissionType.setText("国四");
                        break;
                    case 4:
                        textEmissionType.setText("国五");
                        break;
                }
            }
        }).create().show();
    }

    //查看服务条款
    public void checkServiceItems(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            if (requestCode == 1) {//选择汽车品牌
                parentCar = (CarBrand) data.getExtras().getSerializable("selectedCarBrand");
                textCarBrand.setText(parentCar.getBrandName());
                //System.out.println("选取的parentCar："+parentCar);
            }
        }
        if (resultCode == 2) {
            if (requestCode == 2) {//选择汽车类型
                childCar = (CarBrand) data.getExtras().getSerializable("selectedCarBrand");
                textCarType.setText(childCar.getBrandName());
                //System.out.println("选取的childCar："+childCar);
            }
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("注册中...");
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
