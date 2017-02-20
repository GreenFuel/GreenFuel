package com.example.tr.greenfuel.customView;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.tr.greenfuel.R;

import java.util.List;

/**
 * Created by tangpeng on 2016/11/mVisibleTabCount.
 */

public class ViewPagerIndicator extends LinearLayout {

    private Paint mPaint;
    private Path mPath; //绘制三角形的路径

    private int mTriangleWidth,mTriangleHeight; //三角形的宽度和高度

    private static final float RADIO_TRIANGLE_WIDTH = 1/6f; //三角形的底边的长度占一个tab宽的 1/6

    private int mInitTranslateX;    //三角形初始时的X坐标的偏移位置
    private int mTranslateX;    //三角形跟随手指滑动时的移动距离

    private int mVisibleTabCount ;  //顶部可见的tab
    private static final int COUNT_DEFAULT_TAB = 2;

    private static final int COLOR_TEXT_NORMAL = 0xcc000000;
    private static final int COLOR_TEXT_HIGHLIGHT = 0xff0b76dd;

    private ViewPager mViewPager;

    //提供给外部想要监听viewpager行为的接口，因为本控件使用了viewpager的OnPagerChangedListener接口
    public interface  OnPageChangeListener{
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
        void onPageScrollStateChanged(int state);
    }

    public OnPageChangeListener mListener;

    public ViewPagerIndicator(Context context) {
        this(context,null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mVisibleTabCount = types.getInt(R.styleable.ViewPagerIndicator_visible_tab_count,COUNT_DEFAULT_TAB);
        if(mVisibleTabCount < 0 )
        {
            mVisibleTabCount = COUNT_DEFAULT_TAB;
        }
        types.recycle();

        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xff0b76dd);
        mPaint.setPathEffect(new CornerPathEffect(3));  //避免线段太尖锐，设置断裂处一定圆角
    }

    //当控件的大小改变时触发，在这个方法中 计算并得到三角形路径
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth =(int)( w/mVisibleTabCount*RADIO_TRIANGLE_WIDTH);
        mInitTranslateX = w/mVisibleTabCount/2-mTriangleWidth/2;    //三角形初始的X偏移，默认指示第一个tab
        //初始化三角形path
        initTriangle();
    }

    //初始化三角形
    private void initTriangle() {
        mTriangleHeight = mTriangleWidth/2;
        mPath = new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth/2,-mTriangleHeight+2);    //使用的是canvas的绘制坐标，三角形的底边在X轴上，顶点在Y轴的负方向故高为负数
        mPath.close();
    }

    //ViewGroup在此方法绘制子view
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();  //防止画布被影响
        //将画布平移到三角形指示的tab正下方
        canvas.translate(mInitTranslateX+mTranslateX,getHeight());  //平移canvas的绘图坐标
        canvas.drawPath(mPath,mPaint);
        canvas.restore();
    }

    //设置三角形根据手指滑动重新绘制,指示器跟随手指移动，供viewpager页面滑动监听器调用
    public void scroll(int position, float positionOffset) {
        int tabWidth = getWidth()/mVisibleTabCount;
        mTranslateX = (int) (position*tabWidth+positionOffset*tabWidth);

        //当Indicator移到当前可见tab的倒数第二时，就移动容器即ViewPagerIndicator
        if(position >= (mVisibleTabCount - 2) && positionOffset > 0 && getChildCount() > mVisibleTabCount && getChildCount() - 2  !=  position)
        {
            if (mVisibleTabCount == 1) {
                this.scrollTo((int)(positionOffset*tabWidth)+position*tabWidth,0);
            }else this.scrollTo((position-(mVisibleTabCount - 2))*tabWidth + (int)(positionOffset*tabWidth),0);
        }
        //立刻重绘三角形
        invalidate();
    }

    //当view的xml文件加载完成后调用，在这个方法中去设置可见子控件的宽度
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount = getChildCount();
        if(cCount == 0) return;
        for(int i = 0 ; i < cCount ; i++)
        {
            View v =  getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth()/mVisibleTabCount;   //计算每个子view的宽度
            v.setLayoutParams(lp);
        }
    }

    //动态设置可见tab个数，在setItemTitles之前调用
    public void setVisibleTanCount(int count)
    {
        mVisibleTabCount = count;
    }

    //根据传入的标题动态添加tab标题，而不是在xml文件中去写子view
    public void setItemTitles(List<String> titles)
    {
        if(titles != null && titles.size() > 0 )
        {
            this.removeAllViews();  //无视xml中的子view，只能动态产生
            for(String title: titles)
             addView(generateTextView(title));
        }
    }

    //动态生成的tab -- textview
    private View generateTextView(String titles) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth()/mVisibleTabCount;
        tv.setText(titles);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setLayoutParams(lp);
        return tv;
    }

    //获取屏幕宽度
    public int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Service.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }


    //设置与ViewPager相关联
    public void setViewPager(ViewPager viewPager, int curPos)
    {
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //position是当前页面的索引，positionOffset是随页面切换 从0到1变化的值
            scroll(position,positionOffset);
            if(mListener != null)
            mListener.onPageScrolled( position,  positionOffset,  positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            if(mListener != null)
            mListener.onPageSelected( position);
            setTextHighLight(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(mListener != null)
            mListener.onPageScrollStateChanged( state);
        }
    });
        mViewPager.setCurrentItem(curPos);
        setTextHighLight(curPos);
        addTabClickEvent();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener){
        mListener = listener;
    }

    //将选中tab的文本颜色设置为高亮
    private void setTextHighLight(int pos)
    {
        resetTextColor();
        TextView v = (TextView)getChildAt(pos);
        v.setTextColor(COLOR_TEXT_HIGHLIGHT);
    }

    //重置所有文本颜色
    private void resetTextColor()
    {
        for(int i = 0 ; i < getChildCount() ; i++)
        {
            TextView v = (TextView) getChildAt(i);
            v.setTextColor(COLOR_TEXT_NORMAL);
        }
    }

    //点击tab改变viewpager内容
    private void addTabClickEvent(){
        for(int i = 0 ; i < getChildCount() ; i++)
        {
            final int j = i ;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
