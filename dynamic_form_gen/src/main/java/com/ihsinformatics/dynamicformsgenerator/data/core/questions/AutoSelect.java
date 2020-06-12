package com.ihsinformatics.dynamicformsgenerator.data.core.questions;

import java.util.List;

public class AutoSelect {

    private String targetFieldAnswer;
    private List<SExpression> autoSelectWhen;

    public AutoSelect(String targetFieldAnswer, List<SExpression> autoSelectWhen) {
        this.targetFieldAnswer = targetFieldAnswer;
        this.autoSelectWhen = autoSelectWhen;
    }

    public AutoSelect() {
    }

    public String getTargetFieldAnswer() {
        return targetFieldAnswer;
    }

    public void setTargetFieldAnswer(String targetFieldAnswer) {
        this.targetFieldAnswer = targetFieldAnswer;
    }

    public List<SExpression> getAutoSelectWhen() {
        return autoSelectWhen;
    }

    public void setAutoSelectWhen(List<SExpression> autoSelectWhen) {
        this.autoSelectWhen = autoSelectWhen;
    }
}
