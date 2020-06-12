package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;

import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;

/**
 * Created by Owais on 11/7/2017.
 */
public class SingleSelectTextViewWidget extends SingleSelectEditTextWidget {
    public SingleSelectTextViewWidget(Context context, Question question, int layoutId) {
        super(context, question,layoutId);
        etAnswer.setEnabled(false);
    }
}
