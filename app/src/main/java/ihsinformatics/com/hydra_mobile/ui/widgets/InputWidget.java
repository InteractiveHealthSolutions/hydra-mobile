package ihsinformatics.com.hydra_mobile.ui.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ihsinformatics.com.hydra_mobile.R;
import ihsinformatics.com.hydra_mobile.data.core.options.Option;
import ihsinformatics.com.hydra_mobile.data.core.question.Question;
import ihsinformatics.com.hydra_mobile.data.core.question.config.Configuration;
import ihsinformatics.com.hydra_mobile.utils.*;
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class InputWidget extends RelativeLayout {
    public Observable observable;


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

    public InputWidget(Context context, Question question, int layoutId) {
        super(context);
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
            if (question.getQuestionNumber().equals("")) {
                tvQuestionNumber.setVisibility(View.GONE);
            } else {
                tvQuestionNumber.setText(question.getQuestionNumber());
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
                color = getResources().getColor(R.color.colorRed);
                break;
            case MESSAGE_TYPE_CORRECT:
                color = getResources().getColor(R.color.colorGreen);
                break;
            case MESSAGE_TYPE_INFORMATION:
                color = getResources().getColor(R.color.colorBlue);
                break;
            case MESSAGE_TYPE_WARNING:
                color = getResources().getColor(R.color.colorOrangeGoogle);
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
        if (gainFocus && inputWidgetsType != InputWidgetsType.WIDGET_TYPE_DATE)
            onFocusGained();
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public Observable getObservable() {
        initObservable();
        return observable;
    }

    public void addObservers(Observer... observers) {
        initObservable();
        for (Observer observer : observers)
            observable.addObserver(observer);
    }

    private void initObservable() {
        if (observable == null)
            observable = new Observable();
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

    public abstract void setAnswer(String answer, String uuid, Translator.LANGUAGE language);

    public abstract JSONObject getAnswer() throws JSONException;

    public abstract void destroy();
}
