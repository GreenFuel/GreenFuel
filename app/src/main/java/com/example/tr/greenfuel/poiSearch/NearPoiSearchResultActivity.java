package com.example.tr.greenfuel.poiSearch;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.tr.greenfuel.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangpeng on 2017/2/14.
 */

public class NearPoiSearchResultActivity extends AppCompatActivity implements
        AMap.OnMapClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
        PoiSearch.OnPoiSearchListener {

    private MapView mapView;
    private AMap aMap;

    private Marker locationMaker;
    private Marker detailMarker;
    private Marker lastMarker;
    private MyPoiOverlay myPoiOverlay;  //poi图层

    private PoiResult poiResult;
    private int currPage = 0;
    private int resultPageSize = 20;
    private PoiSearch.Query query; //poi查询的条件
    private LatLonPoint latLonPoint = new LatLonPoint(39.993743, 116.472995);   //默认搜索中心位置
    private PoiSearch poiSearch;

    private List<PoiItem> poiItems; //poi数据

    private LinearLayout poiDetailInfo;
    private TextView searchTitle, navigate, poiName, poiAddress;
    private String keyWord = "";

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_search_result);

        initViews();

        mapView.onCreate(savedInstanceState);

        intent = getIntent();
        keyWord = intent.getStringExtra("keyWord");

        init();
        //开始搜索
        doSearchQuery();
    }

    private void initViews() {
        mapView = (MapView) findViewById(R.id.nearSearchResultMap);
        poiAddress = (TextView) findViewById(R.id.poi_address);
        searchTitle = (TextView) findViewById(R.id.nearSearchTitle);
        poiName = (TextView) findViewById(R.id.poi_name);
        navigate = (TextView) findViewById(R.id.tvNavigate);
        poiDetailInfo = (LinearLayout) findViewById(R.id.poi_detail);
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapClickListener(this);
            aMap.setOnMarkerClickListener(this);
            aMap.setOnInfoWindowClickListener(this);
            aMap.setInfoWindowAdapter(this);

            locationMaker = addLocationMarker();
            //locationMarker.showInfoWindow();
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()), 14));
        }
        searchTitle.setText("附近 - " + keyWord);
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NearPoiSearchResultActivity.this, "调用导航...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //向地图添加定位图标
    private Marker addLocationMarker() {
        return aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).
                icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.point5)))
                .position(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude())));
    }

    //开始进行poi搜索
    private void doSearchQuery() {
        currPage = 0;
        query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(resultPageSize);  // 设置每页最多返回多少条poiitem
        query.setPageNum(currPage); // 设置查第一页

        if (latLonPoint != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 5000, true));// 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn(); // 异步搜索
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        whetherToShowDetailInfo(false);
    }

    //显示poi-marker详细信息
    private void whetherToShowDetailInfo(boolean b) {
        if (b) {
            poiDetailInfo.setVisibility(View.VISIBLE);
        } else {
            poiDetailInfo.setVisibility(View.GONE);
        }
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
        whetherToShowDetailInfo(false);
        if (lastMarker != null) {
            resetLastMarker();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getObject() != null) {
            whetherToShowDetailInfo(true);
            PoiItem currPoiItem = (PoiItem) marker.getObject();
            if (lastMarker != null) {
                resetLastMarker();
            }
            lastMarker = marker;

            detailMarker = marker;
            detailMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(),
                            R.mipmap.poi_marker_pressed)));
            setPoiItemDisplayContent(currPoiItem);
        } else {
            whetherToShowDetailInfo(false);
            resetLastMarker();
        }
        return true;
    }

    //poi搜索成功回调
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {//搜索poi的结果
                if (poiResult.getQuery().equals(query)) {    //是否是同一条query
                    this.poiResult = poiResult;
                    poiItems = poiResult.getPois(); // 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        //清除POI信息显示
                        whetherToShowDetailInfo(false);
                        //并还原点击marker样式
                        if (lastMarker != null) {
                            resetLastMarker();
                        }
                        //清理之前搜索结果的marker
                        if (myPoiOverlay != null) {
                            myPoiOverlay.removeMarkersFromMap();
                        }
                        aMap.clear();

                        myPoiOverlay = new MyPoiOverlay(aMap, poiItems);
                        myPoiOverlay.addMarkersToMap();
                        myPoiOverlay.zoomToSpan();

                        addLocationMarker();

                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        Toast.makeText(this, R.string.no_result, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, R.string.no_result, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
        }
    }

    // 将之前被点击的marker置为原来的状态
    private void resetLastMarker() {
        int index = myPoiOverlay.getPoiIndex(lastMarker);
        if (index < 10) {
            lastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(),
                            markers[index])));
        } else {
            lastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.mipmap.marker_other_highlight)));
        }
        lastMarker = null;
    }

    //显示poi的信息
    private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
        poiName.setText(mCurrentPoi.getTitle());
        poiAddress.setText(mCurrentPoi.getSnippet() + mCurrentPoi.getDistance());
    }

    private int[] markers = {R.mipmap.poi_marker_1,
            R.mipmap.poi_marker_2,
            R.mipmap.poi_marker_3,
            R.mipmap.poi_marker_4,
            R.mipmap.poi_marker_5,
            R.mipmap.poi_marker_6,
            R.mipmap.poi_marker_7,
            R.mipmap.poi_marker_8,
            R.mipmap.poi_marker_9,
            R.mipmap.poi_marker_10
    };

    //自定义poi图层
    private class MyPoiOverlay {
        private AMap map;
        private List<PoiItem> mPoiItems;    //装poiItem
        private List<Marker> mPoiMarkers = new ArrayList<>();   //装poiItem对应的marker

        public MyPoiOverlay(AMap aMap, List<PoiItem> poiItems) {
            map = aMap;
            mPoiItems = poiItems;
        }

        //添加marker到地图中
        public void addMarkersToMap() {
            for (int i = 0; i < mPoiItems.size(); i++) {
                Marker marker = map.addMarker(getMakerOptions(i));
                PoiItem poiItem = mPoiItems.get(i);
                marker.setObject(poiItem);
                mPoiMarkers.add(marker);
            }
        }

        //去掉所有markers
        public void removeMarkersFromMap() {
            for (Marker marker : mPoiMarkers) {
                marker.remove();
            }
        }

        //返回每个poiItem对应的MarkerOptions
        private MarkerOptions getMakerOptions(int index) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPoiItems.get(index).getLatLonPoint()
                                    .getLatitude(), mPoiItems.get(index)
                                    .getLatLonPoint().getLongitude()))
                    .title(getMarkerTitle(index)).snippet(getSnippet(index))
                    .icon(getBitmapDescriptor(index));
        }

        //marker的图标
        private BitmapDescriptor getBitmapDescriptor(int index) {
            if (index < 10) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), markers[index]));
                return icon;
            } else {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.marker_other_highlight));
                return icon;
            }
        }

        //marker详细
        private String getSnippet(int index) {
            return poiItems.get(index).getSnippet();
        }

        //marker标题
        private String getMarkerTitle(int index) {
            return poiItems.get(index).getTitle();
        }

        //返回第index个PoiItem
        public PoiItem getPoiItem(int index) {
            if (index < 0 || index >= mPoiItems.size()) {
                return null;
            }
            return mPoiItems.get(index);
        }

        //根据marker获取poi在list中的位置
        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarkers.size(); i++) {
                if (mPoiMarkers.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }

        //移动镜头到当前视角(使得包括所有的poi)
        public void zoomToSpan() {
            if (mPoiItems != null && mPoiItems.size() > 0) {
                if (map == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }

        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < mPoiItems.size(); i++) {
                b.include(new LatLng(mPoiItems.get(i).getLatLonPoint().getLatitude(),
                        mPoiItems.get(i).getLatLonPoint().getLongitude()));
            }
            return b.build();
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

    public void back(View v) {
        finish();
    }
}
