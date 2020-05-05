package ihsinformatics.com.hydra_mobile.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Locale;


public class GlobalPreferences {
	// TODO make this this class generic
	public static enum KEY {
		USERUUID,
		USERNAME,
		PASSWORD,
		PROVIDER,
		LANGUAGE,
		LOCATION,
		FIRST_RUN,
		WORKFLOW,
		WORKFLOWUUID,
		CURRENT_PHASE_UUID,
		ACTIVE_TIME,
		APP_LANGUAGE
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


	public Translator.LANGUAGE findLanguagePrferenceValue() {
		String language =  PreferenceManager.getDefaultSharedPreferences(context).getString(KEY.LANGUAGE.toString(), Translator.LANGUAGE.ENGLISH.toString());
		Translator.LANGUAGE l = Translator.LANGUAGE.valueOf(language.toUpperCase(Locale.US));
		if(l !=null) {
			 return l;
		}

		return Translator.LANGUAGE.ENGLISH;


	}
/*	public LOCATION findLocationPrferenceValue() {
		String location =  PreferenceManager.getDefaultSharedPreferences(context).getString(KEY.LOCATION.toString(), LOCATION.KARACHI.toString());
		LOCATION l = LOCATION.valueOf(location.toUpperCase());
		if(l !=null) {
			return l;
		}

		return LOCATION.KARACHI;

	}*/
}
