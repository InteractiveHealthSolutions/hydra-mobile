package ihsinformatics.com.hydra_mobile.data.core.question.config;
import ihsinformatics.com.hydra_mobile.ui.dialogs.DateSelector;

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

	public QuestionConfiguration() {
		
	}

 /*   public DateSelector.WIDGET_TYPE getWidgetType() {
        return widgetType;
    }*/

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

	public DateSelector.WIDGET_TYPE getWidgetType() {
		return widgetType;
	}

	public void setWidgetType(DateSelector.WIDGET_TYPE widgetType) {
		this.widgetType = widgetType;
	}
}
