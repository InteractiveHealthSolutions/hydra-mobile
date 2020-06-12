package com.ihsinformatics.dynamicformsgenerator.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.ihsinformatics.dynamicformsgenerator.R;
import org.json.JSONException;


public class MyDialogFragment extends DialogFragment {

    String question, type;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getArguments() != null) {
            this.question = getArguments().getString("question");
            this.type = getArguments().getString("type");


        }

        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tb_confirmation_popup_screen, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);

        TextView tvQuestion = view.findViewById(R.id.tvTitle);
        tvQuestion.setText(this.question);
        return view;

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
                        try {
                            dialogListener.onFinishEditDialog(selectedRadioId.getText().toString(), type);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        void onFinishEditDialog(String inputText, String type) throws JSONException;
    }


}