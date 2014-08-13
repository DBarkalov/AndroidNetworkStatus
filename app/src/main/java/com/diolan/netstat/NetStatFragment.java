package com.diolan.netstat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.diolan.netstat.data.DataEntry;
import com.diolan.netstat.sql.StatDbHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NetStatFragment extends Fragment implements ChangesObservable.ChangesObserver {

    private static String TAG = "NetStatFragment";
    private NetStatListAdapter mAdapter;
    public static final int DLG_REQUEST_CLEAN = 2735;


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
        onChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        final ChangesObservable changesObservable = ((App) getActivity().getApplicationContext()).getChangesObservable();
        changesObservable.registerObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        final ChangesObservable changesObservable = ((App) getActivity().getApplicationContext()).getChangesObservable();
        changesObservable.unregisterObserver(this);
    }

    @Override
    public void onChanged() {
        Log.d(TAG, "onChanged()");
        //TODO cancel task
        new LoadCaptchaTask(this).execute();
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


    private static class LoadCaptchaTask extends AsyncTask<Void, Void, List<DataEntry>>{
        private final SQLiteDatabase db;
        private final WeakReference<NetStatFragment> mFragmentWeakReference;

        LoadCaptchaTask(NetStatFragment fragment){
            this.mFragmentWeakReference = new WeakReference<NetStatFragment>(fragment);
            StatDbHelper dbHelper = new StatDbHelper(fragment.getActivity());
            this.db = dbHelper.getReadableDatabase();
        }

        @Override
        protected List<DataEntry> doInBackground(Void... params) {

            String[] projection = {
                    DataEntry.COLUMN_NAME_TIME,
                    DataEntry.COLUMN_NAME_EVENT,
                    DataEntry.COLUMN_NAME_INFO
            };

            String sortOrder = DataEntry.COLUMN_NAME_TIME + " DESC";

            Cursor c = db.query(
                    DataEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            List<DataEntry> list= new ArrayList<DataEntry>();

            if(c != null){
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    list.add( new DataEntry(
                    c.getLong(c.getColumnIndex(DataEntry.COLUMN_NAME_TIME)),
                    c.getString(c.getColumnIndex(DataEntry.COLUMN_NAME_EVENT)),
                    c.getString(c.getColumnIndex(DataEntry.COLUMN_NAME_INFO))));
                    c.moveToNext();
                }
                c.close();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<DataEntry> list) {
            if(mFragmentWeakReference.get() != null){
                mFragmentWeakReference.get().updateListAdapter(list);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DLG_REQUEST_CLEAN && resultCode == Activity.RESULT_OK){
            clearAll();
        }
    }
}
