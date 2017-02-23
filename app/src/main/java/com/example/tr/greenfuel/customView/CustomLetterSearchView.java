package com.example.tr.greenfuel.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/22.
 */

public class CustomLetterSearchView extends View {

    public static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };
    private Paint paint = new Paint();
    private int choose = -1;    //被点击的letter的索引
    private TextView dialogTextView;

    public void setDialogTextView(TextView textView){
        dialogTextView = textView;
    }

    //提供给listview的接口，当有onTouch事件时，通知listview更新
    public interface onTouchingLetterChangedListener{
        public void onTouchingLetterChanged(String letter);
    }
    private onTouchingLetterChangedListener listener;

    public void  setOnTouchingLetterChangedListener(onTouchingLetterChangedListener letterChangedListener){
        listener = letterChangedListener;
    }

    public CustomLetterSearchView(Context context) {
        super(context);
    }

    public CustomLetterSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLetterSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int mWidth = getWidth();
        int mHeight = getHeight();
        //单个字母的高度
        int singleHeight = mHeight/letters.length;
        //循环绘制每一个字母的样式与颜色
        for(int i = 0 ;i < letters.length ; i++){
            paint.setColor(getResources().getColor(R.color.color_bottom_text_normal));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(30);
            if(i == choose){//更改选中的字母颜色
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            //计算每个字母的绘制坐标
            float x = mWidth / 2 - paint.measureText(letters[i]) /2 ;
            float y = singleHeight*(i + 1);
            canvas.drawText(letters[i],x,y,paint);
            //重置画笔
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int oldChoose = choose;
        int pos = (int)(y/getHeight()*letters.length);  //计算点击字母的下标
        if(pos < 0){
            pos = 0;
        }
        if(pos >= letters.length){
            pos = letters.length - 1;
        }
        switch (action){
            case MotionEvent.ACTION_UP:
            //当手指抬起时，设置view的背景为透明
                setBackgroundResource(R.color.transparent);
                choose = -1;    //重置
                invalidate(); //重绘
                if(dialogTextView != null){
                    dialogTextView.setVisibility(INVISIBLE);
                }
                break;
            default:
                //设置背景为指定色
                setBackgroundResource(R.drawable.letter_view_bg_press);
                if(pos != oldChoose){//如果点击的字母与之前的不同
                    if(listener != null){
                        listener.onTouchingLetterChanged(letters[pos]);
                    }
                    //在左边显示选中的字母，在textview中
                    if(dialogTextView != null){
                        dialogTextView.setText(letters[pos]);
                        dialogTextView.setVisibility(VISIBLE);
                    }
                    choose = pos;
                }
                invalidate();
                break;
        }
        return true;
    }
}
