package com.example.tr.greenfuel.mine;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.tr.greenfuel.customView.CustomRoundedImageView;
import com.example.tr.greenfuel.loginRegister.LoginActivity;
import com.example.tr.greenfuel.util.MyMultipartRequest;

import java.io.File;
import java.io.FileNotFoundException;
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

    private static final int CAMERA_REQUEST_CODE = 1;   //相机
    private static final int GALLEY_REQUEST_CODE = 2;   //图库
    private static final int CROP_REQUEST_CODE = 3; //剪切
    private static final int MESSAGE_SUCCESS = 1;
    private static final int MESSAGE_FAILED = 2;

    private File tempFile;  //临时保存的拍照的照片
    private String tempFileName = "user_header.png";

    private CustomRoundedImageView userHead;  //用户头像

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
        userHead = (CustomRoundedImageView) findViewById(R.id.user_head_image);
        userHead.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (MainActivity.hasLogin || DEBUG) {
            switch (v.getId()) {
                case R.id.user_head_image:
                    new AlertDialog.Builder(this).setItems(new String[]{"拍照", "相册", "取消"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Log.i(TAG, "onClick: 1");
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //启动相机照相获取照片
                                    if (hasSdCard()) {
                                        tempFile = new File(Environment.getExternalStorageDirectory(), tempFileName);
                                        Log.i(TAG, "onClick:tempFile " + tempFile.getPath() + tempFile.getName());
                                        Uri uri = Uri.fromFile(tempFile);
                                        Log.i(TAG, "onClick: fileUri " + uri);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                    }
                                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                                    break;
                                case 1:
                                    Intent intent1 = new Intent();
                                    intent1.setType("image/*"); //设置要获取的内容为图片类型
                                    intent1.setAction(Intent.ACTION_GET_CONTENT);   //设置动作为得到内容
                                    startActivityForResult(intent1, GALLEY_REQUEST_CODE);
                                    break;
                                case 2:
                                    dialog.dismiss();
                            }
                        }
                    }).create().show();
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
            if (requestCode == CAMERA_REQUEST_CODE) {//拍照返回
                if (hasSdCard()) {
                    Log.i(TAG, "onActivityResult: " + Uri.fromFile(tempFile));
                    userHead.setImageBitmap(BitmapFactory.decodeFile(tempFile.getPath()));
                    //上传图片
                    uploadImg(tempFile.getPath());
                    if (data != null)
                        Log.i(TAG, "onActivityResult: getData" + data.getData());
                }
            } else if (requestCode == GALLEY_REQUEST_CODE) {//相册返回
                Uri uri = null;
                if (data != null)
                    uri = data.getData();   //图片的uri
                ContentResolver resolver = getContentResolver();
                try {
                    Bitmap bitmap1 = BitmapFactory.decodeStream(resolver.openInputStream(uri));
                    String filePath = getRealPathFromUri(uri);
                    if (filePath == null) {
                        Toast.makeText(this, "无法加载该照片，请更改！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    File imgFile = new File(filePath);
                    uploadImg(imgFile.getPath());
                    Log.i(TAG, "onActivityResult: imgFile.getPath()" + imgFile.getPath());
                    if (userHead != null && bitmap1 != null) {
                        userHead.setImageBitmap(bitmap1);
                        Log.i(TAG, "onActivityResult: data.getData()" + data.getData());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onActivityResult: FileNotFoundException", e);
                }
            }
        }
    }


    private void uploadImg(String filePath) {
        List<Part> partList = new ArrayList<>();
        partList.add(new StringPart("driPhone", "15520452757"));
        partList.add(new StringPart("driId", 2 + ""));
        showProgressDialog();
        try {
            partList.add(new FilePart("photo", new File(filePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "uploadImg: ", e);
        }
        MyMultipartRequest myMultipartRequest = new MyMultipartRequest(Request.Method.POST, BASIC_URL + IMG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println(s);
                handler.sendEmptyMessage(MESSAGE_SUCCESS);
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

    public String getRealPathFromUri(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
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

    //裁剪图片
    private void cropImg(Uri uri) {
        System.out.println("crop" + 1);
        //裁剪意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //裁剪框的比例 1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        //图片格式
        intent.putExtra("outputFormat", "png");
        //取消人脸识别
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
        System.out.println("crop" + 2);
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
