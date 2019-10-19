package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.view.View;
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
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreSpinner extends InputWidget implements OnItemSelectedListener {

	TextView temp;
	private Spinner spAnswer;
	private ArrayAdapter<String> mAdapter;
	// ArrayList<String> mData;
	HashMap<String, String> mOptions;
	private int oldOptionScore;
	ArrayList<String> dataList;

	public ScoreSpinner(Context context, Question question, int layoutId) {
		super(context, question,layoutId);
		spAnswer = (Spinner) findViewById(R.id.spAnswer);
		// setBackgroundColor(getResources().getColor(android.R.color.transparent));
		spAnswer.setOnItemSelectedListener(this);
		spAnswer.setVisibility(View.GONE);
		findViewById(R.id.llQuestion).setVisibility(View.GONE);
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
		if(data.length > 0) {
			dataList = new ArrayList<String>();
			mOptions = new HashMap<String, String>();
			
			for(int i=0; i<data.length; i++) {
				String translatedText = Translator.getInstance().Translate(data[i].getText(), GlobalPreferences.getinstance(context).findLanguagePrferenceValue());
				if(translatedText != null) {
					mOptions.put(translatedText, data[i].getText());
					dataList.add(translatedText);
				} else {
					mOptions.put(data[i].getText(), data[i].getText());
					dataList.add(data[i].getText());
					
				}
				
				
			}
			
			mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dataList);
			spAnswer.setAdapter(mAdapter);
		}
	}
	
	public void addOption(String value) {
			mOptions.put(value, value);
			dataList.add(value);
			mAdapter.notifyDataSetChanged();
			spAnswer.setSelection(dataList.indexOf(value));
	}
	
	@Override
	public JSONObject getAnswer() throws JSONException {
		JSONObject param = new JSONObject();
		if (isValidInput(question.isMandatory())) {
			dismissMessage();
			if(spAnswer.getSelectedItem().toString().equals("<Select an option>")) {
                param.put(question.getParamName(), null);
			} else {
				LANGUAGE lang = GlobalPreferences.getinstance(context).findLanguagePrferenceValue();
				if (lang != LANGUAGE.ENGLISH) {
					String ans = mOptions.get(spAnswer.getSelectedItem().toString());
					param.put(question.getParamName(), ans);
				} else {
					param.put(question.getParamName(), spAnswer.getSelectedItem().toString());
				}
			}
			
		} else {
			activity.addValidationError(getQuestionId(), "Invalid input");
		}
		
		return param;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        callOnItemSelectionChange();
		
	}
private void callOnItemSelectionChange() {
    String val = spAnswer.getSelectedItem().toString();
    int arg2 = spAnswer.getSelectedItemPosition();
    if(Global.SCOREABLE_QUESTIONS.contains(question.getQuestionId())) {
        Option o = findOptionByText(mOptions.get(val));
        if(o==null) {
            System.out.println("asd");
        }
        activity.addInRating(oldOptionScore-(oldOptionScore*2));
        activity.addInRating(o.getScore());
        oldOptionScore = o.getScore();
    }
    // TODO onItemClickListener can optimize the performance or try to make use of onNothingSelected
    try {
        int[] showables = options.get(arg2).getOpensQuestions();
        int[] hideables = options.get(arg2).getHidesQuestions();
        // if(getVisibility()==View.VISIBLE)
        ((BaseActivity)getContext()).onChildViewItemSelected(showables, hideables, question);

        // TODO disables this feature for CAD4TB project
			/*if("other".equals(val.toLowerCase(Locale.US))) {
				temp = (TextView) arg1;
				activity.startActivityForResult(new Intent(context, ManualInput.class), getQuestionId());
			}*/

    } catch(Exception e) {
        e.printStackTrace();
    }
}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void setOther(String value) {
		if(value == null) {
			// TODO make last selected item selected here in future
			spAnswer.setSelection(0);
			return;
		}
		try {
			if(!Validation.getInstance().areAllSpacesOrNull(value)){
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
		if(spAnswer!=null) {
			// int[] showables = findAlldependantShowAbles();
			if(visibility == View.VISIBLE) {
				int[] showables = options.get(spAnswer.getSelectedItemPosition()).getOpensQuestions();
				int[] hideables = options.get(spAnswer.getSelectedItemPosition()).getHidesQuestions();
				((BaseActivity)getContext()).onChildViewItemSelected(showables, hideables, question);
			} else {
				int[] hideables = findAlldependantHideAbles();
				((BaseActivity)getContext()).onChildViewItemSelected(null, hideables, question);
			}
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
	
	private int[] findAlldependantShowAbles() {
		int[] toReturn = new int[0];
		for(Option o: options) {
			int[] array = o.getOpensQuestions();
			if(array!=null) {
				int[] temp = toReturn;
				toReturn = new int[array.length+toReturn.length];
				
				for(int i=toReturn.length-array.length; i<toReturn.length; i++) {
					toReturn[i] = (array[i-temp.length]);
				}
				
				for(int i=0; i<temp.length; i++) {
					toReturn[i] = temp[i];
				}
			
			}
		}
		
		return toReturn;
	}
	
	private int[] findAlldependantHideAbles() {
		int[] toReturn = new int[0];
		for(Option o: options) {
			int[] array = o.getHidesQuestions();
			if(array!=null) {
				int[] temp = toReturn;
				toReturn = new int[array.length+toReturn.length];
				for(int i=toReturn.length-array.length; i<toReturn.length; i++) {
					toReturn[i] = (array[i-temp.length]);
				}
				for(int i=0; i<temp.length; i++) {
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
        callOnItemSelectionChange();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		
		for(View lol : spAnswer.getTouchables() ) {
		    lol.setEnabled(enabled);
		}
		
		super.setEnabled(enabled);
	}

	@Override
	public void onFocusGained() {
		spAnswer.performClick();
		
	}
	
	@Override
	public void setAnswer(String answer, String uuid, LANGUAGE language) {
		answer = Translator.getInstance().Translate(answer, language);
		int i = dataList.indexOf(answer);
		if(i == -1) {
			addOption(answer);
			spAnswer.setSelection(dataList.indexOf(answer));
			return;
		}
		
		spAnswer.setSelection(dataList.indexOf(answer));
		setVisibility(View.VISIBLE);
		
	}

	private Option findOptionByText(String text) {
		for(Option o:options) {
			if(o.getText().equals(text)) {
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
	
}
