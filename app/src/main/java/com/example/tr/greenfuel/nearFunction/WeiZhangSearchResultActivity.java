package com.example.tr.greenfuel.nearFunction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tr.greenfuel.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tangpeng on 2017/2/23.
 */

public class WeiZhangSearchResultActivity extends AppCompatActivity {

    private static  final  String BASIC_URL = "http://api.jisuapi.com/illegal/query";

    private Intent intent;
    private String province;    //省别名
    private String carNumber;  //车牌
    private String carId; //车辆识别号
    private String engineId;  //发动机号

    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_zhang_result);

        initViews();
        initData();
        requestData();
    }

    private void initViews(){
        listView = (ListView)findViewById(R.id.listview_search_result);
    }

    private void  initData(){
        intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
        province = intent.getStringExtra("province");
        carNumber = intent.getStringExtra("carNumber");
        carId = intent.getStringExtra("carId");
        engineId = intent.getStringExtra("engineId");

        System.out.println("province:"+province+",carNumber:"+carNumber+",carId:"+carId+",engineId:"+engineId);
    }

    private void requestData(){
        String jsonStr = "{\"lsprefix\":" + "\"" + province + "\"," +
                "\"lsnum\":" + "\"" + carNumber + "\"," +
                "\"frameno\":" + "\"" + carId + "\"," +
                "\"engineno\":" + "\"" + engineId + "\""+
                "}";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("json转换异常");
        }
        showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASIC_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println(jsonObject.toString());
                dismissProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("volleyError："+volleyError.getMessage());
                dismissProgressDialog();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在获取数据");
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

    public void back(View v){
        finish();
    }
}
