package com.diolan.netstat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.diolan.netstat.data.DataEntry;
import com.diolan.netstat.data.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class NetStatFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor> {

    private static String TAG = "NetStatFragment";
    private NetStatListAdapter mAdapter;
    public static final int DLG_REQUEST_CLEAN = 2735;
    private static final int STAT_LOADER_ID = 0;


    public NetStatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wifi_log, container, false);
        getLoaderManager().initLoader(STAT_LOADER_ID, null, this);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            showConfirmDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showConfirmDialog() {
        DialogFragment newFragment = ConfirmDialog.newInstance(R.string.action_clear);
        newFragment.setTargetFragment(this, DLG_REQUEST_CLEAN);
        newFragment.show(getFragmentManager(), "yes_no_dialog");
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new NetStatListAdapter(getActivity());
        listView.setAdapter(mAdapter);
    }


    protected void updateListAdapter(final List<DataEntry> list){
        if(isAdded()){
            mAdapter.update(list);
        }
    }

    private void clearAll(){
        Intent serviceIntent = new Intent(getActivity(), DatabaseService.class);
        serviceIntent.setAction(DatabaseService.ACTION_CLEAR);
        getActivity().startService(serviceIntent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case STAT_LOADER_ID:

                String[] projection = {
                        DataEntry.COLUMN_NAME_TIME,
                        DataEntry.COLUMN_NAME_EVENT,
                        DataEntry.COLUMN_NAME_INFO
                };

                String sortOrder = DataEntry.COLUMN_NAME_TIME + " DESC";

                return new CursorLoader(
                        getActivity(),   // Parent activity context
                        DataEntry.CONTENT_URI,        // uri
                        projection,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        sortOrder             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        if (loader.getId() == STAT_LOADER_ID) {
            List<DataEntry> list = new ArrayList<DataEntry>();
            if (c != null) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    list.add(new DataEntry(
                            c.getLong(c.getColumnIndex(DataEntry.COLUMN_NAME_TIME)),
                            c.getString(c.getColumnIndex(DataEntry.COLUMN_NAME_EVENT)),
                            c.getString(c.getColumnIndex(DataEntry.COLUMN_NAME_INFO))));
                    c.moveToNext();
                }
                //c.close(); do not close ! Loader menage it
            }
            updateListAdapter(list);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //release previous cursor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DLG_REQUEST_CLEAN && resultCode == Activity.RESULT_OK){
            clearAll();
        }
    }
}
