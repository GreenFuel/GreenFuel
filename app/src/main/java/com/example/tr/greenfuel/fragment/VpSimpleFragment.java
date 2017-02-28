package com.example.tr.greenfuel.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.adapterSet.EmissionOrderListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import medusa.theone.waterdroplistview.view.WaterDropListView;

/**
 * Created by tangpeng on 2016/11/4.
 */

public class VpSimpleFragment extends Fragment implements WaterDropListView.IWaterDropListViewListener {

    private boolean isInit = false; //是否已经初始化数据

    private TextView myOrder;
    private WaterDropListView waterDropListView;

    private List<Map<String, Object>> mapList = new ArrayList<>();   //adpter的数据
    private EmissionOrderListViewAdapter simpleAdapter;    //填充listview

    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissProgressDialog();
            switch (msg.what) {
                case 1:
                    initAdapter();
                    System.out.println("初始化adapter");
                    break; //获取数据成功
                case 2:
                    waterDropListView.stopRefresh();
                    System.out.println("停止刷新");
                    break; //刷新停止
                case 3:
                    waterDropListView.stopLoadMore();
                    System.out.println("停止加载");
                    break; //加载停止
            }
        }
    };

    public static final String BUNDLE_TITLE = "title";  //参数的键，因为bundle保存键值对

    public static VpSimpleFragment newInstance(String title) {//推荐此种方法传参并生成fragment对象，而不是通过构造方法
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);  //将参数以键值对的形式存放
        VpSimpleFragment fragment = new VpSimpleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isVisible()) {
            if (!isInit) {//没有初始化数据
                initData();
                isInit = true;
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    //初始化排名数据
    private void initData() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }
        showProgressDialog();


        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);
                mHandler.sendEmptyMessage(1);
            }
        }.start();
    }

    //初始化Adapter
    private void initAdapter() {
        for (int i = 0; i < 30; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("order", (i + 1) + "");
            map.put("name", "张三" + i);
            map.put("emissionConsumption", i * 10 + "");
            map.put("imageURL", "http://cdn.sinacloud.net/tp-first-232217/img12.jpg?KID=sina,2fxabjfMjmKgh8HbRsmp&Expires=1488269868&ssig=Qa%2BWslbWV9");
            mapList.add(map);
        }
        simpleAdapter = new EmissionOrderListViewAdapter(getActivity(), mapList, R.layout.listview_item_emission_order, new String[]{"order", "name", "emissionConsumption", "imageURL"}
                , new int[]{R.id.textview_order, R.id.textview_user_name_order, R.id.textview_emission_consumption, R.id.imageview_user_header_order});
        waterDropListView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && !isInit)//当前界面可见且没有初始化
        {
            initData();
            isInit = true;
        }
    }

    @Nullable
    @Override   //返回的view是fragment的显示界面
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emission_order_country, null);
        myOrder = (TextView) rootView.findViewById(R.id.my_order);
        waterDropListView = (WaterDropListView) rootView.findViewById(R.id.listview_emission_order);
        waterDropListView.setWaterDropListViewListener(this);
        waterDropListView.setPullLoadEnable(true);
        return rootView;
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        System.out.println("下拉");
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(1000);
                mHandler.sendEmptyMessage(2);
            }
        }.start();
    }

    //上拉加载
    @Override
    public void onLoadMore() {
        System.out.println("上啦");
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(1000);
                mHandler.sendEmptyMessage(3);
            }
        }.start();
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载数据");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
