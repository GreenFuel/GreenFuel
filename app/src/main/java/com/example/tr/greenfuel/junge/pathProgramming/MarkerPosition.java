package com.example.tr.greenfuel.junge.pathProgramming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.tr.greenfuel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerPosition extends AppCompatActivity implements AMap.OnMarkerDragListener, PoiSearch.OnPoiSearchListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private AMap aMap;
    private MapView mapView;
    private LatLng position;
    private Marker marker;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private ListView poiList;
    private List<Map<String,Object>> dataList;
    private SimpleAdapter simpleAdapter;
    private  int TYPE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_position);
        init();
        mapView.onCreate(savedInstanceState);   //必须重写
        moveToLocation(getIntent().getDoubleExtra("Lat",0f), getIntent().getDoubleExtra("Lng",0f));
    }

    private void moveToLocation(double lat, double lng) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 15));
    }

    private void init() {
        mapView = (MapView) findViewById(R.id.mapView);
        aMap = mapView.getMap();
        position = new LatLng(getIntent().getDoubleExtra("Lat",0f), getIntent().getDoubleExtra("Lng",0f));
        aMap.setOnMarkerDragListener(this);
        marker = aMap.addMarker(new MarkerOptions().position(position).title("北京").snippet("DefaultMarker"));
        marker.setDraggable(true);
        //Poi
        setPoi("",position);
        poiList = (ListView) findViewById(R.id.pois);
        poiList.setOnItemClickListener(this);
        poiList.setOnItemSelectedListener(this);
        dataList = new ArrayList<Map<String,Object>>();
        TYPE = getIntent().getIntExtra("type",0);

    }

    private void setPoi(String keyWord,LatLng lc) {
        query = new PoiSearch.Query("","",null);
        query.setPageSize(10);
        query.setPageNum(1);
        poiSearch = new PoiSearch(MarkerPosition.this,query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lc.latitude,lc.longitude),1000));
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        setPoi("",new LatLng(marker.getPosition().latitude,marker.getPosition().longitude));
        moveToLocation(marker.getPosition().latitude,marker.getPosition().longitude);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (!poiResult.getPois().isEmpty()){
            Log.i("test","-------------===="+poiResult.getPois().get(0).toString());
            dataList.clear();
            int icon = R.drawable.point_my;
            for(PoiItem pi : poiResult.getPois()){
                Map<String,Object>map=new HashMap<String,Object>();
                map.put("item_src",R.drawable.point_my);
                map.put("item_text", pi.toString());
                dataList.add(map);
            }
            if(TYPE ==0){
                simpleAdapter = new SimpleAdapter(this,dataList,R.layout.list_poi_orgin,new String[]{"item_src","item_text"},
                        new int[]{R.id.item_src,R.id.item_text});
            }else {
                simpleAdapter = new SimpleAdapter(this,dataList,R.layout.list_poi,new String[]{"item_src","item_text"},
                        new int[]{R.id.item_src,R.id.item_text});
            }
            poiList.setAdapter(simpleAdapter);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("test","----------------------111111");
        TextView t = (TextView) view.findViewById(R.id.item_text);
        Log.i("test","----------------------"+t.getText());
        if(TYPE == 0){
            startActivity(new Intent(MarkerPosition.this,SetPath.class).putExtra("orgin",t.getText()));
        }else{
            startActivity(new Intent(MarkerPosition.this,SetPath.class).putExtra("orgin",t.getText()));
        }
    }
    public void back(View v) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView t = (TextView) view.findViewById(R.id.item_text);
        Log.i("test","----------------------"+t.getText());
        if(TYPE == 0){
            startActivity(new Intent(MarkerPosition.this,SetPath.class).putExtra("orgin",t.getText()));
        }else{
            startActivity(new Intent(MarkerPosition.this,SetPath.class).putExtra("orgin",t.getText()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
