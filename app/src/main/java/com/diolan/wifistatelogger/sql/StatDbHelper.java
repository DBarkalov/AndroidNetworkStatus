package com.diolan.wifistatelogger.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.diolan.wifistatelogger.data.DataEntry;

/**
 * Created by d.barkalov on 06.08.2014.
 */
public class StatDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wifistat.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CREATE =
            "CREATE TABLE " + DataEntry.TABLE_NAME + " ( " +
            DataEntry.COLUMN_NAME_TIME + " INTEGER, "+
            DataEntry.COLUMN_NAME_EVENT + " TEXT, " +
            DataEntry.COLUMN_NAME_INFO + " TEXT );";

    public StatDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
