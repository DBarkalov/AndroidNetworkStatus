package com.diolan.netstat;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.diolan.netstat.data.DataEntry;
import com.diolan.netstat.sql.StatDbHelper;

/**
 * Created by d.barkalov on 06.08.2014.
 */
public class DatabaseService extends IntentService {

    public static String ACTION_INSERT = "ACTION_INSERT";
    public static String ACTION_CLEAR = "ACTION_CLEAR";

    private static final int MSG_ON_CHANGED = 0x01;
    private NotificationHandler mHandler;

    public DatabaseService() {
        super("SaveEventService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new NotificationHandler(((App)getApplication()).getChangesObservable());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.getAction().equals(ACTION_INSERT)) {
            final DataEntry entry = getFromIntent(intent);
            if (entry != null) {
                if (saveEntry(entry) != -1) {
                    sendNotificationToMainThread();
                }
            }
        } else if(intent.getAction().equals(ACTION_CLEAR)){
            if (removeAll() > 0){
                sendNotificationToMainThread();
            }
        }
    }

    private long removeAll() {
        StatDbHelper dbHelper = new StatDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DataEntry.TABLE_NAME, "1", null);
    }


    private DataEntry getFromIntent(Intent intent) {
        final Object data = intent.getSerializableExtra(NetworkChangesReceiver.EXTRA_WIFIDATAENTRY);
        if(data != null){
            return (DataEntry) data;
        }
        return null;
    }

    private long saveEntry(DataEntry dataEntry){
        StatDbHelper dbHelper = new StatDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataEntry.COLUMN_NAME_TIME, dataEntry.getEventTime());
        values.put(DataEntry.COLUMN_NAME_EVENT, dataEntry.getEvent());
        values.put(DataEntry.COLUMN_NAME_INFO, dataEntry.getInfo());

        long newRowId = db.insert(
                DataEntry.TABLE_NAME,
                null,
                values);

        return newRowId;
    }

    private void sendNotificationToMainThread(){
        Message m = Message.obtain();
        m.what = MSG_ON_CHANGED;
        mHandler.sendMessage(m);
    }

    private static class NotificationHandler extends Handler {

        private final ChangesObservable mChangesObservable;

        private NotificationHandler(ChangesObservable mChangesObservable) {
            this.mChangesObservable = mChangesObservable;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ON_CHANGED:
                    mChangesObservable.notifyChanges();
                    break;
            }
        }
    }
}
