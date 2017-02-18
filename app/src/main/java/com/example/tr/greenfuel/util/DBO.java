package com.example.tr.greenfuel.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tr.greenfuel.model.MyPaths;
import com.example.tr.greenfuel.model.MyPlace;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by TR on 2017/2/17.
 */

public class DBO {
    SQLiteDatabase db;//=// openOrCreateDatabase("mytest.db",MODE_PRIVATE,null);
    Context context;
    public DBO(Context context) {
        db = context.openOrCreateDatabase("mytest.db",MODE_PRIVATE,null);
    }
    //插入路径
    public void  insertToPaths(MyPaths path){
        db.execSQL("create table if not exists paths(_id integer primary key autoincrement," +
                "oName text not null,eName text not null,oLat double not null,oLng double not null," +
                "eLat double not null,eLng double not null)");
        ContentValues values=new ContentValues();
        values.put("oName",path.getOriginName());
        values.put("eName",path.getEndName());
        values.put("oLat",path.getoLat());
        values.put("oLat",path.getoLng());
        values.put("eLat",path.geteLat());
        values.put("eLng",path.geteLng());
        db.insert("paths",null,values);
    }
    public List<MyPaths> getMyPaths(){
        List<MyPaths> ps = new ArrayList<MyPaths>();
        Cursor cursor=db.rawQuery("select * from paths",null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                MyPaths p = new MyPaths(cursor.getString(cursor.getColumnIndex("oName")),cursor.getString(cursor.getColumnIndex("eName")),
                        cursor.getDouble(cursor.getColumnIndex("oLat")),cursor.getDouble(cursor.getColumnIndex("oLng")),
                        cursor.getDouble(cursor.getColumnIndex("eLat")),cursor.getDouble(cursor.getColumnIndex("eLng")));
                ps.add(p);
                Log.i("info",p.toString());
                Log.i("info","!!!!!!!!!!!!!!!!1");
            }
            cursor.close();
        }
        return ps;
    }
    //插入点
    public void  insertToPlace(MyPlace palce){
        db.execSQL("create table if not exists palces(_id integer primary key autoincrement," +
                "name text not null,Lat double not null,Lng double not null,isCollection integer not null)");
        ContentValues values=new ContentValues();
        values.put("name",palce.getName());
        values.put("Lat",palce.getLat());
        values.put("Lng",palce.getLng());
        if(palce.isCollection()){
            values.put("Lng",1);
        }else {
            values.put("Lng",0);
        }
        db.insert("palces",null,values);
    }
    public List<MyPlace> getMyPlace(){
        List<MyPlace> ps = new ArrayList<MyPlace>();
        Cursor cursor=db.query("palces",null,"_id>?",new String[]{"0"},null,null,"name");//db.rawQuery("select * from palces",null);
        Log.i("info","?????!!!!!!!!1");
        if(cursor!=null){
            Log.i("info","?????!!!!!!!!2");
            while(cursor.moveToNext()){
                Log.i("info","?????!!!!!!!!3");
                boolean cc =  cursor.getInt(cursor.getColumnIndex("isCollection"))>0?true:false;
                MyPlace p = new MyPlace(cursor.getString(cursor.getColumnIndex("name")), cursor.getDouble(cursor.getColumnIndex("Lat")),
                        cursor.getDouble(cursor.getColumnIndex("Lng")),cc);
                ps.add(p);
                Log.i("info",p.toString());
                Log.i("info","!!!!!!!!!!!!!!!!1");
            }
            cursor.close();
        }else{
            Log.i("info","?????!!!!!!!!1");
        }
        return ps;
    }
}

