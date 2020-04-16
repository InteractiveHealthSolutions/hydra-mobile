package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.DateSelector;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalenderDateWidget extends InputWidget {
    private EditText etAnswer;
    private DatePickerDialog picker;
    private Calendar calendar;
    private OnClickListener clickListener;
//    private DateOption dateOption;
//    private Period period;
    private QuestionConfiguration configuration;
    private String dateString;
    //days and month should not be more than three
    // 110 pe erorr jy zero hony pr b
    //111 pe b disable


    public CalenderDateWidget(final Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if(super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        etAnswer = findViewById(R.id.etAnswer);
        options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        calendar = Calendar.getInstance();

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
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

        etAnswer.setFocusable(false);
        etAnswer.setOnClickListener(clickListener);


    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Validation validation = Validation.getInstance();

        return validation.validate(etAnswer, question.getValidationFunction(), isMendatory);
    }

    @Override
    public void setOptionsOrHint(Option... data) {

    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();
        try {
            if (isValidInput(question.isMandatory())) {
                dismissMessage();
                String value = null;
                dateString=etAnswer.getText().toString();
                Date date = Global.DATE_TIME_FORMAT.parse(dateString);
                value = Global.OPENMRS_DATE_FORMAT.format(date);
                param.put(ParamNames.PARAM_NAME, question.getParamName());
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
    public void setAnswer(String answer, String uuid, LANGUAGE language) {

    }

    @Override
    public void onFocusGained() {
        etAnswer.performClick();
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getServiceHistoryValue() {
        return getValue();
    }


    @Override
    public String getValue() {
        return etAnswer.getText().toString();
    }

    @Override
    public void setEnabled(boolean enabled) {
        etAnswer.setEnabled(enabled);
        super.setEnabled(enabled);
    }

}