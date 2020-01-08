package com.ihsinformatics.dynamicformsgenerator.screens;

import android.view.View;

import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SExpression;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SkipLogics;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.EditTextWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 22)
public class BaseActivityTest {

    BaseActivity baseActivity;

    private List<Option> options;
    private List<Question> questions;


    @Mock
    InputWidget inputWidget;

    @Mock
    SExpression expression;

    @Before
    public void setUp() throws Exception {
        baseActivity = Robolectric.buildActivity(BaseActivity.class).create().start().resume().get();
        initMocks(this);
    }

    @Test
    public void shouldAddOnRuntimeWidgetsTest() {
        //init9
        int expectedSize = 1;

        //method calling
        baseActivity.addRunTimeWidgetReference(1, inputWidget);

        //verification
        assertEquals(expectedSize, baseActivity.runtimeGeneratedWidgets.size());
    }


    @Test
    public void shouldRemoveRuntimeWidgetReference()
    {
        int expectedSize=0;
        int id=1;
        baseActivity.addRunTimeWidgetReference(1,inputWidget);


        baseActivity.removeRuntimeWidgetReference(id);

        assertEquals(expectedSize, baseActivity.runtimeGeneratedWidgets.size());

    }


    private void initAesiForm() {
        Integer AesiForm = 2;
        questions.add(new Question(false, AesiForm, 7999, "-1", InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING, View.VISIBLE, null, "Patient Registration Form", null, null));
        this.questions.add(new Question(true, AesiForm, 7000, "", InputWidget.InputWidgetsType.WIDGETS_TYPE_IDENTIFIER, View.VISIBLE, Validation.CHECK_FOR_MRNO, "Identifier", ParamNames.PROJECT_IDENTIFIER, null));
        this.questions.add(new Question(true, AesiForm, 7001, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Patient's name", "First_name", null));
        this.questions.add(new Question(true, AesiForm, 7002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", "Last_Name", null));
        this.questions.add(new Question(true, AesiForm, 7003, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Gender", "SEX", null));
        this.options.add(new Option(7003, 604, null, null, "123", "Male", -1));
        this.options.add(new Option(7003, 605, null, null, "456", "Female", -1));

        this.questions.add(new Question(true, AesiForm, 6004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Location", "location2", null));
        this.options.add(new Option(7004, 604, null, null, "111", "ABC", -1));
        this.options.add(new Option(7004, 605, null, null, "222", "DEF", -1));

        this.questions.add(new Question(true, AesiForm, 6005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Aesi Question", "location3", null));
        this.options.add(new Option(7005, 604, null, null, "111", "XYZ", -1));
        this.options.add(new Option(7005, 605, null, null, "222", "ZYX", -1));


        this.questions.add(new Question(true, AesiForm, 6006, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Aesi 2 Question 2", "location4", null));
        this.options.add(new Option(7006, 604, null, null, "111", "AAA", -1));
        this.options.add(new Option(7006, 605, null, null, "222", "BBB", -1));

        this.questions.add(new Question(true, AesiForm, 6007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Aesi 3 Question 3", "location5", null));
        this.options.add(new Option(7007, 604, null, null, "111", "CCC", -1));
        this.options.add(new Option(7007, 605, null, null, "222", "DDD", -1));

        this.questions.add(new Question(true, AesiForm, 6008, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Aesi 4 Question 4", "location6", null));
        this.options.add(new Option(7008, 604, null, null, "111", "EEE", -1));
        this.options.add(new Option(7008, 605, null, null, "222", "FFF", -1));


        //   this.questions.add(new Question(true, patientCreationId, 6004, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Umar (Age in years)", "age", numeric3DigitMin1));
        Question q=new Question(true, AesiForm, 7100, "", InputWidget.InputWidgetsType.WIDGET_TYPE_AGE, View.VISIBLE, Validation.CHECK_FOR_DATE, "Date of Birth", ParamNames.DOB, null);

        //  this.questions.add(new Question(true, patientCreationId, 6005, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Ghar ka patta - Ghar/Street #", "address1", alpha150DigitSpace));

        ArrayList<String> arr=new ArrayList<>();
        arr.add("Female");
        ArrayList<String> arr3=new ArrayList<>();
        arr3.add("ABC");
        ArrayList<String> arr5=new ArrayList<>();
        arr5.add("XYZ");

        ArrayList<String> arr2=new ArrayList<>();
        arr2.add("AAA");
        ArrayList<String> arr4=new ArrayList<>();
        arr4.add("CCC");
        ArrayList<String> arr6=new ArrayList<>();
        arr6.add("EEE");

        ArrayList<String> emptyArr=new ArrayList<>();


        List<SExpression> sExpList1=new ArrayList<>();

        List<SExpression> sExpListNested=new ArrayList<>();

        ArrayList<SkipLogics> nestedSkipLogics =new ArrayList<>();
        nestedSkipLogics.add(new SkipLogics("1",7006,arr2,emptyArr,null,null,null,null));
        nestedSkipLogics.add(new SkipLogics("2",7007,emptyArr,arr4,null,null,null,null));
        nestedSkipLogics.add(new SkipLogics("3",7008,arr6,emptyArr,null,null,null,null));
        sExpListNested.add(new SExpression("OR",nestedSkipLogics,null));

        ArrayList<SkipLogics> s =new ArrayList<>();
        s.add(new SkipLogics("1",7003,emptyArr,arr,null,null,null,null));
        s.add(new SkipLogics("2",7004,arr3,emptyArr,null,null,null,null));
        s.add(new SkipLogics("3",7005,arr5,emptyArr,null,null,null,null));
        sExpList1.add(new SExpression("AND",s,sExpListNested));
        q.setVisibleWhen(sExpList1);



        this.questions.add(q);

      //  this.questions.add(new Question(true, AesiForm, 7007, "", InputWidget.InputWidgetsType.WIDGET_TYPE_ADDRESS, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Address", ParamNames.ADDRESS, addressConfiguration));



    }




    @Test
    public void shouldTestSkipLogic() throws JSONException {



      //  SExpression exp=new SExpression("OR", List<SkipLogics> skipLogicsObjects, List<SExpression> skipLogicsArray)
        //init
        when(expression.getOperator()).thenReturn("OR");


        //method calling
        baseActivity.logicChecker(expression);

    }
}