package com.diolan.netstat.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by d.barkalov on 06.08.2014.
 */
public class DataEntry implements BaseColumns, Serializable {

    public static final String TABLE_NAME = "network_stat";

    public static final String CONTENT_PATH = "network_stat";

    public static final Uri CONTENT_URI = Uri.parse("content://" +  NetStatContentProvider.AUTHORITY + "/" + CONTENT_PATH);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." +
            NetStatContentProvider.AUTHORITY + "." + TABLE_NAME;

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." +
            NetStatContentProvider.AUTHORITY + "." + TABLE_NAME;

    public static final String COLUMN_NAME_TIME = "eventtime" ;
    public static final String COLUMN_NAME_EVENT = "event";
    public static final String COLUMN_NAME_INFO = "info";

    public DataEntry( Long time, String event , String info) {
        this.mEventTime = time;
        this.mInfo = info;
        this.mEvent = event;
    }

    private Long mEventTime;
    private String mInfo;
    private String mEvent;

    public Long getEventTime() {
        return mEventTime;
    }

    public String getInfo() {
        return mInfo;
    }

    public String getEvent() {
        return mEvent;
    }
}
