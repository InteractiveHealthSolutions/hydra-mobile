package com.ihsinformatics.dynamicformsgenerator.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Toolbar;
import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.utils.GlobalConstants;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.JSONUtils;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidgetProvider;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.listeners.OnValueChangeListener;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class Form extends BaseActivity {
    public static final String PARAM_FORM_ID = "formId";
    private static String ENCOUNTER_NAME;

    // public static String REGISTRATION_ENCOUNTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputWidgetProvider provider = InputWidgetProvider.getInstance();
        LinearLayout llMain = (LinearLayout) findViewById(R.id.llWidgetsHolder);



        Intent i = getIntent();
        if (DataProvider.directOpenableForms == null ) {
            throw new UnsupportedOperationException("You need to set value of static variable REGISTRATION_ENCOUNTER in DataProvider class");
        }
        // PatientData patientData = (PatientData) i.getSerializableExtra(ParamNames.DATA);
        if (Global.patientData != null) {
            this.patientData = Global.patientData;
        } else if (DataProvider.directOpenableForms.contains(ENCOUNTER_NAME)) {

        } else {
           // Toasty.warning(this, "No record found", Toast.LENGTH_SHORT).show();
            // TODO finish();
            //return;
        }
        setTitle(ENCOUNTER_NAME);
        toolbar.setTitle(ENCOUNTER_NAME);

        // collapsingToolbar.setTitle(ENCOUNTER_NAME);

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
        final DataProvider dataProvider = DataProvider.getInstance(this);
        List<Question> questions = dataProvider.getQuestions(dataProvider.getFormId(ENCOUNTER_NAME));
        JSONUtils jsonUtils = JSONUtils.getInstance();
        JSONObject temp;
        InputWidgetBakery inputWidgetBakery = new InputWidgetBakery();
        for (final Question q : questions) {
            try {
                List<Option> o=dataProvider.getOptionsByQuestionsID(q.getQuestionId());
                q.setOptions(o);
                InputWidget w = inputWidgetBakery.bakeInputWidget(this, q);
                llMain.addView(w);

                if (loadData && jsonData != null) {
                    temp = jsonUtils.findJSONObjectInJSONArray(q.getParamName(), jsonData);
                    if (temp != null && w != null) {
                        String answer = temp.get(q.getParamName()).toString();
                        w.setAnswer(answer, "", LANGUAGE.URDU);
                    }
                }
                w.setOnValueChangeListener(new OnValueChangeListener() {
                    @Override
                    public void onValueChanged(String newValue) throws JSONException {
                        checkSkipLogics(dataProvider.getFormId(ENCOUNTER_NAME));
                    }
                });

                w.setVisibility(q.getInitialVisibility());
                inputWidgets.put(w.getQuestionId(), w);
            } catch (Exception je) {
                je.printStackTrace();
            }
        }

        //super.handleEncounterType();
    }

    public static String getENCOUNTER_NAME() {
        return ENCOUNTER_NAME;
    }

    public static void setENCOUNTER_NAME(String eNCOUNTER_NAME) {
        ENCOUNTER_NAME = eNCOUNTER_NAME;
    }
}
