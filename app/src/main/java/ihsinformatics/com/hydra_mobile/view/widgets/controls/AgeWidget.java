package ihsinformatics.com.hydra_mobile.view.widgets.controls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ihsinformatics.com.hydra_mobile.R;
import ihsinformatics.com.hydra_mobile.common.Constant;
import ihsinformatics.com.hydra_mobile.data.core.options.DateOption;
import ihsinformatics.com.hydra_mobile.data.core.options.Option;
import ihsinformatics.com.hydra_mobile.data.core.question.Question;
import ihsinformatics.com.hydra_mobile.data.core.question.config.QuestionConfiguration;
import ihsinformatics.com.hydra_mobile.utils.Translator;
import ihsinformatics.com.hydra_mobile.utils.Validation;
import ihsinformatics.com.hydra_mobile.view.dialogs.DateSelector;
import ihsinformatics.com.hydra_mobile.view.widgets.InputWidget;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AgeWidget extends InputWidget {
    private EditText etAnswer;
    private EditText etAge;
    DateOption dateOption;
    Period period;
    private boolean isSetAnswerFromOnCreate = false;
    /*private boolean isSetAnswerAge = false;
    private boolean isFromSetAnswer = false;*/
    private boolean deadLock = false;
    private QuestionConfiguration configuration;

    public AgeWidget(final Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        etAge = (EditText) findViewById(R.id.etAge);
        options = null; //DataProvider.getInstance(context).getOptions(question.getQuestionId());
        period = new Period(new LocalDate(configuration.getMinDate()), new LocalDate(configuration.getMaxDate()));
        if (options.size() > 0) {
            setOptionsOrHint(options.get(0));
            if (options.get(0) instanceof DateOption) {
                dateOption = (DateOption) options.get(0);
            }
        }
        etAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!deadLock && !s.toString().equals("")) {
                    try {
                        int age = Integer.parseInt(s.toString());
                        if (age <= period.getYears()) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.YEAR, age - (age + age));
                            deadLock = true;
                            setAnswer(Constant.Companion.getDATE_FORMAT().format(calendar.getTime()), null, (Translator.LANGUAGE) null);
                        } else {
                            //Toasty.info(context, "Age should be between 0 to " + period.getYears(), Toast.LENGTH_LONG).show();
                        }
                    } catch (NumberFormatException e) {
                        //Toasty.error(context, "Invalid Age", Toast.LENGTH_LONG).show();
                        //Logger.log(e);
                    }
                } else if (deadLock) {
                    deadLock = false;
                    etAge.setText(s.toString());
                }
            }
        });
        etAnswer.setFocusable(false);
        etAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DateSelector.MAX_DATE = configuration.getMaxDate();
                DateSelector.MIN_DATE = configuration.getMinDate();
                Intent intent = new Intent(AgeWidget.this.context, DateSelector.class);
                intent.putExtra(DateSelector.DATE_TYPE, configuration.getWidgetType().toString());
                ((Activity) AgeWidget.this.context).startActivityForResult(intent, getQuestionId());
            }
        });
        isSetAnswerFromOnCreate = true;
        if (configuration.getWidgetType() == DateSelector.WIDGET_TYPE.TIME) {
            setAnswer(Constant.Companion.getTIME_FORMAT().format(new Date()), null, (Translator.LANGUAGE) null);
        } else {
            setAnswer(Constant.Companion.getDATE_FORMAT().format(new Date()), null, (Translator.LANGUAGE) null);
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
                //activity.addValidationError(getQuestionId(), "Invalid input");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return param;
    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
        try {
            if (!deadLock) {
                deadLock = true;
                Period p = new Period(new LocalDate(Constant.Companion.getDATE_FORMAT().parse(answer)), new LocalDate());
                int years = p.getYears();
                etAge.setText(years + "");
            } else {
                deadLock = false;
            }
            etAnswer.setText(answer);
            if (isSetAnswerFromOnCreate) {
                isSetAnswerFromOnCreate = false;
                // setVisibility(View.VISIBLE);
            } else {
                setVisibility(View.VISIBLE);
            }
            if (dateOption != null) {
                if (dateOption.getSkipDateRange().validate(Constant.Companion.getDATE_FORMAT().parse(answer))) {
                    int[] showables = dateOption.getOpensQuestions();
                    int[] hideables = dateOption.getHidesQuestions();
                    //((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables);
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
    public void destroy() {
        // TODO Auto-generated method stub
    }
}
