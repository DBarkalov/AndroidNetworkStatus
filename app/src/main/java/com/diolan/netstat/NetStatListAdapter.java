package com.diolan.netstat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.diolan.netstat.data.DataEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by d.barkalov on 08.08.2014.
 */
public class NetStatListAdapter extends BaseAdapter {

    private final LayoutInflater mInflatter;
    private List<DataEntry> mEntries = new ArrayList<DataEntry>();
    private final SimpleDateFormat mDateFormat;

    public NetStatListAdapter(Context context){
        mInflatter = LayoutInflater.from(context);
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    public void update(List<DataEntry> list) {
        if(list == null){
            throw new IllegalArgumentException("list must be not null");
        }
        mEntries = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return mEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflatter.inflate(R.layout.list_item, null);
        }
        ((TextView)convertView.findViewById(R.id.time_text)).setText(formatDate(mEntries.get(position).getEventTime()));
        ((TextView)convertView.findViewById(R.id.event_text)).setText(mEntries.get(position).getEvent());
        ((TextView)convertView.findViewById(R.id.info_text)).setText(mEntries.get(position).getInfo());

        return convertView;
    }

    String formatDate(long t){
        return mDateFormat.format(t);
    }

}
