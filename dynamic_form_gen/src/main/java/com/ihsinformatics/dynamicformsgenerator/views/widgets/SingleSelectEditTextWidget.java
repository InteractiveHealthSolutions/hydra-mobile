package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleSelectEditTextWidget extends InputWidget implements PopupMenu.OnMenuItemClickListener {
    protected AutoCompleteTextView etAnswer;
    private ArrayAdapter<String> mAdapter;
    // ArrayList<String> mData;
    private HashMap<String, String> mOptions;
    private HashMap<String, Integer> optionValuePairs;
    ArrayList<String> dataList;
    private ImageButton ivSuggestions;
    private boolean secureDeadLock = false;
    private PopupMenu popupMenu;
    private QuestionConfiguration configuration;

    public SingleSelectEditTextWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if(super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        etAnswer = (AutoCompleteTextView) findViewById(R.id.etAnswer);
        // if(options == null) options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        ivSuggestions = (ImageButton) findViewById(R.id.imageButton);
        optionValuePairs = new HashMap<>();
        popupMenu = new PopupMenu(context, etAnswer);
        if (options.size() > 0) {
            String[] items = new String[options.size()];
            for (int i = 0; i < options.size(); i++) {
                items[i] = options.get(i).getText();
                String translatedText = getTranslatedText(options.get(i).getText());
                popupMenu.getMenu().add(question.getQuestionId(), i, i, translatedText != null ? translatedText : options.get(i).getText());
                optionValuePairs.put(options.get(i).getUuid(), i);
            }
            //   setOptionsOrHint(items);
            setOptionsOrHint(options.toArray(new Option[options.size()]));
        }

        InputFilter[] filterArray;
        String allowedCharacters = configuration.getAllowedCharacters();
        if(allowedCharacters!=null) {
            filterArray = new InputFilter[2];
            filterArray[1] = DigitsKeyListener.getInstance(configuration.getAllowedCharacters());
        } else {
            filterArray = new InputFilter[1];
        }
        filterArray[0] = new InputFilter.LengthFilter(configuration.getMaxLength());

        etAnswer.setInputType(configuration.getInputType());
        etAnswer.setFilters(filterArray);
        etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (optionValuePairs.containsKey(str)) {
                    int itemIndex = optionValuePairs.get(str);
                    secureDeadLock = true;
                    onMenuItemClick(popupMenu.getMenu().getItem(itemIndex));
                } else {
                    handleShowHideAll();
                    /*if(str.equals("")) {
                        hideAll();
                    } else {
                        showAll();
                    }*/
                }
            }
        });
        popupMenu.setOnMenuItemClickListener(this);
        ivSuggestions.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // etAnswer.getText().clear();
                popupMenu.show();
            }
        });
    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Validation validation = Validation.getInstance();
        if (configuration.getMinLenght() >= 0) {
            if (etAnswer.getText().toString().length() == 0 && !isMendatory) {
            } else if (etAnswer.getText().toString().length() < configuration.getMinLenght()) {
                return false;
            }
        }
        return validation.validate(etAnswer, question.getValidationFunction(), isMendatory);
    }

    @Override
    public void setOptionsOrHint(Option... data) {
        if (data.length > 0) {
            dataList = new ArrayList<String>();
            mOptions = new HashMap<String, String>();
            for (int i = 0; i < data.length; i++) {
                String translatedText = getTranslatedText(data[i].getText());
                if (translatedText != null) {
                    mOptions.put(translatedText, data[i].getText());
                    dataList.add(translatedText);
                } else {
                    mOptions.put(data[i].getText(), data[i].getText());
                    dataList.add(data[i].getText());
                }
            }
            mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dataList);
            etAnswer.setAdapter(mAdapter);
        }
    }

    private String getTranslatedText(String text) {
        return Translator.getInstance().Translate(text, GlobalPreferences.getinstance(context).findLanguagePrferenceValue());
    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();

        //Necessary for every widget to have PAYLOAD_TYPE AND PERSON_ATTRIBUTE
        param.put(ParamNames.PAYLOAD_TYPE, question.getPayload_type().toString());
        if(question.getAttribute())
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_TRUE);
        else
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_FALSE);


        if (isValidInput(question.isMandatory())) {
            if (etAnswer.getText().toString().length() == 0 && !question.isMandatory()) {
                param.put(question.getParamName(), null);
            } else {
                param.put(question.getParamName(), etAnswer.getText().toString());
            }
            dismissMessage();
        } else {
            activity.addValidationError(getQuestionId(), question.getErrorMessage());
        }
        return param;
    }

    @Override
    public void onFocusGained() {
        etAnswer.requestFocus();
    }

    @Override
    public void setAnswer(String answer, String uuid, LANGUAGE language) {
        etAnswer.setText(answer);
    }

    @Override
    public void setEnabled(boolean enabled) {
        etAnswer.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String val = item.getTitle().toString();
        int position = item.getItemId();
        /*if(Global.SCOREABLE_QUESTIONS.contains(question.getQuestionId())) {
            Option o = findOptionByText(mOptions.get(val));
			if(o==null) {
				System.out.println("asd");
			}
			activity.addInRating(oldOptionScore-(oldOptionScore*2));
			activity.addInRating(o.getScore());
			oldOptionScore = o.getScore();

		}*/
        // TODO onItemClickListener can optimize the performance or try to make use of onNothingSelected
        handleShowHide(position);
        return false;
    }

    private void handleShowHide(int position) {
        int[] showables = options.get(position).getOpensQuestions();
        int[] hideables = options.get(position).getHidesQuestions();
        // if(getVisibility()==View.VISIBLE) {
        ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables, question);
        if (!secureDeadLock) {
            etAnswer.setText(options.get(position).getUuid());
        } else {
            secureDeadLock = false;
        }
        // }
        /*if("other".equals(val.toLowerCase(Locale.US))) {
            temp = (TextView) arg1;
            activity.startActivityForResult(new Intent(context, ManualInput.class), getQuestionId());
        }*/
    }

    private void handleShowHideAll() {
        if (getVisibility() == View.VISIBLE) {
            ArrayList<Integer> allShowAbles = new ArrayList<>();
            ArrayList<Integer> allHideAbles = new ArrayList<>();
            for (int i = 0; i < options.size(); i++) {
                int[] tempOpenQuestions = options.get(i).getOpensQuestions();
                int[] tempHideQuestions = options.get(i).getHidesQuestions();
                if (tempOpenQuestions != null) {
                    for (int j = 0; j < tempOpenQuestions.length; j++) {
                        allShowAbles.add(tempOpenQuestions[j]);
                    }
                }
                if (tempHideQuestions != null) {
                    for (int j = 0; j < tempHideQuestions.length; j++) {
                        allHideAbles.add(tempHideQuestions[j]);
                    }
                }
            }
            int[] showables = new int[allShowAbles.size()];
            for (int i = 0; i < allShowAbles.size(); i++) {
                showables[i] = allShowAbles.get(i);
            }
            int[] hideables = new int[allHideAbles.size()];
            for (int i = 0; i < allHideAbles.size(); i++) {
                hideables[i] = allHideAbles.get(i);
            }
            ((BaseActivity) getContext()).onChildViewItemSelected(hideables, showables, question);
        }
    }

    private void showAll() {
        if (getVisibility() == View.VISIBLE) {
            ArrayList<Integer> allShowAbles = new ArrayList<>();
            for (int i = 0; i < options.size(); i++) {
                int[] temp = options.get(i).getOpensQuestions();
                if (temp != null) {
                    for (int j = 0; j < temp.length; j++) {
                        allShowAbles.add(temp[j]);
                    }
                }
            }
            int[] showables = new int[allShowAbles.size()];
            for (int i = 0; i < allShowAbles.size(); i++) {
                showables[i] = allShowAbles.get(i);
            }
            int[] hideables = new int[]{};
            ((BaseActivity) getContext()).onChildViewItemSelected(showables, hideables, question);
        }
    }

    private void hideAll() {
        if (getVisibility() == View.VISIBLE) {
            ArrayList<Integer> allShowAbles = new ArrayList<>();
            for (int i = 0; i < options.size(); i++) {
                int[] temp = options.get(i).getOpensQuestions();
                if (temp != null) {
                    for (int j = 0; j < temp.length; j++) {
                        allShowAbles.add(temp[j]);
                    }
                }
            }
            int[] showables = new int[allShowAbles.size()];
            for (int i = 0; i < allShowAbles.size(); i++) {
                showables[i] = allShowAbles.get(i);
            }
            int[] hideables = new int[]{};
            ((BaseActivity) getContext()).onChildViewItemSelected(hideables, showables, question);
        }
    }

    @Override
    public String getValue() {
        return etAnswer.getText().toString();
    }

    @Override
    public String getServiceHistoryValue() {
        return getValue();
    }
}
