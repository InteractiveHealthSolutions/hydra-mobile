package com.ihsinformatics.dynamicformsgenerator.screens;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.Utils;
import com.ihsinformatics.dynamicformsgenerator.common.Constants;
import com.ihsinformatics.dynamicformsgenerator.common.FormDetails;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.AutoSelect;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SExpression;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SkipLogics;
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient;
import com.ihsinformatics.dynamicformsgenerator.data.database.SaveableForm;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormType;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Image;
import com.ihsinformatics.dynamicformsgenerator.network.DataSender;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.network.Sendable;
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.DateSelector;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.ManualInput;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.NetworkProgressDialog;
import com.ihsinformatics.dynamicformsgenerator.utils.AES256Endec;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.ImageUtils;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;
import com.ihsinformatics.dynamicformsgenerator.utils.MyDialogFragment;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.DateWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.ImageWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget.InputWidgetsType;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget.MessageType;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.QRReaderWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.SpinnerWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.AgeWidget;
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.widget.Toolbar;

import java.util.*;

import javax.crypto.SecretKey;

import es.dmoral.toasty.Toasty;

public class BaseActivity extends AppCompatActivity implements Sendable, View.OnClickListener, MyDialogFragment.DialogListener {

    protected long editFormId;
    private int scrollPosition;
    // List<InputWidget> inputWidgets;
    protected Map<Integer, InputWidget> inputWidgets;
    protected Map<Integer, InputWidget> runtimeGeneratedWidgets;
    // protected JSONObject dataFromServer;
    protected PatientData patientData;
    // protected Button btnSubmit;
    protected Button btnSave;
    private ArrayList<ValidationError> errors;
    private NetworkProgressDialog networkProgressDialog;
    // private TextView tvRatings;
    private NestedScrollView svQuestions;
    private Bitmap bitmap;
    //FOR use of forms other than REGISTRATION
    private RelativeLayout llPatientInfoDisplayer;
    private TextView tvPatientName;
    private TextView tvAge;
    private TextView tvPatientLastName;
    private TextView tvPatientIdentifier;
    private ImageView ivGender;
    private int score;
    public static String PATIENT_SESSION_NUMBER;
    protected CollapsingToolbarLayout collapsingToolbar;
    protected Toolbar toolbar;

    protected List<Question> questions;
    protected List<Option> options;

    private MyDialogFragment dialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_base);
        score = 0;
        errors = new ArrayList<ValidationError>();
        networkProgressDialog = new NetworkProgressDialog(this);
        llPatientInfoDisplayer = findViewById(R.id.llPatientInfoDisplay);
        tvPatientName = findViewById(R.id.tvName);
        tvPatientLastName = findViewById(R.id.tvLastName);
        tvAge = findViewById(R.id.tvAge);
        tvPatientIdentifier = findViewById(R.id.tvId);
        ivGender = findViewById(R.id.ivGender);
        svQuestions = findViewById(R.id.svQuestions);
//        tvRatings = findViewById(R.id.tvRatings);
        inputWidgets = new LinkedHashMap<Integer, InputWidget>();
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar_to_collapse);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);


    }


    private void saveForm() {
        Iterator<Integer> iterator = inputWidgets.keySet().iterator();
        String encPassword = null;
        JSONObject savableData = new JSONObject();
        JSONObject serviceHistoryValues = new JSONObject();
        JSONArray form_values = new JSONArray();
        JSONArray data = new JSONArray();
        String patientIdentifier = "";
        List<Boolean> flags = new ArrayList<>(0);
        Map<String, JSONObject> mapOfImages = new HashMap<>(0);
        OfflinePatient offlinePatient = new OfflinePatient();
        errors.clear();
        try {
            JSONObject offlineValues = new JSONObject();
            int postOperativeCallResponse = 0;
            String firstName = "";
            while (iterator.hasNext()) {
                InputWidget i = inputWidgets.get(iterator.next());
                try {
                    if (i.getVisibility() == View.VISIBLE && i.getInputWidgetsType() == InputWidgetsType.WIDGETS_TYPE_IDENTIFIER) {
                        i.getAnswer(); // just to validate the identifier
                        if (i.getQuestion().getQuestionId() == 6000) {
                            offlinePatient.setMrNumber(i.getValue());
                        }
                    }
                    // Some fields are not needed to be sent to server
                    if (i.isSendable() && i.getVisibility() == View.VISIBLE && i.getInputWidgetsType() != InputWidgetsType.WIDGET_TYPE_HEADING && i.getInputWidgetsType() != InputWidgetsType.WIDGET_TYPE_IMAGE) {
                        data.put(i.getAnswer());

                        JSONObject formJSONObj = new JSONObject();
                        if (i.getValue() == null || (i.getValue().length() == 0)) {

                            formJSONObj.put(ParamNames.WIDGET_TYPE, i.getInputWidgetsType().toString());
                            formJSONObj.put(ParamNames.PARAM_QUESTION, i.getQuestion().getText());
                            formJSONObj.put(ParamNames.PARAM_VALUE, "-");

                        } else {

                            formJSONObj.put(ParamNames.WIDGET_TYPE, i.getInputWidgetsType().toString());
                            formJSONObj.put(ParamNames.PARAM_QUESTION, i.getQuestion().getText());
                            formJSONObj.put(ParamNames.VALUE, i.getServiceHistoryValue().toString());
                        }
                        form_values.put(formJSONObj);

                        if (i.getQuestion().getQuestionId() == 6003) {
                            offlinePatient.setGender(i.getValue());
                        } else if (i.getQuestion().getQuestionId() == 6004) {
                            offlinePatient.setDob(Global.OPENMRS_DATE_FORMAT.parse(i.getAnswer().getString(ParamNames.VALUE)).getTime());
                        } else if (i.getQuestion().getQuestionId() == 6001) {
                            firstName += i.getValue();
                        }
                    } else if (i.getVisibility() == View.VISIBLE && i.getInputWidgetsType() == InputWidgetsType.WIDGET_TYPE_IMAGE) {
                        mapOfImages.put(i.getQuestion().getParamName(), i.getAnswer());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            offlinePatient.setName(firstName);
            // TODO offlinePatient -> offlineValues
            savableData.put(ParamNames.FORM_DATA, data);
            serviceHistoryValues.put(ParamNames.SERVICE_HISTORY, form_values);

            if (errors.size() < 1) {
                // inserting user and patient related metadata
                JSONObject metaDataField = new JSONObject();
                putAuthenticationData(metaDataField);
                putMetaData(metaDataField);
                savableData.put(ParamNames.METADATA, metaDataField);
                // inserting record into the database
                DataAccess dataAccess = DataAccess.getInstance();
                int id = dataAccess.getFormTypeId(BaseActivity.this, Form.getENCOUNTER_NAME());
                if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT)) {
                    JSONObject encounterCount = new JSONObject();


                    if (offlinePatient.getMrNumber() != null) {

                        //Initialization of summary fields
                        String fieldJsonString = offlinePatient.getFieldDataJson();
                        if (fieldJsonString == null) fieldJsonString = new JSONObject().toString();
                        JSONObject existingFieldsJson = new JSONObject(fieldJsonString);

                        for (String i : ParamNames.SUMMARY_VARIBALES) {
                            existingFieldsJson.put(i, "");
                        }

                        for (String i : ParamNames.SUMMARY_VARIABLES_OBJECTS) {
                            existingFieldsJson.put(i, new JSONObject());
                        }

                        for (String i : ParamNames.SUMMARY_VARIABLES_ARRAYS) {
                            existingFieldsJson.put(i, new JSONArray());
                        }


                        offlinePatient.setFieldDataJson(existingFieldsJson.toString());


                        //Initialization of all filled encounters count via this device as 0
                        Collection<String> encounters = Constants.getEncounterTypes().values();
                        for (String i : encounters) {
                            encounterCount.put(i, 0);
                        }
                        offlinePatient.setEncounterJson(encounterCount.toString());


                        DataAccess.getInstance().insertOfflinePatient(this, offlinePatient);

                        JSONObject serverResponse = Utils.converToServerResponse(offlinePatient);
                        String requestType = ParamNames.GET_PATIENT_INFO;

                        Utils.convertPatientToPatientData(this, serverResponse, 0, requestType);

                        patientIdentifier = offlinePatient.getMrNumber();
                    } else
                        return;
                } else {
                    DataAccess access = DataAccess.getInstance();
                    OfflinePatient existineOfflinePatient = access.getPatientByMRNumber(this, patientData.getPatient().getIdentifier());
                    if (existineOfflinePatient != null) {
                        JSONObject encountersArray = new JSONObject(existineOfflinePatient.getEncounterJson());


                        if (offlinePatient.getPqId() != null)
                            existineOfflinePatient.setPqId(offlinePatient.getPqId());
                        if (offlinePatient.getScId() != null)
                            existineOfflinePatient.setScId(offlinePatient.getScId());
                        int occurrence = encountersArray.optInt(Form.getENCOUNTER_NAME());
                        occurrence++;
                        int responssedOccurrances = encountersArray.optInt("Post Operative Follow Up - Responsed");
                        responssedOccurrances += postOperativeCallResponse;
                        encountersArray.put(Form.getENCOUNTER_NAME(), responssedOccurrances);
                        encountersArray.put("Post Operative Follow Up - Responsed", occurrence);
                        existineOfflinePatient.setEncounterJson(encountersArray.toString());

                        String fieldJsonString = existineOfflinePatient.getFieldDataJson();
                        if (fieldJsonString == null) fieldJsonString = new JSONObject().toString();
                        JSONObject existingFieldsJson = new JSONObject(fieldJsonString);
                        JSONObject recentVisits = new JSONObject(existingFieldsJson.optJSONObject("recentVisits").toString());
                        JSONArray relationships = new JSONArray(existingFieldsJson.optJSONArray("relationships").toString());

                        Iterator<String> fieldsKeys = offlineValues.keys();
                        while (fieldsKeys.hasNext()) {
                            String key = fieldsKeys.next();
                            existingFieldsJson.put(key, offlineValues.opt(key));
                        }

                        existingFieldsJson.put("relationships", relationships);
                        existingFieldsJson.put("recentVisits", recentVisits);
                        existineOfflinePatient.setFieldDataJson(existingFieldsJson.toString());
                        access.insertOfflinePatient(this, existineOfflinePatient);

                        patientIdentifier = existineOfflinePatient.getMrNumber();
                    }

                }

                SaveableForm form;

                if (editFormId > 0) {
                    ToastyWidget.getInstance().displaySuccess(this, "form in edit mode", Toast.LENGTH_SHORT);
                    form = new SaveableForm(id, editFormId, savableData.toString(), Form.getENCOUNTER_NAME(), null, serviceHistoryValues.toString(), patientIdentifier);
                } else {

                    form = new SaveableForm(id, null, savableData.toString(), Form.getENCOUNTER_NAME(), null, serviceHistoryValues.toString(), patientIdentifier);
                }

                long formId = dataAccess.insertForm(BaseActivity.this, form);
                if (formId != -1) {
                    JSONObject jsonObject = new JSONObject();
                    for (String key : mapOfImages.keySet()) {
                        jsonObject = mapOfImages.get(key);
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray(key);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                byte[] imageByteArray = ImageUtils.getBytesFromEncodedString(jsonArray.getString(i));
                                JSONArray metadata = new JSONArray();
                                metadata.put(new JSONObject().put(ParamNames.USERNAME, Global.USERNAME));
                                metadata.put(new JSONObject().put(ParamNames.PASSWORD, encPassword));
                                metadata.put(new JSONObject().put(ParamNames.CAPTURE_IMAGE, key + i));
                                Image image = new Image(null, formId, null, metadata.toString(), imageByteArray, null);
                                flags.add(dataAccess.insertFormImage(BaseActivity.this, image));
                            }
                        } catch (JSONException e) {
                            Toasty.error(BaseActivity.this, e.getMessage());
                            Logger.log(e);
                        }
                    }
                    if (flags != null && flags.size() > 0) {
                        if (checkAllSaved(flags)) {
                            Toasty.success(BaseActivity.this, "Form saved successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toasty.success(BaseActivity.this, "Form is not saved successfully", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toasty.success(BaseActivity.this, "Form saved successfully", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toasty.error(BaseActivity.this, "Form is not saved successfully", Toast.LENGTH_LONG).show();
                }

                // makePostFormDecision();
                finish();

            } else {
                for (ValidationError e : errors) {
                    //TODO requestfocus
                    InputWidget i = inputWidgets.get(e.questionId);
                    i.setMessage(e.cause, MessageType.MESSAGE_TYPE_ERROR);
                }
                new AlertDialog.Builder(BaseActivity.this)
                        .setMessage("Please fill the required fields")
                        .setTitle("ERROR!")
                        .setPositiveButton("OK", null)
                        .show();
            }
        } catch (JSONException e) {
            Logger.log(e);
        } catch (Exception e) {
            Logger.log(e);
        }
    }


    private void onChildViewItemSelected(int[] showables, int[] hideables, Map<Integer, InputWidget> inputWidgets) {
        if (showables != null) {
            for (int i : showables) {
                InputWidget iw = inputWidgets.get(i);
                if (iw != null) {
                    iw.setVisibility(View.VISIBLE);
                }
            }
        }
        if (hideables != null) {
            for (int i : hideables) {
                InputWidget iw = inputWidgets.get(i);
                if (iw != null) {
                    iw.setVisibility(View.GONE);
                }
            }
        }
    }

    public void addRunTimeWidgetReference(int id, InputWidget inputWidget) {
        if (runtimeGeneratedWidgets == null) runtimeGeneratedWidgets = new HashMap<>();
        runtimeGeneratedWidgets.put(id, inputWidget);
    }

    public void removeRuntimeWidgetReference(int id) {
        runtimeGeneratedWidgets.remove(id);
    }

    public void onChildViewItemSelected(int[] showables, int[] hideables, Question question) {
        if (question == null || !question.isRuntimeGenerated())
            onChildViewItemSelected(showables, hideables, inputWidgets);
        if (!question.isRuntimeGenerated())
            onChildViewItemSelected(showables, hideables, inputWidgets);
        else onChildViewItemSelected(showables, hideables, runtimeGeneratedWidgets);
    }

    @Override
    protected void onPause() {
        scrollPosition = svQuestions.getScrollY();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO make this method more strong

        svQuestions.scrollTo(0, scrollPosition);
        InputWidget w = inputWidgets.get(requestCode);
        // w.requestFocus();
        if (resultCode == RESULT_OK) {
            if (w instanceof SpinnerWidget) {
                SpinnerWidget s = (SpinnerWidget) w;
                String dat = data.getStringExtra(ManualInput.VALUE);
                // s.setOther(dat); //TODO setOther
            } else if (w instanceof DateWidget) {
                DateWidget d = (DateWidget) w;
                try {
                    DateSelector.WIDGET_TYPE dialogType = DateSelector.WIDGET_TYPE.valueOf(data.getStringExtra(DateSelector.DATE_TYPE));
                    Calendar date = (Calendar) data.getSerializableExtra("value");
                    if (dialogType == DateSelector.WIDGET_TYPE.TIME) {
                        d.setAnswer(Global.TIME_FORMAT.format(date.getTime()), null, LANGUAGE.ENGLISH);
                    } else {
                        d.setAnswer(Global.DATE_TIME_FORMAT.format(date.getTime()), null, LANGUAGE.ENGLISH);
                    }
                } catch (NullPointerException e) {
                    Logger.log(e);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (w instanceof AgeWidget) {
                AgeWidget d = (AgeWidget) w;
                try {
                    DateSelector.WIDGET_TYPE dialogType = DateSelector.WIDGET_TYPE.valueOf(data.getStringExtra(DateSelector.DATE_TYPE));
                    Calendar date = (Calendar) data.getSerializableExtra("value");
                    if (dialogType == DateSelector.WIDGET_TYPE.TIME) {
                        d.setAnswer(Global.TIME_FORMAT.format(date.getTime()), null, LANGUAGE.ENGLISH);
                    } else {
                        d.setAnswer(Global.DATE_TIME_FORMAT.format(date.getTime()), null, LANGUAGE.ENGLISH);
                    }
                } catch (NullPointerException e) {
                    Logger.log(e);
                }
            } else if (w instanceof ImageWidget) {
                ImageWidget i = (ImageWidget) w;
                // bitmap = data.getExtras().getParcelable(ParamNames.DATA);
                /* try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), i.getImageURI());
                    i.setImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
               /* if (Build.VERSION.SDK_INT >= 23) {
                    i.checkStoragePermission(bitmap);
                } else {
                    i.setImage(bitmap);
                }*/
                if (data != null && data.getExtras() != null) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    i.setImage(imageBitmap);
                }


            }
        } else {
            if (w instanceof SpinnerWidget) {
                SpinnerWidget s = (SpinnerWidget) w;
                s.setOther(null);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addValidationError(int questionId, String cause) {
        errors.add(new ValidationError(questionId, cause));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            new AlertDialog.Builder(BaseActivity.this)
                    .setTitle(R.string.are_you_sure)
                    .setMessage(R.string.confirm_save_form)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            saveForm();
                        }
                    }).show();
        }
    }


    private class ValidationError {
        private int questionId;
        private String cause;

        public ValidationError(int questionId, String cause) {
            super();
            this.questionId = questionId;
            this.cause = cause;
        }
    }

    private boolean checkAllSaved(List<Boolean> flags) {
        for (boolean b : flags) {
            if (!b)
                return false;
        }
        return true;
    }

    @Override
    public void send(JSONObject data, int respId) {
        networkProgressDialog.show();
        new DataSender(BaseActivity.this, BaseActivity.this, 0).execute(data);
    }

    @Override
    public void onResponseReceived(JSONObject resp, int respId) {
        networkProgressDialog.dismiss();
        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setMessage(resp.get(ParamNames.SERVER_RESPONSE).toString())
                    .setTitle("Message");
            if (resp.get(ParamNames.SERVER_RESPONSE).toString().contains("success")) {
                Toast.makeText(this, resp.get(ParamNames.SERVER_RESPONSE).toString(), Toast.LENGTH_LONG).show();
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BaseActivity.this.finish();
                    }
                });
            } else {
                Toast.makeText(this, resp.get(ParamNames.SERVER_RESPONSE).toString(), Toast.LENGTH_LONG).show();
                dialog.setNegativeButton("OK", null);
            }
            dialog.show();
        } catch (JSONException e) {
            Toasty.error(this, "Could not parse server response", Toast.LENGTH_LONG).show();
            Logger.log(e);
        }
    }


    public Map<Integer, InputWidget> getInputWidgets() {
        return inputWidgets;
    }


    private void fillPatientInfoBar() {
        String identifiers = "";
        HashMap<String, String> ids = patientData.getIdentifiers();
        if (ids != null) {
            Iterator<String> it = ids.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = ids.get(key);
                identifiers += value/*+", "*/;
            }
        }
        tvPatientName.setText(patientData.getPatient().getGivenName().toUpperCase());
        tvPatientLastName.setText(patientData.getPatient().getFamilyName().toUpperCase());
        // tvAge.setText(patientData.getPatient().getAge() + ""); //TODO get dob and display full age till days
        Date birthDate = patientData.getPatient().getBirthDate();
        DateTime birthTime = new DateTime(birthDate);
        DateTime nowTime = new DateTime();


        Interval interval = new Interval(birthTime, nowTime);
        Period period = interval.toPeriod().normalizedStandard(PeriodType.yearMonthDay());
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();
        tvAge.setText(years + " years, " + months + " months, " + days + " days");
        tvPatientIdentifier.setText(identifiers);
        if (patientData.getPatient().getGender().toLowerCase().startsWith("m")) {
            ivGender.setImageDrawable(getDrawable(R.drawable.ic_user_profile));
        } else {
            ivGender.setImageDrawable(getDrawable(R.drawable.ic_user_female));
        }

    }

    // To put other necessary form data other than questions and answers
    private void putMetaData(JSONObject data) throws JSONException {
        data.put(ParamNames.REQUEST_TYPE, Form.getENCOUNTER_NAME());
        data.put(ParamNames.WORKFLOW, Global.WORKFLOWUUID);
        if (llPatientInfoDisplayer != null && llPatientInfoDisplayer.getVisibility() == View.VISIBLE) {

            JSONObject patientDataJson = new JSONObject();

            patientDataJson.put(ParamNames.UUID, patientData.getPatient().getUuid());
            patientDataJson.put(ParamNames.PERSON_GENDER, patientData.getPatient().getGender());
            patientDataJson.put(ParamNames.GIVEN_NAME, patientData.getPatient().getGivenName());
            patientDataJson.put(ParamNames.FAMILY_NAME, patientData.getPatient().getFamilyName());


            JSONArray identifiers = new JSONArray();
            JSONObject patientIdentifier = new JSONObject();
            patientIdentifier.put(ParamNames.TYPE, ParamNames.MR_NUMBER);
            patientIdentifier.put(ParamNames.VALUE, patientData.getPatient().getIdentifier());
            identifiers.put(patientIdentifier);


            patientDataJson.put(ParamNames.IDENTIFIERS, identifiers);
            data.put(ParamNames.PATIENT, patientDataJson);

            JSONObject formDetails = new JSONObject();
            FormDetails form = Constants.getFormDetails().get(getFormId(Form.getENCOUNTER_NAME()));
            formDetails.put(ParamNames.COMPONENT_FORM_ID, form.getComponentFormID());
            formDetails.put(ParamNames.COMPONENT_FORM_UUID, form.getComponentFormUUID());


            data.put(ParamNames.FORM_DETAILS, formDetails);
        }
    }

    private void putAuthenticationData(JSONObject data) throws Exception {
        // SecretKey secKey = AES256Endec.getInstance().generateKey();
        // String encPassword = AES256Endec.getInstance().encrypt(Global.PASSWORD != null ? Global.PASSWORD : "", secKey);
        JSONObject authenticationJson = new JSONObject();
        authenticationJson.put(ParamNames.USERNAME, Global.USERNAME);
        authenticationJson.put(ParamNames.PASSWORD, Global.PASSWORD);
        authenticationJson.put(ParamNames.PROVIDER, Global.PROVIDER);
        data.put(ParamNames.AUTHENTICATION, authenticationJson);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        InputWidget w = inputWidgets.get(requestCode);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (w instanceof ImageWidget) {
                ImageWidget i = (ImageWidget) w;
                if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    i.startCamera();
                } else if (permissions[0].equals(Manifest.permission.CAMERA)) {
                    i.callCamera();
                }
            } else if (w instanceof QRReaderWidget) {
                QRReaderWidget q = (QRReaderWidget) w;
                if (permissions[0].equals(Manifest.permission.CAMERA)) {
                    q.showQRCodeReaderDialog();
                }
            }
        } else {
            Toasty.info(this, "Permission denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setMessage("Are you sure to exit Form?")
                .setTitle("Are you sure?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BaseActivity.super.onBackPressed();
                    }
                });
        dialog.show();
    }

    public void addInRating(int i) {
        score += i;
        // ((HiddenFieldWidget)inputWidgets.get(65)).setAnswer(score+"", LANGUAGE.ENGLISH);
        /*if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_MEDICAL_QUESTIONAIRE)) {
            // tvRatings.setText("Score: "+(score));
            tvRatings.setVisibility(View.GONE);
            ScoreSpinner sw = ((ScoreSpinner) inputWidgets.get(65));
            if (sw != null) {
                sw.setselectedIndex(score);
            }
        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SCREENING)) {
            // tvRatings.setText("Score: "+score);
            tvRatings.setVisibility(View.GONE);
            SpinnerWidget ew = ((SpinnerWidget) inputWidgets.get(126));
            if (ew != null) {
                ew.setAnswer(score + "", "", LANGUAGE.ENGLISH);
            }
        }*/
    }

    @Override
    protected void onStop() {
        // Calling destroy function of all input widgets
        Set<Integer> keys = inputWidgets.keySet();
        super.onStop();
        for (Integer i : keys) {
            inputWidgets.get(i).destroy();
        }
    }


//    private void makePostFormDecision() throws JSONException {
//
//        // Intent i = getIntent();
//        //PatientData patientData = (PatientData) i.getSerializableExtra(ParamNames.DATA);
//
//
//        if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_CHILD_SCREENING)) {
//            final SpinnerWidget tbPreemptive = (SpinnerWidget) inputWidgets.get(31037);
//            String ans = tbPreemptive.getValue();
//
//            if (ans.equals("Yes") || ans.equals("yes")) {
//                // finish();
//                Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_CHILD_CLINICAL_EVALUATION);
//                startForm(patientData, null);
//            } else {
//                finish();
//                Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_EOF);
//                startForm(patientData, null);
//
//            }
//        } else if ((Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_CHILD_CLINICAL_EVALUATION))) {
//
//            final SpinnerWidget tbPreemptive = (SpinnerWidget) inputWidgets.get(41025);
//            String ans = tbPreemptive.getValue();
//
//            if (ans.equals("TB Presumptive confirmed")) {
//                //Popup
//                // finish();
//                ShowPopup("TB Diagnosed", "TB_diagnosed");
//                return;
//
//            } else {
//                finish();
//                Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_EOF);
//                startForm(patientData, null);
//
//            }
//        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_CHILD_TX_INITIATION)) {
//            // finish();
//            ShowPopup("Want to fill Contact Registry Form", "ContactRegistry");
//            return;
//        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_ADULT_SCREENING)) {
//            final SpinnerWidget tbPreemptive = (SpinnerWidget) inputWidgets.get(32037);
//            String ans = tbPreemptive.getValue();
//
//            if (ans.equals("Yes") || ans.equals("yes")) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//                alertDialogBuilder.setMessage("Please Collect Sputum Sample");
//                alertDialogBuilder.setTitle("Sputum Collection");
//                alertDialogBuilder.setCancelable(false);
//                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                        Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_ADULT_CLINICAL_EVALUATION);
//                        try {
//                            startForm(patientData, null);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                alertDialogBuilder.show();
//                return;
//
//            } else {
//                finish();
//                Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_EOF);
//                startForm(patientData, null);
//
//            }
//        } else if ((Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_ADULT_CLINICAL_EVALUATION))) {
//
//            final SpinnerWidget tbPreemptive = (SpinnerWidget) inputWidgets.get(42025);
//            String ans = tbPreemptive.getValue();
//
//            if (ans.equals("TB Presumptive confirmed")) {
//                //Popup
//                //finish();
//                ShowPopup("TB Diagnosed", "TB_diagnosed_adult");
//                return;
//
//            } else {
//                finish();
//                Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_EOF);
//                startForm(patientData, null);
//
//            }
//        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_ADULT_TX_INITIATION)) {
//            //finish();
//            ShowPopup("Want to fill Contact Registry Form", "ContactRegistry");
//            return;
//        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_CONTACT_REGISTRY)) {
//            finish();
//            Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_EOF);
//            startForm(patientData, null);
//        }
//        finish();
//        finish();
//
//
//    }


    private void startForm(PatientData patientData, Bundle bundle) throws JSONException {

        if (bundle == null)
            bundle = new Bundle();

        bundle.putSerializable(ParamNames.DATA, patientData);
        Intent intent = new Intent(this, Form.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void ShowPopup(String question, String type) {

        dialogFragment = new MyDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString("question", question);

        bundle.putString("type", type);

        dialogFragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        dialogFragment.show(ft, "dialog");

    }


    @Override
    public void onFinishEditDialog(String inputText, String type) throws JSONException {
        if (TextUtils.isEmpty(inputText)) {
            Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();
        } else {
            if (type.equals("TB_diagnosed")) {
                if (inputText.equals("Yes") || inputText.equals("yes")) {
                    //finish();
                    Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_CHILD_TX_INITIATION);
                    startForm(patientData, null);

                } else {
                    // finish();
                    Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_EOF);
                    startForm(patientData, null);

                }
            } else if (type.equals("TB_diagnosed_adult")) {
                if (inputText.equals("Yes") || inputText.equals("yes")) {
                    // finish();
                    Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_ADULT_TX_INITIATION);
                    startForm(patientData, null);

                } else {
                    //finish();
                    Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_EOF);
                    startForm(patientData, null);

                }
            } else if (type.equals("ContactRegistry")) {
                if (inputText.equals("Yes") || inputText.equals("yes")) {
                    // finish();
                    Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_CONTACT_REGISTRY);
                    startForm(patientData, null);

                } else {
                    // finish();
                    Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_EOF);
                    startForm(patientData, null);

                }
            }
        }
        finish();
    }


    public void checkSkipLogics(int formID) throws JSONException {
//ToDo Provide proper Form id ~Taha
        //TODO Questions are skipped based on specific formID. So the scenario will fail if we want to skip a question based on another form skiplogic ~Taha
        List<Question> questionList = getQuestions(formID);

        for (final Question changeableQuestion : questionList) {
            final InputWidget changeable = inputWidgets.get(changeableQuestion.getQuestionId());

            List<SExpression> showables = changeableQuestion.getVisibleWhen();
            List<SExpression> hiddenable = changeableQuestion.getHiddenWhen();

            List<AutoSelect> autoSelectables = changeableQuestion.getAutoSelect();

            if (changeable != null) {
                changeable.setVisibility(changeableQuestion.getInitialVisibility());

                if (showables != null && showables.size() > 0) {

                    for (SExpression sExp : showables) {

                        Boolean final_visibility = logicChecker(sExp);
                        if (null == final_visibility) {
                            changeable.setVisibility(changeableQuestion.getInitialVisibility());
                        } else if (final_visibility == true) {
                            changeable.setVisibility(View.VISIBLE);
                        } else {
                            changeable.setVisibility(View.GONE);
                        }
                    }
                }

                if (hiddenable != null && hiddenable.size() > 0) {

                    for (SExpression sExp : hiddenable) {


                        Boolean final_visibility = logicChecker(sExp);
                        if (null == final_visibility) {
                            changeable.setVisibility(changeableQuestion.getInitialVisibility());
                        } else if (final_visibility == true) {
                            changeable.setVisibility(View.GONE);
                        } else {
                            changeable.setVisibility(View.VISIBLE);
                        }
                    }

                }

                if (autoSelectables != null && autoSelectables.size() > 0) {


                    for (AutoSelect as : autoSelectables) {


                        for (SExpression sExp : as.getAutoSelectWhen()) {

                            Boolean final_selection = logicChecker(sExp);

                            // We got 3 major cases and then sub-cases in those cases:

                            // CASE 1:  This case shows either skipLogic is invalid or the question (say X) whose answer auto-selects other question (let say Z), is hidden (X is hidden where X is not a parent question of Z)
                            if (null == final_selection) {

                            }

                            //CASE 2:
                            else if (final_selection == true) {

                                // There exists a case when value is already autopopulated value in question Z but it is not disabled
                                if (changeable.isEnabled() && changeable.getValue().equals(as.getTargetFieldAnswer())) {
                                    changeable.setEnabled(false);
                                }

                                //This case shows question X should autoSelect question Z because final selection is true.
                                // Moreover, checking value of changeable(other condition) ensures that the skiplogic doesnot run again and again in loop
                                // because everytime value is changed and it will get stuck in infinite loop because of value change.
                                // So here we are ensuring that value is not already set then run this condition
                                else if (!changeable.getValue().equals(as.getTargetFieldAnswer())) {
                                    changeable.setEnabled(false);
                                    changeable.setAnswer(as.getTargetFieldAnswer(), "", LANGUAGE.ENGLISH);
                                }


                            }


                            //CASE 3:
                            //Case for removing auto-selected text in case of false condition
                            else if (final_selection == false && changeable.getValue().equals(as.getTargetFieldAnswer())) {
                                //For editText we need to fill answer by empty string.
                                if (changeable.getInputWidgetsType().equals(InputWidgetsType.WIDGET_TYPE_EDITTEXT)) {
                                    changeable.setEnabled(true);
                                    changeable.setAnswer("", "", LANGUAGE.ENGLISH);
                                }
                                // For rest of the widgets answer remains same
                                else {
                                    changeable.setEnabled(true);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    //TODO options ka name dekh raha hai rather uuid ~Taha
    protected Boolean logicChecker(SExpression sExp) throws JSONException {
        if (sExp != null) {
            Boolean final_visibility = false;
            Boolean loopFirstIteration = true;
            if (sExp.getOperator().equals("OR")) {
                final_visibility = false;

                if (null != sExp.getSkipLogicsArray()) {
                    for (SExpression nestedSExp : sExp.getSkipLogicsArray()) {
                        final_visibility = logicChecker(nestedSExp);
                    }
                }
                if (final_visibility != null && final_visibility)  // If any case true then return true in OR case... DOnot change final_visibility to true initially in first line
                    return true;

                for (SkipLogics changerQuestion : sExp.getSkipLogicsObjects()) {
                    InputWidget changer = inputWidgets.get(changerQuestion.getQuestionID());

                    if (null != changer && changer.getVisibility() == View.VISIBLE && changer.getInputWidgetsType().equals(InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER)) {

                        if (null != changer.getOptions()) {

                            if (listComparator(changerQuestion.getEqualsList(), changer.getValue())) {
                                final_visibility = true;
                                loopFirstIteration = false;
                            }
                            if (final_visibility != null && !final_visibility && listComparator(changerQuestion.getNotEqualsList(), changer.getValue())) {
                                final_visibility = false;
                                loopFirstIteration = false;
                            } else if (changerQuestion.getNotEqualsList().size() != 0 && !listComparator(changerQuestion.getNotEqualsList(), changer.getValue())) {
                                final_visibility = true;
                                loopFirstIteration = false;
                            }
                        }

                    } else if (null != changer && changer.getVisibility() == View.VISIBLE) {
                        if (null != changer.getOptions()) {

                            if (changerQuestion.getEqualsList().contains(changer.getValue())) {
                                final_visibility = true;
                                loopFirstIteration = false;
                            }
                            if (final_visibility != null && !final_visibility && changerQuestion.getNotEqualsList().contains(changer.getValue())) {
                                final_visibility = false;
                                loopFirstIteration = false;
                            } else if (changerQuestion.getNotEqualsList().size() != 0 && !changerQuestion.getNotEqualsList().contains(changer.getValue())) {
                                final_visibility = true;
                                loopFirstIteration = false;
                            }
                        }
                    } else if (loopFirstIteration) {
                        final_visibility = null;
                    }
                }

            } else if (sExp.getOperator().equals("AND")) {
                final_visibility = true;
                if (null != sExp.getSkipLogicsArray()) {
                    for (SExpression nestedSExp : sExp.getSkipLogicsArray()) {
                        final_visibility = logicChecker(nestedSExp);
                    }
                }
                if (final_visibility != null && final_visibility) {
                    for (SkipLogics changerQuestion : sExp.getSkipLogicsObjects()) {
                        InputWidget changer = inputWidgets.get(changerQuestion.getQuestionID());
                        if (null != changer && changer.getVisibility() == View.VISIBLE && changer.getInputWidgetsType().equals(InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER)) {

                            if (null != changer.getOptions()) {

                                if (changerQuestion.getEqualsList().size() == 0 || listComparatorForANDCase(changerQuestion.getEqualsList(), changer.getValue())) {
                                    final_visibility = true;
                                    loopFirstIteration = false;
                                } else {
                                    final_visibility = false;
                                    loopFirstIteration = false;
                                    break;

                                }


                                if (changerQuestion.getNotEqualsList().size() != 0 && listComparatorForANDCase(changerQuestion.getNotEqualsList(), changer.getValue())) {

                                    final_visibility = false;
                                    loopFirstIteration = false;
                                    break;
                                } else {
                                    final_visibility = true;
                                    loopFirstIteration = false;
                                }

                            }


                        } else if (null != changer && changer.getVisibility() == View.VISIBLE) {
                            if (null != changer.getOptions()) {

                                if (changerQuestion.getEqualsList().size() == 0 || changerQuestion.getEqualsList().contains(changer.getValue())) {
                                    final_visibility = true;
                                    loopFirstIteration = false;
                                } else {
                                    final_visibility = false;
                                    loopFirstIteration = false;
                                    break;

                                }
                                if (changerQuestion.getNotEqualsList().contains(changer.getValue())) {

                                    final_visibility = false;
                                    loopFirstIteration = false;
                                    break;
                                } else {
                                    final_visibility = true;
                                    loopFirstIteration = false;
                                }
                            }
                        } else {
                            final_visibility = null;
                            break;
                        }
                    }
                }
            }
            return final_visibility;
        }
        return true;
    }


    //Returns true if first list contains requiredValue from list2 where valuesList contains all checked members of multiselect
    private boolean listComparator(List<String> comparingList, String valuesJSONObj) throws JSONException {
        JSONObject multiChoicesObj = new JSONObject(valuesJSONObj);
        JSONArray selectedValues = multiChoicesObj.optJSONArray(ParamNames.VALUE);

        if (null != comparingList && null != selectedValues) {
            for (int i = 0; i < comparingList.size(); i++) {
                for (int j = 0; j < selectedValues.length(); j++) {
                    if (comparingList.get(i).equals(selectedValues.opt(j))) {
                        return true;
                    }
                }
            }

        }

        return false;
    }


    // this method will return false as soon as it finds that equals or not equals list does not contain certain value
    private boolean listComparatorForANDCase(List<String> comparingList, String valuesJSONObj) throws JSONException {
        JSONObject multiChoicesObj = new JSONObject(valuesJSONObj);
        JSONArray selectedValues = multiChoicesObj.optJSONArray(ParamNames.VALUE);

        if ((null == comparingList || comparingList.size() == 0) && (null != selectedValues && selectedValues.length() > 0)) {
            return true;
        } else if ((null != comparingList && comparingList.size() > 0) && (null == selectedValues || selectedValues.length() == 0)) {
            return false;
        } else if (null != comparingList && null != selectedValues) {

            ArrayList<String> selectedValuesData = new ArrayList<String>();

            for (int i = 0; i < selectedValues.length(); i++) {
                selectedValuesData.add(selectedValues.opt(i).toString());
            }

            // checks if every element of selected value must match every value of comparingList
            //comparing kai ander selected kai saray hone chahiye

            for (int j = 0; j < comparingList.size(); j++) {
                if (!selectedValuesData.contains(comparingList.get(j))) {
                    return false;
                }

            }

        }

        return true;
    }

    public List<Question> getQuestions(int formId) {
        ArrayList localArrayList = new ArrayList();
        Enumeration<Question> localIterator = Collections.enumeration(this.questions);
        for (; ; ) {
            if (!localIterator.hasMoreElements()) {
                return localArrayList;
            }
            Question localQuestion = (Question) localIterator.nextElement();
            if (localQuestion.getFormTypeId() == formId) {
                localArrayList.add(localQuestion);
            }
        }
    }

    // TODO handle some form type specific tasks
    protected void handleEncounterType() {


        if (Global.patientData == null && !Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT)) {
            // Toasty.warning(this, getResources().getString(R.string.patient_not_loaded), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (DataProvider.directOpenableForms.contains(Form.getENCOUNTER_NAME())) {
            // tvRatings.setVisibility(View.GONE);
            llPatientInfoDisplayer.setVisibility(View.GONE);
        } else {
            fillPatientInfoBar();
        }

    }


    public String getOptionUUIDByQuestionsID(List<Option> option, String selectedValue, int questionId) {

        for (int i = 0; i < option.size(); i++) {
            if (option.get(i).getText().equals(selectedValue) && option.get(i).getQuestionId() == questionId) {
                return option.get(i).getUuid();
            }
        }
        return "";
    }


    public int getFormId(String paramString) {
        int index;
        FormType formType;
        Iterator<Integer> it = Constants.getEncounterTypes().keySet().iterator();
        while (it.hasNext()) {
            index = it.next();
            if (Constants.getEncounterTypes().get(index).equals(paramString)) {
                return index;
            }
        }
        return -1;
    }

    private void setLocale() {
        Resources resources = getResources();
        android.content.res.Configuration configuration = resources.getConfiguration();

        if (Global.APP_LANGUAGE == null) {
            Global.APP_LANGUAGE = "en";
        }

        configuration.setLocale(new Locale(Global.APP_LANGUAGE.toLowerCase()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, getResources().getDisplayMetrics());
        }

    }

}
