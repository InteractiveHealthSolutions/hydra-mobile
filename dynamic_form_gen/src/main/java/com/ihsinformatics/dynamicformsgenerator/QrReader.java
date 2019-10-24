package com.ihsinformatics.dynamicformsgenerator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class QrReader extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qrreader);

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setBackCamera();


    }


    public void onQRCodeRead(String text, PointF[] points) {

        Intent intent=new Intent();
        intent.putExtra("SCAN_RESULT",text);
        setResult(0,intent);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
}
