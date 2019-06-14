package ihsinformatics.com.hydra_mobile.view.widgets;

import android.app.Service;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import ihsinformatics.com.hydra_mobile.R;
import ihsinformatics.com.hydra_mobile.data.core.options.Option;
import ihsinformatics.com.hydra_mobile.data.core.options.RangeOption;
import ihsinformatics.com.hydra_mobile.data.core.question.Question;
import ihsinformatics.com.hydra_mobile.data.core.question.config.QuestionConfiguration;
import ihsinformatics.com.hydra_mobile.utils.SkipRange;
import ihsinformatics.com.hydra_mobile.utils.Translator;
import ihsinformatics.com.hydra_mobile.utils.Validation;
import ihsinformatics.com.hydra_mobile.view.activity.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

public class EditTextWidget extends InputWidget implements TextWatcher {
    private EditText etAnswer;
    private List<RangeOption> rangeOptions;
    private QuestionConfiguration configuration;

    public EditTextWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        rangeOptions = new ArrayList<>(0);
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        options =null;  //DataProvider.getInstance(context).getOptions(question.getQuestionId());
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
        if (allowedCharacters != null) {
            filterArray = new InputFilter[2];
            filterArray[1] = DigitsKeyListener.getInstance(configuration.getAllowedCharacters());
        } else {
            filterArray = new InputFilter[1];
        }
        filterArray[0] = new InputFilter.LengthFilter(configuration.getMaxLength());

        etAnswer.setInputType(configuration.getInputType());
        etAnswer.setFilters(filterArray);
        if (rangeOptions != null && rangeOptions.size() > 0) {
            etAnswer.addTextChangedListener(this);
        }
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
            // activity.addValidationError(getQuestionId(), "Invalid input");
        }
        return param;
    }

    @Override
    public void onFocusGained() {
        etAnswer.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etAnswer.getWindowToken(), 0);
        imm.showSoftInput(etAnswer, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
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
        if (rangeOptions != null && rangeOptions.size() > 0) {
            try {
                for (RangeOption rangeOption : rangeOptions) {
                    int[] showables = rangeOption.getOpensQuestions();
                    int[] hideables = rangeOption.getHidesQuestions();
                    if (s.toString().equals("") && !SkipRange.VALIDATION_TYPE.IS_EMPTY.equals(rangeOption.getSkipRange().getValidationType())) {
                        return;
                    } else if (SkipRange.VALIDATION_TYPE.IS_EMPTY.equals(rangeOption.getSkipRange().getValidationType())) {
                        if (rangeOption.getSkipRange().validateEmpty(s.toString())) {
                            // ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables);
                        } else {
                            //((BaseActivity) getContext()).onChildViewItemSelected(hideables, showables);
                        }
                    } else {
                        int value = Integer.parseInt(s.toString());
                        if (rangeOption.getSkipRange().validate(value)) {
                            //((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables);
                        } else {
                            //((BaseActivity) getContext()).onChildViewItemSelected(hideables, showables);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                Timber.e(e.getMessage());
            }
        }
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
}
