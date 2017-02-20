package com.example.tr.greenfuel.loginRegister;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/19.
 */

public class FillPersonInfoActivity extends AppCompatActivity {

    private CheckBox agreeProtocol;
    private Button register;

    private TextView textEmissionType,textCarBrand,textCarType;
    private EditText editUserName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_person_info);
        initViews();

    }

    private void initViews(){
        agreeProtocol = (CheckBox)findViewById(R.id.agreeProtocol);
        register = (Button)findViewById(R.id.register);
        register.setClickable(agreeProtocol.isChecked());
        textEmissionType = (TextView)findViewById(R.id.textEmissionType);
        textCarBrand = (TextView)findViewById(R.id.textCarBrand);
        textCarType = (TextView)findViewById(R.id.textCarType);
        editUserName = (EditText)findViewById(R.id.editUserName);

        agreeProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                register.setClickable(isChecked);
            }
        });
    }

    //选择品牌
    public  void choseCarBrand(View v){

    }

    //选择车型
    public  void choseCarType(View v){

    }

    //选择排放标准
    public  void choseEmissionStandard(View v){
        new AlertDialog.Builder(this).setItems(new String[]{"国1","国2","国3","国4","国5"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case 0: textEmissionType.setText("国1");break;
                    case 1: textEmissionType.setText("国2");break;
                    case 2: textEmissionType.setText("国3");break;
                    case 3: textEmissionType.setText("国4");break;
                    case 4: textEmissionType.setText("国5");break;
                }
            }
        }).create().show();
    }

    //查看服务条款
    public void checkServiceItems(View v){

    }

    public void back(View v){
      finish();
    }
}
