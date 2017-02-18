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
    SQLiteDatabase db;
    Context context;
    public DBO(Context context) {
        db = context.openOrCreateDatabase("mytest.db",MODE_PRIVATE,null);
        this.context = context;
    }

    public void  insertToPaths(MyPaths path){ //插入路径
        db.execSQL("create table if not exists paths(_id integer primary key autoincrement," +
                "oName text not null,eName text not null,oLat double not null,oLng double not null," +
                "eLat double not null,eLng double not null)");
        db.execSQL("insert into paths(oName,eName,oLat,oLng,eLat,eLng)values('" +
                path.getOriginName()+"','"+path.getEndName()+"',"+path.getoLat()+","+path.getoLng()+","+path.geteLat()+","+path.geteLng()+")");
    }

    public List<MyPaths> getMyPaths(){  //获取历史路线
        List<MyPaths> ps = new ArrayList<MyPaths>();
        Cursor cursor=db.rawQuery("select * from paths",null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                MyPaths p = new MyPaths(cursor.getString(cursor.getColumnIndex("oName")),
                        cursor.getString(cursor.getColumnIndex("eName")),
                        cursor.getDouble(cursor.getColumnIndex("oLat")),
                        cursor.getDouble(cursor.getColumnIndex("oLng")),
                        cursor.getDouble(cursor.getColumnIndex("eLat")),
                        cursor.getDouble(cursor.getColumnIndex("eLng")));
                ps.add(p);
                Log.i("info",p.toString());
                Log.i("info","!!!!!!!!!!!!!!!!1");
            }
            cursor.close();
        }
        return ps;
    }
    //插入点
    public void  insertToPlace(MyPlace palce){  //插入点
        db.execSQL("create table if not exists places(_id integer primary key autoincrement," +
                "name text not null,Lat double not null,Lng double not null,isCollection integer not null)");
        int c = palce.isCollection()?1:0;
        db.execSQL("insert into places(name,Lng,Lat,isCollection)values('" +
                palce.getName()+"',"+palce.getLng()+","+palce.getLat()+","+c+")");
    }

    public List<MyPlace> getMyPlace(boolean b){   //表示是否为收藏的点
        List<MyPlace> ps = new ArrayList<MyPlace>();
        Cursor cursor = db.query("places",null,null,null,null,null,null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                boolean cc =  cursor.getInt(cursor.getColumnIndex("isCollection"))>0?true:false;
                MyPlace p = new MyPlace(cursor.getString(cursor.getColumnIndex("name")), cursor.getDouble(cursor.getColumnIndex("Lat")),
                        cursor.getDouble(cursor.getColumnIndex("Lng")),cc);
                if(b == cc)
                    ps.add(p);
            }
            cursor.close();
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

