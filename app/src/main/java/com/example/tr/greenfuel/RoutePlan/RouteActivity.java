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
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.view.NextTurnTipView;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.customView.SpringProgressView;
import com.example.tr.greenfuel.entity.Travelingdata;
import com.example.tr.greenfuel.junge.BaseActivity;
import com.example.tr.greenfuel.util.EmissionCalculate;
import com.example.tr.greenfuel.util.FuelCalculate;

import java.util.ArrayList;
import java.util.Date;

public class RouteActivity extends BaseActivity implements SensorEventListener{
    private NextTurnTipView mNextTurnTipView;
    private SpringProgressView oilRate;//油耗率
    private SpringProgressView pfRate;//排放率
    private SpringProgressView speedAnalyze;//速度分析
    private TextView nowLeftM;
    private TextView allLeftM;
    private TextView nowRouteName;
    private TextView now;//= (TextView)findViewById(R.id.now_speed);
    private Location loc;
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private double acc = 0;//加速度
    private double angle = 0;//角度
    private double speed = 0;
    private double last_speed = 0;

    private ArrayList<Travelingdata> ts;
    private boolean STARTNAVI = false;
    private AlertDialog dialog;
    private Date startTime = new Date(System.currentTimeMillis());
    private double distanceMin = Integer.MAX_VALUE;
    private double distanceMax = Integer.MIN_VALUE;
    private double fuelConsumption = 0;
    private double carbonEmission = 0;
    private boolean DEBUG = true;
    private double CO = 0;
    private double NO =0;
    private double CH =0;

    /*
    * 每1s变化油耗、排放一次
    * */
    private Handler handler = new Handler();
    private MyRunnable myRunnable=new MyRunnable();
    class MyRunnable implements Runnable{
        @Override
        public void run() {
            if(STARTNAVI){
                setRate(speed,(speed*1)/3600);
                last_speed = speed;
                mAMapNavi.setEmulatorNaviSpeed(60);
                Log.i("acc","state:"+STARTNAVI);
            }
            handler.postDelayed(myRunnable,1000);
        }
    };
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
                speed = location.getSpeed()*3.6;
                now.setText("当前速度"+location.getSpeed()*3.6);
                STARTNAVI = true;
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
        ts = new ArrayList<Travelingdata>();

        initSensor();
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        //自定义UI
        oilRate = (SpringProgressView) findViewById(R.id.oil_pro);
        oilRate.setMaxCount(60f);
        oilRate.setCurrentCount(25);
        pfRate = (SpringProgressView) findViewById(R.id.pf_pro);
        pfRate.setMaxCount(1000.0f);
        pfRate.setCurrentCount(753);
        speedAnalyze = (SpringProgressView) findViewById(R.id.speed_analyze);
        speedAnalyze.setMaxCount(1000.0f);
        speedAnalyze.setCurrentCount(582);
        nowLeftM = (TextView) findViewById(R.id.now_left);
        allLeftM = (TextView) findViewById(R.id.all_left);
        nowRouteName = (TextView) findViewById(R.id.now_route_name);
        now = (TextView)findViewById(R.id.now_speed);
        //设置布局完全不可见
        com.amap.api.navi.AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setLayoutVisible(false);
        mAMapNaviView.setViewOptions(options);

        mNextTurnTipView = (NextTurnTipView) findViewById(R.id.myDirectionView);
        mAMapNaviView.setLazyNextTurnTipView(mNextTurnTipView);

        boolean gps = getIntent().getBooleanExtra("gps", false);
        //gps= false;

        if(gps){
            mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
        }else{
            STARTNAVI = true;
            mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
        }
        mAMapNavi.setEmulatorNaviSpeed(60);
        handler.postDelayed(myRunnable,1000);
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
        if(naviinfo.getPathRetainDistance() < distanceMin){
            distanceMin = naviinfo.getPathRetainDistance();
        }
        if(naviinfo.getPathRetainDistance() > distanceMax){
            distanceMax = naviinfo.getPathRetainDistance();
        }

        now.setText("当前速度："+speed);
        if(DEBUG){
            Log.i("acc","ssssss1:------"+speed);
            speed = (int) (Math.random()*70+10);
            Log.i("acc","ssssss2:------"+speed);
            mAMapNavi.setEmulatorNaviSpeed((int) speed);
            now.setText("当前速度："+speed);
            STARTNAVI = true;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION){
            angle = sensorEvent.values[1];
        }

        if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            acc = sensorEvent.values[1]*Math.cos(2f*3.14159/360f*angle);
            if(Math.abs(acc)<0.1){
                acc = 0;
            }
            if(!STARTNAVI){
                pfRate.setCurrentCount(0);
                oilRate.setCurrentCount(0);
                speedAnalyze.setCurrentCount(0);
                carbonEmission = 0;
                fuelConsumption = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //sensorManager.cancelTriggerSensor();
        if(sensorManager != null){
            sensorManager.unregisterListener(this);
        }
        finish();

        mAMapNavi.destroy();
        mAMapNaviView.onDestroy();
        handler.removeCallbacks(myRunnable,myRunnable);

        super.onDestroy();

    }
    public void endNavi(View v){
        LayoutInflater inflater = LayoutInflater.from (this);
       View view = inflater.inflate(R.layout.analyze_dialog,null);
        Button detail= (Button) view.findViewById(R.id.detail);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RouteActivity.this, "查看详情", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        TextView t = (TextView) view.findViewById(R.id.time);
        TextView dis = (TextView) view.findViewById(R.id.distance);
        TextView oil = (TextView) view.findViewById(R.id.fule_consumption);
        TextView cemmision = (TextView) view.findViewById(R.id.carbon_emmision);

        t.setText("耗时："+((System.currentTimeMillis()-startTime.getTime())/1000/3600)+"小时"+((System.currentTimeMillis()-startTime.getTime())/1000/60%60)+"分钟");
        dis.setText("驾驶行程："+(distanceMax-distanceMin)/1000+"km");
        oil.setText("总共耗油："+(int)fuelConsumption+"ml");
        cemmision.setText("碳排放量："+(int)(carbonEmission/1000)/1000+"kg");
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    public void setRate(double v,double dis){
        ts.clear();
        ts.add(new Travelingdata((int) v,dis,1));
        pfRate.setCurrentCount((int)EmissionCalculate.cEmissionCal(v)/100000);
        oilRate.setCurrentCount(getOilRate(FuelCalculate.CarFuelConsumptionCal(1,ts)));
        speedAnalyze.setCurrentCount((int)(Math.random()*200+400));

        carbonEmission += (EmissionCalculate.cEmissionCal(v)/1000)*dis;
        fuelConsumption += FuelCalculate.CarFuelConsumptionCal(1,ts);
        //Log.i("acc","v:"+v+"  dis:"+dis);
        //Log.i("acc","fuleRate:"+getOilRate(FuelCalculate.CarFuelConsumptionCal(1,ts)));
        //Log.i("acc","fuleNow:"+FuelCalculate.CarFuelConsumptionCal(1,ts));
        //Log.i("acc","fule:"+fuelConsumption);
        acc = (speed - last_speed)/1;
        CO += EmissionCalculate.COEmissionCal(acc,v);
        CH += EmissionCalculate.HCEmissionCal(acc,v);
        NO += EmissionCalculate.NOEmissionCal(acc,v);
    }

    public int getOilRate(double value){
        return (int)(value*100%100)-30;
    }
}
