package ihsinformatics.com.hydra_mobile.ui.widgets;

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
import ihsinformatics.com.hydra_mobile.R;
import ihsinformatics.com.hydra_mobile.data.core.options.Option;
import ihsinformatics.com.hydra_mobile.data.core.question.Question;
import ihsinformatics.com.hydra_mobile.data.core.question.config.QuestionConfiguration;
import ihsinformatics.com.hydra_mobile.utils.AppUtility;
import ihsinformatics.com.hydra_mobile.utils.Translator;
import ihsinformatics.com.hydra_mobile.utils.Validation;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Owais on 11/2/2017.
 */
public class QRReaderWidget extends InputWidget implements View.OnClickListener {
    private EditText etAnswer;
    private LinearLayout QRCodeReader;
    Dialog dialog;
    private QuestionConfiguration configuration;

    public QRReaderWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        QRCodeReader = (LinearLayout) findViewById(R.id.qrCodeReader);
        QRCodeReader.setOnClickListener(this);
        options = null;//DataProvider.getInstance(context).getOptions(question.getQuestionId());
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
            } else if (etAnswer.getText().toString().length() < configuration.getMinLenght()) {
                return false;
            }
        }
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
            //activity.addValidationError(getQuestionId(), "Invalid input");
        }
        return param;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.qrCodeReader) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkCameraPermission();
            } else {
                showQRCodeReaderDialog();
            }
        }
    }

    private void checkCameraPermission() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) QRReaderWidget.this.context,
                    Manifest.permission.CAMERA)) {
                AppUtility.Companion.showMessageOKCancel(context, "For Scanning QR Code , You need to provide permission to access your camera",
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
        showQRCodeReaderDialog();
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
                etAnswer.setEnabled(false);
                qrCodeReaderView.stopCamera();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void destroy() {
    }
}
