package com.ihsinformatics.dynamicformsgenerator.network;
/**
 * @author naveed.iqbal
 * @description this class run in background on request of other class, communicate with server,
 * and receive response. If class calling it need it to return the response then calling class
 * must have to implement AsyncTaskResponseBridge interface, and call its
 * SendDataToserver(AsyncTaskResponseBridge atrb, int respId)constructor. Response will be returned as string
 * in recievedResponse(String resp, int respId) method of AsyncTaskResponseBridge interface.
 */

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.ihsinformatics.dynamicformsgenerator.utils.AppConfiguration;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataSender extends AsyncTask<JSONObject, integer, JSONObject> {
    private int respId;
    private Sendable sendable;
    private Context context;
    private String url;

    public DataSender(Context context, Sendable sendable, int respId) {
        this.sendable = sendable;
        this.respId = respId;
        this.context = context;
    }

    public DataSender(Context context, Sendable sendable, int respId, String url) {
        this(context, sendable, respId);
        this.url = url;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        try {
            if (!true) {
                return new JSONObject().put(ParamNames.SERVER_ERROR, "Device is not connected to internet");
            } else {
                String resp = "";
                //String serverAddress = url == null ? AppConfiguration.getServerAddress(context) + "/BachaKhan" : url;
                String serverAddress = url == null ? AppConfiguration.getServerAddress(context) + "/tipWeb/mobile.form" : url;
                HttpPost httppost = new HttpPost(serverAddress);
                System.out.println(serverAddress);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair(ParamNames.DATA, params[0].toString()));
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpParams httpParameters = new BasicHttpParams();
                    // Set the timeout in milliseconds until a connection is established.
                    // The default value is zero, that means the timeout is not used.
                    int timeoutConnection = 120000;
                    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                    // Set the default socket timeout (SO_TIMEOUT)
                    // in milliseconds which is the timeout for waiting for data.
                    int timeoutSocket = 120000;
                    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                    HttpClient httpclient = new DefaultHttpClient(httpParameters);
                    HttpResponse response = httpclient.execute(httppost);
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        InputStreamReader ir = new InputStreamReader(response.getEntity().getContent());
                        BufferedReader br = new BufferedReader(ir);
                        String b;
                        while ((b = br.readLine()) != null) {
                            resp += b;
                        }
                    } else {
                        resp = Global.errorMessage[5] + statusCode;
                        return new JSONObject().put(ParamNames.SERVER_ERROR, resp);
                    }
                } catch (HttpHostConnectException e) {
                    resp = Global.errorMessage[1];
                    Logger.log(e);
                    return new JSONObject().put(ParamNames.SERVER_ERROR, resp);
                } catch (ClientProtocolException e) {
                    resp = Global.errorMessage[2];
                    Logger.log(e);
                    return new JSONObject().put(ParamNames.SERVER_ERROR, resp);
                } catch (IOException e) {
                    resp = Global.errorMessage[3];
                    Logger.log(e);
                    return new JSONObject().put(ParamNames.SERVER_ERROR, resp);
                } catch (Exception e) {
                    Logger.log(e);
                    return new JSONObject().put(ParamNames.SERVER_ERROR, e.getMessage());
                }
                return new JSONObject(resp);
            }
        } catch (JSONException ee) {
            Logger.log(ee);
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        sendable.onResponseReceived(result, respId);
    }
    //TODO make use of it
    public static boolean isConnected(Activity activity) {
        ConnectivityManager connMgr = (ConnectivityManager) (activity).getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;    
    }
}
