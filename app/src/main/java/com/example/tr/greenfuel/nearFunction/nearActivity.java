package com.example.tr.greenfuel.nearFunction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.adapterSet.MyViewPagerAdapter;
import com.example.tr.greenfuel.poiSearch.NearPoiSearchResultActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangpeng on 2017/2/14.
 */

public class NearActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<ImageView> imageViewsList;
    private int[] images = {R.mipmap.ad1,R.mipmap.ad2,R.mipmap.ad3,R.mipmap.ad4,R.mipmap.ad5};
    private LinearLayout linearLayoutDot;
    private List<ImageView> imageViewsDotList;
    private int currPosition = 1;   //图片在viewpager中的位置
    private int dotPosition = 0;    //圆点当前位置
    private int preDotPosition = 0; //上一个选中圆点的位置
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                viewPager.setCurrentItem(currPosition);
            }
        }
    };
    private Intent intent;  //跳转到搜索结果的activity
    private Intent weiZhangIntent ; //跳转到违章查询的activity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);
        initViews();
        initData();
        setDot();
        setViewPager();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                System.out.println("线程："+thread.getName()+"异常,"+ex.getMessage());
            }
        });

        autoPlay();

        intent = new Intent(this, NearPoiSearchResultActivity.class);

    }

    private void initViews(){
        viewPager = (ViewPager)findViewById(R.id.nearViewPager);
        linearLayoutDot = (LinearLayout)findViewById(R.id.linerLayoutDot);
    }

    //初始化图片集合
    private void initData(){
        imageViewsList = new ArrayList<>();
        imageViewsDotList = new ArrayList<>();
        ImageView imageView;
        for(int  i = 0 ; i < images.length+2 ; i++){
            //在viewpager的正式队列内容中前后又加了一个imageview，为了实现无限循环流畅
            imageView = new ImageView(this);
            if(i == 0){//将第0张iamgeview设置成viewpager的最后一张的内容
                imageView.setBackgroundResource(images[images.length-1]);
            }else if(i == images.length + 1){//将最后一张iamgeview设置成viewpager的第一张的内容
                imageView.setBackgroundResource(images[0]);
            }else{
                imageView.setBackgroundResource(images[i-1]);
            }
            imageViewsList.add(imageView);
        }
    }

    //设置轮播小圆点
    private void setDot(){
        //设置单个小圆点的宽高单位像素,即LinearLayout子空间的布局
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,15);
        params.rightMargin = 20;
        //创建images.length个小圆点
        for(int i = 0 ; i < images.length ; i++){
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            //设置小圆点的背景为暗红图片
            imageView.setBackgroundResource(R.drawable.red_dot_night);
            linearLayoutDot.addView(imageView);
            imageViewsDotList.add(imageView);
        }
        //设置第一个圆点被选中
        imageViewsDotList.get(dotPosition).setBackgroundResource(R.drawable.red_dot);
    }

    //配置viewpager
    private void setViewPager(){
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(imageViewsList);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(currPosition);
        //监听页面切换
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){//将页面切换到最后一张的内容
                    currPosition = images.length;
                    dotPosition = images.length - 1;
                }else if(position == images.length+1){//将页面切换到第一张的内容
                    currPosition = 1;
                    dotPosition = 0;
                }else{
                    currPosition = position;
                    dotPosition = position - 1;
                }
                //将之前的小圆点背景改为暗红色，现在的小圆点改为红色
                imageViewsDotList.get(preDotPosition).setBackgroundResource(R.drawable.red_dot_night);
                imageViewsDotList.get(dotPosition).setBackgroundResource(R.drawable.red_dot);
                preDotPosition = dotPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //当state为SCROLL_STATE_IDLE 即没有滑动的状态时 切换页面
                if(state==ViewPager.SCROLL_STATE_IDLE){
                    viewPager.setCurrentItem(currPosition,false);
                }
            }
        });
        }

    //设置自动播放
    private void autoPlay() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep(3000);
                    currPosition++;
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    public void back(View v){
        finish();
    }

    public void searchDelicious(View v){//查询美食
        intent.putExtra("keyWord","美食");
        startActivity(intent);
    }
    public void searchHotel(View v){//酒店
        intent.putExtra("keyWord","酒店");
        startActivity(intent);
    }
    public void searchFamousPlace(View v){//景点
        intent.putExtra("keyWord","景点");
        startActivity(intent);
    }
    public void searchMovie(View v){//电影
        intent.putExtra("keyWord","电影");
        startActivity(intent);
    }
    public void searchBank(View v){//银行
        intent.putExtra("keyWord","银行");
        startActivity(intent);
    }
    public void searchGasStation(View v){//加油站
        intent.putExtra("keyWord","加油站");
        startActivity(intent);
    }
    public void searchPark(View v){//停车场
        intent.putExtra("keyWord","停车场");
        startActivity(intent);
    }
    public void searchSubway(View v){//地铁
        intent.putExtra("keyWord","地铁");
        startActivity(intent);
    }
    public void searchWeiZhang(View v){//违章查询

    }
}
