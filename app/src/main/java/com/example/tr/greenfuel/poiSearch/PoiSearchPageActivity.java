package com.example.tr.greenfuel.poiSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.tr.greenfuel.MainActivity;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.util.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangpeng on 2017/2/13.
 */

public class PoiSearchPageActivity extends AppCompatActivity implements TextWatcher, Inputtips.InputtipsListener {

    private ListView listViewHistory;
    private ArrayAdapter<String> histories;
    private List<String> contents;

    private AutoCompleteTextView autoKeyWord;
    private List<String> autoResult;

    private MySQLiteOpenHelper mySQLiteOpenHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search_page);

        intiViews();
        initData();
    }

    private void intiViews() {
        listViewHistory = (ListView) findViewById(R.id.listviewSearchHistory);
        autoKeyWord = (AutoCompleteTextView) findViewById(R.id.autoKeyWord);
        autoKeyWord.addTextChangedListener(this);
        autoKeyWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(PoiSearchPageActivity.this, NearPoiSearchResultActivity.class).putExtra("keyWord", autoResult.get(position)).putExtra("fromActivity", 1));
            }
        });
    }

    private void initData() {
        //第二参数：数据库名字，第三个参数为空表示使用默认的CursorFactory,最后参数是数据库版本号
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this,"PoiSearchPage.db3",null,1);

        contents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            contents.add("搜索:" + i);
        }
        histories = new ArrayAdapter<String>(this, R.layout.listview_search_history, contents);
        listViewHistory.setAdapter(histories);
    }

    public void back(View v) {
        finish();
    }

    //点击搜索
    public void search(View v) {
        String keyWords = autoKeyWord.getText().toString();
        if (keyWords != null && !keyWords.trim().equals("")) {
            System.out.println("keyWords:" + keyWords);
            startActivity(new Intent(PoiSearchPageActivity.this, NearPoiSearchResultActivity.class).putExtra("keyWord", keyWords).putExtra("fromActivity", 1));
        } else {
            Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String keyWord = s.toString().trim();
        if (keyWord != null && !keyWord.equals("")) {
            InputtipsQuery inputtipsQuery = new InputtipsQuery(keyWord, MainActivity.cityName);
            //inputtipsQuery.setCityLimit(true);
            //System.out.println("cityName:" + MainActivity.cityName);
            Inputtips inputtips = new Inputtips(PoiSearchPageActivity.this, inputtipsQuery);
            inputtips.setInputtipsListener(this);
            inputtips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    //自动查询补充回调
    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            List<String> strings = new ArrayList<>();
            autoResult = strings;
            for (int j = 0; j < list.size(); j++) {
                strings.add(list.get(j).getName());
                System.out.println(list.get(j).getDistrict() + " : " + list.get(j).getAddress() + " ===> " + list.get(j).describeContents());
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.auto_complete_search, strings);
            autoKeyWord.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        } else {
            System.out.println("自动补充查询失败");
        }
    }

    //8中查询
    public void doSearch(View v) {
        Intent intent = new Intent(PoiSearchPageActivity.this, NearPoiSearchResultActivity.class);
        intent.putExtra("keyWord", ((TextView) v).getText().toString());
        intent.putExtra("fromActivity", 1);
        startActivity(intent);
    }

    private void test(){
        System.out.println("测试一下");
    }
}
