package com.example.tr.greenfuel.mine;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.customView.CustomRoundedImageView;
import com.example.tr.greenfuel.loginRegister.LoginActivity;

import java.io.FileNotFoundException;

/**
 * Created by tangpeng on 2017/2/18.
 */

public class MineActivity extends AppCompatActivity implements View.OnClickListener{
    private CustomRoundedImageView userHead;  //用户头像

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initViews();
    }

    private void initViews(){
        userHead = (CustomRoundedImageView)findViewById(R.id.user_head_image);
        userHead.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_head_image:
                new AlertDialog.Builder(this).setItems(new String[]{"拍照","相册","取消"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //启动相机照相获取照片
                                startActivityForResult(intent,1);break;
                            case 1:
                                Intent intent1 = new Intent();
                                intent1.setType("image/*"); //设置要获取的内容为图片类型
                                intent1.setAction(Intent.ACTION_GET_CONTENT);   //设置动作为得到内容
                                startActivityForResult(intent1,2);break;
                            case 2: dialog.dismiss();
                        }
                    }
                }).create().show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1) {//拍照返回
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (userHead != null && bitmap != null) {
                    userHead.setBitmap(bitmap);
                }
            }else if(requestCode == 2){//相册返回
                    Uri uri = data.getData();   //图片的uri
                    ContentResolver resolver = getContentResolver();
                    try {
                        Bitmap bitmap1 = BitmapFactory.decodeStream(resolver.openInputStream(uri));
                        if (userHead != null && bitmap1 != null) {
                            userHead.setBitmap(bitmap1);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        System.out.println("获取图片异常");
                    }
                }
            }
        }

    //登录或注册
    public void loginRegister(View v){
        System.out.println("1");
        startActivity(new Intent(MineActivity.this, LoginActivity.class));
        System.out.println("2");
    }

    public void setting(View v){

    }
    public void back(View v){
        finish();
    }
}
