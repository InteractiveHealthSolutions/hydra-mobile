package com.ihsinformatics.dynamicformsgenerator.data;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Location;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Option;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Procedure;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserCredentials;
import com.ihsinformatics.dynamicformsgenerator.data.utils.JsonHelper;
import com.ihsinformatics.dynamicformsgenerator.network.DataSender;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.network.Sendable;
import com.ihsinformatics.dynamicformsgenerator.screens.LoginActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.AES256Endec;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;

/**
 * Created by Nabil shafi on 6/6/2018.
 */
public class DataSync extends Service implements Sendable {
    public static final int REQUEST_GET_PROVIDERS = 0;
    public static final int REQUEST_GET_PROCEDURES = 1;
    public static final int REQUEST_GET_LOCATIONS = 2;
    public static final int REQUEST_GET_EVALUATORS = 3;
    private static final int NOTIFICATION_ID = 1;

    private Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notification = createNotification(
                getResources().getString(R.string.title_sync_service),
                getResources().getString(R.string.desc_sync_service));


        startForeground(NOTIFICATION_ID, notification);
        try {
            downloadUsers();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }


    private Notification createNotification(String title, String contentText) {
        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
        Intent showTaskIntent = new Intent(getApplicationContext(), LoginActivity.class);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(contentText)
                .setSmallIcon(R.drawable.sync)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();

        return notification;

    }

    private void downloadUsers() throws Exception {

        SecretKey secKey = AES256Endec.getInstance().generateKey();
        String encPassword = AES256Endec.getInstance().encrypt(Global.PASSWORD, secKey);

        JSONObject sendableData = new JSONObject();
        JSONObject authentication = new JSONObject();

        authentication.put(ParamNames.USERNAME, Global.USERNAME);
        authentication.put(ParamNames.PASSWORD, encPassword);

        sendableData.put(ParamNames.AUTHENTICATION, authentication);
        sendableData.put(ParamNames.REQUEST_TYPE, "providerList"); // TODO paramname

        send(sendableData, REQUEST_GET_PROVIDERS);
    }

    private void downloadEvaluators() throws Exception {

        SecretKey secKey = AES256Endec.getInstance().generateKey();
        String encPassword = AES256Endec.getInstance().encrypt(Global.PASSWORD, secKey);

        JSONObject sendableData = new JSONObject();
        JSONObject authentication = new JSONObject();

        authentication.put(ParamNames.USERNAME, Global.USERNAME);
        authentication.put(ParamNames.PASSWORD, encPassword);

        sendableData.put(ParamNames.AUTHENTICATION, authentication);
        sendableData.put(ParamNames.REQUEST_TYPE, ParamNames.GET_EVALUATORS); // TODO paramname

        send(sendableData, REQUEST_GET_EVALUATORS);
    }

    private void downloadProcedures() throws Exception {

        SecretKey secKey = AES256Endec.getInstance().generateKey();
        String encPassword = AES256Endec.getInstance().encrypt(Global.PASSWORD, secKey);

        JSONObject sendableData = new JSONObject();
        JSONObject authentication = new JSONObject();

        authentication.put(ParamNames.USERNAME, Global.USERNAME);
        authentication.put(ParamNames.PASSWORD, encPassword);

        sendableData.put(ParamNames.AUTHENTICATION, authentication);
        sendableData.put(ParamNames.REQUEST_TYPE, "procedureList"); // TODO paramname

        send(sendableData, REQUEST_GET_PROCEDURES);
    }

    private void downloadLocations() throws Exception {

        SecretKey secKey = AES256Endec.getInstance().generateKey();
        String encPassword = AES256Endec.getInstance().encrypt(Global.PASSWORD, secKey);

        JSONObject sendableData = new JSONObject();
        JSONObject authentication = new JSONObject();

        authentication.put(ParamNames.USERNAME, Global.USERNAME);
        authentication.put(ParamNames.PASSWORD, encPassword);

        sendableData.put(ParamNames.AUTHENTICATION, authentication);
        sendableData.put(ParamNames.REQUEST_TYPE, "locationList"); // TODO paramname

        send(sendableData, REQUEST_GET_LOCATIONS);
    }

    @Override
    public void onDestroy() {
        DataProvider.reInitializeInstance(this);
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public void send(JSONObject data, int respId) {
        new DataSender(
                DataSync.this,
                DataSync.this,
                respId)
                .execute(data);
    }

    @Override
    public void onResponseReceived(JSONObject resp, int respId) {
        JSONArray results;
        switch (respId) {
            case REQUEST_GET_PROVIDERS:
                results = resp.optJSONArray(ParamNames.SERVER_RESPONSE); // TODO paramname
                if (results != null) {
                    Set<UserCredentials> userCredentials = JsonHelper.getInstance(this).parseUsersFromJson(results);
                    DataAccess.getInstance().insertOrReplaceUserCredentialsInTx(DataSync.this, userCredentials);
                }
                try {
                    downloadProcedures();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_GET_PROCEDURES:
                results = resp.optJSONArray(ParamNames.SERVER_RESPONSE); // TODO paramname
                if(results!=null) {
                    List<Procedure> procedureList = JsonHelper.getInstance(this).parseProceduresFromJson(results);
                    DataAccess.getInstance().insertProceduresInTx(DataSync.this, procedureList);
                }
                try {
                    downloadLocations();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_GET_LOCATIONS:
                results = resp.optJSONArray(ParamNames.SERVER_RESPONSE); // TODO paramname
                if(results!=null) {
                    List<Location> locations = JsonHelper.getInstance(this).parseLocationsFromJson(results);
                    // App.getDaoSession().getFormTypeDao().re
                    DataAccess.getInstance().insertLocations(this, locations);
                }

                try {
                    downloadEvaluators();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // stopSelf();
                break;
            case REQUEST_GET_EVALUATORS:
                results = resp.optJSONArray(ParamNames.PERSON); // TODO paramname
                if(results!=null) {
                    List<Option> options = JsonHelper.getInstance(this).parseOptionsFromJson(results);
                    System.out.println("");
                    // App.getDaoSession().getFormTypeDao().re
                    DataAccess.getInstance().insertOptions(this, options);
                }

                DataProvider.getInstance(this).reload();
                stopSelf();
                break;
        }
        System.out.println(resp);
    }
}
