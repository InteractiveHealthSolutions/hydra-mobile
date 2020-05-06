package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Build;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.Result;
import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.ihsinformatics.dynamicformsgenerator.Utils.showMessageOKCancel;

/**
 * Created by Owais on 11/2/2017.
 */
public class QRReaderWidget extends InputWidget implements View.OnClickListener ,ZXingScannerView.ResultHandler  {
    protected EditText etAnswer;
    protected LinearLayout QRCodeReader;
    protected Dialog dialog;
    protected QuestionConfiguration configuration;

    private ZXingScannerView mScannerView;
    protected Dialog dialogBarCodeAndQRCode;

    public QRReaderWidget(Context context, Question question, int layoutId) {
        super(context, question,layoutId);
        if(super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        QRCodeReader = (LinearLayout) findViewById(R.id.qrCodeReader);
        QRCodeReader.setOnClickListener(this);

        mScannerView = new ZXingScannerView(context);   // Programmatically initialize the scanner view
        dialogBarCodeAndQRCode = new Dialog(getContext());
        dialogBarCodeAndQRCode.setContentView(mScannerView);

        // if(options == null) options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        if (options.size() > 0) {
            setOptionsOrHint(options.get(0));
            etAnswer.setText(options.get(0).getUuid());
        }
        InputFilter[] filterArray = new InputFilter[2];
        filterArray[0] = new InputFilter.LengthFilter(configuration.getMaxLength());
        filterArray[1] = DigitsKeyListener.getInstance(configuration.getAllowedCharacters());
        etAnswer.setInputType(configuration.getInputType());
        etAnswer.setFilters(filterArray);
    }

    @Override
    public boolean isValidInput(boolean isMendatory) {

        Validation validation = Validation.getInstance();
        if (configuration.getMinLenght() >= 0) {
            if (etAnswer.getText().toString().length() == 0 && !isMendatory) {
            }
//            else if (etAnswer.getText().toString().length() < configuration.getMinLenght()) {
//                return false;
//            }
        }
        // TODO IDENTIFIER needs to PASS indus_mrno as string in validationFunction. Right now null is passed ~Taha
        return validation.validate(etAnswer, question.getValidationFunction(), isMendatory);
    }

    @Override
    public void setOptionsOrHint(Option... data) {
        if (data.length > 0) {
            etAnswer.setHint(data[0].getText());
        }
    }

    @Override
    public void onFocusGained() {
        etAnswer.requestFocus();
    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
        etAnswer.setText(answer);
    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();



        if (isValidInput(question.isMandatory())) {
            if (etAnswer.getText().toString().length() == 0 && !question.isMandatory()) {
                param.put(question.getParamName(), null);
            } else {
                param.put(question.getParamName(), etAnswer.getText().toString());
            }
            dismissMessage();
            param.put(question.getParamName(), etAnswer.getText().toString());
        } else {
            activity.addValidationError(getQuestionId(), "Invalid input");   //No need to show custom error message on QRWidget  ~Taha
        }
        //Necessary for every widget to have PAYLOAD_TYPE AND PERSON_ATTRIBUTE
        param.put(ParamNames.PAYLOAD_TYPE, question.getPayload_type().toString());
        if(question.getAttribute())
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_TRUE);
        else
            param.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_FALSE);


        return param;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.qrCodeReader) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkCameraPermission();
            } else {
                //showQRCodeReaderDialog();
                showQRAndBarCodeReaderDialog();
            }
        }
    }

    protected void checkCameraPermission() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) QRReaderWidget.this.context,
                    Manifest.permission.CAMERA)) {
                showMessageOKCancel(context, getContext().getString(R.string.qr_code_permission_request_message),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) QRReaderWidget.this.context,
                                        new String[]{Manifest.permission.CAMERA},
                                        getQuestionId());
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions((Activity) QRReaderWidget.this.context,
                    new String[]{Manifest.permission.CAMERA},
                    getQuestionId());
            return;
        }
        //showQRCodeReaderDialog();
        showQRAndBarCodeReaderDialog();
    }

    public void showQRCodeReaderDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_qrcode);
        dialog.show();
        final QRCodeReaderView qrCodeReaderView;
        qrCodeReaderView = (QRCodeReaderView) dialog.findViewById(R.id.qrdecoderview);
        qrCodeReaderView.startCamera();
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setTorchEnabled(true);
        qrCodeReaderView.setFrontCamera();
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void onQRCodeRead(String text, PointF[] points) {
                etAnswer.setText(text);
                etAnswer.setEnabled(true);
                qrCodeReaderView.stopCamera();
                dialog.dismiss();
            }
        });
    }

    public void showQRAndBarCodeReaderDialog()
    {
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
        dialogBarCodeAndQRCode.show();
    }

    public String getValue() {
        return etAnswer.getText().toString();
    }

    @Override
    public void destroy() {
    }

    @Override
    public String getServiceHistoryValue() {
        return getValue();
    }

    @Override
    public void handleResult(Result result) {
        etAnswer.setText(result.getText());
        etAnswer.setEnabled(true);
        mScannerView.stopCamera();
        dialogBarCodeAndQRCode.dismiss();
    }
}
