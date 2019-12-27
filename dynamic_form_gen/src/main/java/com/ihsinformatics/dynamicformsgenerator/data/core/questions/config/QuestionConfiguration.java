package com.ihsinformatics.dynamicformsgenerator.data.core.questions.config;

import android.text.InputType;

import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.DateSelector;

import java.util.Date;

public class QuestionConfiguration extends Configuration {

	
	private int inputType;
	private int maxLength;
	private int minLenght;
	private String allowedCharacters;
	private int id;
	
	//For date widget
	private Date maxDate;
	private Date minDate;
    private DateSelector.WIDGET_TYPE widgetType;


	private int maxValue;
	private int minValue;
	private int maxLines;

	public QuestionConfiguration() {
		
	}

    public DateSelector.WIDGET_TYPE getWidgetType() {
        return widgetType;
    }

    public QuestionConfiguration(Date maxDate, Date minDate, DateSelector.WIDGET_TYPE widgetType, int id) {
		super();
		this.maxDate = maxDate;
		this.minDate = minDate;
		this.id = id;
        this.widgetType = widgetType;
	}
	
	public QuestionConfiguration(int inputType,
			int maxLength, int minLenght, String allowedCharacters, int id) {
		super();
		this.inputType = inputType;
		this.maxLength = maxLength;
		this.minLenght = minLenght;
		this.allowedCharacters = allowedCharacters;
		this.id = id;
	}

	//TODO Need to convert date to date format rather string  ~Taha
	//TODO id needed the utilize widgetType given in json format in config field  ~Taha


	public QuestionConfiguration(int inputType,
								 int maxLength, int minLenght, String allowedCharacters, int id,int maxValue, int minValue,String maxDate,String minDate, int maxLines) {
		super();
		this.inputType = inputType;
		this.maxLength = maxLength;
		this.minLenght = minLenght;
		this.allowedCharacters = allowedCharacters;
		this.id = id;
		this.maxValue=maxValue;
		this.minValue=minValue;
		this.maxLines=maxLines;

	}



	public int getInputType() {
		return inputType;
	}

	public void setInputType(int inputType) {
		this.inputType = inputType;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMinLenght() {
		return minLenght;
	}

	public void setMinLenght(int minLenght) {
		this.minLenght = minLenght;
	}

	public String getAllowedCharacters() {
		return allowedCharacters;
	}

	public void setAllowedCharacters(String allowedCharacters) {
		this.allowedCharacters = allowedCharacters;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public Date getMinDate() {
		return minDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}
	
}
