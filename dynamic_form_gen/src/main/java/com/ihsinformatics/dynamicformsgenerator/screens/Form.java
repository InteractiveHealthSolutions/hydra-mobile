package com.ihsinformatics.dynamicformsgenerator.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Toolbar;

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
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData;
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

import es.dmoral.toasty.Toasty;

public class Form extends BaseActivity {
    public static final String PARAM_FORM_ID = "formId";
    private static String ENCOUNTER_NAME;
    private java.util.Date projectStartDate;
    private Date date25YearsAgo,today,date25YearsAhead,lastMonday,startDate,endDate;
    private static Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputWidgetProvider provider = InputWidgetProvider.getInstance();
        LinearLayout llMain = (LinearLayout) findViewById(R.id.llWidgetsHolder);

        this.questions = new ArrayList<>();
        this.options = new ArrayList<>();

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


    // Only changing the name of parser above wont work because Question class et types accordingly
   /* private void oldParseQuestionsFromEncounterNameData() throws JSONException {


        JSONArray questionsList = new JSONArray(getFormDataByEncounterType(ENCOUNTER_NAME));
        for (int i = 0; i < questionsList.length(); i++) {
            JSONObject question = questionsList.optJSONObject(i);

            JSONObject config = question.optJSONObject("config");
            int id = config.optInt("id");
            String inputType = config.optString("inputType");
            String keyboardCharacters = config.optString("keyboardCharacters");
            String widgetType = config.optString("widgetType");
            int minLength = config.optInt("minLength");
            int maxLength = config.optInt("maxLength");
            int minValue = config.optInt("minValue");
            int maxValue = config.optInt("maxValue");
            String minDate = config.optString("minDate");
            String maxDate = config.optString("maxDate");
            int maxLines = config.optInt("maxLines");

            Configuration configuration;
            if (widgetType.equals("address")) {
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
            } else {
                configuration = new QuestionConfiguration(
                        inputType, maxLength, minLength, keyboardCharacters, id, maxValue, minValue);//, maxDate, minDate, maxLines);
            }
            int questionId = question.optInt("id");
            String questionNumber = question.optString("questionNumber");
            String description = question.optString("description");
            String conceptName = question.optString("conceptName");
            String concetUUID = question.optString("concetUUID");
            String initialVisibility = question.optString("initialVisibility");


            Boolean required = false;
            if (question.optInt("required") == 0) {
                required = false;
            } else {
                required = true;
            }
            //ToDo discuss about widgetType in wrong field
            // QuestionNumber, initialVisiblility and required types, concept Name vs conceptParams and validation function
            // Change edittext to proper widget type  ~Taha


            JSONArray optionsList = question.optJSONArray("options");
            for (int j = 0; j < optionsList.length(); j++) {
                JSONObject option = optionsList.optJSONObject(j);
                int defaultValue = option.optInt("default");
                String optionConceptUUID = option.optString("conceptUUID");
                String display = option.optString("display");
                this.options.add(new Option(questionId, j, null, null, optionConceptUUID, display, -1));
            }

            JSONArray visibleWhenList = question.optJSONArray("visibleWhen");
            List<SExpression> visibleWhen = skipLogicParser(visibleWhenList);

            JSONArray hiddenWhenList = question.optJSONArray("hiddenWhen");
            List<SExpression> hiddenWhen = skipLogicParser(hiddenWhenList);

            JSONArray requiredWhenList = question.optJSONArray("requiredWhen");
            List<SExpression> requiredWhen = skipLogicParser(requiredWhenList);

            //TODO replace validation function with regrex
            Question completeQuestion = new Question(required, getFormId(ENCOUNTER_NAME), questionId, questionNumber, widgetType, initialVisibility, Validation.CHECK_FOR_EMPTY, description, conceptName, configuration, visibleWhen, hiddenWhen, requiredWhen);

            this.questions.add(completeQuestion);

        }
    }   //Old frm parser  */


    private void parseQuestionsFromEncounterNameData() throws JSONException {

        int configurationID = 2;   // configurationID=1 is for address widget
        initDates();
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

            characters="0123456789. abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,   ";  //hardcoded

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


            Question completeQuestion = new Question(mandatory, getFormId(ENCOUNTER_NAME), formFieldId, "*", widgetType, "Visible", Validation.CHECK_FOR_EMPTY, displayText, conceptUUID, configuration,attribute,inputType, visibleWhen, hiddenWhen, requiredWhen);

            if(regix!=null && !regix.equalsIgnoreCase("null")) {
                completeQuestion = new Question(mandatory, getFormId(ENCOUNTER_NAME), formFieldId, "*", widgetType, "Visible", regix, displayText, conceptUUID, configuration,attribute,inputType, visibleWhen, hiddenWhen, requiredWhen);
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

    }

}
