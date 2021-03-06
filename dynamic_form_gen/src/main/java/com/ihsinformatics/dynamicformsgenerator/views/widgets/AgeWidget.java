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

public class AgeWidget extends InputWidget implements TextWatcher {
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
    private QuestionConfiguration configuration;
    private String dateString;
    //days and month should not be more than three
    // 110 pe erorr jy zero hony pr b
    //111 pe b disable

    private View.OnClickListener clickListener;
    private DatePickerDialog picker;;
    private Calendar calendar;

    public AgeWidget(final Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
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
                                SimpleDateFormat dateFormat = Global.DATE_TIME_FORMAT;
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
                c.add(Calendar.YEAR, -110);
                long minDate = c.getTime().getTime();
                picker.getDatePicker().setMinDate(minDate);   // now min date is set to 110years ago max
                picker.show();
            }
        };

        etAnswer.setFocusable(false);
        //Old layout for selecting date
//        etAnswer.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                DateSelector.MAX_DATE = configuration.getMaxDate();
//                DateSelector.MIN_DATE = configuration.getMinDate();
//                Intent intent = new Intent(AgeWidget.this.context, DateSelector.class);
//                intent.putExtra(DateSelector.DATE_TYPE, configuration.getWidgetType().toString());
//                ((Activity) AgeWidget.this.context).startActivityForResult(intent, getQuestionId());
//            }
//        });

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

        int ageYears = Integer.parseInt(etAgeYears.getText().toString());
        int ageMonths = Integer.parseInt(etAgeMonths.getText().toString());
        int ageDays = Integer.parseInt(etAgeDays.getText().toString());

        if ((ageYears > 110 || ageMonths > 11 || ageDays > 30) || (ageYears < 0 || ageMonths < 0 || ageDays < 0))
            return false;

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
                Date date = Global.DATE_TIME_FORMAT.parse(dateString);
                value = Global.OPENMRS_DATE_FORMAT.format(date);
                param.put(ParamNames.PARAM_NAME, "age");
                param.put(ParamNames.VALUE, value);

                //Necessary for every widget to have PAYLOAD_TYPE AND PERSON_ATTRIBUTE
                param.put(ParamNames.PAYLOAD_TYPE, question.getPayload_type());
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
        try {
            if (!deadLock) {
                deadLock = true;
                Period p = new Period(new LocalDate(Global.DATE_TIME_FORMAT.parse(answer)), new LocalDate(), PeriodType.yearMonthDayTime());
                int years = p.getYears();
                int months = p.getMonths();
                int days = p.getDays();
                etAgeYears.setText(years + "");
                etAgeMonths.setText(months + "");
                etAgeDays.setText(days + "");
            } else {
                deadLock = false;
            }
            dateString = answer;
            etAnswer.setText(answer.substring(0, 10));
            if (isSetAnswerFromOnCreate) {
                isSetAnswerFromOnCreate = false;
                // setVisibility(View.VISIBLE);
            } else {
                setVisibility(View.VISIBLE);
            }
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
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getServiceHistoryValue() {
        return getValue();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (!callTextChanged) {
            callTextChanged = true;
            return;
        }
        if (!deadLock && !s.toString().equals("")) {

            try {

                int ageYears = Integer.parseInt(etAgeYears.getText().toString());
                int ageMonths = Integer.parseInt(etAgeMonths.getText().toString());
                int ageDays = Integer.parseInt(etAgeDays.getText().toString());

                if (ageYears > 110) {
                    etAgeYears.setError("Years should be <= 110");
                } else {
                    if (ageYears >= 110) {

                        setText(etAgeMonths, "0", false);
                        setText(etAgeDays, "0", false);

                    } else {
                        etAgeMonths.setEnabled(true);
                        etAgeDays.setEnabled(true);


                    }
                }
                if (ageMonths > 11) {
                    etAgeMonths.setError("Months should be <= 11");
                }
                if (ageDays > 30) {
                    etAgeDays.setError("Days should be <= 30");
                }


                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, ageYears - (ageYears + ageYears));
                calendar.add(Calendar.MONTH, ageMonths - (ageMonths + ageMonths));
                calendar.add(Calendar.DAY_OF_MONTH, ageDays - (ageDays + ageDays));

                if (ageYears <= period.getYears()) {

                    deadLock = true;
                    setAnswer(Global.DATE_TIME_FORMAT.format(calendar.getTime()), null, null);
                } else {
//                    Toasty.info(context, "Age should be between 0 to " + period.getYears(), Toast.LENGTH_LONG).show();
                }
            } catch (NumberFormatException e) {
                //Toasty.error(context, "Invalid Age", Toast.LENGTH_LONG).show();
                Logger.log(e);
            }


        } else if (deadLock) {
            deadLock = false;
            /*
            etAgeYears.setText(s.toString());
            etAgeMonths.setText(s.toString());
            etAgeDays.setText(s.toString());*/
        }
    }

    private boolean callTextChanged = true;

    private void setText(EditText tv, String text, boolean callTextChanged) {
        this.callTextChanged = callTextChanged;
        tv.setText(text);
        tv.setEnabled(false);

    }

    @Override
    public String getValue() {
        return etAnswer.getText().toString();
    }
}
