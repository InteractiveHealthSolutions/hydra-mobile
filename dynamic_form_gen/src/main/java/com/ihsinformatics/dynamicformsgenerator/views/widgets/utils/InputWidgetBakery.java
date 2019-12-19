package com.ihsinformatics.dynamicformsgenerator.views.widgets.utils;

import android.content.Context;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.AddressWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.AutoCompleteTextViewWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.DateWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.EditTextWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.GPSWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.HeadingWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.HiddenFieldWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.IdentifierWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.ImageWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.MultiSelectSpinnerWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.QRReaderWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.RadioButtonWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.ScoreSpinner;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.SingleSelectEditTextWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.SingleSelectTextViewWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.SpinnerWidget;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.AgeWidget;

import org.json.JSONException;

/**
 * Created by Naveed Iqbal on 11/24/2017.
 * Email: h.naveediqbal@gmail.com
 */
public class InputWidgetBakery {
    public InputWidget bakeInputWidget(Context context, Question q) throws JSONException {
        InputWidget inputWidget = null;
        if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT) {
            inputWidget = new EditTextWidget(context, q, R.layout.layout_widget_edit_text);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER) {
            inputWidget = new SpinnerWidget(context, q, R.layout.layout_widget_spinner);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_SCORE_SPINNER) {
            inputWidget = new ScoreSpinner(context, q, R.layout.layout_widget_spinner);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_MULTI_SELECT_SPINNER) {
            inputWidget = new MultiSelectSpinnerWidget(context, q, R.layout.layout_widget_multi_select_spinner);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_DATE) {
            inputWidget = new DateWidget(context, q, R.layout.layout_widget_edit_text);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_HEADING) {
            inputWidget = new HeadingWidget(context, q, R.layout.layout_widget_heading);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_RADIO_BUTTON) {
            inputWidget = new RadioButtonWidget(context, q, R.layout.layout_widget_radio_group);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_GPS) {
            inputWidget = new GPSWidget(context, q, R.layout.layout_widget_gps);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_HIDDEN_INPUT) {
            inputWidget = new HiddenFieldWidget(context, q, R.layout.layout_widget_edit_text);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_EDITTEXT) {
            inputWidget = new SingleSelectEditTextWidget(context, q, R.layout.layout_widget_single_select_edit_text);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGETS_TYPE_SINGLE_SELECT_TEXTVIEW) {
            inputWidget = new SingleSelectTextViewWidget(context, q, R.layout.layout_widget_single_select_edit_text);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_AGE) {
            inputWidget = new AgeWidget(context, q, R.layout.layout_widget_age);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_IMAGE) {
            inputWidget = new ImageWidget(context, q, R.layout.layout_widget_image);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGETS_TYPE_IDENTIFIER) {
            inputWidget = new IdentifierWidget(context, q, R.layout.layout_widget_identifier);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGETS_TYPE_QR_READER) {
            inputWidget = new QRReaderWidget(context, q, R.layout.layout_widget_qrcode_reader);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_AUTOCOMPLETE_EDITTEXT) {
            inputWidget = new AutoCompleteTextViewWidget(context, q, R.layout.layout_widget_autocomplete_edit_text);
        } else if (q.getQuestionType() == InputWidget.InputWidgetsType.WIDGET_TYPE_ADDRESS) {
            inputWidget = new AddressWidget(context, q, R.layout.layout_widget_address);
        }
        return inputWidget;
    }
}
