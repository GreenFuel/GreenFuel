package com.example.tr.greenfuel.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tangpeng on 2017/2/18.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static  final String CREATE_TABLE_SEARCH_HISTORIES = "create table search_histories(recent_time integer,content varchar(255) )";

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //第一次使用数据库，创建表
        db.execSQL(CREATE_TABLE_SEARCH_HISTORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
