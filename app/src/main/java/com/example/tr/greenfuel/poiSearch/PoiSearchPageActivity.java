package com.example.tr.greenfuel.poiSearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.tr.greenfuel.MainActivity;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.model.SearchHistoryClass;
import com.example.tr.greenfuel.util.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tangpeng on 2017/2/13.
 */

public class PoiSearchPageActivity extends AppCompatActivity implements TextWatcher, Inputtips.InputtipsListener {

    private ListView listViewHistory;
    private ArrayAdapter<String> histories;
    private List<String> contents;  //listview的内容

    private AutoCompleteTextView autoKeyWord;
    private List<String> autoResult;    //输入自动提示列表

    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private List<SearchHistoryClass> newSearchRecord = new ArrayList<>();   //新的搜索记录
    ArrayList<SearchHistoryClass> searchHistoryList;    //SQLite中保存的搜索历史

    private View footer;
    private Button clearRecord; //清除搜索记录
    private Button moreRecord;  //加载更多记录

    private static final int ITEMS_PER_PAGE = 3;   //每页加载的记录数
    private int currIndex = 0;  //从searchHistoryList已加载的最后一条数据的索引

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

                SearchHistoryClass searchHistory = new SearchHistoryClass(autoResult.get(position), System.currentTimeMillis());
                newSearchRecord.add(searchHistory);
                //System.out.println("点击Item:"+newSearchRecord.toString());
                addToContents(autoResult.get(position));
                clearRecord.setVisibility(View.VISIBLE);    //使清除按钮可见

                startActivity(new Intent(PoiSearchPageActivity.this, NearPoiSearchResultActivity.class).putExtra("keyWord", autoResult.get(position)).putExtra("fromActivity", 1));
            }
        });
    }

    private void initData() {
        //第二参数：数据库名字，第三个参数为空表示使用默认的CursorFactory,最后参数是数据库版本号
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this, "PoiSearchPage.db3", null, 1);
        //从SQLite获取数据并按照时间排序
        searchHistoryList = cursor2ArrayList(getSearchHistory());
        Collections.sort(searchHistoryList);
        System.out.println(searchHistoryList.toString());
        if (searchHistoryList.size() > 100) {//如果记录已经多余100条则，删除100条以后的记录
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    deleteRecordFromSQLite();
                }
            }.start();
        }

        footer = getLayoutInflater().inflate(R.layout.listview_footer_2buttons, null);
        moreRecord = (Button) footer.findViewById(R.id.moreHistories);
        moreRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadMoreRecord();
                histories.notifyDataSetChanged();
            }
        });
        clearRecord = (Button) footer.findViewById(R.id.clearHistories);
        clearRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PoiSearchPageActivity.this).setMessage("清空历史纪录？").setPositiveButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearAllRecord();
                        contents.clear();
                        histories.notifyDataSetChanged();
                        if (contents.size() <= 0) {
                            footer.setVisibility(View.GONE);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        });
        listViewHistory.addFooterView(footer);

        contents = new ArrayList<>();

        loadMoreRecord();

        histories = new ArrayAdapter<String>(this, R.layout.listview_search_history, contents);
        listViewHistory.setAdapter(histories);


        if (contents.size() <= 0) {
            footer.setVisibility(View.GONE);
        }
    }

    //将刚刚查询的内容添加到listview中
    private void addToContents(String str) {
        boolean isNew = true;   //该内容在之前的列表是否存在
        int index = -1;         //旧记录的下标
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).equals(str)) {
                isNew = false;
                index = i;
                break;
            }
        }
        if (isNew) {//没有存在与列表中
            contents.add(0, str);
        } else {
            contents.remove(index);
            contents.add(0, str);
        }
        histories.notifyDataSetChanged();
    }

    private void loadMoreRecord(){//加载更多的历史记录
        currIndex = contents.size();    //定位到上一次加载项的下一项索引
        if (searchHistoryList.size() < currIndex + ITEMS_PER_PAGE) {//所有的历史记录不到一页
            for (int i = currIndex; i < searchHistoryList.size(); i++) {
                contents.add(searchHistoryList.get(i).getContent());
            }
            moreRecord.setVisibility(View.GONE);
        } else {
            for (int i = currIndex; i < currIndex + ITEMS_PER_PAGE; i++) {
                contents.add(searchHistoryList.get(i).getContent());
            }
        }
    }

    //清除所有的记录
    private void clearAllRecord() {
        mySQLiteOpenHelper.getReadableDatabase().execSQL("delete from search_histories where recent_time > 0", new Object[]{});
    }

    //删除多余的历史纪录
    private void deleteRecordFromSQLite() {
        mySQLiteOpenHelper.getReadableDatabase().execSQL("delete from search_histories where recent_time < ?", new Object[]{searchHistoryList.get(99).getRecentTime()});
    }

    //获取搜索历史记录
    private Cursor getSearchHistory() {
        return mySQLiteOpenHelper.getReadableDatabase().rawQuery("select * from search_histories", new String[]{});
    }

    //将cursor封装成ArrayList<SearchHistoryClass>
    private ArrayList<SearchHistoryClass> cursor2ArrayList(Cursor cursor) {
        ArrayList<SearchHistoryClass> searchHistoryList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            //cursor.moveToFirst();   //指针指向第一行数据，如果在存数据之前执行moveToNext就可以不用这条
            while (cursor.moveToNext()) {//是否含有下一条数据
                SearchHistoryClass searchHistoryClass = new SearchHistoryClass(cursor.getString(1), cursor.getInt(0));
                searchHistoryList.add(searchHistoryClass);
            }
        }
        return searchHistoryList;
    }

    //向search_histories数据表增加数据
    private void addToSQLite() {
        if (newSearchRecord != null && newSearchRecord.size() > 0) {
            for (SearchHistoryClass searchHistory : newSearchRecord) {
                int count = 100;    //默认历史记录最多一百条
                boolean exist = false;
                count = Math.min(count, searchHistoryList.size());
                for (int i = 0; i < count; i++) {
                    if (searchHistory.getContent().equals(searchHistoryList.get(i).getContent())) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {//更新已经存在的搜索记录对应的时间
                    mySQLiteOpenHelper.getReadableDatabase().execSQL("update search_histories set recent_time = ? where content = ?", new Object[]{searchHistory.getRecentTime(), searchHistory.getContent()});
                    //System.out.println("更新数据："+searchHistory.getContent());
                } else {//插入新的搜索记录
                    mySQLiteOpenHelper.getReadableDatabase().execSQL("insert into search_histories values(?,?)", new Object[]{searchHistory.getRecentTime(), searchHistory.getContent()});
                    //System.out.println("插入数据："+searchHistory.getContent());
                }
            }
        }
    }

    public void back(View v) {
        finish();
    }

    //点击搜索
    public void search(View v) {
        String keyWords = autoKeyWord.getText().toString();
        if (keyWords != null && !keyWords.trim().equals("")) {

            SearchHistoryClass searchHistory = new SearchHistoryClass(keyWords, System.currentTimeMillis());
            newSearchRecord.add(searchHistory);
            //System.out.println("点击搜索:"+newSearchRecord.toString());
            addToContents(keyWords);
            clearRecord.setVisibility(View.VISIBLE);    //使清除按钮可见
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
                //System.out.println(list.get(j).getDistrict() + " : " + list.get(j).getAddress() + " ===> " + list.get(j).describeContents());
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.auto_complete_search, strings);
            autoKeyWord.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        } else {
            System.out.println("自动补充查询失败");
        }
    }

    //8种查询
    public void doSearch(View v) {
        Intent intent = new Intent(PoiSearchPageActivity.this, NearPoiSearchResultActivity.class);
        intent.putExtra("keyWord", ((TextView) v).getText().toString());
        intent.putExtra("fromActivity", 1);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread() {
            @Override
            public void run() {
                super.run();
                //System.out.println("开始："+newSearchRecord.toString());
                addToSQLite();
                //关闭数据库连接
                if (mySQLiteOpenHelper != null) {
                    mySQLiteOpenHelper.close();
                }
                //System.out.println("结束："+"关闭数据库");
            }
        }.start();
    }
}
