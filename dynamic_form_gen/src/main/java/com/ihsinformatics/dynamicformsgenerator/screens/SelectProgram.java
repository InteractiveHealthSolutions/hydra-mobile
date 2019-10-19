package com.ihsinformatics.dynamicformsgenerator.screens;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;

public class SelectProgram extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup rgProgram;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_program);

        rgProgram = (RadioGroup) findViewById(R.id.rgOptions);
        btnContinue = (Button) findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(this);
        setFinishOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        if(rgProgram.getCheckedRadioButtonId() == -1)
            return;

        String selectedProgram = ((RadioButton)findViewById(rgProgram.getCheckedRadioButtonId())).getText().toString();
        GlobalPreferences.getinstance(this).addOrUpdatePreference(GlobalPreferences.KEY.PROGRAM, selectedProgram);

        finish();
    }
}
