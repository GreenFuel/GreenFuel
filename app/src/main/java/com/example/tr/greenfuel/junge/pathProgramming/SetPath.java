package com.example.tr.greenfuel.junge.pathProgramming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.tr.greenfuel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetPath extends AppCompatActivity {
    private ListView paths;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>>dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_path);
        paths  = (ListView) findViewById(R.id.path_lists);
        inti();
    }
    public void inti(){
        //初始化历史路径
        dataList = new ArrayList<Map<String,Object>>();
        simpleAdapter = new SimpleAdapter(this,getData(),R.layout.list_paths,new String[]{"item_src","item_text"},
                new int[]{R.id.item_src,R.id.item_text});
        paths.setAdapter(simpleAdapter);
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
}
