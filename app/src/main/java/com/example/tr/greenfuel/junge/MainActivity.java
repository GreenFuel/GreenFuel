package com.example.tr.greenfuel.junge;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.example.tr.greenfuel.R;

public class MainActivity extends AppCompatActivity {
    MapView mapView=null;
    private AMap aMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView= (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        aMap = mapView.getMap();
        LatLng desLatLng = new LatLng(30.765207,103.989339);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(desLatLng));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

}
