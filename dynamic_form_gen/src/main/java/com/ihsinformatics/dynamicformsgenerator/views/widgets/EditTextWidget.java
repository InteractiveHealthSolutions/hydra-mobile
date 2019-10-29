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
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.RangeOption;
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

public class EditTextWidget extends InputWidget implements TextWatcher {

    private EditText etAnswer;
    private List<RangeOption> rangeOptions;
    private QuestionConfiguration configuration;
    private InputWidgetBakery widgetBakery;
    private LinearLayout llRepeatSpace;
    private List<Map<Integer, InputWidget>> repeatGroups;
    BaseActivity baseActivity;

    public EditTextWidget(Context context, Question question, int layoutId){
        super(context, question, layoutId);
        if(super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        rangeOptions = new ArrayList<>(0);
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        for (Option option : options) {
            if (option instanceof RangeOption) {
                rangeOptions.add((RangeOption) option);
            } else {
                setOptionsOrHint(option);
                etAnswer.setText(option.getUuid());
            }
        }

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

        etAnswer.addTextChangedListener(this);
        widgetBakery = new InputWidgetBakery();
        if(question.getRepeatables()!= null && question.getRepeatables().size()>0) {
            llRepeatSpace = findViewById(R.id.llRepeats);
            repeatGroups = new ArrayList<>();
        }

        baseActivity = ((BaseActivity) getContext());
    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Validation validation = Validation.getInstance();
        if (configuration.getMinLenght() >= 0) {
            if (etAnswer.getText().toString().length() == 0 && !isMendatory) {
            } else if (etAnswer.getText().toString().length() < configuration.getMinLenght()) {
                return false;
            }
        }
        if (Validation.CHECK_FOR_RANGE.equals(question.getValidationFunction())) {
            return validation.validateForRange(etAnswer.getText().toString(), rangeOptions, isMendatory);
        }
        return validation.validate(etAnswer, question.getValidationFunction(), isMendatory);
    }

    @Override
    public void setOptionsOrHint(Option... data) {
        if (data.length > 0) {
            etAnswer.setHint(data[0].getText());
        }
    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();
        if (isValidInput(question.isMandatory())) {
            if (etAnswer.getText().toString().length() == 0 && !question.isMandatory()) {
                param.put(question.getParamName(), null);
            } else {
                param.put(question.getParamName(), etAnswer.getText().toString());
            }
            dismissMessage();
            //  param.put(question.getParamName(), etAnswer.getText().toString());
        } else {
            activity.addValidationError(getQuestionId(), "Invalid input");
        }

        if(question.getRepeatables() != null && question.getRepeatables().size()>0) {
            JSONArray childrenArray = new JSONArray();
            for(Map<Integer, InputWidget> li: repeatGroups) {
                Iterator<Integer> it = li.keySet().iterator();
                JSONArray childArray = new JSONArray();
                while (it.hasNext()) {
                    int key = it.next();
                    InputWidget childWidget = li.get(key);
                    if (childWidget.isSendable()
                            && childWidget.getVisibility() == View.VISIBLE
                            && childWidget.getInputWidgetsType() != InputWidgetsType.WIDGET_TYPE_HEADING
                            && childWidget.getInputWidgetsType() != InputWidgetsType.WIDGET_TYPE_IMAGE)

                        if(childWidget.isValidInput(childWidget.getQuestion().isMandatory())) {
                            JSONObject ans = childWidget.getAnswer();
                            childArray.put(ans);
                        } else
                            activity.addValidationError(getQuestionId(), childWidget.getQuestionId()+"");

                }

                childrenArray.put(childArray);
            }
            param.put(ParamNames.CONCEPT_GROUP_MEMBERS, childrenArray);
        }
        return param;
    }

    public String getValue() {

        return etAnswer.getText().toString();
    }

    @Override
    public void onFocusGained() {
        etAnswer.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etAnswer.getWindowToken(), 0);
        imm.showSoftInput(etAnswer, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void setAnswer(String answer, String uuid, LANGUAGE language) {
        etAnswer.setText(answer);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(onValueChangeListener!=null)
            onValueChangeListener.onValueChanged(s.toString());
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

        if(question.getRepeatables() != null && question.getRepeatables().size()>0) {
            if(!newValue.matches(Regex.NUMERIC.toString())) return;

            int repeatTimes = Integer.parseInt(newValue);
            // removing references from parent, clearing local references, and removing from the layout
            reset();
            Map<Integer, InputWidget> childWidgets;
            int uniqueId = 99999;

            for(int i=0; i<repeatTimes; i++) {
                childWidgets = new HashMap<>();
                InputWidget headingWidget  = createHeading(i);
                llRepeatSpace.addView(headingWidget);

                for(Question q: question.getRepeatables()) {
                    try {q = (Question) q.clone();}
                    catch (CloneNotSupportedException e) {continue;}

                    uniqueId = (99999*(i+1))+q.getQuestionId();
                    q.setQuestionId(uniqueId);
                    q.setRuntimeGenerated(true);
                    q.updateSkipLogicReferences(99999, (i+1));
                    InputWidget inputWidget = widgetBakery.bakeInputWidget(context, q);
                    childWidgets.put(uniqueId, inputWidget);
                    baseActivity.addRunTimeWidgetReference(uniqueId, inputWidget);
                    // adding in linear layout
                    llRepeatSpace.addView(inputWidget);
                }

                repeatGroups.add(childWidgets);
            }
        }
    }

    private InputWidget createHeading(int i) {
        Question q = (new Question(
                false,
                question.getFormTypeId(),
                -1,
                "-1",
                InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING,
                View.VISIBLE, null,
                question.getRepeatGroupHeadingPrefix()+" "+ (i+1), null, null)); // TODO get the heading form question or question config class
        InputWidget headingWidget = widgetBakery.bakeInputWidget(context, q);

        return headingWidget;
    }

    private void reset() {
        // removing references from parent, and clearing local references, removing from the layout
        llRepeatSpace.removeAllViews();
        for(Map w: repeatGroups) {
            Iterator<Integer> it = w.keySet().iterator();
            while (it.hasNext()) baseActivity.removeRuntimeWidgetReference(it.next());
        }

        repeatGroups.clear();
    }

    @Override
    public void setEnabled(boolean enabled) {
        etAnswer.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    public EditText getInputField() {
        return etAnswer;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    public String getUnValidatedValue() {
        return etAnswer.getText().toString();
    }
}
