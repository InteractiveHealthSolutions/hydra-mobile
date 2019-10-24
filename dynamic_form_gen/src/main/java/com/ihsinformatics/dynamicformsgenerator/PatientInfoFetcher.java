package com.ihsinformatics.dynamicformsgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient;
import com.ihsinformatics.dynamicformsgenerator.data.utils.JsonHelper;
import com.ihsinformatics.dynamicformsgenerator.network.DataSender;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.network.Sendable;
import com.ihsinformatics.dynamicformsgenerator.network.pojos.Patient;
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData;
import com.ihsinformatics.dynamicformsgenerator.screens.Form;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.NetworkProgressDialog;
import com.ihsinformatics.dynamicformsgenerator.utils.AES256Endec;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

import javax.crypto.SecretKey;

import es.dmoral.toasty.Toasty;

public class PatientInfoFetcher extends AppCompatActivity implements Sendable, CompoundButton.OnCheckedChangeListener {
    NetworkProgressDialog networkProgressDialog;
    private EditText etId;
    private EditText etName;
    private EditText etAge;
    private RadioGroup rg_gender;

    private EditText etProgramID;
    private Button btnContinue;
    private static String ENCOUNTER_NAME;
    private static REQUEST_TYPE REQUEST_TYPE;
    private CheckBox cbSearchByProgramID;
    String requestType;

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b) {
            etId.setVisibility(View.GONE);
            etProgramID.setVisibility(View.VISIBLE);
        } else {
            etId.setVisibility(View.VISIBLE);
            etProgramID.setVisibility(View.GONE);
        }
    }

    public static enum REQUEST_TYPE {
        FETCH_INFO,
        FETCH_IMAGE_INFO
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info_fetcher);
        networkProgressDialog = new NetworkProgressDialog(this);
        etId = (EditText) findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        rg_gender = (RadioGroup)findViewById(R.id.rg_gender);

        etProgramID = (EditText) findViewById(R.id.etProgramId);
        cbSearchByProgramID = findViewById(R.id.cbSearchByProgramId);
        cbSearchByProgramID.setOnCheckedChangeListener(this);
        etId.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(etId, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 50);

        etId.setOnEditorActionListener(editorActionListener);
        etProgramID.setOnEditorActionListener(editorActionListener);

        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /*if(false*//*DataSender.isConnected(PatientInfoFetcher.this)*//*) { // TODO handle this
                    performClick();
                } else */{
                    String identifier = null;
                    if(etId.getVisibility() == View.VISIBLE) {
                        try {
                            identifier = etId.getText().toString();
                            OfflinePatient offlinePatient = null;
                            if(!etId.getText().toString().isEmpty()) {
                                offlinePatient = DataAccess.getInstance().getPatientByMRNumber(PatientInfoFetcher.this, identifier);
                            } else if(!etName.getText().toString().isEmpty()) {
                                offlinePatient = DataAccess.getInstance().getPatientByName(PatientInfoFetcher.this, etName.getText().toString());
                            } else {
                                // Toasty.info(PatientInfoFetcher.this, "No record found!").show();
                                String name="";
                                String gender="";
                                String id="";
                                String dob="";
                                if(!etName.getText().toString().isEmpty())
                                {
                                    name=etName.getText().toString();
                                }
//
                                int selectedID=rg_gender.getCheckedRadioButtonId();

                                RadioButton rb_gender=findViewById(selectedID);

                                if(rb_gender!=null && !rb_gender.getText().toString().isEmpty())
                                {
                                    gender=rb_gender.getText().toString();
                                }
                                if(!etId.getText().toString().isEmpty())
                                {
                                    id=etId.getText().toString();
                                }
                                offlinePatient = DataAccess.getInstance().getPatientByAllFields(PatientInfoFetcher.this, id,name,dob,gender);
                                return;
                            }

                            JSONObject serverResponse = null;

                            serverResponse = Utils.converToServerResponse(offlinePatient);
                            requestType = ParamNames.GET_PATIENT_INFO;

                            onResponseReceived(serverResponse, 0);
                        } catch (JSONException | NullPointerException e ) { // I know its bad to catch NPE -nvd
                            Toasty.normal(PatientInfoFetcher.this, "Could not fetch data", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toasty.normal(PatientInfoFetcher.this, "Offline mode can find by MR Number only", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void performClick() {
        String identifier = null;
        if(etId.getVisibility() == View.VISIBLE) {
            identifier = etId.getText().toString();
        } else {
            identifier = etProgramID.getText().toString();
        }
        try {
            JSONObject obj = new JSONObject().put(ParamNames.PATIENT_IDENTIFIER, identifier);
            requestType = REQUEST_TYPE == REQUEST_TYPE.FETCH_INFO ? ParamNames.GET_PATIENT_INFO : ParamNames.GET_PATIENT_IMAGES_INFO;
            obj.put(ParamNames.REQUEST_TYPE, requestType);
            SecretKey secKey = AES256Endec.getInstance().generateKey();
            String encPassword = AES256Endec.getInstance().encrypt(Global.PASSWORD, secKey);
            obj.put(ParamNames.USERNAME, Global.USERNAME);
//						data.put(new JSONObject().put(ParamNames.PASSWORD, Global.PASSWORD));
            obj.put(ParamNames.PASSWORD, encPassword);

            send(obj, 0);

        } catch (Exception e) {
            Toasty.error(PatientInfoFetcher.this, "Unable to send request", Toast.LENGTH_LONG).show();
            Logger.log(e);
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                performClick();
            }
            return false;
        }
    };

    @Override
    public void send(JSONObject data, int respId) {
        networkProgressDialog.show();
        new DataSender(this, this, 0).execute(data);
    }

    private JSONArray patientEncounters;

    @Override
    public void onResponseReceived(JSONObject resp, int respId) {
        networkProgressDialog.dismiss();
        try {
            if (!resp.has(ParamNames.SERVER_ERROR)) {
                if(requestType.equals(ParamNames.GET_PATIENT_INFO)) {

                    PatientData patientData = null;
                    Patient patient = JsonHelper.getInstance(this).ParsePatientFromUser(resp);
                    OfflinePatient offlinePatient = new OfflinePatient();

                    if (patient != null) {
                        patientData = new PatientData(patient);
                        JSONArray identifiers = resp.optJSONArray(ParamNames.PATIENT).getJSONObject(0).optJSONArray(ParamNames.IDENTIFIERS);
                        if(identifiers!=null)
                            for(int i=0; i<identifiers.length();i++) {
                                JSONObject id = identifiers.getJSONObject(i);
                                String identifier = id.optString(ParamNames.IDENTIFIER);
                                JSONObject idType = id.getJSONObject(ParamNames.IDENTIFIER_TYPE);
                                String identifierType = idType.getString(ParamNames.DISPLAY);
                                patientData.addIdentifier(identifierType, identifier);

                                if(identifierType.equals(ParamNames.INDUS_PROJECT_IDENTIFIER)) {
                                    offlinePatient.setMrNumber(identifier);
                                }
                            }

                    }

                    JSONObject encounters = (JSONObject) resp.getJSONObject(ParamNames.ENCOUNTERS_COUNT);

                    if(offlinePatient.getMrNumber() != null) {
                        offlinePatient.setEncounterJson(encounters.toString());
                        offlinePatient.setFieldDataJson(generateFieldsJon(resp).toString());
                        offlinePatient.setName(patient.getGivenName()+" "+patient.getFamilyName());
                        offlinePatient.setGender(patient.getGender());
                        offlinePatient.setDob(patient.getBirthDate().getTime());
                        DataAccess.getInstance().insertOfflinePatient(this, offlinePatient);
                    }
                    Global.patientData = patientData;

                   /* if (getEncounterName().equals(ParamNames.ENCOUNTER_TYPE_PATIENT_INFO)) {
                        Form.setENCOUNTER_NAME(getEncounterName());
                        startForm(patientData, null);
                    } else if (getEncounterName().equals(ParamNames.ENCOUNTER_TYPE_ADULT_SCREENING)) {
                        Form.setENCOUNTER_NAME(getEncounterName());
                        startForm(patientData, null);
                    } else {
                        Form.setENCOUNTER_NAME(getEncounterName());
                        startForm(patientData, null);
                    }
*/

                }
                finish();
            } else {
                String value;
                value = resp.getString(ParamNames.SERVER_ERROR);
                Toasty.error(this, value, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toasty.error(this, "Could not parse server response", Toast.LENGTH_LONG).show();
            Logger.log(e);
        }
    }

    private JSONObject generateFieldsJon(JSONObject resp) {
        JSONObject jsonObject = resp;
        jsonObject.remove(ParamNames.ENCOUNTERS_COUNT);
        jsonObject.remove(ParamNames.PATIENT);
        return jsonObject;
    }

    private void startForm(PatientData patientData, Bundle bundle) throws JSONException {

        if(bundle == null)
            bundle = new Bundle();

        bundle.putSerializable(ParamNames.DATA, patientData);
        Intent intent = new Intent(this, Form.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public static void init(String encounterName, REQUEST_TYPE requestType) {
        ENCOUNTER_NAME = encounterName;
        REQUEST_TYPE = requestType;
    }

    private String dateToAppFormat(String dateString) {
        String appFormatDateString = null;
        if (dateString != null) {
            Date date = null;
            try {
                date = Global.OPENMRS_TIMESTAMP_FORMAT.parse(dateString);
                appFormatDateString = Global.DATE_TIME_FORMAT.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return appFormatDateString;
    }

    public static String getEncounterName() {
        return ENCOUNTER_NAME;
    }
}
