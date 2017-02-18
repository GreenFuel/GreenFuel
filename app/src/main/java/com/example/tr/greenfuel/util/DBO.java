package com.example.tr.greenfuel.util;

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
        //db=SQLiteDatabase.openOrCreateDatabase("/data/data/com.lingdududu.db/databases/stu.db",null);
        db = context.openOrCreateDatabase("mytest.db",MODE_PRIVATE,null);
        this.context = context;
    }
    //插入路径
    public void  insertToPaths(MyPaths path){
        db.execSQL("create table if not exists paths(_id integer primary key autoincrement," +
                "oName text not null,eName text not null,oLat double not null,oLng double not null," +
                "eLat double not null,eLng double not null)");
        db.execSQL("insert into paths(oName,eName,oLat,oLng,eLat,eLng)values('" +
                path.getOriginName()+"','"+path.getEndName()+"',"+path.getoLat()+","+path.getoLng()+","+path.geteLat()+","+path.geteLng()+")");
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
        db.execSQL("create table if not exists places(_id integer primary key autoincrement," +
                "name text not null,Lat double not null,Lng double not null,isCollection integer not null)");
        db.execSQL("insert into places(name,Lng,Lat,isCollection)values('" +
                palce.getName()+"',"+palce.getLng()+","+palce.getLat()+","+0+")");
        //db.execSQL("insert into places(name,Lng,Lat,isCollection)values('张三',20,2.3,1)");
    }
    public List<MyPlace> getMyPlace(){
        List<MyPlace> ps = new ArrayList<MyPlace>();
        //db.enableWriteAheadLogging();
        Cursor cursor = db.query("places",null,null,null,null,null,null);//db.rawQuery("select * from palces",null);
        Log.i("info","?????!!!!!!!!1");
        if(cursor!=null){
            Log.i("info","?????!!!!!!!!2--"+cursor.getColumnCount()+"|||"+cursor.getCount());
            Log.i("info","?????!!!!!!!!2-next-"+cursor.moveToNext());
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
    public void clearPaths(){
        db.execSQL("delete from paths where _id>0");
    }
    public void clearPlace(){
        db.execSQL("delete from places where _id>0");
    }
}

