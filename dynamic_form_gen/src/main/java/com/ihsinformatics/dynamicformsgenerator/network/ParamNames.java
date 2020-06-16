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
    public static final String JSON_DATA = "json_data";
    public static final String NAME_OF_PROCEDURE = "nameOfProcedure";

    public static final String AUDIT_INFO = "auditInfo";
    public static final String DATE_CREATED = "dateCreated";
    public static final String LOCATION = "location";
    public static final String LOCATION_TAGS = "tags";
    public static final String ATTRIBUTES = "attributes";
    public static final String ATTRIBUTE_TYPES = "attributeType";
    public static final String ATTRIBUTE_TYPE_PQ_PIRANI_SCORING_ROLE = "71e64a72-39d2-4425-9ea7-5b9209388ab3";
    public static final String ATTRIBUTE_TYPE_PQ_PIRANI_SCORING_ROLE_NAME = "PQ Pirani Scoring Role";
    // public static final String PREFERRED_NAME = "preferredName";

    public static final String RETIRED = "retired";

    public static final String ADVICE_DAY_10 = "d7a667a3-fac5-4dba-8f3d-2e333bcec79c"; // day 10 advice
    public static final String ADVICE_DAY_7 = "dae9ed3d-59ad-40ef-83d3-015456b3a36f"; // day 7 advice
    public static final String ADVICE_DAY_1 = "4c4c3ff9-1489-40f7-ab22-a5a1ede84ef6"; // day 1 advice
    public static final String OTHER_DESC_1 = "3ccde19d-6ecb-4491-98b3-71670197c993"; // other day 1
    public static final String OTHER_DESC_7 = "d2513e2b-cadc-4ba7-be1b-176acaacd713"; // other day 7
    public static final String OTHER_DESC_10 = "fe83f3b8-68ef-41e9-aa56-ad929298ed05"; // other day 10
    public static final String HAS_THE_RING_FALLEN = "60edb1c9-fc7f-4541-89a5-9d72c12a4008";

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
    public static final String GIVEN_NAME_PARAM = "GIVEN_NAME";
    public static final String FAMILY_NAME_PARAM = "FAMILY_NAME";
    public static final String PERSON_AGE = "age";
    public static final String PERSON_GENDER = "gender";
    public static final String BIRTH_DATE = "birthdate";
    public static final String PREFERRED_NAME = "preferredName";

    public static final String REQUEST_TYPE = "ENCONTER_TYPE";
    public static final String IMEI = "imei";
    public static final String TYPE = "type";
    public static final String VALUE = "value";
    public static final String DATE_ENTERED = "cur_date";
    public static final String CMHW_NAME = "INTERVIEWER_NAME";
    public static final String PAT_NAME = "pfname";
    public static final String TOWN = "TOWN";
    public static final String DOES_EXIST_SZC_ID = "DOES_EXIST_SZC_ID";
    public static final String WHICH_CLINIC = "WHICH_CLINIC";
    public static final String ID_TYPE = "ID_TYPE";
    public static final String NUMBER_MR = "NUMBER_MR";
    public static final String SZC_ID = "SZC_ID";
    public static final String SID = "sid";
    public static final String GENDER = "GENDER";
    public static final String MARITAL_STATUS = "MARITAL_STATUS";
    public static final String AGE = "AGE";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String RELIGION = "RELIGION";
    public static final String EDUCATION = "EDUCATION";
    public static final String SLEEP = "SLEEP";
    public static final String CRY = "CRY";
    public static final String DAILY_ACTIVITIES = "DAILY_ACTIVITIES";
    public static final String DECISION = "DECISION";
    public static final String DAILY_SUFFERING = "DAILY_SUFFERING";
    public static final String USEFUL_LIFE = "USEFUL_LIFE";
    public static final String SUICIDE = "SUICIDE";
    public static final String TIRED = "TIRED";
    public static final String HEADACHE = "HEADACHE";
    public static final String DIGESTION_POOR = "DIGESTION_POOR";
    public static final String THERAPY_AGREE = "THERAPY_AGREE";
    public static final String SESS_APN = "SESSION_APOINMENT";
    public static final String RMNDR_SMS = "Rmndr_SMS";
    public static final String PATIENT_SMS = "PATIENT_SMS";
    public static final String CONTACT_NUM = "CONTACT_NUMBER";
    public static final String CONTACT_NUM_2 = "CONTACT_NUMBER_2";
    public static final String NAM_CSLR = "NAME_OF_COUNSELOR";
    public static final String TIM_THRPY = "TIME_OF_THERAPY";
    public static final String LOC_THRPY = "LOCATION_OF_THERAPY";
    public static final String PAT_FEELBS = "PATIENT_FEEL_BEFORE_SESSION";
    public static final String PAT_COMP = "PATIENT_COMPLAINTS";
    public static final String PAT_COOP = "PATIENT_COOPERATION";
    public static final String PAT_DEF = "PATIENT_DEFENCIVENESS";
    public static final String PAT_ANX = "PATIENT_LEVEL_OF_ANX";
    public static final String PAT_HYG = "PATIENT_HYGIENE";
    public static final String PAT_ATT = "PATIENT_ATTENTION";
    public static final String PAT_FEELAS = "PATIENT_FEEL_AFTER_SESSION";
    public static final String PAT_IMP = "PATIENT_IMPROVEMENT";
    public static final String PAT_IMPY = "DESCRIBE_PATIENT_IMPROVEMENT";
    public static final String CONT_STAT = "CONTINUATION_STATUS";
    public static final String CONT_STAT_T = "CONTINUATION_STATUS_ON_TERMINATION";
    public static final String OB_CONTP = "OB_CONTP";
    public static final String OB_CONTP1 = "OB_CONTP1";
    public static final String OB_CONTP1O = "OB_CONTP1O";
    public static final String OB_CONTP2 = "OB_CONTP2";
    public static final String OB_CONTP2O = "OB_CONTP2O";
    public static final String OB_CONTP3 = "OB_CONTP3";
    public static final String OB_CONTP3O = "OB_CONTP3O";
    public static final String OB_CONTP4 = "OB_CONTP4";
    public static final String OB_CONTC = "OB_CONTC";
    public static final String OB_CONTC1 = "OB_CONTC1";
    public static final String OB_CONTC1O = "OB_CONTC1O";
    public static final String OB_CONTC2 = "OB_CONTC2";
    public static final String OB_CONTC2O = "OB_CONTC2O";
    public static final String OC_CONTC3 = "OC_CONTC3";
    public static final String OC_CONTC3O = "OC_CONTC3O";
    public static final String OB_CONTC4 = "OB_CONTC4";
    public static final String SES_STATRD = "SES_STATRD";
    public static final String SES_STATRT = "SES_STATRT";
    public static final String PAT_FEEL = "PAT_FEEL";
    public static final String PAT_FEELO = "PAT_FEELO";
    public static final String PAT_REF = "PAT_REF";
    public static final String TX_REAGREE = "Tx_reagree";
    public static final String SCREENING_SCORE = "SCORE";
    public static final String TERMST = "TermSt";
    public static final String RESNRF = "resnrf";
    public static final String TERMSS = "termss";
    public static final String CLIRTER = "clirter";
    public static final String DEFRES = "defres";
    public static final String REFFSS = "reffs";
    public static final String REFFTO = "reffto";
    public static final String RESREF = "resref";
    public static final String CMPSCS = "Cmpscs";
    public static final String CMPSS = "cmpss";
    public static final String COMMREM = "comrem";
    public static final String TYPE_RE_SCR = "Type_Re_scr";
    public static final String SLEEP_LESS = "sleep_less";
    public static final String LACK_IN_HOBBIES = "lack_int_hobbies";
    public static final String LACK_IN_DAILYACT = "lack_int_dailyact";
    public static final String ANX = "anx";
    public static final String SEN_IMP_DOOM = "sen_imp_doom";
    public static final String DIFF_THINK_CLR = "diff_think_clr";
    public static final String PREF_ALONE = "pref_alone";
    public static final String FELT_UNHAPPY = "felt_unhappy";
    public static final String FELT_HOPELESS = "felt_hopeless";
    public static final String FELT_HELPLESS = "felt_helpless";
    public static final String BEEN_WORRIED = "been_worried";
    public static final String CRIED = "cried";
    public static final String TAKING_LIFE = "taking_life";
    public static final String LOSS_APETITE = "loss_appetite";
    public static final String RETRSNL_BURNING = "retrsnl_burning";
    public static final String INDIGESTION = "indigestion";
    public static final String NAUSEA = "nauseamh";
    public static final String CONSTIPATION = "constipationmh";
    public static final String CIRCUMCISION_DATE = "circumcision_date";
    public static final String DIFF_BREATHING = "diff_breathing";
    public static final String FELT_TREM = "felt_trem";
    public static final String NUMB_HANDFEET = "numb_handsfeet";
    public static final String SEN_TEN_NECKSHOUL = "sen_ten_neckshoul";
    public static final String HEADACHES = "headaches";
    public static final String PAIN_BODY = "pain_body";
    public static final String FREQ_URINE = "freq_urine";
    public static final String SCREENING_FORM_TYPE = "screening_form_type";
    public static final String SERVER_RESPONSE = "results";
    public static final String SERVER_RESULT = "result";
    public static final String DATA = "data";
    public static final String PATIENT_ENCOUNTERS = "patient_encounters";
    public static final String ENCOUNTERS_COUNT = "encountersCount";
    public static final String ENCOUNER_NAME = "encounter_name";


    public static final String VIEW_PATIENT_HISTORY = "VIEW_HISTORY";
    public static final String FIND_PATIENT = "FIND_PATIENT";
    public static final String GET_PATIENT_INFO = "GET_PATIENT_INFO";

    public static final String GET_EVALUATORS = "GET_EVALUATORS";
    public static final String EFFECTED_FOOT = "effected_foot";
    public static final String GET_PATIENT_IMAGES_INFO = "Patient Images";
    public static final String PATIENT_IMAGES = "Patient Images";
    public static final String VALIDATE_USER = "VALIDATE_USER";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String PROVIDER_UUID = "provider_uuid";
    public static final String GPS_PARAM = "Form Location GPS Coordinates";
    public static final String SRQ10 = "SRQ-10";
    public static final String AKUADS = "AKUADS";
    public static final String SCORE_TYPE = "SCORE_TYPE";
    public static final String ENCOUNTER_OCCURANCES = "encounter_occurances";
    public static final String PROJECT_IDENTIFIER = "Patient Identifier";
    public static final String PQ_PROJECT_IDENTIFIER = "PQ Identifier";
    public static final String CSC_PROJECT_IDENTIFIER = "CSC Identifier";
    /*
     * Encounter Params
     */
    /*
     * Form Questions Params
     */
    //PATIENT CREATION
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String SEX = "sex";
    public static final String DOB = "dob";
    public static final String PROVINCE = "province";
    public static final String DISTRICT = "district";
    public static final String CITY = "city";

    //SSI
    //GENERAL SSI VARIABLES
    public static final String MR_NUMBER = "MR Number";
    public static final String SSI_MR_NUMBER = "SSI MR Number";
    public static final String ICM_ID_NUMBER = "e29f1c01-8935-40d9-bae4-0af51b1ba140";
    public static final String SSI_STUDY_NO = "95d5eaac-aa3c-4c56-91a1-709c4477886e";
    public static final String FORM_DATE = "a17bbfce-4744-41fd-b0ce-19609d7bc019";
    public static final String PROCEDURE_DATE = "160715AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String USER_ID = "71100371-ff4f-4f06-b594-4433f15b5092";
    public static final String PATIENT_NAME = "patient_name";
    public static final String FATHER_HUSBAND_NAME = "father_husband_name";

    //PRE-OP DEMOGRAPHICS
    public static final String PRE_OP_PATIENT_AGE = "patient_age";
    public static final String PRE_OP_AGE_UNIT = "age_unit";
    public static final String PRE_OP_GENDER = "gender";
    public static final String PRE_OP_MARITAL_STATUS = "de1d9979-000a-4577-86fa-e1594f382277";
    public static final String ADDRESS = "address";
    public static final String AREA = "address3";
    public static final String PRE_OP_RURAL_URBAN = "7b395da2-22b1-4999-a13a-2d599e1cfd38";
    public static final String PRE_OP_CONTACT1 = "5026a3fc-468a-4b3b-a0c8-4c3896b22360";
    public static final String PRE_OP_CONTACT1_RELATIONSHIP = "c21c9e42-6d3c-40f0-aa02-889b0bf77b04";
    public static final String PRE_OP_CONTACT2 = "5d72e935-239c-4119-b4ed-f844b72d44a2";
    public static final String PRE_OP_CONTACT2_RELATIONSHIP = "938484f4-a765-4910-87b3-9d3708f8466e";
    public static final String PRE_OP_ATTENDED_SCHOOL = "c7d279d3-faf5-4113-a579-20698bd9cf64";
    public static final String PRE_OP_FORMAL_SCHOOLING_YEARS = "6374b921-4ab2-4f99-b86c-2a5f399dd26f";
    public static final String PRE_OP_INFORMAL_SCHOOLING = "2de64266-cd53-4277-9e43-17598ae95ff5";
    public static final String PRE_OP_PRIMARY_LANGUAGE = "163230AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String PRE_OP_PRIMARY_LANGUAGE_OTHER = "8925bebf-a953-42b8-b96b-20ba01b970ea";
    public static final String PRE_OP_CAN_READ_WRITE = "b0e93cb8-f532-467c-a8b7-93cc94ac6825";
    public static final String PRE_OP_WHO_ASSIST = "03eb2b49-02bf-4251-9516-b9eda249ac79";
    public static final String PRE_OP_WHO_ASSIST_OTHER = "be753f1a-da78-4aaa-b504-a91cfb6df2f5";
    public static final String PRE_OP_ASSIST_YEARS_SCHOOLING = "19fa2137-0c13-45e7-b483-a564052e2b5a";
    public static final String PRE_OP_ASSIST_INFORMAL_SCHOOL = "e7765471-04e4-42ac-8ca9-e6551e47a07f";
    public static final String PRE_OP_HAVE_BANK = "7d961bd6-0dc6-4972-8ec4-7c863eee405e";
    public static final String PRE_OP_HAVE_CHAIR = "0f0f69ca-d1ad-4e61-a95b-4954f73ecf80";
    public static final String PRE_OP_HAVE_MAITRESS = "abd0923d-c680-41f8-959f-e4a32c3841d9";
    public static final String PRE_OP_HAVE_REFRIGRATOR = "93bf6d1c-d555-46cf-85e6-ebc0d89f8434";
    public static final String PRE_OP_HAVE_TABLE = "fc274c7e-325d-45ab-8e4e-c7356c18efee";
    public static final String PRE_OP_HAVE_TELEVISION = "0326b00f-eece-4663-bf1b-c11c76afe45c";
    public static final String PRE_OP_HAVE_KITCHEN = "18f8e836-6f52-44a3-8a23-09c83b55ac09";
    public static final String PRE_OP_MONTHLY_INCOME = "3f82c24e-a64e-45bc-9ab0-7e0c46a6adb6";
    public static final String PRE_OP_NO_ROOMS = "2569af4d-7dfc-4c8c-8386-98292f5af963";
    public static final String PRE_OP_MEMBERS = "1474AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String PRE_OP_NO_SLEEP_HOUSE = "";
    public static final String PRE_OP_TOILET_FACILITY = "e961c9d9-5fb1-4c76-8c9b-3c3287314ae5";
    public static final String PRE_OP_TOILET_FACILITY_OTHER = "da3a6a6e-759c-42a9-906e-4a821771a234";
    public static final String PRE_OP_WATER_SOURCE = "9db9a915-036e-435e-b934-1439bc7a84b8";
    public static final String PRE_OP_WATER_SOURCE_OTHER = "bad2e37d-1e85-4241-aeb1-3157909f793c";
    public static final String PRE_OP_HAVE_MOBILE = "3133c6c6-a4da-4357-a1b1-4f295149031f";
    public static final String PRE_OP_HAVE_MOBILE_ACCESS = "6caaa152-8f18-44ec-b8b1-89cd1736a0da";
    public static final String PRE_OP_SMS = "b9a3bfe3-224c-4f70-ad2c-f529c208adcf";
    public static final String SMS_LANGUAGE = "24ca5872-641c-4c94-8116-a8d537fad531";
    public static final String PRE_OP_MOBILE_CAMERA = "32f603ff-77de-4a8f-ab30-c94bd7b05c95";
    public static final String PRE_OP_MOBILE_USAGE_DURATION = "0c458c12-293a-4225-b522-1581f40887a1";
    public static final String PRE_OP_MOBILE_USAGE_UNIT = "36eab11a-4b2c-4ef3-9ba7-da1520a82207";
    public static final String PRE_OP_MOBILE_INTERNET = "a888c602-2239-40c7-a3aa-0e9229cba1cd";
    public static final String PRE_OP_LIERACY_COMFORT = "3608e3f5-2cd2-41eb-a397-e06148feeac2";
    public static final String PRE_OP_ASSISTANT_EDUCATION_OTHER = "e7765471-04e4-42ac-8ca9-e6551e47a07f";

    //POST-OPERATIVE DEMOGRAPHICS
    public static final String POST_OP_DATE_SURGERY = "d237e9aa-56a9-4eec-ba64-84391418fdce";
    public static final String POST_OP_TYPE_PLANNED_SURGER = "da2a845c-9576-423f-b400-b8a20cdf71e4";
    public static final String POST_OP_DEPARTMENT_SURGERY = "8f4a7475-b3b6-49dd-af3f-3834da953d00";
    public static final String POST_OP_HAS_SURGERY_PERFORMED = "6875e6bc-4311-4be0-8841-8d9f487a46ad";
    public static final String POST_OP_SURGERY_PERFORMED_OTHER = "0e6e3201-7a68-4a76-a7e5-ec320f1bc98e";
    public static final String POST_OP_SURGERY_CLASSFICATION = "860955bf-e1ae-4be9-984c-c5fb30dc6c16";
    public static final String POST_OP_NAME_PROCEDURE = "02ee4b4d-3fd4-42f5-8def-d27dd50b4562";
    public static final String POST_OP_CAT_SURGERY = "66174e5a-202d-47d7-a7c4-29216b953e7f";
    public static final String POST_OP_INCISION_OTHER = "77836612-af0a-447e-95c8-0d1be28a3979";
    public static final String POST_OP_DURATION_OTHER = "ed4cd314-ce4a-4799-ae49-fcc5ebaf6a00";
    public static final String POST_OP_INCISION_TYPE = "4a03bbaa-7fdb-4166-8362-d8060128837a";
    public static final String POST_OP_DURATION_SURGERY = "146ebe1e-f60d-4b16-836e-3b2b9f1d32e3";
    public static final String POST_OP_ASA_SCORE = "f32c7d36-85e5-4b19-ba8a-2faad30fa01d";
    public static final String POST_OP_ANTIBIOTICS_PROPHYLAXIS = "55a633b1-7065-4634-ba54-d26a3df9b01b";
    public static final String POST_OP_ANESTHESIA_TYPE = "f7f4b513-9885-42b6-897a-d628a7476a22";

    // SSI DETECTION FORM

    public static final String SSI_DETECTION_SSI_DETECTED = "f37075a2-8312-4991-bd9d-54291a8b9558";
    public static final String SSI_DETECTION_READMISSION = "3623f4e2-93e4-4b4a-96ed-8355108bc3bb";
    public static final String SSI_DETECTION_DURING_IN_PATIENT_DAY = "e7950882-a0a3-46b8-9aad-11125960471c";
    public static final String SSI_DETECTION_FUP_WITH_SURGEON = "17cf65e0-1b9a-4d48-ba0e-91c03065c6ea";

    public static final String SSI_DETECTION_DIAGNOSIS_DATE = "159948AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String SSI_DETECTION_DATE_SINCE_SURGERY = "8aa0f407-6055-4e8f-95bd-6da60fc11afa";
    public static final String SSI_DETECTION_FOLLOWUP_STATUS = "605ed0ae-68eb-469b-8275-9669adea1ba3";
    public static final String SSI_DETECTION_REASON_OF_APPOINTMENT = "fd35258c-b547-4338-9ed7-c2d536808476";
    public static final String SSI_DETECTION_EMERGENCY_REPORT = "f09c4a89-2f5a-4828-8d32-2a327d893c81";
    public static final String SSI_DETECTION_FOLLOWUP_CALL_DATE = "87419d15-824b-4186-837e-03453835f565";
    public static final String SSI_DETECTION_PATIENT_CALL_DATE = "221a61ba-543f-4249-908d-a0dc07a1b2af";
    public static final String SSI_DETECTION_REASON_OTHER = "e30468cb-2e82-4025-8644-518c32f92a1a";
    public static final String SSI_DETECTION_PATIENT_DIAGNOSIS_BY = "f01d9a13-ee94-49e7-91ab-c0c0508b3163";
    public static final String SSI_DETECTION_SURGEON_NAME = "2c3128ea-9b8a-4caf-89bb-4c1b0a7d1d21";
    public static final String SSI_DETECTION_ICN_NAME = "5087ba27-007b-4dd7-a2a8-93f71397f86a";

    //SCREEINING CALL-IN
    public static final String SCREENING_TELEPHONE = "26b045a7-1368-4873-a30a-dec3df3a7fff";
    public static final String SCREENING_DATE_CALLED = "221a61ba-543f-4249-908d-a0dc07a1b2af";
    public static final String SCREENING_TIME_CALLED = "";
    public static final String SCREENING_APPOINTMENT_DOCTOR = "f7dd2313-0c85-4ec7-a60e-230d55f1f9db";
    public static final String SCREENING_APPOINTMENT_DATE = "94b5766d-880d-42ab-b421-ef6735e9eb19";
    public static final String SCREENING_APPOINTMENT_TIME = "";
    public static final String SCREENING_PATIENT_REASON_INFECT = "a78dd665-6846-479a-893d-a7c169e9c498";
    public static final String SCREENING_PATIENT_REASON_CALL = "0a3db6ab-4f99-4ff9-b79f-9f463de6e2d2";
    public static final String SCREENING_PATIENT_REASON_OTHER = SSI_DETECTION_REASON_OTHER;
    public static final String SCREENING_COLORED_DRAINAGE = "eb380f8b-e654-4aa4-8229-6c93535a9bfe";
    public static final String SCREENING_COLORED_DRAINAGE_TIME = "86fcaabc-5cbe-48a5-a542-de22f668513c";
    public static final String SCREENING_INCREASING_SWELL = "4815675b-2ad2-4c9e-914f-5481f5726bb8";
    public static final String SCREENING_INCREASING_SWELL_TIME = "b1de5526-15a8-42a7-af85-24dc91fe3200";
    public static final String SCREENING_INCREASING_FEVER = "140238AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String SCREENING_INCREASING_FEVER_TIME = "b19a09c8-6a8e-4c74-8415-b8b8c920ccb6";
    public static final String SCREENING_INCISION_EDGE = "c95b3658-a5c3-444a-92c6-e7a1e33957d0";
    public static final String SCREENING_INCISION_EDGE_TIME = "ed3edbda-dac5-41af-95ed-6d5495e506e9";
    public static final String SCREENING_INCREASING_REDNESS = "031da2b4-877f-4b22-83bb-d125d0b455c7";
    public static final String SCREENING_INCREASING_REDNESS_TIME = "e80dcc59-5043-4ff2-97b7-cbde5d4be6f6";
    public static final String SCREENING_INCREASING_WARMTH = "96ce7429-5d3e-46ca-8225-8f319368e10e";
    public static final String SCREENING_INCREASING_WARMTH_TIME = "721ca4a0-cdbc-4900-9a79-c7986c9e5549";
    public static final String SCREENING_INCREASING_PAIN = "2b614d3b-ebc3-4f3a-b9e7-b645aea3c6b7";
    public static final String SCREENING_INCREASING_PAIN_TIME = "421df799-3536-4c22-8819-9e6960f73aef";
    public static final String SCREENING_THINKS_INFECTION = "13953d94-b1c1-4d0b-a6d9-535e0c56f524";
    public static final String SCREENING_THINKS_INFECTION_TIME = "08840a04-abe7-4f1d-a65c-f07198a32465";
    public static final String SCREENING_NO_SIGNS_TIME = "dc2f9bd5-ae9b-416f-b115-94cbcea6b09b";
    public static final String SCREENING_ANTIBIOTICS = "1195AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String SCREENING_DOP = "160715AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String SCREENING_DAYS_SURGERY = "8aa0f407-6055-4e8f-95bd-6da60fc11afa";
    public static final String SCREENING_FEVER_INTENSITY = "9ef8eccf-3a3c-4278-a085-204298e2b9b7";
    public static final String SCREENING_FEVER_MEASURE = "86ca6f3f-074d-49b1-bf53-8343bfd81db5";
    public static final String SCREENING_FEVER_READING = "1610f711-79b4-4b25-afb8-efe70ef67add";

    //POST-OPERATIVE FOLLOW-UP
    public static final String POST_OP_FUP_FOLLOWUP_STATUS = "605ed0ae-68eb-469b-8275-9669adea1ba3";
    public static final String POST_OP_FUP_FOLLOW_DATE = "6a7d8cb3-0eec-4b17-8b4f-d5ea46d5158f";
    public static final String POST_OP_FUP_HAS_OTHER_HEALTHCARE = "0e79a919-b78a-4ff1-bad2-7396473a472d";
    public static final String POST_OP_FUP_OTHER_HEALTHCARE = "7f6531c6-d5d2-4435-b482-764c01d7786f";
    public static final String POST_OP_FUP_OTHER_HEALTHCARE_WHY = "b0d84f0b-5d75-4c72-874d-ebbbc669484b";
    public static final String POST_OP_FUP_OTHER_HEALTHCARE_TREATMENT = "1185AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String POST_OP_FUP_OTHER_HEALTHCARE_TREATMENT_OTHER = "149bff0e-d897-4a63-813c-b1b80544fc62";
    public static final String POST_OP_FUP_HCW_HAS_INFECTION = "13953d94-b1c1-4d0b-a6d9-535e0c56f524";
    public static final String POST_OP_FUP_HCW_HAS_INFECTION_TIME = "08840a04-abe7-4f1d-a65c-f07198a32465";


    //POST-OPERATIVE FOLLOW UP-CALL
    /*public static final String POST_OP_FUP_REMINDER_NO_REMINDER = "6811ae20-bc95-4438-a28f-11bb2a9fd07a";
    public static final String POST_OP_FUP_DOFC = "87419d15-824b-4186-837e-03453835f565";
    public static final String POST_OP_FUP_SELF_SCREEN = "e46c225b-790f-4348-9efe-1b980e568137";
    public static final String POST_OP_FUP_BROCHURE = "89891260-b473-4b1f-b9db-dd5e049d0d92";
    public static final String POST_OP_FUP_WOUND_HEAL = "1b192b9c-43c1-40e3-bf6a-ac76887cd19d";
    public static final String POST_OP_FUP_FU_DR = "0cb3b175-8085-4aef-a947-3e8eaf0a1e11";
    public static final String POST_OP_FUP_FU_DATE = "6a7d8cb3-0eec-4b17-8b4f-d5ea46d5158f";
    public static final String POST_OP_FUP_FU_TREATMENT = "6f6f4a2e-d773-4ffe-b67c-b9c605fa40c0";
    public static final String POST_OP_FUP_TREATMENT_OTHER = "";
    public static final String POST_OP_FUP_SURGEON_DIAGNOSIS = "";
    public static final String POST_OP_FUP_FU_ELSE = "";
    public static final String POST_OP_FUP_ELSE_PROFESSION = "";
    public static final String POST_OP_FUP_FU_ELSE_DATE = "";
    public static final String POST_OP_FUP_ELSE_TREATMENT = "";
    public static final String POST_OP_FUP_FU_ELSE_WHERE = "";
    public static final String POST_OP_FUP_ELSE_NAME = "";
    public static final String POST_OP_FUP_ELSE_CONTACT = "";
    public static final String POST_OP_FUP_ELSE_TREATMENT_OTHER = "";
    public static final String POST_OP_FUP_ELSE_DIAGNOSIS = "";
    public static final String POST_OP_FUP_READMISSION = "";
    public static final String POST_OP_FUP_NOT_INDUS = "";
    public static final String POST_OP_FUP_HELPLINE_SYMPTOMS = "";*/

    public static final String INTERVENTION_ARM = "intervention_arm";
    public static final String POST_OP_FUP_ICM_ID = "e29f1c01-8935-40d9-bae4-0af51b1ba140";
    public static final String POST_OP_FUP_INTERVENTION_ARM = "6811ae20-bc95-4438-a28f-11bb2a9fd07a";
    public static final String POST_OP_FUP_CONTACT_ESTABLISHED = "fc13140e-b46f-409a-8dc0-16960ffece12";
    public static final String POST_OP_FUP_SELF_SCREEN = "e46c225b-790f-4348-9efe-1b980e568137";
    public static final String POST_OP_FUP_BROCHURE = "89891260-b473-4b1f-b9db-dd5e049d0d92";
    public static final String POST_OP_FUP_WOUND_HEAL = "1b192b9c-43c1-40e3-bf6a-ac76887cd19d";
    public static final String POST_OP_FUP_FOLLOWUP_DOCTOR = "0cb3b175-8085-4aef-a947-3e8eaf0a1e11";
    public static final String POST_OP_FUP_FOLLOWUP_CALL_DATE = "87419d15-824b-4186-837e-03453835f565";
    public static final String POST_OP_FUP_FOLLOWUP_DATE = "6a7d8cb3-0eec-4b17-8b4f-d5ea46d5158f";
    public static final String POST_OP_FUP_FOLLOWUP_TREATMENT = "6f6f4a2e-d773-4ffe-b67c-b9c605fa40c0";
    public static final String POST_OP_FUP_FOLLOWUP_TREATMENT_OTHER = "b6c9f4ec-3af0-454e-8c74-8d6c540a3fbc";
    public static final String POST_OP_FUP_SURGEON_DIAGNOSIS = "a220c2e2-b8a3-4fc2-a1b1-5eac2e7f42cd";
    public static final String POST_OP_FUP_FOLLOWUP_ELSE = "7efa2df7-4054-4f34-9407-cc3e6be8177a";
    public static final String POST_OP_FUP_ELSE_PROFESSION = "3fcd7855-2a7d-4c74-9f62-abe49b487fc2";
    public static final String POST_OP_FUP_ELSE_DATE = "7bbbf09b-3101-4ecf-8934-f3f889b1cbce";
    public static final String POST_OP_FUP_ELSE_WHERE = "dface81a-f5d4-4cf8-aa2a-0f2666d61f23";
    public static final String POST_OP_FUP_ELSE_NAME = "4e9a389e-e7fd-4619-97df-3b12343a50e2";
    public static final String POST_OP_FUP_ELSE_CONTACT = "4c7e5e55-8a6a-49ca-93bb-663887eea95b";
    public static final String POST_OP_FUP_ELSE_TREATMENT = "85e44842-2a6e-4a74-b28e-97ed2424cf0d";
    public static final String POST_OP_FUP_ELSE_TREATMENT_OTHER = "149bff0e-d897-4a63-813c-b1b80544fc62";
    public static final String POST_OP_FUP_ELSE_DIAGNOSIS = "07df6c71-4da5-43b9-a4ad-e82a9558e321";
    public static final String POST_OP_FUP_READMISSION = "1a92c860-1956-44f2-83d6-9a51588a4665";
    public static final String POST_OP_FUP_NOT_INDUS = "e3a454b3-3a49-41b5-a7cd-57f2f842a50f";
    public static final String POST_OP_FUP_COLORED_DRAINAGE = "eb380f8b-e654-4aa4-8229-6c93535a9bfe";
    public static final String POST_OP_FUP_COLORED_DRAINAGE_TIME = "86fcaabc-5cbe-48a5-a542-de22f668513c";
    public static final String POST_OP_FUP_INCREASING_SWELL = "4815675b-2ad2-4c9e-914f-5481f5726bb8";
    public static final String POST_OP_FUP_INCREASING_SWELL_TIME = "b1de5526-15a8-42a7-af85-24dc91fe3200";
    public static final String POST_OP_FUP_INCREASING_FEVER = "140238AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String POST_OP_FUP_INCREASING_FEVER_TIME = "b19a09c8-6a8e-4c74-8415-b8b8c920ccb6";
    public static final String POST_OP_FUP_INCISION_EDGE = "c95b3658-a5c3-444a-92c6-e7a1e33957d0";
    public static final String POST_OP_FUP_INCISION_EDGE_TIME = "ed3edbda-dac5-41af-95ed-6d5495e506e9";
    public static final String POST_OP_FUP_INCREASING_REDNESS = "031da2b4-877f-4b22-83bb-d125d0b455c7";
    public static final String POST_OP_FUP_INCREASING_REDNESS_TIME = "e80dcc59-5043-4ff2-97b7-cbde5d4be6f6";
    public static final String POST_OP_FUP_INCREASING_WARMTH = "96ce7429-5d3e-46ca-8225-8f319368e10e";
    public static final String POST_OP_FUP_INCREASING_WARMTH_TIME = "721ca4a0-cdbc-4900-9a79-c7986c9e5549";
    public static final String POST_OP_FUP_INCREASING_PAIN = "2b614d3b-ebc3-4f3a-b9e7-b645aea3c6b7";
    public static final String POST_OP_FUP_INCREASING_PAIN_TIME = "421df799-3536-4c22-8819-9e6960f73aef";
    public static final String POST_OP_FUP_ANTIBIOTICS_24H = "1556AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String POST_OP_FUP_ICM_ASSESSMENT = "dc2f9bd5-ae9b-416f-b115-94cbcea6b09b";
    public static final String POST_OP_FUP_HELPLINE_SYMPTOMS = "adc4bb95-4d38-4e51-a804-a0cdf3477bb3";

    //SURGEON EVALUATION FORM
    public static final String SURGEON_EVAL_DATE_ASSESSMENT = "69c4ae23-adcf-427e-ab74-23504f1f0fcb";
    public static final String SURGEON_EVAL_DATE_OF_PROCEDURE = "160715AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String SURGEON_EVAL_FOLLOWUP_STATUS = "605ed0ae-68eb-469b-8275-9669adea1ba3";
    public static final String SURGEON_EVAL_REASON_OF_URGENT_APPOINTMENT = "71c3eef2-99dd-4f28-8ad4-52379443e780";
    public static final String SURGEON_EVAL_DOC_INTERVENTION = "f255c04b-552a-48d1-9f37-ce615fd9a2e6";
    public static final String SURGEON_EVAL_ICM_ID = "e29f1c01-8935-40d9-bae4-0af51b1ba140";
    public static final String SURGEON_EVAL_ICN_NAME = "5087ba27-007b-4dd7-a2a8-93f71397f86a";
    public static final String SURGEON_EVAL_SURGEON_NAME = "2c3128ea-9b8a-4caf-89bb-4c1b0a7d1d21";
    public static final String SURGEON_EVAL_FEVER_HIGH_LOW = "9ef8eccf-3a3c-4278-a085-204298e2b9b7";
    public static final String SURGEON_EVAL_FEVER_HAS_MEASURED = "86ca6f3f-074d-49b1-bf53-8343bfd81db5";
    public static final String SURGEON_EVAL_FEVER_READING = "b29debf1-cac3-434e-a72a-1caa1bdc7876";
    public static final String SURGEON_EVAL_ANTIBIOTICS_24H = "1556AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String SURGEON_EVAL_WOUND_EXPLORATON = "ed021e3d-7516-44ea-8072-b517c64812f0";
    public static final String SURGEON_EVAL_COLORED_DRAINAGE = "eb380f8b-e654-4aa4-8229-6c93535a9bfe";
    public static final String SURGEON_EVAL_INCREASING_SWELL = "4815675b-2ad2-4c9e-914f-5481f5726bb8";
    public static final String SURGEON_EVAL_INCREASING_FEVER = "140238AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String SURGEON_EVAL_INCREASING_FEVER_TIME = "b19a09c8-6a8e-4c74-8415-b8b8c920ccb6";
    public static final String SURGEON_EVAL_INCISION_EDGE = "c95b3658-a5c3-444a-92c6-e7a1e33957d0";
    public static final String SURGEON_EVAL_INCREASING_REDNESS = "031da2b4-877f-4b22-83bb-d125d0b455c7";
    public static final String SURGEON_EVAL_INCREASING_WARMTH = "96ce7429-5d3e-46ca-8225-8f319368e10e";
    public static final String SURGEON_EVAL_INCREASING_PAIN = "2b614d3b-ebc3-4f3a-b9e7-b645aea3c6b7";
    public static final String SURGEON_EVAL_THINKS_INFECTION = "13953d94-b1c1-4d0b-a6d9-535e0c56f524";
    public static final String SURGEON_EVAL_SURGEON_DIAGNOSIS = "a220c2e2-b8a3-4fc2-a1b1-5eac2e7f42cd";
    public static final String SURGEON_EVAL_SSI_TYPE = "164520AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String SURGEON_EVAL_ANTIBOTIC_REQD = "afefcf5e-0c69-49cb-85ad-ca8088283928";
    public static final String SURGEON_EVAL_DRUG_DOSAGE = "0633ffab-07e2-40e9-ba1d-1b0503d19b2a";
    public static final String SURGEON_EVAL_CULTURE_TAKEN = "d7ceeaae-302b-4ace-9865-e4afdb478605";
    public static final String SURGEON_EVAL_DATE_OF_CULTURE_TAKE = "861d3bdb-096f-4fd7-8f6f-110f134823ae";
    public static final String EVALUATION_TYPE = "d975a079-b835-4e64-a5f3-0f1a113cbb8d";
    public static final String ICM_EVALUATION = "6f1a1d5b-f715-41a3-84eb-6f3da8b7768d";
    public static final String FINAL_EVALUATION = "0358928f-2ac7-49c1-8a22-d04dd2dd2628";
    public static final String SURGEON = "96edc8a7-8bbc-47c3-a1ef-1c41087dfad5";
    public static final String ICN = "7b728f10-a0d1-490b-8b71-746ab879cf6d";
    public static final String CONSENT_GIVEN_BY = "798d1b29-85cf-4a23-90a7-77ae3fa651f3";

    //Circumcision
    // public static final String PATIENT_ID = "patientId";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String CONCEPT_GROUP_MEMBERS = "conceptGroupMembers";
    public static final String DATE_CALLED = "dateCalled";
    public static final String DATE_OF_DIAGNOSIS = "dateOfDiagnosis";
    public static final String DATE_OF_PROCEDURE = "dateOfProcedure";
    public static final String FOLLOW_UP_STATUS = "followupStatus";
    public static final String DATE_OF_FOLLOWUP = "dateOfFollowUp";
    public static final String URGENT_REASON = "urgentReason";
    public static final String PLACE_OF_DELIVERY = "placeOfDelivery";
    public static final String DIFFERENT_FACILITY = "differentFacility";
    public static final String NO_OF_CHILDREN = "noOfChildren";
    public static final String RELIGION_OTHER = "religionOther";
    public static final String FORMAL_SCHOOL_FATHER = "formalSchoolFather";
    public static final String IN_FORMAL_SCHOOL_FATHER = "inFormalSchoolFather";
    public static final String IN_FORMAL_SCHOOL_FATHER_OTHER = "inFormalSchoolFatherOther";
    public static final String FORMAL_SCHOOL_MOTHER = "formalSchoolMother";
    public static final String IN_FORMAL_SCHOOL_MOTHER = "inFormalSchoolMother";
    public static final String IN_FORMAL_SCHOOL_MOTHER_OTHER = "inFormalSchoolMotherOther";
    public static final String OCCUPATION_FATHER = "fatherOccupation";
    public static final String OCCUPATION_FATHER_OTHER = "fatherOccupationOther";
    public static final String SPONTANEOUS_RESPONSE = "spontaneousResponse";
    public static final String OCCUPATION_MOTHER = "motherOccupation";
    public static final String FAMILY_SYSTEM = "familySystem";
    public static final String CIRCUMCISION_REASON = "circumcisionReason";
    public static final String CIRCUMCISION_REASON_OTHER = "circumcisionReasonOther";
    public static final String CIRCUMCISION_CLINIC = "circumCisionClinic";
    public static final String CIRCUMCISION_CLINIC_OTHER = "circumCisionClinicOther";
    public static final String CIRCUMCISION_BROUCHER = "circumCisionBroucher";
    public static final String CIRCUMCISION_BROUCHER_HELP = "circumCisionBroucherHelp";
    public static final String CIRCUMCISION_VIDEO = "circumCisionVideo";
    public static final String CIRCUMCISION_VIDEO_HELP = "circum_cision_video_help";
    public static final String CONVULSIONS = "Convulsions";
    public static final String BLEEDING_DISORDER = "bleeding_disorder";
    public static final String TERM_BABY = "term_baby";
    public static final String CIRCUMCISION_NOTES = "Notes";
    public static final String HOSPITALIZATION_OF_BABY = "hospitalization_of_baby";
    public static final String HOSPITALIZATION_REASON = "hospitalization_reason";
    public static final String HOSPITALIZATION_DURATION = "hospitalization_duration";
    public static final String VITAMIN_K_AFTER_BIRTH = "vitamin_k_after_birth";
    public static final String VITAMIN_K_CIRCUMCISION = "vitamin_k_circumcision";
    public static final String VITAMIN_K_CIRCUMCISION_OTHER = "vitamin_k_circumcision_other";
    public static final String FAMILY_HO_BLEEDING_DISORDERS = "family_ho_bleeding_disorders";
    public static final String FAMILY_HO_BLEEDING_DISORDERS_OTHER = "family_ho_bleeding_disorders_other";
    public static final String TEMPERATURE = "temperature";
    public static final String TEMPERATURE_NORMAL = "temperature_normal";
    public static final String PULSE = "pulse";
    public static final String PULSE_NORMAL = "pulse_normal";
    public static final String RESPIRATORY = "respiratory";
    public static final String RESPIRATORY_NORMAL = "respiratory_normal";
    public static final String JAUNDICE = "jaundice";
    public static final String TYPE_OF_JAUNDICE = "type_of_jaundice";
    public static final String SEVERITY_OF_JAUNDICE = "severity_of_jaundice";
    public static final String AGE_OF_BABY = "age_of_baby";
    public static final String AGE_OF_BABY_DAYS = "age_of_baby_days";
    public static final String AGE_NOTES = "age_notes";
    public static final String WEIGHT_OF_BABY = "weight_of_baby";
    public static final String WEIGHT_OF_BABY_KG = "weight_of_baby_kg";
    public static final String PHYSICAL_ANY_OTHER_ABNORMAL_FINDINGS = "physical_any_other_abnormal_findings";
    public static final String GENITAL_ANY_OTHER_ABNORMAL_FINDINGS = "genital_any_other_abnormal_findings";
    public static final String CONVELSION_DETAILS = "3954d1e5-d2fd-48c3-b58e-1eac2217734f";
    public static final String OTHER_FACILITY_DETAILS = "46c72596-acb5-4ca4-a395-b99888b597a8";
    public static final String FAMILY_BLOOD_DISORDER_HISTORY_DETAILS = "0610236d-98f5-40b9-9511-34324b6312e9";
    public static final String HYPOSPADIAS = "hypospadias";
    public static final String EPISPADIAS = "epispadias";
    public static final String CONGENITAL_CHORDEE = "congenital_chordee";
    public static final String MICROPENIS = "micropenis";
    public static final String AMBIGUOUS_GENITALIA = "ambiguous_genitalia";
    public static final String PENOSCROTAL_WEB = "penoscrotal_web";
    public static final String CONGENITAL_BURIED_PENIS = "congenital_buried_penis";
    public static final String BILATERAL_HYDROCELES = "bilateral_hydroceles";
    public static final String BILATERAL_UNDESCENDED_TESTES = "bilateral_undescended_testes";
    public static final String SUITABLE_FOR_CIRCUMCISION = "suitable_for_circumcision";
    public static final String PARENTS_GIVEN_INFORMED_CONSENT = "parents_given_informed_consent";
    public static final String NAME_OF_THE_HEALTH_WORKER = "name_of_the_health_worker";
    public static final String GENITAL_SCREENING = "genital_screening";
    public static final String ANESTHESIA = "anesthesia";
    public static final String PLASTIBELL_DEVICE_SIZE = "plastibell_device_size";
    public static final String HEMOSTASIS = "hemostasis";
    public static final String BABY_TOLERATED = "baby_tolerated";
    public static final String COMPLICATIONS = "complications";
    public static final String WHICH_COMPLICATIONS = "which_complications";
    public static final String BLOOD_LOSS = "blood_loss";
    public static final String NAPA_SUPPOSITORY = "napa_suppository";
    public static final String STABLE_CONDITION = "stable_condition";
    public static final String CARE_INSTRUCTIONS = "care_instructions";
    public static final String CONTACT_INFORMATION = "contact_information";
    public static final String NAME_OF_THE_HEALTH_PROVIDER = "name_of_the_health_provider";
    public static final String CONTACT_ESTABLISHED = "fc13140e-b46f-409a-8dc0-16960ffece12";
    public static final String GENERAL_WELL_BEING_OF_BABY = "";

    //PonSeti
    public static final String PROGRAM_LOCATION = "Program location";
    public static final String PATIENT_IDENTIFIER = "patient_identifier";
    public static final String INDUS_PROJECT_IDENTIFIER = "Indus Project Identifier";
    public static final String PATIENT_ID = "Patient Id";
    public static final String GIVEN_CONSENT = "314138a9-dc8b-4881-969c-94c8b0e2c3a6";
    public static final String ADDRESS_CARD = "8590f1a6-ae3a-428e-aba4-5cfc8326dcf4";
    public static final String HOSPITAL_NAME = "hospital_name";
    public static final String PTCL_NUMBER = "b7f428dc-787b-4d82-bcf8-53da98d24670";
    public static final String PARTICIPANT_BORN = "a493897b-e59b-4b9f-902a-ea91e03003dc";
    public static final String BORN_OTHER = "d7164d73-ebd0-4a79-a9ac-2cbbc9fecb0f";
    public static final String PARTICIPANT_RESIDENCE_AREA = "d791a3aa-47af-4d5b-a55a-5f9eefc4905e";
    public static final String ETHNICITY = "43a8f7e3-7f30-4171-933e-2ddf8f5eb75e";
    public static final String ETHNICITY_OTHER = "16400ed6-9d9f-488b-bc7a-e73a7cbb5b43";
    public static final String HIGHEST_EDUCATION = "1712AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String HIGHEST_EDUCATION_OTHER = "48175591-ae07-42a8-89d0-4eccd56e3e14";
    public static final String HOUSEHOLD_CURRENTLY_WORKING = "670b476c-45c4-4c18-8304-45f601caa0c4";
    public static final String HOUSEHOLD_CURRENTLY_WORKING_OTHER = "c6cd0b68-fdd0-4534-86f2-76ee90bd2a7d";
    public static final String OCCUPATION_OF_HEAD_OF_HOUSEHOLD = "1542AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String OCCUPATION_OF_HEAD_OF_HOUSEHOLD_OTHER = "4670784b-b9d4-4d1a-8b1a-465de6d0c3ed";
    public static final String HOUSEHOLD_INCOME = "60da5456-f345-4928-8f25-a90223b3a8d9";
    public static final String INCOME_EARNED = "04038729-e0a6-4c14-b9ad-3445e21b6dbd";
    public static final String PEOPLE_ABOVE_18 = "088520ab-596f-417e-9b9f-3dd0f943d571";
    public static final String PEOPLE_BELOW_18 = "1474AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String NO_OF_BATHROOMS = "d647420a-dd73-40c7-b829-f43d91ab3104";
    public static final String SEPRATE_KITCHEN = "902b7536-59ff-43bd-8932-772036aa7ff3";
    public static final String SEPRATE_KITCHEN_OTHER = "c5c8dee5-506e-47e8-872f-f5fd9205b7d6";
    public static final String MATERIAL_COOK = "e2c3f67a-8a82-4a08-ac31-42dddffb1781";
    public static final String MATERIAL_COOK_OTHER = "76b2a3a5-edae-4d50-8483-40c0eab66972";
    public static final String CHILD_BORN = "a9d79761-f077-4115-8def-9a596fbf53fe";
    public static final String CHILD_BORN_OTHER = "4ad1bcc3-6ef8-4cb7-b618-ee14823b63ee";
    public static final String COMPLICATIONS_DURING_PREGNANCY = "1b1b99d6-4e3c-4e00-a6b6-7272f7578597";
    public static final String WHAT_COMPLICATIONS_DURING_PREGNANCY = "b2fdeee2-a322-43c2-adc3-a030a16114fd";
    public static final String MODE_OF_DELIVERY_OTHER = "ff5a0f6a-ff86-4386-ab02-a1a3d6c17747";
    public static final String MODE_OF_DELIVERY = "5749d88d-bc0f-45c3-9420-0f64898d5a63";
    public static final String TERM_OF_PREGNANCY = "ac9ef6d4-8f69-4627-9cae-47a46038ba66";
    public static final String WHAT_COMPLICATIONS_DURING_BIRTH = "b315837a-d022-47bc-8d6b-2ed4dcd2fdf1";
    public static final String COMPLICATIONS_DURING_BIRTH = "e4945470-cc45-4906-a5ad-42fcf1329490";
    public static final String SMOKE_DURING_PREGNANCY = "57018392-5640-4452-b096-1987d7cc5bd3";
    public static final String CONSUME_ALCOHOL = "b2c2363d-6eae-4089-bc7a-30b3a8e07602";
    public static final String FAMILY_HISTORY_OF_CLUBFOOT = "366545c6-b91b-4f3b-b542-1dbc3758f1c5";
    public static final String FAMILY_HISTORY_OF_CLUBFOOT_OTHER = "738fc573-30d2-42a5-9589-6fe183d1dfde";
    public static final String RELATIONSHIP_BETWEEN_PARENTS = "2f2248df-593c-41d9-9d98-afa131237b1a";
    public static final String RELATIONSHIP_BETWEEN_PARENTS_OTHER = "d69602ff-8b54-457a-be1b-b905ccc48fc5";
    public static final String DEFORMITY = "c40c7035-7dd3-48c5-bd89-840afa17d742";
    public static final String DEFORMED_SIDE = "4f9f0ae2-e5d4-4007-8ad7-8aad1871f0e9";
    public static final String PREVIOUS_TREATMENT = "aa2a31eb-6beb-4d91-b7d4-9e7a3b0e1e61";
    public static final String TREATMENT_TILL_DATE = "14365e2d-4d9e-46c1-8dd8-ddd7c683ec1d";
    public static final String TREATMENT_TILL_DATE_OTHER = "ec779178-c6d7-4f7a-8e96-3f6a103cf307";
    public static final String NO_PREVIOUS_TREATMENTS = "df2c8441-e1ba-439a-b3b5-7044cffe9694";
    public static final String DIAGNOSED_PRENATALLY = "f0e6f79b-b796-4a15-a55d-6389ccdb3aed";
    public static final String PRENATAL_DIAGNOSIS_WEEK = "7457ca04-1d10-40ad-85ef-a4ad225343f4";
    public static final String CLUBFOOT_CLINIC = "ac013c80-461f-4efa-8e88-78fcace99e94";
    public static final String CLUBFOOT_CLINIC_OTHER = "e906f712-d9ae-466c-ac5f-be56eff70679";
    public static final String NAME_OF_DOCTOR = "0b2c12bc-961d-4acc-bcd4-725f73389d0d";
    public static final String NAME_OF_HOSPITAL = "21cd6cd7-da3c-4c70-9c66-bad648859eb5";
    public static final String COMMENTS = "a64455ff-c6c3-477d-afcd-2165d186d610";
    public static final String FINAL_TREATMENT_VISIT = "1b76a6b4-2140-438c-8e7a-8f1638f0561f";
    public static final String DATE_NEXT_VISIT = "cb4913f3-9aed-4b0e-956f-5211f91a38b6";
    public static final String PATIENT_EXPERIENCE_A_RELAPSE = "6b0d1d29-f6e8-4a1f-932c-0f5fc8112738";
    public static final String RIGHT_VARUS = "95acd3bd-1b70-4862-8390-eb32881862bd";
    public static final String LEFT_VARUS = "17752270-ea5c-4fe9-9315-30d0e5238262";
    public static final String RIGHT_CAVUS = "34936013-fe74-42d0-b7d9-8affcb578bf0";
    public static final String LEFT_CAVUS = "bc58b283-c867-4197-9f0f-3465772d7ba0";
    public static final String RIGHT_ADDUCTUS = "69d04ba3-4b9c-470f-b0e1-f01c73044cbe";
    public static final String LEFT_ADDUCTUS = "be59fcda-435e-49ad-b408-448f67dc7fa2";
    public static final String RIGHT_EQUINUS = "4cd04d61-44c5-4521-b899-5c393049f071";
    public static final String LEFT_EQUINUS = "160ec2c5-39ff-4f17-9e86-ea74b86a3003";
    public static final String POSTERIOR_CREASE_RIGHT_FOOT = "5c039a7c-c01b-4512-88e3-f6bc498d4b3d";
    public static final String POSTERIOR_CREASE_LEFT_FOOT = "b6e93ac7-21ec-4df9-a7b5-fdcd64ee0f0b";
    public static final String EMPTY_HEEL_RIGHT_FOOT = "66b3833b-55f3-47a9-86c0-8137bc16ce50";
    public static final String EMPTY_HEEL_LEFT_FOOT = "5ebe42b4-83ea-4313-aa00-704f9fa8661a";
    public static final String RIGID_EQUINUS_RIGHT_FOOT = "b202ae31-6a6a-4ea6-a473-e30d26dd9b10";
    public static final String RIGID_EQUINUS_LEFT_FOOT = "5376f10d-04ff-48ce-8742-bf35689079af";
    public static final String HIND_FOOT_SCORE_RIGHT_FOOT = "d0ebbb02-3b30-42e7-80a8-02544db9baad";
    public static final String HIND_FOOT_SCORE_LEFT_FOOT = "83890a59-b29f-4072-827b-1666e7e1d780";
    public static final String TALAR_HEAD_COVERAGE_RIGHT_FOOT = "add27148-0ae6-4da6-8aa5-9f4ef44a324b";
    public static final String TALAR_HEAD_COVERAGE_LEFT_FOOT = "86969694-7bd2-41ef-92c6-f6a53ecb847f";
    public static final String MEDIAL_CREASE_RIGHT_FOOT = "6d2127f7-516f-4400-bf03-674bcecb52c1";
    public static final String MEDIAL_CREASE_LEFT_FOOT = "5f3f8c01-c03e-487a-a0ba-62a2427b1a89";
    public static final String CURVED_LATERAL_BORDER_RIGHT_FOOT = "51612535-ed02-48de-802b-2ac7c41b4d7d";
    public static final String CURVED_LATERAL_BORDER_LEFT_FOOT = "09d1e89d-0085-4a28-9c75-6be8e0024be9";
    public static final String MID_FOOT_SCORE_RIGHT_FOOT = "32c7b06f-34fc-43fa-9cef-92cebe35e38f";
    public static final String MID_FOOT_SCORE_LEFT_FOOT = "e7662f88-e87d-4de1-ac8a-3794c5180957";
    public static final String TOTAL_SCORE_RIGHT_FOOT = "9f2aa930-5900-4c15-8f44-3908f80a6426";
    public static final String TOTAL_SCORE_LEFT_FOOT = "985bdd62-0839-4f8f-96b9-f20b84f0a8e0";
    public static final String TREATMENT_RIGHT_FOOT = "c628b21a-02d0-4379-84ff-f00f05c208a8";
    public static final String TREATMENT_LEFT_FOOT = "e9aa5985-d707-4f4d-ab2b-e9e1817da985";
    public static final String WHO_PERFORMED_CASTING_RIGHT_FOOT = "81e7013e-84a5-40bc-acf4-efc4bb55a4ac";
    public static final String WHO_PERFORMED_CASTING_LEFT_FOOT = "f313fb83-8c85-4d82-86b7-81c49d13702b";
    public static final String CAST_NUMBER_RIGHT = "e5c98ed0-129d-4bec-a429-18659cdd1e26";
    public static final String CAST_NUMBER_LEFT = "56e16789-4699-4a84-bdf3-d231bc7f1686";
    public static final String DEGREES_ABDUCTION_RIGHT = "d1c5b437-23ee-45f2-bfb0-e736ed456411";
    public static final String DEGREES_ABDUCTION_LEFT = "8fded521-82e1-494f-ac51-fd0b485bec0b";
    public static final String DEGREES_DORSIFLEXION_RIGHT = "25a23f27-065f-4c20-bd44-92b51d057af4";
    public static final String DEGREES_DORSIFLEXION_LEFT = "d01ded52-27a4-4408-8621-a3df39b9b545";
    public static final String DETAILS_FOR_OTHER_RIGHT_FOOT = "d4f73a9b-17fd-4cc4-8a8e-4bde5db90f38";
    public static final String DETAILS_FOR_OTHER_LEFT_FOOT = "723191a8-5867-4ff0-ad61-de90b9f05ab9";
    public static final String BRACE_COMPLIANCE_RIGT_FOOT = "809d3959-0407-47a3-b65a-a29b6a0224a5";
    public static final String BRACE_COMPLIANCE_LEFT_FOOT = "db6e7c26-6250-4004-8946-e961e00020f6";
    public static final String PROBLEM_RIGHT_FOOT = "d11b2428-e807-4e4a-be2b-5039f944f9af";
    public static final String PROBLEM_LEFT_FOOT = "8a1941f5-8c40-48cd-990b-305d5a1870b5";
    public static final String ACTION_TAKEN_RIGHT_FOOT = "4a191708-cfea-412f-833e-63086d886841";
    public static final String ACTION_TAKEN_LEFT_FOOT = "31f9aa86-8790-4cb3-b831-eca510ed7e09";
    public static final String SURGERY_DATE_RIGHT_FOOT = "8291557a-468c-47a7-9f1f-d06e44b4be38";
    public static final String SURGERY_DATE_LEFT_FOOT = "140afc64-f6dc-432d-a600-7c08cf0f7832";
    public static final String SURGERY_TYPE_RIGHT_FOOT = "e33ed6cf-3ccc-48d0-a004-f123d621eb70";
    public static final String SURGERY_TYPE_LEFT_FOOT = "d5ce4dd1-71d8-4fcf-a7e1-06dc726d57c5";
    public static final String SURGERY_TYPE_RIGHT_FOOT_OTHER = "65637793-42b7-45f4-b652-66ec2f22c5af";
    public static final String SURGERY_TYPE_LEFT_FOOT_OTHER = "403cee95-d5d4-47ff-9fd5-41fcdc8504a8";
    public static final String SURGERY_COMMENTS_RIGHT_FOOT = "93fb9bda-94fa-461e-9066-788746319a20";
    public static final String SURGERY_COMMENTS_LEFT_FOOT = "5d06614e-2c63-4b62-9a90-9f75537bd45e";
    public static final String WERE_THERE_ANY_COMPLICATIONS_RIGHT_FOOT = "15f38530-8371-4ad5-85aa-ece7356021ca";
    public static final String WERE_THERE_ANY_COMPLICATIONS_LEFT_FOOT = "90f035ff-9a0d-46c8-8c8b-0f25655e203a";
    public static final String DESCRIPTION_OF_COMPLICATION_RIGHT_FOOT = "427de948-ca1f-4296-b317-48d85d4d2525";
    public static final String DESCRIPTION_OF_COMPLICATION_LEFT_FOOT = "a3fa1e70-5776-4acc-902b-1c8abc1000dc";
    public static final String DESCRIPTION_OF_COMPLICATION_RIGHT_FOOT_OTHER = "810b88f6-974c-4efa-b1bb-d3a14508cc92";
    public static final String DESCRIPTION_OF_COMPLICATION_RIGHT_FOOT_ER = "1fa7ce46-4bed-4d65-930b-a61d2edc8b29";
    public static final String DESCRIPTION_OF_COMPLICATION_LEFT_FOOT_OTHER = "0b981581-b41c-44a9-ac20-8cddf74494c7";
    public static final String DESCRIPTION_OF_COMPLICATION_LEFT_FOOT_ER = "15c5257e-a88d-44ac-9f70-e29a984ccfa4";
    public static final String TREATMENT_OF_COMPLICATION_RIGHT_FOOT = "529fe446-0fa9-426a-a40e-85c2ae788bf8";
    public static final String TREATMENT_OF_COMPLICATION_LEFT_FOOT = "314d9a7d-8f39-43f3-9a22-944265ca3e45";
    public static final String RESULTS_AFTER_TREATMENT_RIGHT_FOOT = "7d4ea22a-12b5-4941-ad27-b3d903c437dd";
    public static final String RESULTS_AFTER_TREATMENT_LEFT_FOOT = "777287df-0842-4bd0-bd36-b2e9afc0c111";
    public static final String CAPTURE_IMAGE = "picture";


    //SafeCircumcision Form1
    public static final String FORM1_AGE_OF_SON = "e975bb54-a3ce-4d8e-9417-8d40dcc0e7bf";
    public static final String FORM1_AGE_AT_CIRCUMCISION_DAYS = "99ad19fb-cd5e-4133-9569-b428f34102fb";
    public static final String FORM1_AGE_AT_CIRCUMCISION = "11060a48-0419-4408-9514-8ec493eed1ff";
    public static final String FORM1_AGE_BABY_LESS_THAN_90_DAYS = "5a4af0df-6fa7-40b0-8fc7-680cde80bcdb";
    public static final String FORM1_REASON_OTHER = SSI_DETECTION_REASON_OTHER;
    public static final String FORM1_BABY_PRE_TERM = "4cd2b769-62d6-4a1a-976c-0976129d8d69";
    public static final String FORM1_OTHER = "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String FORM1_TERM_BABY_GREATEROREQUAL_37 = "18c4bf8c-bf16-4bcd-afac-d22c8e31f5e9";
    public static final String FORM1_MENTION_WEEK_OF_PRAGNANCY = "146bb172-6003-4030-b7ea-9d0c1a4bfc61";
    public static final String FORM1_32_WEEKS = "e16af596-e639-4ab8-8db7-df75ffd6b9bc";
    public static final String FORM1_33_WEEKS = "537b4796-118c-4137-a4b5-2ee160677483";
    public static final String FORM1_34_WEEKS = "1ed37ffa-38be-4b04-a832-704fd51734d7";
    public static final String FORM1_35_WEEKS = "43880c23-86fb-48ee-9c3c-6a7d745b262c";
    public static final String FORM1_36_WEEKS = "c2b586dc-b3de-4ccb-9335-8d6489279cdc";
    public static final String FORM1_DO_NOT_KNOW = "4b091cc0-e216-498f-a5f6-8322c0ac57cd";
    public static final String FORM1_WEIGHT_OF_BABY_KG = "5089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String FORM1_WEIGHT_OF_BABY_GREATER_OR_EQUAL_THATN_2_POINT_5 = "930e5eb3-e3d9-451f-9f84-f948da937fdd";
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
    public static final String CONTACT_REGISTRY = "contactRegistry";
    public static final String PARAM_CREATE_PATIENT = "createPatient";
    public static final String WIDGET_TYPE = "widget";
    public static final String PERSON_ATTRIBUTE = "person_attribute";
    public static final boolean PERSON_ATTRIBUTE_TRUE = true;
    public static final boolean PERSON_ATTRIBUTE_FALSE = false;
    public static final String CHARACTER = "characters";
    public static final String COVID_RESULT = "covidResult";
    public static final String OTHER_DETAILS = "otherDetails";

}