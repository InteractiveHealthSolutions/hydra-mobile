package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Owais on 5/18/2018.
 */
public class AutoCompleteTextViewWidget extends InputWidget {
    protected AutoCompleteTextView etAnswer;
    private ArrayAdapter<String> mAdapter;
    ArrayList<String> dataList;
    HashMap<String, Option> mOptions;
    private boolean secureDeadLock = false;
    private QuestionConfiguration configuration;

    public AutoCompleteTextViewWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        etAnswer = (AutoCompleteTextView) findViewById(R.id.etAnswer);
        options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        if (options.size() > 0) {
            setOptionsOrHint(options.toArray(new Option[options.size()]));
        }
        if(super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;

        InputFilter[] filterArray;
        String allowedCharacters = configuration.getAllowedCharacters();
        if(allowedCharacters!=null) {
            filterArray = new InputFilter[2];
            filterArray[1] = DigitsKeyListener.getInstance(configuration.getAllowedCharacters());
        } else {
            filterArray = new InputFilter[1];
        }
        filterArray[0] = new InputFilter.LengthFilter(configuration.getMaxLength());

        etAnswer.setInputType(configuration.getInputType());
        etAnswer.setFilters(filterArray);
        etAnswer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                int pos = -1;
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).equals(selection)) {
                        pos = i;
                        break;
                    }
                }
                if (pos != -1)
                    handleShowHide(pos);
            }
        });
    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Option ans = mOptions.get(etAnswer.getText().toString());
        if(ans == null) return false;
        Validation validation = Validation.getInstance();
        if (configuration.getMinLenght() >= 0) {
            if (etAnswer.getText().toString().length() == 0 && !isMendatory) {
            } else if (etAnswer.getText().toString().length() < configuration.getMinLenght()) {
                return false;
            }
        }
        return validation.validate(etAnswer, question.getValidationFunction(), isMendatory);
    }

    @Override
    public void setOptionsOrHint(Option... data) {
        if (data.length > 0) {
            dataList = new ArrayList<>();
            mOptions = new HashMap<String, Option>();
            for (int i = 0; i < data.length; i++) {
                String translatedText = getTranslatedText(data[i].getText());
                if (translatedText != null) {
                    mOptions.put(translatedText, data[i]);
                    dataList.add(translatedText);
                } else {
                    mOptions.put(data[i].getText(), data[i]);
                    dataList.add(data[i].getText());
                }
            }
            mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dataList);
            etAnswer.setAdapter(mAdapter);
        }
    }

    private String getTranslatedText(String text) {
        return Translator.getInstance().Translate(text, GlobalPreferences.getinstance(context).findLanguagePrferenceValue());
    }


    // If a uuid is assigned to the option then it will send the uuid in answer otherwise the written text
    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();

        //Necessary for every widget to have PAYLOAD_TYPE AND PERSON_ATTRIBUTE
        param.put(ParamNames.PAYLOAD_TYPE, question.getPayload_type().toString());
        if(question.getAttribute())
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_TRUE);
        else
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_FALSE);


        if (isValidInput(question.isMandatory())) {
            if (etAnswer.getText().toString().length() == 0 && !question.isMandatory()) {
                param.put(question.getParamName(), null);
            } else {
                Option ans = mOptions.get(etAnswer.getText().toString());
                String uuid = ans.getUuid();
                if (uuid != null && !uuid.isEmpty()) {
                    param.put(question.getParamName(), ans.getUuid());
                } else {
                    param.put(question.getParamName(), ans.getText());
                }
            }
            dismissMessage();
        } else {
            activity.addValidationError(getQuestionId(), question.getErrorMessage());
        }
        return param;
    }

    @Override
    public void onFocusGained() {
        etAnswer.requestFocus();
    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
        etAnswer.setText(answer);
    }

    @Override
    public void setEnabled(boolean enabled) {
        etAnswer.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    private void handleShowHide(int position) {
        int[] showables = options.get(position).getOpensQuestions();
        int[] hideables = options.get(position).getHidesQuestions();
        ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables, question);
        if (!secureDeadLock) {
            etAnswer.setText(options.get(position).getText());
        } else {
            secureDeadLock = false;
        }
    }

    @Override
    public String getValue() {
        return etAnswer.getText().toString();
    }

    @Override
    public String getServiceHistoryValue() {
        return getValue();
    }
}

