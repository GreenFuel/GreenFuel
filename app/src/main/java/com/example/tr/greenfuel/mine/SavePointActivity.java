package com.example.tr.greenfuel.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.junge.pathProgramming.MarkerPosition;
import com.example.tr.greenfuel.model.MyPlace;
import com.example.tr.greenfuel.util.DBO;
import com.example.tr.greenfuel.util.MyLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavePointActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnFocusChangeListener, View.OnClickListener {
    private ListView positions;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> dataList;
    private Boolean DEGUG = false;
    private MyLocation myLocation;
    private boolean ISCOLLECT=false;
    private DBO dao;
    private List<MyPlace> myPlaces = new ArrayList<MyPlace>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_point);
        dao = new DBO(this);
        //dao.clearPlace();
        isDebug(DEGUG);
        init();
    }
    public  void isDebug(Boolean debug){
        if(debug){
            dao.init();
            dao.insertToPlace(new MyPlace("城北客运站",30.6562406723,104.0660393993,false));
            dao.insertToPlace(new MyPlace("西南交大",30.5891985869,104.0364842722,true));
            dao.insertToPlace(new MyPlace("城北客运站",30.6562406723,104.0660393993,false));
            dao.insertToPlace(new MyPlace("西南交大",30.5891985869,104.0364842722,true));
            dao.insertToPlace(new MyPlace("城北客运站",30.6562406723,104.0660393993,false));
            dao.insertToPlace(new MyPlace("西南交大",30.5891985869,104.0364842722,true));
        }
    }
    public void init(){
        myLocation = new MyLocation(this);
        //初始化站点
        positions = (ListView) findViewById(R.id.positions);
        dataList = new ArrayList<Map<String,Object>>();
        getCollect();
        //设置list监听
        positions.setOnItemClickListener(this);

    }

    private void getHistorySearch() {
        dataList.clear();
        myPlaces.clear();
        myPlaces = dao.getMyPlace(false);
        for(MyPlace p : myPlaces){
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("item_src",R.drawable.search_input);
            map.put("item_text", p.getName());
            dataList.add(map);
        }
        simpleAdapter = new SimpleAdapter(this,dataList,R.layout.list_paths,new String[]{"item_src","item_text"},
                new int[]{R.id.item_src,R.id.item_text});
        positions.setAdapter(simpleAdapter);
    }
    public void clearPaths(View view){
        dao.clearPlaceCollect();
        getCollect();
    }
    private void getCollect(){
        dataList.clear();
        myPlaces.clear();
        myPlaces = dao.getMyPlace(true);
        for(MyPlace p : myPlaces){
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("item_src",R.drawable.vector_drawable_collection);
            map.put("item_text", p.getName());
            dataList.add(map);
        }

        simpleAdapter = new SimpleAdapter(this,dataList,R.layout.list_paths,new String[]{"item_src","item_text"},
                new int[]{R.id.item_src,R.id.item_text});
        positions.setAdapter(simpleAdapter);
    }

    public void back(View v){ finish(); }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b){

        }
    }

    @Override
    public void onClick(View view) {

    }

    public void openCollect(View v){
        //positions.setAdapter(simpleAdapter2);
        findViewById(R.id.place_onMap).setBackgroundResource(R.color.white);
        findViewById(R.id.myplace).setBackgroundResource(R.color.white);
        if(!ISCOLLECT){
            v.setBackgroundResource(R.color.gary);
            ISCOLLECT = true;
            getCollect();
        }else{
            ISCOLLECT = false;
            v.setBackgroundColor(Color.WHITE);
            getHistorySearch();
        }
    }

    //地图选点
    public void placeOnMap(View view){
        Intent intent = new Intent(this, MarkerPosition.class);
        intent.putExtra("type",2);
        intent.putExtra("Lat",myLocation.getMyLocation().latitude);
        intent.putExtra("Lng",myLocation.getMyLocation().longitude);
        intent.putExtra("startLat2",myLocation.getMyLocation().latitude);
        intent.putExtra("startLng2",myLocation.getMyLocation().longitude);
        startActivity(intent);
        finish();
    }

}
