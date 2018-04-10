package com.escience.weather.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Stark on 2017/2/28.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String name = "data"; //数据库名称
    private static final int version = 1; //数据库版本
    public DBHandler(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS mood(sponsor varchar(20),nick varchar(16),head varchar(32),place varchar(100),temp varchar(4),sex varchar(2),skin varchar(2),msg varchar(512),msgcode varchar(20),msgcode2 varchar(20),anum varchar(10))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static ContentValues MapToContentValues(HashMap map){
        ContentValues cv=new ContentValues();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if(entry.getValue()!=null) {
                cv.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return cv;
    }
}