package com.ihsinformatics.dynamicformsgenerator.wrapper;

import android.content.Context;
import android.widget.Toast;

import androidx.test.core.app.ApplicationProvider;

import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.screens.Form;
import com.ihsinformatics.dynamicformsgenerator.screens.FormDataDisplayActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.*;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 22)
public class ToastyWidgetTest {

    private Context context;
    private FormDataDisplayActivity activity;


    @Before
    public void setUp() throws Exception {
         activity = Robolectric.buildActivity(FormDataDisplayActivity.class).create().start().resume().get();
        //context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void shouldDisplayError() {
        //1 initialization
        String expectedMessage = "this is error message";

        //method calling
        ToastyWidget.getInstance().displayError(activity,expectedMessage, Toast.LENGTH_SHORT);

        //asserting /verification
        assertEquals(expectedMessage, ShadowToast.getTextOfLatestToast());
    }


}