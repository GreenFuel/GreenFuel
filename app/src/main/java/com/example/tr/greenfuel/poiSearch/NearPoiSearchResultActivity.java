package com.example.tr.greenfuel.poiSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.tr.greenfuel.R;

import java.util.List;

/**
 * Created by tangpeng on 2017/2/14.
 */

public class NearPoiSearchResultActivity extends AppCompatActivity  implements
        AMap.OnMapClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
        PoiSearch.OnPoiSearchListener {

    private MapView mapView;
    private AMap aMap;

    private Marker detailMarker;
    private Marker lastMarker;

    private PoiResult poiResult;
    private int currPage = 0;
    private int resultPageSize  = 20;
    private PoiSearch.Query query; //poi查询的条件
    private LatLonPoint latLonPoint = new LatLonPoint(39.993743, 116.472995);   //默认搜索中心位置
    private PoiSearch poiSearch;

    private List<PoiItem> poiItems; //poi数据

    private LinearLayout poiDetailInfo;
    private TextView searchTitle,navigate,poiName,poiAddress;
    private String keyWord = "";

    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_search_result);
        initViews();
        mapView.onCreate(savedInstanceState);
    }

    private void  initViews(){
        mapView = (MapView)findViewById(R.id.nearSearchResultMap);
        poiAddress = (TextView)findViewById(R.id.poi_address);
        searchTitle = (TextView)findViewById(R.id.nearSearchTitle);
        poiName = (TextView)findViewById(R.id.poi_name);
        navigate = (TextView)findViewById(R.id.tvNavigate);
        poiDetailInfo = (LinearLayout)findViewById(R.id.poi_detail);
    }

    private  void init(){
        if(aMap == null){
            aMap = mapView.getMap();
            aMap.setOnMapClickListener(this);
            aMap.setOnMarkerClickListener(this);
            aMap.setOnInfoWindowClickListener(this);
            aMap.setInfoWindowAdapter(this);

            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()), 14));
        }
    }

    //开始进行poi搜索
    private void doSearchQuery(){
        keyWord = "测试";
        currPage = 0 ;
        query = new PoiSearch.Query(keyWord,"","");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(resultPageSize);  // 设置每页最多返回多少条poiitem
        query.setPageNum(currPage); // 设置查第一页

        if(latLonPoint != null){
            poiSearch = new PoiSearch(this,query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint,500,true));// 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn(); // 异步搜索
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        //whetherToShowDetailInfo(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    //poi搜索回调
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if(i == AMapException.CODE_AMAP_SUCCESS){
            if(poiResult != null && poiResult.getQuery() != null){//搜索poi的结果
                if(poiResult.getQuery().equals(query)){    //是否是同一条query
                    this.poiResult = poiResult;
                    poiItems = poiResult.getPois(); // 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if(poiItems != null && poiItems.size() >0){

                    }
                }

            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(this, infomation, Toast.LENGTH_SHORT).show();
    }

}
