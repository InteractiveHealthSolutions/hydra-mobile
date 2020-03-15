package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;

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

        // if(options == null) options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
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
    public void setAnswer(String answer, String uuid, LANGUAGE language) {
        answer = Translator.getInstance().Translate(answer, language);
        int id = getIdByText(answer);
        if (id != -1) {
            rgAnswer.check(id);
            setVisibility(View.VISIBLE);
        }

    }

    public String getValue() {
        String value = "";
        RadioButton rb = ((RadioButton) rgAnswer.findViewById(rgAnswer.getCheckedRadioButtonId()));
        if (rb != null) value = rb.getText().toString();

        return value;
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

                    param.put(ParamNames.PARAM_NAME, question.getParamName());
                    param.put(ParamNames.VALUE, ans.getText());

                } else {

                    param.put(ParamNames.PARAM_NAME, question.getParamName());
                    param.put(ParamNames.VALUE, ans.getUuid());

                }

            } else {
                param.put(question.getParamName(), null);
            }

        } else {
            RadioButton rb = ((RadioButton) rgAnswer.findViewById(rgAnswer.getCheckedRadioButtonId()));
            if (rb == null) {
                activity.addValidationError(getQuestionId(), "Required Field");
            } else {
                activity.addValidationError(getQuestionId(), question.getErrorMessage());
            }
        }

        return param;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String val = ((RadioButton) group.findViewById(checkedId)).getText().toString();
        if (onValueChangeListener != null) {
            try {
                onValueChangeListener.onValueChanged(val);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (Global.SCOREABLE_QUESTIONS.contains(question.getQuestionId())) {
            Option o = mOptions.get(val);

            activity.addInRating(oldOptionScore - (oldOptionScore * 2));
            activity.addInRating(o.getScore());
            oldOptionScore = o.getScore();

        }

        try {
            int index = findOptionIndexById(checkedId);
            if (index != -1) {
                int[] showables = options.get(index).getOpensQuestions();
                int[] hideables = options.get(index).getHidesQuestions();
                ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables, question);
            }
			/*if("other".equals(val)) {
				temp = (TextView) arg1;
				activity.startActivityForResult(new Intent(context, ManualInput.class), getQuestionId());
			}*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnClickListener onClickListener;

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        for (int i = 0; i < rgAnswer.getChildCount(); i++) {

            rgAnswer.getChildAt(i).setClickable(enabled);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (rgAnswer != null) {
            // int[] showables = findAlldependantShowAbles();
            if (visibility == View.VISIBLE) {
                int index = findOptionIndexById(rgAnswer.getCheckedRadioButtonId());
                if (index != -1) {
                    int[] showables = options.get(index).getOpensQuestions();
                    int[] hideables = options.get(index).getHidesQuestions();
                    ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables, question);
                }
            } else {
                int[] hideables = findAlldependantHideAbles();
                ((BaseActivity) getContext()).onChildViewItemSelected(null, hideables, question);
            }
        }

        super.setVisibility(visibility);
    }

    private int[] findAlldependantHideAbles() {
        int[] toReturn = new int[0];
        for (Option o : options) {
            int[] array = o.getHidesQuestions();
            if (array != null) {
                int[] temp = toReturn;
                toReturn = new int[array.length + toReturn.length];
                for (int i = toReturn.length - array.length; i < toReturn.length; i++) {
                    toReturn[i] = (array[i - temp.length]);
                }
                for (int i = 0; i < temp.length; i++) {
                    toReturn[i] = temp[i];
                }
            }
        }
        return toReturn;
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
        if (rgAnswer == null) return -1;
        RadioButton radioButton = (RadioButton) rgAnswer.findViewById(id);
        if (radioButton == null) return -1;
        String text = radioButton.getText().toString();
        int i = 0;
        for (Option o : options) {

            if (o.getText().equals(Translator.getInstance().Translate(text, LANGUAGE.ENGLISH))) {
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
