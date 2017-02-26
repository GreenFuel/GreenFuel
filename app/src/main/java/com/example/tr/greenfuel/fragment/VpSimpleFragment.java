package com.example.tr.greenfuel.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tr.greenfuel.R;

import medusa.theone.waterdroplistview.view.WaterDropListView;

/**
 * Created by tangpeng on 2016/11/4.
 */

public class VpSimpleFragment extends Fragment implements WaterDropListView.IWaterDropListViewListener{

    private WaterDropListView waterDropListView;
    private TextView myOrder;

    public static final String BUNDLE_TITLE = "title";  //参数的 键，因为bundle保存键值对

    public static VpSimpleFragment newInstance(String title) {//推荐此种方法传参并生成fragment对象，而不是通过构造方法
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);  //将参数以键值对的形式存放
        VpSimpleFragment fragment = new VpSimpleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser && isVisible()){

        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Nullable
    @Override   //返回的view是fragment的显示界面
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emission_order_country,null);
        myOrder = (TextView) rootView.findViewById(R.id.my_order);
        waterDropListView = (WaterDropListView)rootView.findViewById(R.id.listview_emission_order);

        Bundle bundle = getArguments(); //获取创建时保存的参数
        if (bundle != null && bundle.containsKey(BUNDLE_TITLE)) {
           // mTitle = bundle.getString(BUNDLE_TITLE);
        }
        TextView tv = new TextView(getActivity());
        //tv.setText(mTitle);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
