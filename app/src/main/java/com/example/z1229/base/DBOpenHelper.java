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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
