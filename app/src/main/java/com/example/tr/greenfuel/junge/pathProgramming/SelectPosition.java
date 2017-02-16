package com.example.tr.greenfuel.junge.pathProgramming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.poiSearch.PoiByKeyWordsActivity;
import com.example.tr.greenfuel.util.MyLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectPosition extends AppCompatActivity implements Inputtips.InputtipsListener, AdapterView.OnItemClickListener, View.OnFocusChangeListener, View.OnClickListener, TextWatcher {
    private ListView positions;
    private ListView auto_poi_name;
    private SimpleAdapter simpleAdapter;
    private LinearLayout poiList;
    private List<Map<String,Object>> dataList;
    private Intent i;
    private EditText searchText;
    private  int POSITION_TYPE;
    private TextView positionName;
    private MyLocation myLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_position);
        init();
    }
    public void init(){
        myLocation = new MyLocation(SelectPosition.this);
        //初始化站点
        positions = (ListView) findViewById(R.id.positions);
        auto_poi_name = (ListView) findViewById(R.id.auto_poi_name);
        dataList = new ArrayList<Map<String,Object>>();
        simpleAdapter = new SimpleAdapter(this,getData(),R.layout.list_paths,new String[]{"item_src","item_text"},
                new int[]{R.id.item_src,R.id.item_text});
        positions.setAdapter(simpleAdapter);
        //设置list监听
        positions.setOnItemClickListener(this);
        auto_poi_name.setOnItemClickListener(this);
        poiList = (LinearLayout) findViewById(R.id.poiList);
        poiList.setVisibility(View.INVISIBLE);
        //初始化输入框
        initEditText();

    }

    private void initEditText() {
        searchText = (EditText) findViewById(R.id.search_text);
        i = SelectPosition.this.getIntent();
        POSITION_TYPE = getIntent().getIntExtra("POSITION_TYPE",0);
        Log.i("POSITION_TYPE","POSITION_TYPE========"+POSITION_TYPE);
        if(POSITION_TYPE == 1){
            searchText.setHint("输入终点");
        } else{
            searchText.setHint("输入起点");
        }
        searchText.setOnFocusChangeListener(this);
        searchText.addTextChangedListener(this);
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
    public void setAutoSearch(String name){
        InputtipsQuery inputtipsQuery = new InputtipsQuery(name,"成都");
        inputtipsQuery.setCityLimit(true);
        Inputtips inputtips = new Inputtips(SelectPosition.this,inputtipsQuery);
        inputtips.setInputtipsListener(this);
        inputtips.requestInputtipsAsyn();
        //searchText.setInputExtras(inputtips);
    }
    public void back(View v){ finish(); }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        List<Map<String,Object>> dataList2;

        dataList2 = new ArrayList<Map<String,Object>>();
        if(list.isEmpty()){
            poiList.setVisibility(View.INVISIBLE);
        }else{
            poiList.setVisibility(View.VISIBLE);
        }
        for(Tip t : list){
            Log.i("TIP","TIP------"+t);
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("item_src",R.mipmap.search_input);
            map.put("item_text", t.getName());
            dataList2.add(map);
        }
        SimpleAdapter simpleAdapter2 = new SimpleAdapter(this,dataList2,R.layout.list_paths,new String[]{"item_src","item_text"},
                new int[]{R.id.item_src,R.id.item_text});
        auto_poi_name.setAdapter(simpleAdapter2);
        //auto_poi_name.setVisibility(ListView.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(view.getId() == R.id.positions) {
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
                Log.i("test","==============-11"+myLocation.getMyLocation());
                startActivity(intent);
            }
        } else {

        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b){

        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i("test","----------------beforeTextChanged");

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i("test","----------------onTextChanged");
        if(charSequence.equals("")||charSequence.length()==0){
            poiList.setVisibility(View.INVISIBLE);
        }else{
            setAutoSearch(charSequence.toString());
        }
        //poiList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.i("test","----------------afterTextChanged");
    }
}
