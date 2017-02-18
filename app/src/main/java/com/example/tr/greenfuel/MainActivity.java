package com.example.tr.greenfuel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.example.tr.greenfuel.junge.pathProgramming.SetPath;
import com.example.tr.greenfuel.model.MyPlace;
import com.example.tr.greenfuel.nearFunction.NearActivity;
import com.example.tr.greenfuel.poiSearch.PoiSearchPageActivity;
import com.example.tr.greenfuel.util.DBO;
import com.example.tr.greenfuel.util.SensorEventHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener,AMap.OnPOIClickListener,
        AMap.OnMarkerClickListener {
    private MapView mapView = null;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMap aMap;
    private AMapLocation aMapLocation;  //返回的定位信息
    private boolean mFirstFix = false;
    private Marker mLocMarker;
    private Marker poiTagMarker;    //点击poi时弹出的marker
    private TextView poiTextView;   //被点击poi上的tv
    public static final String LOCATION_MARKER_FLAG = "myLocation";
    private SensorEventHelper mSensorHelper;

    public static String cityName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        mapView.onCreate(savedInstanceState);   //必须重写
        //初始化aMap
        init();
        DBO dao = new DBO(this);
        dao.insertToPlace(new MyPlace("TEST",0.2,0.3,false));

        List<MyPlace> ps = new ArrayList<MyPlace>();
        ps =dao.getMyPlace();
        Log.i("info",""+ps.size());
        if(ps.size()>0){
            Log.i("info",ps.get(0).toString());
        }
    }

    private void findViews(){
        mapView= (MapView) findViewById(R.id.map);
    }

    private void init(){
        if(aMap == null)
        {
            aMap = mapView.getMap();
            setUpMap();
        }

        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
//        LatLng desLatLng = new LatLng(30.765207,103.989339);
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(desLatLng));
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
    }

    //设置aMap的属性
    private void setUpMap(){
        aMap.setLocationSource(this);     //设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);  //显示定位按钮
        aMap.setMyLocationEnabled(true); //显示定位层并可触发定位
        aMap.setMyLocationType(AMap.MAP_TYPE_NORMAL);  //设置定位方式：定位，跟随，旋转
        aMap.setOnMarkerClickListener(this);    //监听点击marker
        aMap.setOnPOIClickListener(this);   //监听点击poi
    }

    //定位到自己的位置
    public void backMyLocation(View v) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0)
            {//将地图定位到当前位置
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 18));
            }else{
                Toast.makeText(this, "定位失败！", Toast.LENGTH_SHORT).show();
            }
        }
    
    //点击关键字搜索
    public void keySearch(View v){
        startActivity(new Intent(MainActivity.this, PoiSearchPageActivity.class));
    }

    //点击实时路况开关
    public void realRouteChange(View v){
        CheckedTextView checkedTextView = (CheckedTextView)v;
        if(aMap != null){
            aMap.setTrafficEnabled(checkedTextView.isChecked());
        }
        if(checkedTextView.isChecked()){
            checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.route_situation_selected),null,null);
            System.out.println("checked");
            checkedTextView.setChecked(false);
        }else{
            checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.route_situation),null,null);
            System.out.println("unchecked");
            checkedTextView.setChecked(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSensorHelper != null){
            mSensorHelper.unregisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mapView.onPause();
        deactivate();
        mFirstFix = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSensorHelper != null){
            mSensorHelper.registerSensorListener();
        }else{
            mSensorHelper = new SensorEventHelper(this);
            if(mSensorHelper != null){
                if(mSensorHelper.getCurrentMarker() == null && mLocMarker != null){
                    mSensorHelper.setCurrentMarker(mLocMarker);
                }
            }
        }
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(mListener != null && aMapLocation != null){
            if(aMapLocation != null && aMapLocation.getErrorCode() == 0){
                this.aMapLocation = aMapLocation;
                cityName = aMapLocation.getCity();
                LatLng latLng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                if(!mFirstFix){
                    mFirstFix = true;
                    addMarker(latLng);  //在地图上添加定位箭头
                    mSensorHelper.setCurrentMarker(mLocMarker); //控制定位箭头旋转
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
                }else{
                        mLocMarker.setPosition(latLng);
                    }
                }
        }else{
            String errStr = "定位失败,"+aMapLocation.getErrorCode()+":"+aMapLocation.getErrorInfo();
            Toast.makeText(this, errStr, Toast.LENGTH_SHORT).show();
        }
    }

    //添加定位带箭头的marker
    private void addMarker(LatLng latLng) {
        if(mLocMarker != null){
            return ;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.mipmap.navi_arrow);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(bitmapDescriptor);
        markerOptions.anchor(0.5f,0.5f);
        markerOptions.position(latLng);
        mLocMarker = aMap.addMarker(markerOptions);
        //mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }

    //激活定位，打开定位服务
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if(null == mLocationClient){
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationClient.setLocationListener(this);  //设置定位监听
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置高精度定位模式
            mLocationClient.setLocationOption(mLocationOption); //设置定位参数
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if(null != mLocationClient){
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    //转到附近页面
    public void  goNearActivity(View v){
        startActivity(new Intent(MainActivity.this,NearActivity.class));
    }

    //设置路线
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.route:
                startActivity(new Intent(MainActivity.this, SetPath.class));
                break;
        }
    }

    //点击marker调用
    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, "marker被点击", Toast.LENGTH_SHORT).show();
        if(marker.equals(poiTagMarker)){
            marker.destroy();
        }
        //aMap.clear();
        return false;
    }

    //点击poi时调用
    @Override
    public void onPOIClick(Poi poi) {
        //aMap.clear();
        if(poiTagMarker != null)
            poiTagMarker.destroy();
        poiTextView = null;
        poiTextView = new TextView(this);
        poiTextView.setGravity(Gravity.CENTER);
        poiTextView.setTextColor(getResources().getColor(R.color.colorIconBlue));
        poiTextView.setBackgroundResource(R.drawable.custom_info_bubble);
        poiTextView.setText("到" + poi.getName() + "去");
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(poi.getCoordinate());    // 获取POI坐标
        markerOptions.icon(BitmapDescriptorFactory.fromView(poiTextView));
        poiTagMarker = aMap.addMarker(markerOptions);
        //poiTagMarker.setPosition(poi.getCoordinate());
    }
}
