package com.example.tr.greenfuel.nearFunction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/14.
 */

public class nearActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);
    }

    public void back(View v){
        finish();
    }

    public void searchDelicious(View v){//查询美食

    }
    public void searchHotel(View v){//酒店

    }
    public void searchFamousPlace(View v){//景点

    }
    public void searchMovie(View v){//电影

    }
    public void searchBank(View v){//银行

    }
    public void searchGasStation(View v){//加油站

    }
    public void searchPark(View v){//停车场

    }
    public void searchSubway(View v){//地铁

    }
    public void searchWeiZhang(View v){//违章查询

    }
}
