package com.example.tr.greenfuel.loginRegister.aboutCar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.customView.CustomLetterSearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by tangpeng on 2017/2/22.
 */

public class CarBrandActivity extends AppCompatActivity {

    private ListView listView;
    private TextView dialogTextView;
    private CustomLetterSearchView customLetterSearchView;  //字母栏

    private List<CarBrand> carBrandList;    //汽车品牌集合
    private CharacterParser characterParser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand);

        intiData();
        initViews();
    }

    private void intiData() {
        characterParser = CharacterParser.getInstance();
        String[] carNames = getResources().getStringArray(R.array.car_brand);
        carBrandList = new ArrayList<>();
        for (int i = 0; i < carNames.length; i++) {
            CarBrand carBrand = new CarBrand();
            carBrand.setBrandName(carNames[i]);
            String pinyin = characterParser.getSelling(carNames[i]);
            carBrand.setSortLetter(pinyin.substring(0, 1).toUpperCase());
            carBrandList.add(carBrand);
        }
        Collections.sort(carBrandList, new Comparator<CarBrand>() {//对carBrandList的数据按拼音先后排序
            @Override
            public int compare(CarBrand lhs, CarBrand rhs) {
                return lhs.getSortLetter().compareTo(rhs.getSortLetter());
            }
        });
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.listview_car_brand);
        dialogTextView = (TextView) findViewById(R.id.textview_dialog_letter);
        customLetterSearchView = (CustomLetterSearchView) findViewById(R.id.customview_letter_search);
        customLetterSearchView.setDialogTextView(dialogTextView);

        final CarBrandListViewAdapter carBrandListViewAdapter = new CarBrandListViewAdapter(carBrandList, this);
        listView.setAdapter(carBrandListViewAdapter);

        customLetterSearchView.setOnTouchingLetterChangedListener(new CustomLetterSearchView.onTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                //将定位listview到该字母处
                int pos = carBrandListViewAdapter.getPositionFromSection(letter.charAt(0));
                if (pos != -1) {
                    listView.setSelection(pos);
                }
            }
        });
    }

    public void back(View v) {
        finish();
    }
}
