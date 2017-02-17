package com.example.tr.greenfuel.RoutePlan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStaticInfo;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.example.tr.greenfuel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestRouteShowActivity extends Activity implements AMapNaviListener, OnClickListener, OnCheckedChangeListener {
    private boolean congestion, cost, hightspeed, avoidhightspeed;
    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;
    private AMap mAmap;
    /**
     * 地图对象
     */
    private MapView mRouteMapView;

    private NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    private NaviLatLng startLatlng = new NaviLatLng(39.925041, 116.437901);
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();

    /**
     * 当前用户选中的路线，在下个页面进行导航
     */
    //private int routeIndex;
    /**路线的权值，重合路线情况下，权值高的路线会覆盖权值低的路线**/
    private int zindex = 1;
    /**
     * 路线计算成功标志位
     */
    private boolean calculateSuccess = false;
    private boolean  chooseRouteSuccess =false;
    private LinearLayout selectroute;
    private LinearLayout selectroute1;
    private LinearLayout selectroute2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rest_calculate);
        intiView();
        setMap(savedInstanceState);
    }

    private void setMap(Bundle savedInstanceState) {

        if(getIntent().getDoubleExtra("startLat",0f)!=0){
            Log.i("suc","成功-------------");
            startLatlng = new NaviLatLng(getIntent().getDoubleExtra("startLat",0f),getIntent().getDoubleExtra("startLng",0f));
        }else{
            Log.i("suc","失败-------------");
            startLatlng = new NaviLatLng(30.6562406723,104.0660393993);
        }
        if(getIntent().getDoubleExtra("endLat",0f)!=0){
            Log.i("suc","成功-------------");
            endLatlng = new NaviLatLng(getIntent().getDoubleExtra("endLat",0f),getIntent().getDoubleExtra("endLng",0f));
        }else{
            Log.i("suc","失败-------------");
            endLatlng = new NaviLatLng(30.5891985869,104.0364842722);
        }

        startList.clear();
        startList.add(startLatlng);
        endList.clear();
        endList.add(endLatlng);
        mRouteMapView = (MapView) findViewById(R.id.navi_view);
        mRouteMapView.onCreate(savedInstanceState);
        mAmap = mRouteMapView.getMap();
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);

        mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLatlng.getLatitude(),startLatlng.getLongitude()), 12));
        startCalulate();
    }

    private void intiView() {
        Log.i("stragy","Route1:"+getIntent().getBooleanExtra("congestion",false)+getIntent().getBooleanExtra("cost",false)+
                getIntent().getBooleanExtra("hightspeed",false)+getIntent().getBooleanExtra("avoidhightspeed",false));
        this.congestion = getIntent().getBooleanExtra("congestion",false);
        this.hightspeed = getIntent().getBooleanExtra("hightspeed",false);
        this.cost = getIntent().getBooleanExtra("cost",false);
        this.avoidhightspeed = getIntent().getBooleanExtra("avoidhightspeed",false);
        Log.i("stragy","mpTture:"+this.congestion+this.cost+this.hightspeed+this.avoidhightspeed);
        CheckBox congestion2 = (CheckBox) findViewById(R.id.congestion);
        CheckBox cost2 = (CheckBox) findViewById(R.id.cost);
        CheckBox hightspeed2 = (CheckBox) findViewById(R.id.hightspeed);
        CheckBox avoidhightspeed2 = (CheckBox) findViewById(R.id.avoidhightspeed);
        congestion2.setChecked(this.congestion);
        cost2.setChecked(this.cost);
        hightspeed2.setChecked(this.hightspeed);
        avoidhightspeed2.setChecked(this.avoidhightspeed);
        Log.i("stragy","mpTture2:"+this.congestion+this.cost+this.hightspeed+this.avoidhightspeed);
        selectroute = (LinearLayout) findViewById(R.id.selectroute);
        selectroute2 = (LinearLayout) findViewById(R.id.selectroute2);
        selectroute1 = (LinearLayout) findViewById(R.id.selectroute1);
        Button gpsnavi = (Button) findViewById(R.id.gpsnavi);

        selectroute.setOnClickListener(this);
        selectroute1.setOnClickListener(this);
        selectroute2.setOnClickListener(this);
        gpsnavi.setOnClickListener(this);
        congestion2.setOnCheckedChangeListener(this);
        cost2.setOnCheckedChangeListener(this);
        hightspeed2.setOnCheckedChangeListener(this);
        avoidhightspeed2.setOnCheckedChangeListener(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mRouteMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mRouteMapView.onPause();
        startList.clear();
        wayList.clear();
        endList.clear();
        routeOverlays.clear();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRouteMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteMapView.onDestroy();
        /**
         * 当前页面只是展示地图，activity销毁后不需要再回调导航的状态
         */
        mAMapNavi.removeAMapNaviListener(this);
        //注意：不要调用这个destory方法，因为在当前页面进行算路，算路成功的数据全部存在此对象中。到另外一个activity中只需要开始导航即可。
        //如果用户是回退退出当前activity，可以调用下面的destory方法。
        mAMapNavi.destroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.congestion:
                congestion = isChecked;
                break;
            case R.id.avoidhightspeed:
                avoidhightspeed = isChecked;
                break;
            case R.id.cost:
                cost = isChecked;
                break;
            case R.id.hightspeed:
                hightspeed = isChecked;
                break;
            default:
                break;
        }
    }

    @Override
    public void onInitNaviSuccess() {
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        //清空上次计算的路径列表。
        routeOverlays.clear();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
    }

    @Override
    public void onCalculateRouteSuccess() {
        /**
         * 清空上次计算的路径列表。
         */
        routeOverlays.clear();
        AMapNaviPath path = mAMapNavi.getNaviPath();
        /**
         * 单路径不需要进行路径选择，直接传入－1即可
         */

        drawRoutes(-1, path);
    }

    @Override
    public void onCalculateRouteFailure(int arg0) {
        calculateSuccess = false;
        Toast.makeText(getApplicationContext(), "计算路线失败，errorcode＝" + arg0, Toast.LENGTH_SHORT).show();
    }

    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;
        mAmap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(mAmap, path, this);
        routeOverLay.setTrafficLine(true);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
        Log.i("routes","==================++++"+routeId);
        if(routeId<=3){
            setRoutesDetails(path,routeId);
        }
    }

    private void setRoutesDetails(AMapNaviPath path,int index) {
        int[] ts = {R.id.time1,R.id.time2,R.id.time3};
        int[] ds = {R.id.dis1,R.id.dis2,R.id.dis3};
        int[] os = {R.id.oil1,R.id.oil2,R.id.oil3};
        TextView pathTiem = (TextView)findViewById(ts[index-1]);
        TextView pathDis = (TextView)findViewById(ds[index-1]);
        TextView pathOil = (TextView)findViewById(os[index-1]);
        pathTiem.setText(String.valueOf(path.getAllTime()/60)+"分钟");
        pathDis.setText(String.valueOf(path.getAllLength()/1000f)+"km");
        pathOil.setText(""+getOil(path)+"g");

    }

    private int getOil(AMapNaviPath path) {
        return 1000;
    }

    public void changeRoute(int routeIndex) {
        if (!calculateSuccess) {
            Toast.makeText(this, "请先算路", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 计算出来的路径只有一条
         */
        if (routeOverlays.size() == 1) {
            chooseRouteSuccess = true;
            //Toast.makeText(this, "导航距离:" + (mAMapNavi.getNaviPath()).getAllLength() + "m" + "\n" + "导航时间:" + (mAMapNavi.getNaviPath()).getAllTime() + "s", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast.makeText(this,"---"+routeIndex,Toast.LENGTH_SHORT).show();
        if (routeIndex < routeOverlays.size()){
        int routeID = routeOverlays.keyAt(routeIndex);
            Log.i("ID","---------routeIndex=="+routeIndex);
            Log.i("ID","---------id=="+routeID);
            //突出选择的那条路
            for (int i = 0; i < routeOverlays.size(); i++) {
                int key = routeOverlays.keyAt(i);
                routeOverlays.get(key).setTransparency(0f);
            }
            routeOverlays.get(routeID).setTransparency(0.7f);
            /**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
            routeOverlays.get(routeID).setZindex(zindex++);

            //必须告诉AMapNavi 你最后选择的哪条路
            mAMapNavi.selectRouteId(routeID);
            chooseRouteSuccess = true;
        }else {
            Toast.makeText(this, "暂无此方案", Toast.LENGTH_SHORT).show();
        }
    }
    public void startCalulate(){
        if (avoidhightspeed && hightspeed) {
            Toast.makeText(getApplicationContext(), "不走高速与高速优先不能同时为true.", Toast.LENGTH_LONG).show();
        }
        if (cost && hightspeed) {
            Toast.makeText(getApplicationContext(), "高速优先与避免收费不能同时为true.", Toast.LENGTH_LONG).show();
        }
            /*
			 * strategyFlag转换出来的值都对应PathPlanningStrategy常量，用户也可以直接传入PathPlanningStrategy常量进行算路。
			 * 如:mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList,PathPlanningStrategy.DRIVING_DEFAULT);
			 */
        int strategyFlag = 0;
        try {
            strategyFlag = mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (strategyFlag >= 0) {
            mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
            //Toast.makeText(getApplicationContext(), "策略:" + strategyFlag, Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectroute:
                selectroute.setBackgroundResource(R.color.colorIconBlue);
                selectroute1.setBackgroundResource(R.color.bgofdefault);
                selectroute2.setBackgroundResource(R.color.bgofdefault);
                changeRoute(0);
                break;
            case R.id.selectroute1:
                selectroute.setBackgroundResource(R.color.bgofdefault);
                selectroute1.setBackgroundResource(R.color.colorIconBlue);
                selectroute2.setBackgroundResource(R.color.bgofdefault);
                changeRoute(1);
                break;
            case R.id.selectroute2:
                selectroute.setBackgroundResource(R.color.bgofdefault);
                selectroute1.setBackgroundResource(R.color.bgofdefault);
                selectroute2.setBackgroundResource(R.color.colorIconBlue);
                changeRoute(2);
                break;
            case R.id.gpsnavi:
                Intent gpsintent = new Intent(getApplicationContext(), RouteActivity.class);
                gpsintent.putExtra("gps", true);
                startActivity(gpsintent);
                this.finish();
                break;
            default:
                break;
        }
    }

    /**
     * ************************************************** 在算路页面，以下接口全不需要处理，在以后的版本中我们会进行优化***********************************************************************************************
     **/

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo arg0) {


    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {


    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] arg0) {


    }

    @Override
    public void hideCross() {


    }

    @Override
    public void hideLaneInfo() {


    }

    @Override
    public void notifyParallelRoad(int arg0) {


    }

    @Override
    public void onArriveDestination() {


    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {

    }

    @Override
    public void onArriveDestination(AMapNaviStaticInfo aMapNaviStaticInfo) {

    }

    @Override
    public void onArrivedWayPoint(int arg0) {


    }

    @Override
    public void onEndEmulatorNavi() {


    }

    @Override
    public void onGetNavigationText(int arg0, String arg1) {


    }

    @Override
    public void onGpsOpenStatus(boolean arg0) {


    }

    @Override
    public void onInitNaviFailure() {


    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {


    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {


    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {


    }

    @Override
    public void onReCalculateRouteForTrafficJam() {


    }

    @Override
    public void onReCalculateRouteForYaw() {


    }

    @Override
    public void onStartNavi(int arg0) {


    }

    @Override
    public void onTrafficStatusUpdate() {


    }

    @Override
    public void showCross(AMapNaviCross arg0) {


    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] arg0, byte[] arg1, byte[] arg2) {


    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo arg0) {


    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat arg0) {


    }
}
