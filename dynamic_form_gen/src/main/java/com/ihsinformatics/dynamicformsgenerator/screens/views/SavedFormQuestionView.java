package com.ihsinformatics.dynamicformsgenerator.screens.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.dynamicformsgenerator.R;

public class SavedFormQuestionView extends LinearLayout  {

	private TextView tvQuestion;
	private TextView tvAnswer;
	private Context context;
	private TextView tvNumber;
	private String question;

	public SavedFormQuestionView(Context context, String question, String answer) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.view_question_answer, this, false);
		addView(view);
		this.question = question;
		tvQuestion = (TextView) findViewById(R.id.tvQuestion);
		tvAnswer = (TextView) findViewById(R.id.tvAnswer);
		//tvNumber = (TextView) findViewById(R.id.tvNumber);

		tvQuestion.setText(question);
		tvAnswer.setText(answer);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tvAnswer == null) ? 0 : tvAnswer.hashCode());
		result = prime * result + ((tvQuestion == null) ? 0 : tvQuestion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(question.equals(((SavedFormQuestionView)obj).question.toString())) {
			return true;
		}
		
		return false;
	}
}
