package com.example.tr.greenfuel.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/18.
 */

public class CustomRoundedImageView extends ImageView {

    private Bitmap imgSrc;
    private int type;   //图片类型
    private static final int TYPE_CIRLE = 0;    //圆形
    private static final int TYPE_ROUND = 1;    //圆角矩形
    private int cornerRadius;   //圆角半径

    private int mWidth; //控件的宽度
    private int mHeight;    //控件的高度


    public CustomRoundedImageView(Context context) {
        this(context,null);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomRoundedImageView,defStyleAttr,0);
        int n = array.getIndexCount();
        for(int i = 0 ; i < n ; i++){
            int index = array.getIndex(i);
            switch (index){
                case  R.styleable.CustomRoundedImageView_cornerRadius://圆角半径默认为10dp
                    cornerRadius = array.getDimensionPixelSize(index, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics()));break;
                case R.styleable.CustomRoundedImageView_imgSrc:
                    imgSrc = BitmapFactory.decodeResource(getResources(),array.getResourceId(index,0));break;
                case R.styleable.CustomRoundedImageView_type:
                    type = array.getInt(index,0);break;   //默认为圆形
            }
        }
        array.recycle();
    }

    //计算控件的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        //设置宽度
        if(specMode == MeasureSpec.EXACTLY){//match_parent或精确值
            mWidth = specSize;
        }else{
            int desireByImg = getPaddingLeft() + getPaddingRight() + imgSrc.getWidth(); //由图片决定的宽度
            if(specMode == MeasureSpec.AT_MOST){//wrap_parent
                mWidth = Math.min(desireByImg,specSize);
            }else mWidth = desireByImg;
        }

         specMode = MeasureSpec.getMode(heightMeasureSpec);
         specSize = MeasureSpec.getSize(heightMeasureSpec);
        //设置高度
        if(specMode == MeasureSpec.EXACTLY){//match_parent或精确值
            mHeight = specSize;
        }else{
            int desireByImg = getPaddingTop() + getPaddingBottom() + imgSrc.getHeight(); //由图片决定的宽度
            if(specMode == MeasureSpec.AT_MOST){//wrap_parent
                mHeight = Math.min(desireByImg,specSize);
            }else mHeight = desireByImg;
        }
        setMeasuredDimension(mWidth,mHeight);
        if(type == TYPE_CIRLE){
            int min = Math.min(mWidth,mHeight);
            //如果长度不一致，按照短边压缩
            imgSrc = getScaledBitmap(imgSrc,min);
        }
    }

    //动态设置图像
    public void setImageBitmap(Bitmap bitmap){
        if(type == TYPE_CIRLE){
            int min = Math.min(mWidth,mHeight);
            //如果长度不一致，按照短边压缩
            imgSrc = getScaledBitmap(bitmap,min);
        }else{
            imgSrc = bitmap;
        }
        postInvalidate();
    }

    //得到压缩后的图片
    private Bitmap getScaledBitmap(Bitmap bitmap,int min){
        return Bitmap.createScaledBitmap(bitmap,min,min,false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (type){
            case TYPE_CIRLE://绘制圆形
                canvas.drawBitmap(createCircleBitmap(imgSrc,Math.min(mWidth,mHeight)),0,0,null);break;
            case TYPE_ROUND://绘制圆角矩形
                canvas.drawBitmap(createRoundBitmap(imgSrc),0,0,null);break;
        }
    }

    //根据原图和边长绘制圆形
    private Bitmap createCircleBitmap(Bitmap src,int min){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min,min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target); //创建一个同样大小的画布
        //先绘制圆形
        canvas.drawCircle(min/2,min/2,min/2,paint);
        //设置为SRC_IN模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //再绘制图片
        canvas.drawBitmap(src,0,0,paint);
        return target;
    }

    //根据原图和半径绘制圆角矩形
    private Bitmap createRoundBitmap(Bitmap src){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target); //创建一个同样大小的画布
        RectF rectF = new RectF(0,0,mWidth,mHeight);
        //先绘制圆角矩形
        canvas.drawRoundRect(rectF,cornerRadius,cornerRadius,paint);
        //设置为SRC_IN模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //再绘制图片
        canvas.drawBitmap(src,0,0,paint);
        return target;
    }
}
