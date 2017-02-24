package com.example.tr.greenfuel.nearFunction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tr.greenfuel.R;

import java.util.ArrayList;

/**
 * Created by tangpeng on 2017/2/23.
 */

public class WeiZhangActivity extends AppCompatActivity {

    private Spinner province;    //省别名
    private AutoCompleteTextView carNumber;  //车牌
    private AutoCompleteTextView carId; //车辆识别号
    private AutoCompleteTextView engineId;  //发动机号

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_zhang);
        sharedPreferences = getSharedPreferences("car_number", MODE_PRIVATE);
        initViews();
    }

    private void initViews() {
        province = (Spinner) findViewById(R.id.province);
        carNumber = (AutoCompleteTextView) findViewById(R.id.carNumber);
        carId = (AutoCompleteTextView) findViewById(R.id.carId);
        engineId = (AutoCompleteTextView) findViewById(R.id.engineId);

        setTextWatcher(carNumber, "carNumber");
        setTextWatcher(carId, "carId");
        setTextWatcher(engineId, "engineId");

    }

    private void setTextWatcher(final AutoCompleteTextView completeTextView, final String getStr) {

        completeTextView.addTextChangedListener(new MyTextWater() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);

                int number = sharedPreferences.getInt("number", 0);  //保存的记录数目
                if (number > 0) {//如果有记录
                    ArrayList<String> arrayList = new ArrayList<String>();
                    for (int i = 0; i < number; i++) {
                        arrayList.add(sharedPreferences.getString(getStr + i, null));
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WeiZhangActivity.this, R.layout.auto_complete_search, arrayList);
                    completeTextView.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
        completeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                completeTextView.setText(((TextView) view).getText());
            }
        });
    }


    //查询违章
    public void searchNow(View v) {
        String provinceStr = (String) province.getSelectedItem();
        String carNumberStr = carNumber.getText().toString().trim();
        String carIdStr = carId.getText().toString().trim();
        String engineIdStr = engineId.getText().toString().trim();
        if (provinceStr != null && carNumberStr != null && carNumberStr.length() == 6) {
            if (carIdStr != null && carIdStr.length() == 17) {
                if (engineIdStr != null && !engineIdStr.equals("")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int number = sharedPreferences.getInt("number", 0);
                    if (number >= 3) {
                        number = number % 3;
                        editor.putInt("number", 3);
                    } else {
                        editor.putInt("number", number + 1);
                    }
                    editor.putString("carNumber" + number, carNumberStr);
                    editor.putString("carId" + number, carIdStr);
                    editor.putString("engineId" + number, engineIdStr);
                    editor.commit();

                    Intent intent = new Intent(WeiZhangActivity.this, WeiZhangSearchResultActivity.class);
                    intent.putExtra("province", provinceStr);
                    intent.putExtra("carNumber", carNumberStr);
                    intent.putExtra("carId", carIdStr);
                    intent.putExtra("engineId", engineIdStr);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "请填写发动机号", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "请填全车辆识别号", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请填全车牌号", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View v) {
        finish();
    }


    class MyTextWater implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
