package com.ihsinformatics.dynamicformsgenerator.data.core.options;

import com.ihsinformatics.dynamicformsgenerator.data.utils.SkipDateRange;

/**
 * Created by Naveed Iqbal on 3/4/2017.
 * Email: h.naveediqbal@gmail.com
 */

public class DateOption extends Option {

    private SkipDateRange skipDateRange;

    public DateOption(int questionId, int optionId, int[] opensQuestions, int[] hidesQuestions, String uuid, String text, int score, SkipDateRange skipDateRange) {
        super(questionId, optionId, opensQuestions, hidesQuestions, uuid, text, score);
        this.skipDateRange = skipDateRange;
    }

    public SkipDateRange getSkipDateRange() {
        return skipDateRange;
    }

    public void setSkipDateRange(SkipDateRange skipDateRange) {
        this.skipDateRange = skipDateRange;
    }
}
