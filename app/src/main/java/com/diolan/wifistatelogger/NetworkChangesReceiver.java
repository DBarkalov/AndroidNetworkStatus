package com.diolan.wifistatelogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.diolan.wifistatelogger.data.DataEntry;

import java.util.Date;

/**
 * Created by d.barkalov on 31.07.2014.
 */
public class NetworkChangesReceiver extends BroadcastReceiver {

    public static final String EXTRA_WIFIDATAENTRY = "EXTRA_WIFIDATAENTRY";

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            String eventInfo = null;
            String event;
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            Log.v("WifiStateChangesReceiver", "info=" + info.toString());
            event = info.getState().toString() + " " + info.getTypeName();
            eventInfo = info.toString();
            if (info.getState() == NetworkInfo.State.CONNECTED && (info.getType() == ConnectivityManager.TYPE_WIFI)) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                eventInfo+=  " ConnectionInfo=" + wifiInfo.toString();
                eventInfo+=" IpAddress:" + wifiInfo.getIpAddress();
            }

            Intent serviceIntent = new Intent(context, DatabaseService.class);
            serviceIntent.setAction(DatabaseService.ACTION_INSERT);
            serviceIntent.putExtra(EXTRA_WIFIDATAENTRY, new DataEntry(new Date().getTime(), event, eventInfo));
            context.startService(serviceIntent);
        }

    }

}
