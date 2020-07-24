package com.ihsinformatics.dynamicformsgenerator.network;

public class ParamNames {

    public static final String ENCOUNTER_TYPE_CREATE_PATIENT = "Create Patient";
    public static final String ENCOUNTER_TYPE_PATIENT_INFO = "Patient Information";
    public static final String ENCOUNTER_TYPE_ADULT_SCREENING = "Adult Screening";
    public static final String ENCOUNTER_TYPE_CHILD_SCREENING = "Child Screening";
    public static final String ENCOUNTER_TYPE_CHILD_CLINICAL_EVALUATION = "Child Clinician Evaluation";
    public static final String ENCOUNTER_TYPE_ADULT_CLINICAL_EVALUATION = "Adult Clinician Evaluation";
    public static final String ENCOUNTER_TYPE_ADULT_TX_INITIATION = "Adult Treatment Initiation";
    public static final String ENCOUNTER_TYPE_CHILD_TX_INITIATION = "Child Treatment Initiation";
    public static final String ENCOUNTER_TYPE_EOF = "End Of Followup";
    public static final String ENCOUNTER_TYPE_CONTACT_REGISTRY = "Contact Registry";
    public static final String ENCOUNTER_TYPE_ADVERSE_EVENT = "Adverse Event";
    public static final String ENCOUNTER_TB_DISEASE_CONFIRMATION = "TB Disease Confirmation";


//    public static final String[] ENCOUNTER_TYPES = new String[]{
//            ENCOUNTER_TYPE_CREATE_PATIENT,
//            ENCOUNTER_TYPE_PATIENT_INFO,
//            ENCOUNTER_TYPE_ADULT_SCREENING,
//            ENCOUNTER_TYPE_CHILD_SCREENING,
//            ENCOUNTER_TYPE_CHILD_CLINICAL_EVALUATION,
//            ENCOUNTER_TYPE_ADULT_CLINICAL_EVALUATION,
//            ENCOUNTER_TYPE_ADULT_TX_INITIATION,
//            ENCOUNTER_TYPE_CHILD_TX_INITIATION,
//            ENCOUNTER_TYPE_EOF,
//            ENCOUNTER_TYPE_CONTACT_REGISTRY
//    };

    public static final String[] SUMMARY_VARIBALES = new String[]{
            "patientType", "patientRiskCategory","patientSource","confirmationType",
            "weight", "height", "bmi", "endOfFollowUpFor", "relatedOutcome", "diseaseSite",
            "tbType", "nextTBAppointment", "nextTBAppointment"
    };
    public static final String[] SUMMARY_VARIABLES_OBJECTS = new String[]{
            "recentVisits"
    };

    public static final String[] SUMMARY_VARIABLES_ARRAYS = new String[]{
            "relationships"
    };

    public static final String COUNTRY = "country";
    public static final String STATE = "stateProvince";
    public static final String COUNTRY_DISTRICT = "countyDistrict";
    public static final String CITY_VILLAGE = "cityVillage";
    public static final String ADDRESS1 = "address1";
    public static final String ADDRESS2 = "address2";
    public static final String ADDRESS3 = "address3";
    public static final String ADDRESS4 = "address4";
    public static final String ADDRESS5 = "address5";
    public static final String ADDRESS6 = "address6";
    public static final String PARENT_LOCATION = "parentLocation";
    public static final String PATIENT = "patient";
    public static final String FORM_DATA = "data";
    public static final String SERVICE_HISTORY = "values";
    public static final String AUTHENTICATION = "authentication";

    public static final String AUDIT_INFO = "auditInfo";
    public static final String DATE_CREATED = "dateCreated";
    public static final String LOCATION = "location";
    public static final String LOCATION_TAGS = "tags";
    public static final String ATTRIBUTES = "attributes";
    public static final String ATTRIBUTE_TYPE_PQ_PIRANI_SCORING_ROLE_NAME = "PQ Pirani Scoring Role";


    public static final String UUID = "uuid";
    public static final String PROVIDER = "provider";
    public static final String DISPLAY = "display";
    public static final String PERSON = "person";
    public static final String SERVER_ERROR = "error";
    public static final String SERVER_MESSAGE = "message";
    public static final String IDENTIFIER = "identifier";
    public static final String IDENTIFIERS = "identifiers";
    public static final String IDENTIFIER_TYPE = "identifierType";
    public static final String GIVEN_NAME = "givenName";
    public static final String FAMILY_NAME = "familyName";
    public static final String PERSON_AGE = "age";
    public static final String PERSON_GENDER = "gender";
    public static final String BIRTH_DATE = "birthdate";
    public static final String PREFERRED_NAME = "preferredName";

    public static final String REQUEST_TYPE = "ENCONTER_TYPE";
    public static final String IMEI = "imei";
    public static final String TYPE = "type";
    public static final String VALUE = "value";
    public static final String GENDER = "GENDER";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String SERVER_RESPONSE = "results";
    public static final String SERVER_RESULT = "result";
    public static final String DATA = "data";
    public static final String ENCOUNTERS_COUNT = "encountersCount";


    public static final String GET_PATIENT_INFO = "GET_PATIENT_INFO";
    public static final String GET_EVALUATORS = "GET_EVALUATORS";
    public static final String GET_PATIENT_IMAGES_INFO = "Patient Images";
    public static final String VALIDATE_USER = "VALIDATE_USER";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String GPS_PARAM = "Form Location GPS Coordinates";
    public static final String PROJECT_IDENTIFIER = "Patient Identifier";
    public static final String PQ_PROJECT_IDENTIFIER = "PQ Identifier";
    public static final String CSC_PROJECT_IDENTIFIER = "CSC Identifier";


    //PATIENT CREATION
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String SEX = "sex";
    public static final String DOB = "dob";
    public static final String PROVINCE = "province";
    public static final String DISTRICT = "district";
    public static final String CITY = "city";

    public static final String MR_NUMBER = "MR Number";


    public static final String CONCEPT_GROUP_MEMBERS = "conceptGroupMembers";
    public static final String PATIENT_IDENTIFIER = "patient_identifier";
    public static final String INDUS_PROJECT_IDENTIFIER = "Indus Project Identifier";
    public static final String CAPTURE_IMAGE = "picture";


    public static final String CONTACT = "contact";

    public static final String PARAM_NAME = "param_name";


    //!!! for encryption and decryption
    public static final String KEY_USERNAME = "usernameusername";
    public static final String KEY_PASSWORD = "passwordpassword";
    public static final byte[] KEY_VALUE =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't',
                    'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
    public static final String PAYLOAD_TYPE = "payload_type";
    public static final String METADATA = "metadata";
    public static final String DATE_ENTERED_PARAM = "Date Entered";
    public static final String FORM_DETAILS = "formDetails";
    public static final String COMPONENT_FORM_ID = "componentFormId";
    public static final String COMPONENT_FORM_UUID = "uuid";
    public static final String WORKFLOW = "workflow";
    public static final String PARAM_VALUE = "value";
    public static final String PARAM_QUESTION = "question";
    public static final String PARAM_CREATE_PATIENT = "createPatient";
    public static final String WIDGET_TYPE = "widget";
    public static final String PERSON_ATTRIBUTE = "person_attribute";
    public static final boolean PERSON_ATTRIBUTE_TRUE = true;
    public static final boolean PERSON_ATTRIBUTE_FALSE = false;
    public static final String CHARACTER = "characters";
    public static final String COVID_RESULT = "covidResult";
    public static final String OTHER_DETAILS = "otherDetails";

    public static final String OFFLINE_CONTACT = "offlineContact";

    public static final String ONLINE_ENCOUNTERS = "online";
    public static final String OFFLINE_ENCOUNTERS = "offline";
    public static final String OFFLINE_UPLOADED_ENCOUNTERS = "offline_uploaded";

}