package com.ihsinformatics.dynamicformsgenerator.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.ihsinformatics.dynamicformsgenerator.R;


public class MyDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tb_confirmation_popup_screen, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final RadioGroup tb_diagnosed = view.findViewById(R.id.tb_diagnosed);

        Button btnDone = view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int selectedId = tb_diagnosed.getCheckedRadioButtonId();

                if (selectedId != -1) {
                    RadioButton selectedRadioId = MyDialogFragment.this.getView().findViewById(selectedId);


                    if (selectedRadioId != null) {
                        DialogListener dialogListener = (DialogListener) getActivity();
                        dialogListener.onFinishEditDialog(selectedRadioId.getText().toString());
                        dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Please Select Something", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public interface DialogListener {
        void onFinishEditDialog(String inputText);
    }


}