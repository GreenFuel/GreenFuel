package com.example.tr.greenfuel.util;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TR on 2017/2/15.
 */

public class MyLocation implements AMapLocationListener {
    private LatLng myLocation;
    public Context context;
    public  AMapLocationClientOption mapLocationClientOption = null;
    public AMapLocationClient mapLocationClient;
    public MyLocation(Context context) {
        this.context = context;
        mapLocationClient = new AMapLocationClient(context);
        mapLocationClientOption = new AMapLocationClientOption();
        //设置定位监听
        mapLocationClient.setLocationListener(this);
        //设置定位模式为高精度模式
        mapLocationClientOption.setLocationMode(AMapLocationClientOption
                .AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒
        mapLocationClientOption.setInterval(20);
        mapLocationClient.setLocationOption(mapLocationClientOption);
        //启动定位
        mapLocationClient.startLocation();
    }

    public MyLocation(LatLng myLocation) {
        this.myLocation = myLocation;
    }

    public void setMyLocation(LatLng l){
        this.myLocation = l;
    }

    public  LatLng getMyLocation(){
        //mapLocationClientOption
        return this.myLocation;
    }

    public void stop(){
        mapLocationClient.stopLocation();
    }
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if(amapLocation !=null){
            if(amapLocation.getErrorCode() == 0){
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                this.myLocation = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
}
