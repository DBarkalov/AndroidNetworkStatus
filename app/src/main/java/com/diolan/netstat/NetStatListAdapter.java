package com.diolan.netstat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.diolan.netstat.data.DataEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by d.barkalov on 08.08.2014.
 */
public class NetStatListAdapter extends BaseAdapter {

    public static final String SELECTION_STATE = "SELECTION_STATE";
    private final LayoutInflater mInflatter;
    private List<DataEntry> mEntries;
    private final SimpleDateFormat mDateFormat;
    private final HashSet<DataEntry> mSelection;


    public NetStatListAdapter(Context context, HashSet<DataEntry> selection){
        mInflatter = LayoutInflater.from(context);
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        mSelection =  selection;
        mEntries = new ArrayList<DataEntry>();
    }

    public NetStatListAdapter(Context context){
        this(context, new HashSet<DataEntry>());
    }

    public void update(List<DataEntry> list) {
        if(list == null) {
            throw new IllegalArgumentException("list must be not null");
        }
        if(list.isEmpty()){
            mSelection.clear();
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

        if(isSelected(mEntries.get(position))) {
            convertView.findViewById(R.id.info_text).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.info_text)).setText(mEntries.get(position).getInfo());
        } else {
            convertView.findViewById(R.id.info_text).setVisibility(View.GONE);
        }

        convertView.findViewById(R.id.item_head).setTag(mEntries.get(position));
        convertView.findViewById(R.id.item_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DataEntry entry = (DataEntry) v.getTag();
                if (isSelected(entry)) {
                    mSelection.remove(entry);
                } else {
                    mSelection.add(entry);
                }
                NetStatListAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private boolean isSelected(final DataEntry dataEntry) {
        return mSelection.contains(dataEntry);
    }

    String formatDate(long t){
        return mDateFormat.format(t);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SELECTION_STATE, mSelection);
    }
}
