package com.example.tr.greenfuel.loginRegister.aboutCar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.customView.CustomLetterSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by tangpeng on 2017/2/22.
 */

public class CarBrandActivity extends AppCompatActivity {

    public static final String APPKEY = "64215fcd04e105fd";
    public static final String URL = "http://api.jisuapi.com/car/brand?appkey=" + APPKEY;
    public static final String CAR_TYPE_URL = "http://api.jisuapi.com/car/carlist?appkey=" + APPKEY + "&parentid=";

    private Intent intent;
    private int parentid = 0;    //上级ID
    private int fromActivity = 1;

    private TextView titles;
    private ListView listView;
    private TextView dialogTextView;
    private CustomLetterSearchView customLetterSearchView;  //字母栏
    private List<CarBrand> carBrandList;    //汽车品牌集合
    private CarBrandListViewAdapter carBrandListViewAdapter;

    //private CharacterParser characterParser;

    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {//获取数据成功,已填充carBrandList
                setAdapter();   //初始化CarBrandListViewAdapter
            } else if (msg.what == 2) {//获取数据失败
                Toast.makeText(CarBrandActivity.this, "返回异常数据", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {//数据处理异常
                Toast.makeText(CarBrandActivity.this, "数据处理异常", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {//网络异常，请求失败
                Toast.makeText(CarBrandActivity.this, "网络异常，请求数据失败", Toast.LENGTH_SHORT).show();
            }
            if (msg.what != 1) {
                findViewById(R.id.layout_load_failed).setVisibility(View.VISIBLE);
            }
            dismissProgressDialog();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand);

        initViews();

        intent = getIntent();
        fromActivity = intent.getIntExtra("fromActivity", 1);
        if (fromActivity == 1) {//选择汽车品牌
            titles.setText("车辆品牌");
        } else if (fromActivity == 2) {//选择车型
            titles.setText(intent.getStringExtra("carBrand"));
            parentid = intent.getIntExtra("parentid", 1);
            //System.out.println("接收到的值："+",parentid:"+parentid+",carBrand:"+intent.getStringExtra("carBrand"));
        }

        intiData();
    }

    private void initViews() {
        titles = (TextView) findViewById(R.id.titles);
        listView = (ListView) findViewById(R.id.listview_car_brand);
        dialogTextView = (TextView) findViewById(R.id.textview_dialog_letter);
        customLetterSearchView = (CustomLetterSearchView) findViewById(R.id.customview_letter_search);
        customLetterSearchView.setDialogTextView(dialogTextView);
    }

    private void intiData() {
        //characterParser = CharacterParser.getInstance();
        //String[] carNames = getResources().getStringArray(R.array.car_brand);

        showProgressDialog();   //打开进度框
        carBrandList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        String tempURL = null;
        if (fromActivity == 1) {
            tempURL = URL;
        } else if (fromActivity == 2) {
            tempURL = CAR_TYPE_URL + parentid;
        }
        //System.out.println("tempURL:" + tempURL);
        //创建汽车品牌API请求对象
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, tempURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    String result = jsonObject.getString("result");
                   //System.out.println("status:" + status + ",msg:" + msg + ",\nresult:" + result);
                    if (status.equals("0") && msg.equals("ok")) {
                        if (carBrandList != null)
                            carBrandList.clear();
                        if (fromActivity == 1) {
                            JSONArray jsonArray = new JSONArray(result);
                            System.out.println("品牌：" + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject carJson = jsonArray.getJSONObject(i);
                                CarBrand carBrand = jsonObject2CarBrand(carJson);
                                if (carBrand != null)
                                    carBrandList.add(carBrand);
                            }
                            System.out.println("加载的品牌：" + carBrandList.size());
                        } else if (fromActivity == 2) {
                            JSONArray jsonArray = new JSONArray(result);
                            //System.out.println("是不是为1 --> jsonArray.length():" + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject parentCarJson = jsonArray.getJSONObject(i);
                                String carList = parentCarJson.getString("carlist");
                                JSONArray childCarJson = new JSONArray(carList);
                                System.out.println("carList " + i + " 的长度：" + childCarJson.length());
                                for (int j = 0; j < childCarJson.length(); j++) {
                                    JSONObject carTypeJson = childCarJson.getJSONObject(j);
                                    CarBrand carBrand = jsonObject2CarType(carTypeJson);
                                    if (carBrand != null)
                                        carBrandList.add(carBrand);
                                }
                            }
                            System.out.println("加载的车型:" + carBrandList.size());
                        }
                        handler.sendEmptyMessage(1);
                    } else {
                        System.out.println("返回结果异常");
                        handler.sendEmptyMessage(2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("json转化异常：" + e.getMessage());
                    handler.sendEmptyMessage(3);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handler.sendEmptyMessage(4);
                System.out.println("get请求失败：" + volleyError.getMessage());
            }
        });
        if (requestQueue != null) {
            requestQueue.add(jsonObjectRequest);
        }
//        for (int i = 0; i < carNames.length; i++) {
//            CarBrand carBrand = new CarBrand();
//            carBrand.setBrandName(carNames[i]);
//            String pinyin = characterParser.getSelling(carNames[i]);
//            carBrand.setSortLetter(pinyin.substring(0, 1).toUpperCase());
//            carBrandList.add(carBrand);
//        }
    }

    private void setAdapter() {
        if(fromActivity == 2){
            CharacterParser characterParser = CharacterParser.getInstance();
            for(int i = 0 ;i < carBrandList.size(); i++){
                String pinyin = characterParser.getSelling(carBrandList.get(i).getBrandName());
                carBrandList.get(i).setSortLetter(pinyin.substring(0,1).toUpperCase());
            }
        }
        Collections.sort(carBrandList, new Comparator<CarBrand>() {//对carBrandList的数据按拼音先后排序
            @Override
            public int compare(CarBrand lhs, CarBrand rhs) {
                return lhs.getSortLetter().compareTo(rhs.getSortLetter());
            }
        });
        carBrandListViewAdapter = new CarBrandListViewAdapter(carBrandList, this);
        listView.setAdapter(carBrandListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedCarBrand", carBrandList.get(position));
                intent.putExtras(bundle);
                CarBrandActivity.this.setResult(fromActivity, intent);
                finish();
            }
        });
        customLetterSearchView.setOnTouchingLetterChangedListener(new CustomLetterSearchView.onTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                //将定位listview到该字母处
                int pos = carBrandListViewAdapter.getPositionFromSection(letter.charAt(0));
                //System.out.println("listview - pos:" + pos);
                if (pos != -1) {
                    listView.setSelection(pos);
                }
            }
        });
    }

    //转换为车品牌
    public static CarBrand jsonObject2CarBrand(JSONObject jsonObject) throws JSONException {
        CarBrand carBrand = new CarBrand();
        int depth = jsonObject.getInt("depth");
        if (depth == 1) {
            carBrand.setId(jsonObject.getInt("id"));
            carBrand.setBrandName(jsonObject.getString("name"));
            carBrand.setSortLetter(jsonObject.getString("initial"));
            carBrand.setParentId(jsonObject.getInt("parentid"));
            carBrand.setLogoURL(jsonObject.getString("logo"));
            carBrand.setDepth(jsonObject.getInt("depth"));
            return carBrand;
        }
//        System.out.println("carBrand:" + "id->" + jsonObject.getInt("id") + ",name->" + jsonObject.getString("name") + ",initial->" + jsonObject.getString("initial")
//                + ",parentid->" + jsonObject.getInt("parentid") + ",logo->" + jsonObject.getString("logo") + ",depth->" + jsonObject.getInt("depth"));
        return null;
    }

    //转换为车型
    public static CarBrand jsonObject2CarType(JSONObject jsonObject) throws JSONException {
        CarBrand carBrand = new CarBrand();
        int depth = jsonObject.getInt("depth");
        if (depth == 3) {
            carBrand.setId(jsonObject.getInt("id"));
            carBrand.setBrandName(jsonObject.getString("fullname"));
            carBrand.setSortLetter(jsonObject.getString("initial"));
            carBrand.setParentId(jsonObject.getInt("parentid"));
            carBrand.setLogoURL(jsonObject.getString("logo"));
            carBrand.setDepth(jsonObject.getInt("depth"));
        }
//       System.out.println("carType:" + "id->" + jsonObject.getInt("id") + ",fullname->" + jsonObject.getString("fullname") + ",initial->" + jsonObject.getString("initial")
//                + ",parentid->" + jsonObject.getInt("parentid") + ",logo->" + jsonObject.getString("logo") + ",depth->" + jsonObject.getInt("depth"));
        return carBrand;
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载数据");
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
