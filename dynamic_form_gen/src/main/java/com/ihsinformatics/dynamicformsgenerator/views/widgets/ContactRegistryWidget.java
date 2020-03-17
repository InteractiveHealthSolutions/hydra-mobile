package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import com.ihsinformatics.dynamicformsgenerator.data.core.options.DateOption;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.RangeOption;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.utils.SkipRange;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.DateSelector;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;
import com.ihsinformatics.dynamicformsgenerator.utils.Regex;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ContactRegistryWidget extends InputWidget implements TextWatcher {

    private EditText etContactID;
    private EditText etContactName;


    private EditText etAnswer;
    private EditText etAgeYears;
    private EditText etAgeMonths;
    private EditText etAgeDays;
    private DateOption dateOption;
    private Period period;
    private boolean isSetAnswerFromOnCreate = false;
    /*private boolean isSetAnswerAge = false;
    private boolean isFromSetAnswer = false;*/
    private boolean deadLock = false;
    private String dateString;


    private List<RangeOption> rangeOptions;
    private QuestionConfiguration configuration;
    private InputWidgetBakery widgetBakery;
    private LinearLayout llRepeatSpace;
    private List<Map<Integer, InputWidget>> repeatGroups;
    BaseActivity baseActivity;

    public ContactRegistryWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        rangeOptions = new ArrayList<>(0);
        etContactID = (EditText) findViewById(R.id.etPatientID);
        etContactName = (EditText) findViewById(R.id.etPatientName);

        InputFilter[] filterArray;
        String allowedCharacters = configuration.getAllowedCharacters();
        if (allowedCharacters != null) {
            filterArray = new InputFilter[2];
            filterArray[1] = DigitsKeyListener.getInstance(configuration.getAllowedCharacters());
        } else {
            filterArray = new InputFilter[1];
        }
        filterArray[0] = new InputFilter.LengthFilter(configuration.getMaxLength());

        etContactID.setInputType(configuration.getInputType());
        etContactID.setFilters(filterArray);

        etContactName.setInputType(configuration.getInputType());
        etContactName.setFilters(filterArray);


        etContactID.addTextChangedListener(this);
        widgetBakery = new InputWidgetBakery();
        if (question.getRepeatables() != null && question.getRepeatables().size() > 0) {
            llRepeatSpace = findViewById(R.id.llRepeats);
            repeatGroups = new ArrayList<>();
        }

        baseActivity = ((BaseActivity) getContext());


        etAnswer = findViewById(R.id.etAnswer);
        etAgeYears = findViewById(R.id.etAgeYears);
        etAgeMonths = findViewById(R.id.etAgeMonths);
        etAgeDays = findViewById(R.id.etAgeDays);
        options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        period = new Period(new LocalDate(configuration.getMinDate()), new LocalDate(configuration.getMaxDate()));
        if (options.size() > 0) {
            setOptionsOrHint(options.get(0));
            if (options.get(0) instanceof DateOption) {
                dateOption = (DateOption) options.get(0);
            }
        }

        etAgeYears.addTextChangedListener(this);
        etAgeMonths.addTextChangedListener(this);
        etAgeDays.addTextChangedListener(this);


        etAnswer.setFocusable(false);
        etAnswer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DateSelector.MAX_DATE = configuration.getMaxDate();
                DateSelector.MIN_DATE = configuration.getMinDate();
                Intent intent = new Intent(ContactRegistryWidget.this.context, DateSelector.class);
                intent.putExtra(DateSelector.DATE_TYPE, configuration.getWidgetType().toString());
                ((Activity) ContactRegistryWidget.this.context).startActivityForResult(intent, getQuestionId());
            }
        });
        isSetAnswerFromOnCreate = true;
        if (configuration.getWidgetType() == DateSelector.WIDGET_TYPE.TIME) {
            setAnswer(Global.TIME_FORMAT.format(new Date()), null, null);
        } else {
            setAnswer(Global.DATE_TIME_FORMAT.format(new Date()), null, null);
        }
    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Validation validation = Validation.getInstance();
        if (configuration.getMinLenght() >= 0) {
            if (etContactID.getText().toString().length() == 0 && !isMendatory) {
            } else if (etContactID.getText().toString().length() < configuration.getMinLenght()) {
                return false;
            }
            if (etContactName.getText().toString().length() == 0 && !isMendatory) {
            } else if (etContactName.getText().toString().length() < configuration.getMinLenght()) {
                return false;
            }
        }

        return
                validation.validate(etContactID, question.getValidationFunction(), isMendatory)
                        && validation.validate(etContactName, question.getValidationFunction(), isMendatory);
    }

    @Override
    public void setOptionsOrHint(Option... data) {

    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();
        param.put(ParamNames.PAYLOAD_TYPE, Question.PAYLOAD_TYPE.NAME);

        if (isValidInput(question.isMandatory())) {
            JSONObject givenNameObj = new JSONObject();
            param.put(ParamNames.GIVEN_NAME, etContactID.getText().toString());

            JSONObject familyNameObj = new JSONObject();
            param.put(ParamNames.FAMILY_NAME, etContactName.getText().toString());

            JSONArray names = new JSONArray();
            names.put(givenNameObj);
            names.put(familyNameObj);
            dismissMessage();
        } else {
            if (etContactID.getText().toString().length() == 0 || etContactName.getText().toString().length() == 0) {
                activity.addValidationError(getQuestionId(), "Required Field");

            } else {
                activity.addValidationError(getQuestionId(), question.getErrorMessage());
            }
        }

        return param;
    }

    public String getValue() {
        return etContactID.getText().toString() + " " + etContactName.getText().toString();
    }

    @Override
    public void onFocusGained() {

    }

    @Override
    public void setAnswer(String answer, String uuid, LANGUAGE language) {
       // etContactID.setText(answer);
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
        etContactID.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    public EditText getInputField() {
        return etContactID;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    public String getUnValidatedValue() {
        return etContactID.getText().toString();
    }
}
