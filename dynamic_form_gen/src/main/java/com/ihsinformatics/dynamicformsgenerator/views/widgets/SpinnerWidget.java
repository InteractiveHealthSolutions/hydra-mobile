package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SpinnerWidget extends InputWidget implements OnItemSelectedListener {
    private TextView temp;
    private Spinner spAnswer;
    private ArrayAdapter<String> mAdapter;
    // ArrayList<String> mData;
    private HashMap<String, Option> mOptions;
    private int oldOptionScore;
    private ArrayList<String> dataList;

    public SpinnerWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        spAnswer = (Spinner) findViewById(R.id.spAnswer);
        spAnswer.setOnItemSelectedListener(this);
        // if(options == null) options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        setOptionsOrHint(options.toArray(new Option[options.size()]));
    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Validation validation = Validation.getInstance();
        return validation.validate(spAnswer, question.getValidationFunction(), isMendatory);
    }

    @Override
    public void setOptionsOrHint(Option... data) {
        if (data.length > 0) {
            dataList = new ArrayList<>();
            mOptions = new HashMap<>();
            for (int i = 0; i < data.length; i++) {
                Option option = data[i];
                String optionText = option.getText();
                String translatedText = Translator.getInstance().Translate(optionText, GlobalPreferences.getinstance(context).findLanguagePrferenceValue());
                if (translatedText != null) {
                    mOptions.put(translatedText, option);
                    dataList.add(translatedText);
                } else {
                    mOptions.put(optionText, option);
                    dataList.add(optionText);
                }
            }
            mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dataList);
            spAnswer.setAdapter(mAdapter);
        }
    }

    public void addOption(Option value) {
        mOptions.put(value.getText(), value);
        dataList.add(value.getText());
        mAdapter.notifyDataSetChanged();
        spAnswer.setSelection(dataList.indexOf(value.getText()));
    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();
        if (isValidInput(question.isMandatory())) {
            dismissMessage();
            if (spAnswer.getSelectedItem().toString().equals("<Select an option>") || spAnswer.getSelectedItem().toString().equals("")) {
                param.put(ParamNames.PARAM_NAME, null);
            } else {
                Option ans = mOptions.get(spAnswer.getSelectedItem().toString());
                if (Objects.equals(ans.getUuid(), "") || ans.getUuid() == null) {
                    param.put(ParamNames.PARAM_NAME,question.getParamName());
                    param.put(ParamNames.VALUE, ans.getText());
                } else {
                    param.put(ParamNames.PARAM_NAME,question.getParamName());
                    param.put(ParamNames.VALUE,  ans.getUuid());
                }
            }
        } else {
            activity.addValidationError(getQuestionId(), question.getErrorMessage());
        }

        //Necessary for every widget to have PAYLOAD_TYPE AND PERSON_ATTRIBUTE
        param.put(ParamNames.PAYLOAD_TYPE, question.getPayload_type().toString());
        if(question.getAttribute())
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_TRUE);
        else
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_FALSE);


        return param;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // inform the user class about value change
        if(onValueChangeListener != null) {
            try {
                onValueChangeListener.onValueChanged(spAnswer.getSelectedItem().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (Global.SCOREABLE_QUESTIONS.contains(question.getQuestionId())) {
            Option o = options.get(arg2);
            if (o == null) {
                System.out.println("asd");
            }
            activity.addInRating(oldOptionScore - (oldOptionScore * 2));
            activity.addInRating(o.getScore());
            oldOptionScore = o.getScore();
        }
        // TODO onItemClickListener can optimize the performance or try to make use of onNothingSelected
        try {
            int[] showables = options.get(arg2).getOpensQuestions();
            int[] hideables = options.get(arg2).getHidesQuestions();
            // if(getVisibility()==View.VISIBLE)
            ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables, question);
            // TODO disables this feature for CAD4TB project
            /*if("other".equals(val.toLowerCase(Locale.US))) {
				temp = (TextView) arg1;
				activity.startActivityForResult(new Intent(context, ManualInput.class), getQuestionId());
			}*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void setOther(Option value) {
        if (value == null) {
            // TODO make last selected item selected here in future
            spAnswer.setSelection(0);
            return;
        }
        try {
            if (!Validation.getInstance().areAllSpacesOrNull(value.getText())) {
                addOption(value);
            } else {
                // TODO make last selected item selected here in future
                spAnswer.setSelection(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        int questionId = question.getQuestionId();
        try {
            if (questionId == 21006) {
                System.out.println("");
            }
            if (spAnswer != null) {
                if(options.size() < 1) throw new Exception("Options not provided for question number "+questionId+" , widget type: "+ SpinnerWidget.class.getName());
                // int[] showables = findAlldependantShowAbles();
                if (visibility == View.VISIBLE) {
                    int[] showables = options.get(spAnswer.getSelectedItemPosition()).getOpensQuestions();
                    int[] hideables = options.get(spAnswer.getSelectedItemPosition()).getHidesQuestions();
                    ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables, question);
                } else {
                    int[] hideables = findAlldependantHideAbles();
                    ((BaseActivity) getContext()).onChildViewItemSelected(null, hideables, question);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

		/*if(visibility == View.GONE) {
			if(Global.SCOREABLE_QUESTIONS.contains(question.getQuestionId())) {

				activity.addInRating(oldOptionScore-(oldOptionScore*2));
				activity.addInRating(0);
				// oldOptionScore = o.getScore();
			}
		} else {
			String val = spAnswer.getSelectedItem().toString();
			if(Global.SCOREABLE_QUESTIONS.contains(question.getQuestionId())) {
				Option o = findOptionByText(mOptions.get(val));
				if(o==null) {
					System.out.println("asd");
				}
				activity.addInRating(oldOptionScore-(oldOptionScore*2));
				activity.addInRating(o.getScore());
				oldOptionScore = o.getScore();
			}
		}*/
        super.setVisibility(visibility);
    }

    public String value() {
        return spAnswer.getSelectedItem().toString();
    }

    private int[] findAlldependantShowAbles() {
        int[] toReturn = new int[0];
        for (Option o : options) {
            int[] array = o.getOpensQuestions();
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

    @Override
    public void setClickable(boolean clickable) {
        spAnswer.setClickable(clickable);
        super.setClickable(clickable);
    }

    public void setselectedIndex(int index) {
        spAnswer.setSelection(index);
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (View lol : spAnswer.getTouchables()) {
            lol.setEnabled(enabled);
        }
        spAnswer.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public void onFocusGained() {
        spAnswer.performClick();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    @Override
    public void setAnswer(String answer, String uuid, LANGUAGE language) {
        answer = Translator.getInstance().Translate(answer, language);
        int i = dataList.indexOf(answer);
        if (i == -1) {
            addOption(new Option(question.getQuestionId(), -1, null, null, uuid, answer, -1));
            spAnswer.setSelection(dataList.indexOf(answer));
            return;
        }
        spAnswer.setSelection(dataList.indexOf(answer));
        setVisibility(View.VISIBLE);
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

    @Override
    public String getValue() {
        return spAnswer.getSelectedItem().toString();
    }

    @Override
    public String getServiceHistoryValue() {
        return getValue();
    }
}
