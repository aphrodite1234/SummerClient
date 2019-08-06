package com.example.z1229.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

public class DBOpenHelper extends SQLiteOpenHelper {
    private Context mcontext;
    private Gson gson = new Gson();

    public DBOpenHelper(Context context) {
        super(context, "summer.db", null, 1);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cr_blood = "CREATE TABLE bloodline(id Integer PRIMARY KEY AUTOINCREMENT, morning_a float," +
                "morning_b float, noon_a float, noon_b float, evening_a float, evening_b float,bed float, b_date text)";
        String cr_eat = "CREATE TABLE eatbar(id Integer PRIMARY KEY AUTOINCREMENT, morning float," +
                "noon float, evening float,bed float, e_date text)";

//        db.execSQL(cr_blood);
//        db.execSQL(cr_eat);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
