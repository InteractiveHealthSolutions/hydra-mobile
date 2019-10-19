package com.ihsinformatics.dynamicformsgenerator;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;

/**
 * Created by Owais on 12/20/2017.
 */
public class LocationSelector extends AppCompatActivity {
    public static enum LOCATION {
        KARACHI,
        MUZAFFARGARH,
        MANAWAN,
        BHONG,
        BADIN,
        OTHER
    }

    RadioGroup rgLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selector);
        rgLocation = (RadioGroup) findViewById(R.id.rgLocation);
        LOCATION location = GlobalPreferences.getinstance(this).findLocationPrferenceValue();
        switch (location) {
            case KARACHI:
                ((RadioButton)findViewById(R.id.rbKarachi)).setChecked(true);
                break;
            case MUZAFFARGARH:
                ((RadioButton)findViewById(R.id.rbMuzaffargarh)).setChecked(true);
                break;
            case MANAWAN:
                ((RadioButton)findViewById(R.id.rbManawan)).setChecked(true);
                break;
            case BHONG:
                ((RadioButton)findViewById(R.id.rbBhong)).setChecked(true);
                break;
            case BADIN:
                ((RadioButton)findViewById(R.id.rbBadin)).setChecked(true);
                break;
            case OTHER:
                ((RadioButton)findViewById(R.id.rbOther)).setChecked(true);
                break;
            default:
                break;
        }
        rgLocation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                String location = ((RadioButton)findViewById(arg1)).getText().toString();
                GlobalPreferences.getinstance(LocationSelector.this).addOrUpdatePreference(GlobalPreferences.KEY.LOCATION, location.toUpperCase());
                DataProvider.reInitializeInstance(LocationSelector.this);
            }
        });
    }


}

