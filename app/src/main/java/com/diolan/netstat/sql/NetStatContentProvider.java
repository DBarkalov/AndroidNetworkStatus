package com.diolan.netstat.sql;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.diolan.netstat.data.DataEntry;

/**
 * Created by d.barkalov on 14.08.2014.
 */
public class NetStatContentProvider extends ContentProvider {

    private static final String TAG = "NetStatContentProvider";

    public static final String AUTHORITY = "com.diolan.netstat.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final int ALL = 1;
    private static final int DATA_ENTRY_ITEM = 2;
    private static UriMatcher matcher;

    static {
        matcher = new UriMatcher(0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            matcher.addURI(AUTHORITY, ("/" + DataEntry.CONTENT_PATH + "/*"), DATA_ENTRY_ITEM);
            matcher.addURI(AUTHORITY, ("/" + DataEntry.CONTENT_PATH), ALL);
        } else {
            matcher.addURI(AUTHORITY, (DataEntry.CONTENT_PATH + "/*"), DATA_ENTRY_ITEM);
            matcher.addURI(AUTHORITY, (DataEntry.CONTENT_PATH), ALL);
        }
    }

    private StatDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new StatDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DataEntry.TABLE_NAME);

        switch (matcher.match(uri)) {
            case ALL:
                break;
            case DATA_ENTRY_ITEM:
                String id = uri.getLastPathSegment();
                qb.appendWhere(DataEntry._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case ALL:
                return DataEntry.CONTENT_TYPE;
            case DATA_ENTRY_ITEM:
                return DataEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (matcher.match(uri) != ALL) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowId = db.insert(DataEntry.TABLE_NAME, null, values);

        Log.d(TAG, "insert row id =" + rowId);

        Uri result = null;

        if (rowId > 0) {
            result = ContentUris.withAppendedId(DataEntry.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(result, null);
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (matcher.match(uri)) {
            case ALL:
                count = db.delete(DataEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DATA_ENTRY_ITEM:
                String id = uri.getLastPathSegment();
                String where = DataEntry._ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                count = db.delete(DataEntry.TABLE_NAME, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (matcher.match(uri)) {
            case ALL:
                count = db.update(DataEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case DATA_ENTRY_ITEM:
                String id = uri.getLastPathSegment();
                String where = DataEntry._ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                count = db.update(DataEntry.TABLE_NAME, values, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

}
