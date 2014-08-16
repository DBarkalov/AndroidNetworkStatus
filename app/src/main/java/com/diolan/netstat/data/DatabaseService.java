package com.diolan.netstat.data;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import com.diolan.netstat.data.NetworkChangesReceiver;

/**
 * Created by d.barkalov on 06.08.2014.
 */
public class DatabaseService extends IntentService {

    public static String ACTION_INSERT = "ACTION_INSERT";
    public static String ACTION_CLEAR = "ACTION_CLEAR";

    public DatabaseService() {
        super("SaveEventService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.getAction().equals(ACTION_INSERT)) {
            final DataEntry entry = getFromIntent(intent);
            if (entry != null) {
                saveEntry(entry);
            }
        } else if(intent.getAction().equals(ACTION_CLEAR)){
            removeAll();
        }
    }

    private DataEntry getFromIntent(Intent intent) {
        final Object data = intent.getSerializableExtra(NetworkChangesReceiver.EXTRA_WIFIDATAENTRY);
        if(data != null){
            return (DataEntry) data;
        }
        return null;
    }


    private void removeAll() {
        this.getContentResolver().delete(DataEntry.CONTENT_URI, null, null);
    }

    private void saveEntry(final DataEntry dataEntry){
        ContentValues values = new ContentValues();
        values.put(DataEntry.COLUMN_NAME_TIME, dataEntry.getEventTime());
        values.put(DataEntry.COLUMN_NAME_EVENT, dataEntry.getEvent());
        values.put(DataEntry.COLUMN_NAME_INFO, dataEntry.getInfo());
        this.getContentResolver().insert(DataEntry.CONTENT_URI,values);
    }

}
