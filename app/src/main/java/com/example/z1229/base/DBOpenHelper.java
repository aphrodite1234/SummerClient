package com.example.z1229.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DBOpenHelper extends SQLiteOpenHelper {
    private Context mcontext;
    private Gson gson = new Gson();

    public DBOpenHelper(Context context) {
        super(context, "summer.db", null, 1);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cre_blood = "CREATE TABLE blood_info(id Integer PRIMARY KEY AUTOINCREMENT, " +
                "b_category text, b_value text, b_date text)";
        String cre_eat = "CREATE TABLE eat_info(id Integer PRIMARY KEY AUTOINCREMENT, " +
                "e_category text, e_ke text, e_cal text, e_date text)";
        String cre_sport = "CREATE TABLE sport_info(id Integer PRIMARY KEY AUTOINCREMENT," +
                "s_category text, s_cal text, s_date text, s_time text)";

        db.execSQL(cre_blood);
        db.execSQL(cre_eat);
        db.execSQL(cre_sport);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //存储血糖记录
    public void save_b(DBOpenHelper dbOpenHelper,String cate,String value,String date){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("blood_info",null,
                "b_category='"+cate+"' and b_date='"+date+"'",
                null,null,null,null);
        if(cursor.getCount()==0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("b_category",cate);
            contentValues.put("b_value",value);
            contentValues.put("b_date",date);
            db.insert("blood_info",null,contentValues);
        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("b_value",value);
            db.update("blood_info",contentValues,
                    "b_category='"+cate+"' and b_date='"+date+"'",null);
        }
        cursor.close();
    }
    //查询血糖记录
    public ArrayList<ArrayList<String>> search_b(DBOpenHelper dbOpenHelper, int item){
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("blood_info",null,
                "b_date > date('now','start of day','-"+item+" day')",
                null,null,null,"b_date ASC,b_category ASC");
        while (cursor.moveToNext()){
            ArrayList<String> one = new ArrayList<>();
            one.add(cursor.getString(cursor.getColumnIndex("b_category")));
            one.add(cursor.getString(cursor.getColumnIndex("b_value")));
            one.add(cursor.getString(cursor.getColumnIndex("b_date")));
            arrayLists.add(one);
        }
        cursor.close();
        return arrayLists;
    }

    //存储饮食记录
    public void save_e(DBOpenHelper dbOpenHelper,String cate,String item,String cal,String date){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = db.query("eat_info",null,
                "e_category='"+cate+"' and e_date='"+date+"'",
                null,null,null,null);
        if(cursor.getCount() == 0){
            contentValues.put("e_category",cate);
            contentValues.put("e_ke",item);
            contentValues.put("e_cal",cal);
            contentValues.put("e_date",date);
            db.insert("eat_info",null,contentValues);
        }else {
            contentValues.put("e_ke",item);
            contentValues.put("e_cal",cal);
            db.update("eat_info",contentValues,
                    "e_category='"+cate+"' and e_date='"+date+"'",null);
        }
        cursor.close();
    }
    //查询近几天饮食摄入
    public ArrayList<ArrayList<String>> search_e(DBOpenHelper dbOpenHelper, int item){
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String sql ="select e_date,sum(e_cal) from eat_info " +
                "where e_date > date('now','start of day','-"+item+" day') " +
                "group by e_date order by e_date asc";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            ArrayList<String> one = new ArrayList<>();
            one.add(cursor.getString(cursor.getColumnIndex("sum(e_cal)")));
            one.add(cursor.getString(cursor.getColumnIndex("e_date")));
            arrayLists.add(one);
        }
        cursor.close();
        return arrayLists;
    }
    //查询指定日期饮食记录
    public ArrayList<ArrayList<String>> search_e(DBOpenHelper dbOpenHelper, String date){
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("eat_info",null,
                "e_date = '"+date+"'",null,null,null,
                "e_category ASC");
        while (cursor.moveToNext()){
            ArrayList<String> one = new ArrayList<>();
            one.add(cursor.getString(cursor.getColumnIndex("e_category")));
            one.add(cursor.getString(cursor.getColumnIndex("e_ke")));
            one.add(cursor.getString(cursor.getColumnIndex("e_cal")));
            one.add(cursor.getString(cursor.getColumnIndex("e_date")));
            arrayLists.add(one);
        }
        cursor.close();
        return arrayLists;
    }

    //存储运动记录
    public void save_s(DBOpenHelper dbOpenHelper,String cate,String cal,String date,String time){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = db.query("sport_info",null,
                "s_date='"+date+"' and s_time ='"+time+"'",
                null,null,null,null);
        if(cursor.getCount() == 0){
            contentValues.put("s_category",cate);
            contentValues.put("s_cal",cal);
            contentValues.put("s_date",date);
            contentValues.put("s_time",time);
            db.insert("sport_info",null,contentValues);
        }else {
            contentValues.put("s_category",cate);
            contentValues.put("s_cal",cal);
            db.update("sport_info",contentValues,
                    "s_date='"+date+"' and s_time ='"+time+"'",null);
        }
        cursor.close();
    }
    //查询近几天运动消耗
    public ArrayList<ArrayList<String>> search_s(DBOpenHelper dbOpenHelper, int item){
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String sql ="select s_date,sum(s_cal) from sport_info " +
                "where s_date > date('now','start of day','-"+item+" day') " +
                "group by s_date order by s_date asc";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            ArrayList<String> one = new ArrayList<>();
            one.add(cursor.getString(cursor.getColumnIndex("sum(s_cal)")));
            one.add(cursor.getString(cursor.getColumnIndex("s_date")));
            arrayLists.add(one);
        }
        cursor.close();
        return arrayLists;
    }
    //查询指定日期运动记录
    public ArrayList<ArrayList<String>> search_s(DBOpenHelper dbOpenHelper, String date){
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("sport_info",null,
                "s_date = '"+date+"'",
                null,null,null,"s_time ASC");
        while (cursor.moveToNext()){
            ArrayList<String> one = new ArrayList<>();
            one.add(cursor.getString(cursor.getColumnIndex("s_category")));
            one.add(cursor.getString(cursor.getColumnIndex("s_cal")));
            one.add(cursor.getString(cursor.getColumnIndex("s_date")));
            one.add(cursor.getString(cursor.getColumnIndex("s_time")));
            arrayLists.add(one);
        }
        cursor.close();
        return arrayLists;
    }
}
