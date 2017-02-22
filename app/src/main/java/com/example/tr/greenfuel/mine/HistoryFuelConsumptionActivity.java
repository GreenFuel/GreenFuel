package com.example.tr.greenfuel.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.customView.ViewPagerIndicator;
import com.example.tr.greenfuel.fragment.VpSimpleFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tangpeng on 2017/2/20.
 */

public class HistoryFuelConsumptionActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerIndicator viewPagerIndicator;
    private List<String> tabTitle = Arrays.asList(new String[]{"每日","每月"});
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_fuel_consumption);
        initViews();
        initData();
    }

    private void initViews(){
        viewPager = (ViewPager)findViewById(R.id.id_viewpager);
        viewPagerIndicator = (ViewPagerIndicator)findViewById(R.id.id_viewpager_indicator);
    }

    private void initData(){
        for(String str:tabTitle){
            VpSimpleFragment fragment = VpSimpleFragment.newInstance(str);
            fragmentList.add(fragment);
        }
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPagerIndicator.setRadioTriangleWidth(1/8f); //设置三角形底边的宽度
        viewPagerIndicator.setVisibleTanCount(2);   //设置可见的tab数量
        viewPagerIndicator.setItemTitles(tabTitle); //动态设置tab
        viewPagerIndicator.setViewPager(viewPager,0);   //与viewpager关联
    }

    public void back(View v){
        finish();
    }
}
