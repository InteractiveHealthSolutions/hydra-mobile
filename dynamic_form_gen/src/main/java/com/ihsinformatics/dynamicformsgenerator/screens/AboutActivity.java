package com.ihsinformatics.dynamicformsgenerator.screens;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.ihsinformatics.dynamicformsgenerator.R;

public class AboutActivity extends ToolbarActivity {
    private TextView tvVersionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        addToolbar();
        tvVersionNumber = (TextView) findViewById(R.id.tvVresionNumber);
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersionNumber.setText("Version: " + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e1) {
            tvVersionNumber.setText("");
            e1.printStackTrace();
        }
    }
}
