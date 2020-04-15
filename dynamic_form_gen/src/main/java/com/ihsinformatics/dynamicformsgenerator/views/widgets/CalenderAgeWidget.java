package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;
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


public class CalenderAgeWidget extends InputWidget {
    private EditText etAnswer;
    private EditText etAgeYears;
    private EditText etAgeMonths;
    private EditText etAgeDays;
    private DatePickerDialog picker;
    private Calendar calendar;
    private View.OnClickListener clickListener;
    private LinearLayout ageWidget;
//    private DateOption dateOption;
//    private Period period;
//    private QuestionConfiguration configuration;
    private String dateString;
    //days and month should not be more than three
    // 110 pe erorr jy zero hony pr b
    //111 pe b disable


    public CalenderAgeWidget(final Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        etAnswer = findViewById(R.id.etAnswer);
        etAgeYears = findViewById(R.id.etAgeYears);
        etAgeMonths = findViewById(R.id.etAgeMonths);
        etAgeDays = findViewById(R.id.etAgeDays);
        ageWidget = findViewById(R.id.linearAgeWidgetLayout);
        options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        calendar = Calendar.getInstance();

        clickListener = new View.OnClickListener() {
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
                                Period p = null;
                                try {
                                    p = new Period(new LocalDate(Global.DATE_TIME_FORMAT.parse(dateString)), new LocalDate(), PeriodType.yearMonthDayTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if(null!=p) {
                                    etAgeYears.setText(p.getYears() + "");
                                    etAgeMonths.setText(p.getMonths() + "");
                                    etAgeDays.setText(p.getDays() + "");
                                }else
                                {
                                    etAgeYears.setText(year + " ");
                                    etAgeMonths.setText(monthOfYear + " ");
                                    etAgeDays.setText(dayOfMonth + " ");
                                }
                                etAgeYears.setError(null);
                                etAgeMonths.setError(null);
                                etAgeDays.setError(null);


                            }
                        }, yearToday, monthToday, dayToday);
                picker.getDatePicker().setMaxDate(calendar.getTimeInMillis());

                Date today = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(today);
                c.add(Calendar.YEAR, -110); // Subtract 6 years
                long minDate = c.getTime().getTime(); // Twice!
                picker.getDatePicker().setMinDate(minDate);   // now min date is set to 110years ago max
                picker.show();
            }
        };

        etAnswer.setFocusable(false);
        etAnswer.setOnClickListener(clickListener);
        ageWidget.setOnClickListener(clickListener);
        etAgeYears.setOnClickListener(clickListener);
        etAgeMonths.setOnClickListener(clickListener);
        etAgeDays.setOnClickListener(clickListener);

    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Validation validation = Validation.getInstance();

        int ageYears = Integer.parseInt(etAgeYears.getText().toString());
        int ageMonths = Integer.parseInt(etAgeMonths.getText().toString());
        int ageDays = Integer.parseInt(etAgeDays.getText().toString());

        if (ageYears > 110 || ageMonths > 11 || ageDays > 30)
            return false;

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
                param.put(ParamNames.PARAM_NAME, "age");
                param.put(ParamNames.VALUE, value);
                param.put(ParamNames.PAYLOAD_TYPE, question.getPayload_type());

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
}