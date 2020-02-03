package com.ihsinformatics.dynamicformsgenerator.data.core.questions;

import android.view.View;

import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Displayable;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.Configuration;
import com.ihsinformatics.dynamicformsgenerator.data.utils.SkipLogic;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;
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

    public static enum PAYLOAD_TYPE {
        IDENTIFIER,
        PERSON_ATTRIBUTE,
        NAME,
        OBS,
        OBS_CODED,
        OBS_NUMERIC,
        OBS_CODED_MULTI,
        OBS_DATE_TIME,
        LOCATION,
        ENCOUNTER_TYPE,   //need to resolve
        AGE,
        GENDER,
        DOB,
        DATE_ENTERED    //need to resolve
    }

    private Boolean isAttribute = false;
    private PAYLOAD_TYPE payload_type;

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


    private List<SExpression> visibleWhen;
    private List<SExpression> hiddenWhen;
    private List<SExpression> requiredWhen;

    public List<SExpression> getVisibleWhen() {
        return visibleWhen;
    }

    public void setVisibleWhen(List<SExpression> visibleWhen) {
        this.visibleWhen = visibleWhen;
    }

    public List<SExpression> getHiddenWhen() {
        return hiddenWhen;
    }

    public void setHiddenWhen(List<SExpression> hiddenWhen) {
        this.hiddenWhen = hiddenWhen;
    }

    public List<SExpression> getRequiredWhen() {
        return requiredWhen;
    }

    public void setRequiredWhen(List<SExpression> requiredWhen) {
        this.requiredWhen = requiredWhen;
    }


    public Boolean getAttribute() {
        return isAttribute;
    }

    public void setAttribute(Boolean attribute) {
        isAttribute = attribute;
    }

    // if this question is generataed on runtime
    private boolean isRuntimeGenerated = false;

    private List<Question> repeatables;
    private String repeatGroupHeadingPrefix;

    public void addRepeatable(Question question) {
        if (repeatables == null) repeatables = new ArrayList<>();

        repeatables.add(question);
    }

    public List<Question> getRepeatables() {
        return repeatables;
    }

    public Question() {

    }

    public Question(boolean isMandatory, int formTypeId, int questionId, String questionNumber, String questionType, String initialVisibility, String validationFunction, String text, String paramName, Configuration questionConfiguration,Boolean attribute, String inputType ,List<SExpression> visibleWhen, List<SExpression> hiddenWhen, List<SExpression> requiredWhen) {
        super();
        this.isMandatory = isMandatory;
        this.formTypeId = formTypeId;
        this.questionId = questionId;


        setQuestionType(questionType);
        setInitialVisibility(initialVisibility);

        this.validationFunction = validationFunction;
        this.text = text;
        this.paramName = paramName;
        this.questionConfiguration = questionConfiguration;
        this.questionNumber = questionNumber;
        this.tag = QUESTION_TAG.TAG_OBS.toString();

        this.visibleWhen = visibleWhen;
        this.hiddenWhen = hiddenWhen;
        this.requiredWhen = requiredWhen;

        isAttribute=attribute;
        payload_type=filterPayloadType(questionType,inputType);
    }




    public Question(boolean isMandatory, int formTypeId, int questionId, String questionNumber, String questionType, String initialVisibility, String validationFunction, String text, String paramName, Configuration questionConfiguration, QUESTION_TAG tag) {
        super();
        this.isMandatory = isMandatory;
        this.formTypeId = formTypeId;
        this.questionId = questionId;

        setQuestionType(questionType);
        setInitialVisibility(initialVisibility);

        this.validationFunction = validationFunction;
        this.text = text;
        this.paramName = paramName;
        this.questionConfiguration = questionConfiguration;
        this.questionNumber = questionNumber;
        this.tag = tag.toString();
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

    public void addOption(Option option) {
        if (options == null) options = new ArrayList<>();

        options.add(option);
    }

    public PAYLOAD_TYPE getPayload_type() {
        return payload_type;
    }

    public void setPayload_type(PAYLOAD_TYPE payload_type) {
        this.payload_type = payload_type;
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

    public void setQuestionType(String questionType) {

        switch (questionType) {
            case "Date/ Time Picker": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_DATE;
                break;
            }
            case "Textbox": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_EDITTEXT;
                break;
            }
            case "Single Select Dropdown": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_SPINNER;
                break;
            }
            case "score_spinner": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_SCORE_SPINNER;
                break;
            }
            case "Multiple Choice": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER;
                break;
            }
            case "Single Select Radiobuttons": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON;
                break;
            }
//            case "check_box": {
//                this.questionType = InputWidgetsType.WIDGET_TYPE_CHECK_BOX;
//                break;
//            }
            case "Heading": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_HEADING;
                break;
            }
            case "gps": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_GPS;
                break;
            }
            case "hidden_input": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_HIDDEN_INPUT;
                break;
            }
            case "Age": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_AGE;
                break;
            }
            case "image": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_IMAGE;
                break;
            }
            case "single_select_edittext": {
                this.questionType = InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT;
                break;
            }
            case "single_select_textview": {
                this.questionType = InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW;
                break;
            }
            case "qr_reader": {
                this.questionType = InputWidgetsType.WIDGETS_TYPE_QR_READER;
                break;
            }
            case "identifier": {
                this.questionType = InputWidgetsType.WIDGETS_TYPE_IDENTIFIER;
                break;
            }
            case "autocomplete_edittext": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_AUTOCOMPLETE_EDITTEXT;
                break;
            }
            case "Address": {
                this.questionType = InputWidgetsType.WIDGET_TYPE_ADDRESS;
                break;
            }
        }
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
        if (options != null) {
            for (Option o : options) {
                int[] showables = o.getOpensQuestions();
                if (showables != null)
                    for (int i = 0; i < showables.length; i++) {
                        showables[i] = showables[i] + (uniqueId * repeatCount);
                    }

                int[] hideables = o.getHidesQuestions();
                if (hideables != null)
                    for (int i = 0; i < hideables.length; i++) {
                        hideables[i] = hideables[i] + (uniqueId * repeatCount);
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
        if (repeatables != null) {
            List<Question> questionsClone = new ArrayList(repeatables.size());
            for (Question item : repeatables) questionsClone.add((Question) item.clone());
            clone.repeatables = questionsClone;
        }

        if (options != null) {
            List<Option> optionsClone = new ArrayList(options.size());
            for (Option item : options) optionsClone.add((Option) item.clone());
            clone.options = optionsClone;
        }

        return clone;
    }

    public int getInitialVisibility() {
        return initialVisibility;
    }

    public void setInitialVisibility(String initialVisibilityStr) {
        if (initialVisibilityStr.equalsIgnoreCase("VISIBLE"))
            this.initialVisibility = View.VISIBLE;
        else if (initialVisibilityStr.equalsIgnoreCase("GONE"))
            this.initialVisibility = View.GONE;
    }


    public PAYLOAD_TYPE filterPayloadType(String questionType,String inputType){
        PAYLOAD_TYPE payload=PAYLOAD_TYPE.OBS;

        if(isAttribute){
            return PAYLOAD_TYPE.PERSON_ATTRIBUTE;
        }

        switch (questionType) {
            case "Date/ Time Picker": {
                return PAYLOAD_TYPE.OBS_DATE_TIME;

            }
            case "Textbox": {

                if(inputType.equalsIgnoreCase("text"))
                    return PAYLOAD_TYPE.OBS;
                else if(inputType.equalsIgnoreCase("numeric"))
                    return PAYLOAD_TYPE.OBS_NUMERIC;

            }
            case "Single Select Dropdown": {
                return PAYLOAD_TYPE.OBS_CODED;
            }

            case "Multiple Choice": {
               return PAYLOAD_TYPE.OBS_CODED_MULTI;
            }
            case "Single Select Radiobuttons": {
               return PAYLOAD_TYPE.OBS_CODED;
            }
            case "Age": {
             return PAYLOAD_TYPE.AGE;
            }

            case "identifier": {
                return PAYLOAD_TYPE.IDENTIFIER;
            }

            case "Address": {
                return PAYLOAD_TYPE.LOCATION;
            }
        }

        return payload;
    }
}
