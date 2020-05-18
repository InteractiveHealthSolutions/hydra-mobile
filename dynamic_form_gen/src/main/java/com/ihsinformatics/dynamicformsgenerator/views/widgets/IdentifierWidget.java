package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient;
import com.ihsinformatics.dynamicformsgenerator.network.DataSender;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.network.Sendable;
import com.ihsinformatics.dynamicformsgenerator.utils.AES256Endec;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.SecretKey;

import es.dmoral.toasty.Toasty;

/**
 * Created by Owais on 11/2/2017.
 */
public class IdentifierWidget extends QRReaderWidget implements View.OnFocusChangeListener, Sendable, TextWatcher {
    private static final int REQUEST_GET_PROCEDURES = 0;
    private boolean suspiciousIdentifier = true;


    public IdentifierWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);

        // etAnswer.setOnFocusChangeListener(this);
        etAnswer.addTextChangedListener(this);
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {

        }
    }

    private void checkID() throws Exception {
        JSONObject obj = new JSONObject().put(ParamNames.PATIENT_IDENTIFIER, getValue());
        // requestType = REQUEST_TYPE == REQUEST_TYPE.FETCH_INFO ? ParamNames.GET_PATIENT_INFO : ParamNames.GET_PATIENT_IMAGES_INFO;
        obj.put(ParamNames.REQUEST_TYPE, ParamNames.GET_PATIENT_INFO);
        SecretKey secKey = AES256Endec.getInstance().generateKey();
        String encPassword = AES256Endec.getInstance().encrypt(Global.PASSWORD, secKey);
        obj.put(ParamNames.USERNAME, Global.USERNAME);
//						data.put(new JSONObject().put(ParamNames.PASSWORD, Global.PASSWORD));
        obj.put(ParamNames.PASSWORD, encPassword);

        send(obj, 0);
    }

    @Override
    public void send(JSONObject data, int respId) {
        new DataSender(context, this, 0).execute(data);
    }

    @Override
    public void onResponseReceived(JSONObject resp, int respId) {
        if (resp == null) return;
        if (resp.has(ParamNames.PATIENT)) {
            suspiciousIdentifier = true;
            Toasty.error(context, context.getResources().getString(R.string.error_duplicate_identifier), Toast.LENGTH_LONG).show();
            setMessage(context.getResources().getString(R.string.error_duplicate_identifier), MessageType.MESSAGE_TYPE_ERROR);
        } else if (resp.has(ParamNames.SERVER_RESULT)) {
            suspiciousIdentifier = false;
        } else if (resp.has(ParamNames.SERVER_ERROR)) {
            /*suspiciousIdentifier = true;
            Toasty.error(context, context.getResources().getString(R.string.error_no_internet_form_can_not_save), Toast.LENGTH_LONG).show();*/
        }

    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();


        if (isValidInput(question.isMandatory()) && (!suspiciousIdentifier || !etAnswer.isEnabled())) {
            if (question.getParamName() == null) return null;
            if (etAnswer.getText().toString().length() == 0 && !question.isMandatory()) {
                param.put(ParamNames.PARAM_NAME, question.getParamName());
                param.put(ParamNames.VALUE, "");
            } else {
                param.put(ParamNames.PARAM_NAME, question.getParamName());
                param.put(ParamNames.VALUE, etAnswer.getText().toString());

            }
            dismissMessage();
            param.put(ParamNames.PARAM_NAME, question.getParamName());
            param.put(ParamNames.VALUE, etAnswer.getText().toString());
        } else {
            activity.addValidationError(getQuestionId(), context.getString(R.string.identifier_error));   //No need to show custom error message on identifier  ~Taha
        }
        //Necessary for every widget to have PAYLOAD_TYPE AND PERSON_ATTRIBUTE
        param.put(ParamNames.PAYLOAD_TYPE, Question.PAYLOAD_TYPE.IDENTIFIER);

        if (question.getAttribute())
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_TRUE);
        else
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_FALSE);


        return param;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        if (!etAnswer.isEnabled()) return true;
        return super.isValidInput(isMendatory);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // String text = editable.toString();
        if (isValidInput(question.isMandatory())) {
            suspiciousIdentifier = false;
            setMessage("Valid format", MessageType.MESSAGE_TYPE_CORRECT);
            try {
                // checkID();
                if (!(isEditMode && beforeEditIdentifer.equals(getValue()))) {
                    checkOfflineID();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            suspiciousIdentifier = true;
            setMessage("Invalid input", MessageType.MESSAGE_TYPE_ERROR);
        }
    }

    private void checkOfflineID() {

        OfflinePatient offlinePatient = DataAccess.getInstance().getPatientByMRNumber(context, getValue());

        if (offlinePatient == null) {
            suspiciousIdentifier = false;
        } else {
            suspiciousIdentifier = true;
            Toasty.error(context, context.getResources().getString(R.string.error_duplicate_identifier), Toast.LENGTH_LONG).show();
            setMessage(context.getResources().getString(R.string.error_duplicate_identifier), MessageType.MESSAGE_TYPE_ERROR);
        }

    }

    @Override
    public void setEnabled(boolean enabled) {
        etAnswer.setEnabled(false);
        QRCodeReader.setClickable(false);
    }

    @Override
    public String getValue() {
        return etAnswer.getText().toString();
    }

    @Override
    public String getServiceHistoryValue() {
        return getValue();
    }
}
