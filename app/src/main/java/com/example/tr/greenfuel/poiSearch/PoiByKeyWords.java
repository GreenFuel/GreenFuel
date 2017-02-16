package com.example.tr.greenfuel.poiSearch;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.example.tr.greenfuel.util.MyLocation;

import static java.lang.Thread.sleep;

/**
 * Created by TR on 2017/2/15.
 * 通过关键字获取位置信息
 */

public class PoiByKeyWords {
    private String name;
    private LatLng center;
    private int r;
    private MyLocation myLocation;
    private Context context;
    public PoiByKeyWords(String name,Context context) throws InterruptedException {
        this.name = name;
        this.context = context;
        myLocation = new MyLocation(context);
        //Thread.sleep(300);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                center = myLocation.getMyLocation();
                Log.d("test","==========1"+center);
            }
        });
        thread.start();
        //PoiByKeyWords.this.sleep(300);
        center = myLocation.getMyLocation();
        Log.d("test","==========1"+center);
        //myLocation.stop();
    }

    public LatLng getCenter(){
        this.center = myLocation.getMyLocation();
        Log.d("test","==========2"+center);
        return center;
    }

    public PoiByKeyWords(String name,LatLng center,int r) {
        this.name = name;
        this.center = center;
        this.r = r;
    }

    public PoiByKeyWords(String name,int r) {
        this.name = name;
        this.center = myLocation.getMyLocation();
        //myLocation.stop();
        this.r = r;
    }

    public static void main(String[] args) {
        //PoiByKeyWords poiByKeyWords = new PoiByKeyWords("test");
    }
}
