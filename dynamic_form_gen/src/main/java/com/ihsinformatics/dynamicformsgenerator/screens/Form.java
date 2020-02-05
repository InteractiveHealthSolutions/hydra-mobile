package com.ihsinformatics.dynamicformsgenerator.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.common.Constants;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SExpression;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SkipLogics;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.AddressConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.Configuration;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormType;
import com.ihsinformatics.dynamicformsgenerator.data.utils.GlobalConstants;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.DateSelector;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.JSONUtils;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidgetProvider;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.listeners.OnValueChangeListener;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class Form extends BaseActivity {
    public static final String PARAM_FORM_ID = "formId";
    private static String ENCOUNTER_NAME;
    private java.util.Date projectStartDate;
    private Date date25YearsAgo,today,date25YearsAhead,lastMonday,startDate,endDate,oneYearAgo;
    private static Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputWidgetProvider provider = InputWidgetProvider.getInstance();
        LinearLayout llMain = (LinearLayout) findViewById(R.id.llWidgetsHolder);

        this.questions = new ArrayList<>();
        this.options = new ArrayList<>();

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
            DataProvider dataProvider = DataProvider.getInstance(this);
            this.questions = dataProvider.getQuestions(dataProvider.getFormId(ENCOUNTER_NAME));


        } else {


        }


        setTitle(ENCOUNTER_NAME);
        toolbar.setTitle(ENCOUNTER_NAME);


        boolean loadData = i.getBooleanExtra(GlobalConstants.KEY_LOAD_DATA, false);
        String data = i.getStringExtra(GlobalConstants.KEY_JSON_DATA);
        JSONArray jsonData = null;
        try {
            if (loadData) {
                jsonData = new JSONArray(data);
                btnSave.setVisibility(View.GONE);
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
                    temp = jsonUtils.findJSONObjectInJSONArray(q.getParamName(), jsonData);
                    if (temp != null && w != null) {
                        String answer = temp.get(q.getParamName()).toString();
                        w.setAnswer(answer, "", LANGUAGE.URDU);
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

        int idOfForm=getFormId(ENCOUNTER_NAME);
        QuestionConfiguration alphaNumeric150DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 150, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        QuestionConfiguration dateMaxTodayMinLastYear = new QuestionConfiguration(today, oneYearAgo, DateSelector.WIDGET_TYPE.DATE, 8);


        this.questions.add(new Question(true, idOfForm, 1000000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Location", "location", null,Question.PAYLOAD_TYPE.LOCATION));
        this.options.add(new Option(1000000, 2237, null, null, "", "Bedford Hospital", -1));
        this.options.add(new Option(1000000, 2237, null, null, "", "Frere Clinic", -1));

        this.questions.add(new Question(true, idOfForm, 1000001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_GPS, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Geo Location", ParamNames.GPS_PARAM, alphaNumeric150DigitSpace));



        this.questions.add(new Question(true, 1000002, 6008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", ParamNames.DATE_ENTERED_PARAM , dateMaxTodayMinLastYear,Question.PAYLOAD_TYPE.DATE_ENTERED));


        JSONArray formFieldsList = new JSONArray(getFormDataByEncounterType(ENCOUNTER_NAME));
        for (int i = 0; i < formFieldsList.length(); i++) {
            JSONObject formFields = formFieldsList.optJSONObject(i);

            int formFieldId = formFields.optInt("formFieldId");
            int displayOrder = formFields.optInt("displayOrder");  //Not mapped
            int minOccurrence = formFields.optInt("minOccurrence");  //Not mapped
            int maxOccurrence = formFields.optInt("maxOccurrence");  //Not mapped
            int minValue = formFields.optInt("minValue");
            int maxValue = formFields.optInt("maxValue");
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

            String defaultValue = formFields.optString("defaultValue");
            String regix = formFields.optString("regix");  // Need to check this field Onc done
            String characters = formFields.optString("characters");

            characters="0123456789 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,";  //hardcoded

            JSONObject field = formFields.optJSONObject("field");

            JSONObject fieldType = field.optJSONObject("fieldType");

            String isAttribute=  field.optString("tableName");
            Boolean attribute=false;
            if(isAttribute!=null && !isAttribute.equalsIgnoreCase("null") && !isAttribute.equalsIgnoreCase("") && !isAttribute.equals(""))
            {
                attribute=true;
            }

            JSONObject concept = field.optJSONObject("concept");
            String conceptUUID = field.optString("uuid");
            String inputType = "text";
            if (concept != null) {
                conceptUUID = concept.optString("uuid");


                JSONArray optionsList = field.optJSONArray("answers");

                for (int j = 0; j < optionsList.length(); j++) {
                    JSONObject option = optionsList.optJSONObject(j);
                    JSONObject optionConcept=option.optJSONObject("concept");
                    String optionUUID = optionConcept.optString("uuid");
                    String optionDisplay = optionConcept.optString("display");
                    this.options.add(new Option(formFieldId, j, null, null, optionUUID, optionDisplay, -1));
                }

                JSONObject datatype = concept.optJSONObject("datatype");
                inputType = datatype.optString("display");
            }

            String widgetType = fieldType.optString("display");


            if(minLength<=0)
            {
                minLength=1;
            }

            if(maxLength<=0)
            {
                maxLength=10;
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
                List<AddressConfiguration.OpenAddressField> openAddressFields = new ArrayList<>();
                openAddressFields.add(openAddressField);
                configuration = new AddressConfiguration(
                        openAddressFields,
                        new AddressConfiguration.AddressTag(1, "Province/State"),
                        new AddressConfiguration.AddressTag(3, "City/Village"));
            } else if (widgetType.equals("Date/ Time Picker")) {

                startDate=today;
                endDate=today;
                regix=Validation.CHECK_FOR_DATE_TIME;


                if(allowFutureDate!=null && allowFutureDate){
                    endDate=date25YearsAhead;
                }
                if(allowPastDate!=null && allowPastDate){
                    startDate=date25YearsAgo;
                }

                configuration = new QuestionConfiguration(endDate,startDate, DateSelector.WIDGET_TYPE.DATE, 8);



            } else {
                if(allowDecimal!=null && allowDecimal)
                {
                    characters=characters+".";
                }

                configuration = new QuestionConfiguration(
                        inputType, maxLength, minLength, characters, configurationID, maxValue, minValue);
                configurationID++;
            }


            //ToDo discuss about widgetType in wrong field
            // QuestionNumber, initialVisiblility , concept Name vs conceptParams and validation function


            JSONArray visibleWhenList = formFields.optJSONArray("visibleWhen");
            List<SExpression> visibleWhen = skipLogicParser(visibleWhenList);

            JSONArray hiddenWhenList = formFields.optJSONArray("hiddenWhen");
            List<SExpression> hiddenWhen = skipLogicParser(hiddenWhenList);

            JSONArray requiredWhenList = formFields.optJSONArray("requiredWhen");
            List<SExpression> requiredWhen = skipLogicParser(requiredWhenList);

            if(displayText==null || displayText.equalsIgnoreCase("") || displayText.equalsIgnoreCase(" ") || displayText.equalsIgnoreCase("null")){
                displayText=field.optString("name");
            }

            if(errorMessage==null || errorMessage.equalsIgnoreCase("null") || errorMessage.equalsIgnoreCase("") || errorMessage.equalsIgnoreCase(" ")){
                errorMessage="Invalid input";
            }

            Question completeQuestion = new Question(mandatory, getFormId(ENCOUNTER_NAME), formFieldId, "*", widgetType, "Visible", Validation.CHECK_FOR_EMPTY, displayText, conceptUUID, configuration,attribute,inputType,errorMessage ,visibleWhen, hiddenWhen, requiredWhen);

            if(regix!=null && !regix.equalsIgnoreCase("null")) {
                completeQuestion = new Question(mandatory, getFormId(ENCOUNTER_NAME), formFieldId, "*", widgetType, "Visible", regix, displayText, conceptUUID, configuration,attribute,inputType, errorMessage,visibleWhen, hiddenWhen, requiredWhen);
            }

            this.questions.add(completeQuestion);

        }
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
                    for (int o = 0; o < skiplogicEqualList.length(); o++) {
                        JSONObject optionUUIDObject = skiplogicEqualList.optJSONObject(o);
                        String optionUUID = optionUUIDObject.optString("uuid");

                        s.getEqualsList().add(o, optionUUID);
                    }

                    JSONArray skiplogicNotEqualList = JSONObj.optJSONArray("notEquals");
                    for (int o = 0; o < skiplogicNotEqualList.length(); o++) {
                        JSONObject optionUUIDObject = skiplogicNotEqualList.optJSONObject(o);
                        String optionUUID = optionUUIDObject.optString("uuid");

                        s.getNotEqualsList().add(o, optionUUID);
                    }

                    JSONArray skiplogicEqualsTo = JSONObj.optJSONArray("equalTo");
                    for (int o = 0; o < skiplogicEqualsTo.length(); o++) {
                        int optionWithNumbers = skiplogicEqualsTo.optInt(o);

                        s.getEqualsToList().add(o, optionWithNumbers);
                    }

                    JSONArray skiplogicNotEqualsTo = JSONObj.optJSONArray("notEqualTo");
                    for (int o = 0; o < skiplogicNotEqualsTo.length(); o++) {
                        int optionWithNumbers = skiplogicNotEqualsTo.optInt(o);

                        s.getNotEqualsToList().add(o, optionWithNumbers);
                    }

                    JSONArray skiplogicLessThan = JSONObj.optJSONArray("lessThan");
                    for (int o = 0; o < skiplogicLessThan.length(); o++) {
                        int optionWithNumbers = skiplogicLessThan.optInt(o);

                        s.getLessThanList().add(o, optionWithNumbers);
                    }

                    JSONArray skiplogicGreaterThan = JSONObj.optJSONArray("greaterThan");
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
        cal.add(Calendar.YEAR, -25);
        date25YearsAgo = cal.getTime();
        cal.add(Calendar.YEAR, 25);
        date25YearsAhead = cal.getTime();
        cal.add(Calendar.YEAR, -1);
        oneYearAgo=cal.getTime();
    }

}
