package com.ihsinformatics.dynamicformsgenerator.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Naveed Iqbal on 4/6/2017.
 * Email: h.naveediqbal@gmail.com
 */

public class UnifiedBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = UnifiedBroadcastReceiver.class.getSimpleName();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final String ACTION_ATTEPT_TO_UPLOAD_DATA = "com.ihsinformatics.DATA_UPLOAD_ATTEMPT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (/*intent.getAction().equals(ACTION_CONNECTIVITY_CHANGE) || */intent.getAction().equals(ACTION_ATTEPT_TO_UPLOAD_DATA)) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnected();//isConnectedOrConnecting()
            boolean isWiFi = false;
            if (activeNetwork!=null) {
                isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            }
            if(isConnected) {
                new AllFormsUploader(context).startUploadingForms();
            }
        }
    }
}
