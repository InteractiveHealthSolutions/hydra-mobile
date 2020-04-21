package com.ihsinformatics.dynamicformsgenerator.data.core.options;

import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class Option implements Cloneable {

	protected int questionId;
	protected int optionId;
	protected int[] opensQuestions;
	protected int[] hidesQuestions;
	protected String uuid;
	protected String text;
	protected int score;
	protected boolean isDefault=false;


	public Option(int questionId, int optionId, int[] opensQuestions, int[] hidesQuestions,
				  String uuid, String text, int score,Boolean isDefault) {
		super();
		this.questionId = questionId;
		this.optionId = optionId;
		this.opensQuestions = opensQuestions;
		this.hidesQuestions = hidesQuestions;
		this.uuid = uuid;
		this.text = text;
		this.score = score;
		this.isDefault=isDefault;

	}

	public Option(int questionId, int optionId, int[] opensQuestions, int[] hidesQuestions,
			String uuid, String text, int score) {
		this(questionId, optionId, opensQuestions, hidesQuestions, uuid, text, score,false);

	}

	public Option(int questionId, int optionId, int[] opensQuestions, int[] hidesQuestions, String uuid, String text) {
		this(questionId, optionId, opensQuestions, hidesQuestions, uuid, text, -1);

	}


	public int getQuestionId() {
		return questionId;
	}


	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}


	public int getOptionId() {
		return optionId;
	}


	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}


	public int[] getOpensQuestions() {
		return opensQuestions;
	}


	public void setOpensQuestions(int[] opensQuestions) {
		this.opensQuestions = opensQuestions;
	}

	

	public int[] getHidesQuestions() {
		return hidesQuestions;
	}


	public void setHidesQuestions(int[] hidesQuestions) {
		this.hidesQuestions = hidesQuestions;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	@Override
	public String toString() {
		return getText();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Option clone = (Option) super.clone();

		if(opensQuestions!=null) {
			int[] opensClone = new int[opensQuestions.length];
			for (int i = 0; i < opensQuestions.length; i++)
				System.arraycopy(opensQuestions, 0, opensClone, 0, opensQuestions.length);
			clone.opensQuestions = opensClone;
		}

		if(hidesQuestions!=null) {
			int[] hidesClone = new int[hidesQuestions.length];
			for (int i = 0; i < hidesQuestions.length; i++)
				System.arraycopy(hidesQuestions, 0, hidesClone, 0, hidesQuestions.length);
			clone.hidesQuestions = hidesClone;
		}
		return clone;
	}
}
