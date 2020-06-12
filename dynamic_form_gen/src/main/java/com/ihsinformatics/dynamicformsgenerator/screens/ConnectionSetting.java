package com.ihsinformatics.dynamicformsgenerator.screens;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ihsinformatics.dynamicformsgenerator.R;

public class ConnectionSetting extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.connection_setting);
     /*   getListView().setBackgroundColor(Color.TRANSPARENT);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        getListView().setBackgroundColor(Color.rgb(253, 253, 253));*/
    }

    
}
