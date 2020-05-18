package com.ihsinformatics.dynamicformsgenerator.utils;

import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class Global {
	public static PatientData patientData;
	public static String HYRDA_CURRENT_URL;

	public static final long INACTIVITY_LIMIT_MILISECOND=18000000;

	public static String WORKFLOWUUID;

	public static String USERUUID;
	public static String USERNAME;
	public static String PASSWORD;
	public static String PROVIDER;

	public static String APP_LANGUAGE;

	public static String identifierFormat;
	public static String currentCountry;


	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
	public static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
	public static final SimpleDateFormat OPENMRS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	public static final SimpleDateFormat OPENMRS_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss a", Locale.US);

	public static void setDateFormat(String format)
	{
		DATE_TIME_FORMAT = new SimpleDateFormat(format, Locale.US);
	}

	public static String[] errorMessage = {"No response from server", "Could not connect to server", 
		"Invalid http protocol", "An error occured, Invalid response from server", "A patient with same MR number already exists", "Request not completed, error code: ", "Unsupported encoding scheme"};
	public static ArrayList<String> errorMessagesList = new ArrayList<String>(Arrays.asList(errorMessage));
	public static final int DATE_TIME = 6;
	public static final int DATE = 7;
	public static final int TIME = 8;
	public static final int PATIENT_LOAD_VIA_TAG = 9;
	public static final int PERIOD_REOPEN = 10;
	public static final int PERIOD_CREATE = 11;
	public static Boolean AGE_DOB_DEADLOCK = false;
	
	public static List<Integer> SCOREABLE_QUESTIONS = Arrays.asList(new Integer[]{9, 11, /*12,*/13,14,56,15,16,17,18});
}
