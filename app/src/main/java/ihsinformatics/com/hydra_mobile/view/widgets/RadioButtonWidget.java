package ihsinformatics.com.hydra_mobile.view.widgets;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import ihsinformatics.com.hydra_mobile.R;
import ihsinformatics.com.hydra_mobile.common.Constant;
import ihsinformatics.com.hydra_mobile.data.core.options.Option;
import ihsinformatics.com.hydra_mobile.data.core.question.Question;
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences;
import ihsinformatics.com.hydra_mobile.utils.Translator;
import ihsinformatics.com.hydra_mobile.utils.Validation;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RadioButtonWidget extends InputWidget implements OnCheckedChangeListener {

    TextView temp;
    private ArrayAdapter<String> mAdapter;
    // ArrayList<String> mData;
    HashMap<String, Option> mOptions;
    private RadioGroup rgAnswer;
    private android.widget.RadioGroup.LayoutParams layoutParams;
    private int oldOptionScore;

    public RadioButtonWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        rgAnswer = (RadioGroup) findViewById(R.id.rgAnswer);
        rgAnswer.setOnCheckedChangeListener(this);

        options = null; //DataProvider.getInstance(context).getOptions(question.getQuestionId());
        oldOptionScore = 0;

        if (options.size() > 0) {

            rgAnswer.setWeightSum(options.size());
            layoutParams = new android.widget.RadioGroup.LayoutParams(0, android.widget.RadioGroup.LayoutParams.WRAP_CONTENT, 1);
            layoutParams.setMargins(3, 0, 3, 0);
            setOptionsOrHint(options.toArray(new Option[options.size()]));
        }

    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Validation validation = Validation.getInstance();
        RadioButton temp = (RadioButton) rgAnswer.findViewById(rgAnswer.getCheckedRadioButtonId());
        return validation.validate(temp, question.getValidationFunction(), isMendatory);
    }

    @Override
    public void setOptionsOrHint(Option... data) {
        if (data.length > 0) {
            mOptions = new HashMap<String, Option>();
            // mData = new ArrayList<String>(Arrays.asList(data));
            int id = 0;
            for (int i = 0; i < data.length; i++) {
                while (findViewById(id) != null) {
                    id++;
                }

                RadioButton temp = new RadioButton(context);
                temp.setTextSize(20);
                temp.setId(id);
                String value = data[i].getText();
                String translatedText = Translator.getInstance().Translate(value, GlobalPreferences.getinstance(context).findLanguagePrferenceValue());
                mOptions.put(translatedText, data[i]);
                if (translatedText != null) {
                    temp.setText(translatedText);
                } else {
                    temp.setText(value);
                }


                temp.setBackgroundColor(getResources().getColor(R.color.lightest_blue_e6edf5));
                temp.setPadding(3, 5, 3, 5);
                temp.setLayoutParams(layoutParams);
                rgAnswer.addView(temp);
                //temp.setChecked(true);
            }
        }

    }

    @Override
    public void onFocusGained() {
        // not applicable

    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
        answer = Translator.getInstance().Translate(answer, language);
        int id = getIdByText(answer);
        if (id != -1) {
            rgAnswer.check(id);
            setVisibility(View.VISIBLE);
        }

    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();
        if (isValidInput(question.isMandatory())) {
            dismissMessage();
            RadioButton rb = ((RadioButton) rgAnswer.findViewById(rgAnswer.getCheckedRadioButtonId()));
            if (rb != null) {
                Option ans = mOptions.get(rb.getText().toString());
                if (ans.getUuid().equals("") || ans.getUuid() == null) {
                    param.put(question.getParamName(), ans.getText());
                } else {
                    param.put(question.getParamName(), ans.getUuid());
                }

            } else {
                param.put(question.getParamName(), null);
            }

        } else {
            //  activity.addValidationError(getQuestionId(), "select an option");
        }

        return param;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String val = ((RadioButton) group.findViewById(checkedId)).getText().toString();
        if (Constant.Companion.getSCOREABLE_QUESTIONS().contains(question.getQuestionId())) {
            Option o = mOptions.get(val);

            //activity.addInRating(oldOptionScore - (oldOptionScore * 2));
            //activity.addInRating(o.getScore());
            oldOptionScore = o.getScore();

        }

        try {
            int index = findOptionIndexById(checkedId);
            int[] showables = options.get(index).getOpensQuestions();
            int[] hideables = options.get(index).getHidesQuestions();
            //((BaseActivity)getContext()).onChildViewItemSelected(showables, hideables);
			
			
			/*if("other".equals(val)) {
				temp = (TextView) arg1;
				activity.startActivityForResult(new Intent(context, ManualInput.class), getQuestionId());
			}*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getIdByText(String text) {
        for (int i = 0; i < rgAnswer.getChildCount(); i++) {
            View v = rgAnswer.getChildAt(i);
            if (v instanceof RadioButton) {
                String t = ((RadioButton) v).getText().toString();
                if (t.equals(text)) {
                    return v.getId();
                }
            }
        }

        return -1;
    }

    private int findOptionIndexById(int id) {
        String text = ((RadioButton) rgAnswer.findViewById(id)).getText().toString();
        int i = 0;
        for (Option o : options) {

            if (o.getText().equals(Translator.getInstance().Translate(text, Translator.LANGUAGE.ENGLISH))) {
                return i;
            }
            i++;
        }

        return -1;
    }

    private Option findOptionByText(String text) {
        for (Option o : options) {
            if (o.getText().equals(text)) {
                return o;
            }
        }

        return null;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
