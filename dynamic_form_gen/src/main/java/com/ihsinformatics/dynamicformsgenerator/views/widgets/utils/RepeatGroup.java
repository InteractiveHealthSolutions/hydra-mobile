package com.ihsinformatics.dynamicformsgenerator.views.widgets.utils;

import android.content.Context;
import android.widget.LinearLayout;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Naveed Iqbal on 11/24/2017.
 * Email: h.naveediqbal@gmail.com
 */

public class RepeatGroup extends LinearLayout implements Observer {


    public RepeatGroup(Context context, Double value, int... questionIds) {
        super(context);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
