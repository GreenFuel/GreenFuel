package com.example.tr.greenfuel.RoutePlan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.model.AMapNaviLink;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStep;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.NextTurnTipView;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.customView.SpringProgressView;
import com.example.tr.greenfuel.entity.Travelingdata;
import com.example.tr.greenfuel.junge.BaseActivity;
import com.example.tr.greenfuel.light.Light;
import com.example.tr.greenfuel.light.MyLoction;
import com.example.tr.greenfuel.light.MyRoute;
import com.example.tr.greenfuel.mine.MineActivity;
import com.example.tr.greenfuel.util.EmissionCalculate;
import com.example.tr.greenfuel.util.FuelCalculate;
import com.example.tr.greenfuel.util.TTSController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteActivity extends BaseActivity implements SensorEventListener{
    private NextTurnTipView mNextTurnTipView;
    private SpringProgressView oilRate;//油耗率
    private SpringProgressView pfRate;//排放率
    private GaugeChart01View vAnalyze;
    private TextView nowLeftM;
    private TextView allLeftM;
    private TextView nowRouteName;
    private TextView speedAdvice;
    private TextView now;//= (TextView)findViewById(R.id.now_speed);
    private LinearLayout nowSpeedBG;
//颜色表
    private int[] vBG = {Color.rgb(231,59,55),Color.rgb(231,59,55),Color.rgb(237,125,49),Color.rgb(237,125,49),Color.rgb(105,178,115),Color.rgb(105,178,115),
        Color.rgb(237,125,49),Color.rgb(237,125,49),Color.rgb(231,59,55),Color.rgb(231,59,55)};
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
    private boolean DEBUG = false;
    private String roadName = null;
    private double CO = 0;
    private double NO =0;
    private double CH =0;
    private int vAdrice = 46;

    private List<MyRoute> rs = new ArrayList<MyRoute>();//设置的8条道路
    private List<Light> ls = new ArrayList<Light>();//8个红绿灯路口
    private List<Integer> index = new ArrayList<Integer>();
    private List<Integer> roadId = new ArrayList<Integer>();
    private int linkIndex = 0;
    private int linkIndex2 = 0;
    private int position = 0;
    private int rId = 0;
    /*
    * 每1s变化油耗、排放一次
    * */
    private Handler handler = new Handler();
    private Handler handler2 = new Handler();
    private MyRunnable3 myRunnable3=new MyRunnable3();
    private Handler handler3 = new Handler();
    private MyRunnable4 myRunnable4=new MyRunnable4();
    private MyRunnable myRunnable=new MyRunnable();
    class MyRunnable implements Runnable{
        @Override
        public void run() {
            if(STARTNAVI){
                setRate(speed,(speed*1)/3600);
                last_speed = speed;
                //mAMapNavi.setEmulatorNaviSpeed(60);
                //Log.i("acc","state:"+STARTNAVI);
            }
            handler.postDelayed(myRunnable,1000);
        }
    };
    class MyRunnable2 implements Runnable{
        @Override
        public void run() {
            findV(position);
        }
    };

    class MyRunnable3 implements Runnable{
        @Override
        public void run() {
            speed = (int) (Math.random()*60+10);
            //Log.i("acc","ssssss2:------"+speed);
            now.setText(""+(int)speed);
            setSpeedColor(speed);
            handler2.postDelayed(myRunnable3,2000);


        }
    };

    class MyRunnable4 implements Runnable{
        @Override
        public void run() {
            TTSController tsc = new TTSController(RouteActivity.this);
            tsc.init();
            tsc.startSpeaking("建议速度"+(int)vAdrice);
            handler3.postDelayed(myRunnable4,60000);
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

        vAnalyze = (GaugeChart01View) findViewById(R.id.speed_analyze);

        nowLeftM = (TextView) findViewById(R.id.now_left);
        allLeftM = (TextView) findViewById(R.id.all_left);
        nowRouteName = (TextView) findViewById(R.id.now_route_name);
        speedAdvice = (TextView) findViewById(R.id.speed_advice);


        nowSpeedBG = (LinearLayout) findViewById(R.id.speed_bg);

        vAdrice = (int)(Math.random()*30f+30);
        speedAdvice.setText(""+(int)vAdrice);

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
        mAMapNavi.setEmulatorNaviSpeed((int)speed);
        handler.postDelayed(myRunnable,1000);
        initSpeed();
        if(DEBUG) {
            handler2.postDelayed(myRunnable3,2000);
        }
        handler3.postDelayed(myRunnable4,20000);
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        super.onCalculateMultipleRoutesSuccess(ints);
        //initSpeed();
    }

    private void initSpeed() {
        Log.i("acc","path");
        rs.clear();
        ls.clear();
        AMapNaviPath p = mAMapNavi.getNaviPath();
        int k = 0;
        int j = 0;
        int s = 0;
        for(AMapNaviStep step: p.getSteps()){
            s = 0;
            Log.i("acc","s:"+step.getStartIndex()+" e:"+step.getEndIndex());
            for(AMapNaviLink link :step.getLinks()){
                s += link.getLength();
                if(link.getTrafficLights()&&link.getRoadName()!=null){
//                    Log.i("acc","getTrafficLights:"+link.getTrafficLights());
                      index.add(Integer.valueOf(j));
                      roadId.add(Integer.valueOf(k));
                      Log.i("acc","getRoadName:"+link.getRoadName());
                      Log.i("acc","K++++:"+k);
//                    Log.i("acc","getCoords:"+link.getCoords().size());
//                    Log.i("acc","getCoords:"+link.getCoords().get(link.getCoords().size()-1));
                    NaviLatLng l = link.getCoords().get(link.getCoords().size()-1);
                    String name = link.getRoadName();
                    ls.add(new Light(new MyLoction(l.getLatitude(), l.getLongitude()), 170, 70,
                            new Date(System.currentTimeMillis())));
                    rs.add(new MyRoute(60,30,s,name));
                    s = 0;
                }
                Log.i("acc","j:"+(j++));
            }
            k++;
        }

        ls.add(new Light(new MyLoction(p.getEndPoint().getLatitude(),p.getEndPoint().getLongitude()), 100, 100,
               new Date(System.currentTimeMillis())));
        rs.add(new MyRoute(60,30,s,"end"));
        Log.i("acc","rssize:"+rs.size());
        Log.i("acc","rssize:"+ls.size());
        for(Integer i : roadId){
            Log.i("acc","id:"+i);
        }
        index.add(j);
        for(Integer i : index){
            Log.i("acc","index__--id:"+i);
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
        //mAMapNavi.startNavi(NaviType.EMULATOR);
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        super.onNaviInfoUpdate(naviinfo);
        Log.i("acc","linkNUM"+naviinfo.getCurLink());
        boolean roadChange =true;
        nowLeftM.setText(String.valueOf(naviinfo.getCurStepRetainDistance())+"米");
        if(!naviinfo.getCurrentRoadName().toString().equals(roadName)){
            roadName = naviinfo.getCurrentRoadName().toString();
            //Log.i("acc","loadName"+roadName);
            roadChange = false;
            if(findName(roadName)){
                //rId ++;
            }

            //findV(roadName);
        }

        if(linkIndex == 0){
             //findV(position);
             //Handler handler = new Handler();
             //MyRunnable2 myRunnable=new MyRunnable2();
             //handler.post(myRunnable);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    findV(0);
                }
            });
            thread.start();
            position ++;
            linkIndex = 1;
        }
        if(linkIndex2 != naviinfo.getCurLink() ){
            linkIndex2 = naviinfo.getCurLink();
            rId ++;
        }
        //rs.get(position).setvUP(naviinfo.getLimitSpeed());
        if(rId > index.get(position) ){
            //findV(position);
//            Handler handler = new Handler();
//            MyRunnable2 myRunnable=new MyRunnable2();
//            handler.post(myRunnable);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    findV(position);
                }
            });
            thread.start();
            position ++;
        }
        nowRouteName.setText(naviinfo.getCurrentRoadName().toString());
        allLeftM.setText(naviinfo.getPathRetainDistance()/1000f+"公里  "+
                naviinfo.getPathRetainTime()/3600+"小时"+naviinfo.getPathRetainTime()/60%60+"分钟");
        if(naviinfo.getPathRetainDistance() < distanceMin){
            distanceMin = naviinfo.getPathRetainDistance();
        }
        if(naviinfo.getPathRetainDistance() > distanceMax){
            distanceMax = naviinfo.getPathRetainDistance();
        }
        now.setText(""+(int)speed);
        //setSpeedColor(speed);
        if(DEBUG){
            //Log.i("acc","ssssss1:------"+speed);
            //speed = (int) (Math.random()*60+10);
            //Log.i("acc","ssssss2:------"+speed);
            mAMapNavi.setEmulatorNaviSpeed((int) speed);
           // now.setText(""+(int)speed);
          //  setSpeedColor(speed);
            vAnalyze.setAngle((float) ((90f/vAdrice)*speed));
            vAnalyze.chartRender();
            vAnalyze.invalidate();
            STARTNAVI = true;
        }
    }

    private void setSpeedColor(double speed) {
        GradientDrawable bg = (GradientDrawable)nowSpeedBG.getBackground();
        int i = (int)((5f/vAdrice)*speed);
        if(i >= 10){
            i = 9;
        }
        bg.setColor(vBG[i]);
        nowSpeedBG.setBackground(bg);
    }

    private boolean findName(String roadName) {
        for(MyRoute r : rs){
            if(r.getName().equals(roadName)){
                return true;
            }
        }
        return false;
    }

    private void findV(int position) {
        List<MyRoute> rr = new ArrayList<MyRoute>();
        Light ll = new Light();
        ll.setMinEst(rs.size()-position);
        if(position == 0){
            ll.F(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                    0, 0, ls.subList(position+1,ls.size()), rs.subList(position+1,ls.size()), rr);
        } else {
            ll.F(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                    0, 0, ls.subList(position, ls.size()), rs.subList(position, ls.size()), rr);
        }
        for(MyRoute r : rr){
            Log.i("acc","NOw---v:"+r.getvDRIVE()+"name"+r.getName());
        }
        vAdrice = rr.get(0).getvDRIVE();
        speedAdvice.setText(""+(int)vAdrice);

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
               // speedAnalyze.setCurrentCount(0);
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
                SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                editor.putFloat("fule",(float) (fuelConsumption/1000f+sp.getFloat("fule",0.0f)));
                editor.putFloat("distance",(float) ((distanceMax-distanceMin)/1000f)+sp.getFloat("distance",0.0f));
                editor.putFloat("carbon",(float) (carbonEmission/1000f+sp.getFloat("carbon",0.0f)));

//                editor.putFloat("fule",(float) (0));
//                editor.putFloat("distance",(float) 0);
//                editor.putFloat("carbon",(float) (0));
                editor.commit();
                startActivity(new Intent(RouteActivity.this, MineActivity.class));
                finish();
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
        cemmision.setText("碳排放量："+(int)(carbonEmission)+"g");
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
//        Button btn = (Button) findViewById(R.id.button2);
//        GradientDrawable mGrad = (GradientDrawable) btn.getBackground();
//        mGrad.setColor(Color.BLUE);
//        btn.setBackground(mGrad);
    }

    public void setRate(double v,double dis){
        ts.clear();
        ts.add(new Travelingdata((int) v,dis,1));
        pfRate.setCurrentCount((int)EmissionCalculate.cEmissionCal(v)/100000);
        oilRate.setCurrentCount(getOilRate(FuelCalculate.CarFuelConsumptionCal(1,ts)));
        //speedAnalyze.setCurrentCount((int)(Math.random()*200+400));

        carbonEmission += ((EmissionCalculate.cEmissionCal(v)/100)*dis)/1000;
        fuelConsumption += FuelCalculate.CarFuelConsumptionCal(1,ts);
        //Log.i("acc","v:"+v+"  dis:"+dis);
        //Log.i("acc","fuleRate:"+getOilRate(FuelCalculate.CarFuelConsumptionCal(1,ts)));
        //Log.i("acc","fuleNow:"+FuelCalculate.CarFuelConsumptionCal(1,ts));
        //Log.i("acc","v:"+v+"carbonEmission:"+carbonEmission);
        acc = (speed - last_speed)/1;
        CO += EmissionCalculate.COEmissionCal(acc,v);
        CH += EmissionCalculate.HCEmissionCal(acc,v);
        NO += EmissionCalculate.NOEmissionCal(acc,v);
    }

    public int getOilRate(double value){
        return (int)(value*100%100)-30;
    }
}
