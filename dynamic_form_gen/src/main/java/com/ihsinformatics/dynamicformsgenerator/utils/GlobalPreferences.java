package com.ihsinformatics.dynamicformsgenerator.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.LocationSelector.LOCATION;
import java.util.Locale;



public class GlobalPreferences {
	// TODO make this this class generic
	public static enum KEY {
		USERNAME,
		PASSWORD,
		LANGUAGE,
		LOCATION,
		FIRST_RUN,
		PROGRAM
	}
	
	private static GlobalPreferences instance;
	private Context context;
	
	private GlobalPreferences(Context context) {
		this.context = context;
	}
	
	public static GlobalPreferences getinstance(Context context) {
		if(instance == null) {
			instance = new  GlobalPreferences(context);
		}
		
		return instance;
	}
	
	public void addOrUpdatePreference(KEY key, String value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key.toString(), value).apply();
	}

	public void addOrUpdatePreference(KEY key, boolean value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key.toString(), value).apply();
	}
	
	public String findPrferenceValue(KEY key, String defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(key.toString(), defaultValue);
	}

	public boolean findPrferenceValue(KEY key, boolean defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key.toString(), defaultValue);
	}

	
	public LANGUAGE findLanguagePrferenceValue() {
		String language =  PreferenceManager.getDefaultSharedPreferences(context).getString(KEY.LANGUAGE.toString(), LANGUAGE.ENGLISH.toString());
		LANGUAGE l = LANGUAGE.valueOf(language.toUpperCase(Locale.US));
		if(l !=null) {
			 return l;
		}
		
		return LANGUAGE.ENGLISH;
		
		
	}
	public LOCATION findLocationPrferenceValue() {
		String location =  PreferenceManager.getDefaultSharedPreferences(context).getString(KEY.LOCATION.toString(), LOCATION.KARACHI.toString());
		LOCATION l = LOCATION.valueOf(location.toUpperCase());
		if(l !=null) {
			return l;
		}

		return LOCATION.KARACHI;

	}
}
