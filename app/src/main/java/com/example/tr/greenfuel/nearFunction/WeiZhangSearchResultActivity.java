package com.example.tr.greenfuel.nearFunction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tr.greenfuel.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2017/2/23.
 */

public class WeiZhangSearchResultActivity extends AppCompatActivity {

    private static  final  String BASIC_URL = "http://api.jisuapi.com/illegal/query";
    private static  final String APP_KEY = "64215fcd04e105fd";
    private Intent intent;
    private String province;    //省别名
    private String carNumber;  //车牌
    private String carId; //车辆识别号
    private String engineId;  //发动机号

    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private String responseResult;  //返回的结果

    private ListView listView;
    private List<Map<String,Object>> mapList;
    private SimpleAdapter simpleAdapter;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgressDialog();
            if(msg.what == 1){//获取到有效数据，初始化simpleAdapter
                initAdapter();
            }
            if(msg.what == 2){//没有违章记录
                TextView tip = (TextView)findViewById(R.id.textview_load_data);
                tip.setText("没有查到违章记录");
                tip.setVisibility(View.VISIBLE);
            }
            if(msg.what == 3){//获取数据失败
                findViewById(R.id.layout_load_failed).setVisibility(View.VISIBLE);
            }
            if(msg.what == 4){//网络异常
                findViewById(R.id.layout_load_failed).setVisibility(View.VISIBLE);
            }
        }
    };

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
        showProgressDialog();
//        String jsonStr = "{\"lsprefix\":" + "\"" + province + "\"," +
//                "\"lsnum\":" + "\"" + carNumber + "\"," +
//                "\"frameno\":" + "\"" + carId + "\"," +
//                "\"engineno\":" + "\"" + engineId + "\","+
//                "\"appkey\":" + "\"" + APP_KEY + "\""+
//                "}";
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(jsonStr);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            System.out.println("json转换异常");
//        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASIC_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println(s);
                responseResult = s;
                if(s == null || s.length() <= 0)
                {
                    handler.sendEmptyMessage(3);
                }else{
                    handler.sendEmptyMessage(1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handler.sendEmptyMessage(4);
                System.out.println("返回结果出错");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("appkey",APP_KEY);
                map.put("lsprefix",province);
                map.put("lsnum",carNumber);
                map.put("frameno",carId);
                map.put("engineno",engineId);
                return map;
            }
        };

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASIC_URL, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                System.out.println(jsonObject.toString());
//                dismissProgressDialog();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                System.out.println("volleyError："+volleyError.getMessage());
//                dismissProgressDialog();
//            }
//        });
//        requestQueue.add(jsonObjectRequest);

        requestQueue.add(stringRequest);
    }

    private void initAdapter(){
        mapList = new ArrayList<>();
        if(responseResult != null){
            try {
                JSONObject jsonObject = new JSONObject(responseResult);
                JSONObject result = jsonObject.getJSONObject("result");
                String listStr = result.getString("list");
                JSONArray jsonArray = new JSONArray(listStr);
                for(int i = 0 ; i < jsonArray.length() ;i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Map<String,Object> map = new HashMap<>();
                    map.put("time",object.getString("time"));
                    map.put("address",object.getString("address"));
                    map.put("content",object.getString("content"));
                    map.put("price",object.getString("price")+"元");
                    map.put("score",object.getString("score")+"分");
                    mapList.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("json处理异常");
            }
        }
        simpleAdapter = new SimpleAdapter(this,mapList,R.layout.listview_item_wei_zhang,new String[]{"time","address","content","price","score"}
                ,new int[]{R.id.textview_time,R.id.textview_address,R.id.textview_content,R.id.textview_fine,R.id.textview_diminish_score});
        listView.setAdapter(simpleAdapter);
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
