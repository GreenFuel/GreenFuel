package com.example.tr.greenfuel.RoutePlan;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.view.NextTurnTipView;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.customView.SpringProgressView;
import com.example.tr.greenfuel.junge.BaseActivity;

public class RouteActivity extends BaseActivity implements SensorEventListener{
    private NextTurnTipView mNextTurnTipView;
    private SpringProgressView oilRate;//油耗率
    private SpringProgressView pfRate;//排放率
    private SpringProgressView speedAnalyze;//排放率
    private TextView nowLeftM;
    private TextView allLeftM;
    private TextView nowRouteName;
    private Location loc;
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private double acc = 0;//加速度
    private double angle = 0;//角度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_next_turn_tip_view);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("speed", "onLocationChanged: "+location.getSpeed());
                loc = location;
                TextView now = (TextView)findViewById(R.id.now_speed);
                now.setText("当前速度"+location.getSpeed()*3.6);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
        initSensor();
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        //自定义UI
        oilRate = (SpringProgressView) findViewById(R.id.oil_pro);
        oilRate.setMaxCount(1000.0f);
        oilRate.setCurrentCount(653);
        pfRate = (SpringProgressView) findViewById(R.id.pf_pro);
        pfRate.setMaxCount(1000.0f);
        pfRate.setCurrentCount(753);
        speedAnalyze = (SpringProgressView) findViewById(R.id.speed_analyze);
        speedAnalyze.setMaxCount(1000.0f);
        speedAnalyze.setCurrentCount(582);
        nowLeftM = (TextView) findViewById(R.id.now_left);
        allLeftM = (TextView) findViewById(R.id.all_left);
        nowRouteName = (TextView) findViewById(R.id.now_route_name);
        //设置布局完全不可见
        com.amap.api.navi.AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setLayoutVisible(false);
        mAMapNaviView.setViewOptions(options);

        mNextTurnTipView = (NextTurnTipView) findViewById(R.id.myDirectionView);
        mAMapNaviView.setLazyNextTurnTipView(mNextTurnTipView);

        boolean gps = getIntent().getBooleanExtra("gps", false);
        //gps= false;
        mAMapNavi.setEmulatorNaviSpeed(60);
        if(gps){
            mAMapNavi.startNavi(AMapNavi.GPSNaviMode);
        }else{
            mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
        }
    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                sensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
    }

    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();
        mAMapNavi.startNavi(NaviType.EMULATOR);
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        super.onNaviInfoUpdate(naviinfo);
        nowLeftM.setText(String.valueOf(naviinfo.getCurStepRetainDistance())+"米");
        nowRouteName.setText(naviinfo.getCurrentRoadName().toString());
        allLeftM.setText(naviinfo.getPathRetainDistance()/1000f+"公里  "+
                naviinfo.getPathRetainTime()/3600+"小时"+naviinfo.getPathRetainTime()/60%60+"分钟");

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION){
            angle = sensorEvent.values[1];
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            Log.i("acc","angle"+angle+"加速度:"+acc+"\tcos"+Math.cos(2f*3.14159/360f*angle));
            acc = sensorEvent.values[1]*Math.cos(2f*3.14159/360f*angle);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
