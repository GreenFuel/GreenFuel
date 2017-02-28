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

public class EmissionOrderActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ViewPagerIndicator viewPagerIndicator;

    private List<String> tabTitles = Arrays.asList(new String[]{"全国", "全省", "全市"});
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emission_order);

        initViews();
        initData();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        viewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.id_viewpager_indicator);
    }

    private void initData() {
        for (String title : tabTitles) {
            VpSimpleFragment fragment = VpSimpleFragment.newInstance(title);
            fragmentList.add(fragment);
        }

        mViewPager.setOffscreenPageLimit(2);    //设置缓存的fragment个数
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPagerIndicator.setVisibleTanCount(3);
        viewPagerIndicator.setItemTitles(tabTitles);
        viewPagerIndicator.setViewPager(mViewPager, 0);
    }


    public void back(View v) {
        finish();
    }
}
