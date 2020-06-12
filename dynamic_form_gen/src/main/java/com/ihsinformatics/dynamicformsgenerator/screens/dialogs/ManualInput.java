package com.ihsinformatics.dynamicformsgenerator.screens.dialogs;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ihsinformatics.dynamicformsgenerator.R;

public class ManualInput extends AppCompatActivity implements OnClickListener {

	private EditText etValue;
	private Button btnCancel, btnOK;
	public final static String VALUE = "value";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_input);
		setTitle("Othre value");
		etValue = (EditText) findViewById(R.id.etValue);
		// etValue.setText("101130800001-3");

		btnOK = (Button) findViewById(R.id.btnOK);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnOK) {
			Intent sendBack = new Intent();
			sendBack.putExtra(VALUE, etValue.getText().toString());
			setResult(RESULT_OK, sendBack);
			finish();
				
		} else if (v == btnCancel) {
			setResult(RESULT_CANCELED);
			finish();
		}
		
		

	}

}
