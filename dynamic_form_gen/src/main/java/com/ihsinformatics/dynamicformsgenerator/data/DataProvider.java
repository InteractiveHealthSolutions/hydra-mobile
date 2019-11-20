package com.ihsinformatics.dynamicformsgenerator.data;

import android.content.Context;
import android.text.InputType;
import android.view.View;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.core.Form;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.AddressConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormType;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.RangeOption;
import com.ihsinformatics.dynamicformsgenerator.data.utils.SkipRange;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.DateSelector;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.FIRST_NAME;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.FORM3_ABSENT;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.FORM3_NO;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.FORM3_NOT_SATISFIED;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.FORM3_PRESENT;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.FORM3_SATISFIED;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.FORM3_YES;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.FORM_DATE;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.ICM_ID_NUMBER;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.LAST_NAME;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.MR_NUMBER;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PATIENT_NAME;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.POST_OP_SURGERY_PERFORMED_OTHER;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_ASSIST_YEARS_SCHOOLING;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_ATTENDED_SCHOOL;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_CAN_READ_WRITE;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_CONTACT1;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_CONTACT1_RELATIONSHIP;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_CONTACT2;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_CONTACT2_RELATIONSHIP;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_FORMAL_SCHOOLING_YEARS;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_HAVE_MOBILE;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_HAVE_MOBILE_ACCESS;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_INFORMAL_SCHOOLING;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_MARITAL_STATUS;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_MOBILE_CAMERA;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_MOBILE_INTERNET;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_MOBILE_USAGE_DURATION;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_PRIMARY_LANGUAGE;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_PRIMARY_LANGUAGE_OTHER;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_RURAL_URBAN;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_SMS;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_WHO_ASSIST;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PRE_OP_WHO_ASSIST_OTHER;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.PROCEDURE_DATE;
import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.SEX;

public class DataProvider {
    private static Calendar cal;
    private static DataProvider dataProvider;
    private static Date lastMonday;
    private static Date lastYear;
    private static Date nextYear;
    private static Date projectStartDate;
    private static Date today;
    private static Date date110YearsAgo;
    private LinkedHashMap<Integer, String> encounterTypes;
    private List<Form> forms;
    private List<Option> options;
    private List<Question> questions;
    private Context context;
    private final String degree_sign = "°";
    QuestionConfiguration mobileNumber, landlineNumber, circumcisionIdentifier, alphaNumeric160DigitSpace, alphaNumeric300DigitSpace, screenerInitials, dateMinTodayMaxLastMonday, dateTimeMinTodayMaxLastMonday, dateMinTodayMaxNextYear, dateMinTodayMaxNextYearTime, dateMinLastYearMaxNextYear, time, sid, dob, numeric2Digit, numeric3DigitMin1, numeric3DigitMin2, numeric4DigitMin1, numeric5Digit, numeric6Digit, numeric8Digit, alpha20DigitSpace, alpha25Digit, alpha30DigitSpace, alpha40DigitSpace, numeric10Digit, numeric11Digit, numeric13Digit, numeric12Digit, numeric33Digit, alpha50DigitSpace, alpha150DigitSpace, alpha150DigitAll, alphaNumeric50DigitSpace, alphaNumeric60DigitSpace, alpha60DigitSpace, alpha50DigitSpaceDot, alpha80DigitSpace, alpha7DigitSpace, alpha50DigitSpaceCapsOnly, alpha100DigitSpace, alpha5DigitSpace, alpha10DigitSpaceWithHyphen, numeric3DigitWithHypen, numeric12DigitWithHypen, numeric13DigitWithHypen, alphanumeric10DigitWithHypen, alphanumeric13DigitWithHypen, alphanumeric100DigitSpace, alphaNumeric150DigitSpace, alpha150DigitSpaceMin3, alpha160DigitSpace, alphaNumeric200DigitSpace, alphaNumeric100DigitSpace, numericDecimal4Digit;
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
                new AddressConfiguration.AddressTag(1, "Province"),
                new AddressConfiguration.AddressTag(3, "Division"),
                new AddressConfiguration.AddressTag(4, "District"),
                new AddressConfiguration.AddressTag(5, "Tehsil"),
                new AddressConfiguration.AddressTag(6, "Union"));
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
        initPatientInformation();
        endFollowUpForm();
        initChildScreeningForm();
        initAdultScreeningForm();
        initChildClinicalEvaluationForm();
        initAdultClinicalEvaluationForm();
        initContactRegistryForm();
        initChildTBInitiationForm();
        initAdultTBInitiationForm();
        initTBDiseaseConfirmationForm();



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


    private void initChildTBInitiationForm() {
        Integer childTBInitiationFormId = 8;


//        this.questions.add(new Question(true,childTBInitiationFormId,53003,"4",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Location",generateUUID(),null));
//        this.questions.add(new Question(false,childTBInitiationFormId,53004,"5",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Longitude",generateUUID(),null));
//        this.questions.add(new Question(false,childTBInitiationFormId,53005,"6",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Latitude",generateUUID(),null));
//        this.questions.add(new Question(true,childTBInitiationFormId,53006,"7",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Weight Percentile",generateUUID(),null));

        this.questions.add(new Question(true, childTBInitiationFormId, 53000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday));


        this.questions.add(new Question(true, childTBInitiationFormId, 53007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Did the MO Consult any Senior Pediatrician for TB diagnosis?", generateUUID(), null));
        this.options.add(new Option(53007, 5301, new int[]{53008, 53009}, null, "1", "Yes", -1));
        this.options.add(new Option(53007, 5302, null, new int[]{53008, 53009}, "2", "No", -1));

        this.questions.add(new Question(true, childTBInitiationFormId, 53008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Name of Consultant", generateUUID(), alpha20DigitSpace));
        this.questions.add(new Question(true, childTBInitiationFormId, 53009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Reason for Consultation", generateUUID(), alpha40DigitSpace));


        this.questions.add(new Question(true, childTBInitiationFormId, 53010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the patient have TB?", generateUUID(), null));
        this.options.add(new Option(53010, 5303, new int[]{53013}, null, "1", "Yes", -1));
        this.options.add(new Option(53010, 5304, null, new int[]{53013}, "2", "No", -1));

        this.questions.add(new Question(true, childTBInitiationFormId, 53011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Registration date", generateUUID(), dateMinLastYearMaxNextYear));

        //TODO The codebook says to check specific type of tb here

        this.questions.add(new Question(false, childTBInitiationFormId, 53012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "TB Registration No.", generateUUID(), numeric13DigitWithHypen));
        this.questions.add(new Question(true, childTBInitiationFormId, 53013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Was treatment initiated?", generateUUID(), null));
        this.options.add(new Option(53013, 5305, new int[]{53021, 53023}, new int[]{53014}, "1", "Yes", -1));
        this.options.add(new Option(53013, 5306, new int[]{53014}, new int[]{53021, 53023}, "2", "No", -1));

        this.questions.add(new Question(true, childTBInitiationFormId, 53014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Reason the treatment was not initiated", generateUUID(), null));
        this.options.add(new Option(53014, 5307, null, new int[]{53015}, "1", "Patient Refused Treatment", -1));
        this.options.add(new Option(53014, 5308, null, new int[]{53015}, "2", "Patient lost to follow up", -1));
        this.options.add(new Option(53014, 5309, null, new int[]{53015}, "3", "Patient Died", -1));
        this.options.add(new Option(53014, 5310, null, new int[]{53015}, "4", "Referral (before start of treatment)", -1));
        this.options.add(new Option(53014, 5311, new int[]{53015}, null, "5", "Other", -1));


        this.questions.add(new Question(true, childTBInitiationFormId, 53015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If other, please specify", generateUUID(), alphanumeric100DigitSpace));


        this.questions.add(new Question(false, childTBInitiationFormId, 53016, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "TB Treatment Initiation", null, null));
        this.questions.add(new Question(true, childTBInitiationFormId, 53017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Duration of sickness prior to TB diagnosis ", generateUUID(), null));
        this.options.add(new Option(53017, 5312, null, null, "1", "Less than or equal to 1 month", -1));
        this.options.add(new Option(53017, 5313, null, null, "2", "2-6 Months", -1));
        this.options.add(new Option(53017, 5314, null, null, "3", "6-12 Months", -1));
        this.options.add(new Option(53017, 5315, null, null, "4", "1-2 Years", -1));
        this.options.add(new Option(53017, 5316, null, null, "5", "2-5 Years", -1));
        this.options.add(new Option(53017, 5317, null, null, "6", "More than or equal to 5 years", -1));
        this.options.add(new Option(53017, 5318, null, null, "7", "Unknown", -1));


        this.questions.add(new Question(true, childTBInitiationFormId, 53018, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "History of past TB drug use", generateUUID(), null));
        this.options.add(new Option(53018, 5319, null, null, "1", "Previously treated only with first line drugs", -1));
        this.options.add(new Option(53018, 5320, null, null, "2", "Previously treated with secondline drugs", -1));
        this.options.add(new Option(53018, 5321, null, null, "3", "Previously treated with FLD (PET)", -1));
        this.options.add(new Option(53018, 5322, null, null, "4", "Previously treated with SLD (PET)", -1));
        this.options.add(new Option(53018, 5323, null, null, "5", "Previously treated with FLD & SLD both", -1));
        this.options.add(new Option(53018, 5324, null, null, "6", "History unclear/unknown", -1));
        this.options.add(new Option(53018, 5325, null, null, "7", "None", -1));


        this.questions.add(new Question(true, childTBInitiationFormId, 53019, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Patient Type", generateUUID(), null));
        this.options.add(new Option(53019, 5326, null, null, "1", "New", -1));
        this.options.add(new Option(53019, 5327, null, null, "2", "Relapse", -1));
        this.options.add(new Option(53019, 5328, null, null, "3", "Treatment after failure", -1));
        this.options.add(new Option(53019, 5329, null, null, "4", "Other previously treated", -1));
        this.options.add(new Option(53019, 5330, null, null, "5", "Treatment after lost to follow-up", -1));
        this.options.add(new Option(53019, 5331, null, null, "6", "Transferred In", -1));
        this.options.add(new Option(53019, 5332, null, null, "7", "Other", -1));


        this.questions.add(new Question(true, childTBInitiationFormId, 52020, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Patient Category", generateUUID(), null));
        this.options.add(new Option(52020, 5333, null, null, "1", "Category I", -1));
        this.options.add(new Option(52020, 5334, null, null, "2", "Category II", -1));
        this.options.add(new Option(52020, 5335, null, null, "3", "Category III", -1));


        this.questions.add(new Question(true, childTBInitiationFormId, 53021, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Are you initiating additional treatment? Please select one or more treatment options if applicable to patient.", generateUUID(), null));
        this.options.add(new Option(52021, 5336, null, new int[]{53022}, "1", "Iron", -1));
        this.options.add(new Option(52021, 5337, null, new int[]{53022}, "2", "Multivitamins", -1));
        this.options.add(new Option(52021, 5338, null, new int[]{53022}, "3", "Anthelmintic", -1));
        this.options.add(new Option(52021, 5339, null, new int[]{53022}, "4", "Pediasure", -1));
        this.options.add(new Option(52021, 5340, null, new int[]{53022}, "5", "Vitamin B-complex", -1));
        this.options.add(new Option(52021, 5341, null, new int[]{53022}, "6", "Calpol", -1));
        this.options.add(new Option(52021, 5342, null, new int[]{53022}, "7", "Other", -1));
        this.options.add(new Option(52021, 5343, new int[]{53022}, null, "8", "None", -1));
        this.questions.add(new Question(true, childTBInitiationFormId, 53022, "15", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If other, specify", generateUUID(), alphanumeric100DigitSpace));


        this.questions.add(new Question(true, childTBInitiationFormId, 53023, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "What is the patient's current treatment plan?", generateUUID(), null));
        this.options.add(new Option(53023, 5344, null, null, "1", "Intensive Phase", -1));
        this.options.add(new Option(53023, 5345, null, null, "2", "Continuation Phase", -1));
        this.options.add(new Option(53023, 5346, null, null, "3", "End Treatment", -1));


//        this.questions.add(new Question(true, childTBInitiationFormId, 53028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Do you want to refer the patient?", generateUUID(), null));
//        this.options.add(new Option(53028, 5347, new int[]{53029}, null, "1", "Yes", -1));
//        this.options.add(new Option(53028, 5348, null, new int[]{53029}, "2", "No", -1));
//
//
//
//        this.questions.add(new Question(true, childTBInitiationFormId, 53029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Do you want to refer the patient to?", generateUUID(), null));
//        this.options.add(new Option(53029, 5349, new int[]{53030}, new int[]{53032,53034,53036}, "1", "Counselor", -1));
//        this.options.add(new Option(53029, 5350, new int[]{53030}, new int[]{53032,53034,53036}, "2", "Psychologist", -1));
//        this.options.add(new Option(53029, 5351, new int[]{53036}, new int[]{53030,53032,53034}, "1", "Clinician", -1));
//        this.options.add(new Option(53029, 5352, new int[]{53034}, new int[]{53030,53032,53036}, "2", "Call Center", -1));
//        this.options.add(new Option(53029, 5353, new int[]{53032}, new int[]{53030,53034,53036}, "2", "Field Supervisor", -1));
//        this.options.add(new Option(53029, 5354, new int[]{53032}, new int[]{53030,53034,53036}, "2", "Site Supervisor", -1));
//
//
//        this.questions.add(new Question(true, childTBInitiationFormId, 53030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Reason for referral to Psychologist/Counselor", generateUUID(), null));
//        this.options.add(new Option(53030, 5355, null, new int[]{53031}, "1", "To check adherence", -1));
//        this.options.add(new Option(53030, 5356, null, new int[]{53031}, "2", "Psychological Issues", -1));
//        this.options.add(new Option(53030, 5357, null, new int[]{53031}, "3", "Behavioral Issues", -1));
//        this.options.add(new Option(53030, 5358, null, new int[]{53031}, "4", "Refusal", -1));
//        this.options.add(new Option(53030, 5359, new int[]{53031}, null, "5", "Other", -1));
//        this.questions.add(new Question(true, childTBInitiationFormId, 53031, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other Specify", generateUUID(), alphanumeric100DigitSpace));
//
//
//        this.questions.add(new Question(true, childTBInitiationFormId, 53032, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Reason for referral to Field Supervisor/Site Supervisor", generateUUID(), null));
//        this.options.add(new Option(53032, 5360, null, new int[]{53033}, "1", "Reminder for Contact Screening", -1));
//        this.options.add(new Option(53032, 5361, null, new int[]{53033}, "2", "Reminder for treatment follow up", -1));
//        this.options.add(new Option(53032, 5362, null, new int[]{53033}, "3", "Check Treatment Adherence", -1));
//        this.options.add(new Option(53032, 5363, null, new int[]{53033}, "4", "Investigation report collection", -1));
//        this.options.add(new Option(53032, 5364, null, new int[]{53031}, "5", "Adverse Events", -1));
//        this.options.add(new Option(53032, 5365, null, new int[]{53033}, "6", "Medicine Collection", -1));
//        this.options.add(new Option(53032, 5366, new int[]{53033}, null, "7", "Other", -1));
//        this.questions.add(new Question(true, childTBInitiationFormId, 53033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other Specify", generateUUID(), alphanumeric100DigitSpace));
//
//
//        this.questions.add(new Question(true, childTBInitiationFormId, 53034, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Reason for referral to Call Center", generateUUID(), null));
//        this.options.add(new Option(53034, 5367, null, new int[]{53035}, "1", "Reminder for Contact Screening", -1));
//        this.options.add(new Option(53034, 5368, null, new int[]{53035}, "2", "Reminder for treatment follow up", -1));
//        this.options.add(new Option(53034, 5369, null, new int[]{53035}, "3", "Check Treatment Adherence", -1));
//        this.options.add(new Option(53034, 5370, null, new int[]{53035}, "4", "Investigation report collection", -1));
//        this.options.add(new Option(53034, 5371, null, new int[]{53035}, "5", "Adverse Events", -1));
//        this.options.add(new Option(53034, 5372, null, new int[]{53035}, "6", "Medicine Collection", -1));
//        this.options.add(new Option(53034, 5373, new int[]{53035}, null, "7", "Other", -1));
//        this.questions.add(new Question(true, childTBInitiationFormId, 53035, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other Specify", generateUUID(), null));
//
//
//        this.questions.add(new Question(true, childTBInitiationFormId, 53036, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Reason for referral to Clinician", generateUUID(), null));
//        this.options.add(new Option(53036, 5374, null, new int[]{53037}, "1", "Expert Opinion", -1));
//        this.options.add(new Option(53036, 5375, null, new int[]{53037}, "2", "Adverse Event", -1));
//        this.options.add(new Option(53036, 5376, new int[]{53037}, null, "3", "Other", -1));
//        this.questions.add(new Question(true, childTBInitiationFormId, 53037, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other Specify", generateUUID(), alphanumeric100DigitSpace));
//


        this.questions.add(new Question(false, childTBInitiationFormId, 53038, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Doctor's notes", generateUUID(), alphaNumeric300DigitSpace));

        this.questions.add(new Question(true, childTBInitiationFormId, 53039, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Is the follow up required?", generateUUID(), null));
        this.options.add(new Option(53039, 5377, new int[]{53040}, null, "1", "Yes", -1));
        this.options.add(new Option(53039, 5378, null, new int[]{53040}, "2", "No", -1));

        this.questions.add(new Question(false, childTBInitiationFormId, 53040, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY, "Next Appointment Date", generateUUID(), dateMinTodayMaxNextYear));


    }


    private void initAdultTBInitiationForm() {
        Integer adultTBInitiationFormId = 7;

//        this.questions.add(new Question(true,adultTBInitiationFormId,52003,"4",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Location",generateUUID(),null));
//        this.questions.add(new Question(false,adultTBInitiationFormId,52004,"5",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Longitude",generateUUID(),null));
//        this.questions.add(new Question(false,adultTBInitiationFormId,52005,"6",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Latitude",generateUUID(),null));
        this.questions.add(new Question(true, adultTBInitiationFormId, 52000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday));

        this.questions.add(new Question(true, adultTBInitiationFormId, 52006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the patient have TB?", generateUUID(), null));
        this.options.add(new Option(52006, 5201, new int[]{52007, 52008, 52015}, null, "1", "Yes", -1));
        this.options.add(new Option(52006, 5202, null, new int[]{52007, 52008, 52015}, "2", "No", -1));

        this.questions.add(new Question(false, adultTBInitiationFormId, 52007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "TB Registration No.", generateUUID(), numeric13DigitWithHypen));
        this.questions.add(new Question(true, adultTBInitiationFormId, 52008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Was treatment initiated?", generateUUID(), null));
        this.options.add(new Option(52008, 5203, new int[]{52016}, new int[]{52009}, "1", "Yes", -1));
        this.options.add(new Option(52008, 5204, new int[]{52009}, new int[]{52016}, "2", "No", -1));   //ToDo need to check this

        this.questions.add(new Question(true, adultTBInitiationFormId, 52009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Reason the treatment was not initiated", generateUUID(), null));
        this.options.add(new Option(52009, 5205, null, new int[]{52010}, "1", "Patient Refused Treatment", -1));
        this.options.add(new Option(52009, 5206, null, new int[]{52010}, "2", "Patient lost to follow up", -1));
        this.options.add(new Option(52009, 5207, null, new int[]{52010}, "3", "Patient Died", -1));
        this.options.add(new Option(52009, 5208, null, new int[]{52010}, "4", "Referral (before start of treatment)", -1));
        this.options.add(new Option(52009, 5209, new int[]{52010}, null, "5", "Other", -1));


        this.questions.add(new Question(true, adultTBInitiationFormId, 52010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If other, please specify", generateUUID(), alphanumeric100DigitSpace));


        this.questions.add(new Question(false, adultTBInitiationFormId, 52011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "TB Treatment Initiation", null, null));
        this.questions.add(new Question(true, adultTBInitiationFormId, 52012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Duration of sickness prior to TB diagnosis ", generateUUID(), null));
        this.options.add(new Option(52012, 5210, null, null, "1", "Less than or equal to 1 month", -1));
        this.options.add(new Option(52012, 5211, null, null, "2", "2-6 Months", -1));
        this.options.add(new Option(52012, 5212, null, null, "3", "6-12 Months", -1));
        this.options.add(new Option(52012, 5213, null, null, "4", "1-2 Years", -1));
        this.options.add(new Option(52012, 5214, null, null, "5", "2-5 Years", -1));
        this.options.add(new Option(52012, 5215, null, null, "6", "More than or equal to 5 years", -1));
        this.options.add(new Option(52012, 5216, null, null, "7", "Unknown", -1));


        this.questions.add(new Question(true, adultTBInitiationFormId, 52013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "History of past TB drug use", generateUUID(), alphanumeric100DigitSpace));
        this.options.add(new Option(52013, 5217, null, null, "1", "Previously treated only with first line drugs", -1));
        this.options.add(new Option(52013, 5218, null, null, "2", "Previously treated with secondline drugs", -1));
        this.options.add(new Option(52013, 5219, null, null, "3", "Previously treated with FLD (PET)", -1));
        this.options.add(new Option(52013, 5220, null, null, "4", "Previously treated with SLD (PET)", -1));
        this.options.add(new Option(52013, 5221, null, null, "5", "Previously treated with FLD & SLD both", -1));
        this.options.add(new Option(52013, 5222, null, null, "6", "History unclear/unknown", -1));
        this.options.add(new Option(52013, 5223, null, null, "7", "None", -1));


        this.questions.add(new Question(true, adultTBInitiationFormId, 52014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Patient Type", generateUUID(), null));
        this.options.add(new Option(52014, 5224, null, null, "1", "New", -1));
        this.options.add(new Option(52014, 5225, null, null, "2", "Relapse", -1));
        this.options.add(new Option(52014, 5226, null, null, "3", "Previously treated after failure (CAT-1)", -1));
        this.options.add(new Option(52014, 5227, null, null, "4", "Previously treated after failure (CAT-2)", -1));
        this.options.add(new Option(52014, 5228, null, null, "5", "Previously treated after failure (MDR)", -1));
        this.options.add(new Option(52014, 5229, null, null, "6", "Previously treated (treatment failure)", -1));
        this.options.add(new Option(52014, 5230, null, null, "7", "Previously treated (loss to follow-up)", -1));
        this.options.add(new Option(52014, 5231, null, null, "8", "Unknown", -1));
        this.options.add(new Option(52014, 5232, null, null, "9", "Others Previously treated", -1));


        this.questions.add(new Question(true, adultTBInitiationFormId, 52015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Patient Category", generateUUID(), null));
        this.options.add(new Option(52015, 5233, null, null, "1", "Category I", -1));
        this.options.add(new Option(52015, 5234, null, null, "2", "Category II", -1));


        this.questions.add(new Question(true, adultTBInitiationFormId, 52016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "What is the patient's current treatment plan?", generateUUID(), null));
        this.options.add(new Option(52016, 5235, null, null, "1", "Intensive Phase", -1));
        this.options.add(new Option(52016, 5236, null, null, "2", "Continuation Phase", -1));
        this.options.add(new Option(52016, 5237, null, null, "3", "End Treatment", -1));
//TODO CODE BOOK HERE SAYS OPEN ENDFOLLOWUP FORM WHEN DONE WHITH THIS UPON CERTAIL CONDIDITION


        //        this.questions.add(new Question(true,adultTBInitiationFormId,52017,"18",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Do you want to refer the patient?",generateUUID(),null));
//        this.questions.add(new Question(true,adultTBInitiationFormId,52018,"19",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Who do you want to refer the patient to?",generateUUID(),null));
//        this.questions.add(new Question(true,adultTBInitiationFormId,52019,"20",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Reason for referral to Psychologist/Counselor",generateUUID(),null));
//        this.questions.add(new Question(true,adultTBInitiationFormId,52020,"21",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Other Specify",generateUUID(),null));
//        this.questions.add(new Question(true,adultTBInitiationFormId,52021,"22",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Reason for referral to Field Supervisor/Site Supervisor",generateUUID(),null));
//        this.questions.add(new Question(true,adultTBInitiationFormId,52022,"23",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Other Specify",generateUUID(),null));
//        this.questions.add(new Question(true,adultTBInitiationFormId,52023,"24",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Reason for referral to Call Center",generateUUID(),null));
//        this.questions.add(new Question(true,adultTBInitiationFormId,52024,"25",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Other Specify",generateUUID(),null));
//        this.questions.add(new Question(true,adultTBInitiationFormId,52025,"26",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Reason for referral to Clinician",generateUUID(),null));
//        this.questions.add(new Question(true,adultTBInitiationFormId,52026,"27",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Other Specify",generateUUID(),null));
        this.questions.add(new Question(false, adultTBInitiationFormId, 52027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Is follow-up required?", generateUUID(), null));
        this.options.add(new Option(52027, 5238, null, null, "1", "Yes", -1));
        this.options.add(new Option(52027, 5239, null, null, "2", "No", -1));
        this.questions.add(new Question(true, adultTBInitiationFormId, 52028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Next Appointment Date", generateUUID(), dateMinTodayMaxNextYear));
//TODO This form doesnot tell if patient is dead or not(as asked by skip logic)


    }


    private void initAdultClinicalEvaluationForm() {
        Integer AdultClinicalEvaluationFormId = 6;

//        this.questions.add(new Question(true,ChildClinicalEvaluationFormId,41002,"3",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Patient ID",generateUUID(),null));
//        this.questions.add(new Question(false,ChildClinicalEvaluationFormId,41003,"4",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Location",generateUUID(),null));
//        this.questions.add(new Question(false,ChildClinicalEvaluationFormId,41004,"5",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Longitude",generateUUID(),null));
//        this.questions.add(new Question(false,ChildClinicalEvaluationFormId,41005,"6",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Latitude",generateUUID(),null));

        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday));

        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "External ID", generateUUID(), numeric3DigitMin1));
        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Weight (in Kg)", generateUUID(), numeric3DigitMin1));
        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Height (in cm)", generateUUID(), numeric3DigitMin1));
        this.questions.add(new Question(false, AdultClinicalEvaluationFormId, 42009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "BMI", generateUUID(), numeric2Digit));

        this.questions.add(new Question(false, AdultClinicalEvaluationFormId, 42010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Physical Examination", null, null));

        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Did the MO perform physical examination?", generateUUID(), null));
        this.options.add(new Option(42011, 4202, null, new int[]{42013, 42012}, "2", "Not performed/examined", -1));
        this.options.add(new Option(42011, 4201, new int[]{42013, 42012}, null, "1", "Performed", -1));

        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Which systems were examined?", generateUUID(), null));
        this.options.add(new Option(42012, 4203, null, null, "1", "General Appearance", -1));
        this.options.add(new Option(42012, 4204, null, null, "2", "Head, Eyes, Ear, Nose, Throat", -1));
        this.options.add(new Option(42012, 4205, null, null, "3", "Performed", -1));
        this.options.add(new Option(42012, 4206, null, null, "4", "Lymph Node Examination (Neck, Axila, Groin)", -1));
        this.options.add(new Option(42012, 4207, null, null, "5", "Spine", -1));
        this.options.add(new Option(42012, 4208, null, null, "6", "Joints", -1));
        this.options.add(new Option(42012, 4209, null, null, "7", "Skin", -1));
        this.options.add(new Option(42012, 4210, null, null, "8", "Chest Examination", -1));
        this.options.add(new Option(42012, 4211, null, null, "9", "Abdominal Examination", -1));


        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Overall Interpretation", generateUUID(), null));
        this.options.add(new Option(42013, 4212, null, null, "1", "Suggestive of TB", -1));
        this.options.add(new Option(42013, 4213, null, null, "2", "Not suggestive of TB", -1));
        this.options.add(new Option(42013, 4214, null, null, "3", "Normal/Unremarkable", -1));


        this.questions.add(new Question(false, AdultClinicalEvaluationFormId, 42014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "TB History", null, null));

        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Have you ever been diagnosed with TB before?", generateUUID(), null));
        this.options.add(new Option(42015, 4215, new int[]{42016, 42017, 42018, 42019}, null, "1", "Yes", -1));
        this.options.add(new Option(42015, 4216, null, new int[]{42016, 42017, 42018, 42019}, "2", "No", -1));
        this.options.add(new Option(42015, 4217, null, new int[]{42016, 42017, 42018, 42019}, "3", "Don't know", -1));
        this.options.add(new Option(42015, 4218, null, new int[]{42016, 42017, 42018, 42019}, "4", "Refused", -1));


        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Have you taken TB medication before?", generateUUID(), null));
        this.options.add(new Option(42016, 4219, null, null, "1", "Yes", -1));
        this.options.add(new Option(42016, 4220, null, null, "2", "No", -1));
        this.options.add(new Option(42016, 4221, null, null, "3", "Don't know", -1));
        this.options.add(new Option(42016, 4222, null, null, "4", "Refused", -1));

        this.questions.add(new Question(false, AdultClinicalEvaluationFormId, 42017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "What type of TB were you treated for?", generateUUID(), null));
        this.options.add(new Option(42017, 4223, null, null, "1", "Drug Susceptible TB", -1));
        this.options.add(new Option(42017, 4224, null, null, "2", "Drug Resistant TB", -1));


        this.questions.add(new Question(false, AdultClinicalEvaluationFormId, 42018, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Duration of TB treatment in Months", generateUUID(), numeric3DigitMin1));  //TODO numeric2Digitmin1
        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42019, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Did you complete your treatment?", generateUUID(), null));
        this.options.add(new Option(42019, 4225, null, null, "1", "Yes", -1));
        this.options.add(new Option(42019, 4226, null, null, "2", "No", -1));
        this.options.add(new Option(42019, 4227, null, null, "3", "Don't know", -1));
        this.options.add(new Option(42019, 4228, null, null, "4", "Refused", -1));


        this.questions.add(new Question(false, AdultClinicalEvaluationFormId, 42020, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Family History", null, null));
        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42021, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Has the person been in close contact with someone diagnosed with TB?", generateUUID(), null));
        this.options.add(new Option(42021, 4230, null, new int[]{42022}, "2", "No", -1));
        this.options.add(new Option(42021, 4229, new int[]{42022}, null, "1", "Yes", -1));
        this.options.add(new Option(42021, 4231, null, new int[]{42022}, "3", "Unknown", -1));
        this.options.add(new Option(42021, 4232, null, new int[]{42022}, "4", "Refused", -1));

        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42022, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Who was the close contact?", generateUUID(), null));
        this.options.add(new Option(42022, 4233, null, new int[]{42023}, "1", "Mother", -1));
        this.options.add(new Option(42022, 4234, null, new int[]{42023}, "2", "Father", -1));
        this.options.add(new Option(42022, 4235, null, new int[]{42023}, "3", "Brother", -1));
        this.options.add(new Option(42022, 4236, null, new int[]{42023}, "4", "Sister", -1));
        this.options.add(new Option(42022, 4237, null, new int[]{42023}, "5", "Son", -1));
        this.options.add(new Option(42022, 4238, null, new int[]{42023}, "6", "Daughter", -1));
        this.options.add(new Option(42022, 4239, null, new int[]{42023}, "7", "Paternal Grandfather", -1));
        this.options.add(new Option(42022, 4240, null, new int[]{42023}, "8", "Paternal Grandmother", -1));
        this.options.add(new Option(42022, 4241, null, new int[]{42023}, "9", "Maternal Grandfather", -1));
        this.options.add(new Option(42022, 4242, null, new int[]{42023}, "10", "Maternal Grandmother", -1));
        this.options.add(new Option(42022, 4243, null, new int[]{42023}, "11", "Uncle", -1));
        this.options.add(new Option(42022, 4244, null, new int[]{42023}, "12", "Aunt", -1));
        this.options.add(new Option(42022, 4245, new int[]{42023}, null, "13", "Other", -1));
        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42023, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify, if other", generateUUID(), null));


        this.questions.add(new Question(false, AdultClinicalEvaluationFormId, 42024, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Conclusion", null, null));
        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42025, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Conclusion", generateUUID(), null));
        this.options.add(new Option(42025, 4246, new int[]{42026, 42028, 42029, 42030}, null, "1", "TB Presumptive confirmed", -1));
        this.options.add(new Option(42025, 4247, null, new int[]{42026, 42028, 42029, 42030}, "2", "Not a TB Presumptive", -1));


        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42026, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Presumptive type", generateUUID(), null));
        this.options.add(new Option(42026, 4248, null, null, "1", "Strongly Suggestive", -1));
        this.options.add(new Option(42026, 4249, null, null, "2", "Suggestive", -1));

        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Clinician's notes", generateUUID(), null));


        this.questions.add(new Question(false, AdultClinicalEvaluationFormId, 42028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, Validation.CHECK_FOR_EMPTY, "Next Visit Details", null, null));
        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Do you want the patient to visit the facility?", generateUUID(), null));
        this.options.add(new Option(42029, 4250, null, null, "1", "Yes", -1));
        this.options.add(new Option(42029, 4251, null, null, "2", "No", -1));

        this.questions.add(new Question(true, AdultClinicalEvaluationFormId, 42030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY, "Next Appointment Date", generateUUID(), dateMinTodayMaxNextYear));

    }

    private void initChildClinicalEvaluationForm() {
        Integer ChildClinicalEvaluationFormId = 5;

//        this.questions.add(new Question(true,ChildClinicalEvaluationFormId,41002,"3",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Patient ID",generateUUID(),null));
//        this.questions.add(new Question(false,ChildClinicalEvaluationFormId,41003,"4",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Location",generateUUID(),null));
//        this.questions.add(new Question(false,ChildClinicalEvaluationFormId,41004,"5",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Longitude",generateUUID(),null));
//        this.questions.add(new Question(false,ChildClinicalEvaluationFormId,41005,"6",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Latitude",generateUUID(),null));

        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday));



        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "External ID", generateUUID(), numeric3DigitMin1));
        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Weight (in Kg)", generateUUID(), numeric3DigitMin1));
        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Height (in cm)", generateUUID(), numeric3DigitMin1));
        this.questions.add(new Question(false, ChildClinicalEvaluationFormId, 41009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "BMI", generateUUID(), numericDecimal4Digit));

        this.questions.add(new Question(false, ChildClinicalEvaluationFormId, 41010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Physical Examination", null, null));

        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Did the MO perform physical examination?", generateUUID(), null));

        this.options.add(new Option(41011, 4102, null, new int[]{41013, 41012}, "1", "Not performed/examined", -1));
        this.options.add(new Option(41011, 4101, new int[]{41013, 41012}, null, "2", "Performed", -1));

        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Which systems were examined?", generateUUID(), null));
        this.options.add(new Option(41012, 4103, null, null, "1", "General Appearance", -1));
        this.options.add(new Option(41012, 4104, null, null, "2", "Head, Eyes, Ear, Nose, Throat", -1));
        this.options.add(new Option(41012, 4105, null, null, "3", "Performed", -1));
        this.options.add(new Option(41012, 4106, null, null, "4", "Lymph Node Examination (Neck, Axila, Groin)", -1));
        this.options.add(new Option(41012, 4107, null, null, "5", "Spine", -1));
        this.options.add(new Option(41012, 4108, null, null, "6", "Joints", -1));
        this.options.add(new Option(41012, 4109, null, null, "7", "Skin", -1));
        this.options.add(new Option(41012, 4110, null, null, "8", "Chest Examination", -1));
        this.options.add(new Option(41012, 4111, null, null, "9", "Abdominal Examination", -1));


        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Overall Interpretation", generateUUID(), null));
        this.options.add(new Option(41013, 4112, null, null, "1", "Suggestive of TB", -1));
        this.options.add(new Option(41013, 4113, null, null, "2", "Not suggestive of TB", -1));
        this.options.add(new Option(41013, 4114, null, null, "3", "Normal/Unremarkable", -1));


        this.questions.add(new Question(false, ChildClinicalEvaluationFormId, 41014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "TB History", null, null));

        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Have you ever been diagnosed with TB before?", generateUUID(), null));
        this.options.add(new Option(41015, 4115, new int[]{41016, 41017, 41018, 41019}, null, "1", "Yes", -1));
        this.options.add(new Option(41015, 4116, null, new int[]{41016, 41017, 41018, 41019}, "2", "No", -1));
        this.options.add(new Option(41015, 4117, null, new int[]{41016, 41017, 41018, 41019}, "3", "Don't know", -1));
        this.options.add(new Option(41015, 4118, null, new int[]{41016, 41017, 41018, 41019}, "4", "Refused", -1));


        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Have you taken TB medication before?", generateUUID(), null));
        this.options.add(new Option(41016, 4119, null, null, "1", "Yes", -1));
        this.options.add(new Option(41016, 4120, null, null, "2", "No", -1));
        this.options.add(new Option(41016, 4121, null, null, "3", "Don't know", -1));
        this.options.add(new Option(41016, 4122, null, null, "4", "Refused", -1));

        this.questions.add(new Question(false, ChildClinicalEvaluationFormId, 41017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "What type of TB were you treated for?", generateUUID(), null));
        this.options.add(new Option(41017, 4123, null, null, "1", "Drug Susceptible TB", -1));
        this.options.add(new Option(41017, 4124, null, null, "2", "Drug Resistant TB", -1));


        this.questions.add(new Question(false, ChildClinicalEvaluationFormId, 41018, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Duration of TB treatment in Months", generateUUID(), numeric3DigitMin1));   //TODO numeric3DigitMin1MaxVal24
        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41019, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Did you complete your treatment?", generateUUID(), null));
        this.options.add(new Option(41019, 4125, null, null, "1", "Yes", -1));
        this.options.add(new Option(41019, 4126, null, null, "2", "No", -1));
        this.options.add(new Option(41019, 4127, null, null, "3", "Don't know", -1));
        this.options.add(new Option(41019, 4128, null, null, "4", "Refused", -1));


        this.questions.add(new Question(false, ChildClinicalEvaluationFormId, 41020, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Family History", null, null));
        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41021, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Has the person been in close contact with someone diagnosed with TB?", generateUUID(), null));

        this.options.add(new Option(41021, 4130, null, new int[]{41022}, "1", "No", -1));
        this.options.add(new Option(41021, 4129, new int[]{41022}, null, "2", "Yes", -1));
        this.options.add(new Option(41021, 4131, null, new int[]{41022}, "3", "Unknow", -1));
        this.options.add(new Option(41021, 4132, null, new int[]{41022}, "4", "Refused", -1));

        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41022, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Who was the close contact?", generateUUID(), null));
        this.options.add(new Option(41022, 4133, null, new int[]{41023}, "1", "Mother", -1));
        this.options.add(new Option(41022, 4134, null, new int[]{41023}, "2", "Father", -1));
        this.options.add(new Option(41022, 4135, null, new int[]{41023}, "3", "Brother", -1));
        this.options.add(new Option(41022, 4136, null, new int[]{41023}, "4", "Sister", -1));
        this.options.add(new Option(41022, 4137, null, new int[]{41023}, "5", "Son", -1));
        this.options.add(new Option(41022, 4138, null, new int[]{41023}, "6", "Daughter", -1));
        this.options.add(new Option(41022, 4139, null, new int[]{41023}, "7", "Paternal Grandfather", -1));
        this.options.add(new Option(41022, 4140, null, new int[]{41023}, "8", "Paternal Grandmother", -1));
        this.options.add(new Option(41022, 4141, null, new int[]{41023}, "9", "Maternal Grandfather", -1));
        this.options.add(new Option(41022, 4142, null, new int[]{41023}, "10", "Maternal Grandmother", -1));
        this.options.add(new Option(41022, 4143, null, new int[]{41023}, "11", "Uncle", -1));
        this.options.add(new Option(41022, 4144, null, new int[]{41023}, "12", "Aunt", -1));
        this.options.add(new Option(41022, 4145, new int[]{41023}, null, "13", "Other", -1));
        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41023, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify, if other", generateUUID(), null));


        this.questions.add(new Question(false, ChildClinicalEvaluationFormId, 41024, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Conclusion", null, null));
        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41025, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Conclusion", generateUUID(), null));
        this.options.add(new Option(41025, 4146, new int[]{41026, 41028, 41029, 41030}, null, "1", "TB Presumptive confirmed", -1));
        this.options.add(new Option(41025, 4147, null, new int[]{41026, 41028, 41029, 41030}, "2", "Not a TB Presumptive", -1));


        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41026, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Presumptive type", generateUUID(), null));
        this.options.add(new Option(41026, 4148, null, null, "1", "Strongly Suggestive", -1));
        this.options.add(new Option(41026, 4149, null, null, "2", "Suggestive", -1));

        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Clinician's notes", generateUUID(), null));


        this.questions.add(new Question(false, ChildClinicalEvaluationFormId, 41028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, Validation.CHECK_FOR_EMPTY, "Next Visit Details", null, null));
        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Do you want the patient to visit the facility?", generateUUID(), null));
        this.options.add(new Option(41029, 4150, null, null, "1", "Yes", -1));
        this.options.add(new Option(41029, 4151, null, null, "2", "No", -1));

        this.questions.add(new Question(true, ChildClinicalEvaluationFormId, 41030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY, "Next Appointment Date", generateUUID(), dateMinTodayMaxNextYear));

    }


    private void initAdultScreeningForm() {

        Integer AdultScreeningFormId = 3;


        //this.questions.add(new Question(true,ChildScreeningFormId,31001,"2",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"User ID",generateUUID(),null));

        //        this.questions.add(new Question(true,ChildScreeningFormId,31002,"3",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Patient ID",generateUUID(),null));
//        this.questions.add(new Question(false,ChildScreeningFormId,31003,"4",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Location",generateUUID(),null));
//        this.questions.add(new Question(false,ChildScreeningFormId,31004,"5",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Longitude",generateUUID(),null));
//        this.questions.add(new Question(false,ChildScreeningFormId,31005,"6",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Latitude",generateUUID(),null));

        this.questions.add(new Question(true, AdultScreeningFormId, 32000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday));

        this.questions.add(new Question(true, AdultScreeningFormId, 32006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Location Type", generateUUID(), null));
        this.options.add(new Option(32006, 3101, new int[]{32010, 32038}, null, "1", "Health Facility", -1));
        this.options.add(new Option(32006, 3102, null, new int[]{32010, 32038}, "2", "Community", -1));
        this.options.add(new Option(32006, 3103, null, new int[]{32010, 32038}, "3", "Factory", -1));
        this.options.add(new Option(32006, 3104, null, new int[]{32010, 32038}, "4", "School", -1));
        this.options.add(new Option(32006, 3105, null, new int[]{32010, 32038}, "5", "Prison", -1));
        this.options.add(new Option(32006, 3106, null, new int[]{32010, 32038}, "6", "Patient's Home", -1));
        this.options.add(new Option(32006, 3107, null, new int[]{32010, 32038}, "7", "Other", -1));

        this.questions.add(new Question(true, AdultScreeningFormId, 32007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Screening Tool Being Used", generateUUID(), null));
        this.options.add(new Option(32007, 3108, new int[]{32017, 32019, 32020}, new int[]{320361, 320362, 320363, 32008}, "1", "Verbal Questionaire", -1));
        this.options.add(new Option(32007, 3108, new int[]{320361, 320362, 320363}, new int[]{32008, 32017, 32019, 32020}, "2", "Chest Xray", -1));
        this.options.add(new Option(32007, 3109, new int[]{32008}, new int[]{32017, 32019, 32020, 320361, 320362, 320363}, "3", "Other", -1));
        this.questions.add(new Question(true, AdultScreeningFormId, 32008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify Other", generateUUID(), null));


        this.questions.add(new Question(true, AdultScreeningFormId, 32009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Type of Population being Screened", generateUUID(), null));
        this.options.add(new Option(32009, 3110, null, null, "1", "Contacts of TB patients", -1));
        this.options.add(new Option(32009, 3111, null, null, "2", "Mining Community", -1));
        this.options.add(new Option(32009, 3112, null, null, "3", "Prison Population", -1));
        this.options.add(new Option(32009, 3113, null, null, "4", "Refugees / Displaced Popultaions", -1));
        this.options.add(new Option(32009, 3114, null, null, "5", "Health Workers", -1));
        this.options.add(new Option(32009, 3115, null, null, "6", "Program Staff", -1));
        this.options.add(new Option(32009, 3116, null, null, "7", "People Visiting Facility", -1));
        this.options.add(new Option(32009, 3117, null, null, "8", "Region's Population", -1));
        this.options.add(new Option(32009, 3118, null, null, "9", "HIV Patients", -1));
        this.options.add(new Option(32009, 3118, null, null, "10", "PWID", -1));


        this.questions.add(new Question(true, AdultScreeningFormId, 32010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Is the child a patient or an attendant?", generateUUID(), null));
        this.options.add(new Option(32010, 3119, null, null, "1", "Patient", -1));
        this.options.add(new Option(32010, 3120, null, null, "2", "Attendant", -1));


        //        "this.questions.add(new Question(false,ChildScreeningFormId,31011,""12"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,""If patient, which speciality/department is the patient here to consult with?	"","generateUUID()",null));"
//        this.questions.add(new Question(false,ChildScreeningFormId,31012,"13",InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"If other specialty, please specify ",generateUUID(),null));
        this.questions.add(new Question(true, AdultScreeningFormId, 31013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Is the person pregnant?", generateUUID(), null));   //TODO how to check if person is female?
        this.options.add(new Option(31013, 3121, null, new int[]{320361, 320362, 320363}, "1", "Yes", -1));
        this.options.add(new Option(31013, 3122, new int[]{320361, 320362, 320363}, null, "2", "No", -1));
        this.options.add(new Option(31013, 3123, new int[]{320361, 320362, 320363}, null, "3", "Don't know", -1));
        this.options.add(new Option(31013, 3124, new int[]{320361, 320362, 320363}, null, "4", "Refused", -1));

        this.questions.add(new Question(false, AdultScreeningFormId, 32014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "SYMPTOMS", null, null));
        this.questions.add(new Question(true, AdultScreeningFormId, 32015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Do you have a cough?", generateUUID(), null));
        this.options.add(new Option(32015, 3121, new int[]{31016, 320161, 320162}, null, "1", "Yes", -1));
        this.options.add(new Option(32015, 3122, null, new int[]{31016, 320161, 320162}, "2", "No", -1));
        this.options.add(new Option(32015, 3123, null, new int[]{31016, 320161, 320162}, "3", "Unknown", -1));
        this.options.add(new Option(32015, 3124, null, new int[]{31016, 320161, 320162}, "4", "Refused", -1));


        this.questions.add(new Question(true, AdultScreeningFormId, 32016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Duration of cough", generateUUID(), null));
        this.options.add(new Option(32016, 3125, null, null, "1", "Less than 2 weeks", -1));
        this.options.add(new Option(32016, 3126, null, null, "2", "2 to 3 weeks", -1));
        this.options.add(new Option(32016, 3127, null, null, "3", "More than 3 weeks", -1));
        this.options.add(new Option(32016, 3128, null, null, "4", "Don't Know", -1));
        this.options.add(new Option(32016, 3129, null, null, "5", "Refused", -1));


        this.questions.add(new Question(true, AdultScreeningFormId, 320161, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Is your cough productive?", generateUUID(), null));
        this.options.add(new Option(320161, 31301, null, null, "1", "Yes", -1));
        this.options.add(new Option(320161, 31302, null, null, "2", "No", -1));
        this.options.add(new Option(320161, 31302, null, null, "3", "Unknown", -1));
        this.options.add(new Option(320161, 31303, null, null, "4", "Refused", -1));


        this.questions.add(new Question(true, AdultScreeningFormId, 320162, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Blood in cough (haemoptysis)", generateUUID(), null));
        this.options.add(new Option(320162, 31304, null, null, "1", "Yes", -1));
        this.options.add(new Option(320162, 31305, null, null, "2", "No", -1));
        this.options.add(new Option(320162, 31306, null, null, "3", "Don't Know", -1));
        this.options.add(new Option(320162, 31307, null, null, "4", "Refused", -1));


        this.questions.add(new Question(true, AdultScreeningFormId, 32017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Do you have fever?", generateUUID(), null));
        this.options.add(new Option(32017, 3130, null, null, "1", "Yes", -1));
        this.options.add(new Option(32017, 3131, null, null, "2", "No", -1));
        this.options.add(new Option(32017, 3132, null, null, "3", "Unknown", -1));
        this.options.add(new Option(32017, 3133, null, null, "4", "Refused", -1));
//        this.questions.add(new Question(false,ChildScreeningFormId,31018,"19",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Duration of fever",generateUUID(),null));
        this.questions.add(new Question(true, AdultScreeningFormId, 32019, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Do you have night sweats?                                               ", generateUUID(), null));
        this.options.add(new Option(32019, 3134, null, null, "1", "Yes", -1));
        this.options.add(new Option(32019, 3135, null, null, "2", "No", -1));
        this.options.add(new Option(32019, 3136, null, null, "3", "Unknown", -1));
        this.options.add(new Option(32019, 3137, null, null, "4", "Refused", -1));

        this.questions.add(new Question(true, AdultScreeningFormId, 32020, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Have you had unexplained weight loss?", generateUUID(), null));
        this.options.add(new Option(32020, 3138, null, null, "1", "Yes", -1));
        this.options.add(new Option(32020, 3139, null, null, "2", "No", -1));
        this.options.add(new Option(32020, 3140, null, null, "3", "Unknown", -1));
        this.options.add(new Option(32020, 3141, null, null, "4", "Refused", -1));


        this.questions.add(new Question(false, AdultScreeningFormId, 32024, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "TB HISTORY", null, null));
        this.questions.add(new Question(true, AdultScreeningFormId, 32025, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Have you ever been diagnosed with TB before?", generateUUID(), null));
        this.options.add(new Option(32025, 3154, null, null, "1", "No", -1));
        this.options.add(new Option(32025, 3155, null, null, "2", "Yes", -1));
        this.options.add(new Option(32025, 3156, null, null, "3", "Unknown", -1));
        this.options.add(new Option(32025, 3157, null, null, "4", "Refused", -1));

        //        this.questions.add(new Question(true,ChildScreeningFormId,31026,"27",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Has the child taken TB medication before?",generateUUID(),null));
        this.questions.add(new Question(true, AdultScreeningFormId, 32027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Have you been in close contact with someone diagnosed with TB", generateUUID(), null));
        this.options.add(new Option(32027, 3158, null, null, "1", "No", -1));
        this.options.add(new Option(32027, 3159, null, null, "2", "Yes", -1));
        this.options.add(new Option(32027, 3160, null, null, "3", "Unknown", -1));
        this.options.add(new Option(32027, 3161, null, null, "4", "Refused", -1));

        this.questions.add(new Question(false, AdultScreeningFormId, 32028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "RISK FACTORS", null, null));
        this.questions.add(new Question(false, AdultScreeningFormId, 32029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Do you smoke?", generateUUID(), null));
        this.options.add(new Option(32029, 3162, null, null, "1", "No", -1));
        this.options.add(new Option(32029, 3163, null, null, "2", "Yes", -1));
        this.options.add(new Option(32029, 3164, null, null, "3", "Unknown", -1));
        this.options.add(new Option(32029, 3165, null, null, "4", "Refused", -1));

        this.questions.add(new Question(false, AdultScreeningFormId, 32030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Do you use injectible drugs for non-medical purposes?", generateUUID(), null));
        this.options.add(new Option(32030, 3166, null, null, "1", "No", -1));
        this.options.add(new Option(32030, 3167, null, null, "2", "Yes", -1));
        this.options.add(new Option(32030, 3168, null, null, "3", "Unknown", -1));
        this.options.add(new Option(32030, 3169, null, null, "4", "Refused", -1));

        this.questions.add(new Question(false, AdultScreeningFormId, 32031, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "MEDICAL HISTORY", generateUUID(), null));

        this.questions.add(new Question(false, AdultScreeningFormId, 32032, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Do you have any comorbid conditions?", generateUUID(), null));
        this.options.add(new Option(32032, 3170, null, new int[]{32033}, "1", "Diabetes", -1));
        this.options.add(new Option(32032, 3171, null, new int[]{32033}, "2", "HIV", -1));
        this.options.add(new Option(32032, 3172, null, new int[]{32033}, "3", "HepC", -1));
        this.options.add(new Option(32032, 3173, null, new int[]{32033}, "4", "Renal Condition", -1));
        this.options.add(new Option(32032, 3174, new int[]{32033}, null, "5", "Other", -1));
        this.questions.add(new Question(true, AdultScreeningFormId, 32033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify Other Comorbid Condition", generateUUID(), null));

//        this.questions.add(new Question(false,ChildScreeningFormId,31034,"35",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Would you like the child to be tested for co-morbid conditions?",generateUUID(),null));
//        this.questions.add(new Question(false,ChildScreeningFormId,31035,"36",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Are you referring the patient for screening tests?",generateUUID(),null));

        this.questions.add(new Question(false, AdultScreeningFormId, 320361, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "XRAY DETAILS", null, null));

        //TODO change option Id here(added for demo)
//        this.questions.add(new Question(true, AdultScreeningFormId, 320362, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Order ID", generateUUID(),null));
//        this.options.add(new Option(320362, 991678, null, null, "1", "201000", -1));
//        this.options.add(new Option(320362, 991679, null, null, "2", "201001", -1));
//        this.options.add(new Option(320362, 991680, null, null, "3", "201003", -1));
//        this.options.add(new Option(320362, 991681, null, null, "4", "201004", -1));

        this.questions.add(new Question(true, AdultScreeningFormId, 320363, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Xray Conclusion", generateUUID(), alphanumeric100DigitSpace));
//        this.options.add(new Option(320363, 31751, null, null, "1", "Normal", -1));
//        this.options.add(new Option(320363, 31752, null, null, "2", "Abnormal, Not Suggestive of TB", -1));
//        this.options.add(new Option(320363, 31753, null, null, "3", "Abnormal, Suggestive of TB", -1));


        this.questions.add(new Question(false, AdultScreeningFormId, 32036, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "CONCLUSION", null, null));
        this.questions.add(new Question(true, AdultScreeningFormId, 32037, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Presumptive TB?", generateUUID(), null));
        this.options.add(new Option(32037, 3175, null, new int[]{32039, 32038}, "1", "No", -1));
        this.options.add(new Option(32037, 3176, new int[]{32039, 32038}, null, "2", "Yes", -1));

        this.questions.add(new Question(false, AdultScreeningFormId, 32038, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Name of TB Diagnosis Facility to which the TB Suspect has been referred to", generateUUID(), null));
        this.questions.add(new Question(true, AdultScreeningFormId, 32039, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY, "Next Appointment Date", generateUUID(), dateMinTodayMaxNextYear));

    }


    private void initChildScreeningForm() {

        Integer ChildScreeningFormId = 4;

        this.questions.add(new Question(true, ChildScreeningFormId, 31000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday));

        //this.questions.add(new Question(true,ChildScreeningFormId,31001,"2",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"User ID",generateUUID(),null));

        //        this.questions.add(new Question(true,ChildScreeningFormId,31002,"3",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Patient ID",generateUUID(),null));
//        this.questions.add(new Question(false,ChildScreeningFormId,31003,"4",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Location",generateUUID(),null));
//        this.questions.add(new Question(false,ChildScreeningFormId,31004,"5",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Longitude",generateUUID(),null));
//        this.questions.add(new Question(false,ChildScreeningFormId,31005,"6",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Latitude",generateUUID(),null));


        this.questions.add(new Question(true, ChildScreeningFormId, 31006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Location Type", generateUUID(), null));
        this.options.add(new Option(31006, 3001, new int[]{31010, 31038}, null, "1", "Health Facility", -1));
        this.options.add(new Option(31006, 3002, null, new int[]{31010, 31038}, "2", "Community", -1));
        this.options.add(new Option(31006, 3003, null, new int[]{31010, 31038}, "3", "Factory", -1));
        this.options.add(new Option(31006, 3004, null, new int[]{31010, 31038}, "4", "School", -1));
        this.options.add(new Option(31006, 3005, null, new int[]{31010, 31038}, "5", "Prison", -1));
        this.options.add(new Option(31006, 3006, null, new int[]{31010, 31038}, "6", "Patient's Home", -1));
        this.options.add(new Option(31006, 3007, null, new int[]{31010, 31038}, "7", "Other", -1));

        this.questions.add(new Question(true, ChildScreeningFormId, 31007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Screening Tool Being Used", generateUUID(), null));
        this.options.add(new Option(31007, 3008, null, new int[]{31008}, "1", "Verbal Questionaire", -1));
        this.options.add(new Option(31007, 3009, new int[]{31008}, null, "2", "Other", -1));
        this.questions.add(new Question(true, ChildScreeningFormId, 31008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify Other", generateUUID(), null));


        this.questions.add(new Question(true, ChildScreeningFormId, 31009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Type of Population being Screened", generateUUID(), null));
        this.options.add(new Option(31009, 3010, null, null, "1", "Contacts of TB patients", -1));
        this.options.add(new Option(31009, 3011, null, null, "2", "Mining Community", -1));
        this.options.add(new Option(31009, 3012, null, null, "3", "Prison Population", -1));
        this.options.add(new Option(31009, 3013, null, null, "4", "Refugees / Displaced Popultaions", -1));
        this.options.add(new Option(31009, 3014, null, null, "5", "Health Workers", -1));
        this.options.add(new Option(31009, 3015, null, null, "6", "Program Staff", -1));
        this.options.add(new Option(31009, 3016, null, null, "7", "People Visiting Facility", -1));
        this.options.add(new Option(31009, 3017, null, null, "8", "Location Population", -1));
        this.options.add(new Option(31009, 3018, null, null, "9", "HIV", -1));

        this.questions.add(new Question(true, ChildScreeningFormId, 31010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Is the child a patient or an attendant?", generateUUID(), null));
        this.options.add(new Option(31010, 3019, null, null, "1", "Patient", -1));
        this.options.add(new Option(31010, 3020, null, null, "2", "Attendant", -1));


        //        "this.questions.add(new Question(false,ChildScreeningFormId,31011,""12"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,""If patient, which speciality/department is the patient here to consult with?	"","generateUUID()",null));"
//        this.questions.add(new Question(false,ChildScreeningFormId,31012,"13",InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"If other specialty, please specify ",generateUUID(),null));
//        this.questions.add(new Question(true,ChildScreeningFormId,31013,"14",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Is the person pregnant?",generateUUID(),null));
        this.questions.add(new Question(false, ChildScreeningFormId, 31014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "SYMPTOMS", null, null));
        this.questions.add(new Question(true, ChildScreeningFormId, 31015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the child have a cough?", generateUUID(), null));
        this.options.add(new Option(31015, 3021, new int[]{31016}, null, "1", "Yes", -1));
        this.options.add(new Option(31015, 3022, null, new int[]{31016}, "2", "No", -1));
        this.options.add(new Option(31015, 3023, null, new int[]{31016}, "3", "Unknown", -1));
        this.options.add(new Option(31015, 3024, null, new int[]{31016}, "4", "Refused", -1));


        this.questions.add(new Question(true, ChildScreeningFormId, 31016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Duration of cough", generateUUID(), null));
        this.options.add(new Option(31016, 3025, null, null, "1", "Less than 2 weeks", -1));
        this.options.add(new Option(31016, 3026, null, null, "2", "2 to 3 weeks", -1));
        this.options.add(new Option(31016, 3027, null, null, "3", "More than 3 weeks", -1));
        this.options.add(new Option(31016, 3028, null, null, "4", "Don't Know", -1));
        this.options.add(new Option(31016, 3029, null, null, "5", "Refused", -1));


        this.questions.add(new Question(true, ChildScreeningFormId, 31017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the child have fever?", generateUUID(), null));
        this.options.add(new Option(31017, 3030, null, null, "1", "Yes", -1));
        this.options.add(new Option(31017, 3031, null, null, "2", "No", -1));
        this.options.add(new Option(31017, 3032, null, null, "3", "Unknown", -1));
        this.options.add(new Option(31017, 3033, null, null, "4", "Refused", -1));
//        this.questions.add(new Question(false,ChildScreeningFormId,31018,"19",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Duration of fever",generateUUID(),null));
        this.questions.add(new Question(true, ChildScreeningFormId, 31019, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the child have night sweats?                                               ", generateUUID(), null));
        this.options.add(new Option(31019, 3034, null, null, "1", "Yes", -1));
        this.options.add(new Option(31019, 3035, null, null, "2", "No", -1));
        this.options.add(new Option(31019, 3036, null, null, "3", "Unknown", -1));
        this.options.add(new Option(31019, 3037, null, null, "4", "Refused", -1));

        this.questions.add(new Question(true, ChildScreeningFormId, 31020, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the child have unexplained weight loss?", generateUUID(), null));
        this.options.add(new Option(31020, 3038, null, null, "1", "Yes", -1));
        this.options.add(new Option(31020, 3039, null, null, "2", "No", -1));
        this.options.add(new Option(31020, 3040, null, null, "3", "Unknown", -1));
        this.options.add(new Option(31020, 3041, null, null, "4", "Refused", -1));

        this.questions.add(new Question(true, ChildScreeningFormId, 31021, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "What is the child's appetite like?", generateUUID(), null));
        this.options.add(new Option(31021, 3042, null, null, "1", "Poor", -1));
        this.options.add(new Option(31021, 3043, null, null, "2", "OK", -1));
        this.options.add(new Option(31021, 3044, null, null, "3", "Unknown", -1));
        this.options.add(new Option(31021, 3045, null, null, "4", "Refused", -1));

        this.questions.add(new Question(true, ChildScreeningFormId, 31022, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the child have lymph node swelling of greater than 2 weeks                                 ", generateUUID(), null));
        this.options.add(new Option(31022, 3046, null, null, "1", "No", -1));
        this.options.add(new Option(31022, 3047, null, null, "2", "Yes", -1));
        this.options.add(new Option(31022, 3048, null, null, "3", "Unknown", -1));
        this.options.add(new Option(31022, 3049, null, null, "4", "Refused", -1));

        this.questions.add(new Question(true, ChildScreeningFormId, 31023, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the child have swelling in the joint/spine of greater than 2 weeks?                                                                                                                          ", generateUUID(), null));
        this.options.add(new Option(31023, 3050, null, null, "1", "No", -1));
        this.options.add(new Option(31023, 3051, null, null, "2", "Yes", -1));
        this.options.add(new Option(31023, 3052, null, null, "3", "Unknown", -1));
        this.options.add(new Option(31023, 3053, null, null, "4", "Refused", -1));


        this.questions.add(new Question(false, ChildScreeningFormId, 31024, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "TB HISTORY", null, null));
        this.questions.add(new Question(true, ChildScreeningFormId, 31025, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Has the child been previously diagnosed with TB?", generateUUID(), null));
        this.options.add(new Option(31025, 3054, null, null, "1", "No", -1));
        this.options.add(new Option(31025, 3055, null, null, "2", "Yes", -1));
        this.options.add(new Option(31025, 3056, null, null, "3", "Unknown", -1));
        this.options.add(new Option(31025, 3057, null, null, "4", "Refused", -1));

        //        this.questions.add(new Question(true,ChildScreeningFormId,31026,"27",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Has the child taken TB medication before?",generateUUID(),null));
        this.questions.add(new Question(true, ChildScreeningFormId, 31027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Has the child been in close contact with someone diagnosed with TB or have a history of contact/family with TB patient in the past 2 years? (e.g. family member he/she live with, or if they lived with anyone who has TB, close friend etc).", generateUUID(), null));
        this.options.add(new Option(31027, 3058, null, null, "1", "No", -1));
        this.options.add(new Option(31027, 3059, null, null, "2", "Yes", -1));
        this.options.add(new Option(31027, 3060, null, null, "3", "Unknown", -1));
        this.options.add(new Option(31027, 3061, null, null, "4", "Refused", -1));

        this.questions.add(new Question(false, ChildScreeningFormId, 31028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "RISK FACTORS", null, null));
        this.questions.add(new Question(false, ChildScreeningFormId, 31029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the child smoke?", generateUUID(), null));
        this.options.add(new Option(31029, 3062, null, null, "1", "No", -1));
        this.options.add(new Option(31029, 3063, null, null, "2", "Yes", -1));
        this.options.add(new Option(31029, 3064, null, null, "3", "Unknown", -1));
        this.options.add(new Option(31029, 3065, null, null, "4", "Refused", -1));

        this.questions.add(new Question(false, ChildScreeningFormId, 31030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the child use injectible drugs for non-medical purposes?", generateUUID(), null));
        this.options.add(new Option(31030, 3066, null, null, "1", "No", -1));
        this.options.add(new Option(31030, 3067, null, null, "2", "Yes", -1));
        this.options.add(new Option(31030, 3068, null, null, "3", "Unknown", -1));
        this.options.add(new Option(31030, 3069, null, null, "4", "Refused", -1));

        this.questions.add(new Question(false, ChildScreeningFormId, 31031, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "MEDICAL HISTORY", generateUUID(), null));

        this.questions.add(new Question(false, ChildScreeningFormId, 31032, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Do you have any comorbid conditions?", generateUUID(), null));
        this.options.add(new Option(31032, 3070, null, new int[]{31033}, "1", "Diabetes", -1));
        this.options.add(new Option(31032, 3071, null, new int[]{31033}, "2", "HIV", -1));
        this.options.add(new Option(31032, 3072, null, new int[]{31033}, "3", "HepC", -1));
        this.options.add(new Option(31032, 3073, null, new int[]{31033}, "4", "Renal Condition", -1));
        this.options.add(new Option(31032, 3074, new int[]{31033}, null, "5", "Other", -1));
        this.questions.add(new Question(true, ChildScreeningFormId, 31033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify Other Comorbid Condition", generateUUID(), null));

//        this.questions.add(new Question(false,ChildScreeningFormId,31034,"35",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Would you like the child to be tested for co-morbid conditions?",generateUUID(),null));
//        this.questions.add(new Question(false,ChildScreeningFormId,31035,"36",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Are you referring the patient for screening tests?",generateUUID(),null));
        this.questions.add(new Question(false, ChildScreeningFormId, 31036, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "CONCLUSION", null, null));
        this.questions.add(new Question(true, ChildScreeningFormId, 31037, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Presumptive TB?", generateUUID(), null));
        this.options.add(new Option(31037, 3075, null, null, "1", "No", -1));
        this.options.add(new Option(31037, 3076, null, null, "2", "Yes", -1));

        this.questions.add(new Question(false, ChildScreeningFormId, 31038, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Name of TB Diagnosis Facility to which the TB Suspect has been referred to", generateUUID(), null));
        this.questions.add(new Question(true, ChildScreeningFormId, 31039, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Next Appointment Date", generateUUID(), dateMinTodayMaxNextYear));
//        this.questions.add(new Question(false,ChildScreeningFormId,,"",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"",generateUUID(),null));

    }


    private void endFollowUpForm() {
        Integer endFollowupFormId = 9;

//        this.questions.add(new Question(true,endFollowupFormId,21000,"1",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Form Entry Start Date/Time",generateUUID(),null));
//        this.questions.add(new Question(true,endFollowupFormId,21001,"2",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"User ID",generateUUID(),null));
//        this.questions.add(new Question(true,endFollowupFormId,21002,"3",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Patient ID",generateUUID(),null));
//        this.questions.add(new Question(false,endFollowupFormId,21003,"4",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Location",generateUUID(),null));
//        this.questions.add(new Question(false,endFollowupFormId,21004,"5",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Longitude",generateUUID(),null));
//        this.questions.add(new Question(false,endFollowupFormId,21005,"6",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Latitude",generateUUID(),null));
        this.questions.add(new Question(true, endFollowupFormId, 21000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday));


        this.questions.add(new Question(true, endFollowupFormId, 21006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "End of Followup for ", generateUUID(), null));
        this.options.add(new Option(21006, 2200, new int[]{21007,21008}, new int[]{21017,21018,21026,21029,21030,21031,21032,21033,21034,21036,21038,21039,21041,21044,21045,21047,21049,21050,21055}, "", "TB Investigation", -1));
        this.options.add(new Option(21006, 2201, new int[]{21017,21018,21026,21029,21030,21031,21032,21033,21034,21036}, new int[]{21007,21008,21038,21039,21041,21044,21045,21047,21049,21050,21055}, "", "TB Treatment", -1));
        this.options.add(new Option(21006, 2202, new int[]{21038,21039},  new int[]{21007,21008,21017,21018,21026,21029,21030,21031,21032,21033,21034,21036,21049,21050,21055}, "", "PET Investigation", -1));
        this.options.add(new Option(21006, 2203, new int[]{21049,21050,21055},  new int[]{21007,21008,21017,21018,21026,21029,21030,21031,21032,21033,21034,21036,21038,21039,21041,21044,21045,21047}, "", "PET Treatment", -1));





        this.questions.add(new Question(false, endFollowupFormId, 21007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "TB Investigation Outcome", null, null));
        this.questions.add(new Question(true, endFollowupFormId, 21008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Patient's TB Investigation Outcome", generateUUID(), null));
        this.options.add(new Option(21008, 2204, new int[]{21012, 21013}, new int[]{21009,21010,21015}, "", "Died", -1));
        this.options.add(new Option(21008, 2205, new int[]{21010}, new int[]{21012, 21013,21009,21015}, "", "Lost to Follow-up", -1));
        this.options.add(new Option(21008, 2206, null, new int[]{21012, 21013,21009,21010,21015}, "", "Clinically evaluated, No TB", -1));
        this.options.add(new Option(21008, 2207, null, new int[]{21012, 21013,21009,21010,21015}, "", "Antibiotic Complete, No TB", -1));
        this.options.add(new Option(21008, 2208, null, new int[]{21012, 21013,21009,21010,21015}, "", "Test Done, No TB", -1));
        this.options.add(new Option(21008, 2209, null, new int[]{21012, 21013,21009,21010,21015}, "", "Dead Bacilli, No TB", -1));
        this.options.add(new Option(21008, 2210, new int[]{21015}, new int[]{21012, 21013,21009,21010}, "", "Not evaluated", -1));
        this.options.add(new Option(21008, 2211, new int[]{21009}, new int[]{21012, 21013,21010,21015}, "", "Other", -1));
        this.questions.add(new Question(false, endFollowupFormId, 21009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other Reason/Remarks", generateUUID(), alphanumeric100DigitSpace));


        this.questions.add(new Question(true, endFollowupFormId, 21010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If lost to follow-up, why was the patient's investigation not completed?", generateUUID(), null));
        this.options.add(new Option(21010, 2212, null, new int[]{21011}, "", "Patient refused follow-up", -1));
        this.options.add(new Option(21010, 2213, null, new int[]{21011}, "", "Substance abuse", -1));
        this.options.add(new Option(21010, 2214, null, new int[]{21011}, "", "Social problem", -1));
        this.options.add(new Option(21010, 2215, null, new int[]{21011}, "", "Left region/country", -1));
        this.options.add(new Option(21010, 2216, null, new int[]{21011}, "", "No confidence in investigation", -1));
        this.options.add(new Option(21010, 2217, null, new int[]{21011}, "", "Contact not established", -1));
        this.options.add(new Option(21010, 2218, new int[]{21011}, null, "", "Other", -1));
        this.options.add(new Option(21010, 2219, null, new int[]{21011}, "", "Unknown", -1));
        this.questions.add(new Question(true, endFollowupFormId, 21011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If reason for lost to follow-up 'Other', then specify:", generateUUID(), alphanumeric100DigitSpace));

        this.questions.add(new Question(false, endFollowupFormId, 21012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY, "If died, then provide date of death:", generateUUID(), dateMinTodayMaxLastMonday));
        this.questions.add(new Question(true, endFollowupFormId, 21013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If died: then provide suspected primary cause of death:", generateUUID(), null));
        this.options.add(new Option(21013, 2220, null, new int[]{21014}, "", "TB immediate cause of death", -1));
        this.options.add(new Option(21013, 2221, null, new int[]{21014}, "", "Cause related to TB treatment", -1));
        this.options.add(new Option(21013, 2222, null, new int[]{21014}, "", "TB contributing to death", -1));
        this.options.add(new Option(21013, 2223, null, new int[]{21014}, "", "Surgery related death", -1));
        this.options.add(new Option(21013, 2224,  new int[]{21014}, null, "", "Cause other than TB", -1));
        //  this.options.add(new Option(21013, 2225, new int[]{21014}, null, "", "Other", -1));
        this.options.add(new Option(21013, 2225, null, new int[]{21014}, "", "Unknown", -1));


        this.questions.add(new Question(true, endFollowupFormId, 21014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If primary cause of death other than TB, then specify:", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(true, endFollowupFormId, 21015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If not evaluated, was the patient transferred out?", generateUUID(), null));
        this.options.add(new Option(21015, 2226, null, new int[]{21016}, "", "Yes", -1));
        this.options.add(new Option(21015, 2227, new int[]{21016}, null, "", "No", -1));


        this.questions.add(new Question(true, endFollowupFormId, 21016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If not transferred out, why does the patient have this outcome?", generateUUID(), alphanumeric100DigitSpace));













        this.questions.add(new Question(false, endFollowupFormId, 21017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "TB Treatment Outcome", null, null));
        this.questions.add(new Question(true, endFollowupFormId, 21018, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Patient's TB Treatment Outcome", generateUUID(), null));
        this.options.add(new Option(21018, 2228, null, new int[]{21019,21020,21021,21022,21023, 21025,21026,21027,21028,21029,21030,21031,21032,21033,21034,21036}, "", "Cured", -1));
        this.options.add(new Option(21018, 2229, null, new int[]{21019,21020,21021,21022,21023, 21025,21026,21027,21028,21029,21030,21031,21032,21033,21034,21036}, "", "Treatment Completed", -1));
        this.options.add(new Option(21018, 2230, new int[]{21027}, new int[]{21019,21020,21021,21022,21023,21026, 21025,21030,21031,21032}, "", "Treatment Failure", -1));
        this.options.add(new Option(21018, 22301, new int[]{21033,21034},  new int[] {21019,21020,21021,21023,21025,21026,21027,21028,21030,21031,21032,}, "", "Died", -1));
        this.options.add(new Option(21018, 2231, new int[]{21020,21023,21030,21031,21032}, new int[]{21019,21027,21021,21022, 21025,21026,21027,21028,21033,21034}, "", "Transfer out", -1));
        this.options.add(new Option(21018, 2232, new int[]{21023,21026,21030,21031,21032}, new int[]{21019,21027,21020,21021,21022,21025,21027,21028}, "", "Referral", -1));
        this.options.add(new Option(21018, 2233, new int[]{21021}, new int[]{21019,21027,21020, 21023, 21025,21026,21027,21028,21030,21031,21032}, "", "Lost to Follow-up", -1));
        this.options.add(new Option(21018, 2234, null, new int[]{21019,21020,21021,21022,21023, 21025,21026,21027,21028,21029,21030,21031,21032,21033,21034,21036}, "", "Treatment adapted", -1));
        this.options.add(new Option(21018, 2235, null, new int[]{21019,21020,21021,21022,21023, 21025,21026,21027,21028,21029,21030,21031,21032,21033,21034,21036}, "", "Relocated", -1));
        this.options.add(new Option(21018, 2236, null, new int[]{21019,21020,21021,21022,21023, 21025,21026,21027,21028,21029,21030,21031,21032,21033,21034,21036}, "", "Treatment Stopped by Doctor", -1));
        this.options.add(new Option(21018, 2237, new int[]{21019}, new int[]{21020,21021,21022,21027,21023, 21025,21026,21027,21028,21029,21030,21031,21032,21033,21034,21036}, "", "Other", -1));


        this.questions.add(new Question(false, endFollowupFormId, 21019, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other Reason/Remarks", generateUUID(), alphanumeric100DigitSpace));

        this.questions.add(new Question(true, endFollowupFormId, 21020, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Location of transfer out", generateUUID(), null));   //ToDO insert location list
        this.options.add(new Option(21020, 2237, null, null, "", "Bedford Hospital", -1));
        this.options.add(new Option(21020, 2237, null, null, "", "Frere Clinic", -1));

        this.questions.add(new Question(true, endFollowupFormId, 21021, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If lost to follow-up, why was the patient's treatment interrupted?", generateUUID(), null));
        this.options.add(new Option(21021, 2238, null, new int[] {21022}, "", "Patient refused follow-up", -1));
        this.options.add(new Option(21021, 2239, null, new int[] {21022}, "", "Substance abuse", -1));
        this.options.add(new Option(21021, 2240, null, new int[] {21022}, "", "Social problem", -1));
        this.options.add(new Option(21021, 2241, null, new int[] {21022}, "", "Adverse events", -1));
        this.options.add(new Option(21021, 2242, null, new int[] {21022}, "", "Left region/country", -1));
        this.options.add(new Option(21021, 2243, null, new int[] {21022}, "", "No confidence in investigation", -1));
        this.options.add(new Option(21021, 2244, null, new int[] {21022}, "", "Contact not established", -1));
        this.options.add(new Option(21021, 2245, new int[]{21022}, null, "", "Other", -1));
        this.options.add(new Option(21021, 2246, null, new int[] {21022}, "", "Unknown", -1));


        this.questions.add(new Question(true, endFollowupFormId, 21022, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If reason for lost of follow-up 'Other', then specify:", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(true, endFollowupFormId, 21023, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Treatment initiated at Referral / Transfer site", generateUUID(), null));
        this.options.add(new Option(21023, 2247, null, new int[]{21024}, "", "Yes", -1));
        this.options.add(new Option(21023, 2248, new int[]{21024}, null, "", "No", -1));
        this.options.add(new Option(21023, 2249, null, new int[]{21024}, "", "Unknown", -1));


        this.questions.add(new Question(true, endFollowupFormId, 21024, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Reason treatment not initiated at referral site", generateUUID(), null));
        this.options.add(new Option(21024, 2250, null, new int[]{21025}, "", "Patient could not be contacted", -1));
        this.options.add(new Option(21024, 2251, null, new int[]{21025}, "", "Patient left the city_village", -1));
        this.options.add(new Option(21024, 2252, null, new int[]{21025}, "", "Patient refused treatment", -1));
        this.options.add(new Option(21024, 2253, null, new int[]{21025}, "", "Patient died", -1));
        this.options.add(new Option(21024, 2254, null, new int[]{21025}, "", "DR not confirmed by baseline repeat test", -1));
        this.options.add(new Option(21024, 2255, new int[]{21025}, null, "", "Other", -1));

        this.questions.add(new Question(true, endFollowupFormId, 21025, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Please specify, if other", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(false, endFollowupFormId, 21026, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "TB Registration No", generateUUID(), numeric11Digit));
        this.questions.add(new Question(true, endFollowupFormId, 21027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "DR Confirmation", generateUUID(), null));
        this.options.add(new Option(21027, 2256, new int[]{21028}, null, "", "Yes", -1));
        this.options.add(new Option(21027, 2257, null, new int[]{21028}, "", "No", -1));

        this.questions.add(new Question(true, endFollowupFormId, 21028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "ENRS Number", generateUUID(), alphanumeric13DigitWithHypen));

        this.questions.add(new Question(false, endFollowupFormId, 21029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Provide name and contact number of person at referral / transfer site who provided details about the patient", null, null));


        this.questions.add(new Question(true, endFollowupFormId, 21030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "First name", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(true, endFollowupFormId, 21031, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Last name", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(true, endFollowupFormId, 21032, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Contact number", generateUUID(), numeric11Digit));
        this.questions.add(new Question(false, endFollowupFormId, 21033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY, "If died, then provide date of death:", generateUUID(), dateMinTodayMaxLastMonday));
        this.questions.add(new Question(true, endFollowupFormId, 21034, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If died: then provide suspected primary cause of death:", generateUUID(), null));
        this.options.add(new Option(21034, 2258, null, new int[] {21035}, "", "TB immediate cause of death", -1));
        this.options.add(new Option(21034, 2259, null, new int[] {21035}, "", "Cause related to TB treatment", -1));
        this.options.add(new Option(21034, 2260, null, new int[] {21035}, "", "TB contributing to death", -1));
        this.options.add(new Option(21034, 2261, null, new int[] {21035}, "", "Surgery related death", -1));
        this.options.add(new Option(21034, 2262, new int[]{21035}, null, "", "Cause other than TB", -1));
        this.options.add(new Option(21034, 2263, null, new int[] {21035}, "", "Unknown", -1));


        this.questions.add(new Question(true, endFollowupFormId, 21035, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If primary cause of death other than TB, then specify:", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(true, endFollowupFormId, 21036, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If failed, then reason for failure:", generateUUID(), null));
        this.options.add(new Option(21036, 2264, null, null, "", "Lack of conversion", -1));
        this.options.add(new Option(21036, 2265, null, null, "", "Bacteriological reversion", -1));
        this.options.add(new Option(21036, 2266, null, null, "", "Additional acquired resistance to FQ or injectables", -1));
        this.options.add(new Option(21036, 2267, null, null, "", "Adverse drug reaction", -1));
        this.options.add(new Option(21036, 2268, new int[]{21037}, null, "", "Other", -1));

        this.questions.add(new Question(true, endFollowupFormId, 21037, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If reason for failure 'Other', then specify:", generateUUID(), alphanumeric100DigitSpace));














        this.questions.add(new Question(false, endFollowupFormId, 21038, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, Validation.CHECK_FOR_EMPTY, "PET Investigation Outcome", null, null));
        this.questions.add(new Question(true, endFollowupFormId, 21039, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Patient's PET Investigation Outcome", generateUUID(), null));
        this.options.add(new Option(21039, 2269, new int[]{21044,21045}, new int[]{21040,21041,21042,21047}, "", "Died", -1));
        this.options.add(new Option(21039, 22691, new int[]{21041}, new int[]{21040,21042,21044,21045,21047}, "", "Transfer out", -1));
        this.options.add(new Option(21039, 2270, null, new int[]{21040,21041,21042,21044,21045,21047}, "", "Referral", -1));
        this.options.add(new Option(21039, 2271, new int[]{21042}, new int[]{21040,21041,21044,21045,21047}, "", "Lost to Follow-up", -1));
        this.options.add(new Option(21039, 2272, new int[]{21047}, new int[]{21040,21041,21042,21044,21045}, "", "Not evaluated", -1));
        this.options.add(new Option(21039, 2273, null, new int[]{21040,21041,21042,21044,21045,21047}, "", "Contact Diagnosed with TB", -1));
        this.options.add(new Option(21039, 2274, null, new int[]{21040,21041,21042,21044,21045,21047}, "", "Refused Screening", -1));
        this.options.add(new Option(21039, 2275, null, new int[]{21040,21041,21042,21044,21045,21047}, "", "Relocated", -1));
        this.options.add(new Option(21039, 2276, new int[]{21040}, new int[]{21041,21042,21044,21045,21047}, "", "Other", -1));


        this.questions.add(new Question(false, endFollowupFormId, 21040, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other Reason/Remarks", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(true, endFollowupFormId, 21041, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Location of transfer out", generateUUID(), null));   //TODO insert location list
        this.options.add(new Option(21041, 2237, null, null, "", "Bedford Hospital", -1));
        this.options.add(new Option(21041, 2237, null, null, "", "Frere Clinic", -1));

        this.questions.add(new Question(true, endFollowupFormId, 21042, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If lost to follow-up, why was the patient's treatment interrupted?", generateUUID(), null));
        this.options.add(new Option(21042, 2277, null, new int[]{21043}, "", "Patient refused follow-up", -1));
        this.options.add(new Option(21042, 2278, null, new int[]{21043}, "", "Substance abuse", -1));
        this.options.add(new Option(21042, 2279, null, new int[]{21043}, "", "Social problem", -1));
        this.options.add(new Option(21042, 2280, null, new int[]{21043}, "", "Adverse events", -1));
        this.options.add(new Option(21042, 2281, null, new int[]{21043}, "", "Left region/country", -1));
        this.options.add(new Option(21042, 2282, null, new int[]{21043}, "", "No confidence in treatment", -1));
        this.options.add(new Option(21042, 2283, null, new int[]{21043}, "", "Contact not established", -1));
        this.options.add(new Option(21042, 2284, null, new int[]{21043}, "", "Index patient refused treatment", -1));
        this.options.add(new Option(21042, 2285, null, new int[]{21043}, "", "Index patient is lost to follow up", -1));
        this.options.add(new Option(21042, 2286, new int[]{21043}, null, "", "Other", -1));
        this.options.add(new Option(21042, 2287, null, new int[]{21043}, "", "Unknown", -1));
        this.questions.add(new Question(true, endFollowupFormId, 21043, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If reason for lost to follow-up 'Other', then specify:", generateUUID(), alphanumeric100DigitSpace));


        this.questions.add(new Question(false, endFollowupFormId, 21044, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY, "If died, then provide date of death:", generateUUID(), dateMinTodayMaxLastMonday));
        this.questions.add(new Question(true, endFollowupFormId, 21045, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If died: then provide suspected primary cause of death:", generateUUID(), null));
        this.options.add(new Option(21045, 2288, null,  new int[]{21046}, "", "TB immediate cause of death", -1));
        this.options.add(new Option(21045, 2289, null,  new int[]{21046}, "", "Cause related to TB treatment", -1));
        this.options.add(new Option(21045, 2290, null,  new int[]{21046}, "", "TB contributing to death", -1));
        this.options.add(new Option(21045, 2291, null,  new int[]{21046}, "", "Surgery related death", -1));
        this.options.add(new Option(21045, 2292, new int[]{21046}, null, "", "Cause other than TB", -1));
        this.options.add(new Option(21045, 2293, null,  new int[]{21046}, "", "Unknown", -1));

        this.questions.add(new Question(true, endFollowupFormId, 21046, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If primary cause of death other than TB, then specify:", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(true, endFollowupFormId, 21047, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If not evaluated, was the patient transferred out?", generateUUID(), null));
        this.options.add(new Option(21047, 2294, null, new int[]{21048}, "", "Yes", -1));
        this.options.add(new Option(21047, 2295,  new int[]{21048}, null, "", "No", -1));

        this.questions.add(new Question(true, endFollowupFormId, 21048, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If not transferred out, why does the patient have this outcome?", generateUUID(), alphanumeric100DigitSpace));












        this.questions.add(new Question(false, endFollowupFormId, 21049, "", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, Validation.CHECK_FOR_EMPTY, "PET Treatment Outcome", null, null));

        this.questions.add(new Question(true, endFollowupFormId, 21050, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Patient's PET Treatment Outcome", generateUUID(), null));
        this.options.add(new Option(21050, 2296, null,  new int[]{21051,21052,21053,21055,21058,21059}, "", "Treatment Completed", -1));
        this.options.add(new Option(21050, 2297, new int[]{21058,21059},  new int[]{21051,21052,21053,21055}, "", "Died", -1));
        this.options.add(new Option(21050, 2298, new int[]{21052,21055},  new int[]{21051,21053,21058,21059}, "", "Transfer out", -1));
        this.options.add(new Option(21050, 2299, new int[]{21055},  new int[]{21051,21052,21053,21058,21059}, "", "Referral", -1));
        this.options.add(new Option(21050, 2300, new int[]{21053},  new int[]{21051,21052,21055,21058,21059}, "", "Lost to Follow-up", -1));
        this.options.add(new Option(21050, 2301, null,  new int[]{21051,21052,21053,21055,21058,21059}, "", "Contact Diagnosed with TB", -1));
        this.options.add(new Option(21050, 2302, null,  new int[]{21051,21052,21053,21055,21058,21059}, "", "Refused to take treatment", -1));
        this.options.add(new Option(21050, 2303, null,  new int[]{21051,21052,21053,21055,21058,21059}, "", "Refused after starting treatment", -1));
        this.options.add(new Option(21050, 2304, null,  new int[]{21051,21052,21053,21055,21058,21059}, "", "Relocated", -1));
        this.options.add(new Option(21050, 2305, null,  new int[]{21051,21052,21053,21055,21058,21059}, "", "Treatment Stopped by Doctor", -1));
        this.options.add(new Option(21050, 2306, new int[]{21051},  new int[]{21052,21053,21055,21058,21059}, "", "Other", -1));

        this.questions.add(new Question(false, endFollowupFormId, 21051, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other Reason/Remarks", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(true, endFollowupFormId, 21052, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Location of transfer out", generateUUID(), null));   //TODO insert location list
        this.options.add(new Option(21052, 2237, null, null, "", "Bedford Hospital", -1));
        this.options.add(new Option(21052, 2237, null, null, "", "Frere Clinic", -1));


        this.questions.add(new Question(true, endFollowupFormId, 21053, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If lost to follow-up, why was the patient's treatment interrupted?", generateUUID(), null));
        this.options.add(new Option(21053, 2307, null, new int[]{21054}, "", "Patient refused follow-upy", -1));
        this.options.add(new Option(21053, 2308, null, new int[]{21054}, "", "Substance abuse", -1));
        this.options.add(new Option(21053, 2309, null, new int[]{21054}, "", "Social problem", -1));
        this.options.add(new Option(21053, 2310, null, new int[]{21054}, "", "Left region/country", -1));
        this.options.add(new Option(21053, 2311, null, new int[]{21054}, "", "Adverse events", -1));
        this.options.add(new Option(21053, 2312, null, new int[]{21054}, "", "No confidence in treatment", -1));
        this.options.add(new Option(21053, 2313, null, new int[]{21054}, "", "Contact not established", -1));
        this.options.add(new Option(21053, 2314, null, new int[]{21054}, "", "Index patient refused treatment", -1));
        this.options.add(new Option(21053, 2315, null, new int[]{21054}, "", "Index patient is lost to follow up", -1));
        this.options.add(new Option(21053, 2316, new int[]{21054}, null, "", "Other", -1));
        this.options.add(new Option(21053, 2317, null, new int[]{21054}, "", "Unknown", -1));
        this.questions.add(new Question(true, endFollowupFormId, 21054, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If reason for lost of follow-up 'Other', then specify:", generateUUID(), alphanumeric100DigitSpace));


        this.questions.add(new Question(true, endFollowupFormId, 21055, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Treatment initiated at Referral / Transfer site", generateUUID(), null));
        this.options.add(new Option(21055, 2318, null, new int[]{21056}, "", "Yes", -1));
        this.options.add(new Option(21055, 2319, new int[]{21056}, null, "", "No", -1));
        this.options.add(new Option(21055, 2320, null, new int[]{21056}, "", "Unknown", -1));

        this.questions.add(new Question(true, endFollowupFormId, 21056, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Reason treatment not initiated at referral site", generateUUID(), null));
        this.options.add(new Option(21056, 2321, null, new int[]{21057}, "", "Patient could not be contacted", -1));
        this.options.add(new Option(21056, 2322, null, new int[]{21057}, "", "Patient left the city_village", -1));
        this.options.add(new Option(21056, 2323, null, new int[]{21057}, "", "Patient refused treatment", -1));
        this.options.add(new Option(21056, 2324, null, new int[]{21057}, "", "Patient died", -1));
        this.options.add(new Option(21056, 2325, null, new int[]{21057}, "", "DR not confirmed by baseline repeat test", -1));
        this.options.add(new Option(21056, 2326, new int[]{21057}, null, "", "Other", -1));

        this.questions.add(new Question(true, endFollowupFormId, 21057, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Please specify, if other", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(false, endFollowupFormId, 21058, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY, "If died, then provide date of death:", generateUUID(), dateMinLastYearMaxNextYear));
        this.questions.add(new Question(true, endFollowupFormId, 21059, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If died: then provide suspected primary cause of death:", generateUUID(), null));
        this.options.add(new Option(21059, 2327, null, new int[]{21060}, "", "TB immediate cause of death", -1));
        this.options.add(new Option(21059, 2328, null, new int[]{21060}, "", "Cause related to TB treatment", -1));
        this.options.add(new Option(21059, 2329, null, new int[]{21060}, "", "TB contributing to death", -1));
        this.options.add(new Option(21059, 2330, null, new int[]{21060}, "", "Surgery related death", -1));
        this.options.add(new Option(21059, 2331, new int[]{21060}, null, "", "Cause other than TB", -1));
        this.options.add(new Option(21059, 2332, null, new int[]{21060}, "", "Unknown", -1));

        this.questions.add(new Question(true, endFollowupFormId, 21060, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If primary cause of death other than TB, then specify:", generateUUID(), alphanumeric100DigitSpace));


    }


    private void initContactRegistryForm() {
        Integer ContactRegistryFormId = 10;

//        this.questions.add(new Question(true,ContactRegistryFormId,51003,"4",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Location",generateUUID(),null));
//        this.questions.add(new Question(false,ContactRegistryFormId,51004,"5",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Longitude",generateUUID(),null));
//        this.questions.add(new Question(false,ContactRegistryFormId,51005,"6",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Latitude",generateUUID(),null));

        //TODO Confirm LabelWidget

        this.questions.add(new Question(true, ContactRegistryFormId, 51000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday));

        this.questions.add(new Question(false, ContactRegistryFormId, 51006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Adult Males", generateUUID(), numeric2Digit));
        this.questions.add(new Question(false, ContactRegistryFormId, 51007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Adult Females", generateUUID(), numeric2Digit));
        this.questions.add(new Question(true, ContactRegistryFormId, 51008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Total Number of adult contacts ", generateUUID(), numeric2Digit));
        this.questions.add(new Question(false, ContactRegistryFormId, 51009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Male Children (between 5-15 yrs)", generateUUID(), numeric2Digit));
        this.questions.add(new Question(false, ContactRegistryFormId, 51010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Female Children (between 5-15 yrs)", generateUUID(), numeric2Digit));
        this.questions.add(new Question(true, ContactRegistryFormId, 51011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Total Number of childhood contacts (between 5-15 yrs)", generateUUID(), numeric2Digit));
        this.questions.add(new Question(false, ContactRegistryFormId, 51012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Male Children (under 5y)", generateUUID(), numeric2Digit));
        this.questions.add(new Question(false, ContactRegistryFormId, 51013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Female Children (under 5y)", generateUUID(), numeric2Digit));
        this.questions.add(new Question(true, ContactRegistryFormId, 51014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Total Number of childhood contacts (under 5 yrs)", generateUUID(), numeric2Digit));
        Question q =new Question(true, ContactRegistryFormId, 51015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Total Number of contacts", generateUUID(), numeric2Digit);
        this.questions.add(q);
        this.questions.add(new Question(true, ContactRegistryFormId, 51016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Details for Individual Contacts?", generateUUID(), null));
        this.options.add(new Option(51016, 5101, null, null, "1", "Yes", -1));
        this.options.add(new Option(51016, 5102, null, null, "2", "No", -1));

//TODO NEED TO REPEAT THIS NUMBER OF QUESTIONS TIMES
        q.setRepeatGroupHeadingPrefix("Contact Details");

        q.addRepeatable(new Question(true, ContactRegistryFormId, 51017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Contact Name", ParamNames.contact_name, alpha20DigitSpace));
        q.addRepeatable(new Question(true, ContactRegistryFormId, 51018, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Contact Age", ParamNames.contact_age, numeric2Digit));
        q.addRepeatable(new Question(true, ContactRegistryFormId, 51019, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Contact Gender", ParamNames.contact_gender, alpha7DigitSpace));

        this.questions.add(new Question(false, ContactRegistryFormId, 51020, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Contact Relationship to Index", generateUUID(), null));
        this.options.add(new Option(51020, 5101, null, null, "", "Mother", -1));
        this.options.add(new Option(51020, 5102, null, null, "", "Father", -1));
        this.options.add(new Option(51020, 5101, null, null, "", "Brother", -1));

        this.questions.add(new Question(true, ContactRegistryFormId, 51021, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Does the family agree for investigation of their contacts for PT?", generateUUID(), null));
        this.options.add(new Option(51021, 5101, null, new int[]{51022}, "1", "Yes", -1));
        this.options.add(new Option(51021, 5102, new int[]{51022}, null, "2", "No", -1));
        this.questions.add(new Question(true, ContactRegistryFormId, 51022, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Reason for not agreeing to investigation", generateUUID(), null));

    }

    private void initPatientCreation() {
        Integer patientCreationId = 1;
        questions.add(new Question(false, patientCreationId, 6999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Patient Registration Form", null, null));
        this.questions.add(new Question(true, patientCreationId, 6000, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_IDENTIFIER, View.VISIBLE, Validation.CHECK_FOR_MRNO, "Identifier", ParamNames.PROJECT_IDENTIFIER, numeric11Digit));
        this.questions.add(new Question(true, patientCreationId, 6001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Patient's name", FIRST_NAME, alpha50DigitSpace));
        this.questions.add(new Question(true, patientCreationId, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, alpha50DigitSpace));
        this.questions.add(new Question(true, patientCreationId, 6003, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Gender", SEX, null));
        this.options.add(new Option(6003, 604, null, null, "", "Male", -1));
        this.options.add(new Option(6003, 605, null, null, "", "Female", -1));
        //   this.questions.add(new Question(true, patientCreationId, 6004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Umar (Age in years)", "age", numeric3DigitMin1));
        this.questions.add(new Question(true, patientCreationId, 6004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_AGE, View.VISIBLE, Validation.CHECK_FOR_DATE, "Date of Birth", ParamNames.DOB, dob));
        //  this.questions.add(new Question(true, patientCreationId, 6005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Ghar ka patta - Ghar/Street #", "address1", alpha150DigitSpace));

        this.questions.add(new Question(true, patientCreationId, 6007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_ADDRESS, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Address", ParamNames.ADDRESS, addressConfiguration));

        this.questions.add(new Question(true, patientCreationId, 6006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Location", "location", null));
        this.options.addAll(DynamicOptions.getFromArray(context, 6006, null, null, context.getResources().getStringArray(R.array.locations_list)));
    }

    private void initPatientInformation() {
        int patientInfoFormId = 2;

        this.questions.add(new Question(true, patientInfoFormId, 20000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday));

        this.questions.add(new Question(true, patientInfoFormId, 20001, "1", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateTimeMinTodayMaxLastMonday));

        this.questions.add(new Question(true, patientInfoFormId, 20002, "2", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Location", null, null));
        this.options.add(new Option(20002, 2237, null, null, "", "Bedford Hospital", -1));
        this.options.add(new Option(20002, 2237, null, null, "", "Frere Clinic", -1));

        this.questions.add(new Question(true, patientInfoFormId, 20003, "3", InputWidget.InputWidgetsType.WIDGET_TYPE_GPS, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Geo Location", generateUUID(), alphaNumeric150DigitSpace));

        this.questions.add(new Question(true, patientInfoFormId, 20019, "-", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Questions", generateUUID(), null));

        this.questions.add(new Question(true, patientInfoFormId, 20004, "4", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Patient Source", generateUUID(), null));
        this.options.add(new Option(20004, 20002, null, new int[]{20005, 20006}, "", "Screening", -1));
        this.options.add(new Option(20004, 20002, null, new int[]{20005, 20006}, "", "Referred", -1));
        this.options.add(new Option(20004, 20002, new int[]{20006}, new int[]{20005}, "", "Contact of TB Patient", -1));
        this.options.add(new Option(20004, 20002, null, new int[]{20005, 20006}, "", "Walk-in / Self Referred", -1));
        this.options.add(new Option(20004, 20002, new int[]{20005}, new int[]{20006}, "", "Other", -1));

        this.questions.add(new Question(true, patientInfoFormId, 20005, "4.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify Other", generateUUID(), alphanumeric100DigitSpace));
        this.questions.add(new Question(true, patientInfoFormId, 20006, "4.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Index Patient ID", generateUUID(), alphanumeric13DigitWithHypen));

        this.questions.add(new Question(true, patientInfoFormId, 20007, "5", InputWidget.InputWidgetsType.WIDGET_TYPE_AUTOCOMPLETE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Primary Nationality", generateUUID(), alphanumeric100DigitSpace));
        this.options.addAll(DynamicOptions.getFromArray(context, 20007, null, null, context.getResources().getStringArray(R.array.countries_array)));

        this.questions.add(new Question(true, patientInfoFormId, 20008, "6", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "National ID Number", generateUUID(), numeric13DigitWithHypen));
        this.questions.add(new Question(true, patientInfoFormId, 20009, "7", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Whose National ID Card is this?", generateUUID(), null));
        this.options.add(new Option(20009, 20002, null, new int[]{20010}, "", "Self", -1));
        this.options.add(new Option(20009, 20002, null, new int[]{20010}, "", "Father", -1));
        this.options.add(new Option(20009, 20002, null, new int[]{20010}, "", "Spouse", -1));
        this.options.add(new Option(20009, 20002, null, new int[]{20010}, "", "Mother", -1));
        this.options.add(new Option(20009, 20002, null, new int[]{20010}, "", "Brother", -1));
        this.options.add(new Option(20009, 20002, null, new int[]{20010}, "", "Sister", -1));
        this.options.add(new Option(20009, 20002, null, new int[]{20010}, "", "Son", -1));
        this.options.add(new Option(20009, 20002, null, new int[]{20010}, "", "Daughter", -1));
        this.options.add(new Option(20009, 20002, null, new int[]{20010}, "", "Uncle", -1));
        this.options.add(new Option(20009, 20002, null, new int[]{20010}, "", "Aunt", -1));
        this.options.add(new Option(20009, 20002, new int[]{20010}, null, "", "Other", -1));
        this.questions.add(new Question(true, patientInfoFormId, 20010, "4.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify Other", generateUUID(), alphanumeric100DigitSpace));


        this.questions.add(new Question(true, patientInfoFormId, 20011, "4", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Where is the address located?", generateUUID(), null));
        this.options.add(new Option(20011, 20002, null, null, "", "Rural", -1));
        this.options.add(new Option(20011, 20002, null, null, "", "Urban", -1));

        this.questions.add(new Question(true, patientInfoFormId, 20012, "4", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Which type of address is this?", generateUUID(), null));
        this.options.add(new Option(20012, 20002, null, null, "", "Permanent", -1));
        this.options.add(new Option(20012, 20002, null, null, "", "Temporary", -1));

        this.questions.add(new Question(true, patientInfoFormId, 20018, "-", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Screener Instructions: Inform the patient regarding phone calls/sms", generateUUID(), mobileNumber));

        this.questions.add(new Question(true, patientInfoFormId, 20013, "4", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Can we call you and SMS you on any of these numbers for matters related to your TB tests and diagnosis?", generateUUID(), null));
        this.options.add(new Option(20013, 20002, null, null, "", "Yes", -1));
        this.options.add(new Option(20013, 20002, null, null, "", "No", -1));

        this.questions.add(new Question(true, patientInfoFormId, 20014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Mobile Number", generateUUID(), mobileNumber));
        this.questions.add(new Question(false, patientInfoFormId, 20015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Secondary Mobile Number", generateUUID(), mobileNumber));
        this.questions.add(new Question(true, patientInfoFormId, 20016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Landline Number", generateUUID(), landlineNumber));
        this.questions.add(new Question(false, patientInfoFormId, 20017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Secondary Landline Number", generateUUID(), landlineNumber));

    }

    private void initTBDiseaseConfirmationForm()
    {
        int TBDiseaseConfirmationFormId = 11;


//        this.questions.add(new Question(true,TBDiseaseConfirmationFormId,49001,"",InputWidget.InputWidgetsType.WIDGET_TYPE_DATE,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Form Entry Start Date/Time",ParamNames.date_start,null));
//        this.questions.add(new Question(true,TBDiseaseConfirmationFormId,49002,"",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"User ID",ParamNames.user_id,null));
//        this.questions.add(new Question(true,TBDiseaseConfirmationFormId,49003,"",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Patient ID",ParamNames.patient_id,null));
//        this.questions.add(new Question(true,TBDiseaseConfirmationFormId,49004,"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Location",ParamNames.,null));
//        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49005,"",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Longitude",ParamNames.,null));
//        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49006,"",InputWidget.InputWidgetsType.WIDGET_TYPE_a,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Latitude",ParamNames.,null));

        this.questions.add(new Question(true, TBDiseaseConfirmationFormId, 49000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", generateUUID(), dateMinTodayMaxLastMonday));


        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49007,"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Disease site",ParamNames.tb_type,null));
        this.options.add(new Option(49007, 6301, null, new int[]{49008}, "", "Pulmonary", -1));
        this.options.add(new Option(49007, 6302, new int[]{49008}, null, "", "Extra Pulmonary", -1));
        this.options.add(new Option(49007, 6303, new int[]{49008}, null, "", "Both", -1));



        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49008,"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.GONE,Validation.CHECK_FOR_EMPTY,"Extrapulmonary exact site:",ParamNames.extra_pulmonary_site,null));
        this.options.add(new Option(49008, 6304, null, new int[]{49009}, "", "Lymph Node", -1));
        this.options.add(new Option(49008, 6305, null, new int[]{49009}, "", "Abdomen", -1));
        this.options.add(new Option(49008, 6306, null, new int[]{49009}, "", "CNS (includes meningeal, brain)", -1));
        this.options.add(new Option(49008, 6307, null, new int[]{49009}, "", "Renal", -1));
        this.options.add(new Option(49008, 6308, null, new int[]{49009}, "", "Bones (includes spine)", -1));
        this.options.add(new Option(49008, 6309, null, new int[]{49009}, "", "Genitourinary", -1));
        this.options.add(new Option(49008, 6310, null, new int[]{49009}, "", "Pleural Effusion", -1));
        this.options.add(new Option(49008, 6311, null, new int[]{49009}, "", "Miliary", -1));
        this.options.add(new Option(49008, 6312, new int[]{49009}, null, "", "Other", -1));
        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49009,"",InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT,View.GONE,Validation.CHECK_FOR_EMPTY,"If extrapulmonary site 'Other', then specify:",ParamNames.extra_pulmonary_site_other,alphanumeric100DigitSpace));


        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49010,"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Confirmation of MTB?",ParamNames.mtb_confirmation,null));
        this.options.add(new Option(49010, 6313, new int[]{49011,49019},new int[]{49013,49020,49021}, "", "Bacteriologically positive", -1));
        this.options.add(new Option(49010, 6314, new int[]{49013,49020,49021}, new int[]{49011,49019}, "", "Bacteriologically negative,clinically diagnosed", -1));
        this.options.add(new Option(49010, 6315, new int[]{49013,49020,49021}, new int[]{49011,49019}, "", "Clinically diagnosed", -1));



        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49011,"",InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER,View.GONE,Validation.CHECK_FOR_EMPTY,"If diagnosis was \"bacteriologically confirmed\", what was the method of confirmation?",ParamNames.mtb_confirmation_method,null));
        this.options.add(new Option(49011, 6316, null,new int[]{49012}, "", "Smear", -1));
        this.options.add(new Option(49011, 6317, null,new int[]{49012}, "", "Xpert MTB/RIF", -1));
        this.options.add(new Option(49011, 6318, null,new int[]{49012}, "", "LPA test", -1));
        this.options.add(new Option(49011, 6319, null,new int[]{49012}, "", "Culture (solid or MGIT)", -1));
        this.options.add(new Option(49011, 6320,  new int[]{49012}, null, "", "Other", -1));
        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49012,"",InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT,View.GONE,Validation.CHECK_FOR_EMPTY,"If other method, specify:",ParamNames.mtb_confirmation_method_other,alphanumeric100DigitSpace));




        this.questions.add(new Question(true,TBDiseaseConfirmationFormId,49013,"",InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER,View.GONE,Validation.CHECK_FOR_EMPTY,"If diagnosis was \"clinically diagnosed\", what was the method of confirmation?",ParamNames.confirmed_diagnosis,null));
        this.options.add(new Option(49013, 6321, null,new int[]{49014}, "", "Chest X-ray", -1));
        this.options.add(new Option(49013, 6322, null,new int[]{49014}, "", "Ultrasound", -1));
        this.options.add(new Option(49013, 6323, null,new int[]{49014}, "", "CT Scan", -1));
        this.options.add(new Option(49013, 6324, null,new int[]{49014}, "", "Mantoux test", -1));
        this.options.add(new Option(49013, 6325, null,new int[]{49014}, "", "Histopathology/FNAC", -1));
        this.options.add(new Option(49013, 6326, null,new int[]{49014}, "", "CBC", -1));
        this.options.add(new Option(49013, 6327, null,new int[]{49014}, "", "ESR", -1));
        this.options.add(new Option(49013, 6328, new int[]{49014},null, "", "Other Diagnosis", -1));
        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49014,"",InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT,View.GONE,Validation.CHECK_FOR_EMPTY,"If other, please specify",ParamNames.other_diagnosis,alphanumeric100DigitSpace));



        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49015,"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Type of TB according to drug sensitivity",ParamNames.drug_resistance_profile,null));
        this.options.add(new Option(49015, 6329, null,new int[]{49016,49017,49018}, "", "Profile Unconfirmed", -1));
        this.options.add(new Option(49015, 6330, new int[]{49016},new int[]{49017,49018}, "", "Confirmed drug susceptible", -1));
        this.options.add(new Option(49015, 6331, new int[]{49017,49018},new int[]{49016}, "", " Confirmed drug resistant TB", -1));
        this.options.add(new Option(49015, 6332, null,new int[]{49016,49017,49018}, "", "Unknown", -1));

        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49016,"",InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT,View.GONE,Validation.CHECK_FOR_EMPTY,"National TB Registration number ",ParamNames.tb_registration_no,numeric13Digit));

        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49017,"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.GONE,Validation.CHECK_FOR_EMPTY,"Subclassification for confirmed drug resistant cases",ParamNames.drug_resistant_profile_class,null));
        this.options.add(new Option(49017, 6333, null,null, "", "MDR", -1));
        this.options.add(new Option(49017, 6334, null,null,"", "MONO", -1));
        this.options.add(new Option(49017, 6335, null,null, "", " POLY", -1));
        this.options.add(new Option(49017, 6336, null,null, "", "PRE-XDR (INJ)", -1));
        this.options.add(new Option(49017, 6337, null,null, "", "PRE-XDR (FQ)", -1));
        this.options.add(new Option(49017, 6338, null,null, "", " XDR", -1));
        this.options.add(new Option(49017, 6339, null,null, "", "RR-TB", -1));

        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49018,"",InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT,View.GONE,Validation.CHECK_FOR_EMPTY,"ENRS Number Assigned",ParamNames.enrs_no,numeric13Digit));

        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49019,"",InputWidget.InputWidgetsType.WIDGET_TYPE_DATE,View.GONE,Validation.CHECK_FOR_EMPTY,"MTB diagnosis date:",ParamNames.mtb_diagnosis_date,null));


        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49020,"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.GONE,Validation.CHECK_FOR_EMPTY,"Histopathological evidence",ParamNames.histopathology_evidence,null));
        this.options.add(new Option(49020, 6340, null,null, "", "Yes", -1));
        this.options.add(new Option(49020, 6341, null,null,"", "No", -1));



        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49021,"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.GONE,Validation.CHECK_FOR_EMPTY,"Radiological evidence",ParamNames.radiological_evidence,null));
        this.options.add(new Option(49021, 6342, null,null, "", "Yes", -1));
        this.options.add(new Option(49021, 6343, null,null,"", "No", -1));


        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49022,"",InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"CAD score",ParamNames.cad4tb_score,numeric3DigitMin1));

        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49023,"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Patient rendered eligible for treatment initiation?",ParamNames.eligible_for_treatment,null));
        this.options.add(new Option(49023, 6344, null,null, "", "Yes", -1));
        this.options.add(new Option(49023, 6345, null,null,"", "No", -1));
        this.options.add(new Option(49023, 6346, null,null, "", "Send for expert panel review", -1));


        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49024,"",InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Proposed Treatment Facility",ParamNames.proposed_treatment_facility,null));
        //TODO List of treatment
        this.questions.add(new Question(false,TBDiseaseConfirmationFormId,49025,"",InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT,View.VISIBLE,Validation.CHECK_FOR_EMPTY,"Other Proposed Treatment Facility",ParamNames.other_proposed_treatment_facility,alphanumeric100DigitSpace));


    }



    private void initEvaluatorCreation() {
        Integer personCreationId = 17;
        questions.add(new Question(false, personCreationId, 17001, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Evaluator/ Doctor Creation", null, null));
        // this.questions.add(new Question(true, personCreationId, 6000, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_IDENTIFIER, View.VISIBLE, Validation.CHECK_FOR_MRNO, "Identifier", ParamNames.PROJECT_IDENTIFIER, numeric11Digit));
        this.questions.add(new Question(true, personCreationId, 17002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "First name", FIRST_NAME, alpha50DigitSpace));
        this.questions.add(new Question(true, personCreationId, 17003, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Last name", LAST_NAME, alpha50DigitSpace));
        this.questions.add(new Question(true, personCreationId, 17004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Gender", SEX, null));
        this.options.add(new Option(17004, 604, null, null, "", "Male", -1));
        this.options.add(new Option(17004, 605, null, null, "", "Frere Clinic", -1));

        this.questions.add(new Question(true, personCreationId, 17005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Role", ParamNames.ATTRIBUTE_TYPE_PQ_PIRANI_SCORING_ROLE, null));
        this.options.add(new Option(17005, 604, null, null, "", "Physical Therapist", -1));
        this.options.add(new Option(17005, 605, null, null, "", "Doctor", -1));
    }


    //form1safecircumcision

    private void initForm1SafeCircumcision() {
        this.questions.add(new Question(true, 14, 14100, "A.2", InputWidget.InputWidgetsType.WIDGETS_TYPE_IDENTIFIER, View.VISIBLE, Validation.CHECK_FOR_CSC_ID, "Program ID", null, circumcisionIdentifier, Question.QUESTION_TAG.TAG_IDENTIFIER));
        this.options.add(new Option(14100, 90, null, null, "", "NOT ALLOWD TO EDIT", -1));


        questions.add(new Question(false, 14, 14000, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Basic Information", null, null));

        this.questions.add(new Question(true, 14, 4111, "A.6", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Date of procedure", PROCEDURE_DATE, dateTimeMinTodayMaxLastMonday));

        this.questions.add(new Question(true, 14, 14101, "A.7", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Age in days", null, numeric5Digit, Question.QUESTION_TAG.TAG_IDENTIFIER));

        this.questions.add(new Question(true, 14, 14002, "A.7a", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Age of baby </= 90 days ?", ParamNames.FORM1_AGE_BABY_LESS_THAN_90_DAYS, null));
        this.options.add(new Option(14002, 359, null, new int[]{14003}, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        this.options.add(new Option(14002, 359, new int[]{14003}, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        this.questions.add(new Question(true, 14, 14003, "A.7b", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Give reason ?", ParamNames.FORM1_REASON_OTHER, alphaNumeric50DigitSpace));
        this.options.add(new Option(14003, 359, null, new int[]{14004}, ParamNames.FORM1_BABY_PRE_TERM, "Baby was pre term", -1));
        this.options.add(new Option(14003, 359, new int[]{14004}, null, ParamNames.FORM1_OTHER, "Other", -1));

        this.questions.add(new Question(true, 14, 14004, "A.7c", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of other", "cfeb03b2-928b-4abe-95b6-70d0ed9dcbba", alphaNumeric150DigitSpace));

        this.questions.add(new Question(true, 14, 14005, "A8", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Term baby (>/= 37 completed weeks of gestation) ?", ParamNames.FORM1_TERM_BABY_GREATEROREQUAL_37, null));
        this.options.add(new Option(14005, 359, null, new int[]{14006}, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        this.options.add(new Option(14005, 359, new int[]{14006}, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));


        this.questions.add(new Question(true, 14, 14006, "A.8a", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Mention weeks of pregnancy ?", ParamNames.FORM1_MENTION_WEEK_OF_PRAGNANCY, null));
        this.options.add(new Option(14006, 338, null, new int[]{14007}, "", "<Select an option>", -1));
        this.options.add(new Option(14006, 359, null, new int[]{14007}, ParamNames.FORM1_32_WEEKS, "32 weeks", -1));
        this.options.add(new Option(14006, 359, null, new int[]{14007}, ParamNames.FORM1_33_WEEKS, "33 weeks", -1));
        this.options.add(new Option(14006, 359, null, new int[]{14007}, ParamNames.FORM1_34_WEEKS, "34 weeks", -1));
        this.options.add(new Option(14006, 359, null, new int[]{14007}, ParamNames.FORM1_35_WEEKS, "35 weeks", -1));
        this.options.add(new Option(14006, 359, null, new int[]{14007}, ParamNames.FORM1_36_WEEKS, "36 weeks", -1));
        this.options.add(new Option(14006, 359, new int[]{14007}, null, ParamNames.FORM1_OTHER, "Other", -1));
        this.options.add(new Option(14006, 359, null, new int[]{14007}, ParamNames.FORM1_DO_NOT_KNOW, "Don't know", -1));

        this.questions.add(new Question(true, 14, 14007, "A.8b", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of other", "6e37bd1c-98cd-421b-ac71-a9e8992567d2", alphaNumeric50DigitSpace));

        this.questions.add(new Question(true, 14, 14008, "A.9", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Weight of baby in kg", ParamNames.FORM1_WEIGHT_OF_BABY_KG, numeric4DigitMin1));

        this.questions.add(new Question(true, 14, 14009, "A.9a", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Weight of baby >/= 2.5 kg", ParamNames.FORM1_WEIGHT_OF_BABY_GREATER_OR_EQUAL_THATN_2_POINT_5, null));
        int[] allRest = new int[]{14010, 14011, 14012, 14013, 14016, 14017, 14018, 14019, 14022, 14024, 14025,
                14035, 14037, 14039, 14042,
                14045, 14047, 14048, 14049, 14050, 14052, 14054,
                14056, 14058, 14059};
        this.options.add(new Option(14009, 359, allRest, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        this.options.add(new Option(14009, 359, null, allRest, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        questions.add(new Question(true, 14, 14098, "B", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Other Information from parents/guardians:", null, null));

        questions.add(new Question(true, 14, 14010, "B.2a", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Contact 1", null, null));

        this.questions.add(new Question(true, 14, 14011, "B.2a.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Name", ParamNames.CONTACT1_NAME, alpha50DigitSpace));

        this.questions.add(new Question(true, 14, 14012, "B.2a.2", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Relation to the baby ?", ParamNames.FORM1_RELATION_TO_BABY, null));
        this.options.add(new Option(14012, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(14012, 359, null, null, ParamNames.FORM1_FATHER, "Father", -1));
        this.options.add(new Option(14012, 359, null, null, ParamNames.FORM1_MOTHER, "Mother", -1));
        this.options.add(new Option(14012, 359, null, null, ParamNames.FORM1_GRAND_FATHER, "Grand father", -1));
        this.options.add(new Option(14012, 359, null, null, ParamNames.FORM1_GRAND_MOTHER, "Grand mother", -1));
        this.options.add(new Option(14012, 359, null, null, ParamNames.FORM1_AUNCLE, "Uncle", -1));
        this.options.add(new Option(14012, 359, null, null, ParamNames.FORM1_AUNT, "Aunt", -1));
        this.options.add(new Option(14012, 359, null, null, ParamNames.FORM1_NEIGHBOUR, "Neighbour", -1));
        this.options.add(new Option(14012, 359, null, null, ParamNames.FORM1_FRIEND, "Friend", -1));
        this.options.add(new Option(14012, 359, null, null, ParamNames.FORM1_OTHER, "Other", -1));

        this.questions.add(new Question(true, 14, 14013, "B.2a.3", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Contact number", ParamNames.FORM1_CONTACT_NUMBER, null));
        this.options.add(new Option(14013, 338, null, new int[]{14014, 14015}, "", "<Select an option>", -1));
        this.options.add(new Option(14013, 359, new int[]{14014}, new int[]{14015}, ParamNames.FORM1_MOBILE, "Mobile number", -1));
        this.options.add(new Option(14013, 359, new int[]{14015}, new int[]{14014}, ParamNames.FORM1_LANDLINE, "Landline", -1));
        this.options.add(new Option(14013, 359, new int[]{14014, 14015}, null, ParamNames.FORM1_BOTH, "Both", -1));

        this.questions.add(new Question(true, 14, 14014, "B.2a.4", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Mobile number", ParamNames.FORM1_MOBILE, numeric11Digit));
        //this.options.add(new Option(14014, 359, null, null, null, "03XXXXXXXXX", -1));
        this.questions.add(new Question(true, 14, 14015, "B.2a.5", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Landline", ParamNames.FORM1_LANDLINE, numeric11Digit));

        questions.add(new Question(true, 14, 14016, "B.2b", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Contact 2", null, null));

        this.questions.add(new Question(true, 14, 14017, "B.2b.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Name", ParamNames.CONTACT1_2NAME, alpha50DigitSpace));

        this.questions.add(new Question(true, 14, 14018, "B.2b.2", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Relation to the baby ?", ParamNames.FORM1_2RELATION_TO_BABY, null));
        this.options.add(new Option(14018, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(14018, 359, null, null, ParamNames.FORM1_FATHER, "Father", -1));
        this.options.add(new Option(14018, 359, null, null, ParamNames.FORM1_MOTHER, "Mother", -1));
        this.options.add(new Option(14018, 359, null, null, ParamNames.FORM1_GRAND_FATHER, "Grand father", -1));
        this.options.add(new Option(14018, 359, null, null, ParamNames.FORM1_GRAND_MOTHER, "Grand mother", -1));
        this.options.add(new Option(14018, 359, null, null, ParamNames.FORM1_AUNCLE, "Uncle", -1));
        this.options.add(new Option(14018, 359, null, null, ParamNames.FORM1_AUNT, "Aunt", -1));
        this.options.add(new Option(14018, 359, null, null, ParamNames.FORM1_NEIGHBOUR, "Neighbour", -1));
        this.options.add(new Option(14018, 359, null, null, ParamNames.FORM1_FRIEND, "Friend", -1));
        this.options.add(new Option(14018, 359, null, null, ParamNames.FORM1_OTHER, "Other", -1));

        this.questions.add(new Question(true, 14, 14019, "B.2b.3", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Contact number", ParamNames.FORM1_2CONTACT_NUMBER, null));
        this.options.add(new Option(14019, 338, null, new int[]{14020, 14021}, "", "<Select an option>"));
        this.options.add(new Option(14019, 359, new int[]{14020}, new int[]{14021}, ParamNames.FORM1_MOBILE, "Mobile number"));
        this.options.add(new Option(14019, 359, new int[]{14021}, new int[]{14020}, ParamNames.FORM1_LANDLINE, "Landline"));
        this.options.add(new Option(14019, 359, new int[]{14020, 14021}, null, ParamNames.FORM1_BOTH, "Both"));

        this.questions.add(new Question(true, 14, 14020, "B.2b.4", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Mobile number", ParamNames.FORM1_2MOBILE, numeric11Digit));

        this.questions.add(new Question(true, 14, 14021, "B.2b.5", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Landline", ParamNames.FORM1_2LANDLINE, numeric11Digit));

        this.questions.add(new Question(true, 14, 14022, "B.3", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Place of delivery", ParamNames.FORM1_PLACE_OF_DELIVERY, null));
        this.options.add(new Option(14022, 338, null, new int[]{14023}, "", "<Select an option>", -1));
        this.options.add(new Option(14022, 359, null, new int[]{14023}, ParamNames.FORM1_INDUS_HOPITAL, "The Indus Hospital", -1));
        this.options.add(new Option(14022, 359, new int[]{14023}, null, ParamNames.FORM1_DIFF_FACILITY, "Different facility", -1));
        this.options.add(new Option(14022, 359, null, new int[]{14023}, ParamNames.FORM1_HOME, "Home", -1));
        this.options.add(new Option(14022, 359, null, new int[]{14023}, ParamNames.FORM1_REFUSED, "Refused", -1));

        this.questions.add(new Question(true, 14, 14023, "B.3a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If different facility, state name", ParamNames.FORM1_DIFF_FACILITY, alpha50DigitSpace));
/*
        this.questions.add(new Question(true, 14, 14024, "B.4", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Number of children", ParamNames.FORM1_NO_CHILDREN, numeric2Digit));

        Question q = (new Question(true, 14, 14025, "B.5a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "How many older sons?", ParamNames.FORM1_NO_OF_OLDER_SON, numeric2Digit));

        this.questions.add(q);
       // TODO Repeat group starts here
        q.setRepeatGroupHeadingPrefix("Son");

        q.addRepeatable(new Question(true, 14, 14027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Name of the son?", ParamNames.NAME_OF_SON, alpha50DigitSpace));

        q.addRepeatable(new Question(true, 14, 14028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Age in years", ParamNames.FORM1_AGE_OF_SON, numeric3DigitMin1));

        Question status = new Question(true, 14, 14029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Circumcision status", ParamNames.FORM1_CS_STAUTS, null);
        q.addRepeatable(status);
        status.addOption(new Option(14029, 359, null,  new int[]{14030,14031,14033,14102}, ParamNames.FORM1_CIRCUMSISED, "Uncircumcised", -1));
        status.addOption(new Option(14029, 359, new int[]{14030,14031,14033,14102}, null, ParamNames.FORM1_UNCIRCUMSISED, "Circumcised", -1));

        q.addRepeatable(new Question(true, 14, 14102, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Age at circumcision", ParamNames.FORM1_AGE_AT_CIRCUMCISION, alphaNumeric50DigitSpace));
        q.addRepeatable(new Question(true, 14, 14030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Age at circumcision in days", ParamNames.FORM1_AGE_AT_CIRCUMCISION_DAYS, numeric4DigitMin1));

        Question whoPerformed = new Question(true, 14, 14031, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Who performed the circumcision?", ParamNames.FORM1_WHO_PERFORMED_CS, null);
        q.addRepeatable(whoPerformed);
        whoPerformed.addOption(new Option(14031, 338, null, new int[]{14032}, "", "<Select an option>", -1));
        whoPerformed.addOption(new Option(14031, 359, null, new int[]{14032}, ParamNames.FORM1_DOCTOR, "Doctor", -1));
        whoPerformed.addOption(new Option(14031, 359, null, new int[]{14032}, ParamNames.FORM1_BARBER, "Barber", -1));
        whoPerformed.addOption(new Option(14031, 359, null, new int[]{14032}, ParamNames.FORM1_JARRAH, "Jarrah", -1));
        whoPerformed.addOption(new Option(14031, 359, null, new int[]{14032}, ParamNames.FORM1_MAULVI, "Maulvi", -1));
        whoPerformed.addOption(new Option(14031, 359, null, new int[]{14032}, ParamNames.FORM1_NURSE, "Nurse", -1));
        whoPerformed.addOption(new Option(14031, 359, null, new int[]{14032}, ParamNames.FORM1_TECHNICIAN, "Technician", -1));
        whoPerformed.addOption(new Option(14031, 359, null, new int[]{14032}, ParamNames.FORM1_MIDWIFE, "Midwife", -1));
        whoPerformed.addOption(new Option(14031, 359, null, new int[]{14032}, ParamNames.FORM1_DONT_REMEMBER, "Don't remember", -1));
        whoPerformed.addOption(new Option(14031, 359, new int[]{14032}, null, ParamNames.FORM1_OTHER, "Other", -1));
        whoPerformed.addOption(new Option(14031, 359, null, new int[]{14032}, ParamNames.FORM1_REFUSED, "Refused", -1));
        whoPerformed.addOption(new Option(14031, 359, null, new int[]{14032}, ParamNames.FORM1_DO_NOT_KNOW, "Don't know", -1));

        q.addRepeatable(new Question(true, 14, 14032, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of other", "cd4bbd66-6da6-4b60-8d91-0dd1d18f0612", alphaNumeric50DigitSpace));

        Question place = new Question(true, 14, 14033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Place of circumcision", ParamNames.FORM1_PLACE_OF_CS, null);
        q.addRepeatable(place);
        place.addOption(new Option(14033, 338, null, new int[]{14034}, "", "<Select an option>", -1));
        place.addOption(new Option(14033, 359, null, new int[]{14034}, ParamNames.FORM1_HOSPITAL, "Hospital", -1));
        place.addOption(new Option(14033, 359, null, new int[]{14034}, ParamNames.FORM1_CLINIC, "Clinic", -1));
        place.addOption(new Option(14033, 359, null, new int[]{14034}, ParamNames.FORM1_HOME, "Home", -1));
        place.addOption(new Option(14033, 359, null, new int[]{14034}, ParamNames.FORM1_DONT_REMEMBER, "Don't remember", -1));
        place.addOption(new Option(14033, 359, new int[]{14034}, null, ParamNames.FORM1_OTHER, "Other", -1));
        place.addOption(new Option(14033, 359, null, new int[]{14034}, ParamNames.FORM1_REFUSED, "Refused", -1));
        place.addOption(new Option(14033, 359, null, new int[]{14034}, ParamNames.FORM1_DO_NOT_KNOW, "Don't know", -1));

        q.addRepeatable(new Question(true, 14, 14034, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of other", "dfbbb5b4-b71f-4866-819e-85b59a257ec0", alphaNumeric50DigitSpace));
// TODO repeats end here*/
        this.questions.add(new Question(true, 14, 14035, "B.4", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Ethnicity", ParamNames.FORM1_ETHNICITY, null));
        this.options.add(new Option(14035, 338, null, new int[]{14036}, "", "<Select an option>", -1));
        this.options.add(new Option(14035, 359, null, new int[]{14036}, ParamNames.FORM1_BALOCHI, "Balochi", -1));
        this.options.add(new Option(14035, 359, null, new int[]{14036}, ParamNames.FORM1_MUHAJIR, "Muhajir", -1));
        this.options.add(new Option(14035, 359, null, new int[]{14036}, ParamNames.FORM1_PATHAN, "Pathan", -1));
        this.options.add(new Option(14035, 359, null, new int[]{14036}, ParamNames.FORM1_PUNJABI, "Punjabi", -1));
        this.options.add(new Option(14035, 359, null, new int[]{14036}, ParamNames.FORM1_SINDHI, "Sindhi", -1));
        this.options.add(new Option(14035, 359, null, new int[]{14036}, ParamNames.FORM1_SARAIKI, "Siraiki", -1));
        this.options.add(new Option(14035, 359, null, new int[]{14036}, ParamNames.FORM1_DO_NOT_KNOW, "Don't know", -1));
        this.options.add(new Option(14035, 359, new int[]{14036}, null, ParamNames.FORM1_OTHER, "Other", -1));

        this.questions.add(new Question(true, 14, 14036, "B.4a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of other", "5ca51ccb-48c5-4129-adf9-eab8a0fc3262", alphaNumeric50DigitSpace));

        this.questions.add(new Question(true, 14, 14037, "B.5", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Religion of parent / guardian", ParamNames.FORM1_RELIGION_PARENT, null));
        this.options.add(new Option(14037, 338, null, new int[]{14038}, "", "<Select an option>", -1));
        this.options.add(new Option(14037, 359, null, new int[]{14038}, ParamNames.FORM1_ISLAM, "Muslim", -1));
        this.options.add(new Option(14037, 359, null, new int[]{14038}, ParamNames.FORM1_CHRISTIAN, "Christian", -1));
        this.options.add(new Option(14037, 359, null, new int[]{14038}, ParamNames.FORM1_HINDU, "Hindu", -1));
        this.options.add(new Option(14037, 359, null, new int[]{14038}, ParamNames.FORM1_PARSI, "Parsi", -1));
        this.options.add(new Option(14037, 359, new int[]{14038}, null, ParamNames.FORM1_OTHER, "Other", -1));

        this.questions.add(new Question(true, 14, 14038, "B.5a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of other", "ea9a5643-28e3-44c6-853a-69397d458615", alpha20DigitSpace));

        this.questions.add(new Question(true, 14, 14039, "B.6a", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "How many years of formal schooling of father?", ParamNames.FORM1_FORMAL_SCHOOLING_FATHER, null));
        this.options.add(new Option(14039, 338, null, new int[]{14040}, "", "<Select an option>", -1));
        this.options.add(new Option(14039, 359, new int[]{14040}, null, ParamNames.FORM1_0_2_YEARS, "0-2 years (Uneducated)", -1));
        this.options.add(new Option(14039, 359, null, new int[]{14040}, ParamNames.FORM1_3_10_YEARS, "3-10 years", -1));
        this.options.add(new Option(14039, 359, null, new int[]{14040}, ParamNames.FORM1_MORE_THAN_10_YEARS, "More than 10 years", -1));

        this.questions.add(new Question(true, 14, 14040, "B.6b", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "What type of informal education of father?", ParamNames.FORM1_INFORMAL_EDUCATION_FATHER, null));
        this.options.add(new Option(14040, 338, null, new int[]{14041}, "", "<Select an option>", -1));
        this.options.add(new Option(14040, 359, null, new int[]{14041}, ParamNames.FORM1_MADRASSA, "Madrassa", -1));
        this.options.add(new Option(14040, 359, null, new int[]{14041}, ParamNames.FORM1_ADULT_LITRACY, "Adult literacy", -1));
        this.options.add(new Option(14040, 359, null, new int[]{14041}, ParamNames.FORM1_HOME_SCHOOLING, "Home schooling", -1));
        this.options.add(new Option(14040, 359, null, new int[]{14041}, ParamNames.FORM1_SELF_READING, "Self learnt (reading)", -1));
        this.options.add(new Option(14040, 359, null, new int[]{14041}, ParamNames.FORM1_SELF_READING_WRITING, "Self learnt (reading and writing)", -1));
        this.options.add(new Option(14040, 359, null, new int[]{14041}, ParamNames.FORM1_NOT_EDUCATED, "Not educated", -1));
        this.options.add(new Option(14040, 359, new int[]{14041}, null, ParamNames.FORM1_OTHER, "Other", -1));

        this.questions.add(new Question(true, 14, 14041, "B.6b.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of other", "e6179aa6-af7d-43a7-8a5b-19c5ea88bbad", alphaNumeric50DigitSpace));

        this.questions.add(new Question(true, 14, 14042, "B.7a", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "How many years of formal schooling of mother?", ParamNames.FORM1_INFORMAL_EDUCATION_MOTHER, null));
        this.options.add(new Option(14042, 338, null, new int[]{14043}, "", "<Select an option>", -1));
        this.options.add(new Option(14042, 359, new int[]{14043}, null, ParamNames.FORM1_0_2_YEARS, "0-2 years (Uneducated)", -1));
        this.options.add(new Option(14042, 359, null, new int[]{14043}, ParamNames.FORM1_3_10_YEARS, "3-10 years", -1));
        this.options.add(new Option(14042, 359, null, new int[]{14043}, ParamNames.FORM1_MORE_THAN_10_YEARS, "More than 10 years", -1));

        this.questions.add(new Question(true, 14, 14043, "B.7b", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "What type of informal education of mother?", ParamNames.FORM1_INFORMAL_EDUCATION_MOTHER, null));
        this.options.add(new Option(14043, 338, null, new int[]{14044}, "", "<Select an option>", -1));
        this.options.add(new Option(14043, 359, null, new int[]{14044}, ParamNames.FORM1_MADRASSA, "Madrassa", -1));
        this.options.add(new Option(14043, 359, null, new int[]{14044}, ParamNames.FORM1_ADULT_LITRACY, "Adult literacy", -1));
        this.options.add(new Option(14043, 359, null, new int[]{14044}, ParamNames.FORM1_HOME_SCHOOLING, "Home schooling", -1));
        this.options.add(new Option(14043, 359, null, new int[]{14044}, ParamNames.FORM1_SELF_READING, "Self learnt (reading)", -1));
        this.options.add(new Option(14043, 359, null, new int[]{14044}, ParamNames.FORM1_SELF_READING_WRITING, "Self learnt (reading and writing)", -1));
        this.options.add(new Option(14043, 359, null, new int[]{14044}, ParamNames.FORM1_NOT_EDUCATED, "Not educated", -1));
        this.options.add(new Option(14043, 359, new int[]{14044}, null, ParamNames.FORM1_OTHER, "Other", -1));

        this.questions.add(new Question(true, 14, 14044, "B.7b.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of other", "1f562534-b8fb-4767-9c19-273576229fad", alphaNumeric50DigitSpace));

        this.questions.add(new Question(true, 14, 14045, "B.8", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Occupation of father", ParamNames.FORM1_OCCUPATION_FATHER, null));
        this.options.add(new Option(14045, 338, null, new int[]{14046}, "", "<Select an option>", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_MANAGER, "Manager", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_PROFESSIONAL, "Professional", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_TECHNICIAN, "Technician", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_CLERCK, "Clerk", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_SALES_WORKER, "Service/Sales worker ", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_AGRICULTURE_WORKER, "Agriculture/fishery worker", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_CRAFT_WORKER, "Craft worker", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_OPERATOR_DRIVER, "Machine operator/Driver", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_LABOUR, "Cleaner/ Labourer", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_IN_FORCES, "In forces (Police, army etc)", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_UNEMPLOYED, "Unemployed", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_STUDENT, "Student", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_RETIRED, "Retired/Pensioner", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_NOT_ALIVE, "Not alive", -1));
        this.options.add(new Option(14045, 359, new int[]{14046}, null, ParamNames.FORM1_OTHER, "Other", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_REFUSED, "Refused", -1));
        this.options.add(new Option(14045, 359, null, new int[]{14046}, ParamNames.FORM1_DO_NOT_KNOW, "Don't know", -1));

        this.questions.add(new Question(true, 14, 14046, "B.8a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify the Other option", "f811a15f-9f80-4021-9e24-ceeabc74b579", alphaNumeric50DigitSpace));

        this.questions.add(new Question(false, 14, 14047, "B.8.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Spontaneous response:", ParamNames.FORM1_SPONTANOUS_RESPONSE, alphaNumeric200DigitSpace));

        this.questions.add(new Question(true, 14, 14048, "B.9", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Does the mother have a job (from which she earns) apart from household work?", ParamNames.FORM1_MOTHER_JOB, null));
        this.options.add(new Option(14048, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(14048, 359, null, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14048, 359, null, null, ParamNames.FORM1_NO, "No", -1));
        this.options.add(new Option(14048, 359, null, null, ParamNames.FORM1_REFUSED, "Refused", -1));
        this.options.add(new Option(14048, 359, null, null, ParamNames.FORM1_DO_NOT_KNOW, "Don't know", -1));

        this.questions.add(new Question(true, 14, 14049, "B.10", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Family system", ParamNames.FORM1_FAMILY_SYS, null));
        this.options.add(new Option(14049, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(14049, 359, null, null, ParamNames.FORM1_JOINT, "Joint family", -1));
        this.options.add(new Option(14049, 359, null, null, ParamNames.FORM1_NUCLEAR, "Nuclear family", -1));
        this.options.add(new Option(14049, 359, null, null, ParamNames.FORM1_REFUSED, "Refused", -1));

        this.questions.add(new Question(true, 14, 14050, "B.11", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Primary reason for circumcision", ParamNames.FORM1_PRIMERY_REASON_CS, null));
        this.options.add(new Option(14050, 338, null, new int[]{14051}, "", "<Select an option>", -1));
        this.options.add(new Option(14050, 359, null, new int[]{14051}, ParamNames.FORM1_RELIGIOUS, "Religious", -1));
        this.options.add(new Option(14050, 359, null, new int[]{14051}, ParamNames.FORM1_CULTURE, "Cultural/Traditional", -1));
        this.options.add(new Option(14050, 359, null, new int[]{14051}, ParamNames.FORM1_MEDICAL, "Medical", -1));
        this.options.add(new Option(14050, 359, null, new int[]{14051}, ParamNames.FORM1_IMPROVE_SEXUAL_SATISFICATION, "To improve sexual satisfaction", -1));
        this.options.add(new Option(14050, 359, new int[]{14051}, null, ParamNames.FORM1_OTHER, "Other", -1));

        this.questions.add(new Question(true, 14, 14051, "B.11a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify the Other option", "e70f03a1-7808-4435-8dde-e6d57a1991b1", alphaNumeric50DigitSpace));

        this.questions.add(new Question(true, 14, 14052, "B.12", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "How did you find out about this Circumcision Clinic?", ParamNames.FORM1_FINDING_CS_CLINIC, null));
        this.options.add(new Option(14052, 338, null, new int[]{14053}, "", "<Select an option>", -1));
        this.options.add(new Option(14052, 359, null, new int[]{14053}, ParamNames.FORM1_SHAIKH_SAEED_HOSPITAL, "Sheikh Saeed Hospital", -1));
        this.options.add(new Option(14052, 359, null, new int[]{14053}, ParamNames.FORM1_TRADITIONAL_BIRTH_ATTENDANT, "Traditional birth attendant", -1));
        this.options.add(new Option(14052, 359, null, new int[]{14053}, ParamNames.FORM1_BROUCHER, "Brochure", -1));
        this.options.add(new Option(14052, 359, null, new int[]{14053}, ParamNames.FORM1_MATERNITY_HOME, "Maternity home", -1));
        this.options.add(new Option(14052, 359, null, new int[]{14053}, ParamNames.FORM1_LADY_HEALTH_WORKER, "Lady health worker", -1));
        this.options.add(new Option(14052, 359, null, new int[]{14053}, ParamNames.FORM1_VACCINATOR, "Vaccinator", -1));
        this.options.add(new Option(14052, 359, null, new int[]{14053}, ParamNames.FORM1_INDUS_HOSPITAL, "Indus Hospital", -1));
        this.options.add(new Option(14052, 359, null, new int[]{14053}, ParamNames.FORM1_WORD_OF_MOUTH, "Word of mouth", -1));
        this.options.add(new Option(14052, 359, null, new int[]{14053}, ParamNames.FORM1_VIDEO, "Video", -1));
        this.options.add(new Option(14052, 359, new int[]{14053}, null, ParamNames.FORM1_OTHER, "Other", -1));

        this.questions.add(new Question(true, 14, 14053, "B.12a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify the Other option", "39162ead-28bd-49f5-aa81-89c79b36d8e9", alphaNumeric50DigitSpace));

        /*this.questions.add(new Question(true, 14, 14054, "B.13a", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Have you seen the circumcision brochure?", ParamNames.FORM1_SEEN_CS_BROUCHER, null));
        this.options.add(new Option(14054, 338, null, new int[]{14055}, "", "<Select an option>", -1));
        this.options.add(new Option(14054, 359, new int[]{14055}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14054, 359, null, new int[]{14055}, ParamNames.FORM1_NO, "No", -1));
        this.options.add(new Option(14054, 359, null, new int[]{14055}, ParamNames.FORM1_DO_NOT_KNOW, "Dont know", -1));

        this.questions.add(new Question(true, 14, 14055, "B.13b", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If yes, did the brochure help you decide to opt for early and safe circumcision?", ParamNames.FORM1_BROUCHER_HELP, null));
        this.options.add(new Option(14055, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(14055, 359, null, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14055, 359, null, null, ParamNames.FORM1_NO, "No", -1));
        this.options.add(new Option(14055, 359, null, null, ParamNames.FORM1_DO_NOT_KNOW, "Dont know", -1));

        this.questions.add(new Question(true, 14, 14056, "B.14a", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Have you seen the circumcision video?", ParamNames.FORM1_SEEN_CS_VIDEO, null));
        this.options.add(new Option(14056, 338, null, new int[]{14057}, "", "<Select an option>", -1));
        this.options.add(new Option(14056, 359, new int[]{14057}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14056, 359, null, new int[]{14057}, ParamNames.FORM1_NO, "No", -1));
        this.options.add(new Option(14056, 359, null, new int[]{14057}, ParamNames.FORM1_DO_NOT_KNOW, "Dont know", -1));

        this.questions.add(new Question(true, 14, 14057, "B.14b", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If yes, did the video help you decide to opt for early and safe circumcision?", ParamNames.FORM1_VIDEO_HELP, null));
        this.options.add(new Option(14057, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(14057, 359, null, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14057, 359, null, null, ParamNames.FORM1_NO, "No", -1));
        this.options.add(new Option(14057, 359, null, null, ParamNames.FORM1_DO_NOT_KNOW, "Dont know", -1));*/

        questions.add(new Question(false, 14, 14058, "C", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Medical history of baby:", null, null));

        this.questions.add(new Question(true, 14, 14059, "C.1", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Bleeding disorder", ParamNames.FORM1_BLEEDING_DISORDER, null));
        this.options.add(new Option(14059, 359,
                null,
                new int[]{14060, 14061, 14062, 14063, 14064, 14065, 14066, 14067, 14068, 14069, 14070, 14071}, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14059, 359,
                new int[]{14060, 14061, 14062, 14063, 14064, 14065, 14066, 14067, 14068, 14069, 14070, 14071},
                null, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14060, "C.2", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Convulsions", ParamNames.FORM1_CONVULSIONS_BABY, null));
        this.options.add(new Option(14060, 359, new int[]{140100}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14060, 359, null, new int[]{140100}, ParamNames.FORM1_NO, "No", -1));
        this.questions.add(new Question(true, 14, 140100, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Give details", ParamNames.CONVELSION_DETAILS, alphaNumeric200DigitSpace));

        this.questions.add(new Question(true, 14, 14061, "C.3", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Hospitalization of baby after birth", ParamNames.FORM1_HOSPITALIZATION_AFTER_BIRTH, null));
        this.options.add(new Option(14061, 359, new int[]{14062, 14064}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14061, 359, null, new int[]{14062, 14064}, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14062, "C.3a", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Give reason", ParamNames.FORM1_GIVE_REASON, null));
        this.options.add(new Option(14062, 338, null, new int[]{14063}, "", "<Select an option>", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_JAUNDICE, "Jaundice", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_RESPIRATORY_PROB, "Respiratory problem", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_DIARRHOEA, "Diarrhoea", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_ASPIRATION_OF_WATER, "Aspiration of water by baby", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_LOW_BLOOD_SUGAR, "Low blood sugar", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_HEART_PROB, "Heart problem", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_CONVULSIONS_BABY, "Convulsions", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_INFECTION, "Infection", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_FEVER, "Fever", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_SURGREY, "Surgery", -1));
        this.options.add(new Option(14062, 359, new int[]{14063}, null, ParamNames.FORM1_OTHER, "Other", -1));
        this.options.add(new Option(14062, 359, null, new int[]{14063}, ParamNames.FORM1_DO_NOT_KNOW, "Don't know", -1));

        this.questions.add(new Question(true, 14, 14063, "C.3a.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Describe Other", "1e0ecccf-771f-498c-afff-5a2bf4d4f734", alphaNumeric100DigitSpace));

        this.questions.add(new Question(true, 14, 14064, "C.3b", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Give duration", ParamNames.FORM1_GIVE_DURATION, null));
        this.options.add(new Option(14064, 338, null, new int[]{14063}, "", "<Select an option>", -1));
        this.options.add(new Option(14064, 359, null, new int[]{14063}, ParamNames.FORM1_1_DAY, "1 day", -1));
        this.options.add(new Option(14064, 359, null, new int[]{14063}, ParamNames.FORM1_2_5_DAY, "2-5 days", -1));
        this.options.add(new Option(14064, 359, null, new int[]{14063}, ParamNames.FORM1_6_10_DAY, "6-10 days", -1));
        this.options.add(new Option(14064, 359, null, new int[]{14063}, ParamNames.FORM1_MORE_THAN_10_DAY, "More than 10 days", -1));
        this.options.add(new Option(14064, 359, null, new int[]{14063}, ParamNames.FORM1_DO_NOT_KNOW, "Don't know", -1));

        this.questions.add(new Question(true, 14, 14065, "C.4", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Vitamin K given to the baby after birth?", ParamNames.FORM1_VITAMIN_K_GIVEN_TO_BABY, null));
        this.options.add(new Option(14065, 338, null, new int[]{14066, 140101}, "", "<Select an option>", -1));
        this.options.add(new Option(14065, 359, null, new int[]{14066, 140101}, ParamNames.FORM1_YES_AT_INDUS, "Yes, at Indus Hospital", -1));
        this.options.add(new Option(14065, 359, new int[]{140101}, new int[]{14066}, ParamNames.FORM1_YES_AT_OTHER, "Yes, at other facility", -1));
        this.options.add(new Option(14065, 359, new int[]{14066}, new int[]{140101}, ParamNames.FORM1_NO, "No", -1));
        this.options.add(new Option(14065, 359, new int[]{14066}, new int[]{140101}, ParamNames.FORM1_DO_NOT_KNOW, "Dont know", -1));
        this.questions.add(new Question(true, 14, 140101, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Give details of other facility", ParamNames.OTHER_FACILITY_DETAILS, alphaNumeric200DigitSpace));

        this.questions.add(new Question(true, 14, 14066, "C.4a", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Vitamin K administered at CC clinic at the time of  circumcision?", ParamNames.FORM1_VITAMIN_K_ADMINISTERED, null));
        this.options.add(new Option(14066, 338, null, new int[]{14067}, "", "<Select an option>", -1));
        this.options.add(new Option(14066, 359, null, new int[]{14067}, ParamNames.FORM1_YES, "Yes", -1));
        // this.options.add(new Option(14066, 359, null, new int[]{14067}, ParamNames.FORM1_NO, "No", -1));
        this.options.add(new Option(14066, 359, new int[]{14067}, null, ParamNames.FORM1_OTHER, "Other", -1));
        this.options.add(new Option(14066, 359, null, new int[]{14067}, ParamNames.FORM1_REFUSED, "Refused", -1));

        this.questions.add(new Question(true, 14, 14067, "C.4a.a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify the Other option", "27f161a6-3e6f-4a66-bb11-43a0bdce30df", alphaNumeric50DigitSpace));

        this.questions.add(new Question(true, 14, 14068, "C.5", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Family h/o  bleeding disorders (e.g haemophilia)", ParamNames.FORM1_HAEMOPHELIA, null));
        this.options.add(new Option(14068, 338, null, new int[]{140102}, "", "<Select an option>", -1));
        this.options.add(new Option(14068, 359, new int[]{140102}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14068, 359, null, new int[]{140102}, ParamNames.FORM1_NO, "No", -1));
        this.options.add(new Option(14068, 359, null, new int[]{140102}, ParamNames.FORM1_DO_NOT_KNOW, "Dont know", -1));
        questions.add(new Question(true, 14, 140102, "C.5a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Give details", ParamNames.FAMILY_BLOOD_DISORDER_HISTORY_DETAILS, alphaNumeric200DigitSpace));

        this.questions.add(new Question(true, 14, 14069, "C.6", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Any other condition ", ParamNames.FORM1_OTHER_CONDITION, null));
        this.options.add(new Option(14069, 359, new int[]{14070}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14069, 359, null, new int[]{14070}, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14070, "C.6a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details", ParamNames.FORM1_GIVE_DETAIL, alphaNumeric200DigitSpace));

        this.questions.add(new Question(true, 14, 14071, "C.7a", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Baby is feeding after birth", ParamNames.FORM1_FEEDING_AFTER_BIRTH, null));
        this.options.add(new Option(14071, 359, new int[]{14072}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14071, 359, null, new int[]{14072}, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14072, "C.7b", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Baby has passed urine after birth", ParamNames.FORM1_URINE_AFTER_BIRTH, null));
        this.options.add(new Option(14072, 359,
                new int[]{
                        14073, 14074, 14075, 14076, 14077, 14078, 14079, 14080, 14081, 14082, 14083, 14084, 14085,
                        14086, 14087, 14088, 14089, 14090, 14091, 14092, 14093, 14094, 14095, 14096},
                null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14072, 359, null,
                new int[]{
                        14073, 14074, 14075, 14076, 14077, 14078, 14079, 14080, 14081, 14082, 14083, 14084, 14085,
                        14086, 14087, 14088, 14089, 14090, 14091, 14092, 14093, 14094, 14095, 14096},
                ParamNames.FORM1_NO, "No", -1));

        questions.add(new Question(false, 14, 14073, "D", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Physical examination :", null, null));

        this.questions.add(new Question(true, 14, 14074, "D.1a", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Temperature recorded", ParamNames.FORM1_TEMPRATURE_RECORDED, null));
        this.options.add(new Option(14074, 359, new int[]{14075}, new int[]{14076}, ParamNames.FORM1_FAHRENHIET, "Fahrenheit", -1));
        this.options.add(new Option(14074, 359, new int[]{14076}, new int[]{14075}, ParamNames.FORM1_CENTIGRADE, "Centigrade", -1));

        this.questions.add(new Question(true, 14, 14075, "D.1a.1", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Temperature - Axillary: (97.5*F-99.3*F)", ParamNames.FORM1_AXILLARY_FAHRENHIETE, null));
        this.options.add(new Option(14075, 359, null, null, ParamNames.FORM1_97_POINT_5_TO_98, "97.5*F - 98*F", -1));
        this.options.add(new Option(14075, 359, null, null, ParamNames.FORM1_98_POINT_1_TO_98_POINT_9, "98.1*F - 98.9*F", -1));
        this.options.add(new Option(14075, 359, null, null, ParamNames.FORM1_99_TO_99_POINT_3, "99*F - 99.3*F", -1));

        this.questions.add(new Question(true, 14, 14076, "D.1a.2", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Temperature - Axillary: (36.5*C- 37.4*C)", ParamNames.FORM1_AXILLARY_CENTIGRADE, null));
        this.options.add(new Option(14076, 359, null, null, ParamNames.FORM1_36_POINT_5_TO_37_FAHRENHIETE, "36.5*C - 37*F", -1));
        this.options.add(new Option(14076, 359, null, null, ParamNames.FORM1_37_POINT_1_TO_37_POINT_4_FAHRENHIETE, "37.1*F - 37.4*F", -1));

        this.questions.add(new Question(true, 14, 14077, "D.1b", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Pulse (90-160 beats per minute)", ParamNames.FORM1_PULSE, null));
        this.options.add(new Option(14077, 359, null, null, ParamNames.FORM1_90_120, "90-120", -1));
        this.options.add(new Option(14077, 359, null, null, ParamNames.FORM1_121_160, "121-160", -1));

        this.questions.add(new Question(true, 14, 14078, "D.1c", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Respiratory rate (30-60 breaths per minute)", ParamNames.FORM1_RESPIRATORY, null));
        this.options.add(new Option(14078, 359, null, null, ParamNames.FORM1_30_40, "30 - 40", -1));
        this.options.add(new Option(14078, 359, null, null, ParamNames.FORM1_41_50, "41 - 50", -1));
        this.options.add(new Option(14078, 359, null, null, ParamNames.FORM1_51_60, "51 - 60", -1));

        this.questions.add(new Question(true, 14, 14079, "D.2", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Jaundice", ParamNames.FORM1_JAUNDICE_BABY, null));
        this.options.add(new Option(14079, 359, new int[]{14080, 14081}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14079, 359, null, new int[]{14080, 14081}, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14080, "D.2a", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "If yes, type of jaundice?", ParamNames.FORM1_JAUNDICE_TYPE, null));
        this.options.add(new Option(14080, 359, null, null, ParamNames.FORM1_PHYSIOLOGICAL, "Physiological", -1));
        this.options.add(new Option(14080, 359, null, null, ParamNames.FORM1_PATHALOGICAL, "Pathological", -1));

        this.questions.add(new Question(true, 14, 14081, "D.2b", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If yes, severity of jaundice?", ParamNames.FORM1_SEVERITY_OF_JAUNDICE, null));
        this.options.add(new Option(14081, 359, null, null, ParamNames.FORM1_MILD, "Mild", -1));
        this.options.add(new Option(14081, 359, null, null, ParamNames.FORM1_MODERATE, "Moderate", -1));
        this.options.add(new Option(14081, 359, null, null, ParamNames.FORM1_SEVERE, "Severe", -1));

        this.questions.add(new Question(true, 14, 14082, "D.3", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Any other abnormal findings:", ParamNames.FORM1_ABNORMAL_FINDING, null));
        this.options.add(new Option(14082, 359, new int[]{14083}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14082, 359, null, new int[]{14083}, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14083, "D.3a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details", ParamNames.FORM1_GIVE_DETAIL, alphaNumeric200DigitSpace));

        questions.add(new Question(false, 14, 14084, "E", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Genital Examination:", null, null));

        this.questions.add(new Question(true, 14, 14085, "E.1", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Hypospadias", ParamNames.FORM1_HYPOSPADIAS_BABY, null));
        this.options.add(new Option(14085, 359, null, new int[]{14086}, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14085, 359, new int[]{14086}, null, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14086, "E.2", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Epispadias", ParamNames.FORM1_EPSIPADIAS_BABY, null));
        this.options.add(new Option(14086, 359, null, new int[]{14087}, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14086, 359, new int[]{14087}, null, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14087, "E.3", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Congenital chordee", ParamNames.FORM1_CONGENITAL_BABY, null));
        this.options.add(new Option(14087, 359, null, new int[]{14088}, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14087, 359, new int[]{14088}, null, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14088, "E.4", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Micropenis", ParamNames.FORM1_MICRO_PENIS_BABY, null));
        this.options.add(new Option(14088, 359, null, new int[]{14089}, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14088, 359, new int[]{14089}, null, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14089, "E.5", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Ambiguous genitalia", ParamNames.FORM1_AMBIGUOUS_GENITALIA_BABY, null));
        this.options.add(new Option(14089, 359, null, new int[]{14090}, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14089, 359, new int[]{14090}, null, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14090, "E.6", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Penoscrotal web", ParamNames.FORM1_PENOSCROTAL, null));
        this.options.add(new Option(14090, 359, null, new int[]{14091}, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14090, 359, new int[]{14091}, null, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14091, "E.7", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Congenital buried penis", ParamNames.FORM1_CONGENITAL_BURRIED_PENIS, null));
        this.options.add(new Option(14091, 359, null, new int[]{14092}, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14091, 359, new int[]{14092}, null, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14092, "E.8", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Bilateral Hydroceles", ParamNames.FORM1_BILATERAL, null));
        this.options.add(new Option(14092, 359, null, new int[]{14093}, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14092, 359, new int[]{14093}, null, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14093, "E.9", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Any other abnormal findings in genital examination", ParamNames.FORM1_ABNORMAL_FINDING_GENETAL, null));
        this.options.add(new Option(14093, 359, new int[]{14094}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14093, 359, null, new int[]{14094}, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14094, "E.9a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details", ParamNames.FORM1_GIVE_DETAIL, alphaNumeric150DigitSpace));

        questions.add(new Question(false, 14, 14095, "F", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Informed Consent:", null, null));

        this.questions.add(new Question(true, 14, 14096, "F.1", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Have the baby's parents/guardians given informed consent?", ParamNames.FORM1_PARENT_GIVEN_INFORMED_CONSENT, null));
        this.options.add(new Option(14096, 359, new int[]{14097, 14099, 14103, 14104}, null, ParamNames.FORM1_YES, "Yes", -1));
        this.options.add(new Option(14096, 359, null, new int[]{14097, 14099, 14103, 14104}, ParamNames.FORM1_NO, "No", -1));

        this.questions.add(new Question(true, 14, 14104, "F.2", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Parent / legal guardian's name", ParamNames.FORM1_LEGAL_GUARDIAN_NAME, alpha20DigitSpace));

        questions.add(new Question(false, 14, 14103, "F.3", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Relation to baby", "798d1b29-85cf-4a23-90a7-77ae3fa651f3", null));
        options.add(new Option(14103, 338, null, new int[]{14105}, "", "<Select an option>", -1));
        options.add(new Option(14103, 550, null, new int[]{14105}, "Father", "Father", -1));
        options.add(new Option(14103, 551, null, new int[]{14105}, "Mother", "Mother", -1));
        options.add(new Option(14103, 550, null, new int[]{14105}, "Uncle", "Uncle", -1));
        options.add(new Option(14103, 551, null, new int[]{14105}, "Aunt", "Aunt", -1));
        options.add(new Option(14103, 550, null, new int[]{14105}, "Grandparents", "Grandparents", -1));
        options.add(new Option(14103, 551, null, new int[]{14105}, "Guardian", "Guardian", -1));
        options.add(new Option(14103, 551, new int[]{14105}, null, "Other", "Other", -1));

        this.questions.add(new Question(true, 14, 14105, "F.3a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other relation", ParamNames.FORM1_OTHER_RELATION, alpha20DigitSpace));


        questions.add(new Question(true, 14, 14097, "G", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Health worker:", null, null));

        this.questions.add(new Question(true, 14, 14099, "G.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Name of health worker", ParamNames.FORM1_NAME_OF_COMMUNITY_WORKER, alpha50DigitSpace));

    }

    private void initForm2SafeCircumcision() {
        questions.add(new Question(false, 15, 15000, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Basic Information", null, null));
        this.questions.add(new Question(true, 15, 15016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Procedure Date", null, alpha160DigitSpace));

        // this.questions.add(new Question(true, 15, 15001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form date", FORM_DATE, dateTimeMinTodayMaxLastMonday));

        this.questions.add(new Question(true, 15, 15002, "H.1", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Genital Screening (Normal)?", ParamNames.FORM2_GENITAL_SCREENING, null));
        this.options.add(new Option(15002, 359, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        this.options.add(new Option(15002, 359, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        this.questions.add(new Question(true, 15, 15003, "H.2", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Anesthesia (Dorsal Penile Nerve Block) given?", ParamNames.FORM2_ANESTHESIA, null));
        this.options.add(new Option(15003, 359, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        this.options.add(new Option(15003, 359, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        this.questions.add(new Question(true, 15, 15004, "H.3", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Plastibell device size used", ParamNames.FORM2_PLASTIBELL, null));
        this.options.add(new Option(15004, 338, null, new int[]{15005}, "", "<Select an option>", -1));
        this.options.add(new Option(15004, 359, null, new int[]{15005}, ParamNames.FORM2_PLASTIBELL_1_POINT_1, "1.1", -1));
        this.options.add(new Option(15004, 359, null, new int[]{15005}, ParamNames.FORM2_PLASTIBELL_1_POINT_2, "1.2", -1));
        this.options.add(new Option(15004, 359, null, new int[]{15005}, ParamNames.FORM2_PLASTIBELL_1_POINT_3, "1.3", -1));
        this.options.add(new Option(15004, 359, null, new int[]{15005}, ParamNames.FORM2_PLASTIBELL_1_POINT_4, "1.4", -1));
        this.options.add(new Option(15004, 359, null, new int[]{15005}, ParamNames.FORM2_PLASTIBELL_1_POINT_5, "1.5", -1));
        this.options.add(new Option(15004, 359, null, new int[]{15005}, ParamNames.FORM2_PLASTIBELL_1_POINT_6, "1.6", -1));
        this.options.add(new Option(15004, 359, null, new int[]{15005}, ParamNames.FORM2_PLASTIBELL_1_POINT_7, "1.7", -1));
        this.options.add(new Option(15004, 359, new int[]{15005}, null, ParamNames.FORM2_PLASTIBELL_1_POINT_1, "Other", -1));

        this.questions.add(new Question(true, 15, 15005, "H.3a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of other", ParamNames.FORM2_OTHER_PLASTIBELL_DEVICE, alphaNumeric150DigitSpace));

        this.questions.add(new Question(true, 15, 15006, "H.4", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Were complications seen during or immediately after the procedure?", ParamNames.FORM2_COMPLICATIONS, null));
        this.options.add(new Option(15006, 359, new int[]{15007, 15009}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        this.options.add(new Option(15006, 359, null, new int[]{15007, 15009}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        this.questions.add(new Question(true, 15, 15007, "H.4a", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If yes, state which complications?", ParamNames.FORM2_WHICH_COMPLICATIONS, null));
        this.options.add(new Option(15007, 338, null, new int[]{15008}, "", "<Select an option>", -1));
        this.options.add(new Option(15007, 359, null, new int[]{15008}, ParamNames.FORM2_BLEEDING, "Bleeding", -1));
        this.options.add(new Option(15007, 359, null, new int[]{15008}, ParamNames.FORM2_SKIN_TEAR, "Skin tear", -1));
        this.options.add(new Option(15007, 359, null, new int[]{15008}, ParamNames.FORM2_ALERGIC_REACTION, "Allergic reaction", -1));
        this.options.add(new Option(15007, 359, new int[]{15008}, null, ParamNames.FORM2_OTHER, "Other", -1));

        this.questions.add(new Question(true, 15, 15008, "H.4b", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify Other", "d381f149-a87e-4da3-bbd9-a2dcb4982347", alphaNumeric150DigitSpace));

        this.questions.add(new Question(true, 15, 15009, "H.4c", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If yes, steps taken", ParamNames.FORM2_STEPS_TAKEN, null));
        this.options.add(new Option(15009, 338, null, new int[]{15010}, "", "<Select an option>", -1));
        this.options.add(new Option(15009, 359, null, new int[]{15010}, ParamNames.FORM2_PRESURE_WITH_GUAZE, "Direct pressure with gauze", -1));
        this.options.add(new Option(15009, 359, null, new int[]{15010}, ParamNames.FORM2_PRESURE_WITH_GUAZE_SPRAYED_WITH_ADRENALINE, "Direct pressure with gauze sprayed with adrenaline", -1));
        this.options.add(new Option(15009, 359, null, new int[]{15010}, ParamNames.FORM2_REFFER_TO_INDUS_ER, "Referred to Indus ER", -1));
        this.options.add(new Option(15009, 359, new int[]{15010}, null, ParamNames.FORM2_OTHER, "Other", -1));

        this.questions.add(new Question(true, 15, 15010, "H.4d", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Specify Other", "13e69fea-a79e-4d3a-b2b7-fb9fcc93db24", alphaNumeric150DigitSpace));

        this.questions.add(new Question(true, 15, 15012, "H.5", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "NAPA suppository 125mg given?", ParamNames.FORM2_NAPA_SUPPOSITORY_125MG, null));
        this.options.add(new Option(15012, 359, new int[]{15013, 15014, 15015, 1500001, 1501001, 1501002}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        this.options.add(new Option(15012, 359, null, new int[]{15013, 15014, 15015, 1500001, 1501001, 1501002}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        // questions.add(new Question(false, 15, 15013, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Post operative care: (To be filled by health provider)", null, null));

        this.questions.add(new Question(true, 15, 15014, "I.1", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Baby returned to parent/guardian in stable condition?", ParamNames.FORM2_BABY_RETURN_STABLE_CONDITION, null));
        this.options.add(new Option(15014, 359, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        this.options.add(new Option(15014, 359, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        this.questions.add(new Question(true, 15, 15015, "I.2", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Postoperative care instructions / helpline number provided to parent/guardian?", ParamNames.FORM2_POSTPERATIVE_INSTRUCTION_GIVEN_PARENT, null));
        this.options.add(new Option(15015, 359, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        this.options.add(new Option(15015, 359, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));


        questions.add(new Question(false, 15, 1500001, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.GONE, null, "Health Provider:", null, null));

        this.questions.add(new Question(true, 15, 1501001, "J.1", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Name of the health provider", ParamNames.FORM2_NAME_HEALTH_PROVIDER, alphaNumeric150DigitSpace));
        this.questions.add(new Question(true, 15, 1501002, "J.2", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Name of the health supervisor", ParamNames.FORM2_NAME_SUPERVISOR, alphaNumeric150DigitSpace));
    }

    private void initDay1CallSafeCircumcision() {
        int formId = 18;
        questions.add(new Question(false, formId, 16000, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Follow up documentation: Call on Day 1:", null, null));
        this.questions.add(new Question(true, formId, 11113, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Procedure Date", null, alpha160DigitSpace));

        this.questions.add(new Question(true, formId, 16001, "K.a", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Day 1 call", ParamNames.FORM_DATE, dateMinTodayMaxLastMonday));

        this.questions.add(new Question(true, formId, 16002, "K.1", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Contact established?", ParamNames.FORM3_CONTACT_ESTABLISHED, null));
        this.options.add(new Option(16002, 338, null, new int[]{16003, 16004, 16005, 16006, 16007, 16008}, "", "<Select an option>", -1));
        this.options.add(new Option(16002, 359, new int[]{16003, 16004, 16005, 16006, 16007, 16008, 16009}, null, ParamNames.FORM3_YES, "YES", -1));
        this.options.add(new Option(16002, 359, null, new int[]{16003, 16004, 16005, 16006, 16007, 16008}, ParamNames.FORM3_NO_TRIED_3TIMES_1SMS, "No  (Tried 3 times + 1 SMS)", -1));

        this.questions.add(new Question(true, formId, 16003, "K.2", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "General well being of baby", ParamNames.FORM3_GENERAL_WELL_OF_BEAING_BABY, null));
        this.options.add(new Option(16003, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(16003, 359, null, null, ParamNames.FORM3_COMFORTABLE_HEALTHY, "Comfortable and healthy", -1));
        this.options.add(new Option(16003, 359, null, null, ParamNames.FORM3_UNCOMFORTABLE, "Uncomfortable", -1));
        this.options.add(new Option(16003, 359, null, null, ParamNames.FORM3_INDISTRESS, "In distress and inconsolable", -1));

        this.questions.add(new Question(true, formId, 16004, "K.3", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Feeding", ParamNames.FORM3_FEEDING, null));
        this.options.add(new Option(16004, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(16004, 359, null, null, ParamNames.FORM3_WELL, "Well", -1));
        this.options.add(new Option(16004, 359, null, null, ParamNames.FORM3_LESS_THAN_NORMAL, "Less than normal", -1));
        this.options.add(new Option(16004, 359, null, null, ParamNames.FORM3_NOT_FEEDING_AT_ALL, "Not feeding at all", -1));

        this.questions.add(new Question(true, formId, 16005, "K.4", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Analgesia (No of doses)", ParamNames.FORM3_ANALGESIA, null));
        this.options.add(new Option(16005, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(16005, 359, null, null, ParamNames.FORM3_ONCE, "Once", -1));
        this.options.add(new Option(16005, 359, null, null, ParamNames.FORM3_2TIMES, "2 times", -1));
        this.options.add(new Option(16005, 359, null, null, ParamNames.FORM3_3TIMES, "3 times", -1));
        this.options.add(new Option(16005, 359, null, null, ParamNames.FORM3_4TIMES, "4 times", -1));
        this.options.add(new Option(16005, 359, null, null, ParamNames.FORM3_NOT_GIVEN, "Not given", -1));

        this.questions.add(new Question(true, formId, 16006, "K.5", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Bleeding", ParamNames.FORM3_BLEEDING, null));
        this.options.add(new Option(16006, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(16006, 359, null, null, ParamNames.FORM3_NO_BLEEDING, "No bleeding / 1-2 drops", -1));
        this.options.add(new Option(16006, 359, null, null, ParamNames.FORM3_CONTINUIOUS_BLEEDING, "Continuous / large amount of bleeding", -1));

        this.questions.add(new Question(true, formId, 16007, "K.6", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Has the baby passed urine after the procedure?", ParamNames.FORM3_BABY_PASSED_URINE_AFTER_PROCEDURE, null));
        this.options.add(new Option(16007, 359, null, null, ParamNames.FORM3_YES, "Yes", -1));
        this.options.add(new Option(16007, 359, null, null, ParamNames.FORM3_NO, "No", -1));

        this.questions.add(new Question(true, formId, 16008, "K.7", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Advice given", ParamNames.FORM3_ADVICE_GIVEN, null));
        this.options.add(new Option(16008, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(16008, 359, null, null, ParamNames.FORM3_REASSURANCE, "Reassurance", -1));
        this.options.add(new Option(16008, 359, null, null, ParamNames.FORM3_REFFER_TO_INDUS_ER, "Refer to Indus ER", -1));
        this.options.add(new Option(16008, 359, null, null, ParamNames.FORM3_NONE, "None", -1));
        this.options.add(new Option(16008, 359, null, null, ParamNames.FORM3_OTHER, "Other", -1));

        this.questions.add(new Question(false, formId, 16009, "K.7a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Other (Details of events that happened after circumcision and before Day 7 )", ParamNames.FORM3_OTHER_DETAIL_OF_EVENT_THAT_HAPPEN, alphaNumeric200DigitSpace));
    }


    private void initDay7CallSafeCircumcision() {
        int formId = 19;
        questions.add(new Question(false, formId, 16010, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Follow up documentation: Call on Day 7:", null, null));
        this.questions.add(new Question(true, formId, 11112, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Procedure Date", null, alpha160DigitSpace));

        this.questions.add(new Question(true, formId, 16011, "L.a", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Day 7 call", ParamNames.FORM3_DATE, dateMinTodayMaxLastMonday));

        this.questions.add(new Question(true, formId, 16012, "L.1", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Contact established?", ParamNames.FORM3_CONTACT_ESTABLISHED, null));
        this.options.add(new Option(16012, 338, null, new int[]{16013, 16017, 16021}, "", "<Select an option>", -1));
        this.options.add(new Option(16012, 359, new int[]{16013, 16017, 16021}, null, ParamNames.FORM3_YES, "YES", -1));
        this.options.add(new Option(16012, 359, null, new int[]{16013, 16017, 16021}, ParamNames.FORM3_NO_TRIED_3TIMES_1SMS, "No  (Tried 3 times + 1 SMS)", -1));

        this.questions.add(new Question(true, formId, 16013, "L.2", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Has the ring fallen?", ParamNames.FORM3_RING_FALLEN, null));
        this.options.add(new Option(16013, 338, null, new int[]{16018, 16019, 16020, 16014, 16015, 19035, 19041}, "", "<Select an option>", -1));
        this.options.add(new Option(16013, 359, new int[]{16018, 16019, 16020, 16014, 16015, 19035, 19041}, new int[]{16016}, ParamNames.FORM3_YES, "YES", -1));
        this.options.add(new Option(16013, 359, new int[]{16016, 19035, 19041}, new int[]{16018, 16019, 16020, 16014, 16015}, ParamNames.FORM3_NO, "No", -1));
        this.options.add(new Option(16013, 359, new int[]{16018, 16019, 16020, 19035, 19041}, new int[]{16014, 16015, 16016}, ParamNames.FORM3_CUT_N_REMOVED, "Cut and removed", -1));

        this.questions.add(new Question(true, formId, 16014, "L.2a", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_DATE_TIME, "If yes, give date", ParamNames.FORM3_DATE, dateMinTodayMaxLastMonday));

        this.questions.add(new Question(true, formId, 16015, "L.2b", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If yes, which day after circumcision did the ring fall?", ParamNames.FORM3_DAY, numeric2Digit));

        this.questions.add(new Question(true, formId, 16016, "L.2c", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "If no, is the ring stuck?", ParamNames.FORM3_RING_STUCK, null));
        this.options.add(new Option(16016, 359, null, null, ParamNames.FORM3_YES, "Yes", -1));
        this.options.add(new Option(16016, 359, null, null, ParamNames.FORM3_NO, "No", -1));

        this.questions.add(new Question(true, formId, 16017, "L.3", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Signs of infection (increasing redness or swelling, fever, pus)", ParamNames.FORM3_SIGN_OF_INFECTION, null));
        this.options.add(new Option(16017, 359, null, null, FORM3_ABSENT, "No, absent", -1));
        this.options.add(new Option(16017, 359, null, null, FORM3_PRESENT, "Yes, present", -1));

        this.questions.add(new Question(true, formId, 16018, "L.4", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Parent / guardian satisfied with circumcision wound healing", ParamNames.GUARDIAN_SATISFACTION, null));
        this.options.add(new Option(16018, 359, null, null, FORM3_SATISFIED, "Yes, satisfied", -1));
        this.options.add(new Option(16018, 359, null, null, FORM3_NOT_SATISFIED, "No, not satisfied", -1));

        this.questions.add(new Question(true, formId, 16019, "L.5", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Overall parent / guardian satisfaction with our program", ParamNames.FORM3_OVERALL_PARENT_SATISFIED, null));
        this.options.add(new Option(16019, 359, null, null, FORM3_SATISFIED, "Yes, satisfied", -1));
        this.options.add(new Option(16019, 359, null, null, FORM3_NOT_SATISFIED, "No, not satisfied", -1));

        this.questions.add(new Question(true, formId, 16020, "L.5a", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If satisfied, grade your satisfaction on a scale of 1-3", ParamNames.FORM3_SATISFACTION_SCALE, null));
        this.options.add(new Option(16020, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(16020, 359, null, null, ParamNames.FORM3_MILD, "Mild", -1));
        this.options.add(new Option(16020, 359, null, null, ParamNames.FORM3_MODERATE, "Moderate", -1));
        this.options.add(new Option(16020, 359, null, null, ParamNames.FORM3_EXTEREM, "Extreme", -1));

        this.questions.add(new Question(true, formId, 16021, "L.6", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Advice given", ParamNames.FORM3_ADVICE_GIVEN_DAY_7, null));
        this.options.add(new Option(16021, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(16021, 359, null, null, ParamNames.FORM3_RING_DOESNOT_FALL_DAY10, "If ring does not fall by Day 10, call our Helpline", -1));
        this.options.add(new Option(16021, 359, null, null, ParamNames.FORM3_REFFER_TO_INDUS_ER, "Refer to Indus ER", -1));
        this.options.add(new Option(16021, 359, null, null, ParamNames.FORM3_NONE, "None", -1));
        this.options.add(new Option(16021, 359, null, null, ParamNames.FORM3_OTHER, "Other", -1));
        this.options.add(new Option(16021, 359, null, null, ParamNames.FORM3_REFFER_TO_CS_CLINIC, "Refer to Circumcision Clinic", -1));
        this.options.add(new Option(16021, 359, null, null, ParamNames.FORM3_REFFER_TO_PAEDS_SURGERY_CLINIC, "Refer to Paeds Surgery Clinic- TIH", -1));
        this.options.add(new Option(16021, 359, null, null, ParamNames.FORM3_REFFER_TO_PAEDS_SURGERY_OTHER, "Refer to Paeds Surgery - Other", -1));

        this.questions.add(new Question(false, formId, 16022, "L.6a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Other (Details of events that happened on or after Day 7 and before Day 10))", ParamNames.FORM3_OTHER_DETAIL_OF_EVENT_THAT_HAPPEN_7, alphaNumeric160DigitSpace));

        ////////////////////////
        this.questions.add(new Question(true, formId, 19035, "M.7", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Complications / Outcome (Any complication or visit to CC clinic or ER for circumcision related issues)", ParamNames.COMPLICATION_OUTCOME, alphaNumeric300DigitSpace));

        this.questions.add(new Question(true, formId, 19036, "M.7a", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Is it a complication of circumcision?", ParamNames.FORM3_COMPLICATIONS_OF_CS, null));
        this.options.add(new Option(19036, 359, new int[]{19037}, new int[]{19039}, ParamNames.FORM3_YES, "Yes", -1));
        this.options.add(new Option(19036, 359, new int[]{19039}, new int[]{19037}, ParamNames.FORM3_NO, "No", -1));

        this.questions.add(new Question(true, formId, 19037, "M.7b", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Classify complications / Outcome", ParamNames.FORM3_CLASSIFY_COMPLICATIONS, null));
        this.options.add(new Option(19037, 338, null, new int[]{19038}, "", "<Select an option>", -1));
        this.options.add(new Option(19037, 359, null, new int[]{19038}, ParamNames.FORM3_POST_CIRC_BLEED, "Post circ bleed needing pressure and / or adrenaline dressing (minor)", -1));
        this.options.add(new Option(19037, 359, null, new int[]{19038}, ParamNames.FORM3_PLASTIBELL_NEEDED_TO_BE_CUT, "Plastibell needed to be cut and removed (minor)", -1));
        this.options.add(new Option(19037, 359, null, new int[]{19038}, ParamNames.FORM3_INFECTION, "Infection (major)", -1));
        this.options.add(new Option(19037, 359, null, new int[]{19038}, ParamNames.FORM3_SKIN_TEAR, "Skin tear (major)", -1));
        this.options.add(new Option(19037, 359, null, new int[]{19038}, ParamNames.FORM3_UNCONTROLLED_BLEEDING, "Uncontrolled bleeding requiring major intervention (major)", -1));
        this.options.add(new Option(19037, 359, null, new int[]{19038}, ParamNames.FORM3_INADEQUATE_SKIN_REMOVAL, "Inadequate skin removal (minor)", -1));
        this.options.add(new Option(19037, 359, null, new int[]{19038}, ParamNames.FORM3_EXCESSIVE_SKIN_REMOVAL, "Excessive skin removal (major)", -1));
        this.options.add(new Option(19037, 359, new int[]{19038}, null, ParamNames.FORM3_OTHER, "Other", -1));

        this.questions.add(new Question(true, formId, 19038, "M.7c", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of Other or Multiple problems", ParamNames.FORM3_OTHER, alphaNumeric160DigitSpace));

        this.questions.add(new Question(true, formId, 19039, "M.7f", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If No, select", ParamNames.FORM3_SELECT, null));
        this.options.add(new Option(19039, 338, null, new int[]{19040}, "", "<Select an option>", -1));
        this.options.add(new Option(19039, 359, null, new int[]{19040}, ParamNames.FORM3_ONLY_REASSURANCE, "Only reassurance / minor intervention required", -1));
        this.options.add(new Option(19039, 359, null, new int[]{19040}, ParamNames.FORM3_REQUIRES_REVIEW_IN_FUTURE, "Requires review in future", -1));
        this.options.add(new Option(19039, 359, null, new int[]{19040}, ParamNames.FORM3_DELAYED_SHEDDING_OF_RING, "Delayed shedding of ring", -1));
        this.options.add(new Option(19039, 359, null, new int[]{19040}, ParamNames.FORM3_EARLY_SHEDDING_OF_RING, "Early shedding of ring", -1));
        this.options.add(new Option(19039, 359, new int[]{19040}, null, ParamNames.FORM3_OTHER, "Other", -1));

        this.questions.add(new Question(true, formId, 19040, "M.7g", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of Other", ParamNames.FORM3_OTHER, alphaNumeric160DigitSpace));

        this.questions.add(new Question(true, formId, 19041, "M.8", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Ring cut and removed", ParamNames.FORM3_RING_CUT_AND_REMOVED, null));
        this.options.add(new Option(19041, 359, null, null, FORM3_YES, "Yes", -1));
        this.options.add(new Option(19041, 359, null, null, FORM3_NO, "No", -1));
    }

    private void initDay10CallSafeCircumcision() {
        int formId = 20;
        questions.add(new Question(false, formId, 160230, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Follow up documentation: Call on Day 10: (If ring did not fall by day 7)", null, null));

        this.questions.add(new Question(true, formId, 11111, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Procedure Date", null, alpha160DigitSpace));


        this.questions.add(new Question(true, formId, 16023, "M.a", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Day 10 call", ParamNames.FORM3_DATE, dateMinTodayMaxLastMonday));

        this.questions.add(new Question(true, formId, 16024, "M.1", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Contact established?", ParamNames.FORM3_CONTACT_ESTABLISHED, null));
        this.options.add(new Option(16024, 338, null, new int[]{16025, 16029, 16033, 16042}, "", "<Select an option>", -1));
        this.options.add(new Option(16024, 359, new int[]{16025, 16029, 16033, 16042}, null, ParamNames.FORM3_YES, "YES", -1));
        this.options.add(new Option(16024, 359, null, new int[]{16025, 16029, 16033, 16042}, ParamNames.FORM3_NO_TRIED_3TIMES_1SMS, "No  (Tried 3 times + 1 SMS)", -1));

        this.questions.add(new Question(true, formId, 16025, "M.2", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Has the ring fallen?", ParamNames.FORM3_CONTACT_ESTABLISHED, null));
        this.options.add(new Option(16025, 338, null, new int[]{16026, 16027, 16028, 16030, 16031, 16035, 16041}, "", "<Select an option>", -1));
        this.options.add(new Option(16025, 359, new int[]{16026, 16027, 16030, 16031, 16035, 16041}, new int[]{16028}, ParamNames.FORM3_YES, "YES", -1));
        this.options.add(new Option(16025, 359, new int[]{16028, 16035, 16041}, new int[]{16026, 16027, 16030, 16031}, ParamNames.FORM3_NO, "No", -1));
        this.options.add(new Option(16025, 359, new int[]{16030, 16031, 16035, 16041}, new int[]{16026, 16027, 16028}, ParamNames.FORM3_CUT_N_REMOVED, "Cut and removed", -1));

        this.questions.add(new Question(true, formId, 16026, "M.2a", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_DATE_TIME, "If yes, give date", ParamNames.DATE_RING_FALLEN, dateMinTodayMaxLastMonday));

        this.questions.add(new Question(true, formId, 16027, "M.2b", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "If yes, which day after circumcision did the ring fall?", ParamNames.DAY_RING_FALL, numeric2Digit));

        this.questions.add(new Question(true, formId, 16028, "M.2c", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "If no, is the ring stuck?", ParamNames.FORM3_RING_STUCK, null));
        this.options.add(new Option(16028, 359, null, null, ParamNames.FORM3_YES, "Yes", -1));
        this.options.add(new Option(16028, 359, null, null, ParamNames.FORM3_NO, "No", -1));

        this.questions.add(new Question(true, formId, 16029, "M.3", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Signs of infection (increasing redness or swelling, fever, pus)", ParamNames.FORM3_SIGN_OF_INFECTION, null));
        this.options.add(new Option(16029, 359, null, null, ParamNames.FORM3_ABSENT, "No, absent", -1));
        this.options.add(new Option(16029, 359, null, null, ParamNames.FORM3_PRESENT, "Yes, present", -1));

        this.questions.add(new Question(true, formId, 16030, "M.4", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Parent / guardian satisfied with circumcision wound healing", ParamNames.GUARDIAN_SATISFACTION, null));
        this.options.add(new Option(16030, 359, null, null, ParamNames.FORM3_SATISFIED, "Yes, satisfied", -1));
        this.options.add(new Option(16030, 359, null, null, ParamNames.FORM3_NOT_SATISFIED, "No, not satisfied", -1));

        this.questions.add(new Question(true, formId, 16031, "M.5", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Overall parent / guardian satisfaction with our program", ParamNames.FORM3_OVERALL_PARENT_SATISFIED, null));
        this.options.add(new Option(16031, 359, new int[]{16032}, null, ParamNames.FORM3_SATISFIED, "Yes, satisfied", -1));
        this.options.add(new Option(16031, 359, null, new int[]{16032}, ParamNames.FORM3_NOT_SATISFIED, "No, not satisfied", -1));

        this.questions.add(new Question(true, formId, 16032, "M.5a", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If satisfied, grade your satisfaction on a scale of 1-3", ParamNames.FORM3_SATISFACTION_SCALE, null));
        this.options.add(new Option(16032, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(16032, 359, null, null, ParamNames.FORM3_MILD, "Mild", -1));
        this.options.add(new Option(16032, 359, null, null, ParamNames.FORM3_MODERATE, "Moderate", -1));
        this.options.add(new Option(16032, 359, null, null, ParamNames.FORM3_EXTEREM, "Extreme", -1));

        this.questions.add(new Question(true, formId, 16033, "M.6", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Advice given", ParamNames.FORM3_ADVICE_GIVEN_DAY_10, null));
        this.options.add(new Option(16033, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(16033, 359, null, null, ParamNames.FORM3_REFFER_TO_INDUS_ER, "Refer to Indus ER", -1));
        this.options.add(new Option(16033, 359, null, null, ParamNames.FORM3_NONE, "None", -1));
        this.options.add(new Option(16033, 359, null, null, ParamNames.FORM3_OTHER, "Other", -1));
        this.options.add(new Option(16033, 359, null, null, ParamNames.FORM3_REFFER_TO_CS_CLINIC, "Refer to Circumcision Clinic", -1));
        this.options.add(new Option(16033, 359, null, null, ParamNames.FORM3_REFFER_TO_PAEDS_SURGERY_CLINIC, "Refer to Paeds Surgery Clinic- TIH", -1));
        this.options.add(new Option(16033, 359, null, null, ParamNames.FORM3_REFFER_TO_PAEDS_SURGERY_OTHER, "Refer to Paeds Surgery - Other", -1));

        this.questions.add(new Question(false, formId, 16034, "M.6a", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Other (Details of events that happened on or after Day 10)", ParamNames.FORM3_OTHER_DETAIL_OF_EVENT_THAT_HAPPEN_10, alphaNumeric160DigitSpace));


        /////day 10 ends here

        this.questions.add(new Question(true, formId, 16035, "M.7", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Complications / Outcome (Any complication or visit to CC clinic or ER for circumcision related issues)", ParamNames.COMPLICATION_OUTCOME, alphaNumeric300DigitSpace));

        this.questions.add(new Question(true, formId, 16036, "M.7a", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Is it a complication of circumcision?", ParamNames.FORM3_COMPLICATIONS_OF_CS, null));
        this.options.add(new Option(16036, 359, new int[]{16037}, new int[]{16039}, ParamNames.FORM3_YES, "Yes", -1));
        this.options.add(new Option(16036, 359, new int[]{16039}, new int[]{16037}, ParamNames.FORM3_NO, "No", -1));

        this.questions.add(new Question(true, formId, 16037, "M.7b", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Classify complications / Outcome", ParamNames.FORM3_CLASSIFY_COMPLICATIONS, null));
        this.options.add(new Option(16037, 338, null, new int[]{16038}, "", "<Select an option>", -1));
        this.options.add(new Option(16037, 359, null, new int[]{16038}, ParamNames.FORM3_POST_CIRC_BLEED, "Post circ bleed needing pressure and / or adrenaline dressing (minor)", -1));
        this.options.add(new Option(16037, 359, null, new int[]{16038}, ParamNames.FORM3_PLASTIBELL_NEEDED_TO_BE_CUT, "Plastibell needed to be cut and removed (minor)", -1));
        this.options.add(new Option(16037, 359, null, new int[]{16038}, ParamNames.FORM3_INFECTION, "Infection (major)", -1));
        this.options.add(new Option(16037, 359, null, new int[]{16038}, ParamNames.FORM3_SKIN_TEAR, "Skin tear (major)", -1));
        this.options.add(new Option(16037, 359, null, new int[]{16038}, ParamNames.FORM3_UNCONTROLLED_BLEEDING, "Uncontrolled bleeding requiring major intervention (major)", -1));
        this.options.add(new Option(16037, 359, null, new int[]{16038}, ParamNames.FORM3_INADEQUATE_SKIN_REMOVAL, "Inadequate skin removal (minor)", -1));
        this.options.add(new Option(16037, 359, null, new int[]{16038}, ParamNames.FORM3_EXCESSIVE_SKIN_REMOVAL, "Excessive skin removal (major)", -1));
        this.options.add(new Option(16037, 359, new int[]{16038}, null, ParamNames.FORM3_OTHER, "Other", -1));

        this.questions.add(new Question(true, formId, 16038, "M.7c", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of Other", ParamNames.FORM3_OTHER, alphaNumeric160DigitSpace));

        this.questions.add(new Question(true, formId, 16039, "M.7d", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "If No, select", ParamNames.FORM3_SELECT, null));
        this.options.add(new Option(16039, 338, null, new int[]{16040}, "", "<Select an option>", -1));
        this.options.add(new Option(16039, 359, null, new int[]{16040}, ParamNames.FORM3_ONLY_REASSURANCE, "Only reassurance / minor intervention required", -1));
        this.options.add(new Option(16039, 359, null, new int[]{16040}, ParamNames.FORM3_REQUIRES_REVIEW_IN_FUTURE, "Requires review in future", -1));
        this.options.add(new Option(16039, 359, null, new int[]{16040}, ParamNames.FORM3_DELAYED_SHEDDING_OF_RING, "Delayed shedding of ring", -1));
        this.options.add(new Option(16039, 359, null, new int[]{16040}, ParamNames.FORM3_EARLY_SHEDDING_OF_RING, "Early shedding of ring", -1));

        this.options.add(new Option(16039, 359, new int[]{16040}, null, ParamNames.FORM3_OTHER, "Other", -1));

        this.questions.add(new Question(true, formId, 16040, "M.7e", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Give details of Other", ParamNames.FORM3_OTHER, alphaNumeric160DigitSpace));

        this.questions.add(new Question(true, formId, 16041, "M.8", InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON, View.GONE, Validation.CHECK_FOR_EMPTY, "Ring cut and removed", ParamNames.FORM3_RING_CUT_AND_REMOVED, null));
        this.options.add(new Option(16041, 359, null, null, FORM3_YES, "Yes", -1));
        this.options.add(new Option(16041, 359, null, null, FORM3_NO, "No", -1));

        this.questions.add(new Question(false, formId, 16042, "N", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other shedding day", ParamNames.FORM3_OTHER_SHEDDING_DAY, alphaNumeric160DigitSpace));

    }

    //SSI FORMS
    private void initPreOperativeDemographics() {
        //Heading General Information
        questions.add(new Question(false, 1, 999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Pre-Operative Demographics", null, null));
        this.questions.add(new Question(true, 1, 91, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Form Date", FORM_DATE, dateMinTodayMaxLastMonday));
        this.options.add(new Option(91, 359, null, null, "", "Touch to Select Date", -1));
        // this.questions.add(new Question(true, 1, 92, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "User ID", USER_ID, alpha50DigitSpace));
        this.questions.add(new Question(true, 1, 93, "", InputWidget.InputWidgetsType.WIDGET_TYPE_AUTOCOMPLETE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "ICM Username", ICM_ID_NUMBER, alpha20DigitSpace));
        this.options.addAll(DynamicOptions.getProviderOptions(context, 93, null, null));
        //  this.options.add(new Option(93, 359, null, null, "", "Enter MR Number", -1));
        // this.questions.add(new Question(true, 1, 94, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "SSI Study Number", SSI_STUDY_NO, numeric3DigitMin1));
        //  this.options.add(new Option(94, 359, null, null, "", "Enter SSI Number", -1));
        this.questions.add(new Question(true, 1, 100, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Azdawaji hasiyat (Marital status)", PRE_OP_MARITAL_STATUS, null));
        this.options.add(new Option(100, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(100, 359, null, null, "5615AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Ghair shaadi shuda", -1));
        this.options.add(new Option(100, 359, null, null, "5555AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Shaadi shuda", -1));
        this.options.add(new Option(100, 359, null, null, "1059AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Bewa", -1));
        this.options.add(new Option(100, 359, null, null, "1058AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Talaaq yafta", -1));
        this.options.add(new Option(100, 359, null, null, "163007AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Alhaidgi", -1));

       /* this.questions.add(new Question(true, 1, 101, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Ghar ka patta - Ghar/Street #", ADDRESS, alpha150DigitSpace));
        // this.options.add(new Option(101, 359, null, null, "", "Ghar ka pata", -1));
        this.questions.add(new Question(true, 1, 102, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Ilaqa (Area)", AREA, alpha150DigitSpace));
        //  this.options.add(new Option(102, 359, null, null, "", "Ilaqa", -1));
        this.questions.add(new Question(true, 1, 103, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Sheher/ Gaaon", CITY_VILLAGE, alpha150DigitSpace));
        //   this.options.add(new Option(103, 359, null, null, "", "Sheher/ Gaaon", -1));*/
        this.questions.add(new Question(true, 1, 104, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Dehati ya shehri", PRE_OP_RURAL_URBAN, null));
        this.options.add(new Option(104, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(104, 359, null, null, "5ec39f45-ca7f-426d-a9a8-864dd7c08cad", "Dehati", -1));
        this.options.add(new Option(104, 359, null, null, "e969929c-b6dc-4fc1-b6e8-d2c2f27a6a3a", "Shehri", -1));
        this.questions.add(new Question(true, 1, 105, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Rabta number 1", PRE_OP_CONTACT1, numeric11Digit));
        //   this.options.add(new Option(105, 359, null, null, "", "Enter Phone Number", -1));
        this.questions.add(new Question(true, 1, 106, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Kiss ka number hai?", PRE_OP_CONTACT1_RELATIONSHIP, null));
        this.options.add(new Option(106, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(106, 359, null, null, "6f7df0d7-92b0-4947-8232-11812aa71560", "Khud ka", -1));
        this.options.add(new Option(106, 359, null, null, "b708c81d-b480-43ed-ae0a-27ddf0694d2f", "Rishtedar ka", -1));
        this.options.add(new Option(106, 359, null, null, "163502AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Parosi ka", -1));
        this.options.add(new Option(106, 359, null, null, "5618AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Dost ka", -1));
        this.questions.add(new Question(false, 1, 107, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Rabta number 2", PRE_OP_CONTACT2, numeric11Digit));
        options.add(new RangeOption(107, 359, null, new int[]{108}, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.IS_EMPTY, -1)));
        //   this.options.add(new Option(107, 359, null, null, "", "Enter Phone Number", -1));
        this.questions.add(new Question(true, 1, 108, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Kiss ka number hai?", PRE_OP_CONTACT2_RELATIONSHIP, null));
        this.options.add(new Option(108, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(108, 359, null, null, "6f7df0d7-92b0-4947-8232-11812aa71560", "Khud ka", -1));
        this.options.add(new Option(108, 359, null, null, "b708c81d-b480-43ed-ae0a-27ddf0694d2f", "Rishtedar ka", -1));
        this.options.add(new Option(108, 359, null, null, "163502AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Parosi ka", -1));
        this.options.add(new Option(108, 359, null, null, "5618AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Dost ka", -1));
        this.questions.add(new Question(true, 1, 109, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Kya aapney kabhi school mein parhai ki hai?", PRE_OP_ATTENDED_SCHOOL, null));
        this.options.add(new Option(109, 338, null, new int[]{110, 111}, "", "<Select an option>", -1));
        this.options.add(new Option(109, 359, new int[]{110}, new int[]{111}, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        this.options.add(new Option(109, 359, new int[]{111}, new int[]{110}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        this.questions.add(new Question(false, 1, 110, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Agar haan, to school mein kitni taleem haasil ki", PRE_OP_FORMAL_SCHOOLING_YEARS, numeric2Digit));
        //  this.options.add(new Option(110, 359, null, null, "", "", -1));
        this.questions.add(new Question(false, 1, 111, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Agar Nahi, to koi ghair rasmi taleem haasil ki?", PRE_OP_INFORMAL_SCHOOLING, null));
        this.options.add(new Option(111, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(111, 359, null, null, "1175AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Not Applicabe", -1));
        this.options.add(new Option(111, 359, null, null, "3124348d-45ed-41f8-b9c0-b277ae12aa0f", "Madrassa", -1));
        this.options.add(new Option(111, 359, null, null, "2db3b316-1349-4791-9b13-837e65b7980d", "Adult literacy", -1));
        this.options.add(new Option(111, 359, null, null, "1109d187-f642-428e-bd5a-b83c97e99c04", "Ghar par taleem hasil ki", -1));
        this.options.add(new Option(111, 359, null, null, "e24b00d8-485f-4572-abde-9811d8bd5869", "Khud se parhna seekha", -1));
        this.options.add(new Option(111, 359, null, null, "ab6ee439-8b57-45d1-8e10-8bcd9a9e4a2e", "Khud se parhna aur likhna seekha", -1));
        this.questions.add(new Question(true, 1, 112, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Kya aap parh likh saktey hein?", PRE_OP_CAN_READ_WRITE, null));

        this.options.add(new Option(112, 338, null, new int[]{113, 139, 115, 140}, "", "<Select an option>", -1));
        this.options.add(new Option(112, 359, new int[]{139}, new int[]{113, 115, 140}, "159400AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Parh", -1));
        this.options.add(new Option(112, 359, new int[]{139}, new int[]{113, 115, 140}, "159401AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Likh", -1));
        this.options.add(new Option(112, 359, new int[]{139}, new int[]{113, 115, 140}, "9bea9937-1c95-433e-b4d0-5d4ae4cc8366", "Dono", -1));
        this.options.add(new Option(112, 359, new int[]{113, 115, 140}, new int[]{139}, "fb71ac28-2b6e-401d-b3f1-5ff4cb2467c1", "Nahi", -1));
        this.questions.add(new Question(true, 1, 139, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Agar haan, to aap ke liye kis zubaan mein maaloomati brochure (jo aap ko diya jai ga) parh ke samajhna aasan hoga?", ParamNames.PRE_OP_LIERACY_COMFORT, null));
        this.options.add(new Option(139, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(139, 359, null, null, "c20201d0-826a-4ecf-96fa-407e3e2f54fc", "Urdu", -1));
        this.options.add(new Option(139, 359, null, null, "163215AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Punjabi", -1));
        this.options.add(new Option(139, 359, null, null, "34e0c006-0379-4870-98b2-2e5de971e713", "Sindhi", -1));
        this.options.add(new Option(139, 359, null, null, "cfdb049e-b4f0-40d6-91d6-5cf72cfadbe1", "Pushto", -1));
        this.options.add(new Option(139, 359, null, null, "e443f97b-a24c-4aac-a352-6fdc52103a1b", "Siraiki", -1));
        this.options.add(new Option(139, 359, null, null, "3574e507-5b17-4d1f-acd9-405d10f49487", "Balochi", -1));
        this.options.add(new Option(139, 359, null, null, "55baf400-6fb4-4a23-97d8-e90bbc500d93", "Bengali", -1));
        this.options.add(new Option(139, 359, null, null, "1a07c6e7-f00e-4f01-a7bc-5fde26b4a0fa", "Brohi", -1));
        this.options.add(new Option(139, 359, null, null, "144f0a84-1956-4dac-827a-1dcc50302838", "Memoni", -1));
        this.options.add(new Option(139, 359, null, null, "4b3413e9-b7e0-481a-8c90-3265d04cf9ac", "Hindko", -1));
        this.options.add(new Option(139, 359, null, null, "3b545e42-f6eb-4ec2-8ef0-b1b4f90f0f76", "Kashmiri", -1));
        this.options.add(new Option(139, 359, null, null, "4033fa11-dcfe-4f24-855b-35ad18048336", "Pahari", -1));
        this.options.add(new Option(139, 359, null, null, "ba7514f3-a134-4946-906c-5cf6e10b91e7", "Pothwari", -1));
        this.options.add(new Option(139, 359, null, null, "ef01f53a-8280-40db-94b4-8f3affe7d701", "Marwari", -1));
        this.options.add(new Option(139, 359, null, null, "91b98da1-a1c9-4f70-9f87-06d27e354a0c", "Kathiawari", -1));
        this.options.add(new Option(139, 359, null, null, "b4310016-116d-4a82-82b4-c1eb22788a8d", "Faarsi", -1));
        this.options.add(new Option(139, 359, null, null, "e8a8b8fd-3bba-40f2-8083-fc1b1a8cfda1", "Angrezi", -1));
        this.options.add(new Option(139, 359, null, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Koi Aur", -1));
        this.options.add(new Option(139, 359, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata Nahi", -1));
        this.questions.add(new Question(false, 1, 113, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Agar nahi, ya agar aap kam umar mareez hein, to aap ko brochure samjhnay mein kon madad kare ga?", PRE_OP_WHO_ASSIST, null));
        this.options.add(new Option(113, 338, null, new int[]{114}, "", "<Select an option>", -1));
        this.options.add(new Option(113, 359, null, new int[]{114}, "1527AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Waledein", -1));
        this.options.add(new Option(113, 359, null, new int[]{114}, "5620AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Koi aur rishtedar", -1));
        this.options.add(new Option(113, 359, null, new int[]{114}, "163502AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Parosi", -1));
        this.options.add(new Option(113, 359, null, new int[]{114}, "5618AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Dost", -1));
        this.options.add(new Option(113, 359, new int[]{114}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Koi aur", -1));
        this.questions.add(new Question(false, 1, 114, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Agar koi aur maded karey ga, to wazai karey", PRE_OP_WHO_ASSIST_OTHER, alpha60DigitSpace));
        //  this.options.add(new Option(114, 359, null, null, "", "", -1));
        this.questions.add(new Question(false, 1, 115, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Madad kerney waley ney kitney saal school mein parhai ki?", PRE_OP_ASSIST_YEARS_SCHOOLING, numeric3DigitMin1));
        //  this.options.add(new Option(115, 359, null, null, "", "", -1));
        this.questions.add(new Question(false, 1, 140, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Agar ghair rasmi taleem hai to wazahat karein", ParamNames.PRE_OP_ASSISTANT_EDUCATION_OTHER, alpha60DigitSpace));
        this.questions.add(new Question(true, 1, 116, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Aap ki madari zaban kya hai? ", PRE_OP_PRIMARY_LANGUAGE, null));
        this.options.add(new Option(116, 338, null, new int[]{117}, "", "<Select an option>", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "c20201d0-826a-4ecf-96fa-407e3e2f54fc", "Urdu", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "163215AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Punjabi", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "34e0c006-0379-4870-98b2-2e5de971e713", "Sindhi", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "cfdb049e-b4f0-40d6-91d6-5cf72cfadbe1", "Pushto", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "e443f97b-a24c-4aac-a352-6fdc52103a1b", "Siraiki", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "3574e507-5b17-4d1f-acd9-405d10f49487", "Balochi", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "55baf400-6fb4-4a23-97d8-e90bbc500d93", "Bengali", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "1a07c6e7-f00e-4f01-a7bc-5fde26b4a0fa", "Brohi", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "144f0a84-1956-4dac-827a-1dcc50302838", "Memoni", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "4b3413e9-b7e0-481a-8c90-3265d04cf9ac", "Hindko", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "3b545e42-f6eb-4ec2-8ef0-b1b4f90f0f76", "Kashmiri", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "4033fa11-dcfe-4f24-855b-35ad18048336", "Pahari", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "ba7514f3-a134-4946-906c-5cf6e10b91e7", "Pothwari", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "ef01f53a-8280-40db-94b4-8f3affe7d701", "Marwari", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "91b98da1-a1c9-4f70-9f87-06d27e354a0c", "Kathiawari", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "b4310016-116d-4a82-82b4-c1eb22788a8d", "Faarsi", -1));
        this.options.add(new Option(116, 359, null, null, "e8a8b8fd-3bba-40f2-8083-fc1b1a8cfda1", "Angrezi", -1));
        this.options.add(new Option(116, 359, new int[]{117}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Koi Aur", -1));
        this.options.add(new Option(116, 359, null, new int[]{117}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata Nahi", -1));
        this.questions.add(new Question(false, 1, 117, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Aap ki madari zaban kya hai? (Koi aur)", PRE_OP_PRIMARY_LANGUAGE_OTHER, alpha60DigitSpace));

        this.questions.add(new Question(true, 1, 132, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Kya aap key pass apna mobile phone hai?", PRE_OP_HAVE_MOBILE, null));
        this.options.add(new Option(132, 338, null, new int[]{133}, "", "<Select an option>", -1));
        this.options.add(new Option(132, 359, null, new int[]{133}, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        this.options.add(new Option(132, 359, new int[]{133}, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        this.questions.add(new Question(false, 1, 133, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Agar nahi hai, to kya aap ke pass aisa koi phone hai jis per aap se aasani se rabta ho sakta hai?", PRE_OP_HAVE_MOBILE_ACCESS, null));
        this.options.add(new Option(133, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(133, 359, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        this.options.add(new Option(133, 359, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        this.questions.add(new Question(true, 1, 134, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Kya aap mobile phone se sms ker saktey hein?", PRE_OP_SMS, null));
        this.options.add(new Option(134, 338, null, new int[]{141}, "", "<Select an option>", -1));
        this.options.add(new Option(134, 359, new int[]{141}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        this.options.add(new Option(134, 359, null, new int[]{141}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        this.questions.add(new Question(true, 1, 141, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Agar haan, to aap amooman kis zaban mein sms parh or likh saktay hein", ParamNames.SMS_LANGUAGE, null));
        this.options.add(new Option(141, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(141, 359, null, null, "c20201d0-826a-4ecf-96fa-407e3e2f54fc", "Urdu", -1));
        this.options.add(new Option(141, 359, null, null, "163215AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Punjabi", -1));
        this.options.add(new Option(141, 359, null, null, "34e0c006-0379-4870-98b2-2e5de971e713", "Sindhi", -1));
        this.options.add(new Option(141, 359, null, null, "cfdb049e-b4f0-40d6-91d6-5cf72cfadbe1", "Pushto", -1));
        this.options.add(new Option(141, 359, null, null, "e443f97b-a24c-4aac-a352-6fdc52103a1b", "Siraiki", -1));
        this.options.add(new Option(141, 359, null, null, "3574e507-5b17-4d1f-acd9-405d10f49487", "Balochi", -1));
        this.options.add(new Option(141, 359, null, null, "55baf400-6fb4-4a23-97d8-e90bbc500d93", "Bengali", -1));
        this.options.add(new Option(141, 359, null, null, "1a07c6e7-f00e-4f01-a7bc-5fde26b4a0fa", "Brohi", -1));
        this.options.add(new Option(141, 359, null, null, "144f0a84-1956-4dac-827a-1dcc50302838", "Memoni", -1));
        this.options.add(new Option(141, 359, null, null, "4b3413e9-b7e0-481a-8c90-3265d04cf9ac", "Hindko", -1));
        this.options.add(new Option(141, 359, null, null, "3b545e42-f6eb-4ec2-8ef0-b1b4f90f0f76", "Kashmiri", -1));
        this.options.add(new Option(141, 359, null, null, "4033fa11-dcfe-4f24-855b-35ad18048336", "Pahari", -1));
        this.options.add(new Option(141, 359, null, null, "ba7514f3-a134-4946-906c-5cf6e10b91e7", "Pothwari", -1));
        this.options.add(new Option(141, 359, null, null, "ef01f53a-8280-40db-94b4-8f3affe7d701", "Marwari", -1));
        this.options.add(new Option(141, 359, null, null, "91b98da1-a1c9-4f70-9f87-06d27e354a0c", "Kathiawari", -1));
        this.options.add(new Option(141, 359, null, null, "b4310016-116d-4a82-82b4-c1eb22788a8d", "Faarsi", -1));
        this.options.add(new Option(141, 359, null, null, "e8a8b8fd-3bba-40f2-8083-fc1b1a8cfda1", "Angrezi", -1));
        this.options.add(new Option(141, 359, null, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Koi Aur", -1));
        this.options.add(new Option(141, 359, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata Nahi", -1));
        this.questions.add(new Question(true, 1, 135, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Kya aapke phone mein camera hai?", PRE_OP_MOBILE_CAMERA, null));
        this.options.add(new Option(135, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(135, 359, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        this.options.add(new Option(135, 359, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));

        this.questions.add(new Question(true, 1, 136, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Aap kitney arsey sey mobile phones istamal kar rahey hai?", PRE_OP_MOBILE_USAGE_DURATION, null));
        this.options.add(new Option(136, 359, null, null, "1734AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Saal", -1));
        this.options.add(new Option(136, 359, null, null, "1074AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Mahiney", -1));
        this.options.add(new Option(136, 359, null, null, "1073AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haftey", -1));
        this.options.add(new Option(136, 359, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "N/A", -1));

        this.questions.add(new Question(true, 1, 138, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Kya aap mobile phone per internet istamal kartey hai?", PRE_OP_MOBILE_INTERNET, null));
        this.options.add(new Option(138, 338, null, null, "", "<Select an option>", -1));
        this.options.add(new Option(138, 359, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        this.options.add(new Option(138, 359, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
    }

    private void initPostOpDemographics() {
        Integer postOpDemographicId = 2;
        //Heading General Information
        questions.add(new Question(false, postOpDemographicId, 3999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Post-Operative Questionnaire", null, null));
        //Date of Procedure
        questions.add(new Question(true, postOpDemographicId, 3000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME,
                "Date of Procedure", ParamNames.POST_OP_DATE_SURGERY, dateMinTodayMaxLastMonday));
        this.options.add(new Option(3000, 359, null, null, "", "Touch to Select Date", -1));
        //Surgery Related Questions
        // questions.add(new Question(false, postOpDemographicId, 3998, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Surgery Questions", null, null));
        //Name of Procedure
        this.questions.add(new Question(true, postOpDemographicId, 3001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_AUTOCOMPLETE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Name of Procedure", ParamNames.POST_OP_NAME_PROCEDURE, alpha150DigitAll));
        this.options.addAll(DynamicOptions.getProcedureOptions(context, 3001, null, null));
        //Category of Procedure
        questions.add(new Question(true, postOpDemographicId, 3002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Category of Procedure", ParamNames.POST_OP_CAT_SURGERY, null));
        options.add(new Option(3002, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(3002, 300, null, null, "c056b0fc-2c7a-410b-9288-3f9ce823f76a", "Orthopedics", -1));
        options.add(new Option(3002, 301, null, null, "9691a342-8dcd-444c-bd51-305357824510", "General Surgery", -1));
        options.add(new Option(3002, 301, null, null, "ac0ccdbf-2834-4f32-b8f4-d21fb8a32052", "Urology", -1));
        options.add(new Option(3002, 302, null, null, "638dc267-64a6-4716-b3b5-cad123f2df3a", "Gynaecology and Obstetrics", -1));
        options.add(new Option(3002, 303, null, null, "93c85a70-08c0-4bed-8e5b-3e787aa2ea48", "Thoracic", -1));
        options.add(new Option(3002, 304, null, null, "5b1cf78e-a96c-4dfd-b110-cb871a40c586", "Vascular", -1));
        options.add(new Option(3002, 300, null, null, "723df495-5ddb-4a85-a07d-e9e02bd4cc79", "Trauma", -1));
        options.add(new Option(3002, 301, null, null, "bf1501eb-1058-4abd-b966-75af04441c38", "Pediatrics", -1));
        options.add(new Option(3002, 302, null, null, "2f8a97be-59c1-4c7e-ad61-3bcd27da87d6", "Oncology", -1));
        options.add(new Option(3002, 303, null, null, "ea0d47b7-f0f9-4979-ad99-b5981dcb67ba", "ENT/Head and Neck", -1));
        options.add(new Option(3002, 304, null, null, "5cd31d7f-110a-410e-ba9f-f8c774359ef1", "Neurosurgery", -1));
        options.add(new Option(3002, 304, null, null, "161252AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Dental", -1));
        options.add(new Option(3002, 304, null, null, "85776cd4-295c-4fcb-8aae-16b4e6d1d3fb", "Other", -1));

        //Surgery Performed
        questions.add(new Question(true, postOpDemographicId, 3006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Surgery Performed", ParamNames.POST_OP_HAS_SURGERY_PERFORMED, null));
        options.add(new Option(3006, 338, null, new int[]{3007}, "", "<Select an option>", -1));
        options.add(new Option(3006, 323, null, new int[]{3007}, "e712e5ef-2371-4dec-bd43-bd1d8106121b", "Elective", -1));
        options.add(new Option(3006, 323, null, new int[]{3007}, "aaf66fd2-fc4d-418a-ad51-197d7bd65512", "Emergency ", -1));
        options.add(new Option(3006, 324, new int[]{3007}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other", -1));
        // Surgery Performed (Other)
        this.questions.add(new Question(false, postOpDemographicId, 3007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Surgery Performed(Other)", POST_OP_SURGERY_PERFORMED_OTHER, alpha60DigitSpace));
        this.options.add(new Option(3007, 359, null, null, "", "", -1));
        //Surgery Classification
        questions.add(new Question(false, postOpDemographicId, 3008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Surgery Classification", ParamNames.POST_OP_SURGERY_CLASSFICATION, null));
        options.add(new Option(3008, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(3008, 325, null, null, "d6152d59-efed-44ed-afcb-3ef4d3bb30c9", "Clean", -1));
        options.add(new Option(3008, 326, null, null, "972ce109-6c83-403c-8601-e9f1c809f921", "Clean-Contaminated", -1));
        options.add(new Option(3008, 327, null, null, "ded9e53c-218b-4f6d-8e3a-af09c92cb11d", "Contaminated", -1));
        options.add(new Option(3008, 328, null, null, "c927ca56-420e-4684-94da-953c4fdb96ab", "Dirty", -1));
        //Type of Anesthesia
        questions.add(new Question(true, postOpDemographicId, 3009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Type of Anesthesia", ParamNames.POST_OP_ANESTHESIA_TYPE, null));
        options.add(new Option(3009, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(3009, 325, null, null, "a03617b6-04a5-47cb-b48f-ababbee4b1f4", "General with ETT", -1));
        options.add(new Option(3009, 326, null, null, "159b54da-b6ad-456c-9e70-1eff12d2be95", "General without ETT", -1));
        options.add(new Option(3009, 327, null, null, "161914AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Local", -1));
        options.add(new Option(3009, 328, null, null, "505e0d6b-18ce-42ff-89f7-1f0b86e2d191", "Regional", -1));
        options.add(new Option(3009, 327, null, null, "debb8123-9595-437e-bb6c-818a88fca7ab", "Neuraxial (spinal/epidural)", -1));
        options.add(new Option(3009, 328, null, null, "85200077-6212-4405-a49f-00a06c42f093", "Monitored Anesthesia Care (Sedation)", -1));
        //Antibiotic Prophylaxis
        questions.add(new Question(true, postOpDemographicId, 3010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Antibiotic Prophylaxis given within 2hrs before incision", ParamNames.POST_OP_ANTIBIOTICS_PROPHYLAXIS, null));
        options.add(new Option(3010, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(3010, 208, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(3010, 209, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        //ASA Score
        questions.add(new Question(false, postOpDemographicId, 3011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "ASA Score: ASA (Functional status score)", ParamNames.POST_OP_ASA_SCORE, null));
        options.add(new Option(3011, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(3011, 325, null, null, "c02c9d30-6b23-41b6-bc41-0bb61a352856", "1", -1));
        options.add(new Option(3011, 326, null, null, "ad71cfb1-7c50-4bbb-a992-c117edee9997", "2", -1));
        options.add(new Option(3011, 327, null, null, "5e019e04-74a5-4954-81a7-a3feab76f17e", "3", -1));
        options.add(new Option(3011, 328, null, null, "416e910d-6156-4a19-b55b-a9cf5ce7783a", "4", -1));
        options.add(new Option(3011, 327, null, null, "439e2cf8-e5e9-4415-aa84-311862e91f72", "5", -1));
        options.add(new Option(3011, 328, null, null, "0023a456-e2d6-4124-9d8b-f6defb725654", "6", -1));
        //Duration of Surgery
        questions.add(new Question(false, postOpDemographicId, 3012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Duration of Surgery", ParamNames.POST_OP_DURATION_SURGERY, null));
        options.add(new Option(3012, 338, null, new int[]{3013}, "", "<Select an option>", -1));
        options.add(new Option(3012, 325, null, new int[]{3013}, "4b1363da-ee4f-46c8-bdc8-9ca86c0a7cb4", "< 60 mins", -1));
        options.add(new Option(3012, 326, null, new int[]{3013}, "e8dd8fc2-008b-4556-8fc8-b9439994983a", "60-120 mins", -1));
        options.add(new Option(3012, 327, null, new int[]{3013}, "0ee55aff-fda6-469f-b1bf-1fbe543e916e", "120-180 mins", -1));
        options.add(new Option(3012, 328, null, new int[]{3013}, "523ccf75-f531-4771-9100-55c6c4a6b9d7", "180-360 mins", -1));
        options.add(new Option(3012, 327, new int[]{3013}, null, "ed4cd314-ce4a-4799-ae49-fcc5ebaf6a00", "Other", -1));
        //options.add(new Option(3011, 328, null, null, "", "6", -1));
        // Duration of Surgery (Other)
        this.questions.add(new Question(false, postOpDemographicId, 3013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Duration of Surgery (Other)", ParamNames.POST_OP_DURATION_OTHER, alphaNumeric60DigitSpace));
        //Type of Incisional closure
        questions.add(new Question(false, postOpDemographicId, 3014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Type of Incisional closure", ParamNames.POST_OP_INCISION_TYPE, null));
        options.add(new Option(3014, 338, null, new int[]{3015}, "", "<Select an option>", -1));
        options.add(new Option(3014, 325, null, new int[]{3015}, "bcea6bd5-e73e-4165-8605-f0b9311c96f8", "Running Suture", -1));
        options.add(new Option(3014, 326, null, new int[]{3015}, "6bb79da3-7c0f-441f-a30a-3e58d2b8c03e", "Interrupted Suture", -1));
        options.add(new Option(3014, 327, null, new int[]{3015}, "da9b964b-1cc1-47a1-854c-84113490fe61", "Staples", -1));
        options.add(new Option(3014, 328, null, new int[]{3015}, "320cde84-1c3e-44d4-bf2c-13ad0ff436d8", "Steri Strips", -1));
        options.add(new Option(3014, 327, new int[]{3015}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other", -1));
        // Type of Incisional closure (Other)
        this.questions.add(new Question(false, postOpDemographicId, 3015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Type of Incisional closure (Other)", ParamNames.POST_OP_INCISION_OTHER, alpha60DigitSpace));
    }

    private void initScreeningCall() {
        Integer screeningCallId = 3;
        //Heading General Information
        questions.add(new Question(false, screeningCallId, 999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Screening Call-in", null,
                null));
        //Date Called
        questions.add(new Question(true, screeningCallId, 2003, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Date/Time patient called", ParamNames.SCREENING_DATE_CALLED, dateTimeMinTodayMaxLastMonday));
        //Date Of Procedure
        questions.add(new Question(true, screeningCallId, 2028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Date of Procedure", ParamNames.SCREENING_DOP, dateMinTodayMaxLastMonday));
        //Days since surgery
        questions.add(new Question(true, screeningCallId, 2029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Days since surgery", ParamNames.SCREENING_DAYS_SURGERY, numeric3DigitMin1));
        //Appointment Doctor
        questions.add(new Question(true, screeningCallId, 2027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Appointment with Doctor", ParamNames.SCREENING_APPOINTMENT_DOCTOR, alpha20DigitSpace));
        //Appointment Date & Time
        questions.add(new Question(true, screeningCallId, 2004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Appointment Date/Time", ParamNames.SCREENING_APPOINTMENT_DATE, dateMinTodayMaxNextYearTime));

        //Screening Questions
        //  questions.add(new Question(false, screeningCallId, 2999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Screening Questions", null,null));
        //Patient Infected
        questions.add(new Question(true, screeningCallId, 2006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Is baat ki Tasdeek karein ke mareez ne infection ka khadsha hone ki wajah se phone kiya", ParamNames.SCREENING_PATIENT_REASON_CALL, null));
        options.add(new Option(2006, 338, null, new int[]{2007, 2009, 2011, 2023, 2013, 2015, 2017, 2033, 2021, 2002, 2025}, "", "<Select an option>", -1));
        options.add(new Option(2006, 200, new int[]{2007, 2009, 2011, 2023, 2013, 2015, 2017, 2033, 2021, 2002}, new int[]{2025}, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2006, 201, new int[]{2025}, new int[]{2007, 2009, 2011, 2023, 2013, 2015, 2017, 2033, 2021, 2002}, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Koi aur wajah", -1));
        questions.add(new Question(false, screeningCallId, 2025, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar koi aur wajah hai tou wazey karein", ParamNames.SCREENING_PATIENT_REASON_OTHER, alpha60DigitSpace));
        options.add(new Option(2025, 200, null, null, "", "", -1));
        //Colored Drainage
        questions.add(new Question(true, screeningCallId, 2007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga per pus (peeli, hari ya safaid) hona", ParamNames.SCREENING_COLORED_DRAINAGE, null));
        options.add(new Option(2007, 338, null, new int[]{2008}, "", "<Select an option>", -1));
        options.add(new Option(2007, 202, new int[]{2008}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2007, 203, null, new int[]{2008}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(2007, 204, null, new int[]{2008}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Colored Drainage
        questions.add(new Question(false, screeningCallId, 2008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_COLORED_DRAINAGE_TIME, numeric2Digit));
        //Increasing Swellness
        questions.add(new Question(true, screeningCallId, 2009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga per soojan hai", ParamNames.SCREENING_INCREASING_SWELL, null));
        options.add(new Option(2009, 338, null, new int[]{2010}, "", "<Select an option>", -1));
        options.add(new Option(2009, 205, new int[]{2010}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2009, 206, null, new int[]{2010}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(2009, 207, null, new int[]{2010}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Increasing Swellness
        questions.add(new Question(false, screeningCallId, 2010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCREASING_SWELL_TIME, numeric2Digit));
        //Increasing Fever
        questions.add(new Question(true, screeningCallId, 2011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ke 24 ghantay ke baad Bukhar hona", ParamNames.SCREENING_INCREASING_FEVER, null));
        options.add(new Option(2011, 338, null, new int[]{2012, 2030, 2031}, "", "<Select an option>", -1));
        options.add(new Option(2011, 208, new int[]{2012, 2030, 2031}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2011, 209, null, new int[]{2012, 2030, 2031}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(2011, 210, null, new int[]{2012, 2030, 2031}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Fever
        questions.add(new Question(false, screeningCallId, 2012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCREASING_FEVER_TIME, numeric2Digit));
        //Agar haan, tou kitna
        questions.add(new Question(false, screeningCallId, 2030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitna?", ParamNames.SCREENING_FEVER_INTENSITY, null));
        options.add(new Option(2030, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(2030, 208, null, null, "1408AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Tez", -1));
        options.add(new Option(2030, 209, null, null, "1407AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Halka", -1));
        //Aap ne check kiya?
        questions.add(new Question(false, screeningCallId, 2031, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Aap ne check kiya?", ParamNames.SCREENING_FEVER_MEASURE, null));
        options.add(new Option(2031, 338, null, new int[]{2032}, "", "<Select an option>", -1));
        options.add(new Option(2031, 208, new int[]{2032}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2031, 209, null, new int[]{2032}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        //Fever Reading
        questions.add(new Question(false, screeningCallId, 2032, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Kitna tez bukhar?", ParamNames.SCREENING_FEVER_READING, numeric3DigitMin1));
        //Incision Edge Separation
        questions.add(new Question(true, screeningCallId, 2023, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga key kinarey alag hona", ParamNames.SCREENING_INCISION_EDGE, null));
        options.add(new Option(2023, 338, null, new int[]{2024}, "", "<Select an option>", -1));
        options.add(new Option(2023, 208, new int[]{2024}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2023, 209, null, new int[]{2024}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(2023, 210, null, new int[]{2024}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Edge Separation
        questions.add(new Question(false, screeningCallId, 2024, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCISION_EDGE_TIME, numeric2Digit));
        //Increasing Redness
        questions.add(new Question(true, screeningCallId, 2013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga per surkhi hona", ParamNames.SCREENING_INCREASING_REDNESS, null));
        options.add(new Option(2013, 338, null, new int[]{2014}, "", "<Select an option>", -1));
        options.add(new Option(2013, 211, new int[]{2014}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2013, 212, null, new int[]{2014}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(2013, 213, null, new int[]{2014}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Redness
        questions.add(new Question(false, screeningCallId, 2014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCREASING_REDNESS_TIME, numeric2Digit));
        //Increasing Warmth
        questions.add(new Question(true, screeningCallId, 2015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga per garmahat  mehsoos hona", ParamNames.SCREENING_INCREASING_WARMTH, null));
        options.add(new Option(2015, 338, null, new int[]{2016}, "", "<Select an option>", -1));
        options.add(new Option(2015, 214, new int[]{2016}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2015, 215, null, new int[]{2016}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(2015, 216, null, new int[]{2016}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Warmth
        questions.add(new Question(false, screeningCallId, 2016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCREASING_WARMTH_TIME, numeric2Digit));
        //Increasing Pain
        questions.add(new Question(true, screeningCallId, 2017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga mein dard ka barhna", ParamNames.SCREENING_INCREASING_PAIN, null));
        options.add(new Option(2017, 338, null, new int[]{2018}, "", "<Select an option>", -1));
        options.add(new Option(2017, 214, new int[]{2018}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2017, 215, null, new int[]{2018}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(2017, 216, null, new int[]{2018}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Pain
        questions.add(new Question(false, screeningCallId, 2018, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCREASING_PAIN_TIME, numeric2Digit));
        //Antibiotic
        questions.add(new Question(true, screeningCallId, 2033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Kya operation ke 24 ghantay ke baad, antibiotic lenay ki hidayat di gai thi?", ParamNames.SCREENING_ANTIBIOTICS, null));
        options.add(new Option(2033, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(2033, 214, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2033, 215, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(2033, 216, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));

        //No Signs & Symptoms
        questions.add(new Question(true, screeningCallId, 2021, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Koi alamaat nahi hai?", ParamNames.SCREENING_NO_SIGNS_TIME, null));
        options.add(new Option(2021, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(2021, 214, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(2021, 215, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        //Telephone Number
        questions.add(new Question(true, screeningCallId, 2002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_CELL_NUMBER,
                "Telephone number Patient called from", ParamNames.SCREENING_TELEPHONE, numeric11Digit));
    }

    private void initSsiDetection() {
        Integer ssiDetectionId = 13;
        //Heading General Information

        // Autopopulate
        questions.add(new Question(false, ssiDetectionId, 5056, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Name of procedure", null, alpha30DigitSpace));

// Autopopulate
        questions.add(new Question(true, ssiDetectionId, 5057, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Date of Procedure", null, dateMinLastYearMaxNextYear));


        questions.add(new Question(true, ssiDetectionId, 5049, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "SSI Detected", ParamNames.SSI_DETECTION_SSI_DETECTED, null));
        options.add(new Option(5049, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(5049, 202, null, null, ParamNames.SSI_DETECTION_DURING_IN_PATIENT_DAY, "During In patient stay", -1));
        options.add(new Option(5049, 203, null, null, ParamNames.SSI_DETECTION_READMISSION, "Readmission", -1));
        options.add(new Option(5049, 204, null, null, ParamNames.SSI_DETECTION_FUP_WITH_SURGEON, "Follow up with Surgeon", -1));

        questions.add(new Question(true, ssiDetectionId, 5050, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Date of Diagnosis", ParamNames.SSI_DETECTION_DIAGNOSIS_DATE, dateMinTodayMaxLastMonday));
// Autopopulate
        questions.add(new Question(true, ssiDetectionId, 5058, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Days since surgery", null, numeric3DigitMin1));

        questions.add(new Question(true, ssiDetectionId, 5059, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Follow up status", null, null));
        options.add(new Option(5059, 400, null, null, "1883AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Urgent", -1));
        options.add(new Option(5059, 401, null, null, "812b7e76-93df-42a9-90f3-1a6685b9513b", "Routine", -1));

        questions.add(new Question(false, ssiDetectionId, 5062, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Reason for urgent appointment", null, alpha60DigitSpace));

        questions.add(new Question(false, ssiDetectionId, 5051, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "If patient showed up in Emergency, then who reported?", ParamNames.SSI_DETECTION_EMERGENCY_REPORT, alpha60DigitSpace));

        questions.add(new Question(false, ssiDetectionId, 5052, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Reason for urgent appointment (Other)", ParamNames.SSI_DETECTION_REASON_OTHER, alpha60DigitSpace));

        questions.add(new Question(true, ssiDetectionId, 5060, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "If detected during Post-Discharge Surveillance, date of follow-up call?", null, dateMinTodayMaxLastMonday));

        questions.add(new Question(true, ssiDetectionId, 5061, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "If patient called on the helpline, date & time patient called?", null, dateMinTodayMaxLastMonday));

        questions.add(new Question(true, ssiDetectionId, 5053, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Patient Diagnosed by", ParamNames.SSI_DETECTION_PATIENT_DIAGNOSIS_BY, null));
        options.add(new Option(5053, 338, null, new int[]{5054, 5055}, "", "<Select an option>", -1));
        options.add(new Option(5053, 202, new int[]{5054}, new int[]{5055}, ParamNames.SURGEON, "SURGEON", -1));
        options.add(new Option(5053, 203, new int[]{5055}, new int[]{5054}, ParamNames.ICN, "ICN", -1));

        questions.add(new Question(true, ssiDetectionId, 5054, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Surgeon Name", ParamNames.SSI_DETECTION_SURGEON_NAME, alpha30DigitSpace));

        questions.add(new Question(true, ssiDetectionId, 5055, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "ICN Name", ParamNames.SSI_DETECTION_ICN_NAME, alpha30DigitSpace));

    }

    private void initPostOpFollowUpCall() {
        Integer postopFollowUpId = 4;
        //Surgery Related Questions
        questions.add(new Question(false, postopFollowUpId, 4996, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Post-Operative Follow-up Call", null, null));

        //Surgery Related Questions
        questions.add(new Question(false, postopFollowUpId, 4998, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Part A", null, null));

        this.questions.add(new Question(true, postopFollowUpId, 4001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_AUTOCOMPLETE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "ICM Username", ICM_ID_NUMBER, alpha20DigitSpace));
        this.options.addAll(DynamicOptions.getProviderOptions(context, 4001, null, null));

        //Intervention Arm
        questions.add(new Question(true, postopFollowUpId, 4002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Intervention Arm", ParamNames.POST_OP_FUP_INTERVENTION_ARM, null));
        options.add(new Option(4002, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4002, 400, null, null, "1fcabb03-7f1e-4831-ab8a-9e6a75ced237", "Reminder", -1));
        options.add(new Option(4002, 401, null, null, "033716b9-005e-4115-b783-7fa20e33617c", "N- Reminder", -1));

        //Date Of Followup-call
        questions.add(new Question(true, postopFollowUpId, 4003, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Date of Follow up Call", ParamNames.POST_OP_FUP_FOLLOWUP_CALL_DATE, dateMinLastYearMaxNextYear));

        //Raabta hua?
        questions.add(new Question(true, postopFollowUpId, 4004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Raabta hua?", ParamNames.CONTACT_ESTABLISHED, null));
        options.add(new Option(4004, 338, null, new int[]{4005, 4006, 4007, 4008, 4013, 4022, 4023, 4024, 4026, 4028, 4033, 4035, 4037, 4039, 4041, 4042, 4043, 4997}, "", "<Select an option>", -1));
        options.add(new Option(4004, 400, new int[]{4005, 4006, 4007, 4008, 4013, 4022, 4023, 4024, 4026, 4028, 4033, 4035, 4037, 4039, 4041, 4042, 4043, 4997}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan ", -1));
        options.add(new Option(4004, 401, null, new int[]{4005, 4006, 4007, 4008, 4013, 4022, 4023, 4024, 4026, 4028, 4033, 4035, 4037, 4039, 4041, 4042, 4043, 4997}, "b07a037c-dc46-42ed-ab67-60a16dc0da7a", "Nahi (3 martaba koshish ki)", -1));

        // muaina kiya?
        questions.add(new Question(true, postopFollowUpId, 4005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Kya mareez ne khud zakhm ka muaina kiya?", ParamNames.POST_OP_FUP_SELF_SCREEN, null));
        options.add(new Option(4005, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4005, 400, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan ", -1));
        options.add(new Option(4005, 401, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1)); // muaina kiya?

        questions.add(new Question(true, postopFollowUpId, 4006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Hospital mein aap ko jo maloomati parcha diya gaya tha, kya wo abhi bhi aap ke paas hai?", ParamNames.POST_OP_FUP_BROCHURE, null));
        options.add(new Option(4006, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4006, 400, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan ", -1));
        options.add(new Option(4006, 401, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));

        questions.add(new Question(true, postopFollowUpId, 4007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Kya aaap ka zakhm mukammal taur per thek ho gaya hai?", ParamNames.POST_OP_FUP_WOUND_HEAL, null));
        options.add(new Option(4007, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4007, 400, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan ", -1));
        options.add(new Option(4007, 401, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));

        questions.add(new Question(true, postopFollowUpId, 4008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Kya aap ne aapnay doctor ki di gayi tareekh per unse muaina karaya?", ParamNames.POST_OP_FUP_FOLLOWUP_DOCTOR, null));
        options.add(new Option(4008, 338, null, new int[]{4009, 4010, 4012}, "", "<Select an option>", -1));
        options.add(new Option(4008, 400, new int[]{4009, 4010, 4012}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan ", -1));
        options.add(new Option(4008, 401, null, new int[]{4009, 4010, 4012}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));

        questions.add(new Question(true, postopFollowUpId, 4009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, to muaina kis tareekh ko hua?", ParamNames.POST_OP_FUP_FOLLOWUP_DATE, dateMinTodayMaxLastMonday));

        questions.add(new Question(true, postopFollowUpId, 4010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, to aap ke doctor ne aaap ko kya ilaaj feraham kiya?", ParamNames.POST_OP_FUP_FOLLOWUP_TREATMENT, null));
        options.add(new Option(4010, 338, null, new int[]{4011}, "", "<Select an option>", -1));
        options.add(new Option(4010, 400, null, new int[]{4011}, "1935AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Zakhm ki Safai", -1));
        options.add(new Option(4010, 401, null, new int[]{4011}, "1195AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Antibiotics", -1));
        options.add(new Option(4010, 400, null, new int[]{4011}, "151596AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Patti Tabdeel Ki", -1));
        options.add(new Option(4010, 401, new int[]{4011}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Kuch aur", -1));

        this.questions.add(new Question(false, postopFollowUpId, 4011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Agar kuch aur to wazahat karein", ParamNames.POST_OP_FUP_FOLLOWUP_TREATMENT_OTHER, alpha150DigitSpace));

        questions.add(new Question(true, postopFollowUpId, 4012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Kya aap ke doctor ne infection (SSI) tashkhees kiya?", ParamNames.POST_OP_FUP_SURGEON_DIAGNOSIS, null));
        options.add(new Option(4012, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4012, 400, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan ", -1));
        options.add(new Option(4012, 401, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));

        questions.add(new Question(true, postopFollowUpId, 4013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Kya aap apnay operation se mutaaliq pareshani ki waja se kisi aur ke paas ilaaj ke liye gaye?", ParamNames.POST_OP_FUP_FOLLOWUP_ELSE, null));
        options.add(new Option(4013, 338, null, new int[]{4014, 4015, 4016, 4017, 4018, 4019, 4021, 4023}, "", "<Select an option>", -1));
        options.add(new Option(4013, 400, new int[]{4014, 4015, 4016, 4017, 4018, 4019, 4021, 4023}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan ", -1));
        options.add(new Option(4013, 401, null, new int[]{4014, 4015, 4016, 4017, 4018, 4019, 4021, 4023}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));

        questions.add(new Question(true, postopFollowUpId, 4014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, to kis ke paas gaye?", ParamNames.POST_OP_FUP_ELSE_PROFESSION, null));
        options.add(new Option(4014, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4014, 400, null, null, "f0ec5490-f103-4276-bdbc-70fa063e08fb", "GP", -1));
        options.add(new Option(4014, 401, null, null, "163557AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pharmacist", -1));
        options.add(new Option(4014, 400, null, null, "ef498532-4c0d-4c11-adfb-93f982222c19", "Roohani", -1));
        options.add(new Option(4014, 401, null, null, "58df7382-1d53-4c8a-a3c2-92953e03ea9f", "Homeopathic Doctor", -1));
        options.add(new Option(4014, 401, null, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Koi aur", -1));

        questions.add(new Question(true, postopFollowUpId, 4015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Kis tareekh ko gaye thay?", ParamNames.POST_OP_FUP_ELSE_DATE, dateMinTodayMaxLastMonday));

        questions.add(new Question(false, postopFollowUpId, 4016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Jis Idaaray mein gaye thay uska naam bataein", ParamNames.POST_OP_FUP_ELSE_WHERE, alpha160DigitSpace));

        questions.add(new Question(false, postopFollowUpId, 4017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Ilaaj karnay walay ka naam bataein", ParamNames.POST_OP_FUP_ELSE_NAME, alpha160DigitSpace));

        questions.add(new Question(false, postopFollowUpId, 4018, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Ilaaj karnay walay ka mobile number batein", ParamNames.POST_OP_FUP_ELSE_CONTACT, numeric11Digit));

        questions.add(new Question(true, postopFollowUpId, 4019, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, to unhon ne aap ko kya ilaaj feraham kiya?", ParamNames.POST_OP_FUP_ELSE_TREATMENT, null));
        options.add(new Option(4019, 338, null, new int[]{4020}, "", "<Select an option>", -1));
        options.add(new Option(4019, 400, null, new int[]{4020}, "1935AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Zakhm ki Safai", -1));
        options.add(new Option(4019, 401, null, new int[]{4020}, "1195AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Antibiotics", -1));
        options.add(new Option(4019, 400, null, new int[]{4020}, "151596AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Patti Tabdeel Ki", -1));
        options.add(new Option(4019, 401, new int[]{4020}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Kuch aur", -1));

        questions.add(new Question(false, postopFollowUpId, 4020, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Agar kuch aur to wazahat karein", ParamNames.POST_OP_FUP_ELSE_TREATMENT_OTHER, alpha160DigitSpace));

        questions.add(new Question(true, postopFollowUpId, 4021, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Kya unhon ne infection (SSI) tashkhees kiya?", ParamNames.POST_OP_FUP_ELSE_DIAGNOSIS, null));
        options.add(new Option(4021, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4021, 202, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4021, 203, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(4021, 204, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));

        questions.add(new Question(true, postopFollowUpId, 4022, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Kya aap operation ke baad dobara kisi hospital mein daakhil rahe?", ParamNames.POST_OP_FUP_READMISSION, null));
        options.add(new Option(4022, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4022, 202, new int[]{4023}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4022, 203, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(4022, 204, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));

        questions.add(new Question(true, postopFollowUpId, 4023, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Aap muainay ke liye Indus hospital kyun nahi aaye?", ParamNames.POST_OP_FUP_NOT_INDUS, alpha160DigitSpace));

        //Part B
        questions.add(new Question(false, postopFollowUpId, 4997, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Part B", null, null));

        //Colored Drainage
        questions.add(new Question(true, postopFollowUpId, 4024, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga per pus (peeli, hari ya safaid) hona", ParamNames.SCREENING_COLORED_DRAINAGE, null));
        options.add(new Option(4024, 338, null, new int[]{4025}, "", "<Select an option>", -1));
        options.add(new Option(4024, 202, new int[]{4025}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4024, 203, null, new int[]{4025}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(4024, 204, null, new int[]{4025}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Colored Drainage
        questions.add(new Question(false, postopFollowUpId, 4025, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_COLORED_DRAINAGE_TIME, numeric2Digit));
        //Increasing Swellness
        questions.add(new Question(true, postopFollowUpId, 4026, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga per soojan hai", ParamNames.SCREENING_INCREASING_SWELL, null));
        options.add(new Option(4026, 338, null, new int[]{4027}, "", "<Select an option>", -1));
        options.add(new Option(4026, 205, new int[]{4027}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4026, 206, null, new int[]{4027}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(4026, 207, null, new int[]{4027}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Increasing Swellness
        questions.add(new Question(false, postopFollowUpId, 4027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCREASING_SWELL_TIME, numeric2Digit));
        //Increasing Fever
        questions.add(new Question(true, postopFollowUpId, 4028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ke 24 ghantay ke baad Bukhar hona", ParamNames.SCREENING_INCREASING_FEVER, null));
        options.add(new Option(4028, 338, null, new int[]{4029, 4030, 4031}, "", "<Select an option>", -1));
        options.add(new Option(4028, 208, new int[]{4029, 4030, 4031}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4028, 209, null, new int[]{4029, 4030, 4031}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(4028, 210, null, new int[]{4029, 4030, 4031}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Fever
        questions.add(new Question(false, postopFollowUpId, 4029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCREASING_FEVER_TIME, numeric2Digit));
        //Agar haan, tou kitna
        questions.add(new Question(false, postopFollowUpId, 4030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitna?", ParamNames.SCREENING_FEVER_INTENSITY, null));
        options.add(new Option(4030, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4030, 208, null, null, "1408AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Tez", -1));
        options.add(new Option(4030, 209, null, null, "1407AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Halka", -1));
        //Aap ne check kiya?
        questions.add(new Question(false, postopFollowUpId, 4031, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Aap ne check kiya?", ParamNames.SCREENING_FEVER_MEASURE, null));
        options.add(new Option(4031, 338, null, new int[]{4032}, "", "<Select an option>", -1));
        options.add(new Option(4031, 208, new int[]{4032}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4031, 209, null, new int[]{4032}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        //Fever Reading
        questions.add(new Question(false, postopFollowUpId, 4032, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Kitna tez bukhar?", ParamNames.SCREENING_FEVER_READING, numeric3DigitMin1));
        //Incision Edge Separation
        questions.add(new Question(true, postopFollowUpId, 4033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga key kinarey alag hona", ParamNames.SCREENING_INCISION_EDGE, null));
        options.add(new Option(4033, 338, null, new int[]{4034}, "", "<Select an option>", -1));
        options.add(new Option(4033, 208, new int[]{4034}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4033, 209, null, new int[]{4034}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(4033, 210, null, new int[]{4034}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Edge Separation
        questions.add(new Question(false, postopFollowUpId, 4034, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCISION_EDGE_TIME, numeric2Digit));
        //Increasing Redness
        questions.add(new Question(true, postopFollowUpId, 4035, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga per surkhi hona", ParamNames.SCREENING_INCREASING_REDNESS, null));
        options.add(new Option(4035, 338, null, new int[]{4036}, "", "<Select an option>", -1));
        options.add(new Option(4035, 211, new int[]{4036}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4035, 212, null, new int[]{4036}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(4035, 213, null, new int[]{4036}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Redness
        questions.add(new Question(false, postopFollowUpId, 4036, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCREASING_REDNESS_TIME, numeric2Digit));
        //Increasing Warmth
        questions.add(new Question(true, postopFollowUpId, 4037, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga per garmahat  mehsoos hona", ParamNames.SCREENING_INCREASING_WARMTH, null));
        options.add(new Option(4037, 338, null, new int[]{4038}, "", "<Select an option>", -1));
        options.add(new Option(4037, 214, new int[]{4038}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4037, 215, null, new int[]{4038}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(4037, 216, null, new int[]{4038}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Warmth
        questions.add(new Question(false, postopFollowUpId, 4038, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCREASING_WARMTH_TIME, numeric2Digit));
        //Increasing Pain
        questions.add(new Question(true, postopFollowUpId, 4039, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Operation ki jaga mein dard ka barhna", ParamNames.SCREENING_INCREASING_PAIN, null));
        options.add(new Option(4039, 338, null, new int[]{4040}, "", "<Select an option>", -1));
        options.add(new Option(4039, 214, new int[]{4040}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4039, 215, null, new int[]{4040}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(4039, 216, null, new int[]{4040}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));
        //Duration of Pain
        questions.add(new Question(false, postopFollowUpId, 4040, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Agar haan, tou kitney dinoon sey?", ParamNames.SCREENING_INCREASING_PAIN_TIME, numeric2Digit));
        //Antibiotic
        questions.add(new Question(true, postopFollowUpId, 4041, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Kya operation ke 24 ghantay ke baad, antibiotic lenay ki hidayat di gai thi?", ParamNames.SCREENING_ANTIBIOTICS, null));
        options.add(new Option(4041, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4041, 214, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4041, 215, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));
        options.add(new Option(4041, 216, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pata nahi", -1));

        //No Signs & Symptoms
        questions.add(new Question(true, postopFollowUpId, 4042, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Koi alamaat nahi hai?", ParamNames.SCREENING_NO_SIGNS_TIME, null));
        options.add(new Option(4042, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(4042, 214, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Haan", -1));
        options.add(new Option(4042, 215, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Nahi", -1));

        questions.add(new Question(false, postopFollowUpId, 4043, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Agar mareez ko ek ya ek se zyada alamatein hein, to unse poochein ke unhon ne khud zakhm ka muaina kar ke helpline per call kyun nahi ki?", ParamNames.POST_OP_FUP_HELPLINE_SYMPTOMS, alpha160DigitSpace));

    }

    private void initSurgeonEvaluation() {
        Integer surgeonEvalId = 5;
        questions.add(new Question(false, surgeonEvalId, 5999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Surgeon Evaluation Form", null, null));
      /*  //MR No
        questions.add(new Question(true, surgeonEvalId, 5000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "MR No", MR_NUMBER, numeric12Digit));
        //SSI No
        questions.add(new Question(true, surgeonEvalId, 5001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "SSI Study No", SSI_STUDY_NO, numeric3DigitMin1));*/
        // questions.add(new Question(false, surgeonEvalId, 5998, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Assessment Questions", null, null));
        //Date of Assessment
        questions.add(new Question(true, surgeonEvalId, 5002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME,
                "Date of Assessment", ParamNames.SURGEON_EVAL_DATE_ASSESSMENT, dateMinTodayMaxLastMonday));
        //Date Of Procedure
        questions.add(new Question(true, surgeonEvalId, 5037, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Date of Procedure", null, dateMinTodayMaxLastMonday));

        //Follow-up Status
        questions.add(new Question(true, surgeonEvalId, 5003, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Follow-up Status", ParamNames.POST_OP_FUP_FOLLOWUP_STATUS, null));
        options.add(new Option(5003, 338, null, new int[]{5038}, "", "<Select an option>", -1));
        options.add(new Option(5003, 500, null, new int[]{5038}, "812b7e76-93df-42a9-90f3-1a6685b9513b", "Routine", -1));
        options.add(new Option(5003, 501, new int[]{5038}, null, "1883AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Urgent", -1));

        questions.add(new Question(true, surgeonEvalId, 5038, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Reason for urgent appointment", ParamNames.SURGEON_EVAL_REASON_OF_URGENT_APPOINTMENT, null));
        options.add(new Option(5038, 338, null, new int[]{5039}, "", "<Select an option>", -1));
        options.add(new Option(5038, 500, null, new int[]{5039}, "aaf66fd2-fc4d-418a-ad51-197d7bd65512", "Emergency", -1));
        options.add(new Option(5038, 501, new int[]{5039}, null, "c9962f5b-ab49-475a-bf60-1116d808aa8e", "Follow up calls by ICMs", -1));
        options.add(new Option(5038, 501, null, new int[]{5039}, "c047a033-c474-49a2-9e00-2dc4f14279c9", "Patient called on Helpline", -1));

        //Date Of follow up call
        questions.add(new Question(true, surgeonEvalId, 5039, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Date of follow up call by ICM", ParamNames.SURGEON_EVAL_DOC_INTERVENTION, dateMinTodayMaxLastMonday));
      /*  //Patient Name
        questions.add(new Question(true, surgeonEvalId, 5004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Name of the patient", PATIENT_NAME, alpha60DigitSpace));
        //Father's/Husband's Name
        questions.add(new Question(true, surgeonEvalId, 5005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Fathers/Husbands Name", FATHER_HUSBAND_NAME, alpha60DigitSpace));*/

        this.questions.add(new Question(true, surgeonEvalId, 5040, "", InputWidget.InputWidgetsType.WIDGET_TYPE_AUTOCOMPLETE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "ICM Username", ICM_ID_NUMBER, alpha20DigitSpace));
        this.options.addAll(DynamicOptions.getProviderOptions(context, 5040, null, null));

        questions.add(new Question(false, surgeonEvalId, 5047, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Evaluation Type", ParamNames.EVALUATION_TYPE, null));
        options.add(new Option(5047, 300, null, new int[]{5048}, ParamNames.ICM_EVALUATION, "ICM evaluation", -1));
        options.add(new Option(5047, 301, new int[]{5048}, null, ParamNames.FINAL_EVALUATION, "Final evaluation", -1));

        questions.add(new Question(false, surgeonEvalId, 5048, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Final Evaluation", ParamNames.FINAL_EVALUATION, null));
        options.add(new Option(5048, 300, new int[]{5041}, new int[]{5042}, ParamNames.SURGEON, "Surgeon", -1));
        options.add(new Option(5048, 301, new int[]{5042}, new int[]{5041}, ParamNames.ICN, "ICN", -1));

        questions.add(new Question(false, surgeonEvalId, 5041, "", InputWidget.InputWidgetsType.WIDGET_TYPE_AUTOCOMPLETE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Surgeon's Name", ParamNames.SURGEON_EVAL_SURGEON_NAME, alpha50DigitSpaceDot));
        //TODO please remove this dummy list and add the real data when provided.
        options.add(new Option(5041, 300, null, null, "", "Dr. Irfan Javed", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Irshad Hussain", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Muhammad Ismail Seerat", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Mazhar Ali", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Zeeshan Arshad", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Gauhar Nawaz Khan", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Hassan Raza Khosa", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Ismail", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Zahida Liaqat", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Saima Gul Bashir", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Samiea Parveen", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Navera Ashraf", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Hina Faheem", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Khizra Akram Chouhan", -1));
        options.add(new Option(5041, 301, null, null, "", "Dr. Afshan Rasheed", -1));

        questions.add(new Question(true, surgeonEvalId, 5042, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "ICN's Name", ParamNames.SURGEON_EVAL_ICN_NAME, alpha30DigitSpace));

        questions.add(new Question(true, surgeonEvalId, 5007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Presence of colored drainage (yellow, green or white) at the incision site", ParamNames.SURGEON_EVAL_COLORED_DRAINAGE, null));
        options.add(new Option(5007, 338, null, new int[]{5064}, "", "<Select an option>", -1));
        options.add(new Option(5007, 502, new int[]{5064}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5007, 503, null, new int[]{5064}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));


        questions.add(new Question(false, surgeonEvalId, 5064, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Since (no. of days)", ParamNames.POST_OP_FUP_COLORED_DRAINAGE_TIME, numeric2Digit));


        questions.add(new Question(true, surgeonEvalId, 5009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Localized swelling", ParamNames.SURGEON_EVAL_INCREASING_SWELL, null));
        options.add(new Option(5009, 338, null, new int[]{5065}, "", "<Select an option>", -1));
        options.add(new Option(5009, 505, new int[]{5065}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5009, 506, null, new int[]{5065}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        questions.add(new Question(false, surgeonEvalId, 5065, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Since (no. of days)", ParamNames.POST_OP_FUP_INCREASING_SWELL_TIME, numeric2Digit));


        questions.add(new Question(true, surgeonEvalId, 5011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Fever > 101" + degree_sign + "F/38" + degree_sign + "C since your surgery", ParamNames.SCREENING_INCREASING_FEVER, null));
        options.add(new Option(5011, 338, null, new int[]{5012, 5013, 5035}, "", "<Select an option>", -1));
        options.add(new Option(5011, 508, new int[]{5012, 5013, 5035}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5011, 509, null, new int[]{5012, 5013, 5035}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        //Duration of Fever
        questions.add(new Question(false, surgeonEvalId, 5012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Since (no.of days)", ParamNames.SCREENING_INCREASING_FEVER_TIME, numeric2Digit));
        //High/Low
        questions.add(new Question(false, surgeonEvalId, 5035, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "If yes?", ParamNames.SURGEON_EVAL_FEVER_HIGH_LOW, null));
        options.add(new Option(5035, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(5035, 550, null, null, "1408AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "High", -1));
        options.add(new Option(5035, 551, null, null, "1407AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Low", -1));
        //Did measure fever
        questions.add(new Question(false, surgeonEvalId, 5013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Did you measure it?", ParamNames.SURGEON_EVAL_FEVER_HAS_MEASURED, null));
        options.add(new Option(5013, 338, null, new int[]{5014}, "", "<Select an option>", -1));
        options.add(new Option(5013, 510, new int[]{5014}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5013, 511, null, new int[]{5014}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        //Fever Reading
        questions.add(new Question(false, surgeonEvalId, 5014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What was the reading", ParamNames.SURGEON_EVAL_FEVER_READING, numeric3DigitMin1));
        //Incision Edge Separation
        questions.add(new Question(true, surgeonEvalId, 5015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Edges of the incision separating/ Dehiscence (Superficial/Deep Wound)", ParamNames.SCREENING_INCISION_EDGE, null));
        options.add(new Option(5015, 338, null, new int[]{5066}, "", "<Select an option>", -1));
        options.add(new Option(5015, 512, new int[]{5066}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5015, 513, null, new int[]{5066}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        questions.add(new Question(false, surgeonEvalId, 5066, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Since (no. of days)", ParamNames.POST_OP_FUP_INCISION_EDGE_TIME, numeric2Digit));


        questions.add(new Question(true, surgeonEvalId, 5017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Redness at the incision site", ParamNames.SURGEON_EVAL_INCREASING_REDNESS, null));
        options.add(new Option(5017, 338, null, new int[]{5067}, "", "<Select an option>", -1));
        options.add(new Option(5017, 515, new int[]{5067}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5017, 516, null, new int[]{5067}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        questions.add(new Question(false, surgeonEvalId, 5067, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Since (no. of days)", ParamNames.POST_OP_FUP_INCREASING_REDNESS_TIME, numeric2Digit));

        questions.add(new Question(true, surgeonEvalId, 5019, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Increasing warmth/heat from the incision", ParamNames.SCREENING_INCREASING_WARMTH, null));
        options.add(new Option(5019, 338, null, new int[]{5063}, "", "<Select an option>", -1));
        options.add(new Option(5019, 518, new int[]{5063}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5019, 519, null, new int[]{5063}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        questions.add(new Question(false, surgeonEvalId, 5063, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Since (no. of days)", ParamNames.SCREENING_INCREASING_WARMTH_TIME, numeric2Digit));

        questions.add(new Question(true, surgeonEvalId, 5021, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Increase in pain at the incision since the surgery", ParamNames.SCREENING_INCREASING_PAIN, null));
        options.add(new Option(5021, 338, null, new int[]{5068}, "", "<Select an option>", -1));
        options.add(new Option(5021, 521, new int[]{5068}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5021, 522, null, new int[]{5068}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        questions.add(new Question(false, surgeonEvalId, 5068, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Since (no. of days)", ParamNames.POST_OP_FUP_INCREASING_PAIN_TIME, numeric2Digit));

        questions.add(new Question(true, surgeonEvalId, 5022, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Were you given any antibiotics post-operatively beyond 24 hours?", ParamNames.SURGEON_EVAL_ANTIBIOTICS_24H, null));
        options.add(new Option(5022, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(5022, 524, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5022, 525, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        questions.add(new Question(true, surgeonEvalId, 5043, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Has the patient developed a SURGICAL SITE INFECTION?", ParamNames.SURGEON_EVAL_SURGEON_DIAGNOSIS, null));
        options.add(new Option(5043, 338, null, new int[]{5044, 5045, 5069}, "", "<Select an option>", -1));
        options.add(new Option(5043, 544, new int[]{5044, 5045, 5069}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5043, 545, null, new int[]{5044, 5045, 5069}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        questions.add(new Question(false, surgeonEvalId, 5069, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Since (no. of days)", ParamNames.POST_OP_FUP_HCW_HAS_INFECTION_TIME, numeric2Digit));

        questions.add(new Question(true, surgeonEvalId, 5044, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "If yes, then what type of infection is it?", ParamNames.SURGEON_EVAL_SSI_TYPE, null));
        options.add(new Option(5044, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(5044, 544, null, null, "164218AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Superficial Incisonal SSI", -1));
        options.add(new Option(5044, 545, null, null, "164219AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Deep Incisional SSI", -1));
        options.add(new Option(5044, 545, null, null, "164220AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Organ/Space SSI", -1));

        questions.add(new Question(true, surgeonEvalId, 5045, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Are antibiotics required to treat the infection?", ParamNames.SURGEON_EVAL_ANTIBOTIC_REQD, null));
        options.add(new Option(5045, 338, null, new int[]{5046}, "", "<Select an option>", -1));
        options.add(new Option(5045, 544, new int[]{5046}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5045, 545, null, new int[]{5046}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));

        questions.add(new Question(false, surgeonEvalId, 5046, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "If Yes, specify the name and dosage", ParamNames.SURGEON_EVAL_DRUG_DOSAGE, alphaNumeric150DigitSpace));

        //Wound exploration
        questions.add(new Question(true, surgeonEvalId, 5032, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Wound exploration or incision and drainage needed?", ParamNames.SURGEON_EVAL_WOUND_EXPLORATON, null));
        options.add(new Option(5032, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(5032, 544, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5032, 545, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        //Wound culture
        questions.add(new Question(false, surgeonEvalId, 5033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Wound culture taken?", ParamNames.SURGEON_EVAL_CULTURE_TAKEN, null));
        options.add(new Option(5033, 338, null, new int[]{5034}, "", "<Select an option>", -1));
        options.add(new Option(5033, 546, new int[]{5034}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(5033, 547, null, new int[]{5034}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        //Wound culture date
        questions.add(new Question(false, surgeonEvalId, 5034, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Date culture was taken:", ParamNames.SURGEON_EVAL_DATE_OF_CULTURE_TAKE, dateMinTodayMaxLastMonday));
    }

    //CIRCUMCISION FORMS
    private void initBeforeCircumcision() {
        Integer beforeCircumcisionId = 7;
        //Heading Basic Information
        questions.add(new Question(false, beforeCircumcisionId, 7999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Basic Information", null, null));
        //MR No
        questions.add(new Question(true, beforeCircumcisionId, 7000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_MRNO,
                "MR Number", MR_NUMBER, numeric12Digit));
        // this.options.add(new Option(7000, 90, null, null, "", "Enter MR No", -1));
        //Patient Id
        questions.add(new Question(true, beforeCircumcisionId, 7001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Patient ID", ParamNames.PATIENT_ID, alpha7DigitSpace));
        //Patient Name
        questions.add(new Question(true, beforeCircumcisionId, 7002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Patient Name (CAPITAL LETTERS)", PATIENT_NAME, alpha50DigitSpaceCapsOnly));
        //Father Name
        questions.add(new Question(true, beforeCircumcisionId, 7003, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Father/Guardian's Name (CAPITAL LETTERS)", ParamNames.FATHER_HUSBAND_NAME, alpha50DigitSpaceCapsOnly));
        //Date of birth
        this.questions.add(new Question(true, beforeCircumcisionId, 7004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Date of Birth", ParamNames.DATE_OF_BIRTH, dateMinTodayMaxLastMonday));
        //Date of Procedure
        this.questions.add(new Question(true, beforeCircumcisionId, 7005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Date of Procedure", ParamNames.DATE_OF_PROCEDURE, dateMinTodayMaxLastMonday));
        //Other Information from parents/guardians:
        questions.add(new Question(false, beforeCircumcisionId, 7998, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Other Information from parents/guardians", null, null));
        //Address
        questions.add(new Question(true, beforeCircumcisionId, 7006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Address", ParamNames.ADDRESS1, alphanumeric100DigitSpace));
        //Contact
        questions.add(new Question(true, beforeCircumcisionId, 7007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Contact Number 1", ParamNames.PRE_OP_CONTACT1, numeric12Digit));
        //Relationship
        questions.add(new Question(true, beforeCircumcisionId, 7008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Name/(Relationship)", PRE_OP_CONTACT1_RELATIONSHIP, alpha50DigitSpace));
        //Contact 2
        questions.add(new Question(true, beforeCircumcisionId, 7009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Contact Number 2", ParamNames.PRE_OP_CONTACT2, numeric12Digit));
        //Relationship 2
        questions.add(new Question(true, beforeCircumcisionId, 7010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Name/(Relationship)", PRE_OP_CONTACT2_RELATIONSHIP, alpha50DigitSpace));
        //Place of Delivery
        questions.add(new Question(true, beforeCircumcisionId, 7011, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Place of Delivery", ParamNames.PLACE_OF_DELIVERY, numeric3DigitMin1));
        options.add(new Option(7011, 500, null, null, "1", "Sheikh Saeed Hospital", -1));
        options.add(new Option(7011, 501, null, null, "2", "Different facility", -1));
        options.add(new Option(7011, 501, null, null, "3", "Home", -1));
        options.add(new Option(7011, 501, null, null, "88", "Refused", -1));
        //Differnet Facility
        questions.add(new Question(true, beforeCircumcisionId, 7012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "If different facility, state name", ParamNames.DIFFERENT_FACILITY, alpha50DigitSpace));
        //No of children
        questions.add(new Question(true, beforeCircumcisionId, 7013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Number of children", ParamNames.NO_OF_CHILDREN, numeric2Digit));
        //Ethnicity
        questions.add(new Question(true, beforeCircumcisionId, 7022, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Ethnicity", ParamNames.ETHNICITY, numeric3DigitMin1));
        options.add(new Option(7022, 500, null, new int[]{7023}, "1", "Balochi", -1));
        options.add(new Option(7022, 501, null, new int[]{7023}, "2", "Muhajir", -1));
        options.add(new Option(7022, 500, null, new int[]{7023}, "3", "Pathan", -1));
        options.add(new Option(7022, 501, null, new int[]{7023}, "4", "Punjabi", -1));
        options.add(new Option(7022, 501, null, new int[]{7023}, "5", "Sindhi", -1));
        options.add(new Option(7022, 501, null, new int[]{7023}, "6", "Siraiki", -1));
        options.add(new Option(7022, 501, null, new int[]{7023}, "99", "Don't know", -1));
        options.add(new Option(7022, 501, new int[]{7023}, null, "66", "Other", -1));
        //Other Ethnicity
        questions.add(new Question(true, beforeCircumcisionId, 7023, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.ETHNICITY_OTHER, alpha20DigitSpace));
        //Religion
        questions.add(new Question(true, beforeCircumcisionId, 7024, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Religion of parent / guardian", ParamNames.RELIGION, numeric3DigitMin1));
        options.add(new Option(7024, 500, null, new int[]{7025}, "1", "Muslim", -1));
        options.add(new Option(7024, 501, null, new int[]{7025}, "2", "Christian", -1));
        options.add(new Option(7024, 500, null, new int[]{7025}, "3", "Hindu", -1));
        options.add(new Option(7024, 501, null, new int[]{7025}, "4", "Parsi", -1));
        options.add(new Option(7024, 501, new int[]{7025}, null, "66", "Other", -1));
        //Other Religion
        questions.add(new Question(true, beforeCircumcisionId, 7025, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.RELIGION_OTHER, alpha20DigitSpace));
        //Formal Schooling
        questions.add(new Question(true, beforeCircumcisionId, 7026, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "How many years of formal schooling of father? ", ParamNames.FORMAL_SCHOOL_FATHER, numeric2Digit));
        options.add(new RangeOption(7026, 500, new int[]{7027}, new int[]{}, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.EQUALS, 0)));
        //  options.add(new RangeOption(7026, 500, new int[]{7084}, new int[]{}, "", "", -1,new SkipRange(SkipRange.VALIDATION_TYPE.EQUALS,1)));
        //  options.add(new Option(7026, 90, null, null, "", "Enter formal schooling of father", -1));
        //INFORMAL EDUCATION
        questions.add(new Question(true, beforeCircumcisionId, 7027, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What type of informal education of father?", ParamNames.IN_FORMAL_SCHOOL_FATHER, numeric3DigitMin1));
        options.add(new Option(7027, 500, null, new int[]{7028}, "1", "Madrassa", -1));
        options.add(new Option(7027, 501, null, new int[]{7028}, "2", "Adult literacy", -1));
        options.add(new Option(7027, 500, null, new int[]{7028}, "3", "Home schooling", -1));
        options.add(new Option(7027, 501, null, new int[]{7028}, "4", "Self learnt (reading)", -1));
        options.add(new Option(7027, 501, null, new int[]{7028}, "5", "Self learnt (reading and writing)", -1));
        options.add(new Option(7027, 501, null, new int[]{7028}, "6", "Not educated", -1));
        options.add(new Option(7027, 501, new int[]{7028}, null, "66", "Other", -1));

         /*   //Temp Qs for checking skip logic
            questions.add(new Question(true, beforeCircumcisionId, 7084, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                    "Temp Qs for checking skip logic ?", ParamNames.IN_FORMAL_SCHOOL_FATHER_OTHER, alpha50DigitSpace));*/
        //Other Education
        questions.add(new Question(true, beforeCircumcisionId, 7028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.IN_FORMAL_SCHOOL_FATHER_OTHER, alpha50DigitSpace));
        //Formal Schooling
        questions.add(new Question(true, beforeCircumcisionId, 7029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "How many years of formal schooling of mother? ", ParamNames.FORMAL_SCHOOL_MOTHER, numeric2Digit));
        options.add(new RangeOption(7029, 500, new int[]{7030}, new int[]{}, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.EQUALS, 0)));
        //INFORMAL EDUCATION
        questions.add(new Question(true, beforeCircumcisionId, 7030, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What type of informal education of mother?", ParamNames.IN_FORMAL_SCHOOL_MOTHER, numeric3DigitMin1));
        options.add(new Option(7030, 500, null, new int[]{7031}, "1", "Madrassa", -1));
        options.add(new Option(7030, 501, null, new int[]{7031}, "2", "Adult literacy", -1));
        options.add(new Option(7030, 500, null, new int[]{7031}, "3", "Home schooling", -1));
        options.add(new Option(7030, 501, null, new int[]{7031}, "4", "Self learnt (reading)", -1));
        options.add(new Option(7030, 501, null, new int[]{7031}, "5", "Self learnt (reading and writing)", -1));
        options.add(new Option(7030, 501, null, new int[]{7031}, "6", "Not educated", -1));
        options.add(new Option(7030, 501, new int[]{7031}, null, "66", "Other", -1));
        //Other education
        questions.add(new Question(true, beforeCircumcisionId, 7031, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.IN_FORMAL_SCHOOL_MOTHER_OTHER, alpha50DigitSpace));
        //OCUPATION OF FATHER
        questions.add(new Question(true, beforeCircumcisionId, 7032, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Occupation of father", ParamNames.OCCUPATION_FATHER, numeric3DigitMin1));
        options.add(new Option(7032, 500, null, new int[]{7033}, "1", "Manager", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "2", "Professional", -1));
        options.add(new Option(7032, 500, null, new int[]{7033}, "3", "Technician", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "4", "Clerk", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "5", "Service/Sales worker", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "6", "Agriculture/fishery worker", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "7", "Craft worker", -1));
        options.add(new Option(7032, 500, null, new int[]{7033}, "8", "Machine operator/Driver", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "9", "Cleaner/ Labourer", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "10", "In forces (Police, army etc)", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "11", "Unemployed", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "12", "Student", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "13", "Retired/Pensioner", -1));
        options.add(new Option(7032, 501, new int[]{7033}, null, "66", "Other", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "88", "Refused", -1));
        options.add(new Option(7032, 501, null, new int[]{7033}, "99", "Don't know", -1));
        //Other occupation
        questions.add(new Question(true, beforeCircumcisionId, 7033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.OCCUPATION_FATHER_OTHER, alpha50DigitSpace));
        //Spontaneous Response
        questions.add(new Question(true, beforeCircumcisionId, 7034, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Spontaneous response:", ParamNames.SPONTANEOUS_RESPONSE, alpha50DigitSpace));
        //OCUPATION OF MOTHER
        questions.add(new Question(true, beforeCircumcisionId, 7035, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Does the mother have a job (from which she earns) apart from household work?", ParamNames.OCCUPATION_MOTHER, numeric3DigitMin1));
        options.add(new Option(7035, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7035, 501, null, null, "2", "No", -1));
        options.add(new Option(7035, 500, null, null, "88", "Refused", -1));
        options.add(new Option(7035, 500, null, null, "99", "Don't know", -1));
        //FAMILY
        questions.add(new Question(true, beforeCircumcisionId, 7036, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Family system", ParamNames.FAMILY_SYSTEM, numeric3DigitMin1));
        options.add(new Option(7036, 500, null, null, "1", "Joint family", -1));
        options.add(new Option(7036, 501, null, null, "2", " Nuclear family", -1));
        options.add(new Option(7036, 500, null, null, "88", "Refused", -1));
        //Circumcision Reason
        questions.add(new Question(true, beforeCircumcisionId, 7037, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Primary reason for circumcision", ParamNames.CIRCUMCISION_REASON, numeric3DigitMin1));
        options.add(new Option(7037, 500, null, new int[]{7038}, "1", "Religious", -1));
        options.add(new Option(7037, 501, null, new int[]{7038}, "2", "Cultural/Traditional", -1));
        options.add(new Option(7037, 500, null, new int[]{7038}, "3", "Medical", -1));
        options.add(new Option(7037, 500, null, new int[]{7038}, "4", "To improve sexual satisfaction", -1));
        options.add(new Option(7037, 500, new int[]{7038}, null, "66", "Other", -1));
        options.add(new Option(7037, 500, null, new int[]{7038}, "99", "Don't know", -1));
        //Circumcision Reason Other
        questions.add(new Question(true, beforeCircumcisionId, 7038, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Specify the Other option", ParamNames.CIRCUMCISION_REASON_OTHER, alpha50DigitSpace));
        //Circumcision clinic
        questions.add(new Question(true, beforeCircumcisionId, 7039, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "How did you find out about this Circumcision Clinic?", ParamNames.CIRCUMCISION_CLINIC, numeric3DigitMin1));
        options.add(new Option(7039, 500, null, new int[]{7040}, "1", "Sheikh Saeed Hospital", -1));
        options.add(new Option(7039, 501, null, new int[]{7040}, "2", "Traditional birth attendant", -1));
        options.add(new Option(7039, 500, null, new int[]{7040}, "3", "Brochure", -1));
        options.add(new Option(7039, 500, null, new int[]{7040}, "4", "Maternity home", -1));
        options.add(new Option(7039, 500, null, new int[]{7040}, "5", "Lady health worker", -1));
        options.add(new Option(7039, 500, null, new int[]{7040}, "6", "Vaccinator", -1));
        options.add(new Option(7039, 500, null, new int[]{7040}, "7", "Indus Hospital (main campus)", -1));
        options.add(new Option(7039, 500, null, new int[]{7040}, "8", "Word of mouth", -1));
        options.add(new Option(7039, 500, new int[]{7040}, null, "66", "Other", -1));
        //Circumcision Reason Other
        questions.add(new Question(true, beforeCircumcisionId, 7040, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Specify the Other option", ParamNames.CIRCUMCISION_CLINIC_OTHER, alpha50DigitSpace));
        //Circumcision Broucher
        questions.add(new Question(true, beforeCircumcisionId, 7041, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Have you seen the circumcision brochure?", ParamNames.CIRCUMCISION_BROUCHER, numeric3DigitMin1));
        options.add(new Option(7041, 500, new int[]{7042}, null, "1", "Yes", -1));
        options.add(new Option(7041, 501, null, new int[]{7042}, "2", "No", -1));
        options.add(new Option(7041, 500, null, new int[]{7042}, "99", "Don't know", -1));
        //Circumcision Broucher hELP
        questions.add(new Question(true, beforeCircumcisionId, 7042, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.GONE, Validation.CHECK_FOR_EMPTY,
                "If yes, did the brochure help you decide to opt for early and safe circumcision?", ParamNames.CIRCUMCISION_BROUCHER_HELP, numeric3DigitMin1));
        options.add(new Option(7042, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7042, 501, null, null, "2", "No", -1));
        options.add(new Option(7042, 500, null, null, "99", "Don't know", -1));
        //Circumcision Video
        questions.add(new Question(true, beforeCircumcisionId, 7043, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Have you seen the circumcision video?", ParamNames.CIRCUMCISION_VIDEO, numeric3DigitMin1));
        options.add(new Option(7043, 500, new int[]{7044}, null, "1", "Yes", -1));
        options.add(new Option(7043, 501, null, new int[]{7044}, "2", "No", -1));
        options.add(new Option(7043, 500, null, new int[]{7044}, "99", "Don't know", -1));
        //Circumcision Video help
        questions.add(new Question(true, beforeCircumcisionId, 7044, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.GONE, Validation.CHECK_FOR_EMPTY,
                "If yes, did the video help you decide to opt for early and safe circumcision?", ParamNames.CIRCUMCISION_VIDEO_HELP, numeric3DigitMin1));
        options.add(new Option(7044, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7044, 501, null, null, "2", "No", -1));
        options.add(new Option(7044, 500, null, null, "99", "Don't know", -1));
        //Heading Medical History
        questions.add(new Question(false, beforeCircumcisionId, 7997, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Medical history of baby:", null, null));
        //Bleeding disorder
        questions.add(new Question(true, beforeCircumcisionId, 7045, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Bleeding disorder", ParamNames.BLEEDING_DISORDER, numeric3DigitMin1));
        options.add(new Option(7045, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7045, 501, null, null, "2", "No", -1));
        //Convulsions
        questions.add(new Question(true, beforeCircumcisionId, 7046, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Convulsions", ParamNames.CONVULSIONS, numeric3DigitMin1));
        options.add(new Option(7046, 500, new int[]{7085}, null, "1", "Yes", -1));
        options.add(new Option(7046, 501, null, new int[]{7085}, "2", "No", -1));

        //Term baby
        questions.add(new Question(true, beforeCircumcisionId, 7047, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Term baby(>/= 37 completed weeks of gestation)", ParamNames.TERM_BABY, numeric3DigitMin1));
        options.add(new Option(7047, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7047, 501, null, null, "2", "No", -1));
        //Notes
        questions.add(new Question(true, beforeCircumcisionId, 7048, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Notes", ParamNames.CIRCUMCISION_NOTES, alpha100DigitSpace));
        //Hospitalization of baby
        questions.add(new Question(true, beforeCircumcisionId, 7049, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Hospitalization of baby after birth", ParamNames.HOSPITALIZATION_OF_BABY, numeric3DigitMin1));
        options.add(new Option(7049, 500, new int[]{7050, 7051}, null, "1", "Yes", -1));
        options.add(new Option(7049, 501, null, new int[]{7050, 7051}, "2", "No", -1));
        //Reason
        questions.add(new Question(false, beforeCircumcisionId, 7050, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "If yes, reason?", ParamNames.HOSPITALIZATION_REASON, alpha150DigitSpace));
        //Duration
        questions.add(new Question(false, beforeCircumcisionId, 7051, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "If yes, duration?", ParamNames.HOSPITALIZATION_DURATION, alpha150DigitSpace));
        //Vitamin K
        questions.add(new Question(true, beforeCircumcisionId, 7052, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Vitamin K given to the baby after birth?", ParamNames.VITAMIN_K_AFTER_BIRTH, numeric3DigitMin1));
        options.add(new Option(7052, 500, null, new int[]{7053}, "1", "Yes, at Indus Hospital", -1));
        options.add(new Option(7052, 501, new int[]{7053}, null, "2", "Yes, at other facility", -1));
        options.add(new Option(7052, 501, new int[]{7053}, null, "2", "No", -1));
        options.add(new Option(7052, 501, new int[]{7053}, null, "99", "Don't know", -1));
        //Vitamin K
        questions.add(new Question(true, beforeCircumcisionId, 7053, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Vit K administered at CC clinic at the time of  circumcision?", ParamNames.VITAMIN_K_CIRCUMCISION, numeric3DigitMin1));
        options.add(new Option(7053, 500, null, new int[]{7054}, "1", "Yes", -1));
        // options.add(new Option(7053, 501, null, new int[]{7054}, "2", "No", -1));
        options.add(new Option(7053, 501, new int[]{7054}, null, "66", "Other", -1));
        options.add(new Option(7053, 501, null, new int[]{7054}, "88", "Refused", -1));
        //Vitamin K Other
        questions.add(new Question(false, beforeCircumcisionId, 7054, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Details of the Other option", ParamNames.VITAMIN_K_CIRCUMCISION_OTHER, alpha50DigitSpace));
        //Family h/o  bleeding disorders
        questions.add(new Question(true, beforeCircumcisionId, 7055, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Family h/o  bleeding disorders (e.g haemophilia)", ParamNames.FAMILY_HO_BLEEDING_DISORDERS, numeric3DigitMin1));
        options.add(new Option(7055, 500, new int[]{7086}, null, "1", "Yes", -1));
        options.add(new Option(7055, 501, null, new int[]{7086}, "2", "No", -1));

        // options.add(new Option(7055, 501, null, null, "99", "Don't know", -1));
        //Vitamin K Other
        questions.add(new Question(false, beforeCircumcisionId, 7056, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Any other (Specify):", ParamNames.FAMILY_HO_BLEEDING_DISORDERS_OTHER, alpha100DigitSpace));
        //Heading Physical Examination
        questions.add(new Question(false, beforeCircumcisionId, 7996, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Physical examination :", null, null));
        //Temprature
        questions.add(new Question(true, beforeCircumcisionId, 7056, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Temperature (Axillary: 97.5*F-99.3*F; 36.5*C- 37.4*C)", ParamNames.TEMPERATURE, alpha5DigitSpace));
        //Temperature normal?
        questions.add(new Question(true, beforeCircumcisionId, 7057, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Temperature normal?", ParamNames.TEMPERATURE_NORMAL, numeric3DigitMin1));
        options.add(new Option(7057, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7057, 501, null, null, "2", "No", -1));
        //Pulse
        questions.add(new Question(true, beforeCircumcisionId, 7058, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Pulse (90-160 beats per minute)", ParamNames.PULSE, numeric3DigitMin1));
        //Temperature normal?
        questions.add(new Question(true, beforeCircumcisionId, 7059, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Pulse normal?", ParamNames.PULSE_NORMAL, numeric3DigitMin1));
        options.add(new Option(7059, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7059, 501, null, null, "2", "No", -1));
        //Respiratory
        questions.add(new Question(true, beforeCircumcisionId, 7060, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Respiratory rate (30-60 breaths per miute)", ParamNames.RESPIRATORY, numeric2Digit));
        //Respiratory rate normal? normal?
        questions.add(new Question(true, beforeCircumcisionId, 7061, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Respiratory rate normal?", ParamNames.RESPIRATORY_NORMAL, numeric3DigitMin1));
        options.add(new Option(7061, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7061, 501, null, null, "2", "No", -1));
        //Jaundice
        questions.add(new Question(true, beforeCircumcisionId, 7062, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Jaundice", ParamNames.JAUNDICE, numeric3DigitMin1));
        options.add(new Option(7062, 500, new int[]{7063, 7064}, null, "1", "Yes", -1));
        options.add(new Option(7062, 501, null, new int[]{7063, 7064}, "2", "No", -1));
        //type of jaundice
        questions.add(new Question(true, beforeCircumcisionId, 7063, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.GONE, Validation.CHECK_FOR_EMPTY,
                "If yes, type of jaundice?", ParamNames.TYPE_OF_JAUNDICE, numeric3DigitMin1));
        options.add(new Option(7063, 500, null, null, "1", "Physiological", -1));
        options.add(new Option(7063, 501, null, null, "2", "Pathological ", -1));
        //severity of jaundice
        questions.add(new Question(true, beforeCircumcisionId, 7064, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.GONE, Validation.CHECK_FOR_EMPTY,
                "If yes, severity of jaundice?", ParamNames.SEVERITY_OF_JAUNDICE, numeric3DigitMin1));
        options.add(new Option(7064, 500, null, null, "1", "Mild", -1));
        options.add(new Option(7064, 501, null, null, "2", "Moderate", -1));
        options.add(new Option(7064, 501, null, null, "3", " Severe", -1));
        //Age of the baby
        questions.add(new Question(true, beforeCircumcisionId, 7065, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Age of the baby in days", ParamNames.AGE_OF_BABY, numeric2Digit));
        //Age of baby </= 60 days
        questions.add(new Question(true, beforeCircumcisionId, 7066, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Age of baby </= 60 days", ParamNames.AGE_OF_BABY_DAYS, numeric3DigitMin1));
        options.add(new Option(7066, 500, null, new int[]{7067}, "1", "Yes", -1));
        options.add(new Option(7066, 501, new int[]{7067}, null, "2", "No", -1));
        //Notes
        questions.add(new Question(false, beforeCircumcisionId, 7067, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Notes", ParamNames.AGE_NOTES, alpha150DigitSpace));
        //Weight of baby in kg
        questions.add(new Question(true, beforeCircumcisionId, 7068, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Weight of baby in kg", ParamNames.WEIGHT_OF_BABY, numeric3DigitMin1));
        //Weight of baby >/= 2.5 kg
        questions.add(new Question(true, beforeCircumcisionId, 7069, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Weight of baby >/= 2.5 kg", ParamNames.WEIGHT_OF_BABY_KG, numeric3DigitMin1));
        options.add(new Option(7069, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7069, 501, null, null, "2", "No", -1));
        //Any other abnormal findings:
        questions.add(new Question(true, beforeCircumcisionId, 7070, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Any other abnormal findings:", ParamNames.PHYSICAL_ANY_OTHER_ABNORMAL_FINDINGS, alpha50DigitSpace));
        //Heading Genital Examination:
        questions.add(new Question(false, beforeCircumcisionId, 7996, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Genital Examination:", null, null));
        //Hypospadias
        questions.add(new Question(true, beforeCircumcisionId, 7071, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Hypospadias", ParamNames.HYPOSPADIAS, numeric3DigitMin1));
        options.add(new Option(7071, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7071, 501, null, null, "2", "No", -1));
        //Epispadias
        questions.add(new Question(true, beforeCircumcisionId, 7072, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Epispadias", ParamNames.EPISPADIAS, numeric3DigitMin1));
        options.add(new Option(7072, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7072, 501, null, null, "2", "No", -1));
        //Congenital chordee
        questions.add(new Question(true, beforeCircumcisionId, 7073, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Congenital chordee", ParamNames.CONGENITAL_CHORDEE, numeric3DigitMin1));
        options.add(new Option(7073, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7073, 501, null, null, "2", "No", -1));
        //Micropenis
        questions.add(new Question(true, beforeCircumcisionId, 7074, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Micropenis", ParamNames.MICROPENIS, numeric3DigitMin1));
        options.add(new Option(7074, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7074, 501, null, null, "2", "No", -1));
        //Ambiguous genitalia
        questions.add(new Question(true, beforeCircumcisionId, 7075, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Ambiguous genitalia", ParamNames.AMBIGUOUS_GENITALIA, numeric3DigitMin1));
        options.add(new Option(7075, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7075, 501, null, null, "2", "No", -1));
        //Penoscrotal web
        questions.add(new Question(true, beforeCircumcisionId, 7076, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Penoscrotal web", ParamNames.PENOSCROTAL_WEB, numeric3DigitMin1));
        options.add(new Option(7076, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7076, 501, null, null, "2", "No", -1));
        //Congenital buried penis
        questions.add(new Question(true, beforeCircumcisionId, 7077, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Congenital buried penis", ParamNames.CONGENITAL_BURIED_PENIS, numeric3DigitMin1));
        options.add(new Option(7077, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7077, 501, null, null, "2", "No", -1));
        //Bilateral Hydroceles
        questions.add(new Question(true, beforeCircumcisionId, 7078, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Bilateral Hydroceles", ParamNames.BILATERAL_HYDROCELES, numeric3DigitMin1));
        options.add(new Option(7078, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7078, 501, null, null, "2", "No", -1));
        //Bilateral undescended testes
        questions.add(new Question(true, beforeCircumcisionId, 7079, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Bilateral undescended testes", ParamNames.BILATERAL_UNDESCENDED_TESTES, numeric3DigitMin1));
        options.add(new Option(7079, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7079, 501, null, null, "2", "No", -1));
        //Any other abnormal findings:
        questions.add(new Question(true, beforeCircumcisionId, 7080, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Any other abnormal findings:", ParamNames.GENITAL_ANY_OTHER_ABNORMAL_FINDINGS, alpha50DigitSpace));
        //Heading Suitability / Consent:
        questions.add(new Question(false, beforeCircumcisionId, 7995, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Suitability / Consent:", null, null));
        //suitable for circumcision
        questions.add(new Question(true, beforeCircumcisionId, 7081, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Is the baby suitable for circumcision at this clinic?", ParamNames.SUITABLE_FOR_CIRCUMCISION, numeric3DigitMin1));
        options.add(new Option(7081, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7081, 501, null, null, "2", "No", -1));
        // baby's parents/guardians given informed consent
        questions.add(new Question(true, beforeCircumcisionId, 7082, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Have the baby's parents/guardians given informed consent?", ParamNames.PARENTS_GIVEN_INFORMED_CONSENT, numeric3DigitMin1));
        options.add(new Option(7082, 500, null, null, "1", "Yes", -1));
        options.add(new Option(7082, 501, null, null, "2", "No", -1));
        //Name of the health worker
        questions.add(new Question(true, beforeCircumcisionId, 7083, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Name of the health worker", ParamNames.NAME_OF_THE_HEALTH_WORKER, alpha50DigitSpace));
    }

    private void initAfterCircumcision() {
        Integer afterCircumcisionId = 8;
        //Heading Basic Information
        questions.add(new Question(false, afterCircumcisionId, 8999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Basic Information", null, null));
        //MR No
        questions.add(new Question(true, afterCircumcisionId, 8000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_MRNO,
                "MR Number", MR_NUMBER, numeric12Digit));
        // this.options.add(new Option(8000, 90, null, null, "", "Enter MR No", -1));
        //Patient Id
        questions.add(new Question(true, afterCircumcisionId, 8001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Patient ID", ParamNames.PATIENT_ID, alpha7DigitSpace));
        //Patient Name
        questions.add(new Question(true, afterCircumcisionId, 8002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Patient Name (CAPITAL LETTERS)", PATIENT_NAME, alpha50DigitSpaceCapsOnly));
        //Heading Procedure: (To be filled by health provider)
        questions.add(new Question(false, afterCircumcisionId, 8998, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Procedure: (To be filled by health provider)", null, null));
        //Genital Screening (Normal)?
        questions.add(new Question(true, afterCircumcisionId, 8003, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Genital Screening (Normal)?", ParamNames.GENITAL_SCREENING, numeric3DigitMin1));
        options.add(new Option(8003, 500, null, null, "1", "Yes", -1));
        options.add(new Option(8003, 501, null, null, "2", "No", -1));
        //Anesthesia (Dorsal Penile Nerve Block) given?
        questions.add(new Question(true, afterCircumcisionId, 8004, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Anesthesia (Dorsal Penile Nerve Block) given?", ParamNames.ANESTHESIA, numeric3DigitMin1));
        options.add(new Option(8004, 500, null, null, "1", "Yes", -1));
        options.add(new Option(8004, 501, null, null, "2", "No", -1));
        //Plastibell
        questions.add(new Question(true, afterCircumcisionId, 8005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Plastibell device size used", ParamNames.PLASTIBELL_DEVICE_SIZE, numeric3DigitMin1));
        //Hemostasis
        questions.add(new Question(true, afterCircumcisionId, 8006, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Hemostasis was obtained using:", ParamNames.HEMOSTASIS, numeric3DigitMin1));
        options.add(new Option(8006, 500, null, null, "1", "No intervention", -1));
        options.add(new Option(8006, 501, null, null, "2", "Direct pressure with gauze", -1));
        options.add(new Option(8006, 500, null, null, "3", "Direct pressure with gauze sprayed with adrenaline", -1));
        options.add(new Option(8006, 501, null, null, "4", "Suture", -1));
        options.add(new Option(8006, 501, null, null, "5", "Others", -1));
        //Anesthesia (Dorsal Penile Nerve Block) given?
        questions.add(new Question(true, afterCircumcisionId, 8007, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Baby tolerated procedure well?", ParamNames.BABY_TOLERATED, numeric3DigitMin1));
        options.add(new Option(8007, 500, null, null, "1", "Yes", -1));
        options.add(new Option(8007, 501, null, null, "2", "No", -1));
        //Complications
        questions.add(new Question(true, afterCircumcisionId, 8008, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Complications were seen during the procedure?", ParamNames.COMPLICATIONS, numeric3DigitMin1));
        options.add(new Option(8008, 500, new int[]{8009}, null, "1", "Yes", -1));
        options.add(new Option(8008, 501, null, new int[]{8009}, "2", "No", -1));
        //which complications
        questions.add(new Question(true, afterCircumcisionId, 8009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "If yes, state which complications and how they were handled?", ParamNames.WHICH_COMPLICATIONS, alpha100DigitSpace));
        //Blood loss was minimal?
        questions.add(new Question(true, afterCircumcisionId, 8010, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Blood loss was minimal?", ParamNames.BLOOD_LOSS, numeric3DigitMin1));
        options.add(new Option(8010, 500, null, null, "1", "Yes", -1));
        options.add(new Option(8010, 501, null, null, "2", "No", -1));
        //NAPA suppository
        questions.add(new Question(true, afterCircumcisionId, 8011, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "NAPA suppository 125mg given?", ParamNames.NAPA_SUPPOSITORY, numeric3DigitMin1));
        options.add(new Option(8011, 500, null, null, "1", "Yes", -1));
        options.add(new Option(8011, 501, null, null, "2", "No", -1));
        //Heading Post operative care: (To be filled by health provider)
        questions.add(new Question(false, afterCircumcisionId, 8997, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Post operative care: (To be filled by health provider)", null, null));
        //stable condition
        questions.add(new Question(true, afterCircumcisionId, 8012, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Baby returned to parent/guardian in stable condition?", ParamNames.STABLE_CONDITION, numeric3DigitMin1));
        options.add(new Option(8012, 500, null, null, "1", "Yes", -1));
        options.add(new Option(8012, 501, null, null, "2", "No", -1));
        //care instructions
        questions.add(new Question(true, afterCircumcisionId, 8013, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Postoperative care instructions provided to parent/guardian?", ParamNames.CARE_INSTRUCTIONS, numeric3DigitMin1));
        options.add(new Option(8013, 500, null, null, "1", "Yes", -1));
        options.add(new Option(8013, 501, null, null, "2", "No", -1));
        //contact information
        questions.add(new Question(true, afterCircumcisionId, 8014, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Emergency contact information provided to parent/guardian?", ParamNames.CONTACT_INFORMATION, numeric3DigitMin1));
        options.add(new Option(8014, 500, null, null, "1", "Yes", -1));
        options.add(new Option(8014, 501, null, null, "2", "No", -1));
        //Heading Name of Health Provider:
        questions.add(new Question(false, afterCircumcisionId, 8996, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Name of Health Provider:", null, null));
        //Name of the health provider
        questions.add(new Question(true, afterCircumcisionId, 8015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Name of the health provider", ParamNames.NAME_OF_THE_HEALTH_PROVIDER, alpha50DigitSpace));
    }

    private void initAfterCircumcision2() {
        Integer afterCircumcision2Id = 9;
        //Heading Basic Information
        questions.add(new Question(false, afterCircumcision2Id, 9999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Basic Information", null, null));
        //MR No
        questions.add(new Question(true, afterCircumcision2Id, 9000, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_MRNO,
                "MR Number", MR_NUMBER, numeric12Digit));
        // this.options.add(new Option(8000, 90, null, null, "", "Enter MR No", -1));
        //Patient Id
        questions.add(new Question(true, afterCircumcision2Id, 9001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Patient ID", ParamNames.PATIENT_ID, alpha7DigitSpace));
        //Patient Name
        questions.add(new Question(true, afterCircumcision2Id, 9002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Patient Name (CAPITAL LETTERS)", PATIENT_NAME, alpha50DigitSpaceCapsOnly));
        //Heading Procedure: (To be filled by health provider)
        questions.add(new Question(false, afterCircumcision2Id, 9998, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Follow up documentation: Call on Day 1:", null, null));
        //Contact established
        questions.add(new Question(true, afterCircumcision2Id, 9003, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Contact established?", ParamNames.CONTACT_ESTABLISHED, numeric3DigitMin1));
        options.add(new Option(9003, 500, new int[]{9004}, null, "1", "Yes", -1));
        options.add(new Option(9003, 501, null, new int[]{9004}, "2", "No (Tried 3 times + 1 SMS)", -1));
        //General well being of baby
        questions.add(new Question(true, afterCircumcision2Id, 9003, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW, View.GONE, Validation.CHECK_FOR_EMPTY,
                "General well being of baby", ParamNames.GENERAL_WELL_BEING_OF_BABY, numeric3DigitMin1));
        options.add(new Option(9003, 500, new int[]{9004}, null, "1", "Comfortable and healthy", -1));
        options.add(new Option(9003, 501, null, new int[]{9004}, "2", "Uncomfortable", -1));
    }

    //PONSETI FORMS
    private void initDemographicInformation() {
        Integer demographicInformationId = 11;
        //location
        questions.add(new Question(true, demographicInformationId, 11060, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Program Location", null, numeric3DigitMin1));
        options.add(new Option(11060, 500, null, null, "", "" + GlobalPreferences.getinstance(context).findLocationPrferenceValue(), -1));
        //Patient’s ID (for karachi)
        questions.add(new Question(true, demographicInformationId, 11000, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_IDENTIFIER, View.VISIBLE, Validation.CHECK_FOR_PATIENT_ID_KARACHI,
                "Patient’s ID", null, alphanumeric10DigitWithHypen));
        this.options.add(new Option(11000, 90, null, null, "", "NOT ALLOWD TO EDIT", -1));
        //Participant’s MR no
       /* questions.add(new Question(true, demographicInformationId, 11001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Participant’s MR no", MR_NUMBER, numeric11Digit));*/
        //Date Called
        questions.add(new Question(true, demographicInformationId, 11002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Date of registration", ParamNames.FORM_DATE, dateMinTodayMaxLastMonday));
        questions.add(new Question(true, demographicInformationId, 11003, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Has the patient given consent to participate in the study?", ParamNames.GIVEN_CONSENT, numeric3DigitMin1));
        options.add(new Option(11003, 338, null, new int[]{11004}, "", "<Select an option>", -1));
        options.add(new Option(11003, 500, new int[]{11004}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11003, 501, null, new int[]{11004}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        questions.add(new Question(true, demographicInformationId, 11004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Has the address card been filled?", ParamNames.ADDRESS_CARD, numeric3DigitMin1));
        options.add(new Option(11004, 338, null, new int[]{11005, 11006, 11007, 11008, 11010, 11011, 11013, 11015, 11017, 11019, 11020, 11021, 11022, 11023, 11025, 11027, 11029, 11031, 11032, 11033, 11035, 11036, 11038, 11040, 11042, 11043, 11044, 11048, 11050, 11052, 11053, 11054, 11055, 11056}, "", "<Select an option>", -1));
        options.add(new Option(11004, 500, new int[]{11005, 11006, 11007, 11008, 11010, 11011, 11013, 11015, 11017, 11019, 11020, 11021, 11022, 11023, 11025, 11027, 11029, 11031, 11032, 11033, 11035, 11036, 11038, 11040, 11042, 11043, 11044, 11048, 11050, 11052, 11053, 11054, 11055, 11056}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11004, 501, null, new int[]{11005, 11006, 11007, 11008, 11010, 11011, 11013, 11015, 11017, 11019, 11020, 11021, 11022, 11023, 11025, 11027, 11029, 11031, 11032, 11033, 11035, 11036, 11038, 11040, 11042, 11043, 11044, 11048, 11050, 11052, 11053, 11054, 11055, 11056}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        questions.add(new Question(true, demographicInformationId, 11053, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Hospital name", null, numeric3DigitMin1));
        options.add(new Option(11053, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(11053, 500, null, null, "", "Indus Hospital-Karachi", -1));
        options.add(new Option(11053, 501, null, null, "", "Indus Hospital TEH Muzaffargarh", -1));
        options.add(new Option(11053, 500, null, null, "", "Indus Hospital Manawan", -1));
        options.add(new Option(11053, 500, null, null, "", "Indus Hospital-Badin", -1));
        questions.add(new Question(true, demographicInformationId, 11005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_MOBILE_NUMBER,
                "Mobile number 1", ParamNames.PRE_OP_CONTACT1, numeric12DigitWithHypen));
        this.options.add(new Option(11005, 90, null, null, "", "03xx-xxxxxxx", -1));
        questions.add(new Question(true, demographicInformationId, 11006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_MOBILE_NUMBER,
                "Mobile number 2", ParamNames.PRE_OP_CONTACT2, numeric12DigitWithHypen));
        this.options.add(new Option(11006, 90, null, null, "", "03xx-xxxxxxx", -1));
        questions.add(new Question(false, demographicInformationId, 11054, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "PTCL number", ParamNames.PTCL_NUMBER, numeric11Digit));
        //this.questions.add(new Question(true, demographicInformationId, 11007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_AGE, View.VISIBLE, Validation.CHECK_FOR_DATE_TIME, "Participant’s date of birth", ParamNames.DATE_OF_BIRTH, dob));
        questions.add(new Question(true, demographicInformationId, 11008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Where was the participant born?", ParamNames.PARTICIPANT_BORN, numeric3DigitMin1));
        options.add(new Option(11008, 338, null, new int[]{11009, 110101, 11010}, "", "<Select an option>", -1));
        options.add(new Option(11008, 500, new int[]{11010}, new int[]{11009, 110101}, "", "Karachi", -1));
        options.add(new Option(11008, 501, new int[]{110101}, new int[]{11009, 11010}, "", "Islamabad", -1));
        options.add(new Option(11008, 500, new int[]{110101}, new int[]{11009, 11010}, "", "FATA", -1));
        options.add(new Option(11008, 501, new int[]{110101}, new int[]{11009, 11010}, "", " Kashmir", -1));
        options.add(new Option(11008, 501, new int[]{110101}, new int[]{11009, 11010}, "", "Sindh (not Karachi)", -1));
        options.add(new Option(11008, 501, new int[]{110101}, new int[]{11009, 11010}, "", "Khyber Paktunkhwa", -1));
        options.add(new Option(11008, 501, new int[]{110101}, new int[]{11009, 11010}, "", "Gilgit-Baltistan", -1));
        options.add(new Option(11008, 501, new int[]{110101}, new int[]{11009, 11010}, "", "Balochistan", -1));
        options.add(new Option(11008, 501, new int[]{110101}, new int[]{11009, 11010}, "", "Punjab", -1));
        options.add(new Option(11008, 501, new int[]{11009, 110100}, new int[]{11010}, "", "Other", -1));
        options.add(new Option(11008, 501, new int[]{110101}, new int[]{11009, 11010}, "", "Don't Know", -1));
        options.add(new Option(11008, 501, new int[]{110101}, new int[]{11009, 11010}, "", "Refused", -1));
        this.questions.add(new Question(false, demographicInformationId, 11009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Other", ParamNames.BORN_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Participant’s area (town) of residence", ParamNames.PARTICIPANT_RESIDENCE_AREA, numeric3DigitMin1));
        options.add(new Option(11010, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(11010, 500, null, null, "", "Korangi", -1));
        options.add(new Option(11010, 501, null, null, "", "Landhi", -1));
        options.add(new Option(11010, 500, null, null, "", "Bin Qasim", -1));
        options.add(new Option(11010, 501, null, null, "", "Shah Faisal", -1));
        options.add(new Option(11010, 501, null, null, "", "Korangi-Creek Cantt", -1));
        options.add(new Option(11010, 501, null, null, "", "Jamshed", -1));
        options.add(new Option(11010, 501, null, null, "", "Malir", -1));
        options.add(new Option(11010, 501, null, null, "", "Baldia", -1));
        options.add(new Option(11010, 501, null, null, "", "SITE", -1));
        options.add(new Option(11010, 501, null, null, "", "Orangi", -1));
        options.add(new Option(11010, 501, null, null, "", "Kemari", -1));
        options.add(new Option(11010, 501, null, null, "", "Saddar", -1));
        options.add(new Option(11010, 501, null, null, "", "Lyari Town", -1));
        options.add(new Option(11010, 501, null, null, "", "Liaquatabad", -1));
        options.add(new Option(11010, 501, null, null, "", "New Karachi", -1));
        options.add(new Option(11010, 501, null, null, "", "Gulberg", -1));
        options.add(new Option(11010, 501, null, null, "", "Gulshan", -1));
        options.add(new Option(11010, 501, null, null, "", "North Nazimabad", -1));
        options.add(new Option(11010, 501, null, null, "", "Gadap Town", -1));
        options.add(new Option(11010, 501, null, null, "", "Malir cantt", -1));
        options.add(new Option(11010, 501, null, null, "", "Manora Cantt", -1));
        options.add(new Option(11010, 501, null, null, "", "Faisal Cantt", -1));
        options.add(new Option(11010, 501, null, null, "", "Clifton Cantt", -1));
        options.add(new Option(11010, 501, null, null, "", "Karachi Cantt", -1));

        /*questions.add(new Question(true, demographicInformationId, 110100, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Participant’s area (town) of residence", ParamNames.PARTICIPANT_RESIDENCE_AREA, numeric3DigitMin1));
        options.add(new Option(110100, 501, null, new int[]{110101}, "", "<Select an option>", -1));
        options.add(new Option(110100, 501, new int[]{110101}, null, "", "Out of karachi", -1));*/
        this.questions.add(new Question(false, demographicInformationId, 110101, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY, "Which town?", ParamNames.BORN_OTHER, alphanumeric100DigitSpace));

        //Ethnicity
        questions.add(new Question(true, demographicInformationId, 11011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "What is the ethnicity of the participant?", ParamNames.ETHNICITY, numeric3DigitMin1));
        options.add(new Option(11011, 338, null, new int[]{11012}, "", "<Select an option>", -1));
        options.add(new Option(11011, 500, null, new int[]{11012}, "163219AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Muhajir", -1));
        options.add(new Option(11011, 501, null, new int[]{11012}, "34e0c006-0379-4870-98b2-2e5de971e713", "Sindhi", -1));
        options.add(new Option(11011, 500, null, new int[]{11012}, "55baf400-6fb4-4a23-97d8-e90bbc500d93", "Bengali", -1));
        options.add(new Option(11011, 501, null, new int[]{11012}, "163216AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Pashtun", -1));
        options.add(new Option(11011, 501, null, new int[]{11012}, "e443f97b-a24c-4aac-a352-6fdc52103a1b", "Siraiki", -1));
        options.add(new Option(11011, 500, null, new int[]{11012}, "3574e507-5b17-4d1f-acd9-405d10f49487", "Balochi", -1));
        options.add(new Option(11011, 501, null, new int[]{11012}, "163215AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Punjabi", -1));
        options.add(new Option(11011, 501, null, new int[]{11012}, "3cc6560b-0873-46a3-95b8-1399cd8a6b60", "Refused", -1));
        options.add(new Option(11011, 501, null, new int[]{11012}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        options.add(new Option(11011, 501, new int[]{11012}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other", -1));
        //Other Ethnicity
        questions.add(new Question(false, demographicInformationId, 11012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.ETHNICITY_OTHER, alphanumeric100DigitSpace));
        //Education
        questions.add(new Question(true, demographicInformationId, 11013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "What is the highest level of education or last grade level completed by the head of household?", ParamNames.HIGHEST_EDUCATION, numeric3DigitMin1));
        options.add(new Option(11013, 338, null, new int[]{11014}, "", "<Select an option>", -1));
        options.add(new Option(11013, 500, null, new int[]{11014}, "1107AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No school education", -1));
        options.add(new Option(11013, 501, null, new int[]{11014}, "3124348d-45ed-41f8-b9c0-b277ae12aa0f", "Madrassa", -1));
        options.add(new Option(11013, 500, null, new int[]{11014}, "1713AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Primary (Class 1-5)", -1));
        options.add(new Option(11013, 501, null, new int[]{11014}, "1714AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Secondary (6-8)", -1));
        options.add(new Option(11013, 500, null, new int[]{11014}, "0ec3fed9-92f1-4b80-9528-002a81534007", "Matriculation (9-10)", -1));
        options.add(new Option(11013, 501, null, new int[]{11014}, "e93a77c4-85f8-4d94-966e-3af90196f58a", "Intermediate (Class 11-12)", -1));
        options.add(new Option(11013, 501, null, new int[]{11014}, "9e012dd2-d7e8-44e0-82f6-2dfe604a3344", "University", -1));
        options.add(new Option(11013, 501, new int[]{11014}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other", -1));
        options.add(new Option(11013, 501, null, new int[]{11014}, "3cc6560b-0873-46a3-95b8-1399cd8a6b60", "Refused", -1));
        options.add(new Option(11013, 501, null, new int[]{11014}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        //Education Other
        questions.add(new Question(false, demographicInformationId, 11014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.HIGHEST_EDUCATION_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Is the head of household currently working?", ParamNames.HOUSEHOLD_CURRENTLY_WORKING, numeric3DigitMin1));
        options.add(new Option(11015, 338, null, new int[]{11016}, "", "<Select an option>", -1));
        options.add(new Option(11015, 500, null, new int[]{11016}, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11015, 501, null, new int[]{11016}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        options.add(new Option(11015, 500, new int[]{11016}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        options.add(new Option(11015, 501, null, new int[]{11016}, "3cc6560b-0873-46a3-95b8-1399cd8a6b60", "Refused", -1));
        questions.add(new Question(false, demographicInformationId, 11016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.HOUSEHOLD_CURRENTLY_WORKING_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "What is or was the occupation of the head of household?", ParamNames.OCCUPATION_OF_HEAD_OF_HOUSEHOLD, numeric3DigitMin1));
        options.add(new Option(11017, 338, null, new int[]{11018}, "", "<Select an option>", -1));
        options.add(new Option(11017, 500, null, new int[]{11018}, "ab0c8716-9942-4c87-8a49-f9b83f880947", "Healthcare", -1));
        options.add(new Option(11017, 501, null, new int[]{11018}, "fa59921d-e243-4b3c-af82-2380c0f89843", "Tajir", -1));
        options.add(new Option(11017, 500, null, new int[]{11018}, "8d4bdecb-4ed2-49f2-9d83-ff3260d37a81", "Laborer", -1));
        options.add(new Option(11017, 501, null, new int[]{11018}, "d235ab7d-fb98-4ae3-86f4-8508fec21056", "Factory worker", -1));
        options.add(new Option(11017, 500, null, new int[]{11018}, "5b4eb04c-758f-496e-a002-63324c07ab97", "Skilled laborer", -1));
        options.add(new Option(11017, 501, null, new int[]{11018}, "8c1667e4-3ec1-4bfb-a3bb-8e81cb8efe7f", "Agriculture/fishery work", -1));
        options.add(new Option(11017, 500, new int[]{11018}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        options.add(new Option(11017, 501, null, new int[]{11018}, "3cc6560b-0873-46a3-95b8-1399cd8a6b60", "Refused", -1));
        options.add(new Option(11017, 501, null, new int[]{11018}, "1175AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Not applicable", -1));
        options.add(new Option(11017, 501, null, new int[]{11018}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(false, demographicInformationId, 11018, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.OCCUPATION_OF_HEAD_OF_HOUSEHOLD_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11019, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Can you please provide us with an estimate of your household income?", ParamNames.HOUSEHOLD_INCOME, numeric6Digit));
        // options.add(new Option(11019, 500,  new int[]{11020},null, "", "", -1));
        options.add(new Option(11019, 500, null, new int[]{11020}, "99", "Don't Know", -1));
        options.add(new Option(11019, 501, null, new int[]{11020}, "88", "Refused", -1));
        questions.add(new Question(true, demographicInformationId, 11020, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "How often is this income earned?", ParamNames.INCOME_EARNED, numeric3DigitMin1));
        options.add(new Option(11020, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(11020, 500, null, null, "5924f2d4-c186-43f7-bdba-b437ce9506a7", "Daily wage", -1));
        options.add(new Option(11020, 501, null, null, "1099AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Weekly", -1));
        options.add(new Option(11020, 500, null, null, "1098AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Monthly", -1));
        options.add(new Option(11020, 500, null, null, "163332AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yearly", -1));
        options.add(new Option(11020, 501, null, null, "1175AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Not applicable", -1));
        options.add(new Option(11020, 501, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        options.add(new Option(11020, 501, null, null, "3cc6560b-0873-46a3-95b8-1399cd8a6b60", "Refused", -1));

        /*questions.add(new Question(false, demographicInformationId, 11059, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.OCCUPATION_OF_HEAD_OF_HOUSEHOLD_OTHER, alphanumeric100DigitSpace));*/
        questions.add(new Question(true, demographicInformationId, 11055, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Including yourself how many people live in your household? (Above 18 years of age)", ParamNames.PEOPLE_ABOVE_18, numeric2Digit));
        options.add(new Option(11055, 500, null, null, "99", "Don't Know", -1));
        options.add(new Option(11055, 501, null, null, "88", "Refused", -1));
        questions.add(new Question(true, demographicInformationId, 11056, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "How many people live in your household? (Below 18 years of age)", ParamNames.PEOPLE_BELOW_18, numeric2Digit));
        options.add(new Option(11056, 500, null, null, "99", "Don't Know", -1));
        options.add(new Option(11056, 501, null, null, "88", "Refused", -1));
        questions.add(new Question(true, demographicInformationId, 11021, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "How many rooms do you have in your house (not including bathrooms)?", ParamNames.PRE_OP_NO_ROOMS, numeric2Digit));
        options.add(new Option(11021, 500, null, null, "99", "Don't Know", -1));
        options.add(new Option(11021, 501, null, null, "88", "Refused", -1));
        questions.add(new Question(true, demographicInformationId, 11022, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "How many bathrooms?", ParamNames.NO_OF_BATHROOMS, numeric2Digit));
        options.add(new Option(11022, 500, null, null, "99", "Don't Know", -1));
        options.add(new Option(11022, 501, null, null, "88", "Refused", -1));
        questions.add(new Question(true, demographicInformationId, 11023, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Do you have a separate room for your kitchen?", ParamNames.SEPRATE_KITCHEN, numeric3DigitMin1));
        options.add(new Option(11023, 338, null, new int[]{11024}, "", "<Select an option>", -1));
        options.add(new Option(11023, 500, null, new int[]{11024}, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11023, 501, null, new int[]{11024}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        options.add(new Option(11023, 501, new int[]{11024}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other", -1));
        options.add(new Option(11023, 500, null, new int[]{11024}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        options.add(new Option(11023, 501, null, new int[]{11024}, "3cc6560b-0873-46a3-95b8-1399cd8a6b60", "Refused", -1));
        questions.add(new Question(false, demographicInformationId, 11024, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.SEPRATE_KITCHEN_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11025, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "What material do you use to cook?", ParamNames.MATERIAL_COOK, numeric3DigitMin1));
        options.add(new Option(11025, 338, null, new int[]{11026}, "", "<Select an option>", -1));
        options.add(new Option(11025, 500, null, new int[]{11026}, "04c146b9-c31d-4889-847b-cf8654c3a9c8", "Electric", -1));
        options.add(new Option(11025, 501, null, new int[]{11026}, "00d8b539-533e-4093-b142-46fb7beb61a7", "Coal", -1));
        options.add(new Option(11025, 500, null, new int[]{11026}, "162419AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Gas", -1));
        options.add(new Option(11025, 501, null, new int[]{11026}, "c5f47bcb-47a7-4e62-b531-efb764d99152", "Wood", -1));
        options.add(new Option(11025, 500, null, new int[]{11026}, "6143039c-120e-4784-880f-3ffb8b4a6a6a", "Animal dung", -1));
        options.add(new Option(11025, 501, null, new int[]{11026}, "b89e998c-0140-4f38-90be-c87a28014914", "Stove", -1));
        options.add(new Option(11025, 501, new int[]{11026}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other", -1));
        options.add(new Option(11025, 500, null, new int[]{11026}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        options.add(new Option(11025, 501, null, new int[]{11026}, "3cc6560b-0873-46a3-95b8-1399cd8a6b60", "Refused", -1));
        questions.add(new Question(false, demographicInformationId, 11026, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.MATERIAL_COOK_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Where was your child born (site)?", ParamNames.CHILD_BORN, numeric3DigitMin1));
        options.add(new Option(11027, 338, null, new int[]{11028}, "", "<Select an option>", -1));
        options.add(new Option(11027, 500, null, new int[]{11028}, "1589AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Hospital", -1));
        options.add(new Option(11027, 501, null, new int[]{11028}, "ca5c2b64-561a-4f20-88a9-d535538fa122", "Clinic", -1));
        options.add(new Option(11027, 500, null, new int[]{11028}, "1536AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Home", -1));
        options.add(new Option(11027, 501, new int[]{11028}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other", -1));
        options.add(new Option(11027, 500, null, new int[]{11028}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(false, demographicInformationId, 11028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.CHILD_BORN_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Did the mother have any complications during pregnancy?", ParamNames.COMPLICATIONS_DURING_PREGNANCY, numeric3DigitMin1));
        options.add(new Option(11029, 338, null, new int[]{11030}, "", "<Select an option>", -1));
        options.add(new Option(11029, 500, new int[]{11030}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11029, 501, null, new int[]{11030}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        options.add(new Option(11029, 501, null, new int[]{11030}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(true, demographicInformationId, 11030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What were the complications?", ParamNames.WHAT_COMPLICATIONS_DURING_PREGNANCY, alpha100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11031, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Did the mother consume alcohol during pregnancy?", ParamNames.CONSUME_ALCOHOL, numeric3DigitMin1));
        options.add(new Option(11031, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(11031, 500, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11031, 501, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        options.add(new Option(11031, 501, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(true, demographicInformationId, 11032, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Did the mother smoke during pregnancy?", ParamNames.SMOKE_DURING_PREGNANCY, numeric3DigitMin1));
        options.add(new Option(11032, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(11032, 500, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11032, 501, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        options.add(new Option(11032, 501, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(true, demographicInformationId, 11033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Any complications during birth?", ParamNames.COMPLICATIONS_DURING_BIRTH, numeric3DigitMin1));
        options.add(new Option(11033, 338, null, new int[]{11034}, "", "<Select an option>", -1));
        options.add(new Option(11033, 500, new int[]{11034}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11033, 501, null, new int[]{11034}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        options.add(new Option(11033, 501, null, new int[]{11034}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(true, demographicInformationId, 11034, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What were the complications?", ParamNames.WHAT_COMPLICATIONS_DURING_BIRTH, alpha100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11035, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Term of pregnancy?", ParamNames.TERM_OF_PREGNANCY, numeric3DigitMin1));
        options.add(new Option(11035, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(11035, 500, null, null, "14153374-39c8-431b-ac23-1fb68d2290bd", "Full term", -1));
        options.add(new Option(11035, 501, null, null, "1c969649-4a52-4403-9b8a-7d476c237061", "Pre Term", -1));
        options.add(new Option(11035, 501, null, null, "bed0088c-f41f-4921-93ab-910a25e042cd", "Post term", -1));
        options.add(new Option(11035, 501, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(true, demographicInformationId, 11036, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "What was your mode of delivery?", ParamNames.MODE_OF_DELIVERY, numeric3DigitMin1));
        options.add(new Option(11036, 338, null, new int[]{11037}, "", "<Select an option>", -1));
        options.add(new Option(11036, 500, null, new int[]{11037}, "93834a5f-ea9e-451d-a838-c8d0ccb83247", "Normal vaginal delivery", -1));
        options.add(new Option(11036, 501, null, new int[]{11037}, "3fb77fc0-4ec0-464c-8ba6-9f29050b9aec", "Delivery with instrumentation", -1));
        options.add(new Option(11036, 501, null, new int[]{11037}, "1171AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "C-section", -1));
        options.add(new Option(11036, 501, new int[]{11037}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        options.add(new Option(11036, 501, null, new int[]{11037}, "3cc6560b-0873-46a3-95b8-1399cd8a6b60", "Refused", -1));
        options.add(new Option(11036, 501, null, new int[]{11037}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(false, demographicInformationId, 11037, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.MODE_OF_DELIVERY_OTHER, alphanumeric100DigitSpace));

        questions.add(new Question(true, demographicInformationId, 11038, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Family history of clubfoot?", ParamNames.FAMILY_HISTORY_OF_CLUBFOOT, numeric3DigitMin1));
        options.add(new Option(11038, 500, null, null, "163729AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No family history", -1));
        options.add(new Option(11038, 501, null, null, "1527AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Parent", -1));
        options.add(new Option(11038, 501, null, null, "972AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Sibling", -1));
        options.add(new Option(11038, 500, null, null, "973AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Grandparent", -1));
        options.add(new Option(11038, 501, null, null, "e502a9b0-d5b4-4b93-9a59-8ba39799f75a", "First degree uncle", -1));
        options.add(new Option(11038, 501, null, null, "ccbae95e-c9a9-43ed-9dd5-0e21165fc555", "First degree aunt", -1));
        options.add(new Option(11038, 501, null, null, "c6b77f31-77f3-4eb4-94bb-291c7dbe37c3", "First degree cousin", -1));
        options.add(new Option(11038, 501, new int[]{11039}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        options.add(new Option(11038, 501, null, null, "3cc6560b-0873-46a3-95b8-1399cd8a6b60", "Refused", -1));
        options.add(new Option(11038, 501, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(false, demographicInformationId, 11039, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.FAMILY_HISTORY_OF_CLUBFOOT_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11040, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Relationship between parents?", ParamNames.RELATIONSHIP_BETWEEN_PARENTS, numeric3DigitMin1));
        options.add(new Option(11040, 500, null, new int[]{11041}, ""/*"1107AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"*/, "None", -1));
        options.add(new Option(11040, 501, null, new int[]{11041}, ""/*"ea146853-1687-49a2-9689-b91be467c0b0"*/, "First degree relatives", -1));
        options.add(new Option(11040, 501, null, new int[]{11041}, ""/*"5cfb06cc-e0c2-41be-807a-98c0bab95cf4"*/, "Second degree relatives", -1));
        options.add(new Option(11040, 500, null, new int[]{11041}, ""/*"96f57976-f07e-4864-8ca5-494509812536"*/, "same ethnic background", -1));
        options.add(new Option(11040, 501, new int[]{11041}, null, ""/*"5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"*/, "Other (open text)", -1));
        questions.add(new Question(false, demographicInformationId, 11041, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.RELATIONSHIP_BETWEEN_PARENTS_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11042, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Deformity present at birth?", ParamNames.DEFORMITY, numeric3DigitMin1));
        options.add(new Option(11042, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(11042, 500, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11042, 501, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        options.add(new Option(11042, 501, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(true, demographicInformationId, 11043, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Side of foot deformed?", ParamNames.DEFORMED_SIDE, numeric3DigitMin1));
        options.add(new Option(11043, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(11043, 500, null, null, "5141AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "right", -1));
        options.add(new Option(11043, 501, null, null, "5139AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "left", -1));
        options.add(new Option(11043, 501, null, null, "e60ad653-b0f8-44cd-a2a4-107577c50735", "both", -1));
        questions.add(new Question(true, demographicInformationId, 11044, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Any previous treatment?", ParamNames.PREVIOUS_TREATMENT, numeric3DigitMin1));
        options.add(new Option(11044, 338, null, new int[]{11045, 11047}, "", "<Select an option>", -1));
        options.add(new Option(11044, 500, new int[]{11045, 11047}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11044, 501, null, new int[]{11045, 11047}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        options.add(new Option(11044, 501, null, new int[]{11045, 11047}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(true, demographicInformationId, 11045, "", InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Treatment till date", ParamNames.TREATMENT_TILL_DATE, numeric3DigitMin1));
        options.add(new Option(11045, 500, null, null, "1107AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "None", -1));
        options.add(new Option(11045, 501, null, null, "d8bad2f2-044f-4046-b9b2-92f9623174fe", "Cast above knee", -1));
        options.add(new Option(11045, 501, null, null, "79ec52ed-a967-4e25-9510-d4abff3595f0", "Cast below knee", -1));
        options.add(new Option(11045, 500, null, null, "c0152ca6-b073-4a96-8d11-ccdd2b46038e", "Physiotherapy", -1));
        options.add(new Option(11045, 500, null, null, "7d082ff9-08d0-4553-9202-ea9a901fb057", "Manipulation", -1));
        options.add(new Option(11045, 501, null, null, "58867c36-aa41-441a-b921-cbceb861831e", "Surgical intervention", -1));
        options.add(new Option(11045, 501, null, null, "b2ba0b7f-d449-4df3-a7c3-7c91ff107083", "Home remedies", -1));
        options.add(new Option(11045, 500, null, null, "b1233a78-70ec-4cb4-957d-1ab0ef7f8564", "Hakeem", -1));
        options.add(new Option(11045, 500, null, null, "ef498532-4c0d-4c11-adfb-93f982222c19", "Spiritual", -1));
        options.add(new Option(11045, 501, null, null, "9064fff0-dc00-4d1c-86cd-934006df5174", "Traditional", -1));
        options.add(new Option(11045, 500, null, null, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        options.add(new Option(11045, 501, new int[]{11046}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        questions.add(new Question(false, demographicInformationId, 11046, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.TREATMENT_TILL_DATE_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11047, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Number of previous treatments", ParamNames.NO_PREVIOUS_TREATMENTS, numeric3DigitMin1));
        questions.add(new Question(true, demographicInformationId, 11048, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Diagnosed prenatally?", ParamNames.DIAGNOSED_PRENATALLY, numeric3DigitMin1));
        options.add(new Option(11048, 338, null, new int[]{11049}, "", "<Select an option>", -1));
        options.add(new Option(11048, 500, new int[]{11049}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(11048, 501, null, new int[]{11049}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        options.add(new Option(11048, 501, null, new int[]{11049}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(true, demographicInformationId, 11049, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Prenatal diagnosis at pregnancy week?", ParamNames.PRENATAL_DIAGNOSIS_WEEK, numeric3DigitMin1));
        questions.add(new Question(true, demographicInformationId, 11050, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "How did you find out about this Clubfoot Clinic?", ParamNames.CLUBFOOT_CLINIC, numeric3DigitMin1));
        options.add(new Option(11050, 338, null, new int[]{11051, 11057, 11058}, "", "<Select an option>", -1));
        options.add(new Option(11050, 500, null, new int[]{11051, 11057, 11058}, "6b251a92-c26f-43ce-9609-f5ca6291dfbe", "Lady health worker", -1));
        options.add(new Option(11050, 500, null, new int[]{11051, 11057, 11058}, "48705049-2e81-4b5b-af35-f05e8426f202", "Daya", -1));
        options.add(new Option(11050, 501, null, new int[]{11051, 11057, 11058}, "aa6ccc09-ab84-480c-8d63-e6952240b771", "Maternity home", -1));
        options.add(new Option(11050, 501, null, new int[]{11051, 11057, 11058}, "62978f2f-2ae0-453b-8373-54690ceb7011", "Vaccinator", -1));
        options.add(new Option(11050, 500, null, new int[]{11051, 11057, 11058}, "089bdd87-0cb7-4ea2-92ad-b6f8c88b8ae3", "Friend or relatives", -1));
        options.add(new Option(11050, 500, null, new int[]{11051, 11057, 11058}, "89891260-b473-4b1f-b9db-dd5e049d0d92", "Brochure", -1));
        options.add(new Option(11050, 501, null, new int[]{11051, 11057, 11058}, "6827d066-7394-47e3-b047-ea66b68b1889", "Banner", -1));
        options.add(new Option(11050, 500, null, new int[]{11051, 11057, 11058}, "8033f414-8a13-49d3-a2a9-2971bdcd52f4", "Poster", -1));
        options.add(new Option(11050, 501, null, new int[]{11051, 11057, 11058}, "76a03d7d-5bf1-4258-8fb3-9e5268ee8454", "Cable TV advertisement", -1));
        options.add(new Option(11050, 500, new int[]{11057, 11058}, new int[]{11051}, "1589AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Hospital", -1));
        options.add(new Option(11050, 501, new int[]{11051}, new int[]{11057, 11058}, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        questions.add(new Question(false, demographicInformationId, 11051, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option ?", ParamNames.CLUBFOOT_CLINIC_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(false, demographicInformationId, 11057, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Hospital's name", ParamNames.NAME_OF_HOSPITAL, alpha50DigitSpace));
        questions.add(new Question(false, demographicInformationId, 11058, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Doctor's name", ParamNames.NAME_OF_DOCTOR, alpha50DigitSpace));
        questions.add(new Question(true, demographicInformationId, 11052, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Additional comments", ParamNames.COMMENTS, alphanumeric100DigitSpace));
    }

    private void initPiraniScoring() {
        Integer piraniScoringId = 12;
        //Date Called
        questions.add(new Question(true, piraniScoringId, 12001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Date of visit", ParamNames.FORM_DATE, dateMinTodayMaxLastMonday));
        questions.add(new Question(true, piraniScoringId, 12002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Is this the patient’s final treatment visit?", ParamNames.FINAL_TREATMENT_VISIT, numeric3DigitMin1));
        options.add(new Option(12002, 338, null, new int[]{12003}, "", "<Select an option>", -1));
        options.add(new Option(12002, 500, null, new int[]{12003}, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(12002, 501, new int[]{12003}, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        //Date of next visit
        questions.add(new Question(true, piraniScoringId, 12003, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Date of Next Visit ", ParamNames.DATE_NEXT_VISIT, dateMinTodayMaxNextYear));
        questions.add(new Question(true, piraniScoringId, 12004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Did the patient experience a relapse?", ParamNames.PATIENT_EXPERIENCE_A_RELAPSE, numeric3DigitMin1));
        options.add(new Option(12004, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(12004, 500, null, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(12004, 501, null, null, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));


        questions.add(new Question(true, piraniScoringId, 12070, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Side of foot deformed?", ParamNames.DEFORMED_SIDE, numeric3DigitMin1));
        options.add(new Option(12070, 500,
                new int[]{12005, 12007, 12009, 12011, 12013, 12015, 12017, 12019, 12021, 12023, 12025,
                        12027, 12029, 12031, 12032, 12033, 12034, 12035, 12036, 12037, 12038, 12039,
                        12040, 12041, 12042, 12043, 12044, 12045, 12046, 12047, 12048, 12049},

                new int[]{12006, 12008, 12010, 12012, 12014, 12016, 12018, 12020, 12022, 12024, 12026,
                        12028, 12030, 12050, 12051, 12052, 12053, 12054, 12055, 12056, 12057, 12058,
                        12059, 12060, 12061, 12062, 12063, 12064, 12065, 12066, 12067, 12068}, "5141AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Right", -1));
        options.add(new Option(12070, 501,
                new int[]{12006, 12008, 12010, 12012, 12014, 12016, 12018, 12020, 12022, 12024, 12026,
                        12028, 12030, 12050, 12051, 12052, 12053, 12054, 12055, 12056, 12057, 12058,
                        12059, 12060, 12061, 12062, 12063, 12064, 12065, 12066, 12067, 12068},
                new int[]{12005, 12007, 12009, 12011, 12013, 12015, 12017, 12019, 12021, 12023, 12025,
                        12027, 12029, 12031, 12032, 12033, 12034, 12035, 12036, 12037, 12038, 12039,
                        12040, 12041, 12042, 12043, 12044, 12045, 12046, 12047, 12048, 12049}, "5139AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Left", -1));
        options.add(new Option(12070, 501,
                new int[]{12005, 12007, 12009, 12011, 12013, 12015, 12017, 12019, 12021, 12023, 12025,
                        12027, 12029, 12031, 12032, 12033, 12034, 12035, 12036, 12037, 12038, 12039,
                        12040, 12041, 12042, 12043, 12044, 12045, 12046, 12047, 12048, 12049,
                        12006, 12008, 12010, 12012, 12014, 12016, 12018, 12020, 12022, 12024, 12026,
                        12028, 12030, 12050, 12051, 12052, 12053, 12054, 12055, 12056, 12057, 12058,
                        12059, 12060, 12061, 12062, 12063, 12064, 12065, 12066, 12067, 12068}, null, "e60ad653-b0f8-44cd-a2a4-107577c50735", "Both", -1));

        questions.add(new Question(true, piraniScoringId, 12005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Right Varus", ParamNames.RIGHT_VARUS, numeric3DigitMin1));
        options.add(new Option(12005, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(12005, 500, null, null, "ed83c2fb-3bd5-44b8-918c-b11a65db9ea4", "Varus", -1));
        options.add(new Option(12005, 501, null, null, "69789847-8d3d-47fd-b73b-b71ffce76292", "Neutral", -1));
        options.add(new Option(12005, 501, null, null, "eeaf8a9e-5e76-41f0-96d6-ebeb9ec3cea1", "Valgus", -1));
        questions.add(new Question(true, piraniScoringId, 12007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Right Cavus", ParamNames.RIGHT_CAVUS, numeric3DigitMin1));
        options.add(new Option(12007, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(12007, 500, null, null, "163748AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Present", -1));
        options.add(new Option(12007, 501, null, null, "a8e3d9a7-34a2-47e8-9c88-e329f3062c0b", "Corrected", -1));
        questions.add(new Question(true, piraniScoringId, 12009, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_RANGE,
                "Right Adductus  ° (-30 to 70)", ParamNames.RIGHT_ADDUCTUS, numeric3DigitWithHypen));
        options.add(new RangeOption(12009, 500, null, null, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.BETWEEN, -30, 70)));
        questions.add(new Question(true, piraniScoringId, 12011, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_RANGE,
                "Right Equinus ° (-50 to 30)", ParamNames.RIGHT_EQUINUS, numeric3DigitWithHypen));
        options.add(new RangeOption(12011, 500, null, null, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.BETWEEN, -50, 30)));
        questions.add(new Question(true, piraniScoringId, 12013, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Posterior Crease - Right foot", ParamNames.POSTERIOR_CREASE_RIGHT_FOOT, numeric3DigitMin1));
        options.add(new Option(12013, 338, null, null, "", "", -1));
        options.add(new Option(12013, 500, null, null, "", "1.0", -1));
        options.add(new Option(12013, 501, null, null, "", "0.5", -1));
        options.add(new Option(12013, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12015, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Empty Heel - Right foot", ParamNames.EMPTY_HEEL_RIGHT_FOOT, numeric3DigitMin1));
        options.add(new Option(12015, 338, null, null, "", "", -1));
        options.add(new Option(12015, 500, null, null, "", "1.0", -1));
        options.add(new Option(12015, 501, null, null, "", "0.5", -1));
        options.add(new Option(12015, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12017, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Rigid Equinus - Right foot", ParamNames.RIGID_EQUINUS_RIGHT_FOOT, numeric3DigitMin1));
        options.add(new Option(12017, 338, null, null, "", "", -1));
        options.add(new Option(12017, 500, null, null, "", "1.0", -1));
        options.add(new Option(12017, 501, null, null, "", "0.5", -1));
        options.add(new Option(12017, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12019, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Hind foot score - Right foot", ParamNames.HIND_FOOT_SCORE_RIGHT_FOOT, numeric5Digit));
        //options.add(new RangeOption(12019, 500, null, null, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.BETWEEN, 1, 999)));
        questions.add(new Question(true, piraniScoringId, 12021, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Talar Head Coverage - Right foot", ParamNames.TALAR_HEAD_COVERAGE_RIGHT_FOOT, numeric3DigitMin1));
        options.add(new Option(12021, 338, null, null, "", "", -1));
        options.add(new Option(12021, 500, null, null, "", "1.0", -1));
        options.add(new Option(12021, 501, null, null, "", "0.5", -1));
        options.add(new Option(12021, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12023, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Medial Crease - Right foot", ParamNames.MEDIAL_CREASE_RIGHT_FOOT, numeric3DigitMin1));
        options.add(new Option(12023, 338, null, null, "", "", -1));
        options.add(new Option(12023, 500, null, null, "", "1.0", -1));
        options.add(new Option(12023, 501, null, null, "", "0.5", -1));
        options.add(new Option(12023, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12025, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Curved Lateral Border - Right foot", ParamNames.CURVED_LATERAL_BORDER_RIGHT_FOOT, numeric3DigitMin1));
        options.add(new Option(12025, 338, null, null, "", "", -1));
        options.add(new Option(12025, 500, null, null, "", "1.0", -1));
        options.add(new Option(12025, 501, null, null, "", "0.5", -1));
        options.add(new Option(12025, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12027, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Mid foot score - Right foot", ParamNames.MID_FOOT_SCORE_RIGHT_FOOT, numeric5Digit));
        //options.add(new RangeOption(12027, 500, null, null, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.BETWEEN, 1, 999)));
        questions.add(new Question(true, piraniScoringId, 12029, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Total score - Right foot", ParamNames.TOTAL_SCORE_RIGHT_FOOT, numeric5Digit));
        questions.add(new Question(true, piraniScoringId, 12031, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Treatment - Right foot", ParamNames.TREATMENT_RIGHT_FOOT, numeric3DigitMin1));
        options.add(new Option(12031, 338, null, new int[]{12032, 12033, 12034, 12035, 12037, 12038, 12039, 12040, 12041, 12043, 12044, 12036, 120580}, "", "<Select an option>", -1));
        options.add(new Option(12031, 500, new int[]{12032, 12033}, new int[]{12034, 12035, 12037, 12038, 12039, 12040, 12041, 12043, 12044, 12036, 120580}, "64835f79-a00f-4bf4-91a7-0e727b72e3e0", "Manipulation & Casting", -1));
        options.add(new Option(12031, 501, new int[]{12034, 12035}, new int[]{12032, 12033, 12037, 12038, 12039, 12040, 12041, 12043, 12044, 12036, 120580}, "36187674-996f-4138-bc12-1a264756c3ce", "Tenotomy", -1));
        options.add(new Option(12031, 501, new int[]{12037, 12038, 12039, 120580}, new int[]{12032, 12033, 12034, 12035, 12040, 12041, 12043, 12044, 12036}, "b94e6043-293c-4ef1-ab8b-36f937685371", "Brace application", -1));
        options.add(new Option(12031, 500, new int[]{12032, 12033, 12034, 12035, 12037, 12038, 12039, 12040, 12041, 12043, 12044, 12036, 120580}, null, "d0514185-6d4a-4728-af60-cbd36fc25541", "Refer", -1));
        options.add(new Option(12031, 501, new int[]{12040, 12041, 12043, 12044}, new int[]{12032, 12033, 12034, 12035, 12037, 12038, 12039, 12036, 120580}, "159619AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Surgery", -1));
        options.add(new Option(12031, 501, new int[]{12036}, new int[]{12032, 12033, 12034, 12035, 12037, 12038, 12039, 12040, 12041, 12043, 12044, 120580}, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        questions.add(new Question(true, piraniScoringId, 12032, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Who performed casting - Right foot", ParamNames.WHO_PERFORMED_CASTING_RIGHT_FOOT, null));
        this.options.addAll(DynamicOptions.getEvaluatorOptions(context, 12032, null, null));

        questions.add(new Question(true, piraniScoringId, 12033, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Cast number - Right", ParamNames.CAST_NUMBER_RIGHT, numeric2Digit));
        questions.add(new Question(true, piraniScoringId, 12034, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Degrees abduction - Right (T)", ParamNames.DEGREES_ABDUCTION_RIGHT, numeric3DigitMin1));
        questions.add(new Question(true, piraniScoringId, 12035, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Degrees dorsiflexion - Right (T)", ParamNames.DEGREES_DORSIFLEXION_RIGHT, numeric3DigitMin1));
        questions.add(new Question(true, piraniScoringId, 12036, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Details for Other - Right foot", ParamNames.DETAILS_FOR_OTHER_RIGHT_FOOT, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12037, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Brace Compliance - Rigt foot", ParamNames.BRACE_COMPLIANCE_RIGT_FOOT, numeric3DigitMin1));
        options.add(new Option(12037, 338, null, new int[]{12038, 12039}, "", "<Select an option>", -1));
        options.add(new Option(12037, 500, null, new int[]{12038, 12039}, "159405AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Good", -1));
        options.add(new Option(12037, 501, new int[]{12038, 12039}, null, "159406AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Fair", -1));
        options.add(new Option(12037, 500, new int[]{12038, 12039}, null, "159407AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Poor", -1));
        options.add(new Option(12037, 501, null, new int[]{12038, 12039}, "1175AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Progressed to braces", -1));
        options.add(new Option(12037, 501, null, new int[]{12038, 12039}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(true, piraniScoringId, 12038, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Problem - Right foot (B)", ParamNames.PROBLEM_RIGHT_FOOT, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12039, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Action taken - Right foot (B)", ParamNames.ACTION_TAKEN_RIGHT_FOOT, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12040, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Surgery date - Right foot", ParamNames.SURGERY_DATE_RIGHT_FOOT, dateMinTodayMaxLastMonday));
        questions.add(new Question(true, piraniScoringId, 12041, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Surgery type - Right foot", ParamNames.SURGERY_TYPE_RIGHT_FOOT, numeric3DigitMin1));
        options.add(new Option(12041, 338, null, new int[]{12042}, "", "<Select an option>", -1));
        options.add(new Option(12041, 500, null, new int[]{12042}, "0219a7d7-d5e3-42b4-96b7-1d4719be84c1", "Posterior release", -1));
        options.add(new Option(12041, 501, null, new int[]{12042}, "07c6579f-a040-49b7-8c0d-9c1f93ac1055", "Subtalar release", -1));
        options.add(new Option(12041, 501, null, new int[]{12042}, "72d67ee0-b2ea-4631-9efe-44692ae1e0fe", "Achilles lengthening", -1));
        options.add(new Option(12041, 500, null, new int[]{12042}, "8c79d7bd-009e-4864-8717-c1e02ef50a0e", "Osteotomies", -1));
        options.add(new Option(12041, 501, null, new int[]{12042}, "69c3b764-7c2a-46a0-a4e0-fa3c62d95931", "Talectomy", -1));
        options.add(new Option(12041, 501, null, new int[]{12042}, "34c3c0a5-5b87-447b-ad92-c41374adf4dd", "Medial release", -1));
        options.add(new Option(12041, 500, null, new int[]{12042}, "08c507dd-8dc8-47f6-acf2-771603b40a80", "Plantar release", -1));
        options.add(new Option(12041, 501, null, new int[]{12042}, "a78cdafc-7d53-4839-b143-de5b8a85e03d", "Tendon transfers", -1));
        options.add(new Option(12041, 501, null, new int[]{12042}, "5b640e51-4fbb-42bb-aac5-bf49987106f3", "Arthrodesis", -1));
        options.add(new Option(12041, 501, new int[]{12042}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        questions.add(new Question(false, piraniScoringId, 12042, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option", ParamNames.SURGERY_TYPE_RIGHT_FOOT_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12043, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Surgery comments - Right foot", ParamNames.SURGERY_COMMENTS_RIGHT_FOOT, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12044, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Were there any complications - Right foot (S)", ParamNames.WERE_THERE_ANY_COMPLICATIONS_RIGHT_FOOT, numeric3DigitMin1));
        options.add(new Option(12044, 338, null, new int[]{12045, 12048, 12049}, "", "<Select an option>", -1));
        options.add(new Option(12044, 500, new int[]{12045, 12048, 12049}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(12044, 501, null, new int[]{12045, 12048, 12049}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        questions.add(new Question(true, piraniScoringId, 12045, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Description of complication - Right foot (S)", ParamNames.DESCRIPTION_OF_COMPLICATION_RIGHT_FOOT, numeric3DigitMin1));
        options.add(new Option(12045, 338, null, new int[]{12046, 12047}, "", "<Select an option>", -1));
        options.add(new Option(12045, 500, null, new int[]{12046, 12047}, "1107AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "None", -1));
        options.add(new Option(12045, 501, null, new int[]{12046, 12047}, "3b289638-7576-40d7-b1ee-4a46907b5d6d", "Rocker bottom deformity", -1));
        options.add(new Option(12045, 501, null, new int[]{12046, 12047}, "3d5b759b-21cd-4b9f-adc5-905846468e0d", "Crowded toes", -1));
        options.add(new Option(12045, 500, null, new int[]{12046, 12047}, "1919aac7-bf54-43de-a2c8-260c999c455b", "Flat heel pad", -1));
        options.add(new Option(12045, 501, null, new int[]{12046, 12047}, "a6317dde-39cc-475a-92f3-ffbecba25686", "Superficial sores", -1));
        options.add(new Option(12045, 501, null, new int[]{12046, 12047}, "fab274f8-3090-4db1-8e21-b27940438d13", "Pressure sores", -1));
        options.add(new Option(12045, 500, null, new int[]{12046, 12047}, "f04059df-8e75-43e2-8ccf-c68dfec091be", "Deep sores", -1));
        options.add(new Option(12045, 501, new int[]{12047}, new int[]{12046}, "e1c0544e-fbcd-41cf-80de-6cda9b1fd8dd", "ER visit (open text)", -1));
        options.add(new Option(12045, 500, new int[]{12046}, new int[]{12047}, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        questions.add(new Question(false, piraniScoringId, 12046, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option", ParamNames.DESCRIPTION_OF_COMPLICATION_RIGHT_FOOT_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(false, piraniScoringId, 12047, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "ER visit", ParamNames.DESCRIPTION_OF_COMPLICATION_RIGHT_FOOT_ER, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12048, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Treatment of complication - Right foot (S)", ParamNames.TREATMENT_OF_COMPLICATION_RIGHT_FOOT, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12049, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Results after treatment  - Right foot (S)", ParamNames.RESULTS_AFTER_TREATMENT_RIGHT_FOOT, alpha100DigitSpace));


        questions.add(new Question(true, piraniScoringId, 12006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Left Varus", ParamNames.LEFT_VARUS, numeric3DigitMin1));
        options.add(new Option(12006, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(12006, 500, null, null, "ed83c2fb-3bd5-44b8-918c-b11a65db9ea4", "Varus", -1));
        options.add(new Option(12006, 501, null, null, "69789847-8d3d-47fd-b73b-b71ffce76292", "Neutral", -1));
        options.add(new Option(12006, 501, null, null, "eeaf8a9e-5e76-41f0-96d6-ebeb9ec3cea1", "Valgus", -1));
        questions.add(new Question(true, piraniScoringId, 12008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Left Cavus", ParamNames.LEFT_CAVUS, numeric3DigitMin1));
        options.add(new Option(12008, 338, null, null, "", "<Select an option>", -1));
        options.add(new Option(12008, 500, null, null, "163748AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Present", -1));
        options.add(new Option(12008, 501, null, null, "a8e3d9a7-34a2-47e8-9c88-e329f3062c0b", "Corrected", -1));
        questions.add(new Question(true, piraniScoringId, 12010, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_RANGE,
                "Left Adductus  ° (-30 to 70)", ParamNames.LEFT_ADDUCTUS, numeric3DigitWithHypen));
        options.add(new RangeOption(12010, 500, null, null, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.BETWEEN, -30, 70)));
        questions.add(new Question(true, piraniScoringId, 12012, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_RANGE,
                "Left Equinus ° (-50 to 30)", ParamNames.LEFT_EQUINUS, numeric3DigitWithHypen));
        options.add(new RangeOption(12012, 500, null, null, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.BETWEEN, -50, 30)));
        questions.add(new Question(true, piraniScoringId, 12014, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Posterior Crease - Left foot", ParamNames.POSTERIOR_CREASE_LEFT_FOOT, numeric5Digit));
        options.add(new Option(12014, 338, null, null, "", "", -1));
        options.add(new Option(12014, 500, null, null, "", "1.0", -1));
        options.add(new Option(12014, 501, null, null, "", "0.5", -1));
        options.add(new Option(12014, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12016, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Empty Heel - Left foot", ParamNames.EMPTY_HEEL_LEFT_FOOT, numeric5Digit));
        options.add(new Option(12016, 338, null, null, "", "", -1));
        options.add(new Option(12016, 500, null, null, "", "1.0", -1));
        options.add(new Option(12016, 501, null, null, "", "0.5", -1));
        options.add(new Option(12016, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12018, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Rigid Equinus - Left foot", ParamNames.RIGID_EQUINUS_LEFT_FOOT, numeric5Digit));
        options.add(new Option(12018, 338, null, null, "", "", -1));
        options.add(new Option(12018, 500, null, null, "", "1.0", -1));
        options.add(new Option(12018, 501, null, null, "", "0.5", -1));
        options.add(new Option(12018, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12020, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Hind foot score - Left foot", ParamNames.HIND_FOOT_SCORE_LEFT_FOOT, numeric5Digit));
        // options.add(new RangeOption(12020, 500, null, null, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.BETWEEN, 0, 999)));
        questions.add(new Question(true, piraniScoringId, 12022, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Talar Head Coverage - Left foot", ParamNames.TALAR_HEAD_COVERAGE_LEFT_FOOT, numeric5Digit));
        options.add(new Option(12022, 338, null, null, "", "", -1));
        options.add(new Option(12022, 500, null, null, "", "1.0", -1));
        options.add(new Option(12022, 501, null, null, "", "0.5", -1));
        options.add(new Option(12022, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12024, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Medial Crease - Left foot", ParamNames.MEDIAL_CREASE_LEFT_FOOT, numeric5Digit));
        options.add(new Option(12024, 338, null, null, "", "", -1));
        options.add(new Option(12024, 500, null, null, "", "1.0", -1));
        options.add(new Option(12024, 501, null, null, "", "0.5", -1));
        options.add(new Option(12024, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12026, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Curved Lateral Border - Left foot", ParamNames.CURVED_LATERAL_BORDER_LEFT_FOOT, numeric5Digit));
        options.add(new Option(12026, 338, null, null, "", "", -1));
        options.add(new Option(12026, 500, null, null, "", "1.0", -1));
        options.add(new Option(12026, 501, null, null, "", "0.5", -1));
        options.add(new Option(12026, 501, null, null, "", "0.0", -1));
        questions.add(new Question(true, piraniScoringId, 12028, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Mid foot score - Left foot", ParamNames.MID_FOOT_SCORE_LEFT_FOOT, numeric5Digit));
        // options.add(new RangeOption(12028, 500, null, null, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.BETWEEN, 1, 999)));
        questions.add(new Question(true, piraniScoringId, 12030, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Total score - Left foot", ParamNames.TOTAL_SCORE_LEFT_FOOT, numeric5Digit));
        questions.add(new Question(true, piraniScoringId, 12050, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Treatment - Left foot", ParamNames.TREATMENT_LEFT_FOOT, numeric3DigitMin1));
        options.add(new Option(12050, 338, null, new int[]{12051, 12052, 12053, 12054, 12055, 12056, 12057, 12058, 12059, 12060, 12062, 12063, 120580}, "", "<Select an option>", -1));
        options.add(new Option(12050, 500, new int[]{12051, 12052}, new int[]{12053, 12054, 12055, 12056, 12057, 12058, 12059, 12060, 12062, 12063, 120580}, "64835f79-a00f-4bf4-91a7-0e727b72e3e0", "Manipulation & Casting", -1));
        options.add(new Option(12050, 501, new int[]{12053, 12054, 120580}, new int[]{12051, 12052, 12055, 12056, 12057, 12058, 12059, 12060, 12062, 12063}, "36187674-996f-4138-bc12-1a264756c3ce", "Tenotomy", -1));
        options.add(new Option(12050, 501, new int[]{12056, 12057, 12058}, new int[]{12051, 12052, 12053, 12054, 12055, 12059, 12060, 12062, 12063, 120580}, "b94e6043-293c-4ef1-ab8b-36f937685371", "Brace application", -1));
        options.add(new Option(12050, 500, new int[]{12051, 12052, 12053, 12054, 12055, 12056, 12057, 12058, 12059, 12060, 12062, 12063, 120580}, null, "d0514185-6d4a-4728-af60-cbd36fc25541", "Refer", -1));
        options.add(new Option(12050, 501, new int[]{12059, 12060, 12062, 12063}, new int[]{12051, 12052, 12053, 12054, 12055, 12056, 12057, 12058, 120580}, "159619AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Surgery", -1));
        options.add(new Option(12050, 501, new int[]{12055}, new int[]{12051, 12052, 12053, 12054, 12056, 12057, 12058, 12059, 12060, 12062, 12063, 120580}, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        questions.add(new Question(true, piraniScoringId, 12051, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY, "Who performed casting - Left foot", ParamNames.WHO_PERFORMED_CASTING_LEFT_FOOT, null));
        this.options.addAll(DynamicOptions.getEvaluatorOptions(context, 12051, null, null));

        questions.add(new Question(true, piraniScoringId, 12052, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Cast number - Left", ParamNames.CAST_NUMBER_LEFT, numeric2Digit));
        questions.add(new Question(true, piraniScoringId, 12053, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Degrees abduction - Left (T)", ParamNames.DEGREES_ABDUCTION_LEFT, numeric3DigitMin1));
        questions.add(new Question(true, piraniScoringId, 12054, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Degrees dorsiflexion - Left (T)", ParamNames.DEGREES_DORSIFLEXION_LEFT, numeric3DigitMin1));
        questions.add(new Question(true, piraniScoringId, 12055, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Details for Other - Left foot", ParamNames.DETAILS_FOR_OTHER_LEFT_FOOT, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12056, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Brace Compliance - Left foot", ParamNames.BRACE_COMPLIANCE_LEFT_FOOT, numeric3DigitMin1));
        options.add(new Option(12056, 338, null, new int[]{12057, 12058}, "", "<Select an option>", -1));
        options.add(new Option(12056, 500, null, new int[]{12057, 12058}, "159405AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Good", -1));
        options.add(new Option(12056, 501, new int[]{12057, 12058}, null, "159406AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Fair", -1));
        options.add(new Option(12056, 500, new int[]{12057, 12058}, null, "159407AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Poor", -1));
        options.add(new Option(12056, 501, null, new int[]{12057, 12058}, "1175AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Progressed to braces", -1));
        options.add(new Option(12056, 501, null, new int[]{12057, 12058}, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Don't Know", -1));
        questions.add(new Question(true, piraniScoringId, 12057, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Problem - Left foot (B)", ParamNames.PROBLEM_LEFT_FOOT, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12058, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Action taken - Left foot (B)", ParamNames.ACTION_TAKEN_LEFT_FOOT, alpha100DigitSpace));

        questions.add(new Question(true, piraniScoringId, 120580, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Braces Size", ParamNames.ACTION_TAKEN_LEFT_FOOT, alphaNumeric50DigitSpace));


        questions.add(new Question(true, piraniScoringId, 12059, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Surgery date - Left foot", ParamNames.SURGERY_DATE_LEFT_FOOT, dateMinTodayMaxLastMonday));
        questions.add(new Question(true, piraniScoringId, 12060, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Surgery type - Left foot", ParamNames.SURGERY_TYPE_LEFT_FOOT, numeric3DigitMin1));
        options.add(new Option(12060, 338, null, new int[]{12061}, "", "<Select an option>", -1));
        options.add(new Option(12060, 500, null, new int[]{12061}, "0219a7d7-d5e3-42b4-96b7-1d4719be84c1", "Posterior release", -1));
        options.add(new Option(12060, 501, null, new int[]{12061}, "07c6579f-a040-49b7-8c0d-9c1f93ac1055", "Subtalar release", -1));
        options.add(new Option(12060, 501, null, new int[]{12061}, "72d67ee0-b2ea-4631-9efe-44692ae1e0fe", "Achilles lengthening", -1));
        options.add(new Option(12060, 500, null, new int[]{12061}, "8c79d7bd-009e-4864-8717-c1e02ef50a0e", "Osteotomies", -1));
        options.add(new Option(12060, 501, null, new int[]{12061}, "69c3b764-7c2a-46a0-a4e0-fa3c62d95931", "Talectomy", -1));
        options.add(new Option(12060, 501, null, new int[]{12061}, "34c3c0a5-5b87-447b-ad92-c41374adf4dd", "Medial release", -1));
        options.add(new Option(12060, 500, null, new int[]{12061}, "08c507dd-8dc8-47f6-acf2-771603b40a80", "Plantar release", -1));
        options.add(new Option(12060, 501, null, new int[]{12061}, "a78cdafc-7d53-4839-b143-de5b8a85e03d", "Tendon transfers", -1));
        options.add(new Option(12060, 501, null, new int[]{12061}, "5b640e51-4fbb-42bb-aac5-bf49987106f3", "Arthrodesis", -1));
        options.add(new Option(12060, 501, new int[]{12061}, null, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        questions.add(new Question(false, piraniScoringId, 12061, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option", ParamNames.SURGERY_TYPE_LEFT_FOOT_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12062, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Surgery comments - Left foot", ParamNames.SURGERY_COMMENTS_LEFT_FOOT, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12063, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Were there any complications - Left foot (S)", ParamNames.WERE_THERE_ANY_COMPLICATIONS_LEFT_FOOT, numeric3DigitMin1));
        options.add(new Option(12063, 338, null, new int[]{12064, 12067, 12068}, "", "<Select an option>", -1));
        options.add(new Option(12063, 500, new int[]{12064, 12067, 12068}, null, "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Yes", -1));
        options.add(new Option(12063, 501, null, new int[]{12064, 12067, 12068}, "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "No", -1));
        questions.add(new Question(true, piraniScoringId, 12064, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Description of complication - Left foot (S)", ParamNames.DESCRIPTION_OF_COMPLICATION_LEFT_FOOT, numeric3DigitMin1));
        options.add(new Option(12064, 338, null, new int[]{12065, 12066}, "", "<Select an option>", -1));
        options.add(new Option(12064, 500, null, new int[]{12065, 12066}, "1107AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "None", -1));
        options.add(new Option(12064, 501, null, new int[]{12065, 12066}, "3b289638-7576-40d7-b1ee-4a46907b5d6d", "Rocker bottom deformity", -1));
        options.add(new Option(12064, 501, null, new int[]{12065, 12066}, "3d5b759b-21cd-4b9f-adc5-905846468e0d", "Crowded toes", -1));
        options.add(new Option(12064, 500, null, new int[]{12065, 12066}, "1919aac7-bf54-43de-a2c8-260c999c455b", "Flat heel pad", -1));
        options.add(new Option(12064, 501, null, new int[]{12065, 12066}, "a6317dde-39cc-475a-92f3-ffbecba25686", "Superficial sores", -1));
        options.add(new Option(12064, 501, null, new int[]{12065, 12066}, "fab274f8-3090-4db1-8e21-b27940438d13", "Pressure sores", -1));
        options.add(new Option(12064, 500, null, new int[]{12065, 12066}, "f04059df-8e75-43e2-8ccf-c68dfec091be", "Deep sores", -1));
        options.add(new Option(12064, 501, new int[]{12066}, new int[]{12065}, "e1c0544e-fbcd-41cf-80de-6cda9b1fd8dd", "ER visit (open text)", -1));
        options.add(new Option(12064, 500, new int[]{12065}, new int[]{12066}, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "Other (open text)", -1));
        questions.add(new Question(false, piraniScoringId, 12065, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "What other option", ParamNames.DESCRIPTION_OF_COMPLICATION_LEFT_FOOT_OTHER, alphanumeric100DigitSpace));
        questions.add(new Question(false, piraniScoringId, 12066, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "ER visit", ParamNames.DESCRIPTION_OF_COMPLICATION_LEFT_FOOT_ER, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12067, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Treatment of complication - Left foot (S)", ParamNames.TREATMENT_OF_COMPLICATION_LEFT_FOOT, alpha100DigitSpace));
        questions.add(new Question(true, piraniScoringId, 12068, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.GONE, Validation.CHECK_FOR_EMPTY,
                "Results after treatment  - Left foot (S)", ParamNames.RESULTS_AFTER_TREATMENT_LEFT_FOOT, alpha100DigitSpace));
        questions.add(new Question(false, piraniScoringId, 12069, "", InputWidget.InputWidgetsType.WIDGET_TYPE_IMAGE, View.VISIBLE, Validation.CHECK_FOR_EMPTY,
                "Capture Image", ParamNames.CAPTURE_IMAGE, null));
        options.add(new RangeOption(12069, 500, null, null, "", "", -1, new SkipRange(SkipRange.VALIDATION_TYPE.EQUALS, 3)));
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