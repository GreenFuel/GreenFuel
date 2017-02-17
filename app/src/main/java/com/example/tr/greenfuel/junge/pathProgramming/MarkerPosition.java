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
import com.example.tr.greenfuel.RoutePlan.RestRouteShowActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerPosition extends AppCompatActivity implements AMap.OnMarkerDragListener, PoiSearch.OnPoiSearchListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private AMap aMap;
    private MapView mapView;
    private LatLng position;
    private LatLng orginPosition;
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
        Log.i("stragy","mp:"+getIntent().getBooleanExtra("congestion",false)+getIntent().getBooleanExtra("cost",false)+
                getIntent().getBooleanExtra("hightspeed",false)+getIntent().getBooleanExtra("avoidhightspeed",false));
        position = new LatLng(getIntent().getDoubleExtra("Lat",0f), getIntent().getDoubleExtra("Lng",0f));
        orginPosition = new LatLng(getIntent().getDoubleExtra("startLat2",0f), getIntent().getDoubleExtra("startLng2",0f));
        //Log.i("sp","---in-------"+getIntent().getDoubleExtra("startLat2",0f)+"  ori   "+orginPosition.latitude+" ddd  "+getIntent().getDoubleExtra("Lat",0f));
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
        Log.i("POSITION_TYPE","MK-----:"+TYPE);
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
        position = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
        setPoi("",new LatLng(marker.getPosition().latitude,marker.getPosition().longitude));
        moveToLocation(marker.getPosition().latitude,marker.getPosition().longitude);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (!poiResult.getPois().isEmpty()){
            Log.i("test","-------------===="+poiResult.getPois().get(0).toString());
            Log.i("POSITION_TYPE","pos-----:"+TYPE);
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
            poiList.setOnItemClickListener(this);
            poiList.setItemsCanFocus(true);
            //poiList.setVisibility(ListView.INVISIBLE);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("stragy","----------------------111111");
        Log.i("POSITION_TYPE","mKTO-----:"+TYPE);
        TextView t = (TextView) view.findViewById(R.id.item_text);
        Log.i("test","----------------------"+t.getText());
        if(TYPE == 0){
            //startActivity(new Intent(MarkerPosition.this,SetPath.class).putExtra("orgin",t.getText()));
            Intent ii = new Intent(MarkerPosition.this,SetPath.class);
            ii.putExtra("orgin",t.getText());
            Log.i("sp","mkrt----:startLat"+position.latitude);
            ii.putExtra("Lng",position.longitude);
            ii.putExtra("Lat",position.latitude);
            startActivity(ii);
        }else{
            //startActivity(new Intent(MarkerPosition.this,SetPath.class).putExtra("orgin",t.getText()));
            Intent ii = new Intent(MarkerPosition.this,RestRouteShowActivity.class);
            //i.putExtra("orgin",t.getText());
            ii.putExtra("endLng",position.longitude);
            ii.putExtra("endLat",position.latitude);

            Log.i("sp","mk----:startLat2"+orginPosition.latitude);
            ii.putExtra("startLng",orginPosition.longitude);
            ii.putExtra("startLat",orginPosition.latitude);
            ii.putExtra("congestion",getIntent().getBooleanExtra("congestion",false));
            ii.putExtra("cost",getIntent().getBooleanExtra("cost",false));
            ii.putExtra("hightspeed",getIntent().getBooleanExtra("hightspeed",false));
            ii.putExtra("avoidhightspeed",getIntent().getBooleanExtra("avoidhightspeed",false));
            //position = new LatLng(getIntent().getDoubleExtra("Lat",0f), getIntent().getDoubleExtra("Lng",0f));
            Log.i("stragy","mpT1:"+getIntent().getBooleanExtra("congestion",false)+getIntent().getBooleanExtra("cost",false)+
                    getIntent().getBooleanExtra("hightspeed",false)+getIntent().getBooleanExtra("avoidhightspeed",false));
            startActivity(ii);
            finish();
        }
    }
    public void back(View v) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("stragy","----------------------111111");
        Log.i("POSITION_TYPE","mKTO-----:"+TYPE);
        TextView t = (TextView) view.findViewById(R.id.item_text);
        Log.i("test","----------------------"+t.getText());
        if(TYPE == 0){
            //startActivity(new Intent(MarkerPosition.this,SetPath.class).putExtra("orgin",t.getText()));
            Intent ii = new Intent(MarkerPosition.this,SetPath.class);
            ii.putExtra("orgin",t.getText());
            Log.i("sp","mkrt----:startLat"+position.latitude);
            ii.putExtra("Lng",position.longitude);
            ii.putExtra("Lat",position.latitude);
            startActivity(ii);
        }else{
            //startActivity(new Intent(MarkerPosition.this,SetPath.class).putExtra("orgin",t.getText()));
            Intent ii = new Intent(MarkerPosition.this,RestRouteShowActivity.class);
            //i.putExtra("orgin",t.getText());
            ii.putExtra("endLng",position.longitude);
            ii.putExtra("endLat",position.latitude);

            Log.i("sp","mk----:startLat1"+orginPosition.latitude);
            ii.putExtra("startLng",orginPosition.longitude);
            ii.putExtra("startLat",orginPosition.latitude);
            //position = new LatLng(getIntent().getDoubleExtra("Lat",0f), getIntent().getDoubleExtra("Lng",0f));
            ii.putExtra("congestion",getIntent().getBooleanExtra("congestion",false));
            ii.putExtra("cost",getIntent().getBooleanExtra("cost",false));
            ii.putExtra("hightspeed",getIntent().getBooleanExtra("hightspeed",false));
            ii.putExtra("avoidhightspeed",getIntent().getBooleanExtra("avoidhightspeed",false));
            Log.i("stragy","mpT2:"+getIntent().getBooleanExtra("congestion",false)+getIntent().getBooleanExtra("cost",false)+
                    getIntent().getBooleanExtra("hightspeed",false)+getIntent().getBooleanExtra("avoidhightspeed",false));
            startActivity(ii);
            finish();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void selected(View v){
        Log.i("stragy","----------------------111111");
        Log.i("POSITION_TYPE","mKTO-----:"+TYPE);
        TextView t = (TextView) v.findViewById(R.id.item_text);
        Log.i("test","----------------------"+t.getText());
        if(TYPE == 0){
            //startActivity(new Intent(MarkerPosition.this,SetPath.class).putExtra("orgin",t.getText()));
            Intent ii = new Intent(MarkerPosition.this,SetPath.class);
            ii.putExtra("orgin",t.getText());
            Log.i("sp","mkrt----:startLat"+position.latitude);
            ii.putExtra("Lng",position.longitude);
            ii.putExtra("Lat",position.latitude);
            startActivity(ii);
        }else{
            //startActivity(new Intent(MarkerPosition.this,SetPath.class).putExtra("orgin",t.getText()));
            Intent ii = new Intent(MarkerPosition.this,RestRouteShowActivity.class);
            //i.putExtra("orgin",t.getText());
            ii.putExtra("endLng",position.longitude);
            ii.putExtra("endLat",position.latitude);

            Log.i("sp","mk----:startLat3"+orginPosition.latitude);
            ii.putExtra("startLng",orginPosition.longitude);
            ii.putExtra("startLat",orginPosition.latitude);
            ii.putExtra("congestion",getIntent().getBooleanExtra("congestion",false));
            ii.putExtra("cost",getIntent().getBooleanExtra("cost",false));
            ii.putExtra("hightspeed",getIntent().getBooleanExtra("hightspeed",false));
            ii.putExtra("avoidhightspeed",getIntent().getBooleanExtra("avoidhightspeed",false));
            //position = new LatLng(getIntent().getDoubleExtra("Lat",0f), getIntent().getDoubleExtra("Lng",0f));
            Log.i("stragy","mpT1:"+getIntent().getBooleanExtra("congestion",false)+getIntent().getBooleanExtra("cost",false)+
                    getIntent().getBooleanExtra("hightspeed",false)+getIntent().getBooleanExtra("avoidhightspeed",false));
            startActivity(ii);
            finish();
        }
    }
}
