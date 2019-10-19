package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;

import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;

import org.json.JSONException;
import org.json.JSONObject;

public class HeadingWidget extends InputWidget {

	public HeadingWidget(Context context, Question question, int layoutId) {
		super(context, question,layoutId);
	}

	@Override
	public boolean isValidInput(boolean isNullable) {
		
		return true;
	}

	@Override
	public void setOptionsOrHint(Option... data) {
	}

	@Override
	public JSONObject getAnswer() throws JSONException {
		
		return null;
	}

	@Override
	public void onFocusGained() {
	}

	@Override
	public void setAnswer(String answer, String uuid, LANGUAGE language) {
		// Not Applicable
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getValue() {
		return null;
	}
}
