package com.ihsinformatics.dynamicformsgenerator;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences.KEY;

public class LanguageSelector extends AppCompatActivity {

	
	RadioGroup rgLanguage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language_selector);
		rgLanguage = (RadioGroup) findViewById(R.id.rgLanguage);
		LANGUAGE lang = GlobalPreferences.getinstance(this).findLanguagePrferenceValue();
		switch (lang) {
		case URDU:
			((RadioButton)findViewById(R.id.rbUrdu)).setChecked(true);
			break;
		case ENGLISH:
			((RadioButton)findViewById(R.id.rbEnglish)).setChecked(true);
			break;
		default:
			break;
		}
		rgLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {				
				String language = ((RadioButton)findViewById(arg1)).getText().toString();
				GlobalPreferences.getinstance(LanguageSelector.this).addOrUpdatePreference(KEY.LANGUAGE, language.toUpperCase());
			}
		});
	}

	
}
