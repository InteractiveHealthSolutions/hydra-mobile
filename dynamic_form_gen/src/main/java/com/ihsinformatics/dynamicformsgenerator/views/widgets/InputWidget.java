package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.Configuration;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.listeners.OnValueChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class InputWidget extends RelativeLayout implements Observer {

    public enum MessageType {
        MESSAGE_TYPE_ERROR,
        MESSAGE_TYPE_INFORMATION,
        MESSAGE_TYPE_WARNING,
        MESSAGE_TYPE_CORRECT,
    }

    public enum InputWidgetsType {
        WIDGET_TYPE_DATE,
        WIDGET_TYPE_EDITTEXT,
        WIDGET_TYPE_SPINNER,
        WIDGET_TYPE_SCORE_SPINNER,
        WIDGET_TYPE_MULTI_SELECT_SPINNER,
        WIDGET_TYPE_RADIO_BUTTON,
      //  WIDGET_TYPE_CHECK_BOX,
        WIDGET_TYPE_HEADING,
        WIDGET_TYPE_GPS,
        WIDGET_TYPE_HIDDEN_INPUT,
        WIDGET_TYPE_AGE,
        WIDGET_TYPE_IMAGE,
        WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT,
        WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW,
        WIDGETS_TYPE_QR_READER,
        WIDGETS_TYPE_IDENTIFIER,
        WIDGET_TYPE_AUTOCOMPLETE_EDITTEXT,
        WIDGET_TYPE_ADDRESS,
        WIDGETS_TYPE_ABC,
        WIDGETS_TYPE_NAME,
        WIDGETS_TYPE_CONTACT_TRACING,

    }

    protected TextView tvQuestion;
    protected TextView tvQuestionNumber;
    protected TextView tvMessage;
    protected Question question;
    protected Configuration configuration;
    protected Context context;
    protected List<Option> options;
    protected BaseActivity activity;
    protected Validation validator;
    protected InputWidgetsType inputWidgetsType;
    protected OnValueChangeListener onValueChangeListener;

    public InputWidget(Context context, Question question, int layoutId) {
        super(context);
        if(question.getOptions() == null) options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        else options = question.getOptions();
        setFocusable(true);
        setFocusableInTouchMode(true);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(layoutInflater.inflate(layoutId, this, false));
        this.context = context;
        this.validator = Validation.getInstance();
        this.question = question;
        this.activity = (BaseActivity) context;
        configuration = question.getQuestionConfiguration();
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvQuestionNumber = (TextView) findViewById(R.id.tvNumber);
        inputWidgetsType = question.getQuestionType();
        String translatedText = Translator.getInstance().Translate(question.getText(), GlobalPreferences.getinstance(context).findLanguagePrferenceValue());
        if (translatedText != null) {
            tvQuestion.setText(translatedText);
        } else {
            tvQuestion.setText(question.getText());
        }
        if (tvQuestionNumber != null) {
            if (!question.isMandatory()) {
                tvQuestionNumber.setVisibility(View.GONE);
            } else {
                tvQuestionNumber.setText("*");
                tvQuestionNumber.setTextColor(getResources().getColor(R.color.Red));
                tvQuestionNumber.setBackgroundColor(getResources().getColor(R.color.White));
            }
        }
        setVisibility(question.getInitialVisibility());
    }

    public void setQuestion(String question) {
        tvQuestion.setText(question);
    }

    public void setMessage(String message, MessageType messageType) {
        int color;
        switch (messageType) {
            case MESSAGE_TYPE_ERROR:
                color = getResources().getColor(R.color.Red);
                break;
            case MESSAGE_TYPE_CORRECT:
                color = getResources().getColor(R.color.Green);
                break;
            case MESSAGE_TYPE_INFORMATION:
                color = getResources().getColor(R.color.Blue);
                break;
            case MESSAGE_TYPE_WARNING:
                color = getResources().getColor(R.color.Orange);
                break;
            default:
                color = Color.BLACK;
                break;
        }
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setTextColor(color);
        tvMessage.setText(message);
    }

    public int getQuestionId() {
        return question.getQuestionId();
    }

    public void dismissMessage() {
        tvMessage.setText("");
        tvMessage.setVisibility(View.GONE);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    public InputWidgetsType getInputWidgetsType() {
        return inputWidgetsType;
    }

    public Question getQuestion() {
        return question;
    }

    public String getQuestionText() {
        return tvQuestion.getText().toString();
    }

    public List<Option> getOptions() {
        return options;
    }

    public String getMessage() {
        return tvMessage.getText().toString();
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
//        if (gainFocus && inputWidgetsType != InputWidgetsType.WIDGET_TYPE_DATE)
//            onFocusGained();
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    public OnValueChangeListener getOnValueChangeListener() {
        return this.onValueChangeListener;
    }

    // this will be called when
    @Override
    public void update(Observable observable, Object o) {
        observable.notifyObservers();
    }

    public boolean isSendable() {
        return  question.getParamName()!=null;
    }

    protected void closeForm() {
        activity.finish();
    }

    public abstract boolean isValidInput(boolean isNullable);

    public abstract void setOptionsOrHint(Option... data);

    public abstract void onFocusGained();

    public abstract void setAnswer(String answer, String uuid, LANGUAGE language) throws JSONException;

    public abstract JSONObject getAnswer() throws JSONException;

    public abstract String getValue() throws JSONException;

    public abstract void destroy();

    public abstract String getServiceHistoryValue() throws JSONException;
}
