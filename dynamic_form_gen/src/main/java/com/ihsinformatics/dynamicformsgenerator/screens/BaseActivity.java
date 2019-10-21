package com.ihsinformatics.dynamicformsgenerator.screens;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
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
import com.ihsinformatics.dynamicformsgenerator.utils.AppUtility;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.ImageUtils;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.AutoCompleteTextViewWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.DateWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.EditTextWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.IdentifierWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.ImageWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget.InputWidgetsType;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget.MessageType;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.QRReaderWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.RadioButtonWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.ScoreSpinner;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.SpinnerWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.AgeWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.listeners.OnValueChangeListener;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.crypto.SecretKey;

import es.dmoral.toasty.Toasty;

public class BaseActivity extends AppCompatActivity implements Sendable, View.OnClickListener {

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
    private TextView tvRatings;
    private ScrollView svQuestions;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_base);
        score = 0;
        errors = new ArrayList<ValidationError>();
        networkProgressDialog = new NetworkProgressDialog(this);
        llPatientInfoDisplayer =  findViewById(R.id.llPatientInfoDisplay);
        tvPatientName =  findViewById(R.id.tvName);
        tvPatientLastName =  findViewById(R.id.tvLastName);
        tvAge =  findViewById(R.id.tvAge);
        tvPatientIdentifier =  findViewById(R.id.tvId);
        ivGender =  findViewById(R.id.ivGender);
        svQuestions =  findViewById(R.id.svQuestions);
        tvRatings =  findViewById(R.id.tvRatings);
        inputWidgets = new LinkedHashMap<Integer, InputWidget>();
        btnSave =  findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    private void saveForm() {
        Iterator<Integer> iterator = inputWidgets.keySet().iterator();
        String encPassword = null;
        JSONObject savableData = new JSONObject();
        JSONArray data = new JSONArray();
        List<Boolean> flags = new ArrayList<>(0);
        Map<String, JSONObject> mapOfImages = new HashMap<>(0);
        OfflinePatient offlinePatient = new OfflinePatient();
        errors.clear();
        try {
            JSONObject offlineValues = new JSONObject();
            int postOperativeCallResponse = 0;
            while (iterator.hasNext()) {
                InputWidget i = inputWidgets.get(iterator.next());
                try {
                    if (i.getVisibility() == View.VISIBLE && i.getInputWidgetsType() == InputWidgetsType.WIDGETS_TYPE_IDENTIFIER) {
                        i.getAnswer(); // just to validate the identifier
                        if(i.getQuestion().getQuestionId() == 6000) {
                            offlinePatient.setMrNumber(i.getValue());
                        } else if(i.getQuestion().getQuestionId() == 11000 || i.getQuestion().getQuestionId() == 11061 || i.getQuestion().getQuestionId() == 11062) {
                            offlinePatient.setPqId(i.getValue());
                        } else if(i.getQuestion().getQuestionId() == 14100) {
                            offlinePatient.setScId(i.getValue());
                        }
                    }
                    // Some fields are not needed to be sent to server
                    if (i.isSendable() && i.getVisibility() == View.VISIBLE && i.getInputWidgetsType() != InputWidgetsType.WIDGET_TYPE_HEADING && i.getInputWidgetsType() != InputWidgetsType.WIDGET_TYPE_IMAGE) {
                        data.put(i.getAnswer());
                        if(i.getQuestion().getQuestionId() == 6003) {
                            offlinePatient.setGender(i.getAnswer().getString(ParamNames.SEX));
                        } else if(i.getQuestion().getQuestionId() == 6004) {
                            offlinePatient.setDob(Global.OPENMRS_DATE_FORMAT.parse(i.getAnswer().getString(ParamNames.DOB)).getTime());
                        }
                    } else if (i.getVisibility() == View.VISIBLE && i.getInputWidgetsType() == InputWidgetsType.WIDGET_TYPE_IMAGE) {
                        mapOfImages.put(i.getQuestion().getParamName(), i.getAnswer());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // TODO offlinePatient -> offlineValues
            savableData.put(ParamNames.FORM_DATA, data);

            if (errors.size() < 1) {
                // inserting user and patient related metadata
                putAuthenticationData(savableData);
                putMetaData(savableData);
                // inserting record into the database
                DataAccess dataAccess = DataAccess.getInstance();
                int id = dataAccess.getFormTypeId(BaseActivity.this, Form.getENCOUNTER_NAME());
                if(Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT)) {
                    JSONObject encounterCount = new JSONObject();
                    if(offlinePatient.getMrNumber()!=null) {
                        for(int i=0; i<ParamNames.ENCOUNTER_TYPES.length; i++) {
                            encounterCount.put(ParamNames.ENCOUNTER_TYPES[i], 0);
                        }
                        offlinePatient.setEncounterJson(encounterCount.toString());
                        DataAccess.getInstance().insertOfflinePatient(this, offlinePatient);
                    } else
                        return;
                } else {
                    DataAccess access = DataAccess.getInstance();
                    OfflinePatient existineOfflinePatient = access.getPatientByMRNumber(this, patientData.getPatient().getIdentifier());
                    if(existineOfflinePatient!=null) {
                        JSONObject encountersArray = new JSONObject(existineOfflinePatient.getEncounterJson());
                        if(offlinePatient.getPqId()!=null)existineOfflinePatient.setPqId(offlinePatient.getPqId());
                        if(offlinePatient.getScId()!=null)existineOfflinePatient.setScId(offlinePatient.getScId());
                        int occurrence = encountersArray.optInt(Form.getENCOUNTER_NAME());
                        occurrence++;
                        int responssedOccurrances = encountersArray.optInt("Post Operative Follow Up - Responsed");
                        responssedOccurrances+=postOperativeCallResponse;
                        encountersArray.put(Form.getENCOUNTER_NAME(), responssedOccurrances);
                        encountersArray.put("Post Operative Follow Up - Responsed", occurrence);
                        existineOfflinePatient.setEncounterJson(encountersArray.toString());

                        String fieldJsonString = existineOfflinePatient.getFieldDataJson();
                        if(fieldJsonString == null) fieldJsonString = new JSONObject().toString();
                        JSONObject existingFieldsJson = new JSONObject(fieldJsonString);

                        Iterator<String> fieldsKeys = offlineValues.keys();
                        while (fieldsKeys.hasNext()) {
                            String key = fieldsKeys.next();
                            existingFieldsJson.put(key, offlineValues.opt(key));
                        }
                        existineOfflinePatient.setFieldDataJson(existingFieldsJson.toString());
                        access.insertOfflinePatient(this, existineOfflinePatient);
                    }

                }
                SaveableForm form = new SaveableForm(id, null, savableData.toString(), null);
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
        if(question == null || !question.isRuntimeGenerated()) onChildViewItemSelected(showables, hideables, inputWidgets);
        if (!question.isRuntimeGenerated()) onChildViewItemSelected(showables, hideables, inputWidgets);
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

    // TODO handle some form type specific tasks
    protected void handleEncounterType() {

        if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_CHILD_CLINICAL_EVALUATION))
        {
            final EditTextWidget weightWidget = (EditTextWidget) inputWidgets.get(41007);
            final EditTextWidget heightWidget = (EditTextWidget) inputWidgets.get(41008);
            final EditTextWidget bmiWidget = (EditTextWidget) inputWidgets.get(41009);
            bmiWidget.setEnabled(false);

            OnValueChangeListener valueChangeListener = new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    if(newValue.isEmpty() || weightWidget.getValue().isEmpty() || heightWidget.getValue().isEmpty()) return;

                    float weight = Float.valueOf(weightWidget.getValue());
                    float height  = Float.valueOf(heightWidget.getValue())/100;
                    height = height*height;

                    if(height<=0 || weight <=0) return;

                    float bmi = weight/height;
                    bmiWidget.setAnswer(bmi+"", "", LANGUAGE.ENGLISH);
                }
            };
            heightWidget.setOnValueChangeListener(valueChangeListener);
            weightWidget.setOnValueChangeListener(valueChangeListener);

        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_ADULT_CLINICAL_EVALUATION)) {
            final EditTextWidget weightWidget = (EditTextWidget) inputWidgets.get(42007);
            final EditTextWidget heightWidget = (EditTextWidget) inputWidgets.get(42008);
            final EditTextWidget bmiWidget = (EditTextWidget) inputWidgets.get(42009);
            bmiWidget.setEnabled(false);

            OnValueChangeListener valueChangeListener = new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    if(newValue.isEmpty() || weightWidget.getValue().isEmpty() || heightWidget.getValue().isEmpty()) return;

                    int weight = Integer.valueOf(weightWidget.getValue());
                    int height  = Integer.valueOf(heightWidget.getValue())/100;
                    height = height*height;
                    if(height<=0 || weight <=0) return;

                    long bmi = weight/height;
                    bmiWidget.setAnswer(bmi+"", "", LANGUAGE.ENGLISH);
                }
            };
            heightWidget.setOnValueChangeListener(valueChangeListener);
            weightWidget.setOnValueChangeListener(valueChangeListener);
        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_PATIENT_INFO)) {

        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_CONTACT_REGISTRY)) {

            final EditTextWidget adult_males = (EditTextWidget) inputWidgets.get(51006);
            final EditTextWidget adult_females = (EditTextWidget) inputWidgets.get(51007);
            final EditTextWidget total_adults = (EditTextWidget) inputWidgets.get(51008);
            total_adults.setEnabled(false);

            OnValueChangeListener valueChangeListener = new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    if(newValue.isEmpty() || adult_males.getValue().isEmpty() || adult_females.getValue().isEmpty()) return;

                    int adult_ma = Integer.valueOf(adult_males.getValue());
                    int adult_fe  = Integer.valueOf(adult_females.getValue());


                    if(adult_ma<0 || adult_fe <0)
                    {
                        int tot = adult_ma + adult_fe;
                        total_adults.setAnswer(tot + "", "", LANGUAGE.ENGLISH);
                    }
                   //else if()
                }
            };
            adult_females.setOnValueChangeListener(valueChangeListener);
            adult_males.setOnValueChangeListener(valueChangeListener);


        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_PATIENT_INFO)) {

        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_PATIENT_INFO)) {

        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_PATIENT_INFO)) {

        }
    }

    public Map<Integer, InputWidget> getInputWidgets() {
        return inputWidgets;
    }

    private void fillPatientInfoBar() {
        String identifiers = "";
        HashMap<String, String> ids = patientData.getIdentifiers();
        if(ids != null) {
            Iterator<String> it = ids.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = ids.get(key);
                identifiers+=value+", ";
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
        tvAge.setText(years+" years, "+months+" months, "+days+" days");
        tvPatientIdentifier.setText(identifiers);
        if (patientData.getPatient().getGender().toLowerCase().startsWith("m")) {
            ivGender.setImageDrawable(getDrawable(R.drawable.male_icon));
        } else {
            ivGender.setImageDrawable(getDrawable(R.drawable.female_icon));
        }
    }

    // To put other necessary form data other than questions and answers
    private void putMetaData(JSONObject data) throws JSONException {
        data.put(ParamNames.REQUEST_TYPE, Form.getENCOUNTER_NAME());
        if (llPatientInfoDisplayer.getVisibility() == View.VISIBLE) {

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
        }
    }

    private void putAuthenticationData(JSONObject data) throws Exception {
        SecretKey secKey = AES256Endec.getInstance().generateKey();
        String encPassword = AES256Endec.getInstance().encrypt(Global.PASSWORD!=null?Global.PASSWORD:"", secKey);
        JSONObject authenticationJson = new JSONObject();
        authenticationJson.put(ParamNames.USERNAME, Global.USERNAME);
        authenticationJson.put(ParamNames.PASSWORD, encPassword);
        String providerUUID = resolveProviderUUID();
        authenticationJson.put(ParamNames.PROVIDER_UUID, providerUUID);
        data.put(ParamNames.AUTHENTICATION, authenticationJson);
    }


    // TODO implement to return provider uuid
    private String resolveProviderUUID() throws JSONException {
        String providerUUID;



        return "";
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
}
