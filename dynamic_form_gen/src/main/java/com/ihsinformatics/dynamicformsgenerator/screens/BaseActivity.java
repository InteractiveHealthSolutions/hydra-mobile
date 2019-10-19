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
                    // Some fields might not need to send to server
                    if (i.isSendable() && i.getVisibility() == View.VISIBLE && i.getInputWidgetsType() != InputWidgetsType.WIDGET_TYPE_HEADING && i.getInputWidgetsType() != InputWidgetsType.WIDGET_TYPE_IMAGE) {

                        data.put(i.getAnswer());

                        /*    Project specific code starts here   */
                        if(i.getQuestion().getQuestionId() == 6003) {
                            offlinePatient.setGender(i.getAnswer().getString(ParamNames.SEX));
                        } else if(i.getQuestion().getQuestionId() == 6004) {
                            offlinePatient.setDob(Global.OPENMRS_DATE_FORMAT.parse(i.getAnswer().getString(ParamNames.DOB)).getTime());
                        }
                        if(Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_DAY_1)
                                || Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_DAY_7)
                                || Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_DAY_10)) {
                            if (i.getQuestion().getParamName().equals(ParamNames.ADVICE_DAY_1)
                                    || i.getQuestion().getParamName().equals(ParamNames.ADVICE_DAY_7)
                                    || i.getQuestion().getParamName().equals(ParamNames.ADVICE_DAY_10)
                                    || i.getQuestion().getParamName().equals(ParamNames.OTHER_DESC_1)
                                    || i.getQuestion().getParamName().equals(ParamNames.OTHER_DESC_7)
                                    || i.getQuestion().getParamName().equals(ParamNames.OTHER_DESC_10)
                                    || i.getQuestion().getParamName().equals(ParamNames.HAS_THE_RING_FALLEN)
                                    || i.getQuestion().getParamName().equals(ParamNames.FORM3_OVERALL_PARENT_SATISFIED)
                                    || i.getQuestion().getParamName().equals(ParamNames.GUARDIAN_SATISFACTION)) {

                                offlineValues.put(i.getQuestion().getParamName(), i.getValue());

                            }
                        }
                        if(Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SAFE_CIRCUMCISION_FORM_2)) {
                            if (i.getQuestion().getParamName().equals(ParamNames.FORM2_STEPS_TAKEN)) {
                                offlineValues.put(i.getQuestion().getParamName(), i.getValue()); // TODO array
                            }
                        }
                        if(Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SAFE_CIRCUMCISION_FORM_1)) {
                            if (i.getQuestion().getParamName().equals(ParamNames.PROCEDURE_DATE) || i.getQuestion().getParamName().equals(ParamNames.FORM_DATE)) {
                                offlineValues.put(ParamNames.CIRCUMCISION_DATE, i.getValue()); // TODO array
                            }
                        }
                        if(Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_DEMOGRAPHIC_INFORMATION)) {
                            if (i.getQuestion().getParamName().equals(ParamNames.DEFORMED_SIDE)) {
                                offlineValues.put("effected_foot", i.getValue()); // TODO array
                            }
                        }
                        if(Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_POST_OP_FOLLOW_UP)) {
                            if (i.getQuestion().getParamName().equals(ParamNames.POST_OP_FUP_INTERVENTION_ARM)) {
                                offlineValues.put("intervention_arm", i.getValue()); // TODO array
                            } else if (i.getQuestion().getParamName().equals(ParamNames.POST_OP_FUP_CONTACT_ESTABLISHED)) {
                                if(i.getValue().contains("Yes")) {
                                    postOperativeCallResponse ++;
                                }

                                // offlineValues.put("followupStatus", i.getValue()); // TODO array
                            } else if (i.getQuestion().getParamName().equals(ParamNames.POST_OP_FUP_FOLLOWUP_CALL_DATE)) {
                                offlineValues.put("dateOfFollowUp", i.getValue()); // TODO array
                            }
                        }
                        if(Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_POST_OP_DEMOGRAPHICS)) {
                            if (i.getQuestion().getParamName().equals(ParamNames.POST_OP_DATE_SURGERY)) {
                                offlineValues.put("dateOfProcedure", i.getValue()); // TODO array
                            } else if (i.getQuestion().getParamName().equals(ParamNames.POST_OP_NAME_PROCEDURE)) {
                                offlineValues.put("nameOfProcedure", i.getValue()); // TODO array
                            }
                        }
                        if(Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SURGICAL_SITE_EVALUATION)) {
                            if (i.getQuestion().getParamName().equals(ParamNames.SURGEON_EVAL_DATE_ASSESSMENT)) {
                                offlineValues.put("dateOfDiagnosis", i.getValue()); // TODO array
                            } else if (i.getQuestion().getParamName().equals(ParamNames.SURGEON_EVAL_FOLLOWUP_STATUS)) {
                                offlineValues.put("followupStatus", i.getValue()); // TODO array
                            } else if (i.getQuestion().getParamName().equals(ParamNames.SURGEON_EVAL_REASON_OF_URGENT_APPOINTMENT)) {
                                offlineValues.put("urgentReason", i.getValue()); // TODO array
                            }
                        }
                        /*    Project specific code ends here   */

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
                if(Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION)) {
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

        if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SCREENING_CALL_IN)) {
            int occurrences = getIntent().getIntExtra(ParamNames.ENCOUNTER_OCCURANCES, 0);

            if (occurrences == 0) {
                try {
                    String dopString = getIntent().getStringExtra(ParamNames.DATE_OF_PROCEDURE);
                    if (dopString != null) {
                        InputWidget dop = inputWidgets.get(2028);
                        dop.setAnswer(dopString, null, null);
                        dop.setEnabled(false);
                        long daysSinceSurgery = AppUtility.getDifferenceDays(Global.DATE_TIME_FORMAT.parse(dopString), new Date());
                        InputWidget surgeryDays = inputWidgets.get(2029);
                        surgeryDays.setAnswer(daysSinceSurgery + "", surgeryDays.getQuestion().getParamName(), LANGUAGE.ENGLISH);
                        surgeryDays.setEnabled(false);
                    }
                    InputWidget appointmentDate = inputWidgets.get(2027);
                    InputWidget appointmentTime = inputWidgets.get(2004);
                    appointmentDate.setVisibility(View.GONE);
                    appointmentTime.setVisibility(View.GONE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (occurrences > 0) {
                Set<Integer> it = inputWidgets.keySet();
                for (int i : it) {
                    InputWidget inputWidget = inputWidgets.get(i);
                    if (!(inputWidget.getQuestionId() == 2027 || inputWidget.getQuestionId() == 2004)) {
                        inputWidget.setVisibility(View.GONE);
                    }
                }
            }

        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_DAY_1)) {
            final SpinnerWidget spinnerWidget = (SpinnerWidget) inputWidgets.get(16006);
            final RadioButtonWidget radioButtonWidget = (RadioButtonWidget) inputWidgets.get(16007);

            OnValueChangeListener onChangeListener = new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {

                    if(newValue.equals("Continuous / large amount of bleeding") || newValue.equals("No")) {
                        new AlertDialog.Builder(BaseActivity.this).setTitle("Warning").setMessage("Please refer the patient to Indus Hospital Emergency").setNeutralButton("OK", null).show();
                    }
                }
            };

            spinnerWidget.setOnValueChangeListener(onChangeListener);
            radioButtonWidget.setOnValueChangeListener(onChangeListener);

            Intent intent = getIntent();
            final String circumcisionDate = intent.getStringExtra(ParamNames.CIRCUMCISION_DATE);
            // Setting Date
            Date prodedureDate = new Date();
            try {
                prodedureDate = Global.OPENMRS_TIMESTAMP_FORMAT.parse(circumcisionDate);

            } catch (ParseException e) {
                try {
                    prodedureDate = Global.DATE_TIME_FORMAT.parse(circumcisionDate);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
            inputWidgets.get(11113).setAnswer(Global.DATE_TIME_FORMAT.format(prodedureDate), null, LANGUAGE.ENGLISH);
            inputWidgets.get(11113).setEnabled(false);

            DateWidget dateWidget = (DateWidget) inputWidgets.get(16001);
            Calendar c = Calendar.getInstance();
            c.setTime(prodedureDate);
            c.add(Calendar.DAY_OF_YEAR, 1);
            dateWidget.setAnswer(Global.DATE_TIME_FORMAT.format(c.getTime()), dateWidget.getQuestion().getParamName(), LANGUAGE.ENGLISH);
        }
        else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_DAY_7)) {
            Intent intent = getIntent();
            final String circumcisionDate = intent.getStringExtra(ParamNames.CIRCUMCISION_DATE);
            final String stepsTaken = intent.getStringExtra(ParamNames.PARAM_STEPS_TAKEN)==(null)?"":intent.getStringExtra(ParamNames.PARAM_STEPS_TAKEN);
            final String adviceDay1 = intent.getStringExtra(ParamNames.ADVICE_DAY_1)==(null)?"":intent.getStringExtra(ParamNames.ADVICE_DAY_1);
            String otherDay1 = intent.getStringExtra(ParamNames.OTHER_DESC_1);
            String otherDay7 = intent.getStringExtra(ParamNames.OTHER_DESC_7);


            // Setting Date
            Date prodedureDate = new Date();
            try {
                prodedureDate = Global.OPENMRS_TIMESTAMP_FORMAT.parse(circumcisionDate);

            } catch (ParseException e) {
                try {
                    prodedureDate = Global.DATE_TIME_FORMAT.parse(circumcisionDate);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }

            inputWidgets.get(11112).setAnswer(Global.DATE_TIME_FORMAT.format(prodedureDate), null, LANGUAGE.ENGLISH);
            inputWidgets.get(11112).setEnabled(false);

            DateWidget dateWidget = (DateWidget) inputWidgets.get(16011);
            Calendar c = Calendar.getInstance();
            c.setTime(prodedureDate);
            c.add(Calendar.DAY_OF_YEAR, 7);
            dateWidget.setAnswer(Global.DATE_TIME_FORMAT.format(c.getTime()), dateWidget.getQuestion().getParamName(), LANGUAGE.ENGLISH);

            final RadioButtonWidget complication = (RadioButtonWidget) inputWidgets.get(19036);
            final SpinnerWidget ringFallenWidget = (SpinnerWidget) inputWidgets.get(16013);
            OnValueChangeListener listener = new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    if(newValue.equalsIgnoreCase("Cut and removed")) {
                        if (stepsTaken.contains("Refer") || adviceDay1.contains("Refer")) {
                            complication.setVisibility(View.VISIBLE);
                        } else {
                            complication.setVisibility(View.GONE);
                        }
                    } else {
                        complication.setVisibility(View.GONE);
                    }
                }
            };
            ringFallenWidget.setOnValueChangeListener(listener);
            inputWidgets.get(16021).setOnValueChangeListener(new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    if(newValue.contains("Refer")) {
                        complication.setVisibility(View.VISIBLE);
                    } else {
                        if(ringFallenWidget.value().equalsIgnoreCase("Cut and removed")) {
                            if (stepsTaken.contains("Refer") || adviceDay1.contains("Refer")) {
                                complication.setVisibility(View.VISIBLE);
                            } else {
                                complication.setVisibility(View.GONE);
                            }
                        } else {
                            complication.setVisibility(View.GONE);
                        }
                    }
                }
            });



            final EditTextWidget complicationOutcome = (EditTextWidget) inputWidgets.get(19035);
            final String complicationOutcomeStr = "Day1: "+otherDay1;
            complicationOutcome.setAnswer(complicationOutcomeStr, ParamNames.FORM3_COMPLICATIONS, LANGUAGE.ENGLISH);

            EditTextWidget day7OtherWidget = (EditTextWidget) inputWidgets.get(16022);
            day7OtherWidget.setOnValueChangeListener(new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    complicationOutcome.setAnswer(complicationOutcomeStr+" Day7: "+newValue, ParamNames.FORM3_COMPLICATIONS, LANGUAGE.ENGLISH);
                }
            });

            final DateWidget ringDateWidget = (DateWidget) inputWidgets.get(16014);
            OnValueChangeListener ringFallenListener = new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    try {
                        Date from = Global.OPENMRS_TIMESTAMP_FORMAT.parse(circumcisionDate);
                        Date to = Global.DATE_TIME_FORMAT.parse(newValue);
                        Period period = new Period(new DateTime(from), new DateTime(to), PeriodType.days());

                        EditTextWidget ringDayWidget = (EditTextWidget) inputWidgets.get(16015);
                        if(period.getDays()>=0)
                            ringDayWidget.setAnswer(period.getDays()/*+1*/+"", "", LANGUAGE.ENGLISH);
                        else
                            ringDateWidget.setAnswer(Global.DATE_TIME_FORMAT.format(from), ParamNames.DATE_RING_FALLEN, LANGUAGE.ENGLISH);;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };

            ringDateWidget.setOnValueChangeListener(ringFallenListener);
        }
        else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_DAY_10)) {
            Intent intent = getIntent();
            final String circumcisionDate = intent.getStringExtra(ParamNames.CIRCUMCISION_DATE);
            // Setting Date
            Date procedureDate = new Date();
            try {
                procedureDate = Global.OPENMRS_TIMESTAMP_FORMAT.parse(circumcisionDate);

            } catch (ParseException e) {
                try {
                    procedureDate = Global.DATE_TIME_FORMAT.parse(circumcisionDate);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }

            inputWidgets.get(11111).setAnswer(Global.DATE_TIME_FORMAT.format(procedureDate), null, LANGUAGE.ENGLISH);
            inputWidgets.get(11111).setEnabled(false);

            DateWidget dateWidget = (DateWidget) inputWidgets.get(16023);
            Calendar c = Calendar.getInstance();
            c.setTime(procedureDate);
            c.add(Calendar.DAY_OF_YEAR, 10);
            dateWidget.setAnswer(Global.DATE_TIME_FORMAT.format(c.getTime()), dateWidget.getQuestion().getParamName(), LANGUAGE.ENGLISH);

            boolean caseClosed = intent.getBooleanExtra(ParamNames.CASE_CLOSED, false);
            if (caseClosed) {
                inputWidgets.get(16024).setVisibility(View.GONE);
            } else {

                final String stepsTaken = intent.getStringExtra(ParamNames.PARAM_STEPS_TAKEN);
                final String adviceDay10 = intent.getStringExtra(ParamNames.ADVICE_DAY_10);
                final String adviceDay7 = intent.getStringExtra(ParamNames.ADVICE_DAY_7);
                final String adviceDay1 = intent.getStringExtra(ParamNames.ADVICE_DAY_1);
                String otherDay1 = intent.getStringExtra(ParamNames.OTHER_DESC_1);
                String otherDay7 = intent.getStringExtra(ParamNames.OTHER_DESC_7);
                String otherDay10 = intent.getStringExtra(ParamNames.OTHER_DESC_10);
                String hsRingFallen = intent.getStringExtra(ParamNames.HAS_THE_RING_FALLEN);
                String satisfaction = intent.getStringExtra(ParamNames.GUARDIAN_SATISFACTION);
                String overallSatisfaction = intent.getStringExtra(ParamNames.FORM3_OVERALL_PARENT_SATISFIED);




                if (!satisfaction.startsWith("Yes")) {
                    inputWidgets.get(16030).setVisibility(View.GONE);
                }

                if (!overallSatisfaction.startsWith("Yes")) {
                    inputWidgets.get(16031).setVisibility(View.GONE);
                }
                final RadioButtonWidget complication = (RadioButtonWidget) inputWidgets.get(16036);
                final SpinnerWidget ringFallenWidget = (SpinnerWidget) inputWidgets.get(16025);
                OnValueChangeListener listener = new OnValueChangeListener() {
                    @Override
                    public void onValueChanged(String newValue) {
                        if(newValue.equalsIgnoreCase("Cut and removed")) {
                            if (stepsTaken.contains("Refer") || adviceDay1.contains("Refer")) {
                                complication.setVisibility(View.VISIBLE);
                            } else {
                                complication.setVisibility(View.GONE);
                            }
                        } else {
                            complication.setVisibility(View.GONE);
                        }
                    }
                };
                ringFallenWidget.setOnValueChangeListener(listener);

                inputWidgets.get(16033).setOnValueChangeListener(new OnValueChangeListener() {
                    @Override
                    public void onValueChanged(String newValue) {
                        if(newValue.contains("Refer")) {
                            complication.setVisibility(View.VISIBLE);
                        } else {
                            if(ringFallenWidget.value().equalsIgnoreCase("Cut and removed")) {
                                if (stepsTaken.contains("Refer") || adviceDay1.contains("Refer")) {
                                    complication.setVisibility(View.VISIBLE);
                                } else {
                                    complication.setVisibility(View.GONE);
                                }
                            } else {
                                complication.setVisibility(View.GONE);
                            }
                        }
                    }
                });

                final EditTextWidget complicationOutcome = (EditTextWidget) inputWidgets.get(16035);
                final String complicationOutcomeStr = "Day1: " + otherDay1 + ", Day7: " + otherDay7 + ", " + otherDay10;
                complicationOutcome.setAnswer(complicationOutcomeStr, ParamNames.FORM3_COMPLICATIONS, LANGUAGE.ENGLISH);

                EditTextWidget day10OtherWidget = (EditTextWidget) inputWidgets.get(16034);
                day10OtherWidget.setOnValueChangeListener(new OnValueChangeListener() {
                    @Override
                    public void onValueChanged(String newValue) {
                        complicationOutcome.setAnswer(complicationOutcomeStr + " Day10: " + newValue, ParamNames.FORM3_COMPLICATIONS, LANGUAGE.ENGLISH);
                    }
                });

                final DateWidget ringDateWidget = (DateWidget) inputWidgets.get(16026);
                final Date finalProcedureDate = procedureDate;
                OnValueChangeListener ringFallenListener = new OnValueChangeListener() {
                    @Override
                    public void onValueChanged(String newValue) {
                        try {

                            Date to = Global.DATE_TIME_FORMAT.parse(newValue);
                            Period period = new Period(new DateTime(finalProcedureDate), new DateTime(to), PeriodType.days());

                            EditTextWidget ringDayWidget = (EditTextWidget) inputWidgets.get(16027);
                            if (period.getDays() >= 0)
                                ringDayWidget.setAnswer(period.getDays() /*+ 1*/ + "", "", LANGUAGE.ENGLISH);
                            else
                                ringDateWidget.setAnswer(Global.DATE_TIME_FORMAT.format(finalProcedureDate), ParamNames.DATE_RING_FALLEN, LANGUAGE.ENGLISH);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };

                ringDateWidget.setOnValueChangeListener(ringFallenListener);
            }
        }
        else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SAFE_CIRCUMCISION_FORM_3)) {
            Intent intent = getIntent();
            String circumcisionDate = intent.getStringExtra(ParamNames.CIRCUMCISION_DATE);
            final String stepsTaken = intent.getStringExtra(ParamNames.PARAM_STEPS_TAKEN);
            final String adviceDay10 = intent.getStringExtra(ParamNames.ADVICE_DAY_10);
            final String adviceDay7 = intent.getStringExtra(ParamNames.ADVICE_DAY_7);
            final String adviceDay1 = intent.getStringExtra(ParamNames.ADVICE_DAY_1);
            String otherDay1 = intent.getStringExtra(ParamNames.OTHER_DESC_1);
            String otherDay7 = intent.getStringExtra(ParamNames.OTHER_DESC_7);
            String otherDay10 = intent.getStringExtra(ParamNames.OTHER_DESC_10);
            String hsRingFallen = intent.getStringExtra(ParamNames.HAS_THE_RING_FALLEN);
            final RadioButtonWidget complication = (RadioButtonWidget) inputWidgets.get(16036);
            if (stepsTaken.contains("Refer") || adviceDay1.contains("Refer") || adviceDay7.contains("Refer")) {
                complication.setVisibility(View.VISIBLE);
            }
            final SpinnerWidget day10Advice = (SpinnerWidget) inputWidgets.get(16033);
            day10Advice.setOnValueChangeListener(new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    // adviceDay10 = newValue;
                    if(newValue.contains("Refer") || stepsTaken.contains("Refer") || adviceDay1.contains("Refer") || adviceDay7.contains("Refer"))
                        complication.setVisibility(View.VISIBLE);
                    else
                        complication.setVisibility(View.GONE);
                }
            });

            SpinnerWidget ringFallen7 = (SpinnerWidget) inputWidgets.get(16013);
            SpinnerWidget ringFallen10 = (SpinnerWidget) inputWidgets.get(16025);

            OnValueChangeListener ringFallenListener = new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    if(newValue.equalsIgnoreCase("yes") || newValue.equalsIgnoreCase("no"))
                        complication.setVisibility(View.GONE);
                    else {
                        if(day10Advice.value().contains("Refer") || stepsTaken.contains("Refer") || adviceDay1.contains("Refer") || adviceDay7.contains("Refer"))
                            complication.setVisibility(View.VISIBLE);
                    }
                }
            };

            ringFallen7.setOnValueChangeListener(ringFallenListener);
            ringFallen10.setOnValueChangeListener(ringFallenListener);

            final EditTextWidget complicationOutcome = (EditTextWidget) inputWidgets.get(16035);
            final String complicationOutcomeStr = "Day1: "+otherDay1+", Day7: "+otherDay7+", "+otherDay10;
            complicationOutcome.setAnswer(complicationOutcomeStr, ParamNames.FORM3_COMPLICATIONS, LANGUAGE.ENGLISH);

            EditTextWidget day10OtherWidget = (EditTextWidget) inputWidgets.get(16034);
            day10OtherWidget.setOnValueChangeListener(new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    complicationOutcome.setAnswer(complicationOutcomeStr+" Day10: "+newValue, ParamNames.FORM3_COMPLICATIONS, LANGUAGE.ENGLISH);
                }
            });

        }
        else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SAFE_CIRCUMCISION_FORM_2)) {

            EditTextWidget editTextWidget = (EditTextWidget) inputWidgets.get(1501001);
            editTextWidget.setAnswer(Global.USERNAME, ParamNames.FORM2_NAME_HEALTH_PROVIDER, LANGUAGE.ENGLISH);
            final RadioButtonWidget genitalScreening = (RadioButtonWidget) inputWidgets.get(15002);
            final RadioButtonWidget anesthesia = (RadioButtonWidget) inputWidgets.get(15003);
            final InputWidget[] inputWidgetsArray = new InputWidget[]{inputWidgets.get(15004), inputWidgets.get(15006), inputWidgets.get(15012)};
            OnValueChangeListener onChangeListener = new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {

                    if(!genitalScreening.getValue().equals("Yes") || !anesthesia.getValue().equals("Yes")) {
                        for(InputWidget i: inputWidgetsArray) {
                            i.setVisibility(View.GONE);
                        }
                    } else {
                        for(InputWidget i: inputWidgetsArray) {
                            i.setVisibility(View.VISIBLE);
                        }
                    }
                }
            };

            genitalScreening.setOnValueChangeListener(onChangeListener);
            anesthesia.setOnValueChangeListener(onChangeListener);

            Intent intent = getIntent();
            final String circumcisionDate = intent.getStringExtra(ParamNames.CIRCUMCISION_DATE);
            // Setting Date
            Date prodedureDate = new Date();
            try {
                prodedureDate = Global.OPENMRS_TIMESTAMP_FORMAT.parse(circumcisionDate);

            } catch (ParseException e) {
                try {
                    prodedureDate = Global.DATE_TIME_FORMAT.parse(circumcisionDate);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }

            inputWidgets.get(15016).setAnswer(Global.DATE_TIME_FORMAT.format(prodedureDate), null, LANGUAGE.ENGLISH);
            inputWidgets.get(15016).setEnabled(false);
        }
        else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SAFE_CIRCUMCISION_FORM_1)) {
            inputWidgets.get(14100).setEnabled(false);
            RadioButtonWidget preTermRadio = (RadioButtonWidget) inputWidgets.get(14003);
            OnValueChangeListener preTermValueChangeListener = new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {
                    RadioButtonWidget radioButtonWidget = (RadioButtonWidget) inputWidgets.get(14005);
                    if(newValue.equals("Baby was pre term")) {
                        radioButtonWidget.setAnswer("No", "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", LANGUAGE.ENGLISH);
                        radioButtonWidget.setEnabled(false);
                    } else {
                        radioButtonWidget.setEnabled(true);
                    }
                }
            };
            preTermRadio.setOnValueChangeListener(preTermValueChangeListener);


            Date birthDate = patientData.getPatient().getBirthDate();
            final DateTime birthTime = new DateTime(birthDate);
            inputWidgets.get(14101).setEnabled(false);
            final InputWidget ageGreaterThan90Days = inputWidgets.get(14002);
            int totalDays = Days.daysBetween(new LocalDate(birthTime), new LocalDate()).getDays();
            inputWidgets.get(14101).setAnswer("" + totalDays, "", null);
            if (totalDays <= 90)
                ageGreaterThan90Days.setAnswer("Yes", ParamNames.FORM1_YES, LANGUAGE.ENGLISH);
            else
                ageGreaterThan90Days.setAnswer("No", ParamNames.FORM1_NO, LANGUAGE.ENGLISH);
            DateWidget procedureDateWidget = (DateWidget) inputWidgets.get(4111);
            OnValueChangeListener procedureDateListener = new OnValueChangeListener() {
                @Override
                public void onValueChanged(String newValue) {

                    DateTime procedureTime = null;
                    try {
                        procedureTime = new DateTime(Global.DATE_TIME_FORMAT.parse(newValue));
                        int totalDays = Days.daysBetween(new LocalDate(birthTime), new LocalDate(procedureTime)).getDays();
                        inputWidgets.get(14101).setAnswer("" + totalDays, "", null);

                        if (totalDays <= 90)
                            ageGreaterThan90Days.setAnswer("Yes", ParamNames.FORM1_YES, LANGUAGE.ENGLISH);
                        else
                            ageGreaterThan90Days.setAnswer("No", ParamNames.FORM1_NO, LANGUAGE.ENGLISH);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            procedureDateWidget.setOnValueChangeListener(procedureDateListener);

            inputWidgets.get(14009).setEnabled(false);

            // 14002
            inputWidgets.get(14002).setEnabled(false);


            /*DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
            String formattedDate = df.format(Calendar.getInstance().getTime());
            inputWidgets.get(14100).setAnswer("ESC-" + formattedDate + "-", "", null);*/

            EditTextWidget babyWeight = (EditTextWidget) inputWidgets.get(14008);
            babyWeight.getInputField().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.length()==0) return;
                    float weight = Float.parseFloat(editable.toString());
                    InputWidget babyWeightGT2 = inputWidgets.get(14009);
                    if (weight >=2.5)
                        babyWeightGT2.setAnswer("Yes", ParamNames.FORM1_YES, LANGUAGE.ENGLISH);
                    else
                        babyWeightGT2.setAnswer("No", ParamNames.FORM1_NO, LANGUAGE.ENGLISH);
                }
            });


        }
        else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SURGICAL_SITE_EVALUATION)) {
            String dopString = getIntent().getStringExtra(ParamNames.DATE_OF_PROCEDURE);
            if (dopString != null) {
                InputWidget dop = inputWidgets.get(5037);
                dop.setAnswer(dopString, null, null);
                dop.setEnabled(false);
            }
        }
        if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SSI_DETECTION)) {
            try {
                String nameOfProcedure = getIntent().getStringExtra(ParamNames.NAME_OF_PROCEDURE);
                String dopString = getIntent().getStringExtra(ParamNames.DATE_OF_PROCEDURE);
                String dofString = getIntent().getStringExtra(ParamNames.DATE_OF_FOLLOWUP);
                String followupStatus = getIntent().getStringExtra(ParamNames.FOLLOW_UP_STATUS);
                String urgentReason = getIntent().getStringExtra(ParamNames.URGENT_REASON);
                String dateCalledString = getIntent().getStringExtra(ParamNames.DATE_CALLED);
                String diagnosisDateString = getIntent().getStringExtra(ParamNames.DATE_OF_DIAGNOSIS);

                if (dopString != null) {
                    InputWidget dop = inputWidgets.get(5057);
                    dop.setAnswer(dopString, null, null);
                    dop.setEnabled(false);

                    long daysSinceSurgery = AppUtility.getDifferenceDays(Global.DATE_TIME_FORMAT.parse(dopString), new Date());
                    InputWidget surgeryDays = inputWidgets.get(5058);
                    surgeryDays.setAnswer(daysSinceSurgery + "", surgeryDays.getQuestion().getParamName(), LANGUAGE.ENGLISH);
                    surgeryDays.setEnabled(false);
                } else {
                    inputWidgets.get(5057).setVisibility(View.GONE);
                    inputWidgets.get(5058).setVisibility(View.GONE);
                }

                if (diagnosisDateString != null) {
                    InputWidget dod = inputWidgets.get(5050);
                    dod.setAnswer(diagnosisDateString, null, null);
                    dod.setEnabled(false);
                } else {
                    inputWidgets.get(5050).setVisibility(View.GONE);
                }

                if (dofString != null) {
                    InputWidget dof = inputWidgets.get(5060);
                    dof.setAnswer(dofString, null, null);
                    dof.setEnabled(false);
                } else {
                    inputWidgets.get(5060).setVisibility(View.GONE);
                }

                if (dateCalledString != null) {
                    InputWidget dod = inputWidgets.get(5061);
                    dod.setAnswer(dateCalledString, null, null);
                    dod.setEnabled(false);
                } else {
                    inputWidgets.get(5061).setVisibility(View.GONE);
                }

                if (nameOfProcedure != null) {
                    InputWidget procedureName = inputWidgets.get(5056);
                    procedureName.setAnswer(nameOfProcedure, procedureName.getQuestion().getParamName(), LANGUAGE.ENGLISH);
                    procedureName.setEnabled(false);
                } else {
                    inputWidgets.get(5056).setVisibility(View.GONE);
                }

                if (urgentReason != null) {
                    InputWidget ur = inputWidgets.get(5062);
                    ur.setAnswer(urgentReason, null, null);
                    ur.setEnabled(false);
                } else {
                    inputWidgets.get(5062).setVisibility(View.GONE);
                }

                if (followupStatus != null) {
                    InputWidget fupStatus = inputWidgets.get(5059);
                    fupStatus.setAnswer(followupStatus, null, LANGUAGE.ENGLISH);
                    fupStatus.setEnabled(false);
                } else {
                    inputWidgets.get(5059).setVisibility(View.GONE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_DEMOGRAPHIC_INFORMATION)) {
            try {
                inputWidgets.get(11000).setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //fillPatientInfoBar();
        }

        else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_POST_OP_FOLLOW_UP)) {
            String interventionArm = getIntent().getStringExtra(ParamNames.INTERVENTION_ARM);
            if(Objects.equals(interventionArm, "Reminder")) {
                if(!interventionArm.isEmpty()) {
                    SpinnerWidget spinnerWidget = (SpinnerWidget) inputWidgets.get(4002);
                    spinnerWidget.setEnabled(false);
                    spinnerWidget.setAnswer("Reminder", null, LANGUAGE.ENGLISH);
                }
            } else if(Objects.equals(interventionArm, "N- Reminder")) {
                if(!interventionArm.isEmpty()) {
                    SpinnerWidget spinnerWidget = (SpinnerWidget) inputWidgets.get(4002);
                    spinnerWidget.setEnabled(false);
                    spinnerWidget.setAnswer("N- Reminder", null, LANGUAGE.ENGLISH);
                }
            }
        }

        else if(Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_PIRANI_SCORING)) {

            SpinnerWidget spinnerWidget = (SpinnerWidget) inputWidgets.get(12070);
            spinnerWidget.setEnabled(false);
            String effectedfoot = getIntent().getStringExtra(ParamNames.EFFECTED_FOOT);
            if(effectedfoot!=null)
                spinnerWidget.setAnswer(effectedfoot, null, LANGUAGE.ENGLISH);

            final EditTextWidget rightHindFoot = (EditTextWidget) inputWidgets.get(12019);
            rightHindFoot.setEnabled(false);

            final SpinnerWidget rightCrease = (SpinnerWidget) inputWidgets.get(12013);
            final SpinnerWidget rightEmptyHeel = (SpinnerWidget) inputWidgets.get(12015);
            final SpinnerWidget rightEnquinus = (SpinnerWidget) inputWidgets.get(12017);

            OnValueChangeListener onValueChangeListener = new OnValueChangeListener() {
                float sum;
                @Override
                public void onValueChanged(String newValue) {
                    float v1=0, v2=0, v3=0;
                    if(!rightCrease.value().isEmpty())
                        v1 = Float.parseFloat(rightCrease.value());

                    if(!rightEmptyHeel.value().isEmpty())
                        v2 = Float.parseFloat(rightEmptyHeel.value());

                    if(!rightEnquinus.value().isEmpty())
                        v3 = Float.parseFloat(rightEnquinus.value());

                    sum = v1+v2+v3;
                    rightHindFoot.setAnswer(sum+"", null, null);
                }
            };

            rightCrease.setOnValueChangeListener(onValueChangeListener);
            rightEmptyHeel.setOnValueChangeListener(onValueChangeListener);
            rightEnquinus.setOnValueChangeListener(onValueChangeListener);


            final EditTextWidget rightMidFoot = (EditTextWidget) inputWidgets.get(12027);
            rightMidFoot.setEnabled(false);

            final SpinnerWidget rightTalar = (SpinnerWidget) inputWidgets.get(12021);
            final SpinnerWidget rightMedial = (SpinnerWidget) inputWidgets.get(12023);
            final SpinnerWidget rightCurvedLateral = (SpinnerWidget) inputWidgets.get(12025);

            OnValueChangeListener onValueChangeListener1 = new OnValueChangeListener() {
                float sum;
                @Override
                public void onValueChanged(String newValue) {
                    float v1=0, v2=0, v3=0;
                    if(!rightTalar.value().isEmpty())
                        v1 = Float.parseFloat(rightTalar.value());

                    if(!rightMedial.value().isEmpty())
                        v2 = Float.parseFloat(rightMedial.value());

                    if(!rightCurvedLateral.value().isEmpty())
                        v3 = Float.parseFloat(rightCurvedLateral.value());

                    sum = v1+v2+v3;
                    rightMidFoot.setAnswer(sum+"", null, null);
                }
            };

            rightTalar.setOnValueChangeListener(onValueChangeListener1);
            rightMedial.setOnValueChangeListener(onValueChangeListener1);
            rightCurvedLateral.setOnValueChangeListener(onValueChangeListener1);

            final InputWidget rightTotalScore = inputWidgets.get(12029);
            rightTotalScore.setEnabled(false);

            OnValueChangeListener onValueChangeListener2 = new OnValueChangeListener() {
                float sum;
                @Override
                public void onValueChanged(String newValue) {
                    float v1=0, v2=0;
                    if(!rightHindFoot.getUnValidatedValue().isEmpty())
                        v1 = Float.parseFloat(rightHindFoot.getUnValidatedValue());

                    if(!rightMidFoot.getUnValidatedValue().isEmpty())
                        v2 = Float.parseFloat(rightMidFoot.getUnValidatedValue());

                    sum = v1+v2;
                    rightTotalScore.setAnswer(sum+"", null, null);
                }
            };

            rightHindFoot.setOnValueChangeListener(onValueChangeListener2);
            rightMidFoot.setOnValueChangeListener(onValueChangeListener2);

            //////////////////////////////////////////////////////////////////////////////////////////////////////////

            final EditTextWidget leftHindFoot = (EditTextWidget) inputWidgets.get(12020);
            leftHindFoot.setEnabled(false);

            final SpinnerWidget leftCrease = (SpinnerWidget) inputWidgets.get(12014);
            final SpinnerWidget leftEmptyHeel = (SpinnerWidget) inputWidgets.get(12016);
            final SpinnerWidget leftEnquinus = (SpinnerWidget) inputWidgets.get(12018);

            OnValueChangeListener onValueChangeListener3 = new OnValueChangeListener() {
                float sum;
                @Override
                public void onValueChanged(String newValue) {
                    float v1=0, v2=0, v3=0;
                    if(!leftCrease.value().isEmpty())
                        v1 = Float.parseFloat(leftCrease.value());

                    if(!leftEmptyHeel.value().isEmpty())
                        v2 = Float.parseFloat(leftEmptyHeel.value());

                    if(!leftEnquinus.value().isEmpty())
                        v3 = Float.parseFloat(leftEnquinus.value());

                    sum = v1+v2+v3;
                    leftHindFoot.setAnswer(sum+"", null, null);
                }
            };

            leftCrease.setOnValueChangeListener(onValueChangeListener3);
            leftEmptyHeel.setOnValueChangeListener(onValueChangeListener3);
            leftEnquinus.setOnValueChangeListener(onValueChangeListener3);


            final EditTextWidget leftMidFoot = (EditTextWidget) inputWidgets.get(12028);
            leftMidFoot.setEnabled(false);

            final SpinnerWidget leftTalar = (SpinnerWidget) inputWidgets.get(12022);
            final SpinnerWidget leftMedial = (SpinnerWidget) inputWidgets.get(12024);
            final SpinnerWidget leftCurvedLateral = (SpinnerWidget) inputWidgets.get(12026);

            OnValueChangeListener onValueChangeListener4 = new OnValueChangeListener() {
                float sum;
                @Override
                public void onValueChanged(String newValue) {
                    float v1=0, v2=0, v3=0;
                    if(!leftTalar.value().isEmpty())
                        v1 = Float.parseFloat(leftTalar.value());

                    if(!leftMedial.value().isEmpty())
                        v2 = Float.parseFloat(leftMedial.value());

                    if(!leftCurvedLateral.value().isEmpty())
                        v3 = Float.parseFloat(leftCurvedLateral.value());

                    sum = v1+v2+v3;
                    leftMidFoot.setAnswer(sum+"", null, null);
                }
            };

            leftTalar.setOnValueChangeListener(onValueChangeListener4);
            leftMedial.setOnValueChangeListener(onValueChangeListener4);
            leftCurvedLateral.setOnValueChangeListener(onValueChangeListener4);

            final InputWidget leftTotalScore = inputWidgets.get(12030);
            leftTotalScore.setEnabled(false);

            OnValueChangeListener onValueChangeListener5 = new OnValueChangeListener() {
                float sum;
                @Override
                public void onValueChanged(String newValue) {
                    float v1=0, v2=0;
                    if(!leftHindFoot.getUnValidatedValue().isEmpty())
                        v1 = Float.parseFloat(leftHindFoot.getUnValidatedValue());

                    if(!leftMidFoot.getUnValidatedValue().isEmpty())
                        v2 = Float.parseFloat(leftMidFoot.getUnValidatedValue());

                    sum = v1+v2;
                    leftTotalScore.setAnswer(sum+"", null, null);
                }
            };

            leftHindFoot.setOnValueChangeListener(onValueChangeListener5);
            leftMidFoot.setOnValueChangeListener(onValueChangeListener5);


        }
        if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION)
                && GlobalPreferences.getinstance(this).findPrferenceValue(GlobalPreferences.KEY.PROGRAM, "").equals("Safe Circumcision")) {
            inputWidgets.get(6003).setEnabled(false);
        }
       /* if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_PIRANI_SCORING)) {
            try {
                DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
                String formattedDate = df.format(Calendar.getInstance().getTime());
                String location = inputWidgets.get(12070).getOptions().get(0).getText();
                if (LOCATION.KARACHI.name().equals(location)) {
                    //Set Visibility and Populate answer according to the location
                    inputWidgets.get(12071).setVisibility(View.VISIBLE);
                    inputWidgets.get(12072).setVisibility(View.GONE);
                    inputWidgets.get(12073).setVisibility(View.GONE);
                    inputWidgets.get(12071).setAnswer("CF-"+formattedDate+"-","",null);
                } else if (LOCATION.MUZAFFARGARH.name().equals(location)) {
                    //Set Visibility and Populate answer according to the location
                    inputWidgets.get(12071).setVisibility(View.GONE);
                    inputWidgets.get(12072).setVisibility(View.VISIBLE);
                    inputWidgets.get(12073).setVisibility(View.GONE);
                    inputWidgets.get(12072).setAnswer("CFMUZ-"+formattedDate+"-","",null);
                } else if (LOCATION.MANAWAN.name().equals(location)) {
                    //Set Visibility and Populate answer according to the location
                    inputWidgets.get(12071).setVisibility(View.GONE);
                    inputWidgets.get(12072).setVisibility(View.GONE);
                    inputWidgets.get(12073).setVisibility(View.VISIBLE);
                    inputWidgets.get(12073).setAnswer("CFMAN-"+formattedDate+"-","",null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            fillPatientInfoBar();
        }*/
        if (DataProvider.directOpenableForms.contains(Form.getENCOUNTER_NAME())) {
            // ((SpinnerWidget)inputWidgets.get(125)).setEnabled(false);
            tvRatings.setVisibility(View.GONE);
            llPatientInfoDisplayer.setVisibility(View.GONE);
        } else {
            fillPatientInfoBar();
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
            if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_DEMOGRAPHIC_INFORMATION)) {
                JSONObject pqIdentifier = new JSONObject();
                pqIdentifier.put(ParamNames.TYPE, ParamNames.PQ_PROJECT_IDENTIFIER);
                if (inputWidgets.get(11000).getVisibility() == View.VISIBLE) {
                    pqIdentifier.put(ParamNames.VALUE, ((IdentifierWidget) inputWidgets.get(11000)).getValue());
                }
                if (inputWidgets.get(11061).getVisibility() == View.VISIBLE) {
                    pqIdentifier.put(ParamNames.VALUE, ((IdentifierWidget) inputWidgets.get(11061)).getValue());
                }
                if (inputWidgets.get(11062).getVisibility() == View.VISIBLE) {
                    pqIdentifier.put(ParamNames.VALUE, ((IdentifierWidget) inputWidgets.get(11062)).getValue());
                }
                identifiers.put(pqIdentifier);
            } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SAFE_CIRCUMCISION_FORM_1)){
                JSONObject cscIdentifier = new JSONObject();
                cscIdentifier.put(ParamNames.TYPE, ParamNames.CSC_PROJECT_IDENTIFIER);
                InputWidget inputWidget = inputWidgets.get(14100);
                if (inputWidget.getVisibility() == View.VISIBLE) {
                    cscIdentifier.put(ParamNames.VALUE, ((IdentifierWidget) inputWidget).getValue());
                }
                identifiers.put(cscIdentifier);
            }else {

            }

            patientDataJson.put(ParamNames.IDENTIFIERS, identifiers);
            data.put(ParamNames.PATIENT, patientDataJson);
        }
    }

    private void putAuthenticationData(JSONObject data) throws Exception {
        SecretKey secKey = AES256Endec.getInstance().generateKey();
        String encPassword = AES256Endec.getInstance().encrypt(Global.PASSWORD, secKey);
        JSONObject authenticationJson = new JSONObject();
        authenticationJson.put(ParamNames.USERNAME, Global.USERNAME);
        authenticationJson.put(ParamNames.PASSWORD, encPassword);
        String providerUUID = resolveProviderUUID();
        authenticationJson.put(ParamNames.PROVIDER_UUID, providerUUID);
        data.put(ParamNames.AUTHENTICATION, authenticationJson);
    }

    private String resolveProviderUUID() throws JSONException {
        String providerUUID;

        if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_POST_OP_FOLLOW_UP)) {
            String icmName = ((AutoCompleteTextViewWidget) inputWidgets.get(4001)).getAnswer().getString(ParamNames.ICM_ID_NUMBER);
            providerUUID = DataAccess.getInstance().getUserCredentials(this, icmName).getProviderUUID();
        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_PRE_OP_DEMOGRAPHICS)) {
            String icmName = ((AutoCompleteTextViewWidget) inputWidgets.get(93)).getAnswer().getString(ParamNames.ICM_ID_NUMBER);
            providerUUID = DataAccess.getInstance().getUserCredentials(this, icmName).getProviderUUID();
        } else if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_SURGICAL_SITE_EVALUATION)) {
            String icmName = ((AutoCompleteTextViewWidget) inputWidgets.get(5040)).getAnswer().getString(ParamNames.ICM_ID_NUMBER);
            providerUUID = DataAccess.getInstance().getUserCredentials(this, icmName).getProviderUUID();
        } else {
            providerUUID = DataAccess.getInstance().getUserCredentials(this, Global.USERNAME).getProviderUUID();
            ;
        }

        return providerUUID;
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
        if (Form.getENCOUNTER_NAME().equals(ParamNames.ENCOUNTER_TYPE_MEDICAL_QUESTIONAIRE)) {
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
        }
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
