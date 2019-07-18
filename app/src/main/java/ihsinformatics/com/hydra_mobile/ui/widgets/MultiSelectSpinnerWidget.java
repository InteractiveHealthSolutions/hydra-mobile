package ihsinformatics.com.hydra_mobile.ui.widgets;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import ihsinformatics.com.hydra_mobile.R;
import ihsinformatics.com.hydra_mobile.data.core.options.Option;
import ihsinformatics.com.hydra_mobile.data.core.question.Question;
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences;
import ihsinformatics.com.hydra_mobile.utils.Translator;
import ihsinformatics.com.hydra_mobile.ui.widgets.controls.MultiSelectSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MultiSelectSpinnerWidget extends InputWidget implements MultiSelectSpinner.HandleSkipLogic {
    private LinearLayout llSpinner;
    private MultiSelectSpinner mspAnswer;

    public MultiSelectSpinnerWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        llSpinner = (LinearLayout) findViewById(R.id.llMspAnswer);
        options = null; //DataProvider.getInstance(context).getOptions(question.getQuestionId());
        setOptionsOrHint(options.toArray(new Option[options.size()]));
    }

    @Override
    public boolean isValidInput(boolean isMandatory) {
        if (mspAnswer.getValues().size() > 0 && isMandatory) {
            return true;
        } else if (!isMandatory) {
            return true;
        }
        return false;
    }

    @Override
    public void setOptionsOrHint(Option... data) {
        Option[] options = data;
        if (data.length > 0) {
            for (int i = 0; i < data.length; i++) {
                Option option = data[i];
                String optionText = option.getText();
                String translatedText = Translator.getInstance().Translate(optionText, GlobalPreferences.getinstance(context).findLanguagePrferenceValue());
                if (translatedText != null) {
                    options[i].setText(translatedText);
                } else {
                    options[i].setText(optionText);
                }
            }
            mspAnswer = new MultiSelectSpinner(context, this, options);
            llSpinner.addView(mspAnswer);
        }
    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();
        if (isValidInput(question.isMandatory())) {
            dismissMessage();
            addParams(param);
        } else {
            // activity.addValidationError(getQuestionId(), "Invalid input");
        }
        return param;
    }

    private void addParams(JSONObject param) throws JSONException {
        JSONArray subParams = new JSONArray();
        List<String> selections = mspAnswer.getValues();
        for (String s : selections) {
            subParams.put(s);
        }
        param.put(question.getParamName(), subParams);
    }

    @Override
    public void onFocusGained() {
        mspAnswer.inflate();
    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void applySkipLogic(int position) {
        int[] showables = options.get(position).getOpensQuestions();
        int[] hideables = options.get(position).getHidesQuestions();
        // ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables);
    }

    @Override
    public void revertSkipLogic(int position) {
        int[] showables = options.get(position).getOpensQuestions();
        int[] hideables = options.get(position).getHidesQuestions();
        //((BaseActivity) getContext()).onChildViewItemSelected(hideables, showables);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }
}
