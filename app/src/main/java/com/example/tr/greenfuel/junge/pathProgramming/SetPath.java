package com.example.tr.greenfuel.junge.pathProgramming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.RoutePlan.RestRouteShowActivity;
import com.example.tr.greenfuel.model.MyPaths;
import com.example.tr.greenfuel.util.DBO;
import com.example.tr.greenfuel.util.MyLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetPath extends AppCompatActivity implements View.OnTouchListener, View.OnFocusChangeListener, AdapterView.OnItemClickListener {
    private ListView paths;
    private boolean congestion, cost, hightspeed, avoidhightspeed;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>>dataList;
    private Boolean Debug = true;
    private MyLocation myLocation;
    private LatLng myHome = new LatLng(30.6562406723,104.0660393993);
    private LatLng myCompany = new LatLng(30.5891985869,104.0364842722);
    private DBO dao;
    private List<MyPaths> ps = new ArrayList<MyPaths>();
    EditText origin;
    EditText terminal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_path);
        paths  = (ListView) findViewById(R.id.path_lists);
        inti();
        getHistoryPaths();
    }

    private void getHistoryPaths() {
        ps = new ArrayList<MyPaths>();
        ps = dao.getMyPaths();
        dataList = new ArrayList<Map<String,Object>>();
        dataList.clear();
        for(MyPaths p : ps){
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("item_src",R.mipmap.path_flag);
            map.put("item_text", p.getOriginName()+" 到 "+p.getEndName());
            map.put("imageView",R.mipmap.route);
            dataList.add(map);
        }
        simpleAdapter = new SimpleAdapter(this,dataList,R.layout.list_paths,new String[]{"item_src","item_text","imageView"},
                new int[]{R.id.item_src,R.id.item_text,R.id.imageView});
        paths.setAdapter(simpleAdapter);
        paths.setOnItemClickListener(this);

    }
    public void addMorePaths(){
        dao.insertToPaths(new MyPaths("TEST","TT",30.6562406723,104.0660393993,30.5891985869,104.0364842722));
        dao.insertToPaths(new MyPaths("TEST","TT",30.6562406723,104.0660393993,30.5891985869,104.0364842722));
        dao.insertToPaths(new MyPaths("TEST","TT",30.6562406723,104.0660393993,30.5891985869,104.0364842722));
        dao.insertToPaths(new MyPaths("TEST","TT",30.6562406723,104.0660393993,30.5891985869,104.0364842722));
        dao.insertToPaths(new MyPaths("TEST","TT",30.6562406723,104.0660393993,30.5891985869,104.0364842722));
        dao.insertToPaths(new MyPaths("TEST","TT",30.6562406723,104.0660393993,30.5891985869,104.0364842722));
        dao.insertToPaths(new MyPaths("TEST","TT",30.6562406723,104.0660393993,30.5891985869,104.0364842722));
        dao.insertToPaths(new MyPaths("TEST","TT",30.6562406723,104.0660393993,30.5891985869,104.0364842722));
        dao.insertToPaths(new MyPaths("TEST","TT",30.6562406723,104.0660393993,30.5891985869,104.0364842722));
    }
    public void inti(){
        //初始化历史路径
        dao = new DBO(SetPath.this);
        if(Debug){
            addMorePaths();
        }
        //编辑框设置
        origin = (EditText) findViewById(R.id.origin);
        terminal = (EditText) findViewById(R.id.terminal);
        //terminal.requestFocus();
        String oo = getIntent().getStringExtra("orgin");
        if(oo!=null&&!oo.equals("")){
            origin.setText(getIntent().getStringExtra("orgin"));
        }
        origin.setOnFocusChangeListener(this);
        terminal.setOnFocusChangeListener(this);
        origin.setFocusable(false);
        terminal.setFocusable(false);
        terminal.setInputType(InputType.TYPE_NULL);
        myLocation = new MyLocation(SetPath.this);
    }

    //点击事件
    public void back(View v){
       finish();
    }
    public void setOriginPoint(View v){
        Intent i = new Intent(SetPath.this,SelectPosition.class);
        i.putExtra("POSITION_TYPE",0);

        startActivity(i);
        //finish();
        //startActivity(new Intent(SetPath.this,PoiAroundSearchActivity.class).putExtra("POSITION_TYPE",0));
    }
    public void setEndPoint(View v){
        Intent i = new Intent(SetPath.this,SelectPosition.class);
        i.putExtra("orgin",origin.getText());
        i.putExtra("POSITION_TYPE",1);
        if(getIntent().getDoubleExtra("Lng",0f) == 0f){
            i.putExtra("startLat",myLocation.getMyLocation().latitude);
            i.putExtra("startLng",myLocation.getMyLocation().longitude);
        }else {
            i.putExtra("startLat",getIntent().getDoubleExtra("Lat",0f));
            i.putExtra("startLng",getIntent().getDoubleExtra("Lng",0f));
        }
        i.putExtra("congestion",congestion);
        i.putExtra("cost",cost);
        i.putExtra("hightspeed",hightspeed);
        i.putExtra("avoidhightspeed",avoidhightspeed);
        Log.i("stragy","st:"+congestion+cost+hightspeed+avoidhightspeed);
        startActivity(i);
        finish();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b) {
            switch (view.getId()) {
                case R.id.terminal:
                    //startActivity(new Intent(SetPath.this, SelectPosition.class).putExtra("POSITION_TYPE", 0));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
    public void setStrategy(View view){
        switch (view.getId()){
            case R.id.congestion:
                if(congestion){
                    view.setBackgroundResource(R.color.bgofdefault);
                    congestion = false;
                }else {
                    view.setBackgroundResource(R.color.colorIconBlue);
                    congestion = true;
                }
                break;
            case R.id.cost:
                if(cost){
                    view.setBackgroundResource(R.color.bgofdefault);
                    cost = false;
                }else {
                    view.setBackgroundResource(R.color.colorIconBlue);
                    cost = true;
                }
                break;
            case R.id.hightspeed:
                if(hightspeed){
                    view.setBackgroundResource(R.color.bgofdefault);
                    hightspeed = false;
                }else {
                    if(avoidhightspeed){
                        Toast.makeText(this,"不能同时选择不走高速和高速优先",Toast.LENGTH_SHORT).show();
                    }else {
                        view.setBackgroundResource(R.color.colorIconBlue);
                        hightspeed = true;
                    }
                }
                break;
            case R.id.avoidhightspeed:
                if(avoidhightspeed){
                    view.setBackgroundResource(R.color.bgofdefault);
                    avoidhightspeed = false;
                }else {
                    if (hightspeed){
                        Toast.makeText(this,"不能同时选择不走高速和高速优先",Toast.LENGTH_SHORT).show();
                    }else {
                        view.setBackgroundResource(R.color.colorIconBlue);
                        avoidhightspeed = true;
                    }
                }
                break;
            case R.id.home:
                startNavi(new MyPaths("我的位置","家",myLocation.getMyLocation().latitude,
                        myLocation.getMyLocation().longitude,myHome.latitude,myHome.longitude));
                break;
            case R.id.company:
                startNavi(new MyPaths("我的位置","公司",myLocation.getMyLocation().latitude,
                        myLocation.getMyLocation().longitude,myCompany.latitude,myCompany.longitude));
                break;
        }
    }
    public void clearPaths(View view){
        dao.clearPaths();
        getHistoryPaths();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startNavi(ps.get(i));
    }
    public void startNavi(MyPaths p){
        Log.i("paths","mk----:startLat2"+p.geteLng());
        Intent ii = new Intent(SetPath.this, RestRouteShowActivity.class);
        ii.putExtra("oName",p.getOriginName());
        ii.putExtra("eName",p.getEndName());
        ii.putExtra("endLng",p.geteLng());
        ii.putExtra("endLat",p.geteLat());
        ii.putExtra("startLng",p.getoLng());
        ii.putExtra("startLat",p.getoLat());
        ii.putExtra("congestion",congestion);
        ii.putExtra("cost",cost);
        ii.putExtra("hightspeed",hightspeed);
        ii.putExtra("avoidhightspeed",avoidhightspeed);
        startActivity(ii);
    }
}
