package com.diolan.wifistatelogger.data;

import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by d.barkalov on 06.08.2014.
 */
public class DataEntry implements BaseColumns, Serializable {
    public static final String  COLUMN_NAME_TIME = "eventtime" ;
    public static final String  COLUMN_NAME_EVENT = "event";
    public static final String COLUMN_NAME_INFO = "info";
    public static final String TABLE_NAME = "network_stat";

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
