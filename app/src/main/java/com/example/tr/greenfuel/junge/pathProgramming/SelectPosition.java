package com.example.tr.greenfuel.junge.pathProgramming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.poiSearch.PoiByKeyWordsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectPosition extends AppCompatActivity implements Inputtips.InputtipsListener, AdapterView.OnItemClickListener {
    private ListView positions;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> dataList;
    private Intent i;
    private EditText searchText;
    private  int POSITION_TYPE;
    private TextView positionName;
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
        //设置list监听
        positions.setOnItemClickListener(this);
        //初始化输入框
        initEditText();

    }

    private void initEditText() {
        searchText = (EditText) findViewById(R.id.search_text);
        i = SelectPosition.this.getIntent();
        POSITION_TYPE = getIntent().getIntExtra("POSITION_TYPE",0);
        Log.i("POSITION_TYPE","POSITION_TYPE========"+POSITION_TYPE);
        if(POSITION_TYPE == 1){
            searchText.setText("输入终点");
        } else{
            searchText.setText("输入起点");
        }
    }

    private List<Map<String,Object>> getData(){
        for(int i=0;i<20;i++){
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("item_src",R.mipmap.search_input);
            map.put("item_text", "城北客运站");
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        positionName = (TextView)view.findViewById(R.id.item_text);
        //Toast.makeText(view.getContext(),positionName.getText(), Toast.LENGTH_SHORT).show();
        if(POSITION_TYPE == 0){
            Intent intent = new Intent(SelectPosition.this,SetPath.class);
            intent.putExtra("orgin",positionName.getText());
            startActivity(intent);
            //i.putExtra()
        }else{   //开启路径规划
            Intent intent = new Intent(SelectPosition.this,PoiByKeyWordsActivity.class);
            //intent.putExtra("orgin",getIntent().getStringExtra("orgin"));
            //intent.putExtra("terminal",positionName.getText());
            startActivity(intent);
        }
    }
}
