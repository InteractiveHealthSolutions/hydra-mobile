package com.ihsinformatics.dynamicformsgenerator.screens;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.common.Constants;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.DynamicOptions;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.AutoSelect;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SExpression;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SkipLogics;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.AddressConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.Configuration;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.ContactTraceChildFields;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.ContactTracingConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormType;
import com.ihsinformatics.dynamicformsgenerator.data.utils.GlobalConstants;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.DateSelector;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.JSONUtils;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidgetProvider;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.listeners.OnValueChangeListener;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Form extends BaseActivity {
    public static final String PARAM_FORM_ID = "formId";
    private static String ENCOUNTER_NAME;
    private java.util.Date projectStartDate;
    private Date date25YearsAgo, today, date25YearsAhead, lastMonday, startDate, endDate, oneYearAgo;
    private static Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputWidgetProvider provider = InputWidgetProvider.getInstance();
        LinearLayout llMain = (LinearLayout) findViewById(R.id.llWidgetsHolder);

        this.questions = new ArrayList<>();
        this.options = new ArrayList<>();
        setLocale();
        initDates();

        Intent i = getIntent();
        if (DataProvider.directOpenableForms == null) {
            throw new UnsupportedOperationException("You need to set value of static variable REGISTRATION_ENCOUNTER in DataProvider class");
        }
        // PatientData patientData = (PatientData) i.getSerializableExtra(ParamNames.DATA);
        if (Global.patientData != null && !DataProvider.directOpenableForms.contains(ENCOUNTER_NAME)) {
            this.patientData = Global.patientData;

            try {
                parseQuestionsFromEncounterNameData();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (DataProvider.directOpenableForms.contains(ENCOUNTER_NAME)) {
            DataProvider dataProvider = DataProvider.getRefreshedInstance(this);
            this.questions = dataProvider.getQuestions(dataProvider.getFormId(ENCOUNTER_NAME));


        } else {


        }


        setTitle(ENCOUNTER_NAME);
        toolbar.setTitle(ENCOUNTER_NAME);


        boolean loadData = i.getBooleanExtra(GlobalConstants.KEY_LOAD_DATA, false);
        String data = i.getStringExtra(GlobalConstants.KEY_JSON_DATA);
        editFormId = i.getLongExtra(GlobalConstants.KEY_FORM_ID, -1);
        JSONArray jsonData = null;
        try {
            if (loadData) {
                JSONObject temp = new JSONObject(data);
                jsonData = temp.optJSONArray(ParamNames.SERVICE_HISTORY);
            }
        } catch (JSONException je) {
            jsonData = null;
            je.printStackTrace();
        }


        JSONUtils jsonUtils = JSONUtils.getInstance();
        JSONObject temp;
        InputWidgetBakery inputWidgetBakery = new InputWidgetBakery();
        for (final Question q : questions) {
            try {
                if (DataProvider.directOpenableForms != null && !DataProvider.directOpenableForms.contains(ENCOUNTER_NAME)) {
                    List<Option> optionsList = getOptionsByQuestionsID(q.getQuestionId());
                    q.setOptions(optionsList);
                }
                InputWidget w = inputWidgetBakery.bakeInputWidget(this, q);
                llMain.addView(w);

                if (loadData && jsonData != null) {
                    temp = jsonUtils.findJSONObjectInHYDRAJSONArray(q.getText(), jsonData);
                    if (temp != null && w != null) {

                        if (temp.optString(ParamNames.WIDGET_TYPE).equals(InputWidget.InputWidgetsType.WIDGETS_TYPE_CONTACT_TRACING.toString())) {
                            String answer = temp.get(ParamNames.VALUE).toString();
                            w.setAnswer(answer, "", LANGUAGE.URDU);
                        } else if (temp.optString(ParamNames.WIDGET_TYPE).equals(InputWidget.InputWidgetsType.WIDGETS_TYPE_NAME.toString())) {

                        } else {
                            String answer = temp.get(ParamNames.VALUE).toString();
                            w.setAnswer(answer, "", LANGUAGE.URDU);
                        }
                    }
                }

                if (DataProvider.directOpenableForms != null && !DataProvider.directOpenableForms.contains(ENCOUNTER_NAME)) {
                    w.setOnValueChangeListener(new OnValueChangeListener() {
                        @Override
                        public void onValueChanged(String newValue) throws JSONException {
                            checkSkipLogics(getFormId(ENCOUNTER_NAME));
                        }
                    });
                }

                w.setVisibility(q.getInitialVisibility());
                inputWidgets.put(w.getQuestionId(), w);
            } catch (Exception je) {
                je.printStackTrace();
            }
        }

        super.handleEncounterType();
    }

    public static String getENCOUNTER_NAME() {
        return ENCOUNTER_NAME;
    }

    public static void setENCOUNTER_NAME(String eNCOUNTER_NAME) {
        ENCOUNTER_NAME = eNCOUNTER_NAME;
    }


    private void parseQuestionsFromEncounterNameData() throws JSONException {

        int configurationID = 2;   // configurationID=1 is for address widget

        int idOfForm = getFormId(ENCOUNTER_NAME);
        QuestionConfiguration alphaNumeric150DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 150, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        QuestionConfiguration dateMaxTodayMinLastYear = new QuestionConfiguration(today, oneYearAgo, DateSelector.WIDGET_TYPE.DATE, 8);

        this.questions.add(new Question(true, idOfForm, 10002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", ParamNames.DATE_ENTERED_PARAM, dateMaxTodayMinLastYear, Question.PAYLOAD_TYPE.DATE_ENTERED));


        this.questions.add(new Question(true, idOfForm, 10000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Location", "location", null, Question.PAYLOAD_TYPE.LOCATION));
        this.options.addAll(DynamicOptions.getLocationOptionsFromDataAccessWithCountryName(this, 10000, null, null));

        this.questions.add(new Question(true, idOfForm, 10001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_GPS, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Geo Location", ParamNames.GPS_PARAM, alphaNumeric150DigitSpace, Question.PAYLOAD_TYPE.OBS));


        // this.questions.add(new Question(true, idOfForm, 10003, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_CONTACT_TRACING, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Contacts Of Patient", ParamNames.CONTACT_REGISTRY, alphaNumeric150DigitSpace, Question.PAYLOAD_TYPE.OBS));


        JSONArray formFieldsList = new JSONArray(getFormDataByEncounterType(ENCOUNTER_NAME));
        for (int i = 0; i < formFieldsList.length(); i++) {
            JSONObject formFields = formFieldsList.optJSONObject(i);

            int formFieldId = formFields.optInt("formFieldId");
            int displayOrder = formFields.optInt("displayOrder");  //Not mapped
            int minOccurrence = formFields.optInt("minOccurrence");  //Not mapped
            int maxOccurrence = formFields.optInt("maxOccurrence");  //Not mapped
            int minValue = formFields.optInt("minValue");
            int maxValue = formFields.optInt("maxValue");
            if (minValue == 0 && maxValue == 0) {
                minValue = Integer.MIN_VALUE;
                maxValue = Integer.MAX_VALUE;
            }
            int minLength = formFields.optInt("minLength");
            int maxLength = formFields.optInt("maxLength");
            int minSelections = formFields.optInt("minSelections");  //Not Mapped
            Boolean allowFutureDate = formFields.optBoolean("allowFutureDate");
            Boolean allowPastDate = formFields.optBoolean("allowPastDate");
            String displayText = formFields.optString("displayText");
            String errorMessage = formFields.optString("errorMessage"); //Not Mapped
            Boolean scoreable = formFields.optBoolean("scoreable");  //Not Mapped
            Boolean allowDecimal = formFields.optBoolean("allowDecimal");  //Not Mapped
            Boolean mandatory = formFields.optBoolean("mandatory");
            String initialVisibility = "Visible";

            String defaultValue = formFields.optString("defaultValue");
            String regix = formFields.optString("regix");  // Need to check this field Onc done
            String characters = formFields.optString("characters");

            characters = "0123456789 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,";  //hardcoded

            JSONObject field = formFields.optJSONObject("field");

            JSONObject fieldType = field.optJSONObject("fieldType");

            int formID = -1;

            if (field != null && !field.equals("null")) {
                formID = field.optInt("fieldId");
            } else {
                formID = formFieldId;
            }

            String isAttribute = field.optString("tableName");
            Boolean attribute = false;
            if (isAttribute != null && !isAttribute.equalsIgnoreCase("null") && !isAttribute.equalsIgnoreCase("") && !isAttribute.equals("")) {
                attribute = true;
            }

            JSONObject concept = field.optJSONObject("concept");
            String conceptUUID = field.optString("uuid");
            String inputType = field.optString("attributeName");//"text";
            if(inputType == null){
                inputType="text";
            }
            if (concept != null) {
                conceptUUID = concept.optString("uuid");


                JSONArray optionsList = field.optJSONArray("answers");

                for (int j = 0; j < optionsList.length(); j++) {
                    JSONObject option = optionsList.optJSONObject(j);
                    JSONObject optionConcept = option.optJSONObject("concept");
                    String optionUUID = optionConcept.optString("uuid");
                    String optionDisplay = optionConcept.optString("display");
                    this.options.add(new Option(formID, j, null, null, optionUUID, optionDisplay, -1));
                }

                /*JSONObject datatype = concept.optJSONObject("datatype");
                inputType = datatype.optString("display");*/
            }

            String widgetType = fieldType.optString("display");


            if (minLength <= 0) {
                minLength = 1;
            }

            if (maxLength <= 0) {
                maxLength = 10;
            }


            Configuration configuration = new QuestionConfiguration(
                    inputType, maxLength, minLength, characters, configurationID, maxValue, minValue);

            if (widgetType.equals("Address")) {

                QuestionConfiguration alphaNumeric160DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 160, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);

                AddressConfiguration.OpenAddressField openAddressField = new AddressConfiguration.OpenAddressField(
                        1,
                        "Address",
                        alphaNumeric160DigitSpace,
                        true,
                        ParamNames.ADDRESS2);
                AddressConfiguration.OpenAddressField openAddressField2 = new AddressConfiguration.OpenAddressField(
                        1,
                        "Nearest Landmark",
                        alphaNumeric160DigitSpace,
                        true,
                        ParamNames.ADDRESS3);
                List<AddressConfiguration.OpenAddressField> openAddressFields = new ArrayList<>();
                openAddressFields.add(openAddressField);
                openAddressFields.add(openAddressField2);
                configuration = new AddressConfiguration(
                        openAddressFields,
                        new AddressConfiguration.AddressTag(1, "Country"),
                        new AddressConfiguration.AddressTag(2, "Province/State"),
                        new AddressConfiguration.AddressTag(3, "City/Village"));
            } else if (widgetType.equals("Date/ Time Picker")) {

                startDate = today;
                endDate = today;
                regix = Validation.CHECK_FOR_DATE_TIME;


                if (allowFutureDate != null && allowFutureDate) {
                    endDate = date25YearsAhead;
                }
                if (allowPastDate != null && allowPastDate) {
                    startDate = date25YearsAgo;
                }

                configuration = new QuestionConfiguration(endDate, startDate, DateSelector.WIDGET_TYPE.DATE, 8);


            } else if (widgetType.equals("Contact Tracing")) {

                boolean createPatient = formFields.optBoolean("createPatient");
                JSONArray children = formFields.optJSONArray("children");


                ArrayList<ContactTraceChildFields> arr = new ArrayList<>();

                for (int j = 0; j < children.length(); j++) {

                    JSONObject childObject = children.optJSONObject(j);
                    JSONObject fieldChildren = childObject.optJSONObject("field");

                    String displayTextChildren = childObject.optString("displayText");

                    boolean mandatoryChildren = childObject.optBoolean("mandatory");
                    ;

                    if (displayTextChildren == null || displayTextChildren.equalsIgnoreCase("") || displayTextChildren.equalsIgnoreCase(" ") || displayTextChildren.equalsIgnoreCase("null")) {
                        displayTextChildren = fieldChildren.optString("name");
                    }

                    String childrenFieldId = fieldChildren.optString("uuid");

                    arr.add(new ContactTraceChildFields(childrenFieldId, displayTextChildren, mandatoryChildren));

                }

                configuration = new ContactTracingConfiguration(createPatient, arr);


            } else {
                if (allowDecimal != null && allowDecimal && inputType.equalsIgnoreCase("numeric")) {
                    characters = "1234567890.-+";
                    inputType = "decimalNumeric";
                }

                configuration = new QuestionConfiguration(
                        inputType, maxLength, minLength, characters, configurationID, maxValue, minValue);
                configurationID++;
            }


            //ToDo discuss about widgetType in wrong field
            // QuestionNumber, initialVisiblility , concept Name vs conceptParams and validation function


            JSONArray visibleWhenList = formFields.optJSONArray("visibleWhen");
            List<SExpression> visibleWhen = skipLogicParser(visibleWhenList);

            List<SExpression> hiddenWhen;
            String parsedRule = field.optString("parsedRule");
            if (null != parsedRule && !parsedRule.equals("") && !parsedRule.equals("null")) {
                JSONObject parsedRuleJSON = new JSONObject(parsedRule);
                JSONArray hiddenWhenList = parsedRuleJSON.optJSONArray("hiddenWhen");
                hiddenWhen = skipLogicParser(hiddenWhenList);
                if (hiddenWhen.size() > 0)
                    initialVisibility = "GONE";
            } else {
                JSONArray hiddenWhenList = formFields.optJSONArray("hiddenWhen");
                hiddenWhen = skipLogicParser(hiddenWhenList);
                if (hiddenWhen.size() > 0)
                    initialVisibility = "GONE";
            }

            List<AutoSelect> autoSelectWhen;
            if (null != parsedRule && !parsedRule.equals("") && !parsedRule.equals("null")) {
                JSONObject parsedRuleJSON = new JSONObject(parsedRule);
                JSONArray autoSelectList = parsedRuleJSON.optJSONArray("autoselectWhen");
                autoSelectWhen = autoSelectParser(autoSelectList);

            } else {
                JSONArray autoSelectList = formFields.optJSONArray("autoSelectWhen");
                autoSelectWhen = autoSelectParser(autoSelectList);
            }


            JSONArray requiredWhenList = formFields.optJSONArray("requiredWhen");
            List<SExpression> requiredWhen = skipLogicParser(requiredWhenList);

            if (displayText == null || displayText.equalsIgnoreCase("") || displayText.equalsIgnoreCase(" ") || displayText.equalsIgnoreCase("null")) {
                displayText = field.optString("name");
            }

            if (errorMessage == null || errorMessage.equalsIgnoreCase("null") || errorMessage.equalsIgnoreCase("") || errorMessage.equalsIgnoreCase(" ")) {
                errorMessage = "Invalid input";
            }

            Question completeQuestion = new Question(mandatory, getFormId(ENCOUNTER_NAME), formID, "*", widgetType, initialVisibility, Validation.CHECK_FOR_EMPTY, displayText, conceptUUID, configuration, attribute, inputType, errorMessage, visibleWhen, hiddenWhen, requiredWhen, autoSelectWhen);

            if (regix != null && !regix.equalsIgnoreCase("null")) {
                completeQuestion = new Question(mandatory, getFormId(ENCOUNTER_NAME), formID, "*", widgetType, initialVisibility, regix, displayText, conceptUUID, configuration, attribute, inputType, errorMessage, visibleWhen, hiddenWhen, requiredWhen, autoSelectWhen);
            }

            this.questions.add(completeQuestion);

        }
    }


    private List<AutoSelect> autoSelectParser(JSONArray autoSelectJson) throws JSONException {

        ArrayList<AutoSelect> autoSelectCompleteList = new ArrayList<AutoSelect>();

        if (autoSelectJson != null) {
            for (int n = 0; n < autoSelectJson.length(); n++) {
                AutoSelect autoSelect = new AutoSelect();
                JSONObject autoSelectObj = autoSelectJson.getJSONObject(n);
                autoSelect.setTargetFieldAnswer(autoSelectObj.getString("targetFieldAnswer"));
                autoSelect.setAutoSelectWhen(skipLogicParser(autoSelectObj.getJSONArray("when")));
                autoSelectCompleteList.add(autoSelect);
            }
        }
        return autoSelectCompleteList;

    }

    private List<SExpression> skipLogicParser(JSONArray sExpressionList) throws JSONException {
        ArrayList<SExpression> SExpressionCompleteList = new ArrayList<SExpression>();
        SExpression sExpression = new SExpression();
        if (sExpressionList != null) {
            for (int n = 0; n < sExpressionList.length(); n++) {
                Object obj = sExpressionList.get(n);


                if (obj instanceof String || obj.getClass().equals(String.class)) {
                    sExpression.setOperator(obj.toString());
                } else if (obj instanceof JSONObject || obj.getClass().equals(JSONObject.class)) {
                    JSONObject JSONObj = (JSONObject) obj;
                    SkipLogics s = new SkipLogics();
                    //TODO QuestionId must be a string ~Taha
                    String skiplogicID = JSONObj.optString("id");
                    int skiplogicQuestionId = JSONObj.optInt("questionId");

                    s.setId(skiplogicID);
                    s.setQuestionID(skiplogicQuestionId);

                    JSONArray skiplogicEqualList = JSONObj.optJSONArray("equals");
                    if (null != skiplogicEqualList && !skiplogicEqualList.equals("null"))
                        for (int o = 0; o < skiplogicEqualList.length(); o++) {
                            JSONObject optionUUIDObject = skiplogicEqualList.optJSONObject(o);
                            String optionUUID = optionUUIDObject.optString("uuid");

                            s.getEqualsList().add(o, optionUUID);
                        }

                    JSONArray skiplogicNotEqualList = JSONObj.optJSONArray("notEquals");
                    if (null != skiplogicNotEqualList && !skiplogicNotEqualList.equals("null"))
                        for (int o = 0; o < skiplogicNotEqualList.length(); o++) {
                            JSONObject optionUUIDObject = skiplogicNotEqualList.optJSONObject(o);
                            String optionUUID = optionUUIDObject.optString("uuid");

                            s.getNotEqualsList().add(o, optionUUID);
                        }

                    JSONArray skiplogicEqualsTo = JSONObj.optJSONArray("equalTo");
                    if (null != skiplogicEqualsTo && !skiplogicEqualsTo.equals("null"))
                        for (int o = 0; o < skiplogicEqualsTo.length(); o++) {
                            int optionWithNumbers = skiplogicEqualsTo.optInt(o);

                            s.getEqualsToList().add(o, optionWithNumbers);
                        }


                    JSONArray skiplogicNotEqualsTo = JSONObj.optJSONArray("notEqualTo");
                    if (null != skiplogicNotEqualsTo && !skiplogicNotEqualsTo.equals("null"))
                        for (int o = 0; o < skiplogicNotEqualsTo.length(); o++) {
                            int optionWithNumbers = skiplogicNotEqualsTo.optInt(o);

                            s.getNotEqualsToList().add(o, optionWithNumbers);
                        }

                    JSONArray skiplogicLessThan = JSONObj.optJSONArray("lessThan");
                    if (null != skiplogicLessThan && !skiplogicLessThan.equals("null"))
                        for (int o = 0; o < skiplogicLessThan.length(); o++) {
                            int optionWithNumbers = skiplogicLessThan.optInt(o);

                            s.getLessThanList().add(o, optionWithNumbers);
                        }

                    JSONArray skiplogicGreaterThan = JSONObj.optJSONArray("greaterThan");
                    if (null != skiplogicGreaterThan && !skiplogicGreaterThan.equals("null"))
                        for (int o = 0; o < skiplogicGreaterThan.length(); o++) {
                            int optionWithNumbers = skiplogicGreaterThan.optInt(o);

                            s.getGreaterThanList().add(o, optionWithNumbers);
                        }

                    sExpression.getSkipLogicsObjects().add(s);
                } else if (obj instanceof JSONArray || obj.getClass().equals(JSONArray.class)) {
                    sExpression.setSkipLogicsArray(skipLogicParser((JSONArray) obj));
                }


            }

            if (sExpressionList.length() > 0) {
                SExpressionCompleteList.add(sExpression);
            }
        }

        return SExpressionCompleteList;
    }


    public String getFormDataByEncounterType(String encounterType) {

        String result = Constants.getEncounterTypesData().get(encounterType);

        if (null != result) {
            return result;
        }
        return "[]";
    }


    public List<Option> getOptionsByQuestionsID(int questionId) {
        Enumeration<Option> localIterator = Collections.enumeration(this.options);
        List<Option> op = new ArrayList<>();
        for (; ; ) {
            if (!localIterator.hasMoreElements()) {
                return op;
            }
            Option localOption = (Option) localIterator.nextElement();
            if (localOption.getUuid() != null) {

                if (localOption.getQuestionId() == questionId)
                    op.add(localOption);
            }
        }
    }

    private void initDates() {
        try {
            projectStartDate = new SimpleDateFormat("yyyyMMdd").parse("20160601");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.set(Calendar.DAY_OF_WEEK, 2);
        lastMonday = localCalendar.getTime();
        cal = Calendar.getInstance();
        today = cal.getTime();
        cal.add(Calendar.YEAR, 25);
        date25YearsAhead = cal.getTime();
        cal.add(Calendar.YEAR, -50);
        date25YearsAgo = cal.getTime();
        cal.add(Calendar.YEAR, 24);
        oneYearAgo = cal.getTime();
    }


    private void setLocale() {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        android.content.res.Configuration configuration = resources.getConfiguration();

        if (Global.APP_LANGUAGE == null) {
            Global.APP_LANGUAGE = "en";
        }

        configuration.setLocale(new Locale(Global.APP_LANGUAGE.toLowerCase()));

        //resources.updateConfiguration(configuration,dm)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, getResources().getDisplayMetrics());
        }

    }
}
