package com.example.tr.greenfuel.junge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckedTextView;
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
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.junge.pathProgramming.SetPath;
import com.example.tr.greenfuel.util.SensorEventHelper;

public class MainActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener {
    private MapView mapView=null;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMap aMap;

    private boolean mFirstFix = false;
    private Marker mLocMarker;
    public static final String LOCATION_MARKER_FLAG = "myLocation";
    private SensorEventHelper mSensorHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        mapView.onCreate(savedInstanceState);   //必须重写
        //初始化aMap
        init();
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
        aMap.getUiSettings().setMyLocationButtonEnabled(true);  //显示定位按钮
        aMap.setMyLocationEnabled(true); //显示定位层并可触发定位
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);  //设置定位方式：定位，跟随，旋转
    }

    //点击关键字搜索
    public void keySearch(View v){

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
                LatLng latLng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                if(!mFirstFix){
                    mFirstFix = true;
                    addMarker(latLng);  //在地图上添加定位箭头
                    mSensorHelper.setCurrentMarker(mLocMarker); //控制定位箭头旋转
                }else{
                    mLocMarker.setPosition(latLng);
                }
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
            }
        }else{
            String errStr = "定位失败,"+aMapLocation.getErrorCode()+":"+aMapLocation.getErrorInfo();
            Toast.makeText(this, errStr, Toast.LENGTH_SHORT).show();
        }
    }

    //添加marker
    private void addMarker(LatLng latLng) {
        if(mLocMarker != null){
            return ;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.mipmap.ssss);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(bitmapDescriptor);
        markerOptions.anchor(0.5f,0.5f);
        markerOptions.position(latLng);
        mLocMarker = aMap.addMarker(markerOptions);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }

    //激活定位
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.route:
                Intent i = new Intent(MainActivity.this,SetPath.class);
                startActivity(i);
                break;
        }
    }
}
