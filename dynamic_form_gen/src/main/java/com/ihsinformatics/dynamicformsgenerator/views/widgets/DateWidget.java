package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.DateOption;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.DateSelector;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateWidget extends InputWidget {

    private EditText etAnswer;
    DateOption dateOption;
    private boolean isSetAnswerFromOnCreate = false;
    private QuestionConfiguration configuration;

    private DatePickerDialog picker;
    private OnClickListener clickListener;
    SimpleDateFormat dateFormat = Global.DATE_TIME_FORMAT;

    public DateWidget(final Context context, Question question, int layoutId) throws JSONException {
        super(context, question,layoutId);
        if(super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        options = DataProvider.getInstance(context).getOptions(question.getQuestionId());

        if (options.size() > 0) {
            setOptionsOrHint(options.get(0));
            if (options.get(0) instanceof DateOption) {
                dateOption = (DateOption) options.get(0);
            }
        }

        etAnswer.setFocusable(false);


        setCurrentDate();
        clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                final int dayToday = cldr.get(Calendar.DAY_OF_MONTH);
                final int monthToday = cldr.get(Calendar.MONTH);
                final int yearToday = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                String dateString = dateFormat.format(calendar.getTime());
                                etAnswer.setText(dateString);

                            }
                        }, yearToday, monthToday, dayToday);

                Date today = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(today);
                picker.getDatePicker().setMaxDate(configuration.getMaxDate().getTime());
                picker.getDatePicker().setMinDate(configuration.getMinDate().getTime());
                picker.show();
            }
        };


        //Old layout for selecting date
/*        etAnswer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DateSelector.MAX_DATE = configuration.getMaxDate();
                DateSelector.MIN_DATE = configuration.getMinDate();
                Intent intent = new Intent(DateWidget.this.context, DateSelector.class);
                intent.putExtra(DateSelector.DATE_TYPE, configuration.getWidgetType().toString());
                ((Activity) DateWidget.this.context).startActivityForResult(intent, getQuestionId());

            }
        });*/

        //New calendar date selector
        etAnswer.setOnClickListener(clickListener);
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

        // Checking that selected date must not be greater than today's date
        if(isMendatory && etAnswer.getText().toString().trim().length() > 1) {
            SimpleDateFormat dateFormat = Global.DATE_TIME_FORMAT;

            if (configuration.getWidgetType() == DateSelector.WIDGET_TYPE.TIME) {
                dateFormat = Global.TIME_FORMAT;
            }

            Date strDate = null;
            try {
                strDate = dateFormat.parse(etAnswer.getText().toString());
                if (!configuration.getMaxDate().after(strDate)) {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
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




        try {
            if (isValidInput(question.isMandatory())) {
                dismissMessage();
                String value = null;
                Date date = Global.DATE_TIME_FORMAT.parse(etAnswer.getText().toString());
                value = Global.OPENMRS_DATE_FORMAT.format(date);
                param.put(ParamNames.PARAM_NAME,question.getParamName());
                param.put(ParamNames.VALUE, value);
                param.put(ParamNames.PAYLOAD_TYPE, question.getPayload_type());
                //Necessary for every widget to have PAYLOAD_TYPE AND PERSON_ATTRIBUTE
                if(question.getAttribute())
                    param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_TRUE);
                else
                    param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_FALSE);


            } else {
                activity.addValidationError(getQuestionId(), question.getErrorMessage());
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return param;
    }

    @Override
    public void setAnswer(String answer, String uuid, LANGUAGE language) throws JSONException {
        if(onValueChangeListener!=null) {
            onValueChangeListener.onValueChanged(answer);
        }
        etAnswer.setText(answer);
        if(isSetAnswerFromOnCreate) {
            isSetAnswerFromOnCreate = false;
            // setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.VISIBLE);
        }

        try {
            if (dateOption != null) {
                if (dateOption.getSkipDateRange().validate(Global.DATE_TIME_FORMAT.parse(answer))) {
                    int[] showables = dateOption.getOpensQuestions();
                    int[] hideables = dateOption.getHidesQuestions();
                    ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables, question);
                } else {
                    int[] showables = dateOption.getOpensQuestions();
                    int[] hideables = dateOption.getHidesQuestions();
                    ((BaseActivity) getContext()).onChildViewItemSelected(hideables, showables, question);
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
    @Override
    public String getValue() {
        return etAnswer.getText().toString();
    }

    @Override
    public String getServiceHistoryValue() {
        return getValue();
    }

    private void setCurrentDate()
    {
        String dateString = dateFormat.format(Calendar.getInstance().getTime());
        etAnswer.setText(dateString);
    }
}
