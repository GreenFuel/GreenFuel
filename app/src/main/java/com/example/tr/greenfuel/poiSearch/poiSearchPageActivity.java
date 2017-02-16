package com.example.tr.greenfuel.poiSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tr.greenfuel.R;

/**
 * Created by tangpeng on 2017/2/13.
 */

public class PoiSearchPageActivity extends AppCompatActivity {

    private EditText editKeyWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search_page);

        intiViews();
    }

    private void intiViews(){
        editKeyWord = (EditText)findViewById(R.id.etKeyWord);
    }

    public void back(View v){
        finish();
    }

    //点击搜索
    public void search(View v){
        String keyWords = editKeyWord.getText().toString();
        if(keyWords != null && !keyWords.trim().equals(""))
        {
            System.out.println("keyWords:"+keyWords);
            startActivity(new Intent(PoiSearchPageActivity.this,NearPoiSearchResultActivity.class).putExtra("keyWord",keyWords).putExtra("fromActivity",1));
        }
        else {
            Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
        }
    }
}
