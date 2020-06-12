package com.ihsinformatics.dynamicformsgenerator.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.utils.GlobalConstants;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.views.SavedFormQuestionView;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class FormDataDisplayActivity extends AppCompatActivity {
    private DataProvider dataProvider;
    private LinearLayout llMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_data_display);
        dataProvider = DataProvider.getInstance(this);
        Intent i = getIntent();
        llMain = (LinearLayout) findViewById(R.id.llMain);
        String formData = i.getStringExtra(GlobalConstants.KEY_JSON_DATA);
        if (formData == null) {
            finish();
        }
        try {
            Question q;
            JSONArray array = new JSONArray(formData);
            String formName = (String) getFormNameByKey(array, ParamNames.REQUEST_TYPE);
            for (int j = 0; j < array.length(); j++) {
                JSONObject obj = array.getJSONObject(j);
                Iterator<String> keys = obj.keys();
                String paramName;
                while (keys.hasNext()) {
                    paramName = keys.next();
                    //q = dataProvider.getQuestionByParamName(paramName);
                    q = dataProvider.getQuestionByParam(paramName, formName);
                    if (q != null)
                        if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER) {
                            String optionText = obj.get(paramName).toString();
                            String optionTextByUUID = dataProvider.getOptionsByUUID(optionText, q.getQuestionId());
                            llMain.addView(new SavedFormQuestionView(this, q.getText(), optionTextByUUID != null ? optionTextByUUID : optionText));
                        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER) {
                            try {
                                String answer = obj.get(paramName).toString().replaceAll("[\\[\\]{}\"]", "");
                                llMain.addView(new SavedFormQuestionView(this, q.getText(), answer));
                            } catch (Exception e) {
                                Logger.log(e);
                            }
                        } else {
                            llMain.addView(new SavedFormQuestionView(this, q.getText(), obj.get(paramName).toString()));
                        }
                }
            }
        } catch (JSONException e) {
            Logger.log(e);
        }
    }

    private Object getFormNameByKey(JSONArray array, String key) {
        Object value = null;
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = null;
            try {
                item = array.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Iterator<String> keys = item.keys();
            while (keys.hasNext()) {
                String paramName = keys.next();
                if (paramName.equals(key)) {
                    try {
                        value = item.get(paramName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return value;
    }
}
