package com.ihsinformatics.dynamicformsgenerator.views.widgets.utils;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.screens.FormDataDisplayActivity;
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
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import static com.ihsinformatics.dynamicformsgenerator.network.ParamNames.LAST_NAME;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 22)
public class InputWidgetBakeryTest {

    InputWidgetBakery inp=new InputWidgetBakery();
    private Context context;
    Object shadowContext;

    @Mock
    Question q;

    @Mock
    QuestionConfiguration qc;

    private BaseActivity baseActivity;

    @Before
    public void setUp() throws Exception {
        //activity = Robolectric.buildActivity(FormDataDisplayActivity.class).create().start().resume().get();
        context = RuntimeEnvironment.application.getApplicationContext();
        baseActivity = Robolectric.buildActivity(BaseActivity.class).create().start().resume().get();
        initMocks(this);
    }

    @Test
    public void shouldBakeEditText() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }

    @Test
    public void shouldBakeSpinnerWidget() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }

    @Test
    public void shouldBakeScoreSpinnerWidget() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGET_TYPE_SCORE_SPINNER);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }

    @Test
    public void shouldBakeMultiSelectSpinnerWidget() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }

    @Test
    public void shouldBakeDateWidget() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGET_TYPE_DATE);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }

    @Test
    public void shouldBakeHeadingWidget() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }

    @Test
    public void shouldBakeRadioButtonWidget() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }

    @Test
    public void shouldBakeGPSWidget() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGET_TYPE_GPS);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }

    @Test
    public void shouldBakeHiddenFieldWidget() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGET_TYPE_HIDDEN_INPUT);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }

    @Test
    public void shouldBakeSingleSelectEditTextWidget() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }

    @Test
    public void shouldBakeSingleSelectTextViewWidget() throws JSONException {

        //init
        //Question q=new Question(true, 1, 6002, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Father's or Husband's name", LAST_NAME, null);
        //InputWidget exp= new EditTextWidget(baseActivity, q, R.layout.layout_widget_edit_text);
        when(q.getQuestionType()).thenReturn(InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW);
        when(q.getQuestionConfiguration()).thenReturn(qc);

        //method call

        InputWidget output=inp.bakeInputWidget(baseActivity, q);

        //verify
        assertEquals(output.getInputWidgetsType(),q.getQuestionType());

    }
}