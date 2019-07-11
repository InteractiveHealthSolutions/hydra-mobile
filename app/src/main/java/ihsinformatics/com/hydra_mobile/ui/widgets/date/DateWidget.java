package ihsinformatics.com.hydra_mobile.ui.widgets.date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import ihsinformatics.com.hydra_mobile.R;
import ihsinformatics.com.hydra_mobile.common.Constant;
import ihsinformatics.com.hydra_mobile.data.core.options.DateOption;
import ihsinformatics.com.hydra_mobile.data.core.options.Option;
import ihsinformatics.com.hydra_mobile.data.core.question.Question;
import ihsinformatics.com.hydra_mobile.data.core.question.config.QuestionConfiguration;
import ihsinformatics.com.hydra_mobile.utils.Translator;
import ihsinformatics.com.hydra_mobile.utils.Validation;
import ihsinformatics.com.hydra_mobile.ui.dialogs.DateSelector;
import ihsinformatics.com.hydra_mobile.ui.widgets.InputWidget;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class DateWidget extends InputWidget {

    private EditText etAnswer;
    DateOption dateOption;
    private boolean isSetAnswerFromOnCreate = false;
    private QuestionConfiguration configuration;

    public DateWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        options = null; //DataProvider.getInstance(context).getOptions(question.getQuestionId());

        if (options.size() > 0) {
            setOptionsOrHint(options.get(0));
            if (options.get(0) instanceof DateOption) {
                dateOption = (DateOption) options.get(0);
            }
        }


        etAnswer.setFocusable(false);

        etAnswer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DateSelector.MAX_DATE = configuration.getMaxDate();
                DateSelector.MIN_DATE = configuration.getMinDate();
                Intent intent = new Intent(DateWidget.this.context, DateSelector.class);
                intent.putExtra(DateSelector.DATE_TYPE, configuration.getWidgetType().toString());
                ((Activity) DateWidget.this.context).startActivityForResult(intent, getQuestionId());

            }
        });
        isSetAnswerFromOnCreate = true;
        if (configuration.getWidgetType() == DateSelector.WIDGET_TYPE.TIME) {
            setAnswer(Constant.Companion.getTIME_FORMAT().format(new Date()), null, (Translator.LANGUAGE) null);
        } else {
            setAnswer(Constant.Companion.getDATE_FORMAT().format(new Date()), null, null);
        }
    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Validation validation = Validation.getInstance();
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
        try {
            if (isValidInput(question.isMandatory())) {
                dismissMessage();
                String value = null;
                Date date = Constant.Companion.getDATE_FORMAT().parse(etAnswer.getText().toString());
                value = Constant.Companion.getOPENMRS_DATE_FORMAT().format(date);
                param.put(question.getParamName(), value);
            } else {
                // activity.addValidationError(getQuestionId(), "Invalid input");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return param;
    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
        etAnswer.setText(answer);
        if (isSetAnswerFromOnCreate) {
            isSetAnswerFromOnCreate = false;
            // setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.VISIBLE);
        }

        try {
            if (dateOption != null) {
                if (dateOption.getSkipDateRange().validate(Constant.Companion.getDATE_FORMAT().parse(answer))) {
                    int[] showables = dateOption.getOpensQuestions();
                    int[] hideables = dateOption.getHidesQuestions();
                    ///  ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables);
                } else {
                    int[] showables = dateOption.getOpensQuestions();
                    int[] hideables = dateOption.getHidesQuestions();
                    //((BaseActivity) getContext()).onChildViewItemSelected(hideables, showables);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusGained() {
        etAnswer.performClick();
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
