package com.example.tr.greenfuel.junge.pathProgramming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.tr.greenfuel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectPosition extends AppCompatActivity implements Inputtips.InputtipsListener {
    private ListView positions;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_position);
        init();
        setAutoSearch();
    }
    public void init(){
        //初始化站点
        positions = (ListView) findViewById(R.id.positions);
        dataList = new ArrayList<Map<String,Object>>();
        simpleAdapter = new SimpleAdapter(this,getData(),R.layout.list_paths,new String[]{"item_src","item_text"},
                new int[]{R.id.item_src,R.id.item_text});
        positions.setAdapter(simpleAdapter);

    }
    private List<Map<String,Object>> getData(){
        for(int i=0;i<20;i++){
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("item_src",R.mipmap.path_flag);
            map.put("item_text", "我的位置 到 城北客运站");
            dataList.add(map);
        }
        return dataList;
    }
    public void setAutoSearch(){
        InputtipsQuery inputtipsQuery = new InputtipsQuery("广场","成都");
        inputtipsQuery.setCityLimit(true);
        Inputtips inputtips = new Inputtips(SelectPosition.this,inputtipsQuery);
        inputtips.setInputtipsListener(this);
        inputtips.requestInputtipsAsyn();
    }
    public void back(View v){ finish(); }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {

    }
}
