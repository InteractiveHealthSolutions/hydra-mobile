package com.ihsinformatics.dynamicformsgenerator.data.core.questions;

import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Displayable;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.Configuration;
import com.ihsinformatics.dynamicformsgenerator.data.utils.SkipLogic;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget.InputWidgetsType;

import java.util.ArrayList;
import java.util.List;

public class Question extends Displayable implements Cloneable {

	public static enum QUESTION_TAG {
		TAG_ATTRIBUTE,
		TAG_OBS,
		TAG_IDENTIFIER,
		TAG_LOCATION,
		TAG_ADDRESS
	}
	private int formTypeId;
	private boolean isMandatory;
	private int questionId;
	private InputWidgetsType questionType;
	private int initialVisibility;
	private String validationFunction;
	private String text;
	private String paramName;
	private String questionNumber;
	private Configuration questionConfiguration;
	private List<Option> options;
	private String tag;


	private List<SkipLogics> visibleWhen;
	private List<SkipLogics> hiddenWhen;
	private List<SkipLogics> requiredWhen;

	public List<SkipLogics> getVisibleWhen() {
		return visibleWhen;
	}

	public void setVisibleWhen(List<SkipLogics> visibleWhen) {
		this.visibleWhen = visibleWhen;
	}

	public List<SkipLogics> getHiddenWhen() {
		return hiddenWhen;
	}

	public void setHiddenWhen(List<SkipLogics> hiddenWhen) {
		this.hiddenWhen = hiddenWhen;
	}

	public List<SkipLogics> getRequiredWhen() {
		return requiredWhen;
	}

	public void setRequiredWhen(List<SkipLogics> requiredWhen) {
		this.requiredWhen = requiredWhen;
	}

	// if this question is generataed on runtime
	private boolean isRuntimeGenerated = false;

	private List<Question> repeatables;
	private String repeatGroupHeadingPrefix;

	public void addRepeatable(Question question) {
		if(repeatables==null) repeatables = new ArrayList<>();

		repeatables.add(question);
	}

	public List<Question> getRepeatables() {
		return repeatables;
	}

	public Question() {

	}

	public Question(boolean isMandatory, int formTypeId, int questionId, String questionNumber, InputWidgetsType questionType, int initialVisibility, String validationFunction, String text, String paramName, Configuration questionConfiguration) {
		super();
		this.isMandatory = isMandatory;
		this.formTypeId = formTypeId;
		this.questionId = questionId;
		this.questionType = questionType;
		this.initialVisibility = initialVisibility;
		this.validationFunction = validationFunction;
		this.text = text;
		this.paramName = paramName;
		this.questionConfiguration = questionConfiguration;
		this.questionNumber = questionNumber;
		this.tag = QUESTION_TAG.TAG_OBS.toString();
	}

	public Question(boolean isMandatory, int formTypeId, int questionId, String questionNumber, InputWidgetsType questionType, int initialVisibility, String validationFunction, String text, String paramName, Configuration questionConfiguration, QUESTION_TAG tag) {
		super();
		this.isMandatory = isMandatory;
		this.formTypeId = formTypeId;
		this.questionId = questionId;
		this.questionType = questionType;
		this.initialVisibility = initialVisibility;
		this.validationFunction = validationFunction;
		this.text = text;
		this.paramName = paramName;
		this.questionConfiguration = questionConfiguration;
		this.questionNumber = questionNumber;
		this.tag = tag.toString();
	}

	public void addOption(Option option) {
		if(options == null) options = new ArrayList<>();

		options.add(option);
	}

	public List<Option> getOptions() {
		return options;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getFormTypeId() {
		return formTypeId;
	}

	public void setFormTypeId(int formTypeId) {
		this.formTypeId = formTypeId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public InputWidgetsType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(InputWidgetsType questionType) {
		this.questionType = questionType;
	}

	public int getInitialVisibility() {
		return initialVisibility;
	}

	public void setInitialVisibility(int initialVisibility) {
		this.initialVisibility = initialVisibility;
	}


	public String getValidationFunction() {
		return validationFunction;
	}


	public void setValidationFunction(String uuid) {
		this.validationFunction = uuid;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}

	public void setRepeatables(List<Question> repeatables) {
		this.repeatables = repeatables;
	}

	public String getRepeatGroupHeadingPrefix() {
		return repeatGroupHeadingPrefix;
	}

	public void setRepeatGroupHeadingPrefix(String repeatGroupHeadingPrefix) {
		this.repeatGroupHeadingPrefix = repeatGroupHeadingPrefix;
	}

	public String getParamName() {
		return paramName;
	}


	public void setParamName(String paramName) {
		this.paramName = paramName;
	}


	public Configuration getQuestionConfiguration() {
		return questionConfiguration;
	}


	public void setQuestionConfiguration(Configuration questionConfiguration) {
		this.questionConfiguration = questionConfiguration;
	}

	public boolean isRuntimeGenerated() {
		return isRuntimeGenerated;
	}

	public void setRuntimeGenerated(boolean runtimeGenerated) {
		isRuntimeGenerated = runtimeGenerated;
	}

	public String getQuestionNumber() {
		return questionNumber;
	}


	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}

	public void updateSkipLogicReferences(int uniqueId, int repeatCount) {
		if(options!=null) {
			for(Option o: options) {
				int[] showables = o.getOpensQuestions();
				if(showables!=null)
					for(int i=0; i<showables.length; i++) {
						showables[i] = showables[i]+(uniqueId*repeatCount);
					}

				int[] hideables = o.getHidesQuestions();
				if(hideables!=null)
					for(int i=0; i<hideables.length; i++) {
						hideables[i] = hideables[i]+(uniqueId*repeatCount);
					}

				o.setOpensQuestions(showables);
				o.setHidesQuestions(hideables);
			}
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// Start deep cloning only objects those are needed to be cloned, other will be using the same reference

		// clone the question
		Question clone = (Question) super.clone();

		// cloning the repeatables
		if(repeatables!=null) {
			List<Question> questionsClone = new ArrayList(repeatables.size());
			for (Question item : repeatables) questionsClone.add((Question) item.clone());
			clone.repeatables = questionsClone;
		}

		if(options!=null) {
			List<Option> optionsClone = new ArrayList(options.size());
			for (Option item : options) optionsClone.add((Option) item.clone());
			clone.options = optionsClone;
		}

		return clone;
	}


}
