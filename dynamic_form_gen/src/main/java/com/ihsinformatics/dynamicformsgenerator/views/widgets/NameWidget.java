package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.app.Service;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.RangeOption;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.utils.SkipRange;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;
import com.ihsinformatics.dynamicformsgenerator.utils.Regex;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NameWidget extends InputWidget implements TextWatcher {

    private EditText etFirstName;
    private EditText etLastName;

    private List<RangeOption> rangeOptions;
    private QuestionConfiguration configuration;
    private InputWidgetBakery widgetBakery;
    private LinearLayout llRepeatSpace;
    private List<Map<Integer, InputWidget>> repeatGroups;
    BaseActivity baseActivity;

    public NameWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        rangeOptions = new ArrayList<>(0);
        etFirstName = (EditText) findViewById(R.id.etFirstsName);
        etLastName = (EditText) findViewById(R.id.etLastName);

        InputFilter[] filterArray;
        String allowedCharacters = configuration.getAllowedCharacters();
        if (allowedCharacters != null) {
            filterArray = new InputFilter[2];
            filterArray[1] = DigitsKeyListener.getInstance(configuration.getAllowedCharacters());
        } else {
            filterArray = new InputFilter[1];
        }
        filterArray[0] = new InputFilter.LengthFilter(configuration.getMaxLength());

        etFirstName.setInputType(configuration.getInputType());
        etFirstName.setFilters(filterArray);

        etLastName.setInputType(configuration.getInputType());
        etLastName.setFilters(filterArray);


        etFirstName.addTextChangedListener(this);
        widgetBakery = new InputWidgetBakery();
        if (question.getRepeatables() != null && question.getRepeatables().size() > 0) {
            llRepeatSpace = findViewById(R.id.llRepeats);
            repeatGroups = new ArrayList<>();
        }

        baseActivity = ((BaseActivity) getContext());
    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Validation validation = Validation.getInstance();
        if (configuration.getMinLenght() >= 0) {
            if (etFirstName.getText().toString().length() == 0 && !isMendatory) {
            } else if (etFirstName.getText().toString().length() < configuration.getMinLenght()) {
                return false;
            }
            if (etLastName.getText().toString().length() == 0 && !isMendatory) {
            } else if (etLastName.getText().toString().length() < configuration.getMinLenght()) {
                return false;
            }
        }

        return
                validation.validate(etFirstName, question.getValidationFunction(), isMendatory)
                        && validation.validate(etLastName, question.getValidationFunction(), isMendatory);
    }

    @Override
    public void setOptionsOrHint(Option... data) {

    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();
        //Necessary for every widget to have PAYLOAD_TYPE AND PERSON_ATTRIBUTE

        param.put(ParamNames.PAYLOAD_TYPE, Question.PAYLOAD_TYPE.NAME);
        if(question.getAttribute())
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_TRUE);
        else
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_FALSE);

        if (isValidInput(question.isMandatory())) {
            JSONObject givenNameObj = new JSONObject();
            param.put(ParamNames.GIVEN_NAME, etFirstName.getText().toString());

            JSONObject familyNameObj = new JSONObject();
            param.put(ParamNames.FAMILY_NAME, etLastName.getText().toString());

            JSONArray names = new JSONArray();
            names.put(givenNameObj);
            names.put(familyNameObj);
            dismissMessage();
        } else {
            if (etFirstName.getText().toString().length() == 0 || etLastName.getText().toString().length() == 0) {
                activity.addValidationError(getQuestionId(), "Required Field");

            } else {
                activity.addValidationError(getQuestionId(), question.getErrorMessage());
            }
        }

        return param;
    }

    public String getValue() {
        return etFirstName.getText().toString() + " " + etLastName.getText().toString();
    }

    @Override
    public void onFocusGained() {
        etFirstName.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etFirstName.getWindowToken(), 0);
        imm.showSoftInput(etFirstName, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void setAnswer(String answer, String uuid, LANGUAGE language) {

        String[] allAnswers = answer.split("\n\n");
        etFirstName.setText(allAnswers[0]);
        etLastName.setText(allAnswers[1]);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (onValueChangeListener != null) {
            try {
                onValueChangeListener.onValueChanged(s.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String newValue = s.toString();
        if (rangeOptions != null && rangeOptions.size() > 0) {
            try {
                for (RangeOption rangeOption : rangeOptions) {
                    int[] showables = rangeOption.getOpensQuestions();
                    int[] hideables = rangeOption.getHidesQuestions();
                    if (newValue.equals("") && !SkipRange.VALIDATION_TYPE.IS_EMPTY.equals(rangeOption.getSkipRange().getValidationType())) {
                        return;
                    } else if (SkipRange.VALIDATION_TYPE.IS_EMPTY.equals(rangeOption.getSkipRange().getValidationType())) {
                        if (rangeOption.getSkipRange().validateEmpty(newValue)) {
                            baseActivity.onChildViewItemSelected(showables, hideables, question);
                        } else {
                            baseActivity.onChildViewItemSelected(hideables, showables, question);
                        }
                    } else {
                        int value = Integer.parseInt(newValue);
                        if (rangeOption.getSkipRange().validate(value)) {
                            baseActivity.onChildViewItemSelected(showables, hideables, question);
                        } else {
                            baseActivity.onChildViewItemSelected(hideables, showables, question);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                Logger.log(e);
            }
        }

        if (question.getRepeatables() != null && question.getRepeatables().size() > 0) {
            if (!newValue.matches(Regex.NUMERIC.toString())) return;

            int repeatTimes = Integer.parseInt(newValue);
            // removing references from parent, clearing local references, and removing from the layout
            reset();
            Map<Integer, InputWidget> childWidgets;
            int uniqueId = 99999;

            for (int i = 0; i < repeatTimes; i++) {
                childWidgets = new HashMap<>();
                InputWidget headingWidget = null;
                try {
                    headingWidget = createHeading(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                llRepeatSpace.addView(headingWidget);

                for (Question q : question.getRepeatables()) {
                    try {
                        q = (Question) q.clone();
                    } catch (CloneNotSupportedException e) {
                        continue;
                    }

                    uniqueId = (99999 * (i + 1)) + q.getQuestionId();
                    q.setQuestionId(uniqueId);
                    q.setRuntimeGenerated(true);
                    q.updateSkipLogicReferences(99999, (i + 1));
                    InputWidget inputWidget = null;
                    try {
                        inputWidget = widgetBakery.bakeInputWidget(context, q);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    childWidgets.put(uniqueId, inputWidget);
                    baseActivity.addRunTimeWidgetReference(uniqueId, inputWidget);
                    // adding in linear layout
                    llRepeatSpace.addView(inputWidget);
                }

                repeatGroups.add(childWidgets);
            }
        }
    }

    private InputWidget createHeading(int i) throws JSONException {
        Question q = (new Question(
                false,
                question.getFormTypeId(),
                -1,
                "-1",
                InputWidgetsType.WIDGET_TYPE_HEADING,
                View.VISIBLE, null,
                question.getRepeatGroupHeadingPrefix() + " " + (i + 1), null, null, Question.PAYLOAD_TYPE.HEADING)); // TODO get the heading form question or question config class
        InputWidget headingWidget = widgetBakery.bakeInputWidget(context, q);

        return headingWidget;
    }

    private void reset() {
        // removing references from parent, and clearing local references, removing from the layout
        llRepeatSpace.removeAllViews();
        for (Map w : repeatGroups) {
            Iterator<Integer> it = w.keySet().iterator();
            while (it.hasNext()) baseActivity.removeRuntimeWidgetReference(it.next());
        }

        repeatGroups.clear();
    }

    @Override
    public void setEnabled(boolean enabled) {
        etFirstName.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    public EditText getInputField() {
        return etFirstName;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    public String getUnValidatedValue() {
        return etFirstName.getText().toString();
    }

    @Override
    public String getServiceHistoryValue() {

        return etFirstName.getText().toString() + "\n\n" + etLastName.getText().toString();

    }
}
