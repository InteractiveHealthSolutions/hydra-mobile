package com.ihsinformatics.dynamicformsgenerator.wrapper;

import android.content.Context;
import android.widget.Toast;
import es.dmoral.toasty.Toasty;

public class ToastyWidget {

    private static ToastyWidget instance = new ToastyWidget();

    private ToastyWidget(){}

    public static ToastyWidget getInstance(){
        return instance;
    }

    public void displayError(Context context, String message, int duration)
    {
        Toasty.error(context, message, duration).show();
    }

    public void displayWarning(Context context, String message, int duration)
    {
        Toasty.warning(context, message, duration).show();
    }

    public void displaySuccess(Context context, String message, int duration)
    {
        Toasty.success(context, message, duration).show();
    }



}
