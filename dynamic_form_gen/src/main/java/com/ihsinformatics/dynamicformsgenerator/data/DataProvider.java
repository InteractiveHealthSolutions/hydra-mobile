package com.ihsinformatics.dynamicformsgenerator.data;

import android.content.Context;
import android.text.InputType;
import android.view.View;


import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.core.Form;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SExpression;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SkipLogics;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.AddressConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormType;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.DateSelector;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.FIRST_NAME;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.LAST_NAME;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.SEX;

public class DataProvider {
    private static Calendar cal;
    private static DataProvider dataProvider;
    private static Date lastMonday;
    private static Date lastYear;
    private static Date nextYear;
    private static java.util.Date projectStartDate;
    private static Date today;
    private static Date date110YearsAgo;
    private LinkedHashMap<Integer, String> encounterTypes;
    private List<Form> forms;
    private List<Option> options;
    private List<Question> questions;
    private Context context;
    private final String degree_sign = "°";
    QuestionConfiguration mobileNumber, landlineNumber, circumcisionIdentifier, alphaNumeric160DigitSpace, alphaNumeric300DigitSpace, screenerInitials, dateMinTodayMaxLastMonday, dateTimeMinTodayMaxLastMonday, dateMinTodayMaxNextYear, dateMinTodayMaxNextYearTime, dateMinLastYearMaxNextYear, time, sid, dob, numeric2Digit, numeric3DigitMin1, numeric3DigitMin2, numeric4DigitMin1, numeric5Digit, numeric6Digit, numeric8Digit, alpha20DigitSpace, alpha25Digit, alpha30DigitSpace, alpha40DigitSpace, numeric10Digit, numeric11Digit, numeric13Digit, numeric12Digit, numeric33Digit, alpha50DigitSpace, alpha150DigitSpace, alpha150DigitAll, alphaNumeric50DigitSpace, alphaNumeric60DigitSpace, alpha60DigitSpace, alpha50DigitSpaceDot, alpha80DigitSpace, alpha7DigitSpace, alpha50DigitSpaceCapsOnly, alpha100DigitSpace, alpha5DigitSpace, alpha10DigitSpaceWithHyphen, numeric3DigitWithHypen, numeric12DigitWithHypen, numeric13DigitWithHypen, alphanumeric10DigitWithHypen, alphanumeric13DigitWithHypen, alphanumeric100DigitSpace, alphaNumeric150DigitSpace, alpha150DigitSpaceMin3, alpha160DigitSpace, alphaNumeric200DigitSpace, alphaNumeric100DigitSpace, numericDecimal4Digit, alphaMax30Min3Digit;
    AddressConfiguration addressConfiguration;

    // No patient is needed to be loaded before opening these forms
    public static List<String> directOpenableForms = new ArrayList<>();

    static {
        directOpenableForms.add(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT);
    }

    private DataProvider(Context context) {
        this.context = context;
        initInstance();
    }

    private void initInstance() {
        initDates();
        // REGISTRATION_ENCOUNTER = ENCOUNTER_TYPE_PATIENT_CREATION;
        encounterTypes = new LinkedHashMap<Integer, String>();
        encounterTypes.put(1, ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT);
        encounterTypes.put(2, ParamNames.ENCOUNTER_TYPE_PATIENT_INFO);
        encounterTypes.put(3, ParamNames.ENCOUNTER_TYPE_ADULT_SCREENING);
        encounterTypes.put(4, ParamNames.ENCOUNTER_TYPE_CHILD_SCREENING);
        encounterTypes.put(5, ParamNames.ENCOUNTER_TYPE_CHILD_CLINICAL_EVALUATION);
        encounterTypes.put(6, ParamNames.ENCOUNTER_TYPE_ADULT_CLINICAL_EVALUATION);
        encounterTypes.put(7, ParamNames.ENCOUNTER_TYPE_ADULT_TX_INITIATION);
        encounterTypes.put(8, ParamNames.ENCOUNTER_TYPE_CHILD_TX_INITIATION);
        encounterTypes.put(9, ParamNames.ENCOUNTER_TYPE_EOF);
        encounterTypes.put(10, ParamNames.ENCOUNTER_TYPE_CONTACT_REGISTRY);
        encounterTypes.put(11, ParamNames.ENCOUNTER_TB_DISEASE_CONFIRMATION);


        questions = new ArrayList<Question>();
        options = new ArrayList<Option>();

        numericDecimal4Digit = new QuestionConfiguration(InputType.TYPE_CLASS_PHONE, 4, 1, "0123456789.", 1);
        mobileNumber = new QuestionConfiguration(InputType.TYPE_CLASS_PHONE, 12, 12, "0123456789-", 1);
        landlineNumber = new QuestionConfiguration(InputType.TYPE_CLASS_PHONE, 12, 12, "0123456789-", 1);
        screenerInitials = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 2, 2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha5DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 5, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha7DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 7, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alphaMax30Min3Digit = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 30, 3, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha20DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 20, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha25Digit = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 25, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha30DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 30, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha40DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 40, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha50DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 50, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alphaNumeric50DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 50, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alphaNumeric100DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 100, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha50DigitSpaceDot = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_WORDS, 50, -1, " .abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alphaNumeric60DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 60, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha60DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 60, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha80DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 80, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha100DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 100, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        circumcisionIdentifier = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 11, 11, "0123456789-ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha150DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 150, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha150DigitAll = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 150, -1, null, 1);
        alpha150DigitSpaceMin3 = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 150, 3, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha160DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 160, -1, " 0213456789-,/:abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alphaNumeric160DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 160, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alphaNumeric300DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 160, -1, " ,:0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alphaNumeric200DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 200, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-/?.!", 1);
        alpha50DigitSpaceCapsOnly = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 50, -1, " ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1);
        alpha10DigitSpaceWithHyphen = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 10, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-", 1);
        dateMinTodayMaxLastMonday = new QuestionConfiguration(today, projectStartDate, DateSelector.WIDGET_TYPE.DATE, 8);
        dateTimeMinTodayMaxLastMonday = new QuestionConfiguration(today, projectStartDate, DateSelector.WIDGET_TYPE.DATE_TIME, 8);
        dateMinTodayMaxNextYear = new QuestionConfiguration(nextYear, today, DateSelector.WIDGET_TYPE.DATE, 8);
        dateMinTodayMaxNextYearTime = new QuestionConfiguration(nextYear, today, DateSelector.WIDGET_TYPE.DATE_TIME, 8);
        dateMinLastYearMaxNextYear = new QuestionConfiguration(nextYear, lastYear, DateSelector.WIDGET_TYPE.DATE, 8);

        // defining address hierarchy
        AddressConfiguration.OpenAddressField openAddressField = new AddressConfiguration.OpenAddressField(
                1,
                "Address",
                alphaNumeric160DigitSpace,
                true,
                ParamNames.ADDRESS2);
        List<AddressConfiguration.OpenAddressField> openAddressFields = new ArrayList<>();
        openAddressFields.add(openAddressField);
        addressConfiguration = new AddressConfiguration(
                openAddressFields,
                new AddressConfiguration.AddressTag(1, "Province/State"),
                new AddressConfiguration.AddressTag(3, "City/Village"));
        System.out.print("");

        // TODO create separate configuration class for the date/ time
        time = new QuestionConfiguration(today, lastMonday, DateSelector.WIDGET_TYPE.TIME, 8);
        dob = new QuestionConfiguration(today, date110YearsAgo, DateSelector.WIDGET_TYPE.DATE, 9);
        sid = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 13, 13, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", 7);
        numeric2Digit = new QuestionConfiguration(InputType.TYPE_CLASS_NUMBER, 2, 1, "1234567890", 7);
        numeric3DigitMin2 = new QuestionConfiguration(InputType.TYPE_CLASS_NUMBER, 3, 2, "1234567890", 7);
        numeric3DigitMin1 = new QuestionConfiguration(InputType.TYPE_CLASS_NUMBER, 3, 1, "1234567890", 7);
        numeric4DigitMin1 = new QuestionConfiguration(InputType.TYPE_NUMBER_FLAG_DECIMAL, 4, 1, "1234567890.", 7);
        numeric5Digit = new QuestionConfiguration(InputType.TYPE_NUMBER_FLAG_DECIMAL, 5, 2, "1234567890.", 7);
        numeric6Digit = new QuestionConfiguration(InputType.TYPE_CLASS_NUMBER, 6, 2, "1234567890", 7);
        numeric8Digit = new QuestionConfiguration(InputType.TYPE_CLASS_NUMBER, 8, 8, "1234567890", 7);
        numeric10Digit = new QuestionConfiguration(InputType.TYPE_CLASS_NUMBER, 10, 10, "1234567890", 7);
        numeric11Digit = new QuestionConfiguration(InputType.TYPE_CLASS_NUMBER, 11, 11, "1234567890", 7);
        numeric12Digit = new QuestionConfiguration(InputType.TYPE_CLASS_NUMBER, 12, 12, "1234567890", 7);
        numeric13Digit = new QuestionConfiguration(InputType.TYPE_CLASS_NUMBER, 13, 13, "1234567890", 7);
        numeric33Digit = new QuestionConfiguration(InputType.TYPE_CLASS_NUMBER, 33, 9, "1234567890", 7);
        alphanumeric10DigitWithHypen = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 10, 10, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-", 7);
        alphanumeric13DigitWithHypen = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 13, 13, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-", 7);
        alphanumeric100DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 100, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-.", 1);
        alphaNumeric150DigitSpace = new QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 150, -1, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-.", 1);
        numeric3DigitWithHypen = new QuestionConfiguration(InputType.TYPE_NUMBER_FLAG_SIGNED, 3, 1, "1234567890-", 7);
        numeric12DigitWithHypen = new QuestionConfiguration(InputType.TYPE_NUMBER_FLAG_SIGNED, 12, 12, "1234567890-", 7);
        numeric13DigitWithHypen = new QuestionConfiguration(InputType.TYPE_NUMBER_FLAG_SIGNED, 13, 13, "1234567890-", 7);


        initPatientCreation();




    }

    public static DataProvider getInstance(Context context) {
        if (dataProvider == null) {
            dataProvider = new DataProvider(context);
        } else {
            initDates();
        }
        return dataProvider;
    }

    public static void reInitializeInstance(Context context) {
        dataProvider = new DataProvider(context);
    }

    public String generateUUID() {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();

        return randomUUIDString;
    }


    private void initPatientCreation() {
        Integer patientCreationId = 1;


        questions.add(new Question(false, patientCreationId, 6999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Patient Registration Form", null, null,Question.PAYLOAD_TYPE.HEADING));
        this.questions.add(new Question(true, patientCreationId, 6000, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_IDENTIFIER, View.VISIBLE, Validation.CHECK_FOR_MRNO, "Identifier", ParamNames.PROJECT_IDENTIFIER, alphanumeric13DigitWithHypen, Question.PAYLOAD_TYPE.IDENTIFIER));
        this.questions.add(new Question(true, patientCreationId, 6001, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_NAME, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Patient's name", FIRST_NAME, alphaMax30Min3Digit, Question.PAYLOAD_TYPE.NAME));
        this.questions.add(new Question(true, patientCreationId, 6003, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Gender", SEX, null,Question.PAYLOAD_TYPE.GENDER));
        this.options.add(new Option(6003, 604, null, null, "", "Male", -1));
        this.options.add(new Option(6003, 605, null, null, "", "Female", -1));
        //   this.questions.add(new Question(true, patientCreationId, 6004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Umar (Age in years)", "age", numeric3DigitMin1));
        this.questions.add(new Question(true, patientCreationId, 6004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_AGE, View.VISIBLE, Validation.CHECK_FOR_DATE, "Date of Birth", ParamNames.DOB, dob,Question.PAYLOAD_TYPE.DOB));
        //  this.questions.add(new Question(true, patientCreationId, 6005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Ghar ka patta - Ghar/Street #", "address1", alpha150DigitSpace));


         this.questions.add(new Question(true, patientCreationId, 6006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Location", "location", null,Question.PAYLOAD_TYPE.LOCATION));
        this.options.addAll(DynamicOptions.getLocationOptionsFromDataAccess(context, 6006, null, null));

//        this.questions.add(new Question(true, patientCreationId, 6008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday,Question.PAYLOAD_TYPE.DATE_ENTERED));



    }


    private static void initDates() {
        try {
            projectStartDate = new SimpleDateFormat("yyyyMMdd").parse("20160601");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.set(Calendar.DAY_OF_WEEK, 2);
        lastMonday = localCalendar.getTime();
        cal = Calendar.getInstance();
        today = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        nextYear = cal.getTime();
        cal.add(Calendar.YEAR, -2);
        lastYear = cal.getTime();
        cal.add(Calendar.YEAR, -109);
        date110YearsAgo = cal.getTime();
    }

    public String getEncounterName(int paramInt) {
        return (String) this.encounterTypes.get(Integer.valueOf(paramInt));
    }

    public String[] getEncounterNames() {
        // TODO performance improvement needed
        String[] formNames = new String[encounterTypes.size()];
        Iterator<Integer> it = encounterTypes.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            formNames[i] = encounterTypes.get(it.next());
            i++;
        }
        return formNames;
    }

    public List<FormType> getFormTypes() {
        ArrayList formTypes = new ArrayList();
        Iterator<Integer> it = encounterTypes.keySet().iterator();
        while (it.hasNext()) {
            int index = it.next();
            formTypes.add(new FormType(index, encounterTypes.get(index)));
        }
        return formTypes;
    }

    public FormType getFormType(String formType) {
        Iterator<Integer> it = encounterTypes.keySet().iterator();
        while (it.hasNext()) {
            int index = it.next();
            String type = encounterTypes.get(index);
            if (type.equals(formType)) {
                return new FormType(index, encounterTypes.get(index));
            }
        }
        return null;
    }

    public int getFormId(String paramString) {
        int index;
        FormType formType;
        Iterator<Integer> it = encounterTypes.keySet().iterator();
        while (it.hasNext()) {
            index = it.next();
            if (encounterTypes.get(index).equals(paramString)) {
                return index;
            }
        }
        return -1;
    }

    public List<Option> getOptions(int paramInt) {
        ArrayList localArrayList = new ArrayList();
        Enumeration<Option> localIterator = Collections.enumeration(this.options);
        for (; ; ) {
            if (!localIterator.hasMoreElements()) {
                return localArrayList;
            }
            Option localOption = (Option) localIterator.nextElement();
            if (localOption.getQuestionId() == paramInt) {
                localArrayList.add(localOption);
            }
        }
    }

    public String getOptionsByUUID(String paramString, int questionId) {
        Enumeration<Option> localIterator = Collections.enumeration(this.options);
        for (; ; ) {
            if (!localIterator.hasMoreElements()) {
                return null;
            }
            Option localOption = (Option) localIterator.nextElement();
            if (localOption.getUuid() != null) {
                if (localOption.getUuid().equals(paramString) && localOption.getQuestionId() == questionId)
                    return localOption.getText();
            }
        }
    }

    public List<Option> getOptionsByQuestionsID(int questionId) {
        Enumeration<Option> localIterator = Collections.enumeration(this.options);
        List<Option> op=new ArrayList<>();
        for (; ; ) {
            if (!localIterator.hasMoreElements()) {
                return op;
            }
            Option localOption = (Option) localIterator.nextElement();
            if (localOption.getUuid() != null) {

                if (localOption.getQuestionId() == questionId)
                    op.add(localOption);
            }
        }
    }

    public List<Question> getQuestions(int formId) {
        ArrayList localArrayList = new ArrayList();
        Enumeration<Question> localIterator = Collections.enumeration(this.questions);
        for (; ; ) {
            if (!localIterator.hasMoreElements()) {
                return localArrayList;
            }
            Question localQuestion = (Question) localIterator.nextElement();
            if (localQuestion.getFormTypeId() == formId) {
                localArrayList.add(localQuestion);
            }
        }
    }

    public Question getQuestionByParamName(String paramString) {
        Enumeration<Question> localIterator = Collections.enumeration(this.questions);
        for (; ; ) {
            if (!localIterator.hasMoreElements()) {
                return null;
            }
            Question localQuestion = (Question) localIterator.nextElement();
            if (localQuestion.getParamName() != null)
                if (localQuestion.getParamName().equals(paramString))
                    return localQuestion;
        }
    }

    public Question getQuestionByParam(String paramString, String formName) {
        int formId = getFormId(formName);
        List<Question> questions = getQuestions(formId);
        Enumeration<Question> localIterator = Collections.enumeration(questions);
        for (; ; ) {
            if (!localIterator.hasMoreElements()) {
                return null;
            }
            Question localQuestion = (Question) localIterator.nextElement();
            if (localQuestion.getParamName() != null)
                if (localQuestion.getParamName().equals(paramString))
                    return localQuestion;
        }
    }

    public void reload() {
        initInstance();
    }
}