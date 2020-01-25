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
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormType;
import com.ihsinformatics.dynamicformsgenerator.data.utils.GlobalConstants;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Form extends BaseActivity {
    public static final String PARAM_FORM_ID = "formId";
    private static String ENCOUNTER_NAME;
    // private static String ENCOUNTER_NAME_DATA;


    // public static String REGISTRATION_ENCOUNTER;

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

//    public static void setENCOUNTER_NAME_DATA(String eNCOUNTER_NAME_DATA) {
//        ENCOUNTER_NAME_DATA = eNCOUNTER_NAME_DATA;
//    }


    private void parseQuestionsFromEncounterNameData() throws JSONException {


        JSONArray questionsList = new JSONArray(getFormDataByEncounterType(ENCOUNTER_NAME));
        for (int i = 0; i < questionsList.length(); i++) {
            JSONObject question = questionsList.getJSONObject(i);

            JSONObject config = question.getJSONObject("config");
            int id = config.getInt("id");
            String inputType = config.getString("inputType");
            String keyboardCharacters = config.getString("keyboardCharacters");
            String widgetType = config.getString("widgetType");
            int minLength = config.getInt("minLength");
            int maxLength = config.getInt("maxLength");
            int minValue = config.getInt("minValue");
            int maxValue = config.getInt("maxValue");
            String minDate = config.getString("minDate");
            String maxDate = config.getString("maxDate");
            int maxLines = config.getInt("maxLines");

            QuestionConfiguration configuration = new QuestionConfiguration(
                    inputType, maxLength, minLength, keyboardCharacters, id, maxValue, minValue, maxDate, minDate, maxLines);

            int questionId = question.getInt("id");
            String questionNumber = question.getString("questionNumber");
            String description = question.getString("description");
            String conceptName = question.getString("conceptName");
            String concetUUID = question.getString("concetUUID");
            String initialVisibility = question.getString("initialVisibility");


            Boolean required = false;
            if (question.getInt("required") == 0) {
                required = false;
            } else {
                required = true;
            }
            //ToDo discuss about widgetType in wrong field
            // QuestionNumber, initialVisiblility and required types, concept Name vs conceptParams and validation function
            // Change edittext to proper widget type  ~Taha


            JSONArray optionsList = question.getJSONArray("options");
            for (int j = 0; j < optionsList.length(); j++) {
                JSONObject option = optionsList.getJSONObject(j);
                int defaultValue = option.getInt("default");
                String optionConceptUUID = option.getString("conceptUUID");
                String display = option.getString("display");
                this.options.add(new Option(questionId, j, null, null, optionConceptUUID, display, -1));
            }

            JSONArray visibleWhenList = question.getJSONArray("visibleWhen");
            List<SExpression> visibleWhen = skipLogicParser(visibleWhenList);

            JSONArray hiddenWhenList = question.getJSONArray("hiddenWhen");
            List<SExpression> hiddenWhen = skipLogicParser(hiddenWhenList);

            JSONArray requiredWhenList = question.getJSONArray("requiredWhen");
            List<SExpression> requiredWhen = skipLogicParser(requiredWhenList);

            //TODO replace validation function with regrex
            Question completeQuestion = new Question(required, getFormId(ENCOUNTER_NAME), questionId, questionNumber, widgetType, initialVisibility, Validation.CHECK_FOR_EMPTY, description, conceptName, configuration, visibleWhen, hiddenWhen, requiredWhen);
            this.questions.add(completeQuestion);

        }
    }


    private List<SExpression> skipLogicParser(JSONArray sExpressionList) throws JSONException {
        ArrayList<SExpression> SExpressionCompleteList = new ArrayList<SExpression>();
        SExpression sExpression = new SExpression();
        for (int n = 0; n < sExpressionList.length(); n++) {
            Object obj = sExpressionList.get(n);


            if (obj instanceof String || obj.getClass().equals(String.class)) {
                sExpression.setOperator(obj.toString());
            } else if (obj instanceof JSONObject || obj.getClass().equals(JSONObject.class)) {
                JSONObject JSONObj = (JSONObject) obj;
                SkipLogics s = new SkipLogics();
                //TODO QuestionId must be a string ~Taha
                String skiplogicID = JSONObj.getString("id");
                int skiplogicQuestionId = JSONObj.getInt("questionId");

                s.setId(skiplogicID);
                s.setQuestionID(skiplogicQuestionId);

                JSONArray skiplogicEqualList = JSONObj.getJSONArray("equals");
                for (int o = 0; o < skiplogicEqualList.length(); o++) {
                    JSONObject optionUUIDObject = skiplogicEqualList.getJSONObject(o);
                    String optionUUID = optionUUIDObject.getString("uuid");

                    s.getEqualsList().add(o, optionUUID);
                }

                JSONArray skiplogicNotEqualList = JSONObj.getJSONArray("notEquals");
                for (int o = 0; o < skiplogicNotEqualList.length(); o++) {
                    JSONObject optionUUIDObject = skiplogicNotEqualList.getJSONObject(o);
                    String optionUUID = optionUUIDObject.getString("uuid");

                    s.getNotEqualsList().add(o, optionUUID);
                }

                JSONArray skiplogicEqualsTo = JSONObj.getJSONArray("equalTo");
                for (int o = 0; o < skiplogicEqualsTo.length(); o++) {
                    int optionWithNumbers = skiplogicEqualsTo.getInt(o);

                    s.getEqualsToList().add(o, optionWithNumbers);
                }

                JSONArray skiplogicNotEqualsTo = JSONObj.getJSONArray("notEqualTo");
                for (int o = 0; o < skiplogicNotEqualsTo.length(); o++) {
                    int optionWithNumbers = skiplogicNotEqualsTo.getInt(o);

                    s.getNotEqualsToList().add(o, optionWithNumbers);
                }

                JSONArray skiplogicLessThan = JSONObj.getJSONArray("lessThan");
                for (int o = 0; o < skiplogicLessThan.length(); o++) {
                    int optionWithNumbers = skiplogicLessThan.getInt(o);

                    s.getLessThanList().add(o, optionWithNumbers);
                }

                JSONArray skiplogicGreaterThan = JSONObj.getJSONArray("greaterThan");
                for (int o = 0; o < skiplogicGreaterThan.length(); o++) {
                    int optionWithNumbers = skiplogicGreaterThan.getInt(o);

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
        return SExpressionCompleteList;
    }


    public int getFormId(String paramString) {
        int index;
        FormType formType;
        Iterator<Integer> it = Constants.getInstance().getEncounterTypes().keySet().iterator();
        while (it.hasNext()) {
            index = it.next();
            if (Constants.getInstance().getEncounterTypes().get(index).equals(paramString)) {
                return index;
            }
        }
        return -1;
    }

    public String getFormDataByEncounterType(String encounterType) {

        String result = Constants.getInstance().getEncounterTypesData().get(encounterType);

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

}
