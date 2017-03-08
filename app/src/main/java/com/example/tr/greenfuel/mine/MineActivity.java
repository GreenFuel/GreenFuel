package com.example.tr.greenfuel.mine;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tr.greenfuel.MainActivity;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.loginRegister.LoginActivity;
import com.example.tr.greenfuel.util.MyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangpeng on 2017/2/18.
 */

public class MineActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MineActivity";

    private static final boolean DEBUG = true;

    private static final String BASIC_URL = "http://192.168.1.126:8080/lcx/servlet";
    private static final String IMG_URL = "/DriverHeadPhotoUpload";

    String fileNameCamera = "image_header_camera.png";   // 拍照得到的临时文件名
    String fileNameCrop = "image_header_crop.png";   //裁剪后得到的临时文件名
    //最终截取到的图片的uri
    private Uri finalUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileNameCrop));
    private int cropWidth = 500;    //截取框的宽度
    private int cropHeight = 500;   //截取框的高度

    public static final int FROM_CROP = 1;  //从相机或图库中返回
    private static final int CROP_PICTURE = 2;  //裁剪返回

    private static final int MESSAGE_SUCCESS = 1;
    private static final int MESSAGE_FAILED = 2;

    private ImageView userHead;  //用户头像

    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgressDialog();
            if (msg.what == MESSAGE_SUCCESS) {
                Toast.makeText(MineActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == MESSAGE_FAILED) {
                Toast.makeText(MineActivity.this, "上传失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initViews();

        requestQueue = Volley.newRequestQueue(this);
    }

    private void initViews() {
        userHead = (ImageView) findViewById(R.id.user_head_image);
        userHead.setOnClickListener(this);
        TextView d = (TextView) findViewById(R.id.distance);
        TextView f = (TextView) findViewById(R.id.fule);
        TextView c = (TextView) findViewById(R.id.carbon);
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();

        d.setText(""+df.format(sp.getFloat("distance",0.0f)));
        f.setText(""+df.format(sp.getFloat("fule",0.0f)));
        c.setText(""+df.format(sp.getFloat("carbon",0.0f)));
    }

    @Override
    public void onClick(View v) {
        if (MainActivity.hasLogin || DEBUG) {
            switch (v.getId()) {
                case R.id.user_head_image:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    if (hasSdCard()) {//是否有存储卡
                                        Uri imgUri = null;
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //调用相机
                                        imgUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileNameCamera));
                                        //指定照片保存路径，image_header_camera.png是一个临时文件,数据最终保存到imgUri中
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                                        startActivityForResult(intent, FROM_CROP);
                                    } else {
                                        Toast.makeText(MineActivity.this, "没有存储卡！", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 1://相册选取
                                    Intent libraryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    libraryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                    startActivityForResult(libraryIntent, FROM_CROP);
                            }
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                    break;
            }
        } else {
            Toast.makeText(this, "请登录！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FROM_CROP:
                    Uri uri = null;
                    if (data != null) {//得到数据的uri(相册)
                        uri = data.getData();
                    } else {//得到临时保存的文件(照相)的uri
                        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileNameCamera));
                    }
                    //开始截图
                    cropImg(uri, cropWidth, cropHeight, CROP_PICTURE);
                    break;
                case CROP_PICTURE://系统截图返回
                    Bitmap photo = null;
                    photo = decodeUriAsBitmap(finalUri);    //通过 之前生成的文件的uri来获得返回的照片，而不是data对象
                    Log.i(TAG, "onActivityResult: " + finalUri);
                    //显示截图结果
                    userHead.setImageBitmap(photo);
                    //上传头像
                    Log.i(TAG, "onActivityResult: finalUri.getPath() " + finalUri.getPath());
                    uploadImg(finalUri.getPath());
                    break;
            }
        }
    }

    //调用系统截图
    private void cropImg(Uri uri, int i, int i1, int cropPicture) {
        //系统自带截图
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置截取的图片信息
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //裁剪框的比例 1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", cropWidth);
        intent.putExtra("outputY", cropHeight);
        //图片格式
        intent.putExtra("outputFormat", "png");
        //取消人脸识别
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);  //如果为true，图片会包含在data中返回，当数据过大时（一般是1M）就会崩溃，
        // 所以这里不通过data返回，而是通过下面的uri来获取该照片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, finalUri);

        startActivityForResult(intent, CROP_PICTURE);
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            //进行图片的压缩截取
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "decodeUriAsBitmap: FileNotFoundException ", e);
            return null;
        }
        return bitmap;
    }

    private void uploadImg(String filePath) {
        List<Part> partList = new ArrayList<>();
        //partList.add(new StringPart("driPhone", "15520452757"));
        partList.add(new StringPart("driId", 2 + ""));
        showProgressDialog();
        try {
            partList.add(new FilePart("filebody", new File(filePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "uploadImg: ", e);
        }
        MyMultipartRequest myMultipartRequest = new MyMultipartRequest(Request.Method.POST, BASIC_URL + IMG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {//上传成功
                        handler.sendEmptyMessage(MESSAGE_SUCCESS);
                    } else {//上传失败
                        handler.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onRespe:JSONException ", e);
                }
                Log.i(TAG, "onRespe: jsonObject " + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "onErrorResponse: ", volleyError);
                handler.sendEmptyMessage(MESSAGE_FAILED);
            }
        }, partList.toArray(new Part[partList.size()]));
        requestQueue.add(myMultipartRequest);
    }

    public void goEmissionOrder(View v) {//尾气排放榜单
        if (MainActivity.hasLogin || DEBUG) {
            startActivity(new Intent(MineActivity.this, EmissionOrderActivity.class));
        } else {
            Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
        }
    }

    public void goHistoryData(View v) {//查看历史数据
        if (MainActivity.hasLogin || DEBUG) {
            startActivity(new Intent(MineActivity.this, HistoryDataActivity.class));
        } else {
            Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
        }
    }

    public void goSavePoint(View v) {//收藏点
        startActivity(new Intent(MineActivity.this, SavePointActivity.class));
    }

    //登录或注册
    public void loginRegister(View v) {
        startActivity(new Intent(MineActivity.this, LoginActivity.class));
    }

    //转到设置
    public void setting(View v) {
        startActivity(new Intent(MineActivity.this, SettingActivity.class));
    }

    public void back(View v) {
        finish();
    }


    private boolean hasSdCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("上传头像...");
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
