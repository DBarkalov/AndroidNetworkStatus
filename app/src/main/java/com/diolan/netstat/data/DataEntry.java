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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataEntry)) return false;

        DataEntry entry = (DataEntry) o;

        if (mEvent != null ? !mEvent.equals(entry.mEvent) : entry.mEvent != null) return false;
        if (mEventTime != null ? !mEventTime.equals(entry.mEventTime) : entry.mEventTime != null)
            return false;
        if (mInfo != null ? !mInfo.equals(entry.mInfo) : entry.mInfo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mEventTime != null ? mEventTime.hashCode() : 0;
        result = 31 * result + (mInfo != null ? mInfo.hashCode() : 0);
        result = 31 * result + (mEvent != null ? mEvent.hashCode() : 0);
        return result;
    }
}
