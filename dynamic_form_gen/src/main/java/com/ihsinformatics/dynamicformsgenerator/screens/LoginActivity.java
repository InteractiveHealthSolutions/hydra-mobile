package com.ihsinformatics.dynamicformsgenerator.screens;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserCredentials;
import com.ihsinformatics.dynamicformsgenerator.data.utils.JsonHelper;
import com.ihsinformatics.dynamicformsgenerator.network.DataSender;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.network.Sendable;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.NetworkProgressDialog;
import com.ihsinformatics.dynamicformsgenerator.utils.AES256Endec;
import com.ihsinformatics.dynamicformsgenerator.utils.BackupnRestore;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences.KEY;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.SecretKey;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends ToolbarActivity implements Sendable {
    private Button btnSignin;
    private TextView tvVersionNumber;
    private EditText etUsername, etPassword;
    private CheckBox cbRemember;
    private String username;
    private String password;
    private NetworkProgressDialog networkProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        addToolbar();
        networkProgressDialog = new NetworkProgressDialog(this);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        tvVersionNumber = (TextView) findViewById(R.id.tvVresionNumber);
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersionNumber.setText("Version: " + packageInfo.versionName);
        } catch (NameNotFoundException e1) {
            tvVersionNumber.setText("");
            e1.printStackTrace();
        }
        // Code to remember user
        final String username = GlobalPreferences.getinstance(this).findPrferenceValue(KEY.USERNAME, null);
        final String password = GlobalPreferences.getinstance(this).findPrferenceValue(KEY.PASSWORD, null);
        if (username != null && password != null) {
            Global.USERNAME = username;
            Global.PASSWORD = password;
            login(username, password);
        }
        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////////////////////////////////////////////////////
               /* try {
                    JSONArray jsonArray = new JSONArray();
                    SecretKey secKey = AES256Endec.getInstance().generateKey();
                    String encPassword = AES256Endec.getInstance().encrypt(etPassword.getText().toString().trim(), secKey);

                    jsonArray.put(new JSONObject().put(ParamNames.USERNAME, etUsername.getText().toString()));
                    jsonArray.put(new JSONObject().put(ParamNames.PASSWORD, encPassword));
                    jsonArray .put(new JSONObject().put("locationList","yes"));
                    new DataSender(
                            LoginActivity.this,
                            LoginActivity.this,
                            1,
                            "http://ihs.ihsinformatics.com:6928/openmrs/module/tipWeb/mobile.form")
                            .execute(//"data=[{\"USERNAME\":\"galaxy\"},{\"PASSWORD\":\"UT5KiMba0DsnQL1S5yL0AQ==\"},{\"providerList\":\"yes\"}]"
                                    jsonArray
                            );
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                ///////////////////////////////////////////////////////////
                if (!etUsername.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
                    //!!!
                    //First check against local db
                    UserCredentials userCred = DataAccess.getInstance().getUserCredentials(LoginActivity.this, etUsername.getText().toString());
                    if (userCred!=null && userCred.getPassword()!= null && !userCred.getPassword().isEmpty()) {
                        try {
                            if (userCred.getUsername().equals(etUsername.getText().toString()) && userCred.getPassword().equals(etPassword.getText().toString())) {
                                login(etUsername.getText().toString(), etPassword.getText()
                                        .toString());
                            } else {
                                Toasty.error(LoginActivity.this, getString(R.string.wrong_credentials_provided), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Logger.log(e);
                        }
                    } else {
                        try {
                            SecretKey secKey = AES256Endec.getInstance().generateKey();
                            String encPassword = AES256Endec.getInstance().encrypt(etPassword.getText().toString().trim(), secKey);
                            JSONObject array = new JSONObject();
                            array.put(ParamNames.USERNAME, etUsername.getText().toString());
                            // array.put(new JSONObject().put(ParamNames.PASSWORD, etPassword.getText().toString()));
                            array.put(ParamNames.PASSWORD, encPassword);
                            array.put(ParamNames.REQUEST_TYPE, ParamNames.VALIDATE_USER);
                            send(array, 0);
                        } catch (JSONException e) {
                            networkProgressDialog.dismiss();
                            Toasty.error(LoginActivity.this, "Unable to send request", Toast.LENGTH_LONG).show();
                            Logger.log(e);
                        } catch (Exception e) {
                            Logger.log(e);
                            Toasty.error(LoginActivity.this, "Error encrypting password", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toasty.error(LoginActivity.this, "Username or Password should not be left empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        // startActivity(new Intent(LoginActivity.this, MainMenu.class));
    }

    private void login(String username, String password) {
        if (cbRemember.isChecked()) {
            GlobalPreferences.getinstance(this).addOrUpdatePreference(KEY.USERNAME, etUsername.getText().toString());
            GlobalPreferences.getinstance(this).addOrUpdatePreference(KEY.PASSWORD, etPassword.getText().toString());
        }
        Global.USERNAME = username;
        Global.PASSWORD = password;
        networkProgressDialog.dismiss();
        // startActivity(new Intent(LoginActivity.this, MainScreen.class));
        Toasty.success(LoginActivity.this, "Successfully logged in", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void send(JSONObject data, int respId) {
        networkProgressDialog.show();
        new DataSender(this, this, 0).execute(data);
    }

    @Override
    public void onResponseReceived(JSONObject resp, int respId) {
        if(resp == null) {
            Toasty.error(this, getResources().getString(R.string.unauthenticated_user), Toast.LENGTH_LONG).show();
            return;
        }
        networkProgressDialog.dismiss();
        try {
            if (resp.getJSONObject(ParamNames.SERVER_RESULT).has(ParamNames.SERVER_ERROR)) {
                Toasty.error(this, resp.getJSONObject(ParamNames.SERVER_RESULT).getJSONObject(ParamNames.SERVER_ERROR).get(ParamNames.SERVER_MESSAGE).toString(), Toast.LENGTH_LONG).show();
                networkProgressDialog.dismiss();
                return;
            }
            //!!!
            //Save credentials to local db
            UserCredentials userCredentials = JsonHelper.getInstance(this).parseUserFromJson(etUsername.getText().toString(), etPassword.getText().toString(), resp.getJSONObject(ParamNames.SERVER_RESULT));
            if (userCredentials == null) {
                Toasty.error(this, getResources().getString(R.string.unauthenticated_user), Toast.LENGTH_LONG).show();
                networkProgressDialog.dismiss();
                return;
            }
            DataAccess.getInstance().insertUserCredentials(LoginActivity.this, userCredentials);
            login(etUsername.getText().toString(), etPassword.getText().toString());
            finish();
        } catch (JSONException e) {
            Toasty.error(this, "Could not parse server response", Toast.LENGTH_LONG).show();
            Logger.log(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(LoginActivity.this, ConnectionSetting.class));
            return true;
        } else if (id == R.id.action_export) {
            if (new BackupnRestore().takeEncryptedBackup("com.ihsinformatics.dynamicformsgenerator", "dynamicformsgenerator", "//DCIM", "Galaxy111")) {
                Toasty.success(this, "Data exported", Toast.LENGTH_LONG).show();
            } else {
                Toasty.error(this, "Could not export data, \nPlease check storage permissions", Toast.LENGTH_LONG).show();
            }
        } else if(id==R.id.action_about){
            startActivity(new Intent(LoginActivity.this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
