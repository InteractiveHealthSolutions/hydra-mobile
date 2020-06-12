package com.ihsinformatics.dynamicformsgenerator.screens;

import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import com.ihsinformatics.dynamicformsgenerator.R;

/**
 * Created by Naveed Iqbal on 9/22/2017.
 * Email: h.naveediqbal@gmail.com
 */

public class ToolbarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    protected void addToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /*@Override
    public void setTitle(CharSequence title) {
        toolbar.setLogoDescription(title);
    }*/
}
