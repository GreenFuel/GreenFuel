package com.example.tr.greenfuel.poiSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/13.
 */

public class PoiSearchPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search_page);
    }

    public void back(View v){
        finish();
    }

    public void search(View v){
        startActivity(new Intent(PoiSearchPageActivity.this,NearPoiSearchResultActivity.class).putExtra("keyWord","宾馆"));
        //Toast.makeText(this, "对话框", Toast.LENGTH_SHORT).show();
    }
}
