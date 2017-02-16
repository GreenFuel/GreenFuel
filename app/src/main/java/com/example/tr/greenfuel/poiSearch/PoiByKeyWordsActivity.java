package com.example.tr.greenfuel.poiSearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.example.tr.greenfuel.R;

public class PoiByKeyWordsActivity extends AppCompatActivity {
    PoiByKeyWords poiByKeyWs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_by_key_words);
        init();
    }

    private void init() {
        try {
            poiByKeyWs = new PoiByKeyWords("test",PoiByKeyWordsActivity.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void onClick(View view){
        LatLng c = poiByKeyWs.getCenter();
    }
}
