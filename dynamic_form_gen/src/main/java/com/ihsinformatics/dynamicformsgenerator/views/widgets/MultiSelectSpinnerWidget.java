package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.Translator;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.screens.Form;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.MultiSelectSpinner;

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
        // if(options == null) options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        setOptionsOrHint(options.toArray(new Option[options.size()]));
    }

    @Override
    public boolean isValidInput(boolean isMandatory) {
        if (mspAnswer.getSelectedValues().size() > 0 && isMandatory) {
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
            activity.addValidationError(getQuestionId(), question.getErrorMessage());
        }
        return param;
    }

    private void addParams(JSONObject param) throws JSONException {
        JSONArray subParams = new JSONArray();
        List<String> selections = mspAnswer.getSelectedValues();
        for (String s : selections) {
            subParams.put(s);
        }
        param.put(ParamNames.PARAM_NAME, question.getParamName());
        param.put(ParamNames.VALUE, subParams);
        param.put(ParamNames.PAYLOAD_TYPE, question.getPayload_type());

    }

    @Override
    public void onFocusGained() {
        mspAnswer.inflate();
    }

    @Override
    public void setAnswer(String answer, String uuid, LANGUAGE language) {

        String[] allAnswers = answer.split("\n/<end>\n");
        mspAnswer.setValuesForEdit(allAnswers);

    }

    @Override
    public void applySkipLogic(int position) throws JSONException {
        boolean isSelected = mspAnswer.isSelected(position);
        int[] showables = null;
        int[] hideables = null;
        if (isSelected) {
            showables = options.get(position).getOpensQuestions();
        } else {
            hideables = options.get(position).getOpensQuestions();
        }

        // int[] hideables = options.get(position).getHidesQuestions();

        ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables, question);
        ((BaseActivity) getContext()).checkSkipLogics(((BaseActivity) getContext()).getFormId(Form.getENCOUNTER_NAME()));

    }

    @Override
    public void revertSkipLogic(int position) throws JSONException {
        int[] showables = options.get(position).getOpensQuestions();
        int[] hideables = options.get(position).getHidesQuestions();
        ((BaseActivity) getContext()).onChildViewItemSelected(hideables, showables, question);
        ((BaseActivity) getContext()).checkSkipLogics(((BaseActivity) getContext()).getFormId(Form.getENCOUNTER_NAME()));
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

//    @Override
//    public void setVisibility(int visibility) {
//
//        if (mspAnswer != null) {
//            // int[] showables = findAlldependantShowAbles();
//            if (visibility == View.VISIBLE) {
//                List<Integer> selectedItemsIndexes = mspAnswer.getSelectedValuesPositions();
//                for (int i : selectedItemsIndexes) {
//                    try {
//                        applySkipLogic(i);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//           /* int[] showables = options.get(spAnswer.getSelectedItemPosition()).getOpensQuestions();
//            int[] hideables = options.get(spAnswer.getSelectedItemPosition()).getHidesQuestions();
//            ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables, question);*/
//            } else {
//                int[] hideables = findAlldependantHideAbles();
//                ((BaseActivity) getContext()).onChildViewItemSelected(null, hideables, question);
//            }
//        }
//        super.setVisibility(visibility);
//    }

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
    public String getValue() throws JSONException {

        return getAnswer().toString();
    }

    @Override
    public String getServiceHistoryValue() throws JSONException {
        String toReturn = "";

        List<String> selections = mspAnswer.getSelectedValues();
        for (String s : selections) {
            toReturn += s + "\n/<end>\n";
        }

        return toReturn;
    }
}
